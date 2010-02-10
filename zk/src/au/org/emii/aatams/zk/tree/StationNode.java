package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;


public  class StationNode extends DetectionsTreeBaseNode {
	
	public StationNode(InstallationNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent,mode, identifier, name);
		this.type = DetectionsTree.NodeType.STATION;
	}

	protected void init() {
		try {
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			// find all receiver deployments assigned to this station
			rs = stmt.executeQuery("select deployment_id, device.code_name || '-' || to_char(receiver_deployment.deployment_timestamp,'YYYYMMDD') as name\n"
							+ "from aatams.receiver_deployment, aatams.device\n"
							+ "where receiver_deployment.device_id = device.device_id and receiver_deployment.station_id = "
							+ this.getId() + "\n"
							+ "order by device.code_name");
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
	}
}