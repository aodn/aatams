package org.talend.designer.codegen.translators.databases.oracle;

import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import java.util.List;

public class TOracleOutputEndJava
{
  protected static String nl;
  public static synchronized TOracleOutputEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TOracleOutputEndJava result = new TOracleOutputEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t     \t// TODO unable to parse integer: ";
  protected final String TEXT_3 = NL + "\t     \t// TODO error: ";
  protected final String TEXT_4 = NL + "\t\t\t";
  protected final String TEXT_5 = NL + "\t\tjava.sql.Statement stmtCreateGeoColumns_";
  protected final String TEXT_6 = " = conn_";
  protected final String TEXT_7 = ".createStatement();" + NL + "\t\t" + NL + "\t\t// Delete geometry columns entry if already exists." + NL + "\t\tString deleteGeometryColumns_";
  protected final String TEXT_8 = " = \"DELETE FROM user_sdo_geom_metadata \"" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t+ \"WHERE TABLE_NAME='\" + ";
  protected final String TEXT_9 = " + \"'\";" + NL + "\t\tstmtCreateGeoColumns_";
  protected final String TEXT_10 = ".execute(deleteGeometryColumns_";
  protected final String TEXT_11 = ");" + NL + "" + NL + "\t\tfor (String column: geometryColumnToExtends.keySet()) {" + NL + "\t\t    com.vividsolutions.jts.geom.Envelope extend = geometryColumnToExtends.get(column);" + NL + "\t\t    " + NL + "\t\t\t// Create new entry in geometry columns table" + NL + "\t        String insertGeometryColumns_";
  protected final String TEXT_12 = " = \"INSERT INTO user_sdo_geom_metadata (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID) VALUES ('\" " + NL + "\t        \t\t\t\t\t\t\t\t\t\t+ ";
  protected final String TEXT_13 = " + \"','\" + column.toUpperCase(java.util.Locale.US) + \"', \"" + NL + "\t        \t\t\t\t\t\t\t\t\t\t+ \"MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('x', \" + extend.getMinX() + \", \" + extend.getMaxX() + \", ";
  protected final String TEXT_14 = "), \"" + NL + "\t        \t\t\t\t\t\t\t\t\t\t+ \"MDSYS.SDO_DIM_ELEMENT('y', \" + extend.getMinY() + \", \" + extend.getMaxY() + \", ";
  protected final String TEXT_15 = ")), \"" + NL + "\t        \t\t\t\t\t\t\t\t\t\t+ ";
  protected final String TEXT_16 = " +\")\";" + NL + "\t    \tstmtCreateGeoColumns_";
  protected final String TEXT_17 = ".execute(insertGeometryColumns_";
  protected final String TEXT_18 = ");" + NL + "    \t}" + NL + "        " + NL + "        stmtCreateGeoColumns_";
  protected final String TEXT_19 = ".close();" + NL + "\t\t";
  protected final String TEXT_20 = NL + "\t\tfor (String geometryColumnName: geometryColumnToExtends.keySet()) {" + NL + "" + NL + "\t\t\tString index_name = \"spatialidx_\"+";
  protected final String TEXT_21 = "+\"_\" + geometryColumnName;" + NL + "\t\t\tif (index_name.length() > 30) {" + NL + "\t\t\t\tindex_name = index_name.substring(0, 29);" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t    \tjava.sql.Statement stmtCreateIndex_";
  protected final String TEXT_22 = " = conn_";
  protected final String TEXT_23 = ".createStatement();" + NL + "\t\t\t// Drop spatial index if exists" + NL + "\t\t\tString dropIndex_";
  protected final String TEXT_24 = " = \"DROP INDEX \"+index_name;" + NL + "\t        try {" + NL + "\t\t\t\tstmtCreateIndex_";
  protected final String TEXT_25 = ".execute(dropIndex_";
  protected final String TEXT_26 = ");" + NL + "\t\t\t}" + NL + "\t\t\tcatch (java.sql.SQLException e_";
  protected final String TEXT_27 = ") {" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\t// Create spatial index using GIST on geometry columns" + NL + "\t\t\tString createIndex_";
  protected final String TEXT_28 = " = \"CREATE INDEX \"+index_name + \" \" " + NL + "\t\t\t\t\t\t+ \"ON \" + ";
  protected final String TEXT_29 = " + \"(\" + geometryColumnName + \") \"" + NL + "\t\t\t\t\t\t+ \"INDEXTYPE IS MDSYS.SPATIAL_INDEX\";" + NL + "\t        stmtCreateIndex_";
  protected final String TEXT_30 = ".execute(createIndex_";
  protected final String TEXT_31 = ");" + NL + "\t        stmtCreateIndex_";
  protected final String TEXT_32 = ".close();" + NL + "\t    }" + NL + "\t\t";
  protected final String TEXT_33 = NL + "        if(pstmtUpdate_";
  protected final String TEXT_34 = " != null){" + NL + "" + NL + "            pstmtUpdate_";
  protected final String TEXT_35 = ".close();" + NL + "            " + NL + "        } " + NL + "        if(pstmtInsert_";
  protected final String TEXT_36 = " != null){" + NL + "" + NL + "            pstmtInsert_";
  protected final String TEXT_37 = ".close();" + NL + "            " + NL + "        }" + NL + "        if(pstmt_";
  protected final String TEXT_38 = " != null) {" + NL + "" + NL + "            pstmt_";
  protected final String TEXT_39 = ".close();" + NL + "            " + NL + "        }        ";
  protected final String TEXT_40 = NL + "        if(pstmtUpdate_";
  protected final String TEXT_41 = " != null){" + NL + "" + NL + "            pstmtUpdate_";
  protected final String TEXT_42 = ".close();" + NL + "            " + NL + "        } " + NL + "        if(pstmtInsert_";
  protected final String TEXT_43 = " != null){" + NL + "" + NL + "            pstmtInsert_";
  protected final String TEXT_44 = ".close();" + NL + "            " + NL + "        }        ";
  protected final String TEXT_45 = "                " + NL + "                try {" + NL + "                \tif (pstmt_";
  protected final String TEXT_46 = " != null) { " + NL + "\t\t\t\t\t\tpstmt_";
  protected final String TEXT_47 = ".executeBatch();    " + NL + "            \t    }          \t    " + NL + "                }catch (java.sql.BatchUpdateException e_";
  protected final String TEXT_48 = "){" + NL + "                \t";
  protected final String TEXT_49 = NL + "                \t\tthrow(e_";
  protected final String TEXT_50 = ");" + NL + "                \t";
  protected final String TEXT_51 = NL + "                \tSystem.out.println(e_";
  protected final String TEXT_52 = ".getMessage());" + NL + "                \t";
  protected final String TEXT_53 = "                \t" + NL + "            \t}" + NL + "            \tif (pstmt_";
  protected final String TEXT_54 = " != null) { " + NL + "\t            \ttmp_batchUpdateCount_";
  protected final String TEXT_55 = " = pstmt_";
  protected final String TEXT_56 = ".getUpdateCount();            \t" + NL + "\t    \t    \t";
  protected final String TEXT_57 = NL + "\t    \t    \t\tinsertedCount_";
  protected final String TEXT_58 = " " + NL + "\t    \t    \t";
  protected final String TEXT_59 = NL + "\t    \t    \t\tupdatedCount_";
  protected final String TEXT_60 = NL + "\t    \t    \t";
  protected final String TEXT_61 = NL + "\t    \t    \t    deletedCount_";
  protected final String TEXT_62 = NL + "\t    \t    \t";
  protected final String TEXT_63 = NL + "\t    \t    \t+= (tmp_batchUpdateCount_";
  protected final String TEXT_64 = "!=-1?tmp_batchUpdateCount_";
  protected final String TEXT_65 = ":0);                                  " + NL + "                }";
  protected final String TEXT_66 = "        " + NL + "        if(pstmt_";
  protected final String TEXT_67 = " != null) {            " + NL + "\t\t\t";
  protected final String TEXT_68 = NL + "\t\t\t\tpstmt_";
  protected final String TEXT_69 = ".close();" + NL + "\t\t\t";
  protected final String TEXT_70 = NL + "\t\t\t\tSharedDBPreparedStatement.releasePreparedStatement(keyPsmt_";
  protected final String TEXT_71 = ");   " + NL + "\t\t\t";
  protected final String TEXT_72 = "                    " + NL + "        }        ";
  protected final String TEXT_73 = NL;
  protected final String TEXT_74 = NL + "\t\t    " + NL + "\t\t    " + NL + "\t\t    conn_";
  protected final String TEXT_75 = ".commit();" + NL + "\t\t    ";
  protected final String TEXT_76 = "\t" + NL + "    \tconn_";
  protected final String TEXT_77 = " .close();" + NL + "    \t";
  protected final String TEXT_78 = NL + NL + "\tnb_line_deleted_";
  protected final String TEXT_79 = "=nb_line_deleted_";
  protected final String TEXT_80 = "+ deletedCount_";
  protected final String TEXT_81 = ";" + NL + "\tnb_line_update_";
  protected final String TEXT_82 = "=nb_line_update_";
  protected final String TEXT_83 = " + updatedCount_";
  protected final String TEXT_84 = ";" + NL + "\tnb_line_inserted_";
  protected final String TEXT_85 = "=nb_line_inserted_";
  protected final String TEXT_86 = " + insertedCount_";
  protected final String TEXT_87 = ";" + NL + "\tnb_line_rejected_";
  protected final String TEXT_88 = "=nb_line_rejected_";
  protected final String TEXT_89 = " + rejectedCount_";
  protected final String TEXT_90 = ";" + NL + "" + NL + "    globalMap.put(\"";
  protected final String TEXT_91 = "_NB_LINE\",nb_line_";
  protected final String TEXT_92 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_93 = "_NB_LINE_UPDATED\",nb_line_update_";
  protected final String TEXT_94 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_95 = "_NB_LINE_INSERTED\",nb_line_inserted_";
  protected final String TEXT_96 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_97 = "_NB_LINE_DELETED\",nb_line_deleted_";
  protected final String TEXT_98 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_99 = "_NB_LINE_REJECTED\",nb_line_rejected_";
  protected final String TEXT_100 = ");";
  protected final String TEXT_101 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	
	String cid = node.getUniqueName();
	
	String dataAction = ElementParameterParser.getValue(node,"__DATA_ACTION__");
	
	String commitEvery = ElementParameterParser.getValue(node, "__COMMIT_EVERY__");
	
	String dbVersion = ElementParameterParser.getValue(node, "__DB_VERSION__"); 
	boolean isUseBatchByDBVersion = !"ojdbc12-8i.jar".equalsIgnoreCase(dbVersion);	
	
	boolean useExistingConnection = "true".equals(ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__"));
	
    String dieOnError = ElementParameterParser.getValue(node, "__DIE_ON_ERROR__");
	
	String dbschema = ElementParameterParser.getValue(node, "__SCHEMA_DB__");
	
	String tableName = ElementParameterParser.getValue(node,"__TABLE__");
	
	String tableAction = ElementParameterParser.getValue(node,"__TABLE_ACTION__");	
	
	boolean useSpatialOptions = ("true").equals(ElementParameterParser.getValue(node,"__USE_SPATIAL_OPTIONS__"));
	
	boolean createSpatialIndex = ("true").equals(ElementParameterParser.getValue(node,"__SPATIAL_INDEX__"));
	
	String indexAccuracy = ElementParameterParser.getValue(node, "__SPATIAL_INDEX_ACCURACY__");
	
    if (useSpatialOptions && (("DROP_CREATE").equals(tableAction) || ("CREATE").equals(tableAction) 
    		|| ("CREATE_IF_NOT_EXISTS").equals(tableAction) || ("DROP_IF_EXISTS_AND_CREATE").equals(tableAction))) {

	    int targetSRID = -1;
	    try {
	      	targetSRID = Integer.parseInt(ElementParameterParser.getValue(node,"__SRID__"));
	    }
	    catch (NumberFormatException e) {
			
    stringBuffer.append(TEXT_2);
    stringBuffer.append(ElementParameterParser.getValue(node,"__SRID__"));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(e.getMessage());
    stringBuffer.append(TEXT_4);
    
	     	e.printStackTrace();
	    }
		
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(tableName.toUpperCase(java.util.Locale.US) );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(tableName.toUpperCase(java.util.Locale.US) );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(indexAccuracy );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(indexAccuracy );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(targetSRID < 0 ? "\"NULL\"" : targetSRID );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    
	}
	if (createSpatialIndex && useSpatialOptions) {
		
    stringBuffer.append(TEXT_20);
    stringBuffer.append(tableName );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(tableName );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    
	}
    
	
    String rejectConnName = null;
    List<? extends IConnection> rejectConns = node.getOutgoingConnections("REJECT");
    if(rejectConns != null && rejectConns.size() > 0) {
        IConnection rejectConn = rejectConns.get(0);
        rejectConnName = rejectConn.getName();
    }

	
    if(("INSERT_OR_UPDATE").equals(dataAction)) {
        
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    
    } else if(("UPDATE_OR_INSERT").equals(dataAction)) {
        
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    
    } else { // dataAction equals "INSERT" or "UPDATE" or "DELETE" 
        
    if ((rejectConnName==null && isUseBatchByDBVersion && !useExistingConnection) && (("INSERT").equals(dataAction) || ("UPDATE").equals(dataAction) || ("DELETE").equals(dataAction))) {
            
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    if(("true").equals(dieOnError)) {
                	
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    
                	}else {
                	
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    
                	}
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_56);
    if (("INSERT").equals(dataAction)) {
	    	    	
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_58);
    
	    	    	}else if (("UPDATE").equals(dataAction)) {
	    	    	
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    
	    	    	}else if (("DELETE").equals(dataAction)) {
	    	    	
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    
	    	    	}
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_65);
    
            }
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    
			//to fixed: bug8422
			if(!(cid.equals("talendLogs_DB") || cid.equals("talendStats_DB") || cid.equals("talendMeter_DB"))){
			
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    
			}else{
			
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_71);
    
			}
			
    stringBuffer.append(TEXT_72);
    
    }   
    
    stringBuffer.append(TEXT_73);
    
	if(!useExistingConnection)
	{
		if(!("").equals(commitEvery) && !("0").equals(commitEvery))
		{
		    
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_75);
    
		}
		
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_77);
    
	}
    
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_100);
    stringBuffer.append(TEXT_101);
    return stringBuffer.toString();
  }
}
