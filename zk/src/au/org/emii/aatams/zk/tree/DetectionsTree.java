package au.org.emii.aatams.zk.tree;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.apache.log4j.BasicConfigurator;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.SimpleTreeNode;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;
import javax.sql.*;
import java.util.ArrayList;

/**
 * Tree 'model' for list of tags detected providing access to all detections
 * data for each tag
 * 
 * Design based on the concept that data is retrieved from the database as
 * needed. This gets a bit messy
 * 
 * @author stevec
 * 
 */
public class DetectionsTree extends AbstractTreeModel {

	private static final long serialVersionUID = 1L;

	private DataSource ds = null;
	/**
	 * Log4j instance
	 */
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	//for testing outside zk
	public DetectionsTree(RootNode node, DataSource ds) {
		super(node);
		node.setParentTree(this);
		BasicConfigurator.configure();
		this.ds = ds;
	}

	public DetectionsTree(RootNode node) {
		super(node);
		node.setParentTree(this);
		BasicConfigurator.configure();
		try {
			ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/aatams");
		} catch (NamingException e) {
			logger.log(Level.SEVERE,"Could not find required DataSource object 'java:comp/env/jdbc/aatams'", e);
		}
	}

	public DataSource getDataSource(){
		return this.ds;
	}

	/**
	 * Returns the child of parent at index index in the parent's child array.
	 */
	@Override
	public Object getChild(Object arg0, int arg1) {
		try {
			return ((DetectionsTreeBaseNode) arg0).getChild(arg1);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"",e);
			return null;
		}
	}

	/**
	 * Returns the number of children of parent.
	 */
	@Override
	public int getChildCount(Object arg0) {
		return ((DetectionsTreeBaseNode) arg0).getChildCount();
	}

	/**
	 * Returns true if node is a leaf.
	 */
	@Override
	public boolean isLeaf(Object arg0) {
		return (((DetectionsTreeBaseNode) arg0).getChildCount() == 0);
	}

	/*public static NodeType getNodeType(Object node) {
		if (node instanceof RootNode)
			return NodeType.ROOT;
		else if (node instanceof AllInstallationsNode)
			return NodeType.ALL_INSTALLATIONS;
		else if (node instanceof AllTagsNode)
			return NodeType.ALL_TAGS;
		else if (node instanceof AllClassificationsNode)
			return NodeType.ALL_CLASSIFICATIONS;
		else if (node instanceof InstallationNode)
			return NodeType.INSTALLATION;
		else if (node instanceof StationNode)
			return NodeType.STATION;
		else if (node instanceof ReceiverDeploymentNode)
			return NodeType.DEPLOYMENT;
		else if (node instanceof TagNode)
			return NodeType.TAG;
		else
			return null;
	}*/

	public enum NodeType {
		ROOT, ALL_INSTALLATIONS, ALL_TAGS, ALL_CLASSIFICATIONS, INSTALLATION, STATION, DEPLOYMENT, TAG, FAMILY, GENUS, SPECIES
	}
}
