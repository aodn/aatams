/**
 * 
 */
package au.org.emii.file.processing;

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
import java.io.PrintStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import javax.mail.Message.RecipientType;
import javax.activation.FileDataSource;
import org.codemonkey.vesijama.Email;
import org.codemonkey.vesijama.Mailer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import javax.sql.DataSource;
import java.sql.DriverManager;

/**
 * Processes an uploaded CSV export file from the AATAMS website.
 * 
 * Reads and validates the file, sending feedback to the user.
 * 
 * On validation of the file a <class>'DetectionsInserterAndReportGenerator</class> thread
 * is executed that does the creation of the detection records in the database and creates
 * a report that is emailed to the person responsible for the receiver deployment.
 * 
 * @author Stephen Cameron
 * 
 */
public final class AcousticReceiverFileProcessor {

	public String reportFolder = "c:/temp";

	private static final String timestampFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	private static final SimpleDateFormat timestampParser = new SimpleDateFormat(timestampFormat);
	private static final String pattern = "\"([^\"]+?)\",?|([^,]+),?|,";
	private static final Pattern csvRegex = Pattern.compile(pattern);
	private String downloadFidPrefix = null;
	private String reportSenderName = null;
	private String reportSenderEmailAddress = null;
	private String smtpHost = "postoffice.utas.edu.au";
	private int smtpPort = 25;
	private String smtpUser = null;
	private String smtpPassword = null;
	private String aatamsWebsiteUri = null;
	private Logger logger = Logger.getLogger(this.getClass());
	private Connection conn = null;
	private TreeMap<String, Tag> tags = new TreeMap<String, Tag>();
	private TreeMap<String, Tag> newTags = new TreeMap<String, Tag>();
	private ArrayList<Detection> detections = new ArrayList<Detection>();
	private String receiverName = null;
	private int installationId = 0;
	private int installationStationId = 0;
	private int projectRolePersonId = 0;
	private int receiverDeploymentId = 0;
	private int deploymentDownloadId = 0;
	private Timestamp deploymentDownloadTimestamp = null;
	private float deploymentLatitude;
	private float deploymentLongitude;
	private File file;
	
	//make default constructor private
	private AcousticReceiverFileProcessor(){}
	
	public AcousticReceiverFileProcessor(
			String downloadFidPrefix,
			String reportSenderName,
			String reportSenderEmailAddress,
			String smtpHost,
			int smtpPort,
			String smtpUser,
			String smtpPassword,
			String aatamsWebsiteUri) throws IllegalArgumentException{
		if(downloadFidPrefix == null){
			throw new IllegalArgumentException("downloadFidPrefix argument cannot be null");
		}else{
			this.downloadFidPrefix = downloadFidPrefix;
		}
		if(reportSenderName == null){
			throw new IllegalArgumentException("reportSenderName argument cannot be null");
		}else{
			this.reportSenderName = reportSenderName;
		}
		if(reportSenderEmailAddress == null){
			throw new IllegalArgumentException("reportSenderEmailAddress argument cannot be null");
		}else{
			this.reportSenderEmailAddress = reportSenderEmailAddress;
		}
		if(smtpHost == null){
			throw new IllegalArgumentException("smtpHost argument cannot be null");
		}else{
			this.smtpHost = smtpHost;
		}
		if(smtpUser == null){
			throw new IllegalArgumentException("smtpUser argument cannot be null");
		}else{
			this.smtpUser = smtpUser;
		}
		if(smtpPassword == null){
			throw new IllegalArgumentException("smtpPassword argument cannot be null");
		}else{
			this.smtpPassword = smtpPassword;
		}
		if(aatamsWebsiteUri == null){
			throw new IllegalArgumentException("aatamsWebsiteUri argument cannot be null");
		}else{
			this.aatamsWebsiteUri = aatamsWebsiteUri;
		}	
	}

	/**
	 * Processes an uploaded CSV export file from the AATAMS website. 
	 * 
	 * @param database - database connection
	 * @param downloadFid - the Feature Identifier (gml:id) for the deployment download
	 * @param filePath - the full path to the file for processing
	 * @param messages - array for message addition to return to user
	 */
	public void doProcessing(Connection conn, String downloadFid, String filePath, 
			ArrayList<String> messages) throws IllegalArgumentException {
		//validate parameters
		if(conn == null){
			throw new IllegalArgumentException("database connection argument cannot be null");
		}else{
			try{
				if(!conn.isValid(0)){
					throw new IllegalArgumentException("database connection argument is invalid");
				}
			}catch(SQLException e){
				throw new IllegalArgumentException("database connection argument is invalid", e);
			}
		}
		if(downloadFid == null){
			throw new IllegalArgumentException("downloadFid argument cannot be null");
		}
		if(filePath == null){
			throw new IllegalArgumentException("filePath argument cannot be null");
		}
		if(messages == null){
			throw new IllegalArgumentException("messages argument cannot be null");
		}
		boolean success = false;

		try {
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
						messages.add("fid = aatams.device." + tag.id + ", code_name = " + tag.name );
					}
				}
				rset.close();
			}
			// 3. Check that the deployment_download record exists and is linked
			// to the correct receiver
			Statement smt = null;
			if (downloadFid != null) {
				if (downloadFid.startsWith(downloadFidPrefix)) {
					try {
						deploymentDownloadId = Integer.valueOf(downloadFid.substring(downloadFidPrefix.length()));
					} catch (NumberFormatException e) {
						throw new Exception("the downloadId parameter passed is invalid, it must end with a number", e);
					}
					smt = conn.createStatement();
					// a receiver deployment must have a project_role_person
					// associated with it,
					// extract this to be used for any new tags added.
					rset = smt.executeQuery("SELECT RECEIVER_DEPLOYMENT.DEPLOYMENT_ID, RECEIVER_DEPLOYMENT.PROJECT_ROLE_PERSON_ID, "
							+ "RECEIVER_DEPLOYMENT.INSTALLATION_ID, RECEIVER_DEPLOYMENT.STATION_ID, " 
							+ "RECEIVER_DEPLOYMENT.LONGITUDE, RECEIVER_DEPLOYMENT.LATITUDE, " 
							+ "DEPLOYMENT_DOWNLOAD.DOWNLOAD_TIMESTAMP  FROM "
							+ "DEPLOYMENT_DOWNLOAD, RECEIVER_DEPLOYMENT, DEVICE WHERE "
							+ "DEPLOYMENT_DOWNLOAD.DEPLOYMENT_ID = RECEIVER_DEPLOYMENT.DEPLOYMENT_ID AND "
							+ "RECEIVER_DEPLOYMENT.DEVICE_ID = DEVICE.DEVICE_ID AND " + "DEVICE.CODE_NAME = '" + receiverName + "' AND " + "DOWNLOAD_ID = "
							+ deploymentDownloadId);
					if (rset != null) {
						if (rset.next()) {
							receiverDeploymentId = rset.getInt(1);
							projectRolePersonId = rset.getInt(2);
							installationId = rset.getInt(3);
							installationStationId = rset.getInt(4);
							deploymentLongitude = rset.getFloat(5);
							deploymentLatitude =  rset.getFloat(6);
							deploymentDownloadTimestamp = rset.getTimestamp(7);
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
					throw new Exception("the downloadId parameter passed is invalid, it must start with '" + downloadFidPrefix + "'");
				}
			} else {
				throw new Exception("the downloadId parameter passed is null");
			}
			// 3.1. Check that the file has not already been processed (potentially)
			smt = conn.createStatement();
			rset = smt.executeQuery("SELECT DOWNLOAD_ID FROM DEPLOYMENT_DOWNLOAD\n" +
					"WHERE UPPER(FILENAME) = '" + file.getName().toUpperCase() + "'");
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
						messages.add("New tag devices:");
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
								messages.add("FID = aatams.device." + tag.id + ", code_name = " + tag.name);
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
								+ receiverDeploymentId + ".");
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

	/*
	 * For testing outside of web server
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		BasicConfigurator.configure();
		AcousticReceiverFileProcessor processor = new AcousticReceiverFileProcessor();
		if (args.length == 6) {
			//aatams.deployment_download.28 "c:\temp\aatams.deployment_download.28\VUE_Export.csv" "oracle.jdbc.driver.OracleDriver" "jdbc:oracle:thin:@obsidian.bluenet.utas.edu.au:1521:orcl" AATAMS boomerSIMS
			try{
				Class.forName(args[2]);
				Connection conn = DriverManager.getConnection(args[33], args[4], args[5]);
				processor.doProcessing(conn, args[0], args[1], null);
			}catch(Exception e){
				processor.logger.fatal("unexpected error", e);
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
		private String installationName = null;
		private String installationStationName = null;
		private String receiverDeploymentName = null;
		private String responsiblePersonNameAndRole = null;
		
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
				// outStream.println("file processing of " + downloadFid + " has
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
				// get the main info on installation, deployment and deployment download
				smt = conn.createStatement();
				if(installationId > 0){
					rset = smt.executeQuery("SELECT NAME FROM INSTALLATION WHERE INSTALLATION_ID = " + installationId);
				    if(rset.next()){
				    	installationName = rset.getString(1);
				    }else{ //unlikely
				    	throw new Exception("cannot locate installation with id = " + installationId);
				    }
				    rset.close();
				}else{
					installationName = "N/A";
				}
				if(installationStationId > 0){
					rset = smt.executeQuery("SELECT NAME, CURTAIN_POSITION FROM INSTALLATION_STATION WHERE STATION_ID = " + installationStationId);
				    if(rset.next()){
				    	installationStationName = rset.getString(1);
				    	int pos = rset.getInt(2);
				    	installationStationName += (rset.wasNull()) ? "" : "(Position="+pos+")";
				    }else{ //unlikely
				    	throw new Exception("cannot locate installation station with id = " + installationStationId);
				    }
				    rset.close();
				}else{
					installationStationName = "N/A";
				}
				if(receiverDeploymentId > 0){
					rset = smt.executeQuery("SELECT NAME FROM INSTALLATION_DEPLOYMENT WHERE DEPLOYMENT_ID = " + receiverDeploymentId);
				    if(rset.next()){
				    	receiverDeploymentName = rset.getString(1);
				    }else{ //unlikely
				    	throw new Exception("cannot locate receiver deployment with id = " + receiverDeploymentId);
				    }
				    rset.close();
				}else{//never
					throw new Exception("receiver deployment id is 0");
				}
				if(projectRolePersonId > 0){
					rset = smt.executeQuery("SELECT PERSON_ROLE FROM PROJECT_PERSON WHERE PROJECT_ROLE_PERSON_ID = " + projectRolePersonId);
				    if(rset.next()){
				    	responsiblePersonNameAndRole = rset.getString(1);
				    }else{ //unlikely
				    	throw new Exception("cannot locate project person with id = " + projectRolePersonId);
				    }
				    rset.close();
				}else{//never
					throw new Exception("project role person id is 0");
				}
				// add the known tags table
				reportName = "aatams.deployment_download." + deploymentDownloadId + ".txt";
				File report = new File(reportFolder + "/" + reportName);
				if(report.exists()){
					throw new Exception("receiver file processing report already exists (" + report.getAbsolutePath() + ")");
				}
				FileOutputStream fos = new FileOutputStream(report);
				BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
				buffer.append("RECEIVER DOWNLOAD FILE PROCESSING REPORT\n\n");
				buffer.append(String.format("%-20s %s%n","Processing file:",file.getName()));
				buffer.append(String.format("%-20s %s%n","Installation:", installationName + " (FID*=aatams.installation." + installationId  + ")"));
				buffer.append(String.format("%-20s %s%n","Station:", installationStationName + " (FID*=aatams.installation_station." + installationStationId  + ")"));
				buffer.append(String.format("%-20s %s%n","Receiver Deployment:", receiverDeploymentName +  " (FID*=aatams.receiver_deployment." + receiverDeploymentId + ")" ));
				buffer.append(String.format("%-20s %s%n","Longitude:", deploymentLongitude));
				buffer.append(String.format("%-20s %s%n","Latitude:", deploymentLatitude));
				buffer.append(String.format("%-20s %s%n","Download Timestamp: ", (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(deploymentDownloadTimestamp)) ));
				buffer.append(String.format("%-20s %s%n%n","Deployment Owner: ", responsiblePersonNameAndRole + " (FID*=aatams.project_role_person." + projectRolePersonId + ")"));
				buffer.append("* FID=Feature Identifier, This id code can be used in the AATAMS web site to retrieve specific features (records) of interest.\n");
				buffer.append("Please see the help documentation on the website for more details (" + aatamsWebsiteUri + "/help.html)\n\n");
				// add the known tags table
				if (tags.size() > newTags.size()) {
					buffer.append("KNOWN TAGS\n");
					buffer.append("----------------------------------------\n");
					buffer.append("TAG CODE NAME            TAG DEVICE FID*\n");
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
					buffer.append("TAG CODE NAME            TAG DEVICE FID*\n");
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
						smt.execute("UPDATE DEPLOYMENT_DOWNLOAD SET\n" +
								"FILENAME = '" + file.getName() + "',\n" +
								"DETECTIONS = " + detections.size() + "\n" +
								"WHERE DOWNLOAD_ID = " + deploymentDownloadId);
						conn.commit();
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
							rset = smt.executeQuery(qry);
							if (rset != null) {
								while (rset.next()) {
									if (rset.isFirst()) {
										buffer.append("AVAILABLE RELEASE INFORMATION SUMMARY FOR TAGS DETECTED\n");
										buffer.append("-----------------------------------------------------------------------------------\n");
										buffer.append("TAG CODE NAME     TAG RELEASE FID*          ANIMAL CLASSIFICATION                  \n");
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
							rset.close();
						} else {
							buffer.append("No tag release records have been found for the tags in the download file\n\n");
						}
						smt.close();
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
			Mailer mailer = new Mailer(smtpHost, smtpPort, smtpUser, smtpPassword);
			Email email = new Email();
			try {
				smt = conn.createStatement();
				rset = smt.executeQuery("SELECT PERSON.NAME, PERSON.EMAIL FROM PERSON, PROJECT_ROLE_PERSON\n"
						+ "WHERE PERSON.PERSON_ID = PROJECT_ROLE_PERSON.PERSON_ID AND\n" + "PROJECT_ROLE_PERSON.PROJECT_ROLE_PERSON_ID = "
						+ projectRolePersonId);
				if (rset.next()) {
					projectPersonName = rset.getString(1);
					projectAddressTo = rset.getString(2);
					email.setFromAddress(reportSenderName, reportSenderEmailAddress);
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
				smt.close();
			} catch (SQLException e) {
				logger.error("an sql exception was encountered when accessing report email", e);
			}
			// 8. Send any 'out of project' tag detection reports
			String personName = "", addressTo = "";
			for (Iterator<Entry<Integer, StringBuffer>> i = projectTags.entrySet().iterator(); i.hasNext();) {
				Entry<Integer, StringBuffer> entry = i.next();
				try {
					smt = conn.createStatement();
					rset = smt
							.executeQuery("SELECT PERSON.NAME, PERSON.EMAIL FROM PERSON, PROJECT_ROLE_PERSON\n"
									+ "WHERE PERSON.PERSON_ID = PROJECT_ROLE_PERSON.PERSON_ID AND\n" + "PROJECT_ROLE_PERSON.PROJECT_ROLE_PERSON_ID = "
									+ entry.getKey());
					if (rset.next()) {
						personName = rset.getString(1);
						addressTo = rset.getString(2);
						email = new Email();
						email.setFromAddress(reportSenderName, reportSenderEmailAddress);
						email.addRecipient(personName, addressTo, RecipientType.TO);
						email.addRecipient(projectPersonName, projectAddressTo, RecipientType.CC);
						email.setSubject("AATAMS 'Foreign' Detections Report");
						email.setText("The following tags, having a tag release under your name, have been found "
								+ "in another person's deployment download: \n" + entry.getValue().toString() + "\n\n"
								+ "Please contact the responsible person (carbon-copied this email) or obtain more"
								+ "information via the deployment download record FID=aatams.deployment_download." + deploymentDownloadId
								+ " viewable at the AATAMS web site (" + aatamsWebsiteUri + ").");
						try {
							mailer.sendMail(email);
						} catch (Exception e) {
							logger.error("error sending report to " + addressTo, e);
						}
					}
					rset.close();
					smt.close();
				} catch (SQLException e) {
					logger.error("an sql exception was encountered when accessing report email", e);
				}
			}
		}
	}
}
