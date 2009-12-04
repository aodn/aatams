package au.org.emii.aatams.zk;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Window;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zk.ui.Executions;
import org.zkoss.util.media.Media;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.*;

/**
 * Composer for upload.zul,
 * 
 * Receives single zip archive and extracts it to 
 */
import au.org.emii.aatams.file.processing.AcousticReceiverFileProcessor;

public class ReceiverDataFileUploadAndProcess extends Window implements AfterCompose, Composer {

	private Button file_upload;
	private Listbox messages;
	private String download_id = null;
	private boolean config_ok = true;
	private String DATA_FILE_UPLOADS_HOME_DIR = null;
	private String DEPLOYMENT_DOWNLOAD_FID_PREFIX = null;
	private String REPORT_SENDER_NAME = null;
	private String REPORT_SENDER_EMAIL = null;
	private String SMTP_HOST = null;
	private Integer SMTP_PORT = 25;
	private String SMTP_USER = null;
	private String SMTP_PASSWORD = null;
	private String HOME_URI = null;

	private static final long serialVersionUID = 1L;

	/**
	 * Log4j instance
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * This does nothing but is required to keep zk runtime happy - if you
	 * remove it you will get classcast errors when the page loads because we
	 * are required to implement the do Composer interface even though we
	 * already implement AfterCompose
	 */
	public void doAfterCompose(Component arg0) throws Exception {
	}

	/**
	 * Perform the autowiring - courtesy of the zk mvc 3 tutorial
	 */
	public void afterCompose() {
		// wire variables
		Components.wireVariables(this, this);

		// NO need to register onXxx event listeners

		// auto forward
		Components.addForwards(this, this);

		// default logging
		BasicConfigurator.configure();

		// get config details for sending reports
		DATA_FILE_UPLOADS_HOME_DIR = this.getDesktop().getWebApp().getInitParameter("DATA_FILE_UPLOADS_HOME_DIR");
		if (DATA_FILE_UPLOADS_HOME_DIR == null) {
			logger.fatal("global context parameter 'DATA_FILE_UPLOADS_HOME_DIR' not found");
			config_ok = false;
		}
		DEPLOYMENT_DOWNLOAD_FID_PREFIX = this.getDesktop().getWebApp().getInitParameter("DEPLOYMENT_DOWNLOAD_FID_PREFIX");
		if (DEPLOYMENT_DOWNLOAD_FID_PREFIX == null) {
			logger.fatal("global context parameter 'DEPLOYMENT_DOWNLOAD_FID_PREFIX' not found");
			config_ok = false;
		}
		REPORT_SENDER_NAME = this.getDesktop().getWebApp().getInitParameter("REPORT_SENDER_NAME");
		if (REPORT_SENDER_NAME == null) {
			logger.fatal("global context parameter 'REPORT_SENDER_NAME' not found");
			config_ok = false;
		}
		REPORT_SENDER_EMAIL = this.getDesktop().getWebApp().getInitParameter("REPORT_SENDER_EMAIL");
		if (REPORT_SENDER_EMAIL == null) {
			logger.fatal("global context parameter 'REPORT_SENDER_EMAIL' not found");
			config_ok = false;
		}
		SMTP_HOST = this.getDesktop().getWebApp().getInitParameter("SMTP_HOST");
		if (SMTP_HOST == null) {
			logger.fatal("global context parameter 'SMTP_HOST' not found");
			config_ok = false;
		}
		try {
			SMTP_PORT = Integer.valueOf(this.getDesktop().getWebApp().getInitParameter("SMTP_PORT"));
			if (SMTP_PORT == null) {
				logger.fatal("global context parameter 'SMTP_PORT' not found");
				config_ok = false;
			}
		} catch (NumberFormatException e) {
			logger.fatal("exception parsing SMTP_PORT global context parameter", e);
			config_ok = false;
		}
		SMTP_USER = this.getDesktop().getWebApp().getInitParameter("SMTP_USER");
		if (SMTP_USER == null) {
			logger.fatal("global context parameter 'SMTP_USER' not found");
			config_ok = false;
		}
		SMTP_PASSWORD = this.getDesktop().getWebApp().getInitParameter("SMTP_PASSWORD");
		if (DATA_FILE_UPLOADS_HOME_DIR == null) {
			logger.fatal("global context parameter 'SMTP_PASSWORD' not found");
			config_ok = false;
		}
		HOME_URI = this.getDesktop().getWebApp().getInitParameter("HOME_URI");
		if (DATA_FILE_UPLOADS_HOME_DIR == null) {
			logger.fatal("global context parameter 'downloads_path' not found");
			config_ok = false;
		}
		// extract the download_fid of interest from url params
		download_id = Executions.getCurrent().getParameter("fid");

	}

	public void onClick$file_upload(Event event) {
		// upload the files
		if (download_id != null && config_ok) {
			String zip_file = null;
			String csv_file = null;
			String vrl_file = null;
			messages.appendChild(new Listitem("Upload and process files for deployment download id:" + download_id));
			try {
				// set 5 as the max number of files to upload
				Media[] media = Fileupload.get("Please select a zip archive file containing a vrl file AND it's matching Vue csv export file from your file system, then click the 'Upload' button",
								"File upload for " + download_id, 1);
				if (media != null) {
					try {
						for (int i = 0; i < media.length; i++) {
							String filename = media[i].getName();
							File dir = new File(this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id);
							if (!dir.exists())
								dir.mkdir();
							dir.setWritable(true);
							String filePath = this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id + "/" + filename;
							if (media[i].inMemory()) {
								if (media[i].isBinary()) {
									logger.info("processing in-memory, binary file " + media[i].getName());
									byte[] file = media[i].getByteData();
									ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
									stream.write(file);
									FileOutputStream outputStream = new FileOutputStream(filePath);
									stream.writeTo(outputStream);
									stream.flush();
									stream.close();
								} else {
									logger.info("processing in-memory, character file " + media[i].getName());
									String file = media[i].getStringData();
									FileOutputStream stream = new FileOutputStream(filePath);
									stream.write(file.getBytes());
									stream.flush();
									stream.close();
								}
							} else {
								if (media[i].isBinary()) {
									logger.info("processing streamed, binary file " + media[i].getName());
									java.io.BufferedInputStream buf = new java.io.BufferedInputStream(media[i].getStreamData());
									java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new FileOutputStream(filePath));
									byte[] data = new byte[1024];
									while (buf.read(data) != -1) {
										out.write(data);
									}
									out.flush();
									out.close();
								} else {
									logger.info("processing streamed, character file " + media[i].getName());
									java.io.BufferedReader reader = new java.io.BufferedReader(media[i].getReaderData());
									java.io.PrintWriter writer = new java.io.PrintWriter(new File(filePath));
									String line;
									while ((line = reader.readLine()) != null) {
										writer.write(line);
										writer.write("\n");
									}
									writer.flush();
									writer.close();
								}
							}
							if (filename.toLowerCase().endsWith(".zip")) {
								zip_file = this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id + "/" + filename;
							}
							//if (filename.toLowerCase().endsWith(".csv")) {
							//	csv_file = this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id + "/" + filename;
							//}
							//if (filename.toLowerCase().endsWith(".vrl")) {
							//	vrl_file = this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id + "/" + filename;
							//}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e);
						messages.appendChild(new Listitem("file upload has failed, please contact eMII for details"));
						return;
					}
				}
			} catch (InterruptedException e) {
				logger.error(e);
				messages.appendChild(new Listitem("file upload has failed, please contact eMII for details"));
				return;
			}
			// now do the file processing
			if (zip_file != null) {
				try {
					byte[] buf = new byte[1024];
					ZipInputStream zipinputstream = null;
					ZipEntry zipentry;
					zipinputstream = new ZipInputStream(new FileInputStream(zip_file));
					zipentry = zipinputstream.getNextEntry();
					while (zipentry != null) {
						// for each entry to be extracted
						String entryName = zipentry.getName();
						System.out.println("entryname " + entryName);
						int n;
						FileOutputStream fileoutputstream;
						File newFile = new File(entryName);
						String directory = newFile.getParent();
						if (directory == null) {
							if (newFile.isDirectory()){
								messages.appendChild(new Listitem("all zip entries must be files, not directories"));
								break;
							}
						}
						fileoutputstream = new FileOutputStream(this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id + "/" + entryName);
						if (entryName.toLowerCase().endsWith(".csv")) {
							csv_file = this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id + "/" + entryName;
						}
						if (entryName.toLowerCase().endsWith(".vrl")) {
							vrl_file = this.DATA_FILE_UPLOADS_HOME_DIR + "/" + download_id + "/" + entryName;
						}
						while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
							fileoutputstream.write(buf, 0, n);
						}
						fileoutputstream.close();
						zipinputstream.closeEntry();
						zipentry = zipinputstream.getNextEntry();
					}
					zipinputstream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				messages.appendChild(new Listitem("The file uploaded must have a filename ending in '.zip'"));
				return;
			}
			if (csv_file != null && vrl_file != null) {
				try {
					AcousticReceiverFileProcessor processor = new AcousticReceiverFileProcessor(DEPLOYMENT_DOWNLOAD_FID_PREFIX, REPORT_SENDER_NAME,
							REPORT_SENDER_EMAIL, SMTP_HOST, SMTP_PORT, SMTP_USER, SMTP_PASSWORD, HOME_URI);
					ArrayList<String> msgs = new ArrayList<String>();
					DataSource ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/aatams");
					processor.doProcessing(ds.getConnection(), download_id, csv_file, msgs);
					// output the messages
					for (int i = 0; i < msgs.size(); i++) {
						messages.appendChild(new Listitem(msgs.get(i)));
					}
				} catch (Exception e) {
					logger.error(e);
					messages.appendChild(new Listitem("File upload has failed, please contact eMII fordetails"));
					return;
				}
			} else {
				messages.appendChild(new Listitem(
						"One of the files uploaded must be a csv file with filename ending in '.csv' and another a vrl file with filename ending in '.vrl'"));
			}
		} else {
			logger.error("download 'fid' param not found or configuration is incorrect");
			messages.appendChild(new Listitem("File upload has failed, please contact eMII for details"));
		}
	}
}
