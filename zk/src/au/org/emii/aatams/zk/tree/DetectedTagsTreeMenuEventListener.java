package au.org.emii.aatams.zk.tree;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkforge.timeline.Timeline;
import org.zkforge.timeplot.Timeplot;

import org.apache.log4j.Logger;

import au.org.emii.aatams.zk.tree.DetectedTagsTree.DetectedTagsTreeBaseNode;

public class DetectedTagsTreeMenuEventListener implements EventListener {
	private Logger logger = Logger.getLogger(this.getClass());
	public void onEvent(Event event) throws Exception {
		if(event.getTarget() instanceof Timeline){
			Timeline timeline = (Timeline)event.getTarget();
			logger.info("event for timeline");
		}else if(event.getTarget() instanceof Timeplot){
			logger.info("event for timeplot");
		}
		if(event.getData() != null){
			DetectedTagsTreeBaseNode node = (DetectedTagsTreeBaseNode)event.getData(); 
			logger.info(node.getId());
			logger.info(node.toString());
			//tell the detections data page about the event
		}
	}
}
