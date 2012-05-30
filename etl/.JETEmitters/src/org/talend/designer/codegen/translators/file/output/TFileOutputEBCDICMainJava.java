package org.talend.designer.codegen.translators.file.output;

import java.util.List;
import java.util.Map;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.ElementParameterParser;

public class TFileOutputEBCDICMainJava
{
  protected static String nl;
  public static synchronized TFileOutputEBCDICMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileOutputEBCDICMainJava result = new TFileOutputEBCDICMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t" + NL + "\t\t\tjava.util.List record_";
  protected final String TEXT_2 = " = new java.util.ArrayList();";
  protected final String TEXT_3 = NL + "\t\t\trecord_";
  protected final String TEXT_4 = ".add(";
  protected final String TEXT_5 = ".";
  protected final String TEXT_6 = ") ;";
  protected final String TEXT_7 = NL + "        \trwriter_";
  protected final String TEXT_8 = ".writeRecord(record_";
  protected final String TEXT_9 = ");\t" + NL + "        \tnb_line_";
  protected final String TEXT_10 = "++;\t\t";
  protected final String TEXT_11 = NL + "//////////////////////////////////////////////////" + NL + "the original size in the column:";
  protected final String TEXT_12 = " in the schema should be bigger than 0 and DB Type shouldn't be null or Empty" + NL + "//////////////////////////////////////////////////";
  protected final String TEXT_13 = NL + "\t\t\t\t\t\tbyte[] bb_";
  protected final String TEXT_14 = "_";
  protected final String TEXT_15 = " = new byte[";
  protected final String TEXT_16 = "];";
  protected final String TEXT_17 = "\t\t\t\t" + NL + "\t\t\t\t\t\t\tcobolConversion.EbcdicString ";
  protected final String TEXT_18 = "_";
  protected final String TEXT_19 = " = new cobolConversion.EbcdicString(new cobolConversion.EbcdicTable(), bb_";
  protected final String TEXT_20 = "_";
  protected final String TEXT_21 = ", 0 , ";
  protected final String TEXT_22 = ");" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_23 = "_";
  protected final String TEXT_24 = ".toEbcdic(";
  protected final String TEXT_25 = ".";
  protected final String TEXT_26 = ");" + NL + "\t\t\t\t\t\t\tfOut_";
  protected final String TEXT_27 = ".write(";
  protected final String TEXT_28 = "_";
  protected final String TEXT_29 = ".getByteValueArray());";
  protected final String TEXT_30 = NL + "\t\t\t\t\t\t\tcobolConversion.PackedDecimal ";
  protected final String TEXT_31 = "_";
  protected final String TEXT_32 = " = new cobolConversion.PackedDecimal(bb_";
  protected final String TEXT_33 = "_";
  protected final String TEXT_34 = ", 0, ";
  protected final String TEXT_35 = ", ";
  protected final String TEXT_36 = ");" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_37 = "_";
  protected final String TEXT_38 = ".toPackedDecimal(";
  protected final String TEXT_39 = ".";
  protected final String TEXT_40 = ".toPlainString());" + NL + "\t\t\t\t\t\t\tfOut_";
  protected final String TEXT_41 = ".write(";
  protected final String TEXT_42 = "_";
  protected final String TEXT_43 = ".getByteValueArray());";
  protected final String TEXT_44 = NL + "\t\t\t\t\t\t\tjava.math.BigDecimal ";
  protected final String TEXT_45 = "_";
  protected final String TEXT_46 = " = ";
  protected final String TEXT_47 = ".";
  protected final String TEXT_48 = ";" + NL + "\t\t\t\t\t\t\tbyte[] bArr_";
  protected final String TEXT_49 = "_";
  protected final String TEXT_50 = " = ";
  protected final String TEXT_51 = "_";
  protected final String TEXT_52 = ".toPlainString().getBytes(\"Cp037\");" + NL + "\t\t\t\t\t\t\tfor(int j=0;j<bArr_";
  protected final String TEXT_53 = "_";
  protected final String TEXT_54 = ".length;j++){" + NL + "\t\t\t\t\t\t\t\tbb_";
  protected final String TEXT_55 = "_";
  protected final String TEXT_56 = "[bb_";
  protected final String TEXT_57 = "_";
  protected final String TEXT_58 = ".length-(j+1)] = bArr_";
  protected final String TEXT_59 = "_";
  protected final String TEXT_60 = "[bArr_";
  protected final String TEXT_61 = "_";
  protected final String TEXT_62 = ".length-(j+1)];" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\tfOut_";
  protected final String TEXT_63 = ".write(bb_";
  protected final String TEXT_64 = "_";
  protected final String TEXT_65 = ");";
  protected final String TEXT_66 = NL + "\t\t\t\t\t\t\tjava.math.BigInteger ";
  protected final String TEXT_67 = "_";
  protected final String TEXT_68 = " = ";
  protected final String TEXT_69 = ".";
  protected final String TEXT_70 = ".toBigInteger() ;" + NL + "\t\t\t\t\t\t\tbyte[] bArr_";
  protected final String TEXT_71 = "_";
  protected final String TEXT_72 = " = ";
  protected final String TEXT_73 = "_";
  protected final String TEXT_74 = ".toByteArray();" + NL + "\t\t\t\t\t\t\tfor(int j=0;j<bArr_";
  protected final String TEXT_75 = "_";
  protected final String TEXT_76 = ".length;j++){" + NL + "\t\t\t\t\t\t\t\tbb_";
  protected final String TEXT_77 = "_";
  protected final String TEXT_78 = "[j] = bArr_";
  protected final String TEXT_79 = "_";
  protected final String TEXT_80 = "[j];" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\tfOut_";
  protected final String TEXT_81 = ".write(bb_";
  protected final String TEXT_82 = "_";
  protected final String TEXT_83 = ");";
  protected final String TEXT_84 = NL + "\t\t\t\t\t\t\tbyte[] bArr_";
  protected final String TEXT_85 = "_";
  protected final String TEXT_86 = " = ";
  protected final String TEXT_87 = ".";
  protected final String TEXT_88 = ";" + NL + "\t\t\t\t\t\t\tfor(int j=0;j<bArr_";
  protected final String TEXT_89 = "_";
  protected final String TEXT_90 = ".length;j++){" + NL + "\t\t\t\t\t\t\t\tbb_";
  protected final String TEXT_91 = "_";
  protected final String TEXT_92 = "[j] = bArr_";
  protected final String TEXT_93 = "_";
  protected final String TEXT_94 = "[j];" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\tfOut_";
  protected final String TEXT_95 = ".write(bb_";
  protected final String TEXT_96 = "_";
  protected final String TEXT_97 = ");";
  protected final String TEXT_98 = NL + "//////////////////////////////////////////////////" + NL + "DB Type of the column:";
  protected final String TEXT_99 = " should be X, 3, 9, B, T" + NL + "//////////////////////////////////////////////////";
  protected final String TEXT_100 = NL + "\t\t\t\t\t\t\tfOut_";
  protected final String TEXT_101 = ".flush();" + NL;
  protected final String TEXT_102 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
    CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
    INode node = (INode)codeGenArgument.getArgument();
    String cid = node.getUniqueName();	
    String incomingName = (String)codeGenArgument.getIncomingName();
	String customSetOriginalLengthStr = ElementParameterParser.getValue(node,"__NO_X2CJ_FILE__");
	boolean customSetOriginalLength = (customSetOriginalLengthStr!=null&&!("").equals(customSetOriginalLengthStr))?("true").equals(customSetOriginalLengthStr):true;
if(!customSetOriginalLength){//------11111
		List< ? extends IConnection> conns = node.getIncomingConnections();
    	List<IMetadataTable> preMetadatas = null;
		
		for (int i=0;i<conns.size();i++) {
			IConnection conn = conns.get(i);
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {    			
				if( conn.getName() == incomingName ) {				
					preMetadatas = conn.getSource().getMetadataList();

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_2);
    
			for (IMetadataColumn column: preMetadatas.get(0).getListColumns()) {

    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_6);
    
			}

    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    
				}
			}
		}
}else{//------1111
	List<IMetadataTable> metadatas = node.getMetadataList();
    if ((metadatas!=null)&&(metadatas.size()>0)) {//------2222
		List< ? extends IConnection> conns = node.getIncomingConnections();
       	for (IConnection conn : conns) {//------3333
    		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//------4444
    			if( conn.getName() == incomingName ) {	//------5555
	    			IMetadataTable metadata = metadatas.get(0);
	    			List<IMetadataColumn> columns = metadata.getListColumns();
	    			for (int i = 0; i < columns.size(); i++) {//------6666
						IMetadataColumn column = columns.get(i);
						Integer orgainLength = column.getOriginalLength();
						Integer length = column.getLength();
						String orgainType = column.getType();
						Integer precision = column.getPrecision();
						if(precision==null) precision = 0;
						if(orgainLength==null || orgainLength.intValue()==0 || orgainType==null || "".endsWith(orgainType.trim())) {

    stringBuffer.append(TEXT_11);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_12);
    
							continue;
 						}

    stringBuffer.append(TEXT_13);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(orgainLength );
    stringBuffer.append(TEXT_16);
    
						if(orgainType.equals("X")){

    stringBuffer.append(TEXT_17);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(length);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_29);
    
						}else if(orgainType.equals("3")) {

    stringBuffer.append(TEXT_30);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(length);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(precision);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_39);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    
						}else if(orgainType.equals("9")) {

    stringBuffer.append(TEXT_44);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_46);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_48);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_65);
    
						}else if(orgainType.equals("B")) {

    stringBuffer.append(TEXT_66);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_68);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_70);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_74);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_76);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_78);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    
						}else if (orgainType.equals("T")) {

    stringBuffer.append(TEXT_84);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_86);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_88);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_95);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_97);
    
						}else {

    stringBuffer.append(TEXT_98);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_99);
    
						}

    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_101);
    
					}//------66666
				}//------5555
			}//------4444
		}//------3333
	}//------2222
}//------1111

    stringBuffer.append(TEXT_102);
    return stringBuffer.toString();
  }
}
