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

	protected DataSource ds = null;
	/**
	 * Log4j instance
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	public DetectedTagsTree(Object node) {
		super(node);
		((BaseNode)node).parent = this;
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
	
	public DataSource getDatasource(){
		return this.ds;
	}

	/**
	 * Returns the child of parent at index index in the parent's child array.
	 */
	@Override
	public Object getChild(Object arg0, int arg1) {
		try {
			return ((BaseNode) arg0).getChild(arg1);
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
		return ((BaseNode) arg0).getChildCount();
	}

	/**
	 * Returns true if node is a leaf.
	 */
	@Override
	public boolean isLeaf(Object arg0) {
		return (((BaseNode) arg0).getChildCount() == 0);
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

	private abstract class BaseNode {

		protected DetectedTagsTree parent = null;
		private long id = 0;
		private String name = "";
		protected ArrayList<BaseNode> children = new ArrayList<BaseNode>();
		protected boolean initialised = false;
		protected Connection conn = null;
		protected Statement stmt = null;
		protected ResultSet rs = null;

		public BaseNode(long identifier, String name) {
			this.id = identifier;
			this.name = name;
		}
		
		public void setParent(DetectedTagsTree parent){
			this.parent = parent;
		}

		protected abstract void init();

		public String toString() {
			return this.name;
		}

		public long getId() {
			return this.id;
		}

		public BaseNode getChild(int index) {
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

		protected BaseNode addChild(BaseNode node) {
			this.children.add(node);
			node.parent = this.parent;
			return node;
		}
	}

	private class RootNode extends BaseNode {
		public RootNode() {
			super(0, "");
		}

		protected void init() {
			try {
				conn = this.parent.getDatasource().getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select installation_id, name from installation");
				while (rs.next()) {
					this.addChild(new InstallationNode(rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class InstallationNode extends BaseNode {
		public InstallationNode(long identifier, String name) {
			super(identifier, name);
		}

		protected void init() {
			try {
				conn = this.parent.getDatasource().getConnection();
				stmt = conn.createStatement();
				// find all stations in this installation that have deployments
				rs = stmt.executeQuery("select station_id, name from installation_station\n" + "where station_id in\n"
						+ "(select distinct(station_id) from receiver_deployment\n" + "where installation_id = " + this.getId() + ""
						+ "and station_id is not null)\n" + "order by installation_station.name");
				while (rs.next()) {
					this.addChild(new StationNode(rs.getLong(1), rs.getString(2)));
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
					this.addChild(new ReceiverDeploymentNode(rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class StationNode extends BaseNode {
		public StationNode(long identifier, String name) {
			super(identifier, name);
		}

		protected void init() {
			try {
				conn = this.parent.getDatasource().getConnection();
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
					this.addChild(new ReceiverDeploymentNode(rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class ReceiverDeploymentNode extends BaseNode {
		public ReceiverDeploymentNode(long identifier, String name) {
			super(identifier, name);
		}

		protected void init() {
			try {
				conn = this.parent.getDatasource().getConnection();
				stmt = conn.createStatement();
				// find all distinct tags detected for deployment.
				rs = stmt.executeQuery("select device_id, code_name from device where device_id in "
						+ "(select distinct(device_id) from detection where deployment_id = " + this.getId() + ")" + "order by code_name");
				while (rs.next()) {
					this.addChild(new TagNode(rs.getLong(1), rs.getString(2)));
				}
				rs.close();
				stmt.close();
				this.initialised = true;
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private class TagNode extends BaseNode {
		public TagNode(long identifier, String name) {
			super(identifier, name);
			this.children = new ArrayList<BaseNode>();
		}

		protected void init() {
			this.initialised = true;
		}
	}

	public class TestRoot extends BaseNode {

		public TestRoot() {
			super(1, "root");
			this.init();
		}

		protected void init() {
			BaseNode inst1, inst2, depl1, tag1, tag2, tag3, tag4;
			inst1 = super.addChild(new InstallationNode(2, "Installation1"));
			inst2 = super.addChild(new InstallationNode(3, "Installation2"));
			depl1 = inst1.addChild(new ReceiverDeploymentNode(4, "Deployment1"));
			tag1 = depl1.addChild(new TagNode(5, "Tag1"));
			tag2 = depl1.addChild(new TagNode(6, "Tag2"));
			tag3 = depl1.addChild(new TagNode(7, "Tag3"));
			tag4 = depl1.addChild(new TagNode(8, "Tag4"));
		}
	}
}
