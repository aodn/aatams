package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;

public class FamilyNode extends DetectionsTreeBaseNode {

	public FamilyNode(DetectionsTreeBaseNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.FAMILY;
	}

	@Override
	protected void init() {
		try {
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			if(this.getName().equals("UNKNOWN FAMILY")){
				//find genus with unknown family
				rs = stmt.executeQuery("select classification_id, name from aatams.classification\n"
						+ "where classification_level_id = 11 and parent_classification_id = 0 order by name");
				while(rs.next()) {
					this.addChild(new GenusNode(this, this.mode, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				//any species with unknown genus and tags with no tag_release
				this.addChild(new GenusNode(this, this.mode, 0, "UNKNOWN GENUS"));
			}else{
				rs = stmt.executeQuery("select classification_id, name from aatams.classification\n"
						+ "where classification_level_id = 11 and parent_classification_id = " + this.getId() + " order by name");
				while(rs.next()) {
					this.addChild(new GenusNode(this, this.mode, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
			}
			stmt.close();
			conn.close();
			this.initialised = true;
		} catch (Exception e) {
			this.getLogger().log(Level.SEVERE,"",e);
		}
	}
}
