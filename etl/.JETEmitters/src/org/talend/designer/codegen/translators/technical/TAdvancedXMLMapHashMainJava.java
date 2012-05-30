package org.talend.designer.codegen.translators.technical;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.IConnection;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
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

public class TAdvancedXMLMapHashMainJava {

  protected static String nl;
  public static synchronized TAdvancedXMLMapHashMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TAdvancedXMLMapHashMainJava result = new TAdvancedXMLMapHashMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL;
  protected final String TEXT_2 = NL + "\t\t\t   " + NL + "\t\t\t   ";
  protected final String TEXT_3 = NL + NL + "\t\t\t\t\t";
  protected final String TEXT_4 = "Struct ";
  protected final String TEXT_5 = "_HashRow = tHash_Lookup_";
  protected final String TEXT_6 = ".getNextFreeRow();" + NL + "" + NL + "\t   \t\t\t";
  protected final String TEXT_7 = NL + NL + "\t\t\t\t\t";
  protected final String TEXT_8 = "Struct ";
  protected final String TEXT_9 = "_HashRow = new ";
  protected final String TEXT_10 = "Struct();" + NL + "\t\t\t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_11 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_12 = "Struct ";
  protected final String TEXT_13 = "_";
  protected final String TEXT_14 = "_HashRow = new ";
  protected final String TEXT_15 = "Struct();";
  protected final String TEXT_16 = " " + NL + "\t\t\t\t/*" + NL + "\t\t\t\t * Valid target not found for connection \"";
  protected final String TEXT_17 = "\"" + NL + "\t\t\t\t */ " + NL + "\t\t\t\t";
  protected final String TEXT_18 = NL + "\t\t\t\t\t";
  protected final String TEXT_19 = "_HashRow.";
  protected final String TEXT_20 = " = ";
  protected final String TEXT_21 = ".";
  protected final String TEXT_22 = ".clone();" + NL + "\t\t\t\t\t";
  protected final String TEXT_23 = NL + "\t\t\t\t\t";
  protected final String TEXT_24 = "_HashRow.";
  protected final String TEXT_25 = " = ";
  protected final String TEXT_26 = ".";
  protected final String TEXT_27 = ";" + NL + "\t\t\t\t\t";
  protected final String TEXT_28 = NL + "\t\t\t\t\ttHash_Lookup_";
  protected final String TEXT_29 = ".put(";
  protected final String TEXT_30 = "_HashRow);" + NL + "\t\t\t\t";
  protected final String TEXT_31 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_32 = "_";
  protected final String TEXT_33 = "_HashRow.";
  protected final String TEXT_34 = " = ";
  protected final String TEXT_35 = ".";
  protected final String TEXT_36 = ".clone();" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_37 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_38 = "_";
  protected final String TEXT_39 = "_HashRow.";
  protected final String TEXT_40 = " = ";
  protected final String TEXT_41 = ".";
  protected final String TEXT_42 = ";" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_43 = NL + "\t\t\t\t\ttHash_Lookup_";
  protected final String TEXT_44 = ".put(";
  protected final String TEXT_45 = "_";
  protected final String TEXT_46 = "_HashRow);" + NL + "\t\t\t\t\t";
  protected final String TEXT_47 = NL + "            " + NL + NL + NL;
  protected final String TEXT_48 = NL;

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
    List<IConnection> outConnections = (List<IConnection>) node.getOutgoingConnections();
    
    List<INode> graphicalNodes = (List<INode>) node.getProcess().getGraphicalNodes();
    
	if (connections != null && connections.size() > 0) { // T_AH_100
        for (IConnection connection : connections) { // T_AH_101
        	String connectionName = connection.getName();
   
			INode validTarget = ((IDataConnection) connection).getLinkNodeForHash();
			
			List<InputLoopNodesTable> allInputLoopNodesTables = new ArrayList<InputLoopNodesTable>();
			boolean unvalidLookupKeyExistForHash = false;

			if(validTarget != null) { // T_AH_102
					//////////////////////////////////////////////////////
					boolean isXMLMapComponent = "tXMLMap".equals(validTarget.getComponent().getName());
					boolean findFromBasicType = false;
					boolean findFromDocumentType = false;
					LoopHelper loopHelper = null;
					IMetadataTable metadataTable = connection.getMetadataTable();
					List<IMetadataColumn> listColumns = metadataTable.getListColumns();
					Map<InputLoopNodesTable,List<String>> outputLoopToUnvalidLookupKeys = new HashMap<InputLoopNodesTable,List<String>>();
					if(isXMLMapComponent) {
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
										}
										
									}
									
								}
							}
						}
						
					}
					//////////////////////////////////////////////////////
					
			
					String matchingModeStr = null;
					List<IHashableColumn> hashableColumns = null;
					IMatchingMode matchingMode = null;
					IHashConfiguration hashConfiguration = null;
					boolean bSortOnDisk = "true".equals(ElementParameterParser.getValue(node, "__SORT_ON_DISK__"));
					
					if (validTarget instanceof IHashableInputConnections){
					
					  IHashableInputConnections target = (IHashableInputConnections) validTarget;
					  hashConfiguration = target.getHashConfiguration(connection.getName());
					} else{
				  	  matchingModeStr = "ALL_MATCHES";
				    }
					if(hashConfiguration == null) {
						hashableColumns = new ArrayList(0);
					} else {
						hashableColumns = hashConfiguration.getHashableColumns();
						matchingMode = hashConfiguration.getMatchingMode();
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
			   
    stringBuffer.append(TEXT_2);
    
				if(hashConfiguration != null && hashConfiguration.isPersistent() || bSortOnDisk) {
				
    stringBuffer.append(TEXT_3);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_6);
    	
	   			} else {
	   				if(!unvalidLookupKeyExistForHash) {
	   			
    stringBuffer.append(TEXT_7);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_10);
    
					} else {
						for(int i=0;i<allInputLoopNodesTables.size();i++) {
							String loopKey = "loopKey"+i;

    stringBuffer.append(TEXT_11);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(connectionName);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_15);
    
						}
					}
				}
			} // T_AH_103 
            else { // T_AH_104
				
    stringBuffer.append(TEXT_16);
    stringBuffer.append( connectionName);
    stringBuffer.append(TEXT_17);
    
            } // T_AH_104
			
			IMetadataTable metadataTable = connection.getMetadataTable();
			
			List<IMetadataColumn> listColumns = metadataTable.getListColumns();
			if(!unvalidLookupKeyExistForHash) {
	            for (IMetadataColumn column : listColumns) {
	                String columnName = column.getLabel();
					if("id_Dynamic".equals(column.getTalendType())) {
					
    stringBuffer.append(TEXT_18);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_22);
    
					} else {
					
    stringBuffer.append(TEXT_23);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_27);
    	
					}
	            } 
	      		
    stringBuffer.append(TEXT_28);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_30);
    
	            
			} else {
				for(int i=0;i<allInputLoopNodesTables.size();i++) {
					String loopKey = "loopKey"+i;
					for (IMetadataColumn column : listColumns) {
		                String columnName = column.getLabel();
						if("id_Dynamic".equals(column.getTalendType())) {
						
    stringBuffer.append(TEXT_31);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_36);
    
						} else {
						
    stringBuffer.append(TEXT_37);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_42);
    	
						}
		            }
		            
    stringBuffer.append(TEXT_43);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(connectionName);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_46);
     
	            }
			}
			
		} // T_AH_101
	} // T_AH_100


    stringBuffer.append(TEXT_47);
    stringBuffer.append(TEXT_48);
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
