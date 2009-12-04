package au.org.emii.aatams.zk.tree;

import java.util.logging.Level;

public class GenusNode extends DetectionsTreeBaseNode {

	public GenusNode(DetectionsTreeBaseNode parent, DetectionsTreeMode mode, long identifier, String name) {
		super(parent, mode, identifier, name);
		this.type = DetectionsTree.NodeType.GENUS;
	}

	@Override
	protected void init() {
		try{
			conn = this.getDataSource().getConnection();
			stmt = conn.createStatement();
			if(this.getName().equals("UNKNOWN GENUS")){
				rs = stmt.executeQuery("select classification_id, name from classification\n"
						+ "where classification_level_id = 12 and parent_classification_id = 0 order by name");
				while(rs.next()) {
					this.addChild(new SpeciesNode(this, this.mode, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				this.addChild(new SpeciesNode(this, this.mode, 0, "UNKNOWN SPECIES"));
			}else{
				rs = stmt.executeQuery("select classification_id, name from classification\n"
						+ "where classification_level_id = 12 and parent_classification_id = " + this.getId() + " order by name");
				while(rs.next()) {
					this.addChild(new SpeciesNode(this, this.mode, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
			}
			stmt.close();
			conn.close();
			this.initialised = true;
		}catch(Exception e){
			this.getLogger().log(Level.SEVERE,"",e);
		}
	}
}
