package org.talend.designer.codegen.translators.file.output;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import java.util.Map;

public class TFileOutputLDIFMainJava
{
  protected static String nl;
  public static synchronized TFileOutputLDIFMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileOutputLDIFMainJava result = new TFileOutputLDIFMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\t//////////////////////////" + NL + "\t\tboolean encodingBase64 = false;";
  protected final String TEXT_2 = " " + NL + "\t\tStringBuilder sb_";
  protected final String TEXT_3 = " = new StringBuilder();" + NL + "\t\tboolean needSeparator_";
  protected final String TEXT_4 = " = false;" + NL + "\t\tboolean canOutput_";
  protected final String TEXT_5 = " = false;\t\t\t\t   \t\t";
  protected final String TEXT_6 = NL + "\t\tencodingBase64 = false;" + NL + "\t\tString value_";
  protected final String TEXT_7 = "_";
  protected final String TEXT_8 = " = \"\";";
  protected final String TEXT_9 = "   \t\t\t\t" + NL + "\t    if(";
  protected final String TEXT_10 = ".";
  protected final String TEXT_11 = " != null && !(\"\").equals(";
  protected final String TEXT_12 = ".";
  protected final String TEXT_13 = ")) {";
  protected final String TEXT_14 = NL + "    \tvalue_";
  protected final String TEXT_15 = "_";
  protected final String TEXT_16 = " =  \t\t\t\t\t\t";
  protected final String TEXT_17 = NL + "\t\tFormatterUtils.format_Date(";
  protected final String TEXT_18 = ".";
  protected final String TEXT_19 = ", ";
  protected final String TEXT_20 = ");";
  protected final String TEXT_21 = NL + "\t\t";
  protected final String TEXT_22 = ".";
  protected final String TEXT_23 = ";";
  protected final String TEXT_24 = NL + "\t\tString.valueOf(";
  protected final String TEXT_25 = ");";
  protected final String TEXT_26 = NL + "\t\torg.apache.commons.codec.binary.Base64.encodeBase64String(";
  protected final String TEXT_27 = ".";
  protected final String TEXT_28 = ");";
  protected final String TEXT_29 = NL + "\t\tString.valueOf(";
  protected final String TEXT_30 = ".";
  protected final String TEXT_31 = ");";
  protected final String TEXT_32 = "\t\t" + NL + "\t\tutil_";
  protected final String TEXT_33 = ".breakString(sb_";
  protected final String TEXT_34 = ", dn_";
  protected final String TEXT_35 = " + value_";
  protected final String TEXT_36 = "_";
  protected final String TEXT_37 = ", wrap_";
  protected final String TEXT_38 = ");";
  protected final String TEXT_39 = "\t\t" + NL + "        util_";
  protected final String TEXT_40 = ".breakString(sb_";
  protected final String TEXT_41 = ", changetype_";
  protected final String TEXT_42 = " + \"";
  protected final String TEXT_43 = "\", wrap_";
  protected final String TEXT_44 = ");";
  protected final String TEXT_45 = NL;
  protected final String TEXT_46 = NL + "\tcanOutput_";
  protected final String TEXT_47 = " = true;        ";
  protected final String TEXT_48 = " " + NL;
  protected final String TEXT_49 = NL + "\t    }";
  protected final String TEXT_50 = "   ";
  protected final String TEXT_51 = "\t\t\t\t\t\t";
  protected final String TEXT_52 = NL + "\t    }";
  protected final String TEXT_53 = " \t\t" + NL + "\t";
  protected final String TEXT_54 = NL + "\t\t\t\t\t\t// Convert binary or Base64 value to Base64 encoded string\t\t\t\t" + NL + "\t\t\t\t\t\tencodingBase64 = true;" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_55 = NL;
  protected final String TEXT_56 = NL + "\tif(needSeparator_";
  protected final String TEXT_57 = "){" + NL + "\t\tsb_";
  protected final String TEXT_58 = ".append(\"-\\n\");" + NL + "\t}  " + NL + "\tutil_";
  protected final String TEXT_59 = ".breakString(sb_";
  protected final String TEXT_60 = ", \"";
  protected final String TEXT_61 = ": \" + \"";
  protected final String TEXT_62 = "\", wrap_";
  protected final String TEXT_63 = ");" + NL + "\t";
  protected final String TEXT_64 = "\t\t" + NL + "\t\tString[] values_";
  protected final String TEXT_65 = "_";
  protected final String TEXT_66 = " = value_";
  protected final String TEXT_67 = "_";
  protected final String TEXT_68 = ".split(";
  protected final String TEXT_69 = ");" + NL + "\t\tfor(String item_";
  protected final String TEXT_70 = " : values_";
  protected final String TEXT_71 = "_";
  protected final String TEXT_72 = "){" + NL + "\t\t\t";
  protected final String TEXT_73 = NL + "\t\t\t\t// If content doesn't follow LDIF rules, it must be base64 encoded" + NL + "\t\t\t\tif(!netscape.ldap.util.LDIF.isPrintable(item_";
  protected final String TEXT_74 = ".getBytes(";
  protected final String TEXT_75 = "))){" + NL + "\t\t\t\t\tencodingBase64 = true;" + NL + "\t\t\t\t}" + NL + "\t\t\t\telse{" + NL + "\t\t\t\t\tencodingBase64 = false;" + NL + "\t\t\t\t}" + NL + "\t\t\t\t";
  protected final String TEXT_76 = NL + "\t\t\t// Add \":\" to comply with base64 ldif syntax" + NL + "\t\t\t\titem_";
  protected final String TEXT_77 = " = util_";
  protected final String TEXT_78 = ".getBase64StringOrNot(encodingBase64,item_";
  protected final String TEXT_79 = ",";
  protected final String TEXT_80 = ");" + NL + "\t\t\t\tutil_";
  protected final String TEXT_81 = ".breakString(sb_";
  protected final String TEXT_82 = ", \"";
  protected final String TEXT_83 = ";binary";
  protected final String TEXT_84 = ":";
  protected final String TEXT_85 = ":";
  protected final String TEXT_86 = " \" + item_";
  protected final String TEXT_87 = ", wrap_";
  protected final String TEXT_88 = ");" + NL + "\t\t}\t\t\t\t";
  protected final String TEXT_89 = NL + "\t\t\t// If content doesn't follow LDIF rules, it must be base64 encoded" + NL + "\t\t\tif(!netscape.ldap.util.LDIF.isPrintable(value_";
  protected final String TEXT_90 = "_";
  protected final String TEXT_91 = ".getBytes(";
  protected final String TEXT_92 = "))){" + NL + "\t\t\t\tencodingBase64 = true;" + NL + "\t\t\t}" + NL + "\t\t\t";
  protected final String TEXT_93 = NL + "\t\t// Add \":\" to comply with base64 ldif syntax" + NL + "\t\t\tvalue_";
  protected final String TEXT_94 = "_";
  protected final String TEXT_95 = " = util_";
  protected final String TEXT_96 = ".getBase64StringOrNot(encodingBase64,value_";
  protected final String TEXT_97 = "_";
  protected final String TEXT_98 = ",";
  protected final String TEXT_99 = ");" + NL + "\t\t\tutil_";
  protected final String TEXT_100 = ".breakString(sb_";
  protected final String TEXT_101 = ", \"";
  protected final String TEXT_102 = ";binary";
  protected final String TEXT_103 = ":";
  protected final String TEXT_104 = ":";
  protected final String TEXT_105 = " \" + value_";
  protected final String TEXT_106 = "_";
  protected final String TEXT_107 = ", wrap_";
  protected final String TEXT_108 = ");";
  protected final String TEXT_109 = NL + "\tneedSeparator_";
  protected final String TEXT_110 = " = true;" + NL + "\tcanOutput_";
  protected final String TEXT_111 = " = true;   \t";
  protected final String TEXT_112 = NL + "sb_";
  protected final String TEXT_113 = ".append(\"-\\n\");";
  protected final String TEXT_114 = NL + "\t\t\t\t\t\t// Convert binary or Base64 value to Base64 encoded string\t\t\t\t" + NL + "\t\t\t\t\t\tencodingBase64 = true;" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_115 = NL + "\t";
  protected final String TEXT_116 = NL + "\t\tString[] values_";
  protected final String TEXT_117 = "_";
  protected final String TEXT_118 = " = value_";
  protected final String TEXT_119 = "_";
  protected final String TEXT_120 = ".split(";
  protected final String TEXT_121 = ");" + NL + "\t\tfor(String item_";
  protected final String TEXT_122 = " : values_";
  protected final String TEXT_123 = "_";
  protected final String TEXT_124 = "){" + NL + "\t\t\t\t";
  protected final String TEXT_125 = NL + "\t\t\t\t\t// If content doesn't follow LDIF rules, it must be base64 encoded" + NL + "\t\t\t\t\tif(!netscape.ldap.util.LDIF.isPrintable(item_";
  protected final String TEXT_126 = ".getBytes(";
  protected final String TEXT_127 = "))){" + NL + "\t\t\t\t\t\tencodingBase64 = true;" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\telse{" + NL + "\t\t\t\t\t\tencodingBase64 = false;" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\t";
  protected final String TEXT_128 = NL + "\t\t\t\t// Add \":\" to comply with base64 ldif syntax" + NL + "\t                item_";
  protected final String TEXT_129 = " = util_";
  protected final String TEXT_130 = ".getBase64StringOrNot(encodingBase64,item_";
  protected final String TEXT_131 = ",";
  protected final String TEXT_132 = ");" + NL + "\t\t\t\t\tutil_";
  protected final String TEXT_133 = ".breakString(sb_";
  protected final String TEXT_134 = ", \"";
  protected final String TEXT_135 = ";binary";
  protected final String TEXT_136 = ":";
  protected final String TEXT_137 = ":";
  protected final String TEXT_138 = " \" + item_";
  protected final String TEXT_139 = ", wrap_";
  protected final String TEXT_140 = ");" + NL + "\t\t}";
  protected final String TEXT_141 = NL + "\t\t\t\t\t// If content doesn't follow LDIF rules, it must be base64 encoded" + NL + "\t\t\t\t\tif(!netscape.ldap.util.LDIF.isPrintable(value_";
  protected final String TEXT_142 = "_";
  protected final String TEXT_143 = ".getBytes(";
  protected final String TEXT_144 = "))){" + NL + "\t\t\t\t\t\tencodingBase64 = true;" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t";
  protected final String TEXT_145 = NL + "\t\t\t// Add \":\" to comply with base64 ldif syntax" + NL + "\t\t\t\tvalue_";
  protected final String TEXT_146 = "_";
  protected final String TEXT_147 = " = util_";
  protected final String TEXT_148 = ".getBase64StringOrNot(encodingBase64,value_";
  protected final String TEXT_149 = "_";
  protected final String TEXT_150 = ",";
  protected final String TEXT_151 = ");" + NL + "\t\t\t\tutil_";
  protected final String TEXT_152 = ".breakString(sb_";
  protected final String TEXT_153 = ", \"";
  protected final String TEXT_154 = ";binary";
  protected final String TEXT_155 = ":";
  protected final String TEXT_156 = ":";
  protected final String TEXT_157 = " \" + value_";
  protected final String TEXT_158 = "_";
  protected final String TEXT_159 = ", wrap_";
  protected final String TEXT_160 = ");" + NL;
  protected final String TEXT_161 = NL + "\t\tcanOutput_";
  protected final String TEXT_162 = " = true;";
  protected final String TEXT_163 = NL + "\t\tutil_";
  protected final String TEXT_164 = ".breakString(sb_";
  protected final String TEXT_165 = ", \"";
  protected final String TEXT_166 = ": \" + value_";
  protected final String TEXT_167 = "_";
  protected final String TEXT_168 = ", wrap_";
  protected final String TEXT_169 = ");" + NL + "\t\tcanOutput_";
  protected final String TEXT_170 = " = true;";
  protected final String TEXT_171 = NL;
  protected final String TEXT_172 = NL + "\t    }";
  protected final String TEXT_173 = "   \t\t\t\t";
  protected final String TEXT_174 = "\t\t\t\t\t\t" + NL + "\t\tsb_";
  protected final String TEXT_175 = ".append('\\n');\t\t" + NL + "\t\t" + NL + "\tif(canOutput_";
  protected final String TEXT_176 = "){\t\t" + NL + "\t\tpw_";
  protected final String TEXT_177 = ".write(sb_";
  protected final String TEXT_178 = ".toString());" + NL + "\t\t";
  protected final String TEXT_179 = NL + "            if(nb_line_";
  protected final String TEXT_180 = "%";
  protected final String TEXT_181 = " == 0) {\t\t" + NL + "    \t\tpw_";
  protected final String TEXT_182 = ".flush();" + NL + "    \t\t}" + NL + "\t\t";
  protected final String TEXT_183 = " \t\t" + NL + "\t\t" + NL + "    \tnb_line_";
  protected final String TEXT_184 = "++;" + NL + "    }\t";
  protected final String TEXT_185 = "    \t" + NL + "    \t//////////////////////////";
  protected final String TEXT_186 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String encoding = ElementParameterParser.getValue(node,"__ENCODING__");

List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
        String cid = node.getUniqueName();
        String changetype = ElementParameterParser.getValue(node, "__CHANGETYPE__");
        boolean flushOnRow = ("true").equals(ElementParameterParser.getValue(node, "__FLUSHONROW__"));		
        String flushMod = ElementParameterParser.getValue(node, "__FLUSHONROW_NUM__");
        
        List<Map<String, String>> multiValueColumns = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__MULTIVALUECOLUMNS__");
        List<Map<String, String>> modifyColumns = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__MODIFYCONFIG__");

    stringBuffer.append(TEXT_1);
    
    	List< ? extends IConnection> conns = node.getIncomingConnections();
    	for (IConnection conn : conns) {
    		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {    		

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    		
    			boolean generateOneAlready = false; //only for Modify append the char '-'
    			List<IMetadataColumn> columns = metadata.getListColumns();
    			int sizeColumns = columns.size();
    			boolean firstLoop = true;
    			for (int i = 0; i < sizeColumns; i++) {    				
    				IMetadataColumn column = columns.get(i);
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					boolean isPrimitive = JavaTypesManager.isJavaPrimitiveType( javaType, column.isNullable());					

    stringBuffer.append(TEXT_6);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    					
					if(!isPrimitive) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_13);
    
} 

    stringBuffer.append(TEXT_14);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    
    				String pattern = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
    				if (javaType == JavaTypesManager.DATE && pattern != null && pattern.trim().length() != 0) {

    stringBuffer.append(TEXT_17);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_19);
    stringBuffer.append( pattern );
    stringBuffer.append(TEXT_20);
    				
					} else if(javaType == JavaTypesManager.STRING) {

    stringBuffer.append(TEXT_21);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_23);
    
					} else if (javaType == JavaTypesManager.BIGDECIMAL) {

    stringBuffer.append(TEXT_24);
    stringBuffer.append(column.getPrecision() == null? conn.getName() + "." + column.getLabel() : conn.getName() + "." + column.getLabel() + ".setScale(" + column.getPrecision() + ", java.math.RoundingMode.HALF_UP)" );
    stringBuffer.append(TEXT_25);
    
					} else if(javaType == JavaTypesManager.BYTE_ARRAY) {

    stringBuffer.append(TEXT_26);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_28);
    
					} else {

    stringBuffer.append(TEXT_29);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_31);
    
					}

     
	if(i==0) {

    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_38);
    if(!"none".equals(changetype)){
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(changetype );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    }
    stringBuffer.append(TEXT_45);
    if("delete".equals(changetype)){
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    }
    stringBuffer.append(TEXT_48);
    
	if(!isPrimitive) {

    stringBuffer.append(TEXT_49);
    					
	} 

    stringBuffer.append(TEXT_50);
    
	continue;
 } 

    stringBuffer.append(TEXT_51);
     if ("delete".equals(changetype)) {
	if(firstLoop){
	
    
	if(!isPrimitive) {

    stringBuffer.append(TEXT_52);
    					
	} 

    stringBuffer.append(TEXT_53);
    	
	}
	firstLoop = false; 
	break; 
} 
     if ("modify".equals(changetype)) {
		String operation = null;
		boolean isMultiValue = false;   
		String separator = null;
		boolean isBinary = false;
		boolean isBase64 = false;
		for(Map<String, String> line:modifyColumns){// search in the configuration table
				String columnName = line.get("SCHEMA_COLUMN");				
				if(column.getLabel().equals(columnName)){
					operation = line.get("OPERATION");					
					isMultiValue = "true".equals(line.get("ISMULTIVALUE"));
					separator = line.get("SEPARATOR");
					isBinary = "true".equals(line.get("BINARY"));
					isBase64 = "true".equals(line.get("BASE64"));
					// Binary content --> always Base64 encoded
					// Explicit base64 encoding : base64 checkbox checked for this column
					if(isBinary || isBase64){
						
    stringBuffer.append(TEXT_54);
    	
					}
					break;
				}
		}	

    stringBuffer.append(TEXT_55);
    if(!"none".equals(operation)){
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(operation );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    if(isMultiValue){
    stringBuffer.append(TEXT_64);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(separator );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    
			// Not binary and no explicit base64 encoding --> check if base 64 encoding needed
			if(!isBinary && !isBase64){
				
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_75);
    
			}
			
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(column.getLabel() );
    if(isBinary){
    stringBuffer.append(TEXT_83);
    }
    stringBuffer.append(TEXT_84);
    if(isBase64){
    stringBuffer.append(TEXT_85);
    }
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_88);
    
	}else{
		// Not binary and no explicit base64 encoding --> check if base 64 encoding needed
		if(!isBinary && !isBase64){

    stringBuffer.append(TEXT_89);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_92);
    
		}
		
    stringBuffer.append(TEXT_93);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(column.getLabel() );
    if(isBinary){
    stringBuffer.append(TEXT_102);
    }
    stringBuffer.append(TEXT_103);
    if(isBase64){
    stringBuffer.append(TEXT_104);
    }
    stringBuffer.append(TEXT_105);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    
	}

    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    }
  if(i==sizeColumns - 1){

    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    }
}// here end the if modify

     if ("add".equals(changetype) || "none".equals(changetype)) {   
		boolean isMultiValue = false;
		String separator = null;
		boolean isBinary = false;
		boolean isBase64 = false;
		for(Map<String, String> line:multiValueColumns){// search in the configuration table
				String columnName = line.get("SCHEMA_COLUMN");				
				if(column.getLabel().equals(columnName)){
					isMultiValue = "true".equals(line.get("ISMULTIVALUE"));
					separator = line.get("SEPARATOR");
					isBinary = "true".equals(line.get("BINARY"));
					isBase64 = "true".equals(line.get("BASE64"));
					// Binary content --> always Base64 encoded
					// Explicit base64 encoding : base64 checkbox checked for this column
					if(isBinary || isBase64){
						
    stringBuffer.append(TEXT_114);
    	
					}
					break;
				}
		}

    stringBuffer.append(TEXT_115);
    if(isMultiValue){
    stringBuffer.append(TEXT_116);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_120);
    stringBuffer.append(separator );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_124);
    
				// Not binary and no explicit base64 encoding --> check if base 64 encoding needed
				if(!isBinary && !isBase64){
					
    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_126);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_127);
    
				}
				
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_134);
    stringBuffer.append(column.getLabel() );
    if(isBinary){
    stringBuffer.append(TEXT_135);
    }
    stringBuffer.append(TEXT_136);
    if(isBase64){
    stringBuffer.append(TEXT_137);
    }
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_140);
    
	}else{
				// Not binary and no explicit base64 encoding --> check if base 64 encoding needed
				if(!isBinary && !isBase64){

    stringBuffer.append(TEXT_141);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_144);
    
				}
			
    stringBuffer.append(TEXT_145);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_150);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(column.getLabel() );
    if(isBinary){
    stringBuffer.append(TEXT_154);
    }
    stringBuffer.append(TEXT_155);
    if(isBase64){
    stringBuffer.append(TEXT_156);
    }
    stringBuffer.append(TEXT_157);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_159);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_160);
    
	}

    stringBuffer.append(TEXT_161);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_162);
    
}// here end the if add

     if ("modrdn".equals(changetype)) {

    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_164);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_165);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_166);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_168);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_170);
    
}// here end the if modrdn

    stringBuffer.append(TEXT_171);
    
	if(!isPrimitive) {

    stringBuffer.append(TEXT_172);
    					
	} 

    stringBuffer.append(TEXT_173);
     
}//here end the last for, the List "columns"

    stringBuffer.append(TEXT_174);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_175);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_178);
     if(flushOnRow) { 
    stringBuffer.append(TEXT_179);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_180);
    stringBuffer.append(flushMod );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_182);
    
			}
		
    stringBuffer.append(TEXT_183);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_184);
    
	}
}//here end the first for, the List "conns"

    stringBuffer.append(TEXT_185);
    
  }
}  

    stringBuffer.append(TEXT_186);
    return stringBuffer.toString();
  }
}
