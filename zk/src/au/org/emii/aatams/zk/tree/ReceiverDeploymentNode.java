package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;

public class ReceiverDeploymentNode extends DetectionsTreeBaseNode {
	
	public ReceiverDeploymentNode(InstallationNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.DEPLOYMENT;
	}
	
	public ReceiverDeploymentNode(StationNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.DEPLOYMENT;
	}
	
	public ReceiverDeploymentNode(TagNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.DEPLOYMENT;
	}

	protected void init() {
		switch(this.mode){
		case DEPLOYMENTS:
			try {
				conn = this.getDataSource().getConnection();
				stmt = conn.createStatement();
				// find all distinct tags detected for deployment.
				rs = stmt.executeQuery("select device.device_id, device.code_name from deployment_download, download_tag_summary, device where " +
						"deployment_download.deployment_id = " + this.getId() + " and " + 
						"download_tag_summary.download_id = deployment_download.download_id and " +
						"device.device_id = download_tag_summary.device_id " +
						"group by device.device_id, device.code_name " +
						"order by device.code_name");
				while (rs.next()) {
					this.addChild(new TagNode(this, this.mode, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				conn.close();
				this.initialised = true;
			} catch (Exception e) {
				this.getLogger().log(Level.SEVERE,"",e);
			}
			break;
		case TAGS:
			this.initialised = true;
			break;
		case CLASSIFICATIONS:
			break;
		}
		
		

	}
}
