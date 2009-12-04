package au.org.emii.aatams.zk.tree;

import java.sql.*;
import oracle.jdbc.pool.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
		    OracleDataSource ds = new OracleDataSource();
		    ds.setDriverType("thin");
		    ds.setServerName("obsidian.bluenet.utas.edu.au");
		    ds.setPortNumber(1521);
		    ds.setDatabaseName("orcl");
		    ds.setUser("AATAMS");
		    ds.setPassword("boomerSIMS");
			TestRootNode test = new TestRootNode();
			DetectionsTree tree = new DetectionsTree(test,ds);
			test.print(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
