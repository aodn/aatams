package au.org.emii.aatams.zk.tree;

import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Button;
import org.zkoss.zul.Box;
import org.zkoss.zul.Label;
import org.zkoss.zk.ui.Path;

import au.org.emii.aatams.zk.tree.DetectionsTree;
import au.org.emii.aatams.zk.tree.DetectionsTree.NodeType;

import org.apache.log4j.Logger;

/**
 * Detections download tree menu item renderer
 * 
 * @author stephenc
 *
 */
public class DetectionsTreeMenuRenderer implements TreeitemRenderer {
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
		cell = new Treecell();
		DetectionsTreeBaseNode nodeRef = (DetectionsTreeBaseNode)node;
		if(nodeRef.getNodeType() != NodeType.ALL_INSTALLATIONS &&
			nodeRef.getNodeType() != NodeType.ALL_TAGS && 
			nodeRef.getNodeType() != NodeType.ALL_CLASSIFICATIONS &&
			!(nodeRef.getMode() == DetectionsTreeMode.CLASSIFICATIONS && 
				nodeRef.getName().startsWith("UNKNOWN"))){ 
			Box btn = new Box();
			Label label = new Label("DOWNLOAD");
			btn.appendChild(label);
			btn.setSclass("download");
			label.addForward("onClick", treeitem.getTree(), "onClick", node);
			cell.appendChild(btn);
			cell.setParent(row);
		}
	}
}
