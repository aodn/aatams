package au.org.emii.aatams.zk.tree;

import java.util.ArrayList;
import java.util.logging.Level;

public class TagNode extends DetectionsTreeBaseNode {
	
	public TagNode(ReceiverDeploymentNode parent, DetectionsTreeMode mode, long identifier, String name, int detections) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.TAG;
		this.detection_count = detections;
	}
	
	public TagNode(ReceiverDeploymentNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.TAG;
	}
	
	public TagNode(AllTagsNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.TAG;
	}
	
	public TagNode(SpeciesNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.TAG;
	}
	
	protected void init() {
		switch(this.mode){
		case DEPLOYMENTS:
			this.initialised = true;
			break;
		case TAGS:
			try {
				conn = this.getDataSource().getConnection();
				stmt = conn.createStatement();
				java.sql.Statement stmt1 = conn.createStatement();
				// find all receiver deployments where this tag has been detected
				rs = stmt.executeQuery("select deployment_id, device.code_name ||'-' || to_char(receiver_deployment.deployment_timestamp,'YYYYMMDD') as name\n"
								+ "from aatams.receiver_deployment, aatams.device\n"
								+ "where receiver_deployment.device_id = device.device_id\n"
								+ "and receiver_deployment.deployment_id in (\n"
								+ "select distinct(deployment_id)\n"
								+ "from aatams.download_tag_summary\n"
								+ "where device_id = " + this.getId() + ")\n"
								+ "order by name");
				while (rs.next()) {
					//get the detection count for this deployment-tag combo
					java.sql.ResultSet rs1 = stmt1.executeQuery("select sum(detection_count)\n"
							+ "from aatams.download_tag_summary\n"
							+ "where device_id = " + this.getId() + " and\n"
							+ "deployment_id = " + rs.getLong(1));
					rs1.next();
					this.addChild(new ReceiverDeploymentNode(this, this.mode, rs.getLong(1), rs.getString(2), rs1.getInt(1)));
					rs1.close();
				}
				rs.close();
				stmt.close();
				stmt1.close();
				conn.close();
				this.initialised = true;
			} catch (Exception e) {
				this.getLogger().log(Level.SEVERE,"",e);
			}
			break;
		case CLASSIFICATIONS:
			this.initialised = true;
			break;
		}
	}
}