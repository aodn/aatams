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
 * Tree 'model' for list of tags detected providing access to all
 * detections data for each tag 
 * 
 * Design based on the concept that data is retrieved from the database as needed.
 * This gets a bit messy
 * 
 * @author stevec
 * 
 */
public class DetectedTagsTree extends AbstractTreeModel{ 

	private static final long serialVersionUID = 1L;
	
	private DataSource ds = null;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

	/**
	 * Log4j instance
	 */
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public DetectedTagsTree(Object arg0) {
		super(arg0);
		BasicConfigurator.configure();
		try{
			ds = (DataSource)new InitialContext().lookup("java:comp/env/jdbc/aatams");
		}catch(NamingException e){
			logger.fatal("Could not find required DataSource object 'java:comp/env/jdbc/aatams'", e);
		}
	}
	
	private DetectedTagsTree() {
		super(null);
	}

	/**
	 * Returns the child of parent at index index in the parent's child array.
	 */
	@Override
	public Object getChild(Object arg0, int arg1) {
		try{
			return ((BaseNode)arg0).getChild(arg1);
		}catch(Exception e){
			logger.error(e);
			return null;
		}
	}

	/**
	 * Returns the number of children of parent.
	 */
	@Override
	public int getChildCount(Object arg0) {
		try{
			BaseNode node = (BaseNode)arg0;
			//see if this node has already been visited
			if(!node.initialised()){
				//no, so find the children
				conn = ds.getConnection();
				stmt = conn.createStatement();
				if(node instanceof RootNode){
					//find all installations
					rs = stmt.executeQuery("select installation_id, name from installation");
					while(rs.next()){
						node.addChild(new InstallationNode(rs.getLong(1),rs.getString(2)));
					}
					rs.close();
					stmt.close();
				}else if (node instanceof InstallationNode){
					//find all stations in this installation that have deployments 
					rs = stmt.executeQuery("select station_id, name from installation_station\n" +
							"where station_id in\n" +
							"(select distinct(station_id) from receiver_deployment\n" +
							"where installation_id = " + node.getId() + "" +
							"and station_id is not null)\n" +
							"order by installation_station.name");
					while(rs.next()){
						node.addChild(new StationNode(rs.getLong(1),rs.getString(2)));
					}
					rs.close();
					//find all receiver deployments not assigned to a station for installation 
					rs = stmt.executeQuery("select deployment_id, concat(concat(device.code_name,'-'),to_char(receiver_deployment.deployment_timestamp,'DD/MM/YYYY')) as name\n" +
							"from receiver_deployment, device\n" +
							"where receiver_deployment.device_id = device.device_id and\n" +
							"receiver_deployment.installation_id = " + node.getId() + " and\n" +
							"receiver_deployment.station_id is null\n" +
							"order by device.code_name");
					while(rs.next()){
						node.addChild(new ReceiverDeploymentNode(rs.getLong(1),rs.getString(2)));
					}
					rs.close();
					stmt.close();
				}else if (node instanceof StationNode){
					//find all receiver deployments assigned to this station
					rs = stmt.executeQuery("select deployment_id, concat(concat(device.code_name,'-'),to_char(receiver_deployment.deployment_timestamp,'DD/MM/YYYY')) as name\n" +
							"from receiver_deployment, device\n" +
							"where receiver_deployment.device_id = device.device_id and receiver_deployment.station_id = " + node.getId() + "\n" +
							"order by device.code_name");
					while(rs.next()){
						node.addChild(new ReceiverDeploymentNode(rs.getLong(1),rs.getString(2)));
					}
					rs.close();
					stmt.close();
				}else if (node instanceof ReceiverDeploymentNode){
					// find all distinct tags detected for deployment.
					rs = stmt.executeQuery("select device_id, code_name from device where device_id in " +
										   "(select distinct(device_id) from detection where deployment_id = " + node.getId() + ")" +
										   "order by code_name");
					while(rs.next()){
						node.addChild(new TagNode(rs.getLong(1),rs.getString(2)));
					}
					rs.close();
					stmt.close();
				}else if (node instanceof TagNode){

				}else{
					throw new Exception("Unknown Node Type encountered");
				}
			}
			return node.childCount();
		}catch(Exception e){
			logger.error(e);
			return 0;
		}
	}

	/**
	 *  Returns true if node is a leaf.
	 */
	@Override
	public boolean isLeaf(Object arg0) {
		try{
			BaseNode node = (BaseNode)arg0;
			if(!node.initialised())
				return (this.getChildCount(arg0) == 0);
			else
				return (node.childCount() == 0);  
		}catch(Exception e){
			logger.error(e);
			return true;
		}
	}
	
	public static RootNode getRootNode(){
		DetectedTagsTree tree = new DetectedTagsTree();
		return tree.new RootNode();
	}
	
	public static TestRoot getTestNode(){
		DetectedTagsTree tree = new DetectedTagsTree();
		return tree.new TestRoot();
	}
	
	public static NodeType getNodeType(Object node){
		if(node instanceof RootNode)
			return NodeType.ROOT;
		else if (node instanceof InstallationNode)
			return NodeType.INSTALLATION;
		else if(node instanceof StationNode)
			return NodeType.STATION;
		else if(node instanceof ReceiverDeploymentNode)
			return NodeType.DEPLOYMENT;
		else if(node instanceof TagNode)
			return NodeType.TAG;
		else
			return null;
	}
	
	public enum NodeType{
		ROOT, INSTALLATION, STATION, DEPLOYMENT, TAG
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public abstract class BaseNode {
		
		private long id = 0;
		private String name = "";
		protected ArrayList<BaseNode> children = null;

		public BaseNode(long identifier, String name){
			this.id = identifier;
			this.name = name;
		}
		
		public boolean initialised(){
			return (this.children != null); 
		}
		
		public String toString(){
			return this.name;
		}
		
		public long getId(){
			return this.id;
		}
		
		public BaseNode addChild(BaseNode child){
			if(this.children == null){
				this.children = new ArrayList<BaseNode>();
			}
			this.children.add(child);
			return child;
		}
		
		public BaseNode getChild(int index) throws Exception{
			if(!this.initialised()){
				throw new Exception("node children have not been found");
			}
			return this.children.get(index);
		}
		
		public Integer childCount()throws Exception{
			if(!this.initialised()){
				throw new Exception("node children have not been found");
			}
			else
				return this.children.size();
		}
	}	

	private class RootNode extends BaseNode {
		public RootNode(){
			super(0,"");
		}
	}
	
	private class InstallationNode extends BaseNode {
		public InstallationNode(long identifier, String name) {
			super(identifier,name);
		}
	}
	
	private class StationNode extends BaseNode {
		public StationNode(long identifier, String name) {
			super(identifier,name);
		}
	}
	
	private class ReceiverDeploymentNode extends BaseNode {
		public ReceiverDeploymentNode(long identifier, String name) {
			super(identifier, name);
		}
	}

	private class TagNode extends BaseNode {
		public TagNode(long identifier, String name) {
			super(identifier, name);
			this.children = new ArrayList<BaseNode>();
		}
	}
	
	public class TestRoot extends BaseNode{
		public TestRoot() {
			super(1, "root");
			BaseNode inst1, inst2, depl1, tag1, tag2, tag3, tag4;
			inst1 = this.addChild(new InstallationNode(2,"Installation1"));
			inst2 = this.addChild(new InstallationNode(3,"Installation2"));
			depl1 = inst1.addChild(new ReceiverDeploymentNode(4,"Deployment1"));
			tag1 = depl1.addChild(new TagNode(5,"Tag1"));
			tag2 = depl1.addChild(new TagNode(6,"Tag2"));
			tag3 = depl1.addChild(new TagNode(7,"Tag3"));
			tag4 = depl1.addChild(new TagNode(8,"Tag4"));
		}
	}
}
