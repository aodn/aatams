package au.org.emii.aatams.zk.tree;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Treeitem;

public class DetectedTagsTreeMenuEventListener implements EventListener {
	
	public void onEvent(Event event) throws Exception {
		Treeitem target = (Treeitem )event.getTarget();
		Object obj = event.getData();
		/*logger.debug("Handle tree click");
		MapComposer mapComposer = getMapComposer(event);
		Treeitem target = null;
		MenuItem mi = null; 
		if ((mapComposer != null ) && mapComposer.safeToPerformMapAction()) {
			mi = getMenuItem(event);
			target = getTarget(event);
			if ((mi != null) && mi.isLeaf()) {
				mapComposer.activateMenuItem(mi);
			}
			else if (target.getTreechildren() != null) {
				// open a non-leaf node, then exit
				target.setOpen(true);
			}
		}*/
		System.out.print("clicked");
	}

}
