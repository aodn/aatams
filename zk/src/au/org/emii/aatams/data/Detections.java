package au.org.emii.aatams.data;

import java.text.SimpleDateFormat;
import java.util.logging.*;
import java.io.OutputStream;
import au.org.emii.aatams.zk.tree.*;
import au.org.emii.aatams.zk.tree.DetectionsTree.NodeType;
import java.sql.*;
import javax.sql.DataSource;
import java.util.Iterator;
import java.util.NoSuchElementException;

//getting longitude and latitude
import org.postgis.*;

/**
 * Wrapper for access to detections datasets. Possibly tweek the way datasets are built for download as
 * joining some tables may slow it down.
 * 
 * We could do this using ORM, but no point really, as not putting data into database via java.
 * 
 * @author stephen cameron
 */

public class Detections {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Long installationId = null;
	private Long stationId = null;
	private Long deploymentId = null;
	private Long tagId = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private DataSource dataSource;
	/**
	 * Constructor used when downloading data from tree interface
	 * 
	 * @param node
	 */
	public Detections(DetectionsTreeBaseNode node){
		formatter.setLenient(false); //needed?? can't hurt
		if(node != null){
			this.dataSource =  node.getDataSource();
			switch(node.getMode()){
			case DEPLOYMENTS:
				if(node.getNodeType().equals(NodeType.INSTALLATION)){
					this.setInstallationId(node.getId());
				}else if(node.getNodeType().equals(NodeType.STATION)){
					this.setStationId(node.getId());
				}else if(node.getNodeType().equals(NodeType.DEPLOYMENT)){
					this.setReceiverDeploymentId(node.getId());
				}else if(node.getNodeType().equals(NodeType.TAG)){
					this.setReceiverDeploymentId(node.getParent().getId());
					this.setTagId(node.getId());
				}
				break;
			case TAGS:
				if(node.getNodeType().equals(NodeType.TAG)){
					this.setTagId(node.getId());
				}else if(node.getNodeType().equals(NodeType.DEPLOYMENT)){
					this.setTagId(node.getParent().getId());
					this.setReceiverDeploymentId(node.getId());
				}
				break;
			case CLASSIFICATIONS:
				//uses no argument constructor and sets tagId only.
				//see ClassificationsDetectionsViewComposer
				throw new IllegalArgumentException("node parameter cannot be of mode DetectionsTreeMode.CLASSIFICATIONS");
			}
		}else{
		    throw new IllegalArgumentException("node parameter cannot be null");	
		}
	}
	
	public Detections(){
		formatter.setLenient(false); //needed?? can't hurt
	}
	
	public void setInstallationId(long id){
		this.installationId = id;
	}
	
	public void setStationId(long id){
		this.installationId = null;
		this.stationId = id;
	}
	
	public void setReceiverDeploymentId(long id){
		this.deploymentId = id;
	}
	
	public void setTagId(long id){
		this.tagId = id;
	}
	
	public long getInstallationId(){
		return this.installationId;
	}
	
	public long getStationId(){
		return this.stationId;
	}
	
	public long getReceiverDeploymentId(){
		return this.deploymentId;
	}
	
	public long getTagId(){
		return this.tagId;
	}
	
	public void write(OutputStream out){
		try{
			out.write((DetectionColumns.join().replace("LOCATION", "LONGITUDE,LATITUDE") + "\n").getBytes());
			for(Iterator<Detection> i = this.iterator(); i.hasNext();){
				String line = i.next().toString() + "\n";
				out.write(line.getBytes());
				logger.log(Level.FINER, line);
			}
		}catch(Exception e){
			logger.log(Level.SEVERE, "error trapped writing detection to out stream", e);
		}
	}
	
	private class Detection{
		public long installationId;
		public long stationId;
		public long deploymentId;
		public long detectionId;
		public Timestamp detectionTimestamp; 
		public double longitude;
		public double latitude;
		public long tagId;
		public long receiverId;
		public String tagName;
		public String receiverName;

		public String toString(){
			java.lang.StringBuilder buffer = new java.lang.StringBuilder();
			buffer.append(String.valueOf(this.installationId)); 
			buffer.append(","); 
			buffer.append(String.valueOf(this.stationId)); 
			buffer.append(",");
			buffer.append(String.valueOf(this.deploymentId)); 
			buffer.append(",");
			buffer.append(String.valueOf(this.detectionId));
			buffer.append(",");
			buffer.append(Detections.this.formatter.format((java.util.Date)this.detectionTimestamp)); 
			buffer.append(",");
			buffer.append(String.valueOf(this.longitude)); 
			buffer.append(",");
			buffer.append(String.valueOf(this.latitude));
			buffer.append(",");
			buffer.append(String.valueOf(this.tagId));
			buffer.append(",");
			buffer.append(String.valueOf(this.receiverId));
			buffer.append(",\"");
			buffer.append(this.tagName);
			buffer.append("\",\"");
			buffer.append(this.receiverName);
			buffer.append("\"");
			return buffer.toString();
		}
	}
	
	private enum DetectionColumns{
		INSTALLATION_ID, STATION_ID, DEPLOYMENT_ID, DETECTION_ID, DETECTION_TIMESTAMP, LOCATION, TAG_ID, RECEIVER_ID, TAG_CODE_NAME, RECEIVER_CODE_NAME;
		public static String join(){
			String tmp = "";
			for(DetectionColumns f : DetectionColumns.values()){
				tmp += ((tmp.equals("")) ? f : "," + f);
			}
			return tmp;
		}
		public int index(){
			return this.ordinal()+1;
		}
	}
	
	public Iterator<Detection> iterator(){
		return new DetectionsIterator();
	}
	
	private class DetectionsIterator implements Iterator<Detection>{

		private ResultSet rs;
		private Statement smt;
		private Connection conn;
		private Detection current;
		
		public DetectionsIterator(){
			try{
				this.conn = dataSource.getConnection();
			}catch(SQLException e){
				logger.severe("exception thrown when obtaining db connection");
				return;
			}
			try{
				this.smt = conn.createStatement();
				String qry = "SELECT " + DetectionColumns.join() + " \n" +
						"FROM AATAMS.DETECTION_MIN \nWHERE 1=1" ;
				if(Detections.this.installationId != null){
					qry += "\nAND INSTALLATION_ID = " + Detections.this.installationId;
				}
				if(Detections.this.stationId != null){
					qry += "\nAND STATION_ID = " + Detections.this.stationId;
				}
				if(Detections.this.deploymentId != null){
					qry += "\nAND DEPLOYMENT_ID = " + Detections.this.deploymentId;
				}
				if(Detections.this.tagId != null){
					qry += "\nAND TAG_ID = " + Detections.this.tagId;
				}
				logger.log(Level.INFO,qry.toString());
				rs = smt.executeQuery(qry);
				//load first record ready for reading;
				this.next(); 
			}catch(SQLException e){
				logger.log(Level.SEVERE,"exception thrown when obtaining detection resultset",	e);
				return;
			}
		}
		
		public boolean hasNext(){
			if(current != null){
				return true;
			}else{
				return false;
			}
		}
		
		public Detection next() throws NoSuchElementException {
			Detection next = null;
			try{
				if(this.rs != null){
					if(this.rs.next()){
						next = new Detection();
						next.installationId = rs.getLong(DetectionColumns.INSTALLATION_ID.index());
						next.stationId = rs.getLong(DetectionColumns.STATION_ID.index());
						next.deploymentId = rs.getLong(DetectionColumns.DEPLOYMENT_ID.index());
						next.detectionId = rs.getLong(DetectionColumns.DETECTION_ID.index());
						next.detectionTimestamp = rs.getTimestamp(DetectionColumns.DETECTION_TIMESTAMP.index()); 
						PGgeometry geom = (PGgeometry)rs.getObject(DetectionColumns.LOCATION.index());
						next.longitude =  ((Point)geom.getGeometry()).getX();
						next.latitude = ((Point)geom.getGeometry()).getY();
						next.tagId = rs.getLong(DetectionColumns.TAG_ID.index());
						next.receiverId = rs.getLong(DetectionColumns.RECEIVER_ID.index());
						next.tagName = rs.getString(DetectionColumns.TAG_CODE_NAME.index());
						next.receiverName = rs.getString(DetectionColumns.RECEIVER_CODE_NAME.index());
					}else{
						this.rs.close();
						this.rs = null;
						this.smt.close();
						this.conn.close();
						next = null;
					}
					return current;
				}else{
					throw new NoSuchElementException();
				}
			}catch(SQLException e){
				logger.log(Level.SEVERE,"SQLException thrown when looking for next record",e);
				throw new NoSuchElementException();
			}finally{
				current = next;
			}
		}
		
		public void remove(){
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
