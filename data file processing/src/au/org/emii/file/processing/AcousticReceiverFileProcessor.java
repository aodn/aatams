/**
 * 
 */
package au.org.emii.file.processing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.xml.DOMConfigurator;
//import org.apache.log4j.Level;


//TODO add deployment_download_id to detection table to associate detections with a specific file.
//this allows a final cross-check of detections in database after commit with those in file.
/**
 * @author Stephen Cameron
 * 
 */
public class AcousticReceiverFileProcessor {
	private static final String timestampFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	public static final SimpleDateFormat timestampParser = new SimpleDateFormat(timestampFormat);
	private static final String pattern = "\"([^\"]+?)\",?|([^,]+),?|,";
	private static final Pattern csvRegex = Pattern.compile(pattern);
	private static final String DOWNLOAD_ID_PREFIX = "aatams.deployment_download.";
	private Logger logger = Logger.getLogger(this.getClass());
	public void doProcessing(
			String downloadFid,
			String filePath,
			String jdbcDriverClassName,
			String connectionString,
			String username,
			String password){
		Connection conn = null;
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
			//Load the file
			File file = new File(filePath);
			if(!file.exists()){
				throw new Exception("invalid file path: " + filePath);
			}
			if(!file.canRead()){
				throw new Exception("file at "+  filePath + " cannot be opened for reading");
			}
			//1. Parse the file and get a unique list of tags - there are usually lots of duplicates
			//Also check that there is only one receiver in the file.
			HashMap<String,Tag> tags = new HashMap<String,Tag>();
			ArrayList<Detection> detections = new ArrayList<Detection>();
			String receiverName = null;
			try {
				// open file for reading
				logger.info("reading file: " + file.getAbsolutePath());
				FileInputStream fis = new FileInputStream(file);
				BufferedReader  buffer = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
				String line = buffer.readLine();
				if(this.validHeader(line)){
					while((line = buffer.readLine())!= null){
						List<String> values = this.parseLine(line);
						//id is made of code-space and ID
						String tagName = values.get(1) + "-" + values.get(2);
						//TODO validate the tag name
	
						if(tags.containsKey(tagName)){
							tags.get(tagName).detectionCount++;
						}else{
							tags.put(tagName,new Tag(tagName));
						}
						if(receiverName != null && !values.get(9).equals(receiverName)){
							throw new Exception("more than one receiver is in the file");
						}else{
							receiverName = values.get(9);
						}
						//TODO validate the receiver name
						
						//buffer the record
						try{
							detections.add(new Detection(tags.get(tagName), 
									new Timestamp(timestampParser.parse(values.get(0)).getTime())));
						}catch(ParseException e){
							throw new Exception("an invalid timestamp string has been found ("+values.get(0)+"), expecting format ");
						}
					}
				}
				logger.info("detection count: " + detections.size());
				fis.close();
			}catch(IOException e){
				logger.fatal("exiting, could not read datafile", e);
				return;
			}catch(Exception e){
				logger.fatal("exiting, could not process datafile", e);
				return;
			}
			//2. Find the database ids of the tags detected.
			PreparedStatement pst = conn.prepareStatement(
		    "SELECT DEVICE_ID FROM DEVICE WHERE CODE_NAME = ? AND DEVICE_TYPE_ID = 2");
			ResultSet rset;
			for(Iterator<Entry<String,Tag>> i = tags.entrySet().iterator(); i.hasNext();){
				Tag tag = i.next().getValue();
				logger.info("checking for existing tag '" + tag.name + "' in database" );
				pst.setString(1,tag.name);
				pst.execute();
				rset = pst.getResultSet();
				if(rset != null){
					if(rset.next()){
						tag.id = rset.getInt(1);
					}
				}
				rset.close();
			}
			//3. Check that the deployment_download record exists and is linked to the correct receiver
			Integer projectRolePersonId = 0;
			Integer receiverDeploymentId = 0;
			Integer deploymentDownloadId;
			if(downloadFid != null){
				if(downloadFid.startsWith(DOWNLOAD_ID_PREFIX)){
					try{
						deploymentDownloadId = Integer.valueOf(downloadFid.substring(DOWNLOAD_ID_PREFIX.length()));
					}catch(NumberFormatException e){
						throw new Exception("the downloadId parameter passed is invalid, it must end with a number", e);
					}
					Statement smt = conn.createStatement();
					//a receiver deployment must have a project_role_person associated with it,
					//extract this to be used for any new tags added.  
					rset = smt.executeQuery("SELECT RECEIVER_DEPLOYMENT.DEPLOYMENT_ID, RECEIVER_DEPLOYMENT.PROJECT_ROLE_PERSON_ID FROM " +
							"DEPLOYMENT_DOWNLOAD, RECEIVER_DEPLOYMENT, DEVICE WHERE " +
							"DEPLOYMENT_DOWNLOAD.DEPLOYMENT_ID = RECEIVER_DEPLOYMENT.DEPLOYMENT_ID AND " +
							"RECEIVER_DEPLOYMENT.DEVICE_ID = DEVICE.DEVICE_ID AND " +
							"DEVICE.CODE_NAME = '" + receiverName + "' AND " +
							"DOWNLOAD_ID = "+ deploymentDownloadId );
					if(rset != null){ 
						if(rset.next()){
							receiverDeploymentId = rset.getInt(1);
							projectRolePersonId = rset.getInt(2);
							if(projectRolePersonId == 0){
								logger.info("could find a project_role_person record id to use for adding unknown tags to database"); 
							}
						}else{
							throw new Exception("a matching 'deployment download' record has not been found in the database for '"+ deploymentDownloadId + "'" +
									" being linked to a 'receiver deployment' record with receiver device code named '" + receiverName + "'"); 
						}
					}
					rset.close();
				}else{
					throw new Exception("the downloadId parameter passed is invalid, it must start with '" + DOWNLOAD_ID_PREFIX +  "'");
				}
			}else{
				throw new Exception("the downloadId parameter passed is null");
			}
			//4. Find or create new tag records in the database.
			HashMap<String,Tag> newTags = new HashMap<String,Tag>();
			pst = conn.prepareStatement("INSERT INTO DEVICE (DEVICE_ID,DEVICE_TYPE_ID,CODE_NAME,PROJECT_ROLE_PERSON_ID)" +
					" VALUES (DEVICE_SERIAL.NEXTVAL,2,?,?)",Statement.RETURN_GENERATED_KEYS);
			for(Iterator<Entry<String,Tag>> i = tags.entrySet().iterator(); i.hasNext();){
				Tag tag = i.next().getValue();
				if(tag.id == 0){
					pst.setString(1,tag.name);
					pst.setInt(2,projectRolePersonId);
					pst.execute();
					rset = pst.getGeneratedKeys();
					if(rset != null){ 
						if(rset.next()){
							Statement smt = conn.createStatement();
							ResultSet newId = smt.executeQuery("SELECT DEVICE_ID FROM DEVICE WHERE ROWID = '" + rset.getString(1) + "'");
							if(newId != null && newId.next()){
								tag.id = Integer.valueOf(newId.getInt(1));
							}else{
								throw new Exception("could not retreive id of new tag device just created");
							}
							newId.close();
						}
					}
					rset.close();
					newTags.put(tag.name,tag);
				}
			}
			//5. Create new detection records in the database.
			pst = conn.prepareStatement("INSERT INTO DETECTION (DETECTION_ID, DEPLOYMENT_ID, DOWNLOAD_ID, TAG_ID, DETECTION_TIMESTAMP)" +
			" VALUES (DETECTION_SERIAL.NEXTVAL,"+ receiverDeploymentId +","+ deploymentDownloadId +",?,?)");
			for(Iterator<Detection> i = detections.iterator(); i.hasNext();){
				Detection detection = i.next();
				pst.setInt(1, detection.tag.id);
				pst.setTimestamp(2, detection.timestamp);
				pst.execute();
			}
			//happy ending
			conn.commit();
			conn.close();
			//6. Prepare a report(log) file to be sent back to the submitter and data manager.
			logger.info("deployment download id = " + deploymentDownloadId);
			logger.info("receiver deployment id = " + receiverDeploymentId);
			logger.info("project role person id = " + projectRolePersonId);
			logger.info("new tags added:");
			for(Iterator<Entry<String,Tag>> i = tags.entrySet().iterator(); i.hasNext();){
				Tag tag = i.next().getValue();
				logger.info("tag code_name = " + tag.name + ", id = " + tag.id);
			}
			logger.info("total new detections added = " + detections.size());
		} catch (SQLException se) {
			logger.fatal("SQL Exception:");
			// Loop through the SQL Exceptions
			while (se != null) {
				logger.fatal("State  : " + se.getSQLState());
				logger.fatal("Message: " + se.getMessage());
				logger.fatal("Error  : " + se.getErrorCode());
				se = se.getNextException();
			}
			try{
				if(conn != null && !conn.isClosed()){
					conn.rollback();
				}
			}catch(SQLException e){
				logger.warn("attempted rollback produced an SQLException", e);
				return;
			}
		} catch (Exception e) {
			logger.fatal(e);
			return;
		}
	}
	
	
	private boolean validHeader(String line){
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
		if(args.length == 6){
			processor.doProcessing(args[0],args[1],args[2],args[3],args[4], args[5]);
		}else{
			processor.logger.error("6 parameters are needed: download id, file path, jdbc driver class, connection string, username and password");
		}

	}
	
	private class Detection{
		
		public Timestamp timestamp;	
		public Tag tag;
		
		public Detection(Tag tag, Timestamp detectionDatetime){
			assert(tag != null);
			assert(detectionDatetime != null);
			this.tag = tag;
			this.timestamp = detectionDatetime;
		}
	}
	
	private class Tag{
		
		public String name;
		public int id = 0;
		public Integer detectionCount = 1;
		
		public Tag(String tagName){
			assert(tagName != null);
			this.name = tagName;
		}
	}
}
