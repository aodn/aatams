package org.talend.designer.codegen.translators.file.output;

import org.talend.core.model.process.INode;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.process.ElementParameterParser;
import java.util.List;

public class TFileOutputExcelMainJava
{
  protected static String nl;
  public static synchronized TFileOutputExcelMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileOutputExcelMainJava result = new TFileOutputExcelMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "   \t\t\t\t" + NL + "\t    \t\t\t\tif(";
  protected final String TEXT_2 = ".";
  protected final String TEXT_3 = " != null) {" + NL + "    \t\t\t\t";
  protected final String TEXT_4 = NL + "\t\t\t\t\t" + NL + "//modif start" + NL + "\t\t\t\t\t";
  protected final String TEXT_5 = NL + NL + "\t\t\t\t\t";
  protected final String TEXT_6 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_7 = NL + "\t\t\t\t\t\t\tjxl.write.WritableCell existingCell_";
  protected final String TEXT_8 = "_";
  protected final String TEXT_9 = " = writableSheet_";
  protected final String TEXT_10 = ".getWritableCell(";
  protected final String TEXT_11 = " + ";
  protected final String TEXT_12 = ", startRowNum_";
  protected final String TEXT_13 = " + nb_line_";
  protected final String TEXT_14 = ") ;" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_15 = NL + "\t\t\t\t\t\tjxl.write.WritableCell cell_";
  protected final String TEXT_16 = "_";
  protected final String TEXT_17 = " = new jxl.write.";
  protected final String TEXT_18 = "(";
  protected final String TEXT_19 = " + ";
  protected final String TEXT_20 = ", startRowNum_";
  protected final String TEXT_21 = " + nb_line_";
  protected final String TEXT_22 = " ," + NL + "\t\t\t\t\t";
  protected final String TEXT_23 = NL + "\t\t\t\t\t\tjxl.write.WritableCell cell_";
  protected final String TEXT_24 = "_";
  protected final String TEXT_25 = " = new jxl.write.";
  protected final String TEXT_26 = "(";
  protected final String TEXT_27 = ", startRowNum_";
  protected final String TEXT_28 = " + nb_line_";
  protected final String TEXT_29 = "," + NL + "\t\t\t\t\t";
  protected final String TEXT_30 = NL + "//modif end";
  protected final String TEXT_31 = NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_32 = ".";
  protected final String TEXT_33 = ", cell_format_";
  protected final String TEXT_34 = "_";
  protected final String TEXT_35 = NL + "\t\t\t\t\t\t\tString.valueOf(";
  protected final String TEXT_36 = ".";
  protected final String TEXT_37 = ")";
  protected final String TEXT_38 = "\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_39 = ".";
  protected final String TEXT_40 = ".toString()";
  protected final String TEXT_41 = "\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\tjava.nio.charset.Charset.defaultCharset().decode(java.nio.ByteBuffer.wrap(";
  protected final String TEXT_42 = ".";
  protected final String TEXT_43 = ")).toString()";
  protected final String TEXT_44 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_45 = NL + "\t\t\t\t\t\t\tFormatterUtils.format_Number(String.valueOf(";
  protected final String TEXT_46 = "), ";
  protected final String TEXT_47 = ", ";
  protected final String TEXT_48 = ")\t\t\t\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_49 = NL + "\t\t\t\t\t\t\tFormatterUtils.format_Number(String.valueOf(";
  protected final String TEXT_50 = ".";
  protected final String TEXT_51 = "), ";
  protected final String TEXT_52 = ", ";
  protected final String TEXT_53 = ")\t\t\t\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_54 = "\t\t" + NL + "\t\t\t\t\t\t\t(";
  protected final String TEXT_55 = ").doubleValue()";
  protected final String TEXT_56 = NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_57 = ".";
  protected final String TEXT_58 = NL + "\t\t\t\t\t\t,format_";
  protected final String TEXT_59 = NL + "\t\t\t\t\t\t\t);" + NL + "//modif start\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t//If we keep the cell format from the existing cell in sheet" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_60 = NL + "\t\t\t\t\t\t\t\tif(existingCell_";
  protected final String TEXT_61 = "_";
  protected final String TEXT_62 = ".getCellFormat()!=null)" + NL + "\t\t\t\t\t\t\t\t\tcell_";
  protected final String TEXT_63 = "_";
  protected final String TEXT_64 = ".setCellFormat( existingCell_";
  protected final String TEXT_65 = "_";
  protected final String TEXT_66 = ".getCellFormat());" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_67 = NL + "//modif ends\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\twritableSheet_";
  protected final String TEXT_68 = ".addCell(cell_";
  protected final String TEXT_69 = "_";
  protected final String TEXT_70 = ");";
  protected final String TEXT_71 = NL + "\t\t\t\t\t\t\tint currentWith_";
  protected final String TEXT_72 = "_";
  protected final String TEXT_73 = " = String.valueOf(((jxl.write.Number)cell_";
  protected final String TEXT_74 = "_";
  protected final String TEXT_75 = ").getValue()).trim().length();" + NL + "\t\t\t\t\t\t\tcurrentWith_";
  protected final String TEXT_76 = "_";
  protected final String TEXT_77 = "=currentWith_";
  protected final String TEXT_78 = "_";
  protected final String TEXT_79 = ">10?10:currentWith_";
  protected final String TEXT_80 = "_";
  protected final String TEXT_81 = ";";
  protected final String TEXT_82 = NL + "\t\t\t\t\t\t\tint currentWith_";
  protected final String TEXT_83 = "_";
  protected final String TEXT_84 = " = cell_";
  protected final String TEXT_85 = "_";
  protected final String TEXT_86 = ".getContents().trim().length();";
  protected final String TEXT_87 = NL + "\t\t\t\t\t\t\tfitWidth_";
  protected final String TEXT_88 = "[";
  protected final String TEXT_89 = "]=fitWidth_";
  protected final String TEXT_90 = "[";
  protected final String TEXT_91 = "]>currentWith_";
  protected final String TEXT_92 = "_";
  protected final String TEXT_93 = "?fitWidth_";
  protected final String TEXT_94 = "[";
  protected final String TEXT_95 = "]:currentWith_";
  protected final String TEXT_96 = "_";
  protected final String TEXT_97 = "+2;";
  protected final String TEXT_98 = NL + "\t\t\t\t\t\t\tcurrentWith_";
  protected final String TEXT_99 = "_";
  protected final String TEXT_100 = "=";
  protected final String TEXT_101 = ";" + NL + "\t\t\t\t\t\t\tfitWidth_";
  protected final String TEXT_102 = "[";
  protected final String TEXT_103 = "]=fitWidth_";
  protected final String TEXT_104 = "[";
  protected final String TEXT_105 = "]>currentWith_";
  protected final String TEXT_106 = "_";
  protected final String TEXT_107 = "?fitWidth_";
  protected final String TEXT_108 = "[";
  protected final String TEXT_109 = "]:currentWith_";
  protected final String TEXT_110 = "_";
  protected final String TEXT_111 = "+2;";
  protected final String TEXT_112 = NL + "\t    \t\t\t\t} " + NL + "\t\t\t\t\t";
  protected final String TEXT_113 = NL + "    \t\t\tnb_line_";
  protected final String TEXT_114 = "++;";
  protected final String TEXT_115 = NL + "\t\t\t\txlsxTool_";
  protected final String TEXT_116 = ".addRow();";
  protected final String TEXT_117 = "   \t\t\t\t" + NL + "\t    \t\t\t\tif(";
  protected final String TEXT_118 = ".";
  protected final String TEXT_119 = " != null) {" + NL + "    \t\t\t\t";
  protected final String TEXT_120 = NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_121 = ".addCellValue(";
  protected final String TEXT_122 = ".";
  protected final String TEXT_123 = ", ";
  protected final String TEXT_124 = ");";
  protected final String TEXT_125 = "\t\t\t\t\t" + NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_126 = ".addCellValue(java.nio.charset.Charset.defaultCharset().decode(java.nio.ByteBuffer.wrap(";
  protected final String TEXT_127 = ".";
  protected final String TEXT_128 = ")).toString());";
  protected final String TEXT_129 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_130 = NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_131 = ".addCellValue(FormatterUtils.format_Number(String.valueOf(";
  protected final String TEXT_132 = "), ";
  protected final String TEXT_133 = ", ";
  protected final String TEXT_134 = "));\t\t\t\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_135 = NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_136 = ".addCellValue(FormatterUtils.format_Number(String.valueOf(";
  protected final String TEXT_137 = ".";
  protected final String TEXT_138 = "), ";
  protected final String TEXT_139 = ", ";
  protected final String TEXT_140 = "));" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_141 = "\t\t" + NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_142 = ".addCellValue((";
  protected final String TEXT_143 = ").doubleValue());";
  protected final String TEXT_144 = NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_145 = ".addCellValue(";
  protected final String TEXT_146 = ".";
  protected final String TEXT_147 = ");";
  protected final String TEXT_148 = NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_149 = ".addCellValue(";
  protected final String TEXT_150 = ".";
  protected final String TEXT_151 = ");";
  protected final String TEXT_152 = NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_153 = ".addCellValue(Double.parseDouble(String.valueOf(";
  protected final String TEXT_154 = ".";
  protected final String TEXT_155 = ")));";
  protected final String TEXT_156 = NL + "\t\t\t\t\t\t\txlsxTool_";
  protected final String TEXT_157 = ".addCellValue(String.valueOf(";
  protected final String TEXT_158 = ".";
  protected final String TEXT_159 = "));";
  protected final String TEXT_160 = NL + "\t    \t\t\t\t} else {" + NL + "\t    \t\t\t\t\txlsxTool_";
  protected final String TEXT_161 = ".addCellValue(null);" + NL + "\t    \t\t\t\t}" + NL + "\t\t\t\t\t";
  protected final String TEXT_162 = NL + "    \t\t\tnb_line_";
  protected final String TEXT_163 = "++;";
  protected final String TEXT_164 = NL + "\t\t\t\t\tbufferCount_";
  protected final String TEXT_165 = "++;" + NL + "\t\t\t\t\tif(bufferCount_";
  protected final String TEXT_166 = " >= flushRowNum_";
  protected final String TEXT_167 = "){" + NL + "    \t\t\t\t\txlsxTool_";
  protected final String TEXT_168 = ".flushRowInMemory();" + NL + "    \t\t\t\t\tbufferCount_";
  protected final String TEXT_169 = "=0;" + NL + "    \t\t\t\t}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();

String cid = node.getUniqueName();
boolean version07 = ("true").equals(ElementParameterParser.getValue(node,"__VERSION_2007__"));
String advancedSeparatorStr = ElementParameterParser.getValue(node, "__ADVANCED_SEPARATOR__");
boolean advancedSeparator = (advancedSeparatorStr!=null&&!("").equals(advancedSeparatorStr))?("true").equals(advancedSeparatorStr):false;
String thousandsSeparator = ElementParameterParser.getValueWithJavaType(node, "__THOUSANDS_SEPARATOR__", JavaTypesManager.CHARACTER);
String decimalSeparator = ElementParameterParser.getValueWithJavaType(node, "__DECIMAL_SEPARATOR__", JavaTypesManager.CHARACTER); 
String font = ElementParameterParser.getValue(node, "__FONT__"); 
//modif start
boolean firstCellYAbsolute = ("true").equals(ElementParameterParser.getValue(node, "__FIRST_CELL_Y_ABSOLUTE__"));
String firstCellXStr = ElementParameterParser.getValue(node, "__FIRST_CELL_X__");
String firstCellYStr = ElementParameterParser.getValue(node, "__FIRST_CELL_Y__");
//modif end
boolean keepCellFormating =  ElementParameterParser.getValue(node, "__KEEP_CELL_FORMATING__").equals("true");
boolean flushOnRow=("true").equals(ElementParameterParser.getValue(node, "__FLUSHONROW__" ));
boolean useStream = ("true").equals(ElementParameterParser.getValue(node,"__USESTREAM__"));
boolean isAppendFile = ("true").equals(ElementParameterParser.getValue(node, "__APPEND_FILE__" ));
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
		List<IMetadataColumn> columns = metadata.getListColumns();
		
		if(!version07){//version judgement
    	
    	List< ? extends IConnection> conns = node.getIncomingConnections();
    	for (IConnection conn : conns) {
    		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
    			int sizeColumns = columns.size();
    			for (int i = 0; i < sizeColumns; i++) {
    				IMetadataColumn column = columns.get(i);
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					boolean isPrimitive = JavaTypesManager.isJavaPrimitiveType( javaType, column.isNullable());
					if(!isPrimitive) {
    				
    stringBuffer.append(TEXT_1);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_3);
    
    				} 
    				
    stringBuffer.append(TEXT_4);
    
					//Detect the format required for the cell
					String jxlWriteType;
    				int staticWidth = 0;
    				boolean isNumber = false;
					if(javaType == JavaTypesManager.BOOLEAN){
						staticWidth=5;
						jxlWriteType="Boolean";
					}else if(javaType == JavaTypesManager.DATE){
						jxlWriteType="DateTime";
				    }else if(javaType == JavaTypesManager.STRING||
				    		 javaType == JavaTypesManager.CHARACTER||
				    		 javaType == JavaTypesManager.BYTE_ARRAY||
				    		 javaType == JavaTypesManager.LIST||
				    		 javaType == JavaTypesManager.OBJECT||
				    		 (advancedSeparator && JavaTypesManager.isNumberType(javaType, column.isNullable()))
				    		 ){
				    	jxlWriteType="Label";
					}else{
						isNumber=true;
						jxlWriteType="Number";
					};
   				
    stringBuffer.append(TEXT_5);
    if(firstCellYAbsolute){
    stringBuffer.append(TEXT_6);
    if(keepCellFormating){
    stringBuffer.append(TEXT_7);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(firstCellXStr);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_14);
    }
    stringBuffer.append(TEXT_15);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(jxlWriteType);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(firstCellXStr);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    }else{
    stringBuffer.append(TEXT_23);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(jxlWriteType);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_29);
    }
    stringBuffer.append(TEXT_30);
    
    				String pattern = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
    				if (javaType == JavaTypesManager.DATE && pattern != null && pattern.trim().length() != 0) {
						staticWidth = pattern.trim().length();

    stringBuffer.append(TEXT_31);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_34);
    stringBuffer.append( cid);
    
					} else {
						if(javaType == JavaTypesManager.CHARACTER) {

    stringBuffer.append(TEXT_35);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_37);
    
						} else if(javaType == JavaTypesManager.OBJECT || javaType == JavaTypesManager.LIST){

    stringBuffer.append(TEXT_38);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_40);
    
						} else if(javaType == JavaTypesManager.BYTE_ARRAY){

    stringBuffer.append(TEXT_41);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_43);
    
						} else if(advancedSeparator && JavaTypesManager.isNumberType(javaType, column.isNullable())) { 

    stringBuffer.append(TEXT_44);
     if(javaType == JavaTypesManager.BIGDECIMAL) {
    stringBuffer.append(TEXT_45);
    stringBuffer.append(column.getPrecision() == null? conn.getName() + "." + column.getLabel() : conn.getName() + "." + column.getLabel() + ".setScale(" + column.getPrecision() + ", java.math.RoundingMode.HALF_UP)" );
    stringBuffer.append(TEXT_46);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_47);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_48);
     } else { 
    stringBuffer.append(TEXT_49);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_51);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_52);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_53);
     } 
    
						} else if (javaType == JavaTypesManager.BIGDECIMAL) {

    stringBuffer.append(TEXT_54);
    stringBuffer.append(column.getPrecision() == null? conn.getName() + "." + column.getLabel() : conn.getName() + "." + column.getLabel() + ".setScale(" + column.getPrecision() + ", java.math.RoundingMode.HALF_UP)" );
    stringBuffer.append(TEXT_55);
    
					}else {

    stringBuffer.append(TEXT_56);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(column.getLabel() );
    				
					}
					if(font !=null && font.length()!=0){ 

    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    
					}
				}

    stringBuffer.append(TEXT_59);
    if(keepCellFormating && firstCellYAbsolute){
    stringBuffer.append(TEXT_60);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_66);
    
							}
							
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    
					if(isNumber){

    stringBuffer.append(TEXT_71);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_75);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_77);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    
					}else{

    stringBuffer.append(TEXT_82);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_84);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_86);
    
					}
					if(staticWidth ==0){

    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_88);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_91);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_95);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_97);
    
					}else{

    stringBuffer.append(TEXT_98);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_100);
    stringBuffer.append(staticWidth);
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_102);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_104);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_105);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_108);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_109);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_111);
    
					}
					if(!isPrimitive) {
    				
    stringBuffer.append(TEXT_112);
    
    				} 
    			}
    			
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    
    		}
    	}
    	}else{ //version judgement /***excel 2007 xlsx*****/

		List< ? extends IConnection> conns = node.getIncomingConnections();
    	for (IConnection conn : conns) {
    		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
    			int sizeColumns = columns.size();

    stringBuffer.append(TEXT_115);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_116);
    
    			for (int i = 0; i < sizeColumns; i++) {
    				IMetadataColumn column = columns.get(i);
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					boolean isPrimitive = JavaTypesManager.isJavaPrimitiveType( javaType, column.isNullable());
					if(!isPrimitive) {
    				
    stringBuffer.append(TEXT_117);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_119);
    
    				} 
    				
    
    				String pattern = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
    				if (javaType == JavaTypesManager.DATE && pattern != null && pattern.trim().length() != 0) {

    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_121);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    stringBuffer.append(pattern);
    stringBuffer.append(TEXT_124);
    
					} else if(javaType == JavaTypesManager.BYTE_ARRAY){

    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_126);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_127);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_128);
    
					} else if(advancedSeparator && JavaTypesManager.isNumberType(javaType, column.isNullable())) { 

    stringBuffer.append(TEXT_129);
     if(javaType == JavaTypesManager.BIGDECIMAL) {
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_131);
    stringBuffer.append(column.getPrecision() == null? conn.getName() + "." + column.getLabel() : conn.getName() + "." + column.getLabel() + ".setScale(" + column.getPrecision() + ", java.math.RoundingMode.HALF_UP)" );
    stringBuffer.append(TEXT_132);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_133);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_134);
     } else { 
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_136);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_138);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_139);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_140);
     } 
    
					} else if (javaType == JavaTypesManager.BIGDECIMAL) {

    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_142);
    stringBuffer.append(column.getPrecision() == null? conn.getName() + "." + column.getLabel() : conn.getName() + "." + column.getLabel() + ".setScale(" + column.getPrecision() + ", java.math.RoundingMode.HALF_UP)" );
    stringBuffer.append(TEXT_143);
    
					}else if (javaType == JavaTypesManager.BOOLEAN){

    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_145);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_147);
    
					}else if (javaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_149);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_150);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_151);
    					}else if(JavaTypesManager.isNumberType(javaType)){

    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_153);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_155);
    
					}else{	

    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_157);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_158);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_159);
    				
					}
					if(!isPrimitive) {
    				
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    
    				} 
    			}
    			
    stringBuffer.append(TEXT_162);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_163);
    
				if(flushOnRow && (useStream || !isAppendFile)){

    stringBuffer.append(TEXT_164);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_166);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_168);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_169);
    
				}
    		}
    	}

    	}
    }
}

    return stringBuffer.toString();
  }
}
