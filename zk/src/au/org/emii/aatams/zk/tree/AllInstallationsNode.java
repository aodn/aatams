package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;

public class AllInstallationsNode extends DetectionsTreeBaseNode {

	public AllInstallationsNode(DetectionsTreeBaseNode parent, DetectionsTreeMode mode , long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.ALL_INSTALLATIONS;
	}

	@Override
	public DetectionsTreeBaseNode getParent() {
		return null;
	}

	@Override
	protected void init() {
		try {
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select installation_id, name from aatams.installation order by name");
			while (rs.next()) {
				this.addChild(new InstallationNode(this, this.mode, rs.getLong(1), rs.getString(2)));
			}
			rs.close();
			stmt.close();
			conn.close();
			this.initialised = true;
		} catch (Exception e) {
			this.getLogger().log(Level.SEVERE,"",e);
		}
	}
	
	public String getQualifiedName() {
		return "";
	}
}
