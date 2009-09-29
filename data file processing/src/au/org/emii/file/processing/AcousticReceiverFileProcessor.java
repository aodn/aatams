/**
 * 
 */
package au.org.emii.file.processing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
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
import java.io.PrintStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

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
public class AcousticReceiverFileProcessor {

	public String reportFolder = "c:/temp";

	private static final String timestampFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	private static final SimpleDateFormat timestampParser = new SimpleDateFormat(timestampFormat);
	private static final String pattern = "\"([^\"]+?)\",?|([^,]+),?|,";
	private static final Pattern csvRegex = Pattern.compile(pattern);
	private static final String DOWNLOAD_ID_PREFIX = "aatams.deployment_download.";
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
			PrintStream outStream) {

		boolean success = false;

		// if not passed set outStream to System.out;
		if (outStream == null) {
			outStream = System.out;
		}

		try {
			// Load the database driver
			Class.forName(jdbcDriverClassName);
			// Get a connection to the database
			conn = DriverManager.getConnection(connectionString, username, password);
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
				outStream.println("Processing file: " + file.getName());
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
							outStream.println(msg);
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
							outStream.println(msg);
							throw new Exception(msg);
						}
					}
				}
				String msg = "Total detections count: " + detections.size();
				outStream.println(msg);
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
			outStream.println("<p>Known tag devices:<ul>");
			for (Iterator<Entry<String, Tag>> i = tags.entrySet().iterator(); i.hasNext();) {
				Tag tag = i.next().getValue();
				logger.info("checking for existing tag '" + tag.name + "' in database");
				pst.setString(1, tag.name);
				pst.execute();
				rset = pst.getResultSet();
				if (rset != null) {
					if (rset.next()) {
						tag.id = rset.getInt(1);
						outStream.println("<li>fid = aatams.device." + tag.id + ", code_name = " + tag.name + "</li>");
					}
				}
				rset.close();
			}
			outStream.println("</ul></p>");
			// 3. Check that the deployment_download record exists and is linked
			// to the correct receiver
			if (downloadFid != null) {
				if (downloadFid.startsWith(DOWNLOAD_ID_PREFIX)) {
					try {
						deploymentDownloadId = Integer.valueOf(downloadFid.substring(DOWNLOAD_ID_PREFIX.length()));
					} catch (NumberFormatException e) {
						throw new Exception("the downloadId parameter passed is invalid, it must end with a number", e);
					}
					Statement smt = conn.createStatement();
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
			// 4. Find or create new tag records in the database.
			pst = conn.prepareStatement("INSERT INTO DEVICE (DEVICE_ID,DEVICE_TYPE_ID,CODE_NAME,PROJECT_ROLE_PERSON_ID)"
					+ " VALUES (DEVICE_SERIAL.NEXTVAL,2,?,?)", Statement.RETURN_GENERATED_KEYS);
			outStream.println("<p>New tag devices:<ul>");
			for (Iterator<Entry<String, Tag>> i = tags.entrySet().iterator(); i.hasNext();) {
				Tag tag = i.next().getValue();
				if (tag.id == 0) {
					pst.setString(1, tag.name);
					pst.setInt(2, projectRolePersonId);
					pst.execute();
					rset = pst.getGeneratedKeys();
					if (rset != null) {
						if (rset.next()) {
							Statement smt = conn.createStatement();
							ResultSet rowId = smt.executeQuery("SELECT DEVICE_ID FROM DEVICE WHERE ROWID = '" + rset.getString(1) + "'");
							if (rowId != null && rowId.next()) {
								tag.id = Integer.valueOf(rowId.getInt(1));
								outStream.println("\t" + "fid = aatams.device." + tag.id + ", code_name = " + tag.name);
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
			outStream.println("</ul></p>");
			// 5. Create new detection records in the database.
			// this is the time consuming part, can we fork a thread to handle
			// this?
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
				outStream
						.println("<p>A full download file processing report will be emailed to all email addressees linked to deployment aatams.receiver_deployment."
								+ receiverDeploymentId + ".</p>");
			} else {
				outStream.println("File processing of " + downloadFid + " has failed due to an unforseen error, contact eMII for more information.");
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
			processor.doProcessing(args[0], args[1], args[2], args[3], args[4], args[5], null);
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

		public void run() {
			boolean success = false;
			try {
				PreparedStatement pst = conn.prepareStatement("INSERT INTO DETECTION (DETECTION_ID, DEPLOYMENT_ID, DOWNLOAD_ID, TAG_ID, DETECTION_TIMESTAMP)"
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
				// outStream.println("file processing of " + downloadFid + " has
				// failed due t, contact eMII for more information.");
				logger.fatal(e);
			} finally {
				try {
					if (!success) {
						conn.rollback();
					}
				} catch (SQLException e) {
					logger.fatal("attempted rollback failed", e);
				}
			}

			try {
				// 6. Prepare a report(log) file to be sent back to the
				// submitter and data manager.
				FileOutputStream fos = new FileOutputStream(reportFolder + "/aatams.deployment_download." + deploymentDownloadId + ".txt");
				BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
				buffer.append("RECEIVER DOWNLOAD FILE PROCESSING REPORT\n\n");
				buffer.append("Processing file: " + file.getName() + "\n");
				buffer.append("Deployment Download FID = aatams.deployment_download." + deploymentDownloadId + "\n");
				buffer.append("Receiver Deployment FID = aatams.receiver_deployment." + receiverDeploymentId + "\n");
				buffer.append("Project Role Person FID = aatams.project_role_person." + projectRolePersonId + "\n\n");
				if (tags.size() > newTags.size()) {
					buffer.append("KNOWN TAGS\n");
					buffer.append("----------------------------------------\n");
					buffer.append("TAG CODE NAME                 DEVICE FID\n");
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
				if (newTags.size() > 0) {
					buffer.append("UNKNOWN(NEW)TAGS\n");
					buffer.append("----------------------------------------\n");
					buffer.append("TAG CODE NAME                 DEVICE FID\n");
					buffer.append("----------------------------------------\n");
					for (Iterator<Entry<String, Tag>> i = newTags.entrySet().iterator(); i.hasNext();) {
						Tag tag = i.next().getValue();
						buffer.append(String.format("%-20s%20s%n", tag.name, "aatams.device." + tag.id));
					}
					buffer.append("----------------------------------------\n\n");
				}

				try {
					ResultSet rset;
					long total = 0;
					ArrayList<Long> deviceIds = new ArrayList<Long>();
					if (detections.size() > 0) {
						// add a tag * detections summary
						Statement smt = conn.createStatement();
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
							}
							buffer.append("----------------------------------------\n");
						}
						if (total == detections.size()) {
							buffer.append(String.format("TOTAL COUNT%29d%n", total));
							buffer.append("----------------------------------------\n");
						} else {
							throw new Exception("detections count from database and file don't match: " + total + " vs " + detections.size());
						}
						// add a tag release summary
						StringBuffer sb = new StringBuffer();
						for (Iterator<Long> i = deviceIds.iterator(); i.hasNext();) {
							sb.append(i.next().toString());
							if (i.hasNext())
								sb.append(",");
						}
						String list = sb.toString();
						rset = smt.executeQuery("SELECT COUNT(*) FROM TAG_RELEASE WHERE DEVICE_ID IS IN (" + list + ")");
						if(rset.next() && rset.getInt(1) > 0){
							rset.close();
							String qry = "SELECT TAG_RELEASE.RELEASE_ID, DEVICE.CODE_NAME, CLASSIFICATION.NAME,\n" + 
							"CONCAT(CONCAT(CONCAT(PROJECT_PERSON.PROJECT_NAME,'('),PROJECT_PERSON.PROJECT_FID),')') AS PROJECT,\n" +
							"PROJECT_PERSON.PERSON_ROLE\n" + 
							"FROM TAG_RELEASE INNER JOIN DEVICE ON TAG_RELEASE.DEVICE_ID = DEVICE.DEVICE_ID\n" +
							"INNER JOIN PROJECT_PERSON ON TAG_RELEASE.PROJECT_ROLE_PERSON_ID = PROJECT_PERSON.PROJECT_ROLE_PERSON_ID\n" +
							"LEFT OUTER JOIN CLASSIFICATION ON TAG_RELEASE.CLASSIFICATION_ID = CLASSIFICATION.CLASSIFICATION_ID\n" +
							"WHERE TAG_RELEASE.DEVICE_ID IN (" + list + ")\n" + 
							"ORDER BY CODE_NAME ASC";
							System.out.print(qry);
							rset = smt.executeQuery(qry);
							if (rset != null) {
								while (rset.next()) {
									if (rset.isFirst()) {
										buffer.append("RELATED TAG RELEASE INFORMATION\n");
										buffer.append("------------------------------------------------------------------------------------------\n");
										buffer.append("TAG CODE NAME       CLASSIFICATION                PROJECT             PERSON\n");
										buffer.append("------------------------------------------------------------------------------------------\n");
									}
									buffer.append(String.format("%-20s%-30s%-20s%-20s%n", rset.getString(2), rset.getString(3),
											rset.getString(4), rset.getString(5)));
								}
								buffer.append("------------------------------------------------------------------------------------------\n");
							}
						}else{
							buffer.append("No tag release records have been found for the tags in the download file\n\n");
						}
					} else {
						buffer.append("No detections where found in the file to process into database\n\n");
					}
				} catch (SQLException e) {
					logger.error("an sql exception was encountered when generating the download report", e);
				}
				buffer.flush();
				buffer.close();
			} catch (Exception e) {
				logger.error("unexpected error when generating full report", e);
			}
		}
	}
}
