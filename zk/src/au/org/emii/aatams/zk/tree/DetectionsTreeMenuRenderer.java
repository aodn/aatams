package au.org.emii.aatams.zk.tree;

import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Box;
import org.zkoss.zul.Label;

import au.org.emii.aatams.zk.tree.DetectionsTree.NodeType;
import au.org.emii.aatams.zk.tree.DetectionsTreeMode;

import org.apache.log4j.Logger;

/**
 * Detections download tree menu item renderer
 * 
 * @author stephenc
 *
 */
public class DetectionsTreeMenuRenderer implements TreeitemRenderer {
	protected Logger logger = Logger.getLogger(this.getClass());

	DetectionsTreeMode mode = null;
	
	private DetectionsTreeMenuRenderer(){}
	
	public DetectionsTreeMenuRenderer(DetectionsTreeMode mode){
		this.mode = mode;
	}
	
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
		DetectionsTreeBaseNode nodeRef = (DetectionsTreeBaseNode)node;
		//find Node Id and detection count if present
		String detections = (nodeRef.detection_count != null) ? ", " + String.valueOf(nodeRef.detection_count) + " detections" : "";
	    if(nodeRef.detection_count != null && nodeRef.detection_count == 1) detections = detections.replace("s", "");
		String id = "";
		if(!(nodeRef instanceof AllClassificationsNode ||
			nodeRef instanceof AllInstallationsNode	|| 
			nodeRef instanceof AllTagsNode)){
			id = " (ID="+ nodeRef.getId()+ detections + ")";
		}
		cell.setLabel(nodeRef.getName() + id);
		cell.setParent(row);
		cell = new Treecell();
		if(nodeRef.getNodeType() != NodeType.ALL_INSTALLATIONS &&
			nodeRef.getNodeType() != NodeType.ALL_TAGS && 
			nodeRef.getNodeType() != NodeType.ALL_CLASSIFICATIONS &&
			!(nodeRef.getMode() == DetectionsTreeMode.CLASSIFICATIONS && 
				nodeRef.getName().startsWith("UNKNOWN"))){
			//decide if a download button is needed based on mode
			boolean addDownload = true;
			if(this.mode == DetectionsTreeMode.TAGS){
				//tag with no child receivers
				if(nodeRef.getNodeType() == NodeType.TAG && nodeRef.isLeaf() ){
					addDownload = false;
				}
			}
			if(this.mode == DetectionsTreeMode.DEPLOYMENTS){
				//unless a tag node, if no children then no detections
				if(nodeRef.isLeaf() && !(nodeRef.getNodeType() == NodeType.TAG)){
					addDownload = false;
				}
			}
			if(addDownload){
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
}
