package au.org.emii.aatams.zk;

import java.util.logging.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import java.util.zip.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.sql.*;


import au.org.emii.aatams.zk.tree.*;
import au.org.emii.aatams.data.Detections;

public class ClassificationsDetectionsViewComposer  
	extends Window implements AfterCompose, Composer {

	Tree tree;
	DetectionsTree model= new DetectionsTree(new RootNode(DetectionsTreeMode.CLASSIFICATIONS));
	DetectionsTreeMenuRenderer renderer = new DetectionsTreeMenuRenderer(DetectionsTreeMode.CLASSIFICATIONS);
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
				logger.info(node.getNodeType().toString() + ", id=" + node.getId() + ", name=" +  node.getName());
				//find each tag in turn and add as a separate zip entry
				try{
					String qry = null;
					Connection conn = node.getDataSource().getConnection();
					Statement smt = conn.createStatement();
					ResultSet rs;
					switch(node.getNodeType()){
					case FAMILY:
						qry = "select display_tag_release.tag_id, device.code_name from aatams.display_tag_release, aatams.device\n"
								+ "where display_tag_release.tag_id = device.device_id\n" 
								+ "and family_id = " + node.getId(); 
						break;
					case GENUS:
						qry = "select display_tag_release.tag_id, device.code_name from aatams.display_tag_release, aatams.device\n"
								+ "where display_tag_release.tag_id = device.device_id\n"  
								+ "and genus_id = " + node.getId();
						break;
					case SPECIES:
						qry = "select display_tag_release.tag_id, device.code_name from aatams.display_tag_release, aatams.device\n"
							+ "where display_tag_release.tag_id = device.device_id\n"  
							+ "and species_id = " + node.getId(); //species
						break;
					case TAG:
						qry = "select device.device_id, device.code_name from aatams.device\n"
							+ "where device_id = " + node.getId(); //tag
						break;
					}
					rs = smt.executeQuery(qry);
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
					ZipOutputStream zipfile = new ZipOutputStream(outStream); 
					ZipEntry zipentry = null;
					while(rs.next()){
						zipentry = new ZipEntry(rs.getString(2)+".csv");  
						zipfile.putNextEntry(zipentry); 
						Detections detections = new Detections();
						detections.setTagId(rs.getLong(1));
						detections.setDataSource(node.getDataSource());
						detections.write(zipfile); 
					}
					rs.close(); 
					smt.close();
					conn.close();
					if(zipentry != null){
						zipfile.close();
						Filedownload.save(new ByteArrayInputStream(outStream.toByteArray()), 
								"application/zip", "AATAMS_detections.zip");
					}else{
						Messagebox.show("No tags found to report detection data");
					}
				}catch(SQLException e){
					logger.log(Level.SEVERE, "", e);
					throw new Exception("unexpected sql error, please see log for details");
				}
			}
		}
	}
}