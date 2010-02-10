package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;

public class AllClassificationsNode extends DetectionsTreeBaseNode {

	public AllClassificationsNode(DetectionsTreeBaseNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.ALL_CLASSIFICATIONS;
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
			//Families
			rs = stmt.executeQuery("select classification_id, name from aatams.classification\n"
					+ "where classification_level_id = 10 order by name");
			while(rs.next()) {
				this.addChild(new FamilyNode(this, this.mode, rs.getLong(1), rs.getString(2)));
			}
			rs.close();
			stmt.close();
			conn.close();
			//create an 'UNKNOWN' group for all Genii with no family and tags without a tag_release
			this.addChild(new FamilyNode(this, this.mode, 0, "UNKNOWN FAMILY"));
			this.initialised = true;
		} catch (Exception e) {
			this.getLogger().log(Level.SEVERE,"",e);
		}
	}
	public String getQualifiedName() {
		return "";
	}
}
