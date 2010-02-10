package au.org.emii.aatams.zk.tree;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;
import java.util.logging.Logger;

public abstract class DetectionsTreeBaseNode{

	protected DetectionsTreeBaseNode parent = null;
	private long id = 0;
	private String name = "";
	protected ArrayList<DetectionsTreeBaseNode> children = new ArrayList<DetectionsTreeBaseNode>();
	protected boolean initialised = false;
	protected Connection conn = null;
	protected Statement stmt = null;
	protected ResultSet rs = null;
	protected DetectionsTreeMode mode;
	protected DetectionsTree.NodeType type = null;
	protected Integer detection_count = null;
	
	public DetectionsTreeBaseNode(DetectionsTreeBaseNode parent, DetectionsTreeMode mode, long identifier, String name) {
		this.parent = parent;
		this.mode = mode;
		this.id = identifier;
		this.name = name;
	}
	
	public DetectionsTreeMode getMode(){
		return mode;
	}
	
	public DetectionsTree.NodeType getNodeType(){
		return type;
	}
	
	public DataSource getDataSource(){
		return this.parent.getDataSource();
	}

	protected abstract void init();
	
	public DetectionsTreeBaseNode getParent(){
		return this.parent;
	}

	public String toString() {
		return this.name;
	}

	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getQualifiedName() {
		String prefix = this.parent.getQualifiedName();
		return (!prefix.equals("") ? prefix + "_" + this.name : this.name);
	}

	public DetectionsTreeBaseNode getChild(int index) {
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
	
	public boolean isLeaf() {
		return (this.getChildCount() == 0);
	}

	protected DetectionsTreeBaseNode addChild(DetectionsTreeBaseNode node) {
		this.children.add(node);
		return node;
	}
	
	public Logger getLogger(){
		return parent.getLogger();
	}
	
	/**
	 * Used to print a tree for testing outside web page
	 * 
	 * @param depth
	 */
	public void print(int depth){
		String spaces = "";
		for(int i=0;i<depth;i++){ spaces += "\t"; }
		System.out.print(spaces);
		System.out.print(((this.getChildCount()>0)? "*" : ""));
		System.out.println(this.name+ "(" + id + ")");
		for(int i=0;i<this.children.size();i++){
			this.getChild(i).print(depth+1);
		}
	}
}
