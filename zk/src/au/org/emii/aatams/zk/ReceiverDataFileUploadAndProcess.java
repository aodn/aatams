package au.org.emii.aatams.zk;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Window;
import org.zkoss.zul.Fileupload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.util.media.Media;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.File;

public class ReceiverDataFileUploadAndProcess extends Window implements AfterCompose, Composer {

	String download_id;
	String downloads_path;

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

		/*add following to web.xml under <web-app>
	    <context-param>
	      <param-name>downloads_path</param-name>
	      <param-value>c:/temp</param-value>
	    </context-param>	
		*/
		downloads_path = this.getDesktop().getWebApp().getInitParameter("downloads_path");
		if(downloads_path == null){
			logger.fatal("global context parameter 'downloads_path' not found");
		}
	}

	public void onClick$FileUpload(Event event) {
		// get cookie
		download_id = null;
		Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent().getNativeRequest()).getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("deployment_download_id")) {
				download_id = cookies[0].getValue();
				break;
			}
		}
		System.out.println(download_id);
		if (download_id != null) {
			try {
				Media[] media = Fileupload.get("Specify file(s) located in your local system","Uploads for " + download_id,-1);
				if (media != null) {
					try {
						for(int i=0; i<media.length; i++){
							String filename = media[i].getName();
							byte[] file = media[i].getByteData();
							ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
							stream.write(file);
							File dir = new File(this.downloads_path + "/" + download_id);
							if (!dir.exists())
								dir.mkdir();
							dir.setWritable(true);
							FileOutputStream outputStream = new FileOutputStream(this.downloads_path + "/" + download_id + "/" + filename);
							stream.writeTo(outputStream);
						}
					} catch (Exception e) {
						logger.error(e);
					}
				}
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}
}
