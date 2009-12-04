package au.org.emii.aatams.zk.tree;

import javax.sql.DataSource;

import java.util.logging.Logger;

public class RootNode extends DetectionsTreeBaseNode {
	
	private DetectionsTree parentTree = null;
	
	public RootNode(DetectionsTreeMode mode) {
		super(null, mode, 0, "");
		this.type = DetectionsTree.NodeType.ROOT;
		switch(mode){
		case DEPLOYMENTS:
			super.addChild(new AllInstallationsNode(this, mode, 0, "Installations"));
			break;
		case CLASSIFICATIONS:
			super.addChild(new AllClassificationsNode(this, mode, 0, "Families"));
			break;
		case TAGS:
			super.addChild(new AllTagsNode(this, mode, 0, "All Tags"));
			break;
		}
	}
	
	protected DetectionsTreeBaseNode addChild(DetectionsTreeBaseNode node) {
		return node;
	}

	public void setParentTree(DetectionsTree tree){
		this.parentTree = tree;
	}
	
	public DetectionsTreeBaseNode getParent(){
		return null;
	}
	
	@Override
	public DataSource getDataSource(){
		return this.parentTree.getDataSource();
	}
	
	public String getQualifiedName() {
		return "ROOT";
	}
	
	protected void init() {
	}
	
	@Override
	public Logger getLogger(){
		return this.parentTree.logger;
	}
}
