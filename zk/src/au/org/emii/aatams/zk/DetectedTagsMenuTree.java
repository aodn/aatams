package au.org.emii.aatams.zk;

import org.apache.log4j.Logger;
import org.zkoss.zul.AbstractTreeModel;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;
import javax.sql.*;



/**
 * Dynamic Menu generator for list of tags detected providing access to all
 * detections data for each tag and maybe other services
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
	
	public DetectedTagsTree(Object root) {
		super(root);
		try{
			ds = (DataSource)new InitialContext().lookup("java:comp/env/jdbc/aatams");
		}catch(NamingException e){
			logger.fatal("Could not find required DataSource object 'java:comp/env/jdbc/aatams'", e);
		}
	}

	/**
	 * Returns the child of parent at index index in the parent's child array.
	 */
	@Override
	public Object getChild(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the number of children of parent.
	 */
	@Override
	public int getChildCount(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 *  Returns true if node is a leaf.
	 */
	@Override
	public boolean isLeaf(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	

}