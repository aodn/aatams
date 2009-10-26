package au.org.emii.aatams.zk.tree;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.zkoss.zul.AbstractTreeModel;
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
public class DetectedTagsTree extends AbstractTreeModel {

	private static final long serialVersionUID = 1L;

	private DataSource ds = null;
	/**
	 * Log4j instance
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	public DetectedTagsTree(RootNode node) {
		super(node);
		node.setParentTree(this);
		BasicConfigurator.configure();
		try {
			ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/aatams");
		} catch (NamingException e) {
			logger.fatal("Could not find required DataSource object 'java:comp/env/jdbc/aatams'", e);
		}
	}

	private DetectedTagsTree() {
		super(null);
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
			return ((DetectedTagsTreeBaseNode) arg0).getChild(arg1);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * Returns the number of children of parent.
	 */
	@Override
	public int getChildCount(Object arg0) {
		return ((DetectedTagsTreeBaseNode) arg0).getChildCount();
	}

	/**
	 * Returns true if node is a leaf.
	 */
	@Override
	public boolean isLeaf(Object arg0) {
		return (((DetectedTagsTreeBaseNode) arg0).getChildCount() == 0);
	}

	public static RootNode getRootNode() {
		DetectedTagsTree tree = new DetectedTagsTree();
		return tree.new RootNode();
	}

	public static TestRoot getTestNode() {
		DetectedTagsTree tree = new DetectedTagsTree();
		return tree.new TestRoot();
	}

	public static NodeType getNodeType(Object node) {
		if (node instanceof RootNode)
			return NodeType.ROOT;
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
	}

	public enum NodeType {
		ROOT, INSTALLATION, STATION, DEPLOYMENT, TAG
	}

	public abstract class DetectedTagsTreeBaseNode {

		protected DetectedTagsTreeBaseNode parent = null;
		private long id = 0;
		private String name = "";
		protected ArrayList<DetectedTagsTreeBaseNode> children = new ArrayList<DetectedTagsTreeBaseNode>();
		protected boolean initialised = false;
		protected Connection conn = null;
		protected Statement stmt = null;
		protected ResultSet rs = null;
		
		public DetectedTagsTreeBaseNode(DetectedTagsTreeBaseNode parent, long identifier, String name) {
			this.parent = parent;
			this.id = identifier;
			this.name = name;
		}
		
		public DataSource getDataSource(){
			return this.parent.getDataSource();
		}

		protected abstract void init();
		public abstract DetectedTagsTreeBaseNode getParent();

		public String toString() {
			return this.name;
		}

		public long getId() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}

		public DetectedTagsTreeBaseNode getChild(int index) {
			if (!this.initialised) {
				this.init();
			}
			return this.children.get(index);
		}

		public Integer getChildCount() {
			if (!this.initialised) {
				this.init();
			}
			return this.children.size();
		}

		protected DetectedTagsTreeBaseNode addChild(DetectedTagsTreeBaseNode node) {
			this.children.add(node);
			return node;
		}
	}

	private class RootNode extends DetectedTagsTreeBaseNode {
		
		private DetectedTagsTree parentTree = null;
		
		public RootNode() {
			super(null, 0, "");
		}

		private void setParentTree(DetectedTagsTree tree){
			this.parentTree = tree;
		}
		
		public DetectedTagsTreeBaseNode getParent(){
			return null;
		}
		
		@Override
		public DataSource getDataSource(){
			return this.parentTree.getDataSource();
		}
		
		protected void init() {
			try {
				conn = this.getDataSource().getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select installation_id, name from installation");
				while (rs.next()) {
					this.addChild(new InstallationNode(this, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class InstallationNode extends DetectedTagsTreeBaseNode {
		public InstallationNode(RootNode parent, long identifier, String name) {
			super(parent, identifier, name);
		}
		
		@Override
		public RootNode getParent(){
			return (RootNode)this.parent;
		}

		protected void init() {
			try {
				conn = this.getDataSource().getConnection();
				stmt = conn.createStatement();
				// find all stations in this installation that have deployments
				rs = stmt.executeQuery("select station_id, name from installation_station\n" + "where station_id in\n"
						+ "(select distinct(station_id) from receiver_deployment\n" + "where installation_id = " + this.getId() + ""
						+ "and station_id is not null)\n" + "order by installation_station.name");
				while (rs.next()) {
					this.addChild(new StationNode(this,rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				// find all receiver deployments not assigned to a station for
				// installation
				rs = stmt
						.executeQuery("select deployment_id, concat(concat(device.code_name,'-'),to_char(receiver_deployment.deployment_timestamp,'DD/MM/YYYY')) as name\n"
								+ "from receiver_deployment, device\n"
								+ "where receiver_deployment.device_id = device.device_id and\n"
								+ "receiver_deployment.installation_id = "
								+ this.getId()
								+ " and\n"
								+ "receiver_deployment.station_id is null\n"
								+ "order by device.code_name");
				while (rs.next()) {
					this.addChild(new ReceiverDeploymentNode(this, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class StationNode extends DetectedTagsTreeBaseNode {
		public StationNode(InstallationNode parent, long identifier, String name) {
			super(parent,identifier, name);
		}
		
		@Override
		public InstallationNode getParent(){
			return (InstallationNode)this.parent;
		}

		protected void init() {
			try {
				conn = this.getDataSource().getConnection();
				stmt = conn.createStatement();
				// find all receiver deployments assigned to this station
				rs = stmt
						.executeQuery("select deployment_id, concat(concat(device.code_name,'-'),to_char(receiver_deployment.deployment_timestamp,'DD/MM/YYYY')) as name\n"
								+ "from receiver_deployment, device\n"
								+ "where receiver_deployment.device_id = device.device_id and receiver_deployment.station_id = "
								+ this.getId()
								+ "\n"
								+ "order by device.code_name");
				while (rs.next()) {
					this.addChild(new ReceiverDeploymentNode(this, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class ReceiverDeploymentNode extends DetectedTagsTreeBaseNode {
		public ReceiverDeploymentNode(InstallationNode parent, long identifier, String name) {
			super(parent, identifier, name);
		}
		
		public ReceiverDeploymentNode(StationNode parent, long identifier, String name) {
			super(parent, identifier, name);
		}
		
		@Override
		public DetectedTagsTreeBaseNode getParent(){
			return (DetectedTagsTreeBaseNode)this.parent;
		}

		protected void init() {
			try {
				conn = this.getDataSource().getConnection();
				stmt = conn.createStatement();
				// find all distinct tags detected for deployment.
				rs = stmt.executeQuery("select device_id, code_name from device where device_id in "
						+ "(select distinct(device_id) from detection where deployment_id = " + this.getId() + ")" + "order by code_name");
				while (rs.next()) {
					this.addChild(new TagNode(this, rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class TagNode extends DetectedTagsTreeBaseNode {
		public TagNode(ReceiverDeploymentNode parent, long identifier, String name) {
			super(parent, identifier, name);
			this.children = new ArrayList<DetectedTagsTreeBaseNode>();
		}
		
		@Override
		public ReceiverDeploymentNode getParent(){
			return (ReceiverDeploymentNode)this.parent;
		}

		protected void init() {
			this.initialised = true;
		}
	}

	public class TestRoot extends RootNode {

		public TestRoot() {
			super();
			this.init();
		}

		protected void init() {
			InstallationNode inst1 = (InstallationNode)super.addChild(new InstallationNode(this,2, "Installation1"));
			InstallationNode inst2 = (InstallationNode)super.addChild(new InstallationNode(this,3, "Installation2"));
			ReceiverDeploymentNode depl1 = (ReceiverDeploymentNode)inst1.addChild(new ReceiverDeploymentNode(inst1,4, "Deployment1"));
			TagNode tag1 = (TagNode)depl1.addChild(new TagNode(depl1, 5, "Tag1")); 
			TagNode tag2 = (TagNode)depl1.addChild(new TagNode(depl1, 6, "Tag2"));
			TagNode tag3 = (TagNode)depl1.addChild(new TagNode(depl1, 7, "Tag3"));
			TagNode tag4 = (TagNode)depl1.addChild(new TagNode(depl1, 8, "Tag4"));
		}
	}
}
