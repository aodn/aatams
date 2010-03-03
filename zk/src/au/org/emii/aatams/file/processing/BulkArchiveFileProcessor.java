package au.org.emii.aatams.file.processing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;

/**
 * Processes a zip archive that contains multiple files, in particular vrl, Vue
 * csv export for those vrl, rld and tag and deployment list csv files.
 * 
 * The tag and deployment files are standard formats to ensure completeness of
 * information.
 * 
 * If no tag file is present then tags will be read from the detections file and
 * new tag records created and belonging to the archive submitter.
 * 
 * @author stephen cameron
 */

public class BulkArchiveFileProcessor {

	AcousticReceiverFileProcessor processor = null;
	
	private BulkArchiveFileProcessor() throws Exception{
		throw new Exception("use public constructor");
	}

	public BulkArchiveFileProcessor(String downloadFidPrefix, String reportSenderName, String reportSenderEmailAddress, String smtpHost,
			int smtpPort, String smtpUser, String smtpPassword, String aatamsWebsiteUri) throws IllegalArgumentException {

		this.processor = new AcousticReceiverFileProcessor(downloadFidPrefix, reportSenderName, reportSenderEmailAddress, smtpHost, smtpPort, smtpUser,
				smtpPassword, aatamsWebsiteUri);

	}

	/**
	 * Processes a bulk tag-releases and/or deployment downloads zip archive.
	 * 
	 * @param database -
	 *            database connection
	 * @param filePath -
	 *            the full path to the zip archive file for processing
	 * @param messages -
	 *            array for message addition to return to user
	 */
	public void doProcessing(Connection connection, String filePath, List<String> messages) throws IllegalArgumentException {
		// validate parameters
		if (connection == null) {
			throw new IllegalArgumentException("database connection argument cannot be null");
		} else {
			try {
				if (connection.isClosed()) {
					throw new IllegalArgumentException("database connection argument is closed");
				}
			} catch (SQLException e) {
				throw new IllegalArgumentException("database connection argument is invalid", e);
			}
		}
		if (filePath == null) {
			throw new IllegalArgumentException("filePath argument cannot be null");
		}
		if (messages == null) {
			throw new IllegalArgumentException("messages argument cannot be null");
		}
		
		//extract the archive
				
		//read and validate the tags releases file if present, process data into database.		
	
		//see any and all required files are present for detections (ie. deployments list,
		//vrl, rld, detections(csv)).
		
		//read and validate the deployments file, this will have details for new deployments or
		//the id of an existing deployment owned by the archive submitter.
		
		//read and validate the detections file, split detections into 
		//separate lists per receiver, making sure all receivers are known.
		
		//for each receiver in deployments file submit the detections and tags lists for
		//processing into the database and sending of email reports.
		
	}

	/**
	 * Used for command-line testing outside web-server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		BulkArchiveFileProcessor processor = new BulkArchiveFileProcessor("aatams.deployment_download.",
				 "eMII", "stephen.cameron@utas.edu.au", "postoffice.utas.edu.au", 25, 
				 "sc04","66CoStHo", "http://test.emii.org.au/aatams");
		if (args.length == 5) {
			//example arguments
			//"c:\temp\archive.csv"
			//"oracle.jdbc.driver.OracleDriver"
			//"jdbc:oracle:thin:@obsidian.bluenet.utas.edu.au:1521:orcl" AATAMS boomerSIMS
			try{
				Class.forName(args[1]);
				Connection conn = DriverManager.getConnection(args[2], args[3], args[4]);
				ArrayList<String> messages = new ArrayList<String>();
				processor.doProcessing(conn, args[0], messages);
				for(int i = 0; i<messages.size(); i++){
					System.out.println(messages.get(i));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			System.out.println("6 parameters are needed: download id, file path, jdbc driver class, connection string, username and password");
		}
	}
}
