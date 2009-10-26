/**
 * 
 */
package au.org.emii.aatams.file.processing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
//import java.io.PrintStream;
import java.lang.StringBuffer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import javax.mail.Message.RecipientType;
import javax.activation.FileDataSource;
import org.codemonkey.vesijama.Email;
import org.codemonkey.vesijama.MailException;
import org.codemonkey.vesijama.Mailer;

// import org.apache.log4j.xml.DOMConfigurator;
// import org.apache.log4j.Level;

// TODO add deployment_download_id to detection table to associate detections
// with a specific file.
// this allows a final cross-check of detections in database after commit with
// those in file.
/**
 * @author Stephen Cameron
 * 
 */
public final class AcousticReceiverFileProcessor {

	public String reportFolder = "c:/temp";

	private static final String timestampFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	private static final SimpleDateFormat timestampParser = new SimpleDateFormat(timestampFormat);
	private static final String pattern = "\"([^\"]+?)\",?|([^,]+),?|,";
	private static final Pattern csvRegex = Pattern.compile(pattern);
	private static final String DOWNLOAD_ID_PREFIX = "aatams.deployment_download.";
	private static final String PROCESSOR_FROM = "eMII";
	private static final String PROCESSOR_ADDRESS = "stephen.cameron@utas.edu.au";
	private static final String SMTP_HOST = "postoffice.utas.edu.au";
	private static final int SMTP_PORT = 25;
	private static final String SMTP_USER = "sc04";
	private static final String SMTP_PWD = "66CoStHo";
	private static final String AATAMS_WEB_SITE_URI = "http://test.emii.org.au/aatams";
	private Logger logger = Logger.getLogger(this.getClass());
	private Connection conn = null;
	private TreeMap<String, Tag> tags = new TreeMap<String, Tag>();
	private TreeMap<String, Tag> newTags = new TreeMap<String, Tag>();
	private ArrayList<Detection> detections = new ArrayList<Detection>();
	private String receiverName = null;
	private int projectRolePersonId = 0;
	private int receiverDeploymentId = 0;
	private int deploymentDownloadId;
	private File file;

	public void doProcessing(String downloadFid, String filePath, String jdbcDriverClassName, String connectionString, String username, String password,
			ArrayList<String> messages) {
		boolean success = false;
		// if not passed set outStream to System.out;
		if (messages == null) {
			messages = new ArrayList<String>();
		}

		try {
			//Get db connection.
			Class.forName(jdbcDriverClassName);
			conn = DriverManager.getConnection(connectionString, username, password);
			conn.setAutoCommit(false); //want to rollback if error!
			// Print all warnings
			for (SQLWarning warn = conn.getWarnings(); warn != null; warn = warn.getNextWarning()) {
				logger.warn("SQL Warning:");
				logger.warn("State  : " + warn.getSQLState());
				logger.warn("Message: " + warn.getMessage());
				logger.warn("Error  : " + warn.getErrorCode());
			}
			// Load the file
			this.file = new File(filePath);
			if (!file.exists()) {
				throw new Exception("invalid file path: " + filePath);
			}
			if (!file.canRead()) {
				throw new Exception("file at " + filePath + " cannot be opened for reading");
			}
			// 1. Parse the file and get a unique list of tags - there are
			// usually lots of duplicates
			// Also check that there is only one receiver in the file.
			try {
				// open file for reading
				messages.add("Processing file: " + file.getName());
				logger.info("reading file: " + file.getAbsolutePath());
				FileInputStream fis = new FileInputStream(file);
				BufferedReader buffer = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
				String line = buffer.readLine();
				if (this.validHeader(line)) {
					while ((line = buffer.readLine()) != null) {
						List<String> values = this.parseLine(line);
						// id is made of code-space and ID
						String tagName = values.get(1) + "-" + values.get(2);
						// TODO validate the tag name

						if (tags.containsKey(tagName)) {
							tags.get(tagName).detectionCount++;
						} else {
							tags.put(tagName, new Tag(tagName));
						}
						if (receiverName != null && !values.get(9).equals(receiverName)) {
							String msg = "more than one receiver is in the file";
							messages.add(msg);
							throw new Exception(msg);
						} else {
							receiverName = values.get(9);
						}
						// TODO validate the receiver name

						// buffer the record
						try {
							detections.add(new Detection(tags.get(tagName), new Timestamp(timestampParser.parse(values.get(0)).getTime())));
						} catch (ParseException e) {
							String msg = "an invalid timestamp string has been found (" + values.get(0) + "), expecting format " + timestampFormat;
							messages.add(msg);
							throw new Exception(msg);
						}
					}
				}
				String msg = "Total detections count: " + detections.size();
				messages.add(msg);
				logger.info(msg);
				fis.close();
			} catch (IOException e) {
				throw new Exception("exiting, could not read datafile", e);
			} catch (Exception e) {
				throw new Exception("exiting, could not process datafile", e);
			}
			// 2. Find the database ids of the tags detected.
			PreparedStatement pst = conn.prepareStatement("SELECT DEVICE_ID FROM DEVICE WHERE CODE_NAME = ? AND DEVICE_TYPE_ID = 2");
			ResultSet rset;
			messages.add("Known tag devices:");
			for (Iterator<Entry<String, Tag>> i = tags.entrySet().iterator(); i.hasNext();) {
				Tag tag = i.next().getValue();
				logger.info("checking for existing tag '" + tag.name + "' in database");
				pst.setString(1, tag.name);
				pst.execute();
				rset = pst.getResultSet();
				if (rset != null) {
					if (rset.next()) {
						tag.id = rset.getInt(1);
						messages.add("\tfid = aatams.device." + tag.id + ", code_name = " + tag.name );
					}
				}
				rset.close();
			}
			// 3. Check that the deployment_download record exists and is linked
			// to the correct receiver
			Statement smt = null;
			if (downloadFid != null) {
				if (downloadFid.startsWith(DOWNLOAD_ID_PREFIX)) {
					try {
						deploymentDownloadId = Integer.valueOf(downloadFid.substring(DOWNLOAD_ID_PREFIX.length()));
					} catch (NumberFormatException e) {
						throw new Exception("the downloadId parameter passed is invalid, it must end with a number", e);
					}
					smt = conn.createStatement();
					// a receiver deployment must have a project_role_person
					// associated with it,
					// extract this to be used for any new tags added.
					rset = smt.executeQuery("SELECT RECEIVER_DEPLOYMENT.DEPLOYMENT_ID, RECEIVER_DEPLOYMENT.PROJECT_ROLE_PERSON_ID FROM "
							+ "DEPLOYMENT_DOWNLOAD, RECEIVER_DEPLOYMENT, DEVICE WHERE "
							+ "DEPLOYMENT_DOWNLOAD.DEPLOYMENT_ID = RECEIVER_DEPLOYMENT.DEPLOYMENT_ID AND "
							+ "RECEIVER_DEPLOYMENT.DEVICE_ID = DEVICE.DEVICE_ID AND " + "DEVICE.CODE_NAME = '" + receiverName + "' AND " + "DOWNLOAD_ID = "
							+ deploymentDownloadId);
					if (rset != null) {
						if (rset.next()) {
							receiverDeploymentId = rset.getInt(1);
							projectRolePersonId = rset.getInt(2);
							if (projectRolePersonId == 0) {
								logger.info("could find a project_role_person record id to use for adding unknown tags to database");
							}
						} else {
							throw new Exception("a matching 'deployment download' record has not been found in the database for '" + deploymentDownloadId + "'"
									+ " being linked to a 'receiver deployment' record with receiver device code named '" + receiverName + "'");
						}
					}
					rset.close();
				} else {
					throw new Exception("the downloadId parameter passed is invalid, it must start with '" + DOWNLOAD_ID_PREFIX + "'");
				}
			} else {
				throw new Exception("the downloadId parameter passed is null");
			}
			// 3.1. Check that the file has not already been processed (potentially)
			smt = conn.createStatement();
			rset = smt.executeQuery("SELECT DEPLOYMENT_DOWNLOAD_ID FROM DEPLOYMENT\n" +
					"WHERE UPPERCASE(FILE_NAME) = '" + file.getName().toUpperCase() + "'");
			if(rset.next()){
				throw new Exception("A download file with the same name is associated with another deployment download FID=aatams.deployment_download."+rset.getLong(1));
			}
			rset.close();
			smt.close();
			// 4. Find or create new tag records in the database.
			// make MODEL_ID = 0 which links to the 'UNKNOWN TAG MODEL' model.
			pst = conn.prepareStatement("INSERT INTO DEVICE (DEVICE_ID,DEVICE_TYPE_ID,MODEL_ID,CODE_NAME,PROJECT_ROLE_PERSON_ID)"
					+ " VALUES (DEVICE_SERIAL.NEXTVAL,2,0,?,?)", Statement.RETURN_GENERATED_KEYS);
			boolean first = true;
			for (Iterator<Entry<String, Tag>> i = tags.entrySet().iterator(); i.hasNext();) {
				Tag tag = i.next().getValue();
				if (tag.id == 0) {
					if (first) {
						messages.add("<p>New tag devices:<ul>");
						first = false;
					}
					pst.setString(1, tag.name);
					pst.setInt(2, projectRolePersonId);
					pst.execute();
					rset = pst.getGeneratedKeys();
					if (rset != null) {
						if (rset.next()) {
							smt = conn.createStatement();
							ResultSet rowId = smt.executeQuery("SELECT DEVICE_ID FROM DEVICE WHERE ROWID = '" + rset.getString(1) + "'");
							if (rowId != null && rowId.next()) {
								tag.id = Integer.valueOf(rowId.getInt(1));
								messages.add("\t" + "fid = aatams.device." + tag.id + ", code_name = " + tag.name);
							} else {
								throw new Exception("could not retrieve id of new tag device just created");
							}
							rowId.close();
						}
					}
					rset.close();
					newTags.put(tag.name, tag);
				}
			}
			if (!first)
				messages.add("</ul></p>");
			// 5. Create new detection records in the database.
			// start a thread to handle this time consuming part
			DetectionsInserterAndReportGenerator inserter = new DetectionsInserterAndReportGenerator();
			inserter.start();
			success = true;
		} catch (SQLException se) {
			logger.fatal("SQL Exception:");
			// Loop through the SQL Exceptions
			while (se != null) {
				logger.fatal("State  : " + se.getSQLState());
				logger.fatal("Message: " + se.getMessage());
				logger.fatal("Error  : " + se.getErrorCode());
				se = se.getNextException();
			}
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			if (success) {
				messages.add("A full download file processing report will be emailed to all email addressees linked to deployment aatams.receiver_deployment."
								+ receiverDeploymentId);
			} else {
				messages.add("File processing of " + downloadFid + " has failed due to an unforseen error, contact eMII for more information.");
				try {
					if (conn != null && !conn.isClosed()) {
						conn.rollback();
					}
				} catch (SQLException e) {
					logger.fatal("attempted rollback for ");
				}
			}
		}
	}

	private boolean validHeader(String line) {
		return true;
	}

	/**
	 * Parse one line.
	 * 
	 * @return List of Strings, minus their double quotes
	 */
	private List<String> parseLine(String line) {

		List<String> list = new ArrayList<String>();
		Matcher m = csvRegex.matcher(line);
		// For each field
		while (m.find()) {
			String match = m.group();
			if (match == null)
				break;
			if (match.endsWith(",")) { // trim trailing ,
				match = match.substring(0, match.length() - 1);
			}
			if (match.startsWith("\"")) { // assume also ends with
				match = match.substring(1, match.length() - 1);
			}
			if (match.length() == 0)
				match = null;
			list.add(match);
		}
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		BasicConfigurator.configure();
		AcousticReceiverFileProcessor processor = new AcousticReceiverFileProcessor();
		if (args.length == 6) {
			ArrayList<String> buffer = new ArrayList<String>();
			processor.doProcessing(args[0], args[1], args[2], args[3], args[4], args[5], buffer);
			for(int i = 0; i< buffer.size(); i++){
				System.out.println(buffer.get(i));
			}
		} else {
			processor.logger.error("6 parameters are needed: download id, file path, jdbc driver class, connection string, username and password");
		}
	}

	private class Detection {

		public Timestamp timestamp;
		public Tag tag;

		public Detection(Tag tag, Timestamp detectionDatetime) {
			this.tag = tag;
			this.timestamp = detectionDatetime;
		}
	}

	private class Tag {

		public String name;
		public int id = 0;
		public Integer detectionCount = 1;

		public Tag(String tagName) {
			this.name = tagName;
		}
	}

	private class DetectionsInserterAndReportGenerator extends Thread {

		private boolean success = false;
		private PreparedStatement pst = null;
		private Statement smt = null;
		private ResultSet rset = null;
		private String reportName = null;
		private HashMap<Integer, StringBuffer> projectTags = null;

		public void run() {
			try {
				pst = conn.prepareStatement("INSERT INTO DETECTION (DETECTION_ID, DEPLOYMENT_ID, DOWNLOAD_ID, TAG_ID, DETECTION_TIMESTAMP)"
						+ " VALUES (DETECTION_SERIAL.NEXTVAL," + receiverDeploymentId + "," + deploymentDownloadId + ",?,?)");
				for (Iterator<Detection> i = detections.iterator(); i.hasNext();) {
					Detection detection = i.next();
					pst.setInt(1, detection.tag.id);
					pst.setTimestamp(2, detection.timestamp);
					pst.execute();
				}
				// happy ending
				conn.commit();
				success = true;
			} catch (SQLException se) {
				logger.fatal("SQL Exception:");
				// Loop through the SQL Exceptions
				while (se != null) {
					logger.fatal("State  : " + se.getSQLState());
					logger.fatal("Message: " + se.getMessage());
					logger.fatal("Error  : " + se.getErrorCode());
					se = se.getNextException();
				}
			} catch (Exception e) {
				// messages.add("file processing of " + downloadFid + " has
				// failed due t, contact eMII for more information.");
				logger.fatal(e);
			} finally {
				try {
					if (!success) {
						conn.rollback();
						return;
					}
				} catch (SQLException e) {
					logger.fatal("attempted rollback failed", e);
					return;
				}
			}
			// 6. Prepare a report(log) file to be sent back to the
			// submitter and data manager.
			try {
				// add the main info
				reportName = "aatams.deployment_download." + deploymentDownloadId + ".txt";
				FileOutputStream fos = new FileOutputStream(reportFolder + "/" + reportName);
				BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
				buffer.append("RECEIVER DOWNLOAD FILE PROCESSING REPORT\n\n");
				buffer.append("Processing file:          " + file.getName() + "\n");
				buffer.append("Deployment Download FID:  aatams.deployment_download." + deploymentDownloadId + "\n");
				buffer.append("Receiver Deployment FID:  aatams.receiver_deployment." + receiverDeploymentId + "\n");
				buffer.append("Project Role Person FID:  aatams.project_role_person." + projectRolePersonId + "\n\n");
				// add the known tags table
				if (tags.size() > newTags.size()) {
					buffer.append("KNOWN TAGS\n");
					buffer.append("----------------------------------------\n");
					buffer.append("TAG CODE NAME             TAG DEVICE FID\n");
					buffer.append("----------------------------------------\n");
					for (Iterator<Entry<String, Tag>> i = tags.entrySet().iterator(); i.hasNext();) {
						Entry<String, Tag> entry = i.next();
						if (!newTags.containsKey(entry.getKey())) {
							Tag tag = entry.getValue();
							buffer.append(String.format("%-20s%20s%n", tag.name, "aatams.device." + tag.id));
						}
					}
					buffer.append("----------------------------------------\n\n");
				}
				// add the new tags table
				if (newTags.size() > 0) {
					buffer.append("UNKNOWN(NEW)TAGS\n");
					buffer.append("----------------------------------------\n");
					buffer.append("TAG CODE NAME             TAG DEVICE FID\n");
					buffer.append("----------------------------------------\n");
					for (Iterator<Entry<String, Tag>> i = newTags.entrySet().iterator(); i.hasNext();) {
						Tag tag = i.next().getValue();
						buffer.append(String.format("%-20s%20s%n", tag.name, "aatams.device." + tag.id));
					}
					buffer.append("----------------------------------------\n\n");
				}
				// add the tag detection count table
				// put this data into the DOWNLOAD_TAG_SUMMARY table as well for later use.
				try {
					long total = 0;
					ArrayList<Long> deviceIds = new ArrayList<Long>();
					if (detections.size() > 0) {
						pst = conn.prepareStatement("INSERT INTO DOWNLOAD_TAG_SUMMARY(DOWNLOAD_ID,DEVICE_ID,DETECTION_COUNT)VALUES(?,?,?)");
						smt = conn.createStatement();
						rset = smt.executeQuery("SELECT DEVICE.DEVICE_ID, DEVICE.CODE_NAME, COUNT(*) AS COUNT "
								+ "FROM DETECTION INNER JOIN DEVICE ON DETECTION.TAG_ID = DEVICE.DEVICE_ID " + "WHERE DETECTION.DOWNLOAD_ID = "
								+ deploymentDownloadId + " " + "GROUP BY DEVICE.DEVICE_ID, DEVICE.CODE_NAME " + "ORDER BY COUNT DESC, CODE_NAME ASC");
						if (rset != null) {
							while (rset.next()) {
								if (rset.isFirst()) {
									buffer.append("DETECTION COUNT BY TAG DEVICE SUMMARY\n");
									buffer.append("----------------------------------------\n");
									buffer.append("TAG CODE NAME                      COUNT\n");
									buffer.append("----------------------------------------\n");
								}
								buffer.append(String.format("%-30s%10s%n", rset.getString(2), rset.getString(3)));
								total += rset.getLong(3);
								deviceIds.add(rset.getLong(1));
								pst.setLong(1, deploymentDownloadId);
								pst.setLong(2, rset.getLong(1));
								pst.setLong(3, rset.getLong(3));
								pst.execute();
								rset = pst.getResultSet();
							// add a tag * detections summary
							}
							buffer.append("----------------------------------------\n");
						}
						if (total == detections.size()) {
							buffer.append(String.format("TOTAL COUNT%29d%n", total));
							buffer.append("----------------------------------------\n\n");
						} else {
							throw new Exception("detections count from database and file don't match: " + total + " vs " + detections.size());
						}
						rset.close();
						pst.close();
						// set the file_name and the total detections in the deployment_download record
						// this is the only way to prevent a file being processed twice, by checking that the file name doesn't
						// already exist.
						smt = conn.createStatement();
						smt.execute("UPDATE DEPLOYMENT_DOWNLOAD SET\n" +
								"FILE_NAME = '" + file.getName() + "',\n" +
								"DETECTIONS = " + detections.size() + "\n" +
								"WHERE DEPLOYMENT_DOWNLOAD_ID = " + deploymentDownloadId);
						smt.close();
						// add a tag release summary
						projectTags = new HashMap<Integer, StringBuffer>();
						StringBuffer sb = new StringBuffer();
						for (Iterator<Long> i = deviceIds.iterator(); i.hasNext();) {
							sb.append(i.next().toString());
							if (i.hasNext())
								sb.append(",");
						}
						String list = sb.toString();
						rset = smt.executeQuery("SELECT COUNT(*) FROM TAG_RELEASE WHERE DEVICE_ID IN (" + list + ")");
						if (rset.next() && rset.getInt(1) > 0) {
							rset.close();
							String qry = "SELECT TAG_RELEASE.RELEASE_ID, DEVICE.CODE_NAME, CLASSIFICATION.NAME,\n"
									+ "PROJECT_PERSON.PROJECT_NAME, PROJECT_PERSON.PROJECT_FID,\n"
									+ "PROJECT_PERSON.PERSON_ROLE, TAG_RELEASE.PROJECT_ROLE_PERSON_ID\n"
									+ "FROM TAG_RELEASE INNER JOIN DEVICE ON TAG_RELEASE.DEVICE_ID = DEVICE.DEVICE_ID\n"
									+ "INNER JOIN PROJECT_PERSON ON TAG_RELEASE.PROJECT_ROLE_PERSON_ID = PROJECT_PERSON.PROJECT_ROLE_PERSON_ID\n"
									+ "LEFT OUTER JOIN CLASSIFICATION ON TAG_RELEASE.CLASSIFICATION_ID = CLASSIFICATION.CLASSIFICATION_ID\n"
									+ "WHERE TAG_RELEASE.DEVICE_ID IN (" + list + ")\n" + "ORDER BY CODE_NAME ASC";
							System.out.print(qry);
							rset = smt.executeQuery(qry);
							if (rset != null) {
								while (rset.next()) {
									if (rset.isFirst()) {
										buffer.append("AVAILABLE RELEASE INFORMATION SUMMARY FOR TAGS DETECTED\n");
										buffer.append("-----------------------------------------------------------------------------------\n");
										buffer.append("TAG CODE NAME     TAG RELEASE FID           ANIMAL CLASSIFICATION                  \n");
										buffer.append("-----------------------------------------------------------------------------------\n");
									}
									buffer.append(String.format("%-18s%-26s%-36s%n", rset.getString(2), "aatams.tag_release." + rset.getString(1), rset
											.getString(3)));
									// add to the list of tags from outside the
									// current project
									if (rset.getInt(7) != projectRolePersonId) {
										if (!projectTags.containsKey(rset.getInt(7))) {
											projectTags.put(rset.getInt(7), new StringBuffer(rset.getString(2)));
										} else {
											projectTags.get(rset.getInt(7)).append("\n" + rset.getString(2));
										}
									}
								}
								buffer.append("-----------------------------------------------------------------------------------\n");
							}
						} else {
							buffer.append("No tag release records have been found for the tags in the download file\n\n");
						}
					} else {
						buffer.append("No detections where found in the file to process into database\n\n");
					}
				} catch (SQLException e) {
					logger.error("an sql exception was encountered when generating the download report", e);
					return;
				}
				buffer.flush();
				buffer.close();
			} catch (Exception e) {
				logger.error("unexpected error when generating full report", e);
				return;
			}
			// 7. Send the main report
			String projectPersonName = "", projectAddressTo = "";
			Mailer mailer = new Mailer(SMTP_HOST, SMTP_PORT, SMTP_USER, SMTP_PWD);
			Email email = new Email();
			try {
				rset = smt.executeQuery("SELECT PERSON.NAME, PERSON.EMAIL FROM PERSON, PROJECT_ROLE_PERSON\n"
						+ "WHERE PERSON.PERSON_ID = PROJECT_ROLE_PERSON.PERSON_ID AND\n" + "PROJECT_ROLE_PERSON.PROJECT_ROLE_PERSON_ID = "
						+ projectRolePersonId);
				if (rset.next()) {
					projectPersonName = rset.getString(1);
					projectAddressTo = rset.getString(2);
					email.setFromAddress(PROCESSOR_FROM, PROCESSOR_ADDRESS);
					email.addRecipient(projectPersonName, projectAddressTo, RecipientType.TO);
					email.setSubject("AATAMS Deployment Download Processing Report (FID=aatams.deployment_download." + deploymentDownloadId + ")");
					email.setText("Please find attached a report with details of a Receiver Deployment Download just processed");
					email.addAttachment(reportName, new FileDataSource(reportFolder + "/" + reportName));
					try {
						mailer.sendMail(email);
					} catch (Exception e) {
						logger.error("error sending report to " + projectAddressTo, e);
					}
				}
				rset.close();
			} catch (SQLException e) {
				logger.error("an sql exception was encountered when accessing report email", e);
			}
			// 8. Send any 'out of project' tag detection reports
			String personName = "", addressTo = "";
			for (Iterator<Entry<Integer, StringBuffer>> i = projectTags.entrySet().iterator(); i.hasNext();) {
				Entry<Integer, StringBuffer> entry = i.next();
				try {
					rset = smt
							.executeQuery("SELECT PERSON.NAME, PERSON.EMAIL FROM PERSON, PROJECT_ROLE_PERSON\n"
									+ "WHERE PERSON.PERSON_ID = PROJECT_ROLE_PERSON.PERSON_ID AND\n" + "PROJECT_ROLE_PERSON.PROJECT_ROLE_PERSON_ID = "
									+ entry.getKey());
					if (rset.next()) {
						personName = rset.getString(1);
						addressTo = rset.getString(2);
						email = new Email();
						email.setFromAddress(PROCESSOR_FROM, PROCESSOR_ADDRESS);
						email.addRecipient(personName, addressTo, RecipientType.TO);
						email.addRecipient(projectPersonName, projectAddressTo, RecipientType.CC);
						email.setSubject("AATAMS 'Foreign' Detections Report");
						email.setText("The following tags, having a tag release under your name, have been found "
								+ "in another person's deployment download: \n" + entry.getValue().toString() + "\n\n"
								+ "Please contact the responsible person (carbon-copied this email) or obtain more"
								+ "information via the deployment download record FID=aatams.deployment_download." + deploymentDownloadId
								+ " viewable at the AATAMS web site (" + AATAMS_WEB_SITE_URI + ").");
						try {
							mailer.sendMail(email);
						} catch (Exception e) {
							logger.error("error sending report to " + addressTo, e);
						}
					}
					rset.close();
				} catch (SQLException e) {
					logger.error("an sql exception was encountered when accessing report email", e);
				}
			}
		}
	}
}
