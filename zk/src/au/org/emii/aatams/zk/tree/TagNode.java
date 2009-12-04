package au.org.emii.aatams.zk.tree;

import java.util.ArrayList;
import java.util.logging.Level;

public class TagNode extends DetectionsTreeBaseNode {
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
				// find all receiver deployments assigned to this station
				rs = stmt.executeQuery("select deployment_id, concat(concat(device.code_name,'-'),to_char(receiver_deployment.deployment_timestamp,'YYYYMMDD')) as name\n"
								+ "from receiver_deployment, device\n"
								+ "where receiver_deployment.device_id = device.device_id\n"
								+ "and receiver_deployment.deployment_id in (\n"
								+ "select distinct(deployment_id)\n"
								+ "from download_tag_summary\n"
								+ "where device_id = " + this.getId() + ")\n"
								+ "order by name");
				while (rs.next()) {
					this.addChild(new ReceiverDeploymentNode(this, this.mode, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
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