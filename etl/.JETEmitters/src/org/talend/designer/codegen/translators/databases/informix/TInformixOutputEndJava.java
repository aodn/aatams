package org.talend.designer.codegen.translators.databases.informix;

import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import java.util.List;

public class TInformixOutputEndJava
{
  protected static String nl;
  public static synchronized TInformixOutputEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TInformixOutputEndJava result = new TInformixOutputEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "        if(pstmtUpdate_";
  protected final String TEXT_2 = " != null){" + NL + "" + NL + "            pstmtUpdate_";
  protected final String TEXT_3 = ".close();" + NL + "            " + NL + "        } " + NL + "        if(pstmtInsert_";
  protected final String TEXT_4 = " != null){" + NL + "" + NL + "            pstmtInsert_";
  protected final String TEXT_5 = ".close();" + NL + "            " + NL + "        }" + NL + "        if(pstmt_";
  protected final String TEXT_6 = " != null) {" + NL + "" + NL + "            pstmt_";
  protected final String TEXT_7 = ".close();" + NL + "            " + NL + "        }        ";
  protected final String TEXT_8 = NL + "        if(pstmtUpdate_";
  protected final String TEXT_9 = " != null){" + NL + "" + NL + "            pstmtUpdate_";
  protected final String TEXT_10 = ".close();" + NL + "            " + NL + "        } " + NL + "        if(pstmtInsert_";
  protected final String TEXT_11 = " != null){" + NL + "" + NL + "            pstmtInsert_";
  protected final String TEXT_12 = ".close();" + NL + "            " + NL + "        }        ";
  protected final String TEXT_13 = "                " + NL + "\t\t\ttry {" + NL + "\t\t\t\t// when PreparedStatement execute \"executeBatch\" if have no any data to update that will throw \"java.sql.SQLException: Method can be called only once.\"" + NL + "\t\t\t\tif (pstmt_";
  protected final String TEXT_14 = " != null) {" + NL + "\t\t\t\t\tint[] updateCountArray_";
  protected final String TEXT_15 = " = pstmt_";
  protected final String TEXT_16 = ".executeBatch(); " + NL + "\t\t\t\t\tif (updateCountArray_";
  protected final String TEXT_17 = ".length > 0) {               \t" + NL + "\t\t    \t    \t";
  protected final String TEXT_18 = NL + "\t\t    \t    \t\tinsertedCount_";
  protected final String TEXT_19 = " +=pstmt_";
  protected final String TEXT_20 = ".getUpdateCount(); " + NL + "\t\t    \t    \t";
  protected final String TEXT_21 = NL + "\t\t    \t    \t\tupdatedCount_";
  protected final String TEXT_22 = " += pstmt_";
  protected final String TEXT_23 = ".getUpdateCount();" + NL + "\t\t    \t    \t";
  protected final String TEXT_24 = NL + "\t\t    \t    \t    deletedCount_";
  protected final String TEXT_25 = " += pstmt_";
  protected final String TEXT_26 = ".getUpdateCount();" + NL + "\t\t    \t    \t";
  protected final String TEXT_27 = NL + "\t    \t    \t}            \t    " + NL + "    \t    \t}" + NL + "            }catch (java.sql.BatchUpdateException e){" + NL + "            \t";
  protected final String TEXT_28 = NL + "            \t\tthrow(e);" + NL + "            \t";
  protected final String TEXT_29 = NL + "            \tSystem.out.println(e.getMessage());" + NL + "            \t";
  protected final String TEXT_30 = "                \t" + NL + "        \t}                                  ";
  protected final String TEXT_31 = " " + NL + "        if(pstmt_";
  protected final String TEXT_32 = " != null) {" + NL + "" + NL + "            pstmt_";
  protected final String TEXT_33 = ".close();" + NL + "            " + NL + "        }" + NL + " ";
  protected final String TEXT_34 = NL + "        conn_";
  protected final String TEXT_35 = ".commit();";
  protected final String TEXT_36 = NL + "\tconn_";
  protected final String TEXT_37 = " .close();" + NL + "\t";
  protected final String TEXT_38 = NL + "\tnb_line_deleted_";
  protected final String TEXT_39 = "=nb_line_deleted_";
  protected final String TEXT_40 = "+ deletedCount_";
  protected final String TEXT_41 = ";" + NL + "\tnb_line_update_";
  protected final String TEXT_42 = "=nb_line_update_";
  protected final String TEXT_43 = " + updatedCount_";
  protected final String TEXT_44 = ";" + NL + "\tnb_line_inserted_";
  protected final String TEXT_45 = "=nb_line_inserted_";
  protected final String TEXT_46 = " + insertedCount_";
  protected final String TEXT_47 = ";" + NL + "\tnb_line_rejected_";
  protected final String TEXT_48 = "=nb_line_rejected_";
  protected final String TEXT_49 = " + rejectedCount_";
  protected final String TEXT_50 = ";" + NL + "" + NL + "" + NL + "" + NL + "    globalMap.put(\"";
  protected final String TEXT_51 = "_NB_LINE\",nb_line_";
  protected final String TEXT_52 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_53 = "_NB_LINE_UPDATED\",nb_line_update_";
  protected final String TEXT_54 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_55 = "_NB_LINE_INSERTED\",nb_line_inserted_";
  protected final String TEXT_56 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_57 = "_NB_LINE_DELETED\",nb_line_deleted_";
  protected final String TEXT_58 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_59 = "_NB_LINE_REJECTED\",nb_line_rejected_";
  protected final String TEXT_60 = ");";
  protected final String TEXT_61 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	
	String cid = node.getUniqueName();
	
	String dataAction = ElementParameterParser.getValue(node,"__DATA_ACTION__");
	
	String commitEvery = ElementParameterParser.getValue(node, "__COMMIT_EVERY__");
	
	boolean useTransaction = ("true").equals(ElementParameterParser.getValue(node,"__USE_TRANSACTION__"));
	
	boolean useExistingConnection = ("true").equals(ElementParameterParser.getValue(node, "__USE_EXISTING_CONNECTION__"));
	
	String dieOnError = ElementParameterParser.getValue(node, "__DIE_ON_ERROR__");
	
	String useBatchSize = ElementParameterParser.getValue(node, "__USE_BATCH_SIZE__");
	
	//------get first reject name
	String rejectConnName = null;
    List<? extends IConnection> rejectConns = node.getOutgoingConnections("REJECT");
    if(rejectConns != null && rejectConns.size() > 0) {
        IConnection rejectConn = rejectConns.get(0);
        rejectConnName = rejectConn.getName();
    }

    if(("INSERT_OR_UPDATE").equals(dataAction)) {
        
    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    
    } else if(("UPDATE_OR_INSERT").equals(dataAction)) {
        
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    
    } else {
    //----------------------batch size
		if ((rejectConnName == null && ("true").equals(useBatchSize)&& ! useExistingConnection) && (("INSERT").equals(dataAction) || ("UPDATE").equals(dataAction) || ("DELETE").equals(dataAction))) {

    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    
		    	    	if (("INSERT").equals(dataAction)) {
		    	    	
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    
		    	    	}else if (("UPDATE").equals(dataAction)) {
		    	    	
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    
		    	    	}else if (("DELETE").equals(dataAction)) {
		    	    	
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    
		    	    	}
		    	    	
    stringBuffer.append(TEXT_27);
    
            	if(("true").equals(dieOnError)) {
            	
    stringBuffer.append(TEXT_28);
    
            	}else {
            	
    stringBuffer.append(TEXT_29);
    
            	}
            	
    stringBuffer.append(TEXT_30);
    
		}//--------end batch

    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    
    }   
if(useTransaction && !("").equals(commitEvery)&&!("0").equals(commitEvery)){
    
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    
}
if (!useExistingConnection) {
	
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    
}

    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    stringBuffer.append(TEXT_61);
    return stringBuffer.toString();
  }
}
