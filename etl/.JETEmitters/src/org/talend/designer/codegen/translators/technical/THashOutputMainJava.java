package org.talend.designer.codegen.translators.technical;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class THashOutputMainJava
{
  protected static String nl;
  public static synchronized THashOutputMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    THashOutputMainJava result = new THashOutputMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\t\t";
  protected final String TEXT_3 = NL + "\t\t";
  protected final String TEXT_4 = NL + NL;
  protected final String TEXT_5 = NL;
  protected final String TEXT_6 = "    " + NL + "\t\t";
  protected final String TEXT_7 = "Struct oneRow_";
  protected final String TEXT_8 = " = new ";
  protected final String TEXT_9 = "Struct();" + NL + "\t\t\t\t";
  protected final String TEXT_10 = NL + "\t\t\t\t\toneRow_";
  protected final String TEXT_11 = ".";
  protected final String TEXT_12 = " = ";
  protected final String TEXT_13 = ".";
  protected final String TEXT_14 = ".clone();";
  protected final String TEXT_15 = NL + "\t\t\t\t\toneRow_";
  protected final String TEXT_16 = ".";
  protected final String TEXT_17 = " = ";
  protected final String TEXT_18 = ".";
  protected final String TEXT_19 = ";";
  protected final String TEXT_20 = NL + "\t\t" + NL + "        tHashFile_";
  protected final String TEXT_21 = ".put(oneRow_";
  protected final String TEXT_22 = ");" + NL + "        nb_line_";
  protected final String TEXT_23 = " ++;";
  protected final String TEXT_24 = NL + "\t\tif(tHashFile_";
  protected final String TEXT_25 = " == null){" + NL + "\t\t\ttHashFile_";
  protected final String TEXT_26 = " = mf_";
  protected final String TEXT_27 = ".getAdvancedMemoryHashFile(\"tHashFile_";
  protected final String TEXT_28 = "\");" + NL + "\t\t\tmf_";
  protected final String TEXT_29 = ".getResourceMap().put(\"tHashFile_";
  protected final String TEXT_30 = "\", tHashFile_";
  protected final String TEXT_31 = ");" + NL + "\t\t}" + NL + "\t\t";
  protected final String TEXT_32 = "Struct oneRow_";
  protected final String TEXT_33 = " = new ";
  protected final String TEXT_34 = "Struct();";
  protected final String TEXT_35 = NL + "\t\t\toneRow_";
  protected final String TEXT_36 = ".";
  protected final String TEXT_37 = " = ";
  protected final String TEXT_38 = ".";
  protected final String TEXT_39 = ".clone();";
  protected final String TEXT_40 = NL + "\t\t\toneRow_";
  protected final String TEXT_41 = ".";
  protected final String TEXT_42 = " = ";
  protected final String TEXT_43 = ".";
  protected final String TEXT_44 = ";";
  protected final String TEXT_45 = NL + "        tHashFile_";
  protected final String TEXT_46 = ".put(oneRow_";
  protected final String TEXT_47 = ");" + NL + "        nb_line_";
  protected final String TEXT_48 = " ++;\t";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	class Util{
		//keep the tHashOutput Name when on the searching patch, in order to avoid the cycle reference problem.
		Set<String> nodeNames = new HashSet<String>();
		List<? extends INode> nodes = null;
		//the parameter node is tHashInput/tHashOutput
		public INode getOriginaltHashOutputNode(INode node) throws Exception{	
			//initial it only once.
			if(nodes == null){
				nodes = node.getProcess().getGeneratingNodes();
			}
			
			String uniqueName = node.getUniqueName();
			if(nodeNames.contains(uniqueName)){
				//happen the cycle problem!!!
				//return null;
				throw new Exception("happen the cycle problem!!!");
			}else{
				nodeNames.add(uniqueName);
				//1. get the tHashOuput node
				boolean isLinked = "true".equals(ElementParameterParser.getValue(node, "__LINK_WITH__"));
				String tHashOutputName = ElementParameterParser.getValue(node, "__LIST__");
				if(isLinked){
				
					//System.out.println("Node:" + uniqueName);
					//System.out.println("__LINK_WITH__:" + isLinked);
					//System.out.println("__LIST__:" + tHashOutputName);
					
					if(tHashOutputName != null && !tHashOutputName.trim().equals("")){
						INode tHashOutput = getNodeFromProcess(tHashOutputName);
						if(tHashOutput != null){
							//recursive call it!!!
							return getOriginaltHashOutputNode(tHashOutput);
						}else{
							//if go here, it means that user delete the original tHashOutput 
							//return null;
							throw new Exception("if go here, it means that user delete the original tHashOutput.");
						}
					}else{
						//if go here, it means there should link to a tHashOutput, but user doesn't it.
						//return null;
						throw new Exception("if go here, it means there should link to a tHashOutput, but user doesn't it.");
					}
				}else{
					//get it!!! it can be a 1).tHashInput(read file directly), 2). tHashOuput (the original one)  
					return node;
				}				
			}			
		}
		
		private INode getNodeFromProcess(String nodeName){			
	    	for (INode nd : nodes) {
	     	   if (nd.getUniqueName().equals(nodeName)) {
	     	      return nd;
	     	   }
	     	}			
			return null;
		}	
		
	}

    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	String jobName = codeGenArgument.getJobName();
	INode node = (INode)codeGenArgument.getArgument();	
	String cid = node.getUniqueName();
	
	//1. get the tHashOuput node
	boolean isLinked = "true".equals(ElementParameterParser.getValue(node, "__LINK_WITH__"));
	String tHashOutputName = ElementParameterParser.getValue(node, "__LIST__");
	INode tHashOutputNode = null;
	if(isLinked){
		try{
			Util util = new Util();
			tHashOutputNode = util.getOriginaltHashOutputNode(node);	
			//System.out.println(tHashOutputNode.getUniqueName());
		}catch(Exception e){
    stringBuffer.append(TEXT_2);
    stringBuffer.append(e.getMessage() );
    stringBuffer.append(TEXT_3);
    }
	}
	
	//2. get tHashInput medadata
	IMetadataTable metadata = null;
	List<IMetadataTable> metadatas = node.getMetadataList();
	if (metadatas != null && metadatas.size() > 0) {
    	metadata = metadatas.get(0);
    }
    
    //3. get tHashOutput medadata
	IMetadataTable tHashOutput_metadata = null;	
	if(tHashOutputNode != null){
    	List<IMetadataTable> tHashOutput_metadatas = tHashOutputNode.getMetadataList();
    	if (tHashOutput_metadatas != null && tHashOutput_metadatas.size() > 0) {
        	tHashOutput_metadata = tHashOutput_metadatas.get(0);
        }
	}
	
	//5. get tHashInput output connectionName
	IConnection outputDataConn = null;
	String outputDataConnName = null;
	List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
	if (conns != null) {
		for (IConnection conn : conns) {
			if(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
				outputDataConn = conn;
				outputDataConnName = outputDataConn.getName();
				break;
			}
		}
	}
	
	//6. get tHashOutput incomming connectionName
	IConnection tHashOutput_incommingDataConn = null;
	String tHashOutput_incommingDataConnName = null;
	if(tHashOutputNode != null){
    	List< ? extends IConnection> tHashOutput_conns = tHashOutputNode.getIncomingConnections();
    	if (conns != null) {
    		for (IConnection conn : tHashOutput_conns) {
    			if(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
    				tHashOutput_incommingDataConn = conn;
    				tHashOutput_incommingDataConnName = tHashOutput_incommingDataConn.getName();
    				break;
    			}
    		}
    	}
	}

    stringBuffer.append(TEXT_4);
    	
	String matchingMode = ElementParameterParser.getValue(node,"__KEYS_MANAGEMENT__");
	
	//7. get input data connection	
	IConnection incommingDataConn = null;
    List< ? extends IConnection> connections = node.getIncomingConnections();
	if (connections != null) {
		for (IConnection conn : connections) {
			if(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
				incommingDataConn = conn;
				break;
			}
		}
	}

    stringBuffer.append(TEXT_5);
    	
if(!isLinked){//111
	//////////////////////////////////////////////////////////////
	//it doesn't link to another tHashOutput, it is a new HashFile.
	if(incommingDataConn != null){//222
		String connectionName = incommingDataConn.getName();
		
		IMetadataTable metadataTable = incommingDataConn.getMetadataTable();
		List<IMetadataColumn> listColumns = metadataTable.getListColumns();		

    stringBuffer.append(TEXT_6);
    stringBuffer.append(connectionName );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(connectionName );
    stringBuffer.append(TEXT_9);
    
            for (IMetadataColumn column : listColumns) {
                String columnName = column.getLabel();
				if("id_Dynamic".equals(column.getTalendType())) {

    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(connectionName );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_14);
    
				} else {

    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(connectionName );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_19);
    
				}
        	}

    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    	
	}//222
}else{//111
	//////////////////////////////////////////////////////////////
	//it link to another tHashOutput, it works in append model. 
	if(incommingDataConn != null && tHashOutput_metadata != null){//333
		String connectionName = incommingDataConn.getName();	
        List<IMetadataColumn> columns = metadata.getListColumns();
        List<IMetadataColumn>  tHashOutputcolumns = tHashOutput_metadata.getListColumns();
//fix bug 21630,use method getAdvancedMemoryHashFile to get a exist AdvancedMemoryHashFile

    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(jobName+"_"+tHashOutputName );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(jobName+"_"+cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(tHashOutput_incommingDataConnName );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(tHashOutput_incommingDataConnName );
    stringBuffer.append(TEXT_34);
          
    //min
    int size = Math.min(columns.size(), tHashOutputcolumns.size());
    for(int i=0; i<size; i++){//444
		if("id_Dynamic".equals(tHashOutputcolumns.get(i).getTalendType())) {

    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(tHashOutputcolumns.get(i).getLabel() );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(connectionName );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(columns.get(i).getLabel() );
    stringBuffer.append(TEXT_39);
    
		} else {

    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(tHashOutputcolumns.get(i).getLabel() );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(connectionName );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(columns.get(i).getLabel() );
    stringBuffer.append(TEXT_44);
    
		}
	}//444

    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    	
  }//333

}//111

    return stringBuffer.toString();
  }
}
