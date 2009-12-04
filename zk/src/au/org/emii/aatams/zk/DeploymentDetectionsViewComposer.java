package au.org.emii.aatams.zk;

import java.util.logging.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import java.util.zip.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;


import au.org.emii.aatams.zk.tree.*;
import au.org.emii.aatams.data.Detections;

public class DeploymentDetectionsViewComposer  
	extends Window implements AfterCompose, Composer {

	Tree tree;
	DetectionsTree model= new DetectionsTree(new RootNode(DetectionsTreeMode.DEPLOYMENTS));
	DetectionsTreeMenuRenderer renderer = new DetectionsTreeMenuRenderer();
	DownloadEventListener listener;

	private static final long serialVersionUID = 1L;

	/**
	 * Log4j instance
	 */
	protected Logger logger = Logger.getLogger(this.getClass().getName());

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

		// auto forward
		Components.addForwards(this, this);
		
		//initialise tree
		tree.setTreeitemRenderer(renderer);
		tree.setModel(model);
		listener = new DownloadEventListener(tree);
		tree.addEventListener("onClick",listener);

	}

	private class DownloadEventListener implements EventListener{
		
		Tree tree;
		
		public DownloadEventListener(Tree tree){
			this.tree = tree;
		}
		
		public void onEvent(Event event) throws Exception {
			if(event.getData() != null){
				DetectionsTreeBaseNode node = (DetectionsTreeBaseNode)event.getData(); 
				logger.info(String.valueOf(node.getId()));
				logger.info(node.toString());
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
				ZipOutputStream zipfile = new ZipOutputStream(outStream); 
				ZipEntry zipentry = new ZipEntry(node.getQualifiedName().replace(" ", "_")+".csv");  
				zipfile.putNextEntry(zipentry); 
				Detections detections = new Detections(node);
				detections.write(zipfile);  
				zipfile.close(); 
				Filedownload.save(new ByteArrayInputStream(outStream.toByteArray()), "application/zip", "AATAMS_detections.zip");
			}
		}
	}
}