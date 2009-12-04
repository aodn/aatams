package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;

public class SpeciesNode extends DetectionsTreeBaseNode {

	public SpeciesNode(DetectionsTreeBaseNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.SPECIES;
	}

	@Override
	protected void init() {
		try {
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			if(this.getName().equals("UNKNOWN CLASSIFICATION")){
				//all tags without a tag_release
				rs = stmt.executeQuery("select tag_device.device_id, tag_device.code_name\n"
						+ "from tag_device left outer join tag_release on tag_device.device_id = tag_release.device_id\n"
						+ "where tag_release.release_id is null or tag_release.classification_id is null order by tag_device.code_name");
				while(rs.next()) {
					this.addChild(new TagNode(this, this.mode, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
			}else{
				rs = stmt.executeQuery("select tag_device.device_id, tag_device.code_name\n"
						+ "from tag_device left outer join display_tag_release on tag_device.device_id = display_tag_release.device_id\n"
						+ "where display_tag_release.species_id = " + this.getId() + " order by tag_device.code_name");
				while(rs.next()) {
					this.addChild(new TagNode(this, this.mode, rs.getLong(1), rs.getString(2)));
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
