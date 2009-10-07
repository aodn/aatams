package au.org.emii.aatams.zk.tree;


import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import au.org.emii.aatams.zk.tree.DetectedTagsTree;
import au.org.emii.aatams.zk.tree.DetectedTagsTree.NodeType;

import org.apache.log4j.Logger;

public class DetectedTagsTreeMenuItemRenderer implements TreeitemRenderer {
	protected Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void render(Treeitem treeitem, Object node) throws Exception {
		//setup treerow
		Treerow row = null;	
		if (treeitem.getTreerow() == null) {
			row = new Treerow();
			row.setParent(treeitem);
		}
		else {
			row = treeitem.getTreerow();
			row.getChildren().clear();
		}
		//add tree cell with label to row
		Treecell cell = new Treecell();
		cell.setLabel(node.toString());
		cell.setParent(row);
		//decide what to do in row
		//if (DetectedTagsTree.getNodeType(node).equals(NodeType.TAG)) {
		//} else {
		//	throw new Exception("Unknown Node Type encountered");
		//}
	}
	


}
