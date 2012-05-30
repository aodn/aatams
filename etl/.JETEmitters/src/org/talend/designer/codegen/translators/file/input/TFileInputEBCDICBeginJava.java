package org.talend.designer.codegen.translators.file.input;

import java.util.List;
import java.util.Map;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class TFileInputEBCDICBeginJava
{
  protected static String nl;
  public static synchronized TFileInputEBCDICBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileInputEBCDICBeginJava result = new TFileInputEBCDICBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t// open file" + NL + "\tjavax.xml.bind.JAXBContext jaxbContext_";
  protected final String TEXT_2 = " = javax.xml.bind.JAXBContext.newInstance(\"net.sf.cobol2j\");" + NL + "\tjavax.xml.bind.Unmarshaller unmarshaller_";
  protected final String TEXT_3 = " = jaxbContext_";
  protected final String TEXT_4 = ".createUnmarshaller();" + NL + "\tObject o_";
  protected final String TEXT_5 = " = unmarshaller_";
  protected final String TEXT_6 = ".unmarshal(new java.io.FileInputStream(";
  protected final String TEXT_7 = "));" + NL + "\tnet.sf.cobol2j.FileFormat fF_";
  protected final String TEXT_8 = " = (net.sf.cobol2j.FileFormat) o_";
  protected final String TEXT_9 = ";" + NL + "\tnet.sf.cobol2j.RecordSet rset_";
  protected final String TEXT_10 = " = new net.sf.cobol2j.RecordSet(new java.io.FileInputStream(";
  protected final String TEXT_11 = "), fF_";
  protected final String TEXT_12 = ");" + NL + "\tjava.util.Map recdefs_";
  protected final String TEXT_13 = " = new net.sf.cobol2j.RecordsMap(fF_";
  protected final String TEXT_14 = ");" + NL + "\t" + NL + "\t// read every record, for each record split into column definition" + NL + "\tList inrecord_";
  protected final String TEXT_15 = ";" + NL + "\twhile (rset_";
  protected final String TEXT_16 = ".hasNext()){" + NL + "\t\tinrecord_";
  protected final String TEXT_17 = " = rset_";
  protected final String TEXT_18 = ".next();" + NL + "\t\t{" + NL + "\t\t\t";
  protected final String TEXT_19 = NL + "\t\t\t\t\t";
  protected final String TEXT_20 = " = null;\t\t\t";
  protected final String TEXT_21 = NL + "\t\t\tif(inrecord_";
  protected final String TEXT_22 = ".get(0).equals(\"";
  protected final String TEXT_23 = "\") || recdefs_";
  protected final String TEXT_24 = ".size() == 1 ){" + NL + "\t" + NL + "\t\t\t\t";
  protected final String TEXT_25 = " = new ";
  protected final String TEXT_26 = "Struct();";
  protected final String TEXT_27 = "\t\t\t\t" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_28 = ".";
  protected final String TEXT_29 = " = (String)inrecord_";
  protected final String TEXT_30 = ".get(";
  protected final String TEXT_31 = ");";
  protected final String TEXT_32 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_33 = ".";
  protected final String TEXT_34 = " = (";
  protected final String TEXT_35 = ")inrecord_";
  protected final String TEXT_36 = ".get(";
  protected final String TEXT_37 = ");";
  protected final String TEXT_38 = "\t" + NL + "\t\t\t}\t";
  protected final String TEXT_39 = NL + "//////////////////////////////////////////////////" + NL + "the original size in the column:";
  protected final String TEXT_40 = " should be bigger than 0" + NL + "//////////////////////////////////////////////////";
  protected final String TEXT_41 = NL + "\t\t\tjava.io.FileInputStream fs_";
  protected final String TEXT_42 = " = new java.io.FileInputStream(";
  protected final String TEXT_43 = ");" + NL + "\t\t\tjava.nio.channels.FileChannel inChannel_";
  protected final String TEXT_44 = "  = fs_";
  protected final String TEXT_45 = ".getChannel();" + NL + "\t\t\tjava.nio.ByteBuffer bbRecord_";
  protected final String TEXT_46 = " = java.nio.ByteBuffer.allocateDirect(";
  protected final String TEXT_47 = ");" + NL + "\t\t\tlong iAvailabel_";
  protected final String TEXT_48 = " = fs_";
  protected final String TEXT_49 = ".available();" + NL + "\t\t\tint iReadOffset_";
  protected final String TEXT_50 = " = 0;" + NL + "\t\t\twhile(iAvailabel_";
  protected final String TEXT_51 = " > 0 ){" + NL + "\t\t\t\tiAvailabel_";
  protected final String TEXT_52 = " = iAvailabel_";
  protected final String TEXT_53 = " - ";
  protected final String TEXT_54 = ";" + NL + "\t\t\t\tiReadOffset_";
  protected final String TEXT_55 = " = inChannel_";
  protected final String TEXT_56 = ".read(bbRecord_";
  protected final String TEXT_57 = ");" + NL + "\t\t\t\tbbRecord_";
  protected final String TEXT_58 = ".flip();";
  protected final String TEXT_59 = NL + "//////////////////////////////////////////////////" + NL + "the original size in the column:";
  protected final String TEXT_60 = " in the schema should be bigger than 0 and DB Type shouldn't be null or Empty" + NL + "//////////////////////////////////////////////////";
  protected final String TEXT_61 = NL + "\t\t\t\t\t\tbyte[] bb_";
  protected final String TEXT_62 = "_";
  protected final String TEXT_63 = " = new byte[";
  protected final String TEXT_64 = "];" + NL + "\t\t\t\t\t\tbbRecord_";
  protected final String TEXT_65 = ".get(bb_";
  protected final String TEXT_66 = "_";
  protected final String TEXT_67 = ");";
  protected final String TEXT_68 = "\t\t\t\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_69 = ".";
  protected final String TEXT_70 = " = new String(bb_";
  protected final String TEXT_71 = "_";
  protected final String TEXT_72 = ",";
  protected final String TEXT_73 = ")";
  protected final String TEXT_74 = ";";
  protected final String TEXT_75 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_76 = ".";
  protected final String TEXT_77 = " = new java.lang.Float(java.nio.ByteBuffer.wrap(bb_";
  protected final String TEXT_78 = "_";
  protected final String TEXT_79 = ").order(java.nio.ByteOrder.BIG_ENDIAN).getFloat());";
  protected final String TEXT_80 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_81 = ".";
  protected final String TEXT_82 = " = new java.lang.Double(java.nio.ByteBuffer.wrap(bb_";
  protected final String TEXT_83 = "_";
  protected final String TEXT_84 = ").order(java.nio.ByteOrder.BIG_ENDIAN).getDouble());";
  protected final String TEXT_85 = NL + "\t\t\t\t\t\t\tcobolConversion.PackedDecimal pd_";
  protected final String TEXT_86 = "_";
  protected final String TEXT_87 = " = new cobolConversion.PackedDecimal(bb_";
  protected final String TEXT_88 = "_";
  protected final String TEXT_89 = ", 0, ";
  protected final String TEXT_90 = ", ";
  protected final String TEXT_91 = ");" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_92 = ".";
  protected final String TEXT_93 = " = new java.math.BigDecimal(pd_";
  protected final String TEXT_94 = "_";
  protected final String TEXT_95 = ".toString());";
  protected final String TEXT_96 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_97 = ".";
  protected final String TEXT_98 = " = new java.math.BigDecimal(new String(bb_";
  protected final String TEXT_99 = "_";
  protected final String TEXT_100 = ",";
  protected final String TEXT_101 = "));";
  protected final String TEXT_102 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_103 = ".";
  protected final String TEXT_104 = " = new java.math.BigDecimal(new java.math.BigInteger(bb_";
  protected final String TEXT_105 = "_";
  protected final String TEXT_106 = "),0);";
  protected final String TEXT_107 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_108 = ".";
  protected final String TEXT_109 = " = bb_";
  protected final String TEXT_110 = "_";
  protected final String TEXT_111 = ";";
  protected final String TEXT_112 = NL + "//////////////////////////////////////////////////" + NL + "DB Type of the column:";
  protected final String TEXT_113 = " should be X, 1, 2, 3, 9, B, T" + NL + "//////////////////////////////////////////////////";
  protected final String TEXT_114 = NL + "\t\t\t\tbbRecord_";
  protected final String TEXT_115 = ".clear();";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
    CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
    String cid = node.getUniqueName();
    
	String filename = ElementParameterParser.getValue(node,"__FILENAME__");
	String copybook = ElementParameterParser.getValue(node,"__COPYBOOK__");
	String encoding = ElementParameterParser.getValue(node,"__ENCODING__");
	String customSetOriginalLengthStr = ElementParameterParser.getValue(node,"__NO_X2CJ_FILE__");
	boolean customSetOriginalLength = (customSetOriginalLengthStr!=null&&!("").equals(customSetOriginalLengthStr))?("true").equals(customSetOriginalLengthStr):true;
	List<Map<String, String>> trimSelects = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__TRIMSELECT__");
	String isTrimAllStr = ElementParameterParser.getValue(node,"__TRIMALL__");
	boolean isTrimAll = (isTrimAllStr!=null&&!("").equals(isTrimAllStr))?("true").equals(isTrimAllStr):true;
if(!customSetOriginalLength){//------11111

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( copybook );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append( filename );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    			List< ? extends IConnection> conns = node.getOutgoingSortedConnections();

    		if (conns!=null && conns.size()>0) {
				for (int i=0;i<conns.size();i++) {
					IConnection connTemp = conns.get(i);
					if (connTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {

    stringBuffer.append(TEXT_19);
    stringBuffer.append(connTemp.getName() );
    stringBuffer.append(TEXT_20);
    
					}
				}
    		}
    		
	List<Map<String, String>> schemas = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__SCHEMAS__");
	for(Map<String, String> schemaMap : schemas) {//------AAA0
		String schemaName = schemaMap.get("SCHEMA");
		String code = schemaMap.get("CODE");
	
		IConnection justConn = null; //------->get the right output connection--->to get the columns info
		if(conns != null && conns.size() > 0){
			for(IConnection conn : conns){
				if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
					if(schemaName.equals(conn.getMetadataTable().getLabel())){
						justConn = conn;
						break;
					}
				}
			}
		}

		if(justConn != null){//------BBB0
		IMetadataTable justMetadata = justConn.getMetadataTable();
		List<IMetadataColumn> justColumnList = justMetadata.getListColumns();

    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(code );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(justConn.getName() );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(justConn.getName() );
    stringBuffer.append(TEXT_26);
    
				int sizeListColumns = justColumnList.size();
				for (int valueN=0; valueN<sizeListColumns; valueN++) {
					IMetadataColumn column = justColumnList.get(valueN);
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					if(javaType == JavaTypesManager.STRING){

    stringBuffer.append(TEXT_27);
    stringBuffer.append(justConn.getName() );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_31);
    
					}else {

    stringBuffer.append(TEXT_32);
    stringBuffer.append(justConn.getName() );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_34);
    stringBuffer.append(javaType.getLabel() );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_37);
    
					}
				}

    stringBuffer.append(TEXT_38);
    
		}//------BBB0
	}//------AAA0
}else{//------11111
	List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
	if(conns!=null && conns.size()>0){//------conns
		IConnection conn = conns.get(0);
			int totalRealSize = 0;
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//--------AAA00
				IMetadataTable justMetadata = conn.getMetadataTable();
				List<IMetadataColumn> justColumnList = justMetadata.getListColumns();
				int sizeListColumns = justColumnList.size();
				for (int valueN=0; valueN<sizeListColumns; valueN++) {//------BBB00
					IMetadataColumn column = justColumnList.get(valueN);
					Integer orgainLength  = column.getOriginalLength();
					if(orgainLength==null || orgainLength.intValue()==0) {

    stringBuffer.append(TEXT_39);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_40);
    
						break;
					}
					totalRealSize = totalRealSize + orgainLength.intValue();
				}//------BBB00
			}//--------AAA00

    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(filename );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(totalRealSize);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(totalRealSize);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    
				if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//------BBB1
					IMetadataTable justMetadata = conn.getMetadataTable();
					List<IMetadataColumn> justColumnList = justMetadata.getListColumns();
					int sizeListColumns = justColumnList.size();
					for (int valueN=0; valueN<sizeListColumns; valueN++) {//------BBB12
						IMetadataColumn column = justColumnList.get(valueN);
						Integer orgainLength = column.getOriginalLength();
						Integer length = column.getLength();
						String orgainType = column.getType();
						Integer precision = column.getPrecision();
						if(precision==null) precision = 0;
						if(orgainLength==null || orgainLength.intValue()==0 || orgainType==null || "".endsWith(orgainType.trim())) {

    stringBuffer.append(TEXT_59);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_60);
    
							continue;
						}

    stringBuffer.append(TEXT_61);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(orgainLength );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
    
						if(orgainType.equals("X")){

    stringBuffer.append(TEXT_68);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_70);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_73);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(valueN).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_74);
    
						}else if(orgainType.equals("1")) {

    stringBuffer.append(TEXT_75);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_77);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    
						}else if(orgainType.equals("2")) {

    stringBuffer.append(TEXT_80);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_82);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_84);
    
						}else if(orgainType.equals("3")) {

    stringBuffer.append(TEXT_85);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_87);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(length);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(precision);
    stringBuffer.append(TEXT_91);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_92);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_93);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_95);
    
						}else if(orgainType.equals("9")) {

    stringBuffer.append(TEXT_96);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_98);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_100);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_101);
    
						}else if(orgainType.equals("B")) {

    stringBuffer.append(TEXT_102);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_104);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_106);
    
						}else if (orgainType.equals("T")) {

    stringBuffer.append(TEXT_107);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_109);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_111);
    
						}else {

    stringBuffer.append(TEXT_112);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_113);
    
						}
					}//------BBB12
				}//------BBB1

    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    
	}//------conns
}//------11111

    return stringBuffer.toString();
  }
}
