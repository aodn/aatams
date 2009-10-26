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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.ArrayList;


import au.org.emii.aatams.file.processing.AcousticReceiverFileProcessor;

public class ReceiverDataFileUploadAndProcess extends Window implements AfterCompose, Composer {

	Button file_upload;
	Listbox messages;
	String downloads_path = null;
	String download_id = null;

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

		//default logging
		BasicConfigurator.configure();

		/*
		 * add following to web.xml under <web-app> <context-param>
		 * <param-name>downloads_path</param-name> <param-value>c:/temp</param-value>
		 * </context-param>
		 */
		downloads_path = this.getDesktop().getWebApp().getInitParameter("downloads_path");
		if (downloads_path == null) {
			logger.fatal("global context parameter 'downloads_path' not found");
			messages.appendChild(new Listitem("data file uploading has not be configured correctly, please contact eMII"));
		}
		
		download_id = Executions.getCurrent().getParameter("fid");

	}

	public void onClick$file_upload(Event event) {
		

		AcousticReceiverFileProcessor processor = new AcousticReceiverFileProcessor();
		// get cookie
		/*String download_id = null;
		Integer highest = 0;
		Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getCookies();
		for (int i = 0; i < cookies.length; i++) {
			logger.info("found cookie: "+ cookies[i].getName() + ", value=" + cookies[i].getValue());
			if (cookies[i].getName().equals("deployment_download_id")) {
				if(Integer.valueOf(cookies[i].getValue().replace("aatams.deployment_download.", "")) > highest){
					download_id = cookies[i].getValue();
				}
				break;
			}
		}*/
		// upload the files
		if (download_id != null) {
			String csvFile = null;
			messages.appendChild(new Listitem("processing files for deployment download id:" + download_id));
			try {
				Media[] media = Fileupload.get("Specify file(s) located in your local system", "Uploads for " + download_id, -1);
				if (media != null) {
					try {
						for (int i = 0; i < media.length; i++) {
							String filename = media[i].getName();
							File dir = new File(this.downloads_path + "/" + download_id);
							if (!dir.exists())
								dir.mkdir();
							dir.setWritable(true);
							String filePath = this.downloads_path + "/" + download_id + "/" + filename;
							if(media[i].inMemory()){
								if(media[i].isBinary()){
									byte[] file = media[i].getByteData();
									ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
									stream.write(file);
									FileOutputStream outputStream = new FileOutputStream(filePath);
									stream.writeTo(outputStream);
									stream.flush();
									stream.close();
								}else{
									String file = media[i].getStringData();
									FileOutputStream stream = new FileOutputStream(filePath);
									stream.write(file.getBytes());
									stream.flush();
									stream.close();
								}
							}else{
								if(media[i].isBinary()){
									java.io.BufferedInputStream buf = new java.io.BufferedInputStream( media[i].getStreamData());
									java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new FileOutputStream(filePath)); 
									byte[] data = new byte[1024] ; 
									while( buf.read(data) != -1 ){
										out.write(data);
									}
									out.flush();
									out.close();
								}else{
									java.io.BufferedReader reader = new java.io.BufferedReader( media[i].getReaderData());
									java.io.PrintWriter writer = new java.io.PrintWriter(new File(filePath));
									String line;
									while((line = reader.readLine()) != null){
										writer.write(line);
									}
									writer.flush();
									writer.close();
								}
							}
							if (filename.toLowerCase().endsWith(".csv")){
								csvFile = this.downloads_path + "/" + download_id + "/" + filename;
							}
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
			if(csvFile != null){
				try {
					ArrayList<String> msgs = new ArrayList<String>();
					processor.doProcessing(download_id, csvFile, "oracle.jdbc.driver.OracleDriver",
							"jdbc:oracle:thin:@obsidian.bluenet.utas.edu.au:1521:orcl", "AATAMS", "boomerSIMS", msgs);
					//output the messages
					for (int i = 0; i < msgs.size(); i++) {
						messages.appendChild(new Listitem(msgs.get(i)));
					}
				} catch (Exception e) {
					logger.error(e);
					messages.appendChild(new Listitem("file upload has failed, please contact eMII for details"));
					return;
				}
			}else{
				messages.appendChild(new Listitem("One of the files uploaded must be a csv file with filename ending in '.csv'"));
			}
		} else {
			messages.appendChild(new Listitem("no cookie has been found to identify deployment download"));
		}
	}
}
