package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;

public class InstallationNode extends DetectionsTreeBaseNode {

	public InstallationNode(DetectionsTreeBaseNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.INSTALLATION;
	}

	@Override
	public RootNode getParent() {
		return (RootNode) this.parent;
	}

	protected void init() {
		try {
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			// find all stations in this installation that have deployments
			rs = stmt.executeQuery("select station_id, name, curtain_position from aatams.installation_station\n" + "where station_id in\n"
					+ "(select distinct(station_id) from aatams.receiver_deployment where installation_id = " + this.getId() + "\n"
					+ "and station_id is not null) order by curtain_position, name");
			while (rs.next()) {
				this.addChild(new StationNode(this, this.mode, rs.getLong(1), rs.getString(2)));
			}
			rs.close();
			// find all receiver deployments not assigned to a station for
			// installation
			rs = stmt.executeQuery("select deployment_id, device.code_name || '-' || to_char(receiver_deployment.deployment_timestamp,'YYYYMMDD') as name "
					+ "from aatams.receiver_deployment, aatams.device\n"
					+ "where receiver_deployment.device_id = device.device_id and\n"
					+ "receiver_deployment.installation_id = " + this.getId() + "\n"
					+ "and receiver_deployment.station_id is null order by name");
			while (rs.next()) {
				this.addChild(new ReceiverDeploymentNode(this, this.mode, rs.getLong(1), rs.getString(2)));
			}
			rs.close();
			stmt.close();
			conn.close();
			this.initialised = true;
		} catch (Exception e) {
			this.getLogger().log(Level.SEVERE, "", e);
		}
	}

}
