package org.talend.designer.codegen.translators.processing.fields;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TExtractEBCDICFieldsMainJava
{
  protected static String nl;
  public static synchronized TExtractEBCDICFieldsMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TExtractEBCDICFieldsMainJava result = new TExtractEBCDICFieldsMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL;
  protected final String TEXT_3 = " = null;";
  protected final String TEXT_4 = NL + "\t\t\tbyte [] byteData_";
  protected final String TEXT_5 = " = ";
  protected final String TEXT_6 = ".";
  protected final String TEXT_7 = ";";
  protected final String TEXT_8 = NL + "try{" + NL + "\t";
  protected final String TEXT_9 = " = new ";
  protected final String TEXT_10 = "Struct();";
  protected final String TEXT_11 = NL + "\t\t\t\t\t";
  protected final String TEXT_12 = ".";
  protected final String TEXT_13 = " = ";
  protected final String TEXT_14 = ".";
  protected final String TEXT_15 = ";";
  protected final String TEXT_16 = NL + "\t\t\t\t\t";
  protected final String TEXT_17 = ".";
  protected final String TEXT_18 = " = ";
  protected final String TEXT_19 = ".";
  protected final String TEXT_20 = ";";
  protected final String TEXT_21 = NL + "\t\tbbRecord_";
  protected final String TEXT_22 = " = java.nio.ByteBuffer.wrap(byteData_";
  protected final String TEXT_23 = ");";
  protected final String TEXT_24 = NL + "//////////////////////////////////////////////////" + NL + "the original size in the column:";
  protected final String TEXT_25 = " in the schema should be bigger than 0 and DB Type shouldn't be null or Empty" + NL + "//////////////////////////////////////////////////";
  protected final String TEXT_26 = NL + "\t\t\tbyte[] bb_";
  protected final String TEXT_27 = "_";
  protected final String TEXT_28 = " = new byte[";
  protected final String TEXT_29 = "];" + NL + "\t\t\tbbRecord_";
  protected final String TEXT_30 = ".get(bb_";
  protected final String TEXT_31 = "_";
  protected final String TEXT_32 = ");";
  protected final String TEXT_33 = "\t\t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_34 = ".";
  protected final String TEXT_35 = " = new String(bb_";
  protected final String TEXT_36 = "_";
  protected final String TEXT_37 = ",";
  protected final String TEXT_38 = ")";
  protected final String TEXT_39 = ";";
  protected final String TEXT_40 = NL + "\t\t\t\t";
  protected final String TEXT_41 = ".";
  protected final String TEXT_42 = " = new java.lang.Float(java.nio.ByteBuffer.wrap(bb_";
  protected final String TEXT_43 = "_";
  protected final String TEXT_44 = ").order(java.nio.ByteOrder.BIG_ENDIAN).getFloat());";
  protected final String TEXT_45 = NL + "\t\t\t\t";
  protected final String TEXT_46 = ".";
  protected final String TEXT_47 = " = new java.lang.Double(java.nio.ByteBuffer.wrap(bb_";
  protected final String TEXT_48 = "_";
  protected final String TEXT_49 = ").order(java.nio.ByteOrder.BIG_ENDIAN).getDouble());";
  protected final String TEXT_50 = NL + "\t\t\t\tcobolConversion.PackedDecimal pd_";
  protected final String TEXT_51 = "_";
  protected final String TEXT_52 = " = new cobolConversion.PackedDecimal(bb_";
  protected final String TEXT_53 = "_";
  protected final String TEXT_54 = ", 0, ";
  protected final String TEXT_55 = ", ";
  protected final String TEXT_56 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_57 = ".";
  protected final String TEXT_58 = " = new java.math.BigDecimal(pd_";
  protected final String TEXT_59 = "_";
  protected final String TEXT_60 = ".toString());";
  protected final String TEXT_61 = NL + "\t\t\t\t";
  protected final String TEXT_62 = ".";
  protected final String TEXT_63 = " = new java.math.BigDecimal(new String(bb_";
  protected final String TEXT_64 = "_";
  protected final String TEXT_65 = ",";
  protected final String TEXT_66 = "));";
  protected final String TEXT_67 = NL + "\t\t\t\t";
  protected final String TEXT_68 = ".";
  protected final String TEXT_69 = " = new java.math.BigDecimal(new java.math.BigInteger(bb_";
  protected final String TEXT_70 = "_";
  protected final String TEXT_71 = "),0);";
  protected final String TEXT_72 = NL + "\t\t\t\t";
  protected final String TEXT_73 = ".";
  protected final String TEXT_74 = " = bb_";
  protected final String TEXT_75 = "_";
  protected final String TEXT_76 = ";";
  protected final String TEXT_77 = NL + "//////////////////////////////////////////////////" + NL + "DB Type of the column:";
  protected final String TEXT_78 = " should be X, 1, 2, 3, 9, B, T" + NL + "//////////////////////////////////////////////////";
  protected final String TEXT_79 = NL + "\t\t";
  protected final String TEXT_80 = " = null;";
  protected final String TEXT_81 = NL + "\tbbRecord_";
  protected final String TEXT_82 = ".clear();" + NL + "\tnb_line_";
  protected final String TEXT_83 = "++;" + NL + "}catch(Exception ex_";
  protected final String TEXT_84 = "){";
  protected final String TEXT_85 = NL + "\t\tthrow(ex_";
  protected final String TEXT_86 = ");";
  protected final String TEXT_87 = NL + "\t\t\t";
  protected final String TEXT_88 = " = new ";
  protected final String TEXT_89 = "Struct();";
  protected final String TEXT_90 = NL + "    \t\t\t";
  protected final String TEXT_91 = ".";
  protected final String TEXT_92 = " = ";
  protected final String TEXT_93 = ".";
  protected final String TEXT_94 = ";";
  protected final String TEXT_95 = "    " + NL + "\t\t    ";
  protected final String TEXT_96 = ".errorMessage = ex_";
  protected final String TEXT_97 = ".getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_98 = ";" + NL + "\t\t    ";
  protected final String TEXT_99 = " = null;";
  protected final String TEXT_100 = NL + "\t\t    System.err.println(ex_";
  protected final String TEXT_101 = ".getMessage());" + NL + "\t\t    ";
  protected final String TEXT_102 = " = null;";
  protected final String TEXT_103 = NL + "\t\t\t";
  protected final String TEXT_104 = ".errorMessage = ex_";
  protected final String TEXT_105 = ".getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_106 = ";";
  protected final String TEXT_107 = NL + "}";
  protected final String TEXT_108 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();

String field = ElementParameterParser.getValue(node, "__FIELD__");

String dieOnErrorStr = ElementParameterParser.getValue(node, "__DIE_ON_ERROR__");
boolean dieOnError = (dieOnErrorStr!=null&&!("").equals(dieOnErrorStr))?("true").equals(dieOnErrorStr):false;
String encoding  = ElementParameterParser.getValue(node, "__ENCODING__");
List<Map<String, String>> trimSelects = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__TRIMSELECT__");
String isTrimAllStr = ElementParameterParser.getValue(node,"__TRIMALL__");
boolean isTrimAll = (isTrimAllStr!=null&&!("").equals(isTrimAllStr))?("true").equals(isTrimAllStr):true;

IConnection inConn = null;
Integer byteLength = null;
List< ? extends IConnection> inConns = node.getIncomingConnections();
if(inConns!=null){
    for (IConnection conn : inConns) {
    	if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
    		inConn = conn;
    		break;
    	}
    }
}

String rejectConnName = "";
List<IMetadataColumn> rejectColumnList = null;
List<? extends IConnection> rejectConns = node.getOutgoingConnections("REJECT");
if(rejectConns != null && rejectConns.size() > 0) {
    IConnection rejectConn = rejectConns.get(0);
    rejectColumnList = rejectConn.getMetadataTable().getListColumns();
    rejectConnName = rejectConn.getName();
}

IConnection outConn = null;
String firstConnName = "";
List< ? extends IConnection> outConns = node.getOutgoingConnections();
if(outConns!=null){
    for (IConnection conn : outConns) {
    	if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
    		outConn = conn;
    		firstConnName = outConn.getName();
    		break;
    	}
    }
}

if(outConns!=null){
    for (IConnection conn : outConns) {
    	if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {

    stringBuffer.append(TEXT_2);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_3);
    
    	}
    }
}

//get field column
if(inConn!=null){
	IMetadataTable inputMetadataTable = inConn.getMetadataTable();
	for (IMetadataColumn inputCol : inputMetadataTable.getListColumns()) {
		if(inputCol.getLabel().equals(field) && JavaTypesManager.getJavaTypeFromId(inputCol.getTalendType()) == JavaTypesManager.BYTE_ARRAY){
			byteLength = inputCol.getLength();

    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(inConn.getName());
    stringBuffer.append(TEXT_6);
    stringBuffer.append(inputCol.getLabel());
    stringBuffer.append(TEXT_7);
    
			break;
		}
	}
}

//set original columns
List<IMetadataColumn> newColumnList = new ArrayList<IMetadataColumn>();
if(outConn!=null && inConn!=null){

    stringBuffer.append(TEXT_8);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_10);
    
	IMetadataTable outputMetadataTable = outConn.getMetadataTable();
	IMetadataTable inputMetadataTable = inConn.getMetadataTable();
	for (IMetadataColumn outputCol : outputMetadataTable.getListColumns()) {
		if(outputCol.getLabel().equals(field)){
			continue;
		}
		boolean isOirginalColumn = false;
		for(IMetadataColumn inputCol : inputMetadataTable.getListColumns()){	
			JavaType stringType =  JavaTypesManager.getJavaTypeFromId(inputCol.getTalendType());
			if( outputCol.getLabel().equals( inputCol.getLabel()) ){
				isOirginalColumn = true;
				if(stringType == JavaTypesManager.STRING){
					boolean trimStr = false;
					if(trimSelects!=null){
						for(Map<String, String> mapTrim : trimSelects){
							if(outputCol.getLabel().equals(mapTrim.get("SCHEMA_COLUMN")) && "true".equals(mapTrim.get("TRIM"))){
								trimStr = true;
								break;
							}
						}
					}

    stringBuffer.append(TEXT_11);
    stringBuffer.append(outConn.getName());
    stringBuffer.append(TEXT_12);
    stringBuffer.append(outputCol.getLabel());
    stringBuffer.append(TEXT_13);
    stringBuffer.append(inConn.getName());
    stringBuffer.append(TEXT_14);
    stringBuffer.append(inputCol.getLabel());
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && (trimStr)))?".trim()":"" );
    stringBuffer.append(TEXT_15);
    
				}else{

    stringBuffer.append(TEXT_16);
    stringBuffer.append(outConn.getName());
    stringBuffer.append(TEXT_17);
    stringBuffer.append(outputCol.getLabel());
    stringBuffer.append(TEXT_18);
    stringBuffer.append(inConn.getName());
    stringBuffer.append(TEXT_19);
    stringBuffer.append(inputCol.getLabel());
    stringBuffer.append(TEXT_20);
    
				}
				break;
			}
		}
	
		if(!isOirginalColumn){
			if(!("").equals(rejectConnName)&&rejectConnName.equals(firstConnName)
				&& (outputCol.getLabel().equals("errorMessage") || outputCol.getLabel().equals("errorCode"))){
			}else{
				newColumnList.add(outputCol);
			}
		}
	}

    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    
	for(int valueN = 0 ; valueN < newColumnList.size();valueN++){//----- for begin
			IMetadataColumn column = newColumnList.get(valueN);
			Integer orgainLength = column.getOriginalLength();
			Integer length = column.getLength();
			String orgainType = column.getType();
			Integer precision = column.getPrecision();
			if(precision==null) precision = 0;
			if(orgainLength==null || orgainLength.intValue()==0 || orgainType==null || "".endsWith(orgainType.trim())) {

    stringBuffer.append(TEXT_24);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_25);
    
				continue;
			}

    stringBuffer.append(TEXT_26);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_28);
    stringBuffer.append(orgainLength );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_32);
    
			if(orgainType.equals("X")){
					boolean trimStr = false;
					if(trimSelects!=null){
						for(Map<String, String> mapTrim : trimSelects){
							if(column.getLabel().equals(mapTrim.get("SCHEMA_COLUMN")) && "true".equals(mapTrim.get("TRIM"))){
								trimStr = true;
								break;
							}
						}
					}

    stringBuffer.append(TEXT_33);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_35);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_37);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_38);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && (trimStr)))?".trim()":"" );
    stringBuffer.append(TEXT_39);
    
			}else if(orgainType.equals("1")) {

    stringBuffer.append(TEXT_40);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_42);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_44);
    
			}else if(orgainType.equals("2")) {

    stringBuffer.append(TEXT_45);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_47);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_49);
    
			}else if(orgainType.equals("3")) {

    stringBuffer.append(TEXT_50);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_52);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_54);
    stringBuffer.append(length);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(precision );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_58);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_60);
    
			}else if(orgainType.equals("9")) {

    stringBuffer.append(TEXT_61);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_63);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_65);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_66);
    
			}else if(orgainType.equals("B")) {

    stringBuffer.append(TEXT_67);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_69);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_71);
    
			}else if (orgainType.equals("T")) {

    stringBuffer.append(TEXT_72);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_74);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_76);
    
			}else {

    stringBuffer.append(TEXT_77);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_78);
    
			}
	}//----- for end
	if(!("").equals(rejectConnName) && rejectConnName.equals(firstConnName)){

    stringBuffer.append(TEXT_79);
    stringBuffer.append(outConn.getName() );
    stringBuffer.append(TEXT_80);
    
	}

    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    
	if(dieOnError){

    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    
	}else{
		if(!("").equals(rejectConnName)&&!rejectConnName.equals(firstConnName)&&rejectColumnList != null && rejectColumnList.size() > 0) {

    stringBuffer.append(TEXT_87);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_89);
    
            for(IMetadataColumn column : outConn.getMetadataTable().getListColumns()) {

    stringBuffer.append(TEXT_90);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_91);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_92);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_94);
    
			}

    stringBuffer.append(TEXT_95);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_99);
    
		} else if(("").equals(rejectConnName)){

    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_102);
    
		} else if(rejectConnName.equals(firstConnName)){

    stringBuffer.append(TEXT_103);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_106);
    
		}
	}

    stringBuffer.append(TEXT_107);
    
}

    stringBuffer.append(TEXT_108);
    return stringBuffer.toString();
  }
}
