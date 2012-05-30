package org.talend.designer.codegen.translators.technical;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.IConnection;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.process.IHashableInputConnections;
import org.talend.core.model.process.IHashConfiguration;
import org.talend.core.model.process.IHashableColumn;
import org.talend.core.model.process.IMatchingMode;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IDataConnection;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.eclipse.emf.common.util.EList;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;

public class TAdvancedXMLMapHashBeginJava {

  protected static String nl;
  public static synchronized TAdvancedXMLMapHashBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TAdvancedXMLMapHashBeginJava result = new TAdvancedXMLMapHashBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\t\t   \t\t// connection name:";
  protected final String TEXT_3 = NL + "\t\t\t   \t\t// source node:";
  protected final String TEXT_4 = " | target node:";
  protected final String TEXT_5 = NL + "\t\t\t   \t\t// linked node: ";
  protected final String TEXT_6 = NL + "\t\t\t   ";
  protected final String TEXT_7 = NL + "\t\t\t   \t\torg.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE matchingModeEnum_";
  protected final String TEXT_8 = " = " + NL + "\t\t\t   \t\t\torg.talend.designer.components.lookup.common.ICommonLookup.MATCHING_MODE.";
  protected final String TEXT_9 = ";" + NL + "\t\t\t   " + NL + "\t\t\t   ";
  protected final String TEXT_10 = NL + "\t\t\t\t\torg.talend.designer.components.lookup.persistent.Persistent";
  protected final String TEXT_11 = "LookupManager<";
  protected final String TEXT_12 = "Struct> tHash_Lookup_";
  protected final String TEXT_13 = " = " + NL + "\t   \t\t\t\t\tnew org.talend.designer.components.lookup.persistent.Persistent";
  protected final String TEXT_14 = "LookupManager<";
  protected final String TEXT_15 = "Struct>(matchingModeEnum_";
  protected final String TEXT_16 = ", ";
  protected final String TEXT_17 = " + \"/\"+ jobName +\"_tMapData_\" + pid +\"_Lookup_";
  protected final String TEXT_18 = "_\"" + NL + "\t   \t\t\t\t\t, new org.talend.designer.components.persistent.IRowCreator() {" + NL + "\t   \t\t\t\t\t\tpublic ";
  protected final String TEXT_19 = "Struct createRowInstance() {" + NL + "\t   \t\t\t\t\t\t\treturn new ";
  protected final String TEXT_20 = "Struct();" + NL + "\t   \t\t\t\t\t\t}" + NL + "\t   \t\t\t\t\t}" + NL + "\t   \t\t\t\t\t";
  protected final String TEXT_21 = NL + "\t   \t\t\t\t\t\t, ";
  protected final String TEXT_22 = NL + "\t   \t\t\t\t\t";
  protected final String TEXT_23 = NL + "\t   \t\t\t\t\t); " + NL + "" + NL + "\t   \t\t\t\ttHash_Lookup_";
  protected final String TEXT_24 = ".initPut();" + NL + "" + NL + "\t\t   \t   \t   globalMap.put(\"tHash_Lookup_";
  protected final String TEXT_25 = "\", tHash_Lookup_";
  protected final String TEXT_26 = ");" + NL + "\t   \t\t\t";
  protected final String TEXT_27 = NL + "\t   \t\t\t\torg.talend.designer.components.lookup.memory.AdvancedMemoryLookup<";
  protected final String TEXT_28 = "Struct> tHash_Lookup_";
  protected final String TEXT_29 = " = " + NL + "\t   \t\t\t\t\torg.talend.designer.components.lookup.memory.AdvancedMemoryLookup." + NL + "\t   \t\t\t\t\t\t<";
  protected final String TEXT_30 = "Struct>getLookup(matchingModeEnum_";
  protected final String TEXT_31 = ");" + NL + "" + NL + "\t\t   \t   \t   globalMap.put(\"tHash_Lookup_";
  protected final String TEXT_32 = "\", tHash_Lookup_";
  protected final String TEXT_33 = ");" + NL + "\t\t   \t   \t   " + NL + "\t\t\t\t";
  protected final String TEXT_34 = NL + "\t\t\t\t\t\tclass ";
  protected final String TEXT_35 = "Struct extends ";
  protected final String TEXT_36 = "Struct {" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t{" + NL + "\t\t\t\t\t\t\t\tthis.loopKey = \"";
  protected final String TEXT_37 = "\";" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\tpublic int hashCode() {" + NL + "\t\t\t\t\t\t\t\tif(this.hashCodeDirty){" + NL + "\t\t\t\t\t\t\t\t\tfinal int prime = PRIME;" + NL + "\t\t\t\t\t\t\t\t\tint result = DEFAULT_HASHCODE;";
  protected final String TEXT_38 = NL + "\t\t\t\t\t\t\t        \t\t\tresult = prime * result + (this.";
  protected final String TEXT_39 = " ? 1231 : 1237);" + NL + "\t\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_40 = NL + "\t\t\t\t\t\t\t\t\t\t\t\tresult = prime * result + (int) this.";
  protected final String TEXT_41 = ";" + NL + "\t\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_42 = NL + "\t\t\t\t\t\t\t\t\t\t\tresult = prime * result + java.util.Arrays.hashCode(this.";
  protected final String TEXT_43 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_44 = NL + "\t\t\t\t\t\t\t\t\t\t\tresult = prime * result + ((this.";
  protected final String TEXT_45 = " == null) ? 0 : this.";
  protected final String TEXT_46 = ".hashCode());" + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_47 = NL + "\t\t\t\t\t\t\t\t\tresult = prime * result + ((this.loopKey == null) ? 0 : this.loopKey.hashCode());" + NL + "\t\t\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t\tthis.hashCode = result;" + NL + "    \t\t\t\t\t\t\t\tthis.hashCodeDirty = false;\t\t" + NL + "\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\treturn this.hashCode;" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\tpublic boolean equals(Object obj) {" + NL + "\t\t\t\t\t\t\t\tif (this == obj) return true;" + NL + "\t\t\t\t\t\t\t\tif (obj == null) return false;" + NL + "\t\t\t\t\t\t\t\tif(!(obj instanceof ";
  protected final String TEXT_48 = "Struct)) return false;" + NL + "\t\t\t\t\t\t\t\tfinal ";
  protected final String TEXT_49 = "Struct other = (";
  protected final String TEXT_50 = "Struct) obj;" + NL + "\t\t\t\t\t\t\t\tif(!this.loopKey.equals(other.loopKey)) return false;";
  protected final String TEXT_51 = NL + "\t\t\t\t\t\t\t\t\t\tif (this.";
  protected final String TEXT_52 = " != other.";
  protected final String TEXT_53 = ") " + NL + "\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_54 = NL + "\t\t\t\t\t\t\t\t\t\tif(!java.util.Arrays.equals(this.";
  protected final String TEXT_55 = ", other.";
  protected final String TEXT_56 = ")) {" + NL + "\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_57 = NL + "\t\t\t\t\t\t\t\t\t\tif (this.";
  protected final String TEXT_58 = " == null) {" + NL + "\t\t\t\t\t\t\t\t\t\t\tif (other.";
  protected final String TEXT_59 = " != null) " + NL + "\t\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t\t} else if (!this.";
  protected final String TEXT_60 = ".equals(other.";
  protected final String TEXT_61 = ")) " + NL + "\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_62 = NL + "\t\t\t\t\t\t\t\treturn true;" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t}";
  protected final String TEXT_63 = " " + NL + "\t\t\t\t/*" + NL + "\t\t\t\t * Valid target not found for connection \"";
  protected final String TEXT_64 = "\"" + NL + "\t\t\t\t */ " + NL + "\t\t\t\t";
  protected final String TEXT_65 = NL + "            ";
  protected final String TEXT_66 = NL;

	//get all nodes whose expression is editable;
	void getAllLeaf(TreeNode node,List<TreeNode> result) {
		EList<TreeNode> children = node.getChildren();
		if(children==null || children.size() == 0) {
			result.add(node);//leaf is editable
		} else {
			boolean editableAtExpression = true;
			for(TreeNode child : children) {
				if(child!=null) {
					//attribute and namespace are not treat as subnode , so the expression of treeNode should be editable.
					if(NodeType.ATTRIBUT != child.getNodeType() && NodeType.NAME_SPACE != child.getNodeType()) {
						editableAtExpression = false;
					}
					getAllLeaf(child,result);
				}
			}
			
			if(editableAtExpression) {
				result.add(node);
			}
		}
	}
	
	void getAllLoopNodes(OutputTreeNode node,List<OutputTreeNode> result) {
		if(node == null) {
			return;
		}
		if(node.isLoop()){
			result.add(node);
			return;
		}
		for(TreeNode child : node.getChildren()){
			getAllLoopNodes((OutputTreeNode)child,result);
		}
	}
	
	List<InputLoopNodesTable> getValidInputLoopNodesTables(List<InputLoopNodesTable> inputLoopNodesTablesWithUnValid) {
    	List<InputLoopNodesTable> inputLoopNodesTables = new ArrayList<InputLoopNodesTable>();
    	if(inputLoopNodesTablesWithUnValid == null) return inputLoopNodesTables;
		for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTablesWithUnValid) {
			if(inputLoopNodesTable!=null && inputLoopNodesTable.getInputloopnodes()!=null && inputLoopNodesTable.getInputloopnodes().size()>0)
				inputLoopNodesTables.add(inputLoopNodesTable);
		}
		return inputLoopNodesTables;
    }
	
    public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();


    List<IConnection> connections = (List<IConnection>) node.getIncomingConnections();
    
	if (connections != null && connections.size() > 0) {
        for (IConnection connection : connections) {
        	String connectionName = connection.getName();
        	
    stringBuffer.append(TEXT_2);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(connection.getSource());
    stringBuffer.append(TEXT_4);
    stringBuffer.append(connection.getTarget());
    stringBuffer.append(TEXT_5);
    stringBuffer.append(((IDataConnection) connection).getLinkNodeForHash());
    stringBuffer.append(TEXT_6);
    
			INode validTarget = ((IDataConnection) connection).getLinkNodeForHash();
			if(validTarget != null) {
				boolean isXMLMapComponent = "tXMLMap".equals(validTarget.getComponent().getName());
				boolean findFromBasicType = false;
				boolean findFromDocumentType = false;
				boolean unvalidLookupKeyExistForHash = false;
				List<InputLoopNodesTable> allInputLoopNodesTables = new ArrayList<InputLoopNodesTable>();
				LoopHelper loopHelper = null;
				IMetadataTable metadataTable = connection.getMetadataTable();
				List<IMetadataColumn> listColumns = metadataTable.getListColumns();
				Map<InputLoopNodesTable,List<String>> outputLoopToUnvalidLookupKeys = new HashMap<InputLoopNodesTable,List<String>>();
				if(isXMLMapComponent) {//TD110 
					XmlMapComponent xmlMapComponent = (XmlMapComponent)validTarget;
					XmlMapData xmlMapData=(XmlMapData)ElementParameterParser.getObjectValueXMLTree(xmlMapComponent);
					EList<InputXmlTree> inputTablesWithInvalid = xmlMapData.getInputTrees();
					Map<String, InputXmlTree> nameToTable = new HashMap<String, InputXmlTree>();
					for(InputXmlTree inputTable : inputTablesWithInvalid) {
						nameToTable.put(inputTable.getName(),inputTable);
					}
					InputXmlTree currentInputTree = nameToTable.get(connectionName);
					if(currentInputTree!=null && currentInputTree.isLookup()) {
						EList<TreeNode> treeNodes = currentInputTree.getNodes();
						for(TreeNode treeNode : treeNodes) {
							String columnType = treeNode.getType();
							List<TreeNode> allLeaf = new ArrayList<TreeNode>();
							getAllLeaf(treeNode,allLeaf);
							for(TreeNode leaf : allLeaf) {
								if(leaf == null) {
									continue;
								}
								String expressionKey = leaf.getExpression();
								if (expressionKey != null && !"".equals(expressionKey.trim())) {
									if("id_Document".equals(columnType)) {
										findFromDocumentType = true; 
									} else {
										findFromBasicType = true;
									}	
								}
								
							}
						}
					}
					
					/////////////////////////////////////////////////////////////////////////////////////////////
					//multiloop logic
					EList<OutputXmlTree> outputTables = xmlMapData.getOutputTrees();
					EList<VarTable> varTables = xmlMapData.getVarTables();
					
					List<IConnection> inputConnections = (List<IConnection>)xmlMapComponent.getIncomingConnections();
					HashMap<String, IConnection> hNameToConnection = new HashMap<String, IConnection>();
					for(IConnection conn : inputConnections){
						hNameToConnection.put(conn.getName(), conn);
					}
					
					List<IConnection> outputConnections = (List<IConnection>) xmlMapComponent.getOutgoingConnections();
					Map<String, IConnection> nameToOutputConnection = new HashMap<String, IConnection>();
				    for (IConnection conn : outputConnections) {
					  		nameToOutputConnection.put(conn.getName(), conn);
					}
					
					//filter unvalid input tables
					List<InputXmlTree> inputTables = new ArrayList<InputXmlTree>();
					for(int i=0; i<inputTablesWithInvalid.size(); i++){
						InputXmlTree  currentTree = inputTablesWithInvalid.get(i);
						if(hNameToConnection.get(currentTree.getName()) != null){
							inputTables.add(currentTree);
						}
					}
					
					InputXmlTree mainTable = inputTables.get(0);
					TreeNode documentInMain = null;
					for(TreeNode currentNode : mainTable.getNodes()) {
						if("id_Document".equals(currentNode)) {
							documentInMain = currentNode;
						}
					}
					
					//filter unvalid output tables
					ArrayList<OutputXmlTree> outputTablesSortedByReject = new ArrayList<OutputXmlTree>();
					for(OutputXmlTree outputTable : outputTables) {
						if(nameToOutputConnection.get(outputTable.getName())!=null) {
							outputTablesSortedByReject.add(outputTable);
						}
					}
					// sorting outputs : rejects tables after not rejects table
					Collections.sort(outputTablesSortedByReject, new Comparator<OutputXmlTree>() {
				
						public int compare(OutputXmlTree o1, OutputXmlTree o2) {
							if (o1.isReject() != o2.isReject()) {
								if (o1.isReject()) {
									return 1;
								} else {
									return -1;
								}
							}
							if (o1.isRejectInnerJoin() != o2.isRejectInnerJoin()) {
								if (o1.isRejectInnerJoin()) {
									return 1;
								} else {
									return -1;
								}
							}
							return 0;
						}
				
					});
					
					boolean parallelRelationExist = false;
					
					for(OutputXmlTree outputTable : outputTablesSortedByReject) {
						List<OutputTreeNode> loopNodes = new ArrayList<OutputTreeNode>();
						boolean docExist = false;
						for(OutputTreeNode currentNode : outputTable.getNodes()) {
							if("id_Document".equals(currentNode.getType())) {
								getAllLoopNodes(currentNode,loopNodes);
								docExist = true;
							}
						}
						
						if(docExist) {
							for(OutputTreeNode loop : loopNodes) {
								if(loop.getInputLoopNodesTable()!=null) {
									allInputLoopNodesTables.add(loop.getInputLoopNodesTable());
								}
							}
						} else {//only flat column exist
							allInputLoopNodesTables.addAll(getValidInputLoopNodesTables(outputTable.getInputLoopNodesTables()));
						}
					}
					
					if(allInputLoopNodesTables.size()>0) {
						for(int i=0;i<allInputLoopNodesTables.size()-1;i++) {
							InputLoopNodesTable inputLoopNodesTable = allInputLoopNodesTables.get(i);
							List<TreeNode> inputLoopNodes = inputLoopNodesTable.getInputloopnodes();
							int size = inputLoopNodes.size();
							
							InputLoopNodesTable nextInputLoopNodesTable = allInputLoopNodesTables.get(i+1);
							List<TreeNode> nextInputLoopNodes = nextInputLoopNodesTable.getInputloopnodes();
							int nextSize = nextInputLoopNodes.size();
							if((size != nextSize) || (!inputLoopNodes.containsAll(nextInputLoopNodes))) {
								parallelRelationExist = true;
								break;
							}
						}
					}
					
					if(parallelRelationExist && findFromBasicType && mainTable.isMultiLoops()) {
						loopHelper = new LoopHelper(documentInMain,outputTablesSortedByReject,inputTables);
						for(int i=0;i<allInputLoopNodesTables.size();i++) {
							InputLoopNodesTable inputLoopNodesTable = allInputLoopNodesTables.get(i);
							List<TreeNode> inputLoopNodes = inputLoopNodesTable.getInputloopnodes();
							loopHelper.initForOneOutputTableLoop(inputLoopNodes);
							List<String> unvalidLookupKeys = new ArrayList<String>();
							outputLoopToUnvalidLookupKeys.put(inputLoopNodesTable,unvalidLookupKeys);
							if(currentInputTree!=null && currentInputTree.isLookup()) {
								EList<TreeNode> treeNodes = currentInputTree.getNodes();
								for(TreeNode treeNode : treeNodes) {
									//only check the flat column,because hash only ready for flat column
									if(!loopHelper.validLookupKey(treeNode)) {
										unvalidLookupKeyExistForHash = true;
										unvalidLookupKeys.add(treeNode.getName());
									} else {
										String expressionKey = treeNode.getExpression();
										if (expressionKey == null || "".equals(expressionKey.trim())) {
											unvalidLookupKeys.add(treeNode.getName());
										}
									}
									
								}
								
							}
						}
					}
					
				}//TD110
				List<IHashableColumn> hashableColumns = null;
				IMatchingMode matchingMode = null;
				String tempFolder = null;
				String rowsBufferSize = null;
				IHashConfiguration hashConfiguration = null;				
				String matchingModeStr = null;
				boolean bSortOnDisk = "true".equals(ElementParameterParser.getValue(node, "__SORT_ON_DISK__")); 
				
			    if (validTarget instanceof IHashableInputConnections){
					IHashableInputConnections target = (IHashableInputConnections) validTarget;
					hashConfiguration = target.getHashConfiguration(connection.getName());
				} else{
				    List<java.util.Map<String, String>> listBlockings = (List<java.util.Map<String, String>>)ElementParameterParser.getObjectValue(node, "__BLOCKING_DEFINITION__");
				  	matchingModeStr = (listBlockings == null || listBlockings.size() == 0) ? "ALL_ROWS" : "ALL_MATCHES";
                    tempFolder = ElementParameterParser.getValue(node, "__TMP_DIRECTORY__");
				  	rowsBufferSize = ElementParameterParser.getValue(node, "__ROWS_BUFFER_SIZE__");
				}

					if(hashConfiguration == null) {
						hashableColumns = new ArrayList(0);
						
						//System.out.println(connectionName + " ### " + hashConfiguration + "IS NULL ##### " + validTarget + " " + validTarget.getClass());
						
					} else {
						tempFolder = hashConfiguration.getTemporaryDataDirectory();
						hashableColumns = hashConfiguration.getHashableColumns();
						matchingMode = hashConfiguration.getMatchingMode();
						rowsBufferSize = hashConfiguration.getRowsBufferSize();
					}
					
					if (matchingModeStr == null){
					  if(matchingMode == null) {
						  if(hashableColumns.size() > 0) {
							matchingModeStr = "UNIQUE_MATCH";
						  } else {
							matchingModeStr = "ALL_ROWS";
						  }
					  } else {
						matchingModeStr = matchingMode.toString();
					  }
					}
					if(isXMLMapComponent && findFromBasicType && findFromDocumentType) {
						matchingModeStr = "ALL_MATCHES";
					}
					boolean isAllRows = "ALL_ROWS".equals(matchingModeStr);
			   
    stringBuffer.append(TEXT_7);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_8);
    stringBuffer.append( matchingModeStr );
    stringBuffer.append(TEXT_9);
    
				if (hashConfiguration != null && hashConfiguration.isPersistent() || bSortOnDisk) {
				
    stringBuffer.append(TEXT_10);
    stringBuffer.append( isAllRows ? "" : "Sorted" );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_13);
    stringBuffer.append( isAllRows ? "" : "Sorted" );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_16);
    stringBuffer.append( tempFolder );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_20);
     if(!isAllRows) { 
    stringBuffer.append(TEXT_21);
    stringBuffer.append( rowsBufferSize );
    stringBuffer.append(TEXT_22);
     } 
    stringBuffer.append(TEXT_23);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_26);
    	
	   			} else {
	   			
    stringBuffer.append(TEXT_27);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_33);
    
				}
				
				
				if(unvalidLookupKeyExistForHash) {
					for(int i=0;i<allInputLoopNodesTables.size();i++) {
						String loopKey = "loopKey"+i;
						InputLoopNodesTable inputLoopNodesTable = allInputLoopNodesTables.get(i);
						List<String> unvalidLookupKeys = outputLoopToUnvalidLookupKeys.get(inputLoopNodesTable);

    stringBuffer.append(TEXT_34);
    stringBuffer.append(connectionName);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_37);
    
									for (IMetadataColumn column : listColumns) {
                						String columnName = column.getLabel();
                						if(unvalidLookupKeys.contains(columnName)) {
                							continue;
                						}
                						JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());

										if (JavaTypesManager.isJavaPrimitiveType(column.getTalendType(), column.isNullable())) {
										
										 	String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
										 	if(javaType == JavaTypesManager.BOOLEAN) {
											
    stringBuffer.append(TEXT_38);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_39);
    
											} else {	
											
    stringBuffer.append(TEXT_40);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_41);
    
											}
											
										} else if(javaType == JavaTypesManager.BYTE_ARRAY) {
					
											
    stringBuffer.append(TEXT_42);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_43);
    
										
										} else {
										
    stringBuffer.append(TEXT_44);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_46);
    
										}
										
                					}

    stringBuffer.append(TEXT_47);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_50);
    	
								for (IMetadataColumn column: listColumns) {
									String columnName = column.getLabel();
            						if(unvalidLookupKeys.contains(columnName)) {
            							continue;
            						}
									JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
				
									if (JavaTypesManager.isJavaPrimitiveType(column.getTalendType(), column.isNullable())) {
									
    stringBuffer.append(TEXT_51);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_53);
    
									} else if(javaType == JavaTypesManager.BYTE_ARRAY) {
									
    stringBuffer.append(TEXT_54);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_56);
    
									} else {
									
    stringBuffer.append(TEXT_57);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_61);
    
									}
				
								}

    stringBuffer.append(TEXT_62);
    
					}
				}
				
			} else {
				
    stringBuffer.append(TEXT_63);
    stringBuffer.append( connectionName);
    stringBuffer.append(TEXT_64);
    
			}
		}
	}


    stringBuffer.append(TEXT_65);
    stringBuffer.append(TEXT_66);
    return stringBuffer.toString();
  }
    class LoopHelper {
	
		//all used input loops(if the input loop is no used in any output table,we treat as no loop node)
		List<TreeNode> inputLoopsInAllOutputTables = new ArrayList<TreeNode>();
		
		//all output tables
		List<OutputXmlTree> outputTables;
		
		//all input tables
		List<InputXmlTree> inputTables;
		
		TreeNode inputRootOfMainInputTableDocument;
		
		public LoopHelper(TreeNode inputRoot,List<OutputXmlTree> outputTables,List<InputXmlTree> inputTables) {
			this.inputRootOfMainInputTableDocument = inputRoot;
			this.outputTables = outputTables;
			this.inputTables = inputTables;
			
			for(OutputXmlTree outputTable : outputTables) {
				List<InputLoopNodesTable> inputLoopNodesTables = getValidInputLoopNodesTables(outputTable.getInputLoopNodesTables());
				for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTables) {
					List<TreeNode> inputLoopNodes = inputLoopNodesTable.getInputloopnodes();
					for(TreeNode node : inputLoopNodes) {
						if(!inputLoopsInAllOutputTables.contains(node)) {
							inputLoopsInAllOutputTables.add(node);
						}
					}
					
				}
			}
		}
		
		//all input loops used by current output table
		List<TreeNode> inputLoopsInCurrentOutputTable;
		
		//for output mapping belong to unique input loop
		public Map<TreeNode,TreeNode> outputNodeToLoopNode;
		
		//need a var for lookup output mapping belong to input loop
		
		boolean parallelLoopExistInCurrentOutputTable;
		
		boolean nestedLoopExistInCurrentOutputTable;
		
		//set current output table and init
		public void initForOneOutputTable(OutputXmlTree currentOutputTable) {
			inputLoopsInCurrentOutputTable = new ArrayList<TreeNode>();
			parallelLoopExistInCurrentOutputTable = false;
			nestedLoopExistInCurrentOutputTable = false;
			outputNodeToLoopNode = new HashMap<TreeNode,TreeNode>();
			
			List<InputLoopNodesTable> inputLoopNodesTables = getInputLoopNodesTables(currentOutputTable);
			if(inputLoopNodesTables.size() > 1) {
				parallelLoopExistInCurrentOutputTable = true;
			}
			for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTables) {
				List<TreeNode> inputLoopNodes = inputLoopNodesTable.getInputloopnodes();
				if(inputLoopNodes.size() > 1) {
					nestedLoopExistInCurrentOutputTable = true;
				}
				for(TreeNode inputLoopNode : inputLoopNodes) {
					if(!inputLoopsInCurrentOutputTable.contains(inputLoopNode)) {
						inputLoopsInCurrentOutputTable.add(inputLoopNode);
					}
				}
			}
			if(parallelLoopExistInCurrentOutputTable || nestedLoopExistInCurrentOutputTable) {
				setLoopMapping();
			}
		}
		
		private List<InputLoopNodesTable> getInputLoopNodesTables(OutputXmlTree currentOutputTable) {
			List<InputLoopNodesTable> result = new ArrayList<InputLoopNodesTable>();
			
			List<OutputTreeNode> loopNodes = new ArrayList<OutputTreeNode>();
			boolean docExist = false;
			for(OutputTreeNode currentNode : currentOutputTable.getNodes()) {
				if("id_Document".equals(currentNode.getType())) {
					getAllLoopNodes(currentNode,loopNodes);
					docExist = true;
				}
			}
			
			if(docExist) {
				for(OutputTreeNode loop : loopNodes) {
					if(loop.getInputLoopNodesTable()!=null) {
						result.add(loop.getInputLoopNodesTable());
					}
				}
			} else {//only flat column exist
				result.addAll(getValidInputLoopNodesTables(currentOutputTable.getInputLoopNodesTables()));
			}
			
			return result;
		}
		
		List<TreeNode> currentInputLoopNodes;
		//filter for lookup table
		List<TreeNode> noUsedInputLoopsForCurrentOutputTableLoop;
		List<TreeNode> noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop;
		//set current output loop of current output table  and init
		public void initForOneOutputTableLoop(List<TreeNode> inputLoopNodes) {
			noUsedInputLoopsForCurrentOutputTableLoop = new ArrayList<TreeNode>();
			noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop = new ArrayList<TreeNode>();
			currentInputLoopNodes = inputLoopNodes;
			for(TreeNode inputLoopNode : inputLoopsInAllOutputTables) {
				if(!currentInputLoopNodes.contains(inputLoopNode)) {
					noUsedInputLoopsForCurrentOutputTableLoop.add(inputLoopNode);
					insideOfLoop(inputLoopNode);
				}
			}
		}
		
		private void insideOfLoop(TreeNode loopNode) {
			if(asOutput(loopNode)) {
				noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop.add(loopNode);
			}
			for(TreeNode childNode : loopNode.getChildren()) {
				insideOfLoop(childNode);
			}
		}
	
		/**
		 * used when parallel loop exist in current output table or  parallel relation exist between all output tables
		 * @param nodeOfLookupTable
		 * @return
		 */
		public boolean validLookupKey(TreeNode nodeOfLookupTable) {
			List<LookupConnection> lookupIncomings = nodeOfLookupTable.getLookupIncomingConnections();
			if(lookupIncomings.size()>0) {
				LookupConnection lookupIncoming = lookupIncomings.get(0);
				TreeNode source = (TreeNode)lookupIncoming.getSource();
				return !noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop.contains(source);
			}
			return true;
		}
		
		/**
		 * for the output mapping
		 */
		private void setLoopMapping() {
			for(TreeNode inputLoop : inputLoopsInCurrentOutputTable) {
				initInsideOfLoop(inputLoop,inputLoop);
			}
			initOutsideOfLoops(this.inputRootOfMainInputTableDocument);
		}
		
		private void initInsideOfLoop(TreeNode loopNode,TreeNode node) {
			if(asOutput(node)) {
				outputNodeToLoopNode.put(node, loopNode);
			}
			for(TreeNode childNode : node.getChildren()) {
				initInsideOfLoop(loopNode,childNode);
			}
		}
		
		private void initOutsideOfLoops(TreeNode node) {
			if(node.isLoop()) {
				return;
			}
			if(asOutput(node)) {
				outputNodeToLoopNode.put(node,null);
			}
			for(TreeNode childNode : node.getChildren()) {
				initOutsideOfLoops(childNode);
			}
		}
		
		//for output mapping 
		public boolean belongToCurrentLoop(TreeNode inputLoopNode,String xpath,boolean isMainInputLoopInNestLoop) {
			if(parallelLoopExistInCurrentOutputTable || nestedLoopExistInCurrentOutputTable) {
				for(Map.Entry<TreeNode, TreeNode> entry : outputNodeToLoopNode.entrySet()) {
					TreeNode outputNode = entry.getKey();
					TreeNode loopNode = entry.getValue();
					if(xpath.equals(outputNode.getXpath())) {
						if(isMainInputLoopInNestLoop) {
							if(loopNode == null) {//outside of any input loop
								return true;
							}
							return loopNode == inputLoopNode;
						} else {
							if(loopNode == null) {
								return false;
							}
							return loopNode == inputLoopNode;
						}
					}
				}
			}
			return true;
		}
		
		private boolean asOutput(TreeNode currentNode) {
			return currentNode.getOutgoingConnections().size()>0 || currentNode.getLookupOutgoingConnections().size()>0 || currentNode.getFilterOutGoingConnections().size()>0;
		}
		
		private List<InputLoopNodesTable> getValidInputLoopNodesTables(List<InputLoopNodesTable> inputLoopNodesTablesWithUnValid) {
	    	List<InputLoopNodesTable> inputLoopNodesTables = new ArrayList<InputLoopNodesTable>();
	    	if(inputLoopNodesTablesWithUnValid == null) return inputLoopNodesTables;
			for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTablesWithUnValid) {
				if(inputLoopNodesTable!=null && inputLoopNodesTable.getInputloopnodes()!=null && inputLoopNodesTable.getInputloopnodes().size()>0)
					inputLoopNodesTables.add(inputLoopNodesTable);
			}
			return inputLoopNodesTables;
	    }
		
		private void getAllLoopNodes(OutputTreeNode node,List<OutputTreeNode> result) {
			if(node == null) {
				return;
			}
			if(node.isLoop()){
				result.add(node);
				return;
			}
			for(TreeNode child : node.getChildren()){
				getAllLoopNodes((OutputTreeNode)child,result);
			}
		}
		
	}
    
}
