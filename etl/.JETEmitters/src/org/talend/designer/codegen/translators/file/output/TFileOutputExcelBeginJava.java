package org.talend.designer.codegen.translators.file.output;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import java.util.Map;

public class TFileOutputExcelBeginJava
{
  protected static String nl;
  public static synchronized TFileOutputExcelBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileOutputExcelBeginJava result = new TFileOutputExcelBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\tint nb_line_";
  protected final String TEXT_3 = " = 0;" + NL;
  protected final String TEXT_4 = "\t\t" + NL + "\t\tjava.io.File file_";
  protected final String TEXT_5 = " = new java.io.File(";
  protected final String TEXT_6 = ");" + NL + "\t\t";
  protected final String TEXT_7 = NL + "//create directory only if not exists\t\t  " + NL + "          java.io.File parentFile_";
  protected final String TEXT_8 = " = file_";
  protected final String TEXT_9 = ".getParentFile();" + NL + "          if (parentFile_";
  protected final String TEXT_10 = " != null && !parentFile_";
  protected final String TEXT_11 = ".exists()) {" + NL + "             parentFile_";
  protected final String TEXT_12 = ".mkdirs();" + NL + "          }";
  protected final String TEXT_13 = "\t\t" + NL + "\t\t" + NL + "\t\tjxl.write.WritableWorkbook writeableWorkbook_";
  protected final String TEXT_14 = " = null;" + NL + "\t\tjxl.write.WritableSheet writableSheet_";
  protected final String TEXT_15 = " = null;" + NL + "\t\t" + NL + "\t\tjxl.WorkbookSettings workbookSettings_";
  protected final String TEXT_16 = " = new jxl.WorkbookSettings();" + NL + "        workbookSettings_";
  protected final String TEXT_17 = ".setEncoding(";
  protected final String TEXT_18 = ");";
  protected final String TEXT_19 = NL + "\t\twriteableWorkbook_";
  protected final String TEXT_20 = " = new jxl.write.biff.WritableWorkbookImpl(" + NL + "            \t\tnew java.io.BufferedOutputStream(";
  protected final String TEXT_21 = "), " + NL + "            \t\tfalse, workbookSettings_";
  protected final String TEXT_22 = ");";
  protected final String TEXT_23 = "  " + NL + "        if (file_";
  protected final String TEXT_24 = ".exists()) {" + NL + "        jxl.Workbook workbook_";
  protected final String TEXT_25 = " = jxl.Workbook.getWorkbook(file_";
  protected final String TEXT_26 = ",workbookSettings_";
  protected final String TEXT_27 = ");" + NL + "        workbookSettings_";
  protected final String TEXT_28 = ".setWriteAccess(null);" + NL + "        writeableWorkbook_";
  protected final String TEXT_29 = " = new jxl.write.biff.WritableWorkbookImpl(" + NL + "                \tnew java.io.BufferedOutputStream(new java.io.FileOutputStream(file_";
  protected final String TEXT_30 = ", false)), " + NL + "                \tworkbook_";
  protected final String TEXT_31 = ", " + NL + "                \ttrue," + NL + "                    workbookSettings_";
  protected final String TEXT_32 = ");" + NL + "        }else{        " + NL + "\t\twriteableWorkbook_";
  protected final String TEXT_33 = " = new jxl.write.biff.WritableWorkbookImpl(" + NL + "            \t\tnew java.io.BufferedOutputStream(new java.io.FileOutputStream(";
  protected final String TEXT_34 = ")), " + NL + "            \t\ttrue, " + NL + "            \t\tworkbookSettings_";
  protected final String TEXT_35 = ");        " + NL + "        }     ";
  protected final String TEXT_36 = NL + "\t\twriteableWorkbook_";
  protected final String TEXT_37 = " = new jxl.write.biff.WritableWorkbookImpl(" + NL + "            \t\tnew java.io.BufferedOutputStream(new java.io.FileOutputStream(";
  protected final String TEXT_38 = ")), " + NL + "            \t\ttrue, " + NL + "            \t\tworkbookSettings_";
  protected final String TEXT_39 = ");";
  protected final String TEXT_40 = "       " + NL + "" + NL + "        writableSheet_";
  protected final String TEXT_41 = " = writeableWorkbook_";
  protected final String TEXT_42 = ".getSheet(";
  protected final String TEXT_43 = ");" + NL + "        if(writableSheet_";
  protected final String TEXT_44 = " == null){" + NL + "        \twritableSheet_";
  protected final String TEXT_45 = " = writeableWorkbook_";
  protected final String TEXT_46 = ".createSheet(";
  protected final String TEXT_47 = ", writeableWorkbook_";
  protected final String TEXT_48 = ".getNumberOfSheets());" + NL + "\t\t}" + NL + "\t\t";
  protected final String TEXT_49 = NL + "        else {" + NL + "" + NL + "            String[] sheetNames_";
  protected final String TEXT_50 = " = writeableWorkbook_";
  protected final String TEXT_51 = ".getSheetNames();" + NL + "            for (int i = 0; i < sheetNames_";
  protected final String TEXT_52 = ".length; i++) {" + NL + "                if (sheetNames_";
  protected final String TEXT_53 = "[i].equals(";
  protected final String TEXT_54 = ")) {" + NL + "                    writeableWorkbook_";
  protected final String TEXT_55 = ".removeSheet(i);" + NL + "                    break;" + NL + "                }" + NL + "            }" + NL + "" + NL + "\t\t\twritableSheet_";
  protected final String TEXT_56 = " = writeableWorkbook_";
  protected final String TEXT_57 = ".createSheet(";
  protected final String TEXT_58 = ", writeableWorkbook_";
  protected final String TEXT_59 = ".getNumberOfSheets());" + NL + "        }";
  protected final String TEXT_60 = NL + NL + "        //modif start";
  protected final String TEXT_61 = NL + "\t\tint startRowNum_";
  protected final String TEXT_62 = " = ";
  protected final String TEXT_63 = ";";
  protected final String TEXT_64 = NL + "        int startRowNum_";
  protected final String TEXT_65 = " = writableSheet_";
  protected final String TEXT_66 = ".getRows();";
  protected final String TEXT_67 = NL + "\t\t//modif end" + NL + "\t\t" + NL + "\t\tint[] fitWidth_";
  protected final String TEXT_68 = " = new int[";
  protected final String TEXT_69 = "];" + NL + "\t\tfor(int i_";
  protected final String TEXT_70 = "=0;i_";
  protected final String TEXT_71 = "<";
  protected final String TEXT_72 = ";i_";
  protected final String TEXT_73 = "++){" + NL + "\t\t    int fitCellViewSize_";
  protected final String TEXT_74 = "=writableSheet_";
  protected final String TEXT_75 = ".getColumnView(i_";
  protected final String TEXT_76 = "+";
  protected final String TEXT_77 = ").getSize();" + NL + "\t\t\tfitWidth_";
  protected final String TEXT_78 = "[i_";
  protected final String TEXT_79 = "]=fitCellViewSize_";
  protected final String TEXT_80 = "/256;" + NL + "\t\t\tif(fitCellViewSize_";
  protected final String TEXT_81 = "%256!=0){" + NL + "\t\t\t\tfitWidth_";
  protected final String TEXT_82 = "[i_";
  protected final String TEXT_83 = "]+=1;" + NL + "\t\t\t}" + NL + "\t\t}";
  protected final String TEXT_84 = NL + "\t\t" + NL + "\t\tjxl.write.WritableFont wf_";
  protected final String TEXT_85 = " = new jxl.write.WritableFont(jxl.write.WritableFont.";
  protected final String TEXT_86 = ", 10, jxl.write.WritableFont.NO_BOLD, false);" + NL + "        jxl.write.WritableCellFormat format_";
  protected final String TEXT_87 = "  = new jxl.write.WritableCellFormat(wf_";
  protected final String TEXT_88 = "); ";
  protected final String TEXT_89 = NL;
  protected final String TEXT_90 = NL + "    \t\t\t\t\tfinal jxl.write.WritableCellFormat cell_format_";
  protected final String TEXT_91 = "_";
  protected final String TEXT_92 = "=new jxl.write.WritableCellFormat(wf_";
  protected final String TEXT_93 = " ,new jxl.write.DateFormat(";
  protected final String TEXT_94 = "));";
  protected final String TEXT_95 = "\t\t\t\t\t" + NL + "\t\t\t\t\t\tfinal jxl.write.WritableCellFormat cell_format_";
  protected final String TEXT_96 = "_";
  protected final String TEXT_97 = "=new jxl.write.WritableCellFormat(new jxl.write.DateFormat(";
  protected final String TEXT_98 = "));";
  protected final String TEXT_99 = NL + "\t\tif(true){" + NL + "\t\t\tthrow new RuntimeException(\"Date pattern must be set for column ";
  protected final String TEXT_100 = " in the schema of component ";
  protected final String TEXT_101 = "!\");" + NL + "\t\t}";
  protected final String TEXT_102 = "\t\t" + NL + NL;
  protected final String TEXT_103 = NL + "\tboolean needDel_";
  protected final String TEXT_104 = " = false;";
  protected final String TEXT_105 = NL + "\t\tif (startRowNum_";
  protected final String TEXT_106 = " == ";
  protected final String TEXT_107 = "){";
  protected final String TEXT_108 = NL + "\t\tif (startRowNum_";
  protected final String TEXT_109 = " == 0){";
  protected final String TEXT_110 = NL + "\t//modif end";
  protected final String TEXT_111 = NL + "\t\t//modif start";
  protected final String TEXT_112 = NL + "\t\t\twritableSheet_";
  protected final String TEXT_113 = ".addCell(new jxl.write.Label(";
  protected final String TEXT_114 = " + ";
  protected final String TEXT_115 = ", startRowNum_";
  protected final String TEXT_116 = ", \"";
  protected final String TEXT_117 = "\"";
  protected final String TEXT_118 = NL + "\t\t\t\t\t,format_";
  protected final String TEXT_119 = NL + "\t\t\t));\t\t";
  protected final String TEXT_120 = NL + "\t\t\twritableSheet_";
  protected final String TEXT_121 = ".addCell(new jxl.write.Label(";
  protected final String TEXT_122 = ", nb_line_";
  protected final String TEXT_123 = ", \"";
  protected final String TEXT_124 = "\"";
  protected final String TEXT_125 = NL + "\t\t\t\t\t,format_";
  protected final String TEXT_126 = NL + "\t\t\t));";
  protected final String TEXT_127 = NL + "\t\t//modif end" + NL + "\t\tfitWidth_";
  protected final String TEXT_128 = "[";
  protected final String TEXT_129 = "]=fitWidth_";
  protected final String TEXT_130 = "[";
  protected final String TEXT_131 = "]>";
  protected final String TEXT_132 = "?fitWidth_";
  protected final String TEXT_133 = "[";
  protected final String TEXT_134 = "]:";
  protected final String TEXT_135 = ";";
  protected final String TEXT_136 = NL + "\t\tneedDel_";
  protected final String TEXT_137 = " = true;";
  protected final String TEXT_138 = NL + "\t\tnb_line_";
  protected final String TEXT_139 = "++;" + NL + "\t}";
  protected final String TEXT_140 = NL + "\t\t";
  protected final String TEXT_141 = "\t" + NL + "\t\tint nb_line_";
  protected final String TEXT_142 = " = 0;" + NL + "\t\torg.talend.ExcelTool xlsxTool_";
  protected final String TEXT_143 = " = new org.talend.ExcelTool();";
  protected final String TEXT_144 = NL + "\t\t\tint\tflushRowNum_";
  protected final String TEXT_145 = "=";
  protected final String TEXT_146 = ";" + NL + "\t\t\tint bufferCount_";
  protected final String TEXT_147 = "=0;" + NL + "\t\t\txlsxTool_";
  protected final String TEXT_148 = ".setRowAccessWindowSize(-1);" + NL + "\t\t\t//turn-off auto flush";
  protected final String TEXT_149 = NL + "\t\txlsxTool_";
  protected final String TEXT_150 = ".setSheet(";
  protected final String TEXT_151 = ");" + NL + "\t\txlsxTool_";
  protected final String TEXT_152 = ".setAppend(";
  protected final String TEXT_153 = ",";
  protected final String TEXT_154 = ");" + NL + "\t\txlsxTool_";
  protected final String TEXT_155 = ".setXY(";
  protected final String TEXT_156 = ",";
  protected final String TEXT_157 = ",";
  protected final String TEXT_158 = ",";
  protected final String TEXT_159 = ");" + NL + "\t\t";
  protected final String TEXT_160 = NL + "\t\txlsxTool_";
  protected final String TEXT_161 = ".prepareXlsxFile(";
  protected final String TEXT_162 = ");" + NL + "\t\t";
  protected final String TEXT_163 = NL + "\t\txlsxTool_";
  protected final String TEXT_164 = ".prepareStream();" + NL + "\t\t";
  protected final String TEXT_165 = NL + "\t\txlsxTool_";
  protected final String TEXT_166 = ".setFont(\"";
  protected final String TEXT_167 = "\");" + NL + "\t\t";
  protected final String TEXT_168 = NL + "\t\tboolean needDel_";
  protected final String TEXT_169 = " = false;" + NL + "\t\t";
  protected final String TEXT_170 = NL + "\t\tif (xlsxTool_";
  protected final String TEXT_171 = ".getStartRow() == 0){" + NL + "\t\t";
  protected final String TEXT_172 = NL + "\t\txlsxTool_";
  protected final String TEXT_173 = ".addRow();" + NL + "\t\t";
  protected final String TEXT_174 = NL + "\t\txlsxTool_";
  protected final String TEXT_175 = ".addCellValue(\"";
  protected final String TEXT_176 = "\");" + NL + "\t\t";
  protected final String TEXT_177 = NL + "\t\tneedDel_";
  protected final String TEXT_178 = " = true;" + NL + "\t\t";
  protected final String TEXT_179 = NL + "\t\tnb_line_";
  protected final String TEXT_180 = "++;" + NL + "\t\t";
  protected final String TEXT_181 = NL + "\t}" + NL + "\t\t";
  protected final String TEXT_182 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
boolean version07 = ("true").equals(ElementParameterParser.getValue(node,"__VERSION_2007__"));
	
boolean useStream = ("true").equals(ElementParameterParser.getValue(node,"__USESTREAM__"));
String outStream = ElementParameterParser.getValue(node,"__STREAMNAME__");

String filename = ElementParameterParser.getValue(node, "__FILENAME__");
String sheetname = ElementParameterParser.getValue(node, "__SHEETNAME__");
boolean firstCellYAbsolute = ("true").equals(ElementParameterParser.getValue(node, "__FIRST_CELL_Y_ABSOLUTE__"));
String firstCellXStr = ElementParameterParser.getValue(node, "__FIRST_CELL_X__");
String firstCellYStr = ElementParameterParser.getValue(node, "__FIRST_CELL_Y__");
boolean keepCellFormating = ("true").equals(ElementParameterParser.getValue(node, "__KEEP_CELL_FORMATING__"));
String font = ElementParameterParser.getValue(node, "__FONT__");
boolean isDeleteEmptyFile = ElementParameterParser.getValue(node, "__DELETE_EMPTYFILE__").equals("true");
boolean isIncludeHeader = ("true").equals(ElementParameterParser.getValue(node, "__INCLUDEHEADER__"));
boolean isAppendFile = ("true").equals(ElementParameterParser.getValue(node, "__APPEND_FILE__" ));
boolean isAppendSheet = ("true").equals(ElementParameterParser.getValue(node, "__APPEND_SHEET__" ));

boolean flushOnRow=("true").equals(ElementParameterParser.getValue(node, "__FLUSHONROW__" ));
String flushRowNum=ElementParameterParser.getValue(node, "__FLUSHONROW_NUM__" );

List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
    	List<IMetadataColumn> columns = metadata.getListColumns();
    	if(!version07){//version judgement

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    
		if(!useStream){ // the part of the file path

    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(filename );
    stringBuffer.append(TEXT_6);
    
			if(("true").equals(ElementParameterParser.getValue(node,"__CREATE__"))){

    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    
			}
		}

    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(ElementParameterParser.getValue(node,"__ENCODING__") );
    stringBuffer.append(TEXT_18);
    
		if(useStream){ // the part of the output stream support

    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(outStream );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    
		}else{
			if(isAppendFile){

    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_34);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_35);
    
			} else {

    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_38);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_39);
    
			}
		}

    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(sheetname );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(sheetname );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    if(!useStream && isAppendFile && !isAppendSheet){
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(sheetname );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(sheetname );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    }
    stringBuffer.append(TEXT_60);
    if(firstCellYAbsolute){
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(firstCellYStr);
    stringBuffer.append(TEXT_63);
    }else{
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    }
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(columns.size());
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_71);
    stringBuffer.append(columns.size());
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid);
    if(firstCellYAbsolute){
    stringBuffer.append(TEXT_76);
    stringBuffer.append(firstCellXStr);
    }
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    if(font !=null && font.length()!=0){
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    stringBuffer.append(font);
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_88);
    }
    stringBuffer.append(TEXT_89);
    
    	for (int i = 0; i < columns.size(); i++) {
    		IMetadataColumn column = columns.get(i);
    		JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
    		if (javaType == JavaTypesManager.DATE){
    			String pattern = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
    			if(pattern != null && pattern.trim().length() != 0){
    				if(font !=null && font.length()!=0){

    stringBuffer.append(TEXT_90);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_91);
    stringBuffer.append( cid);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_93);
    stringBuffer.append(pattern );
    stringBuffer.append(TEXT_94);
    
					}else{

    stringBuffer.append(TEXT_95);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_96);
    stringBuffer.append( cid);
    stringBuffer.append(TEXT_97);
    stringBuffer.append(pattern );
    stringBuffer.append(TEXT_98);
    	
					}
				}else{

    stringBuffer.append(TEXT_99);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    				}
			}
		
	    }

    stringBuffer.append(TEXT_102);
    
if(isIncludeHeader){
	if(isDeleteEmptyFile){

    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    
	}
	if(firstCellYAbsolute){

    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(firstCellYStr);
    stringBuffer.append(TEXT_107);
    
	}else{

    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_109);
    
	}

    stringBuffer.append(TEXT_110);
    
	for (int i = 0; i < columns.size(); i++) {
		IMetadataColumn column = columns.get(i);

    stringBuffer.append(TEXT_111);
    
		if(firstCellYAbsolute){

    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_114);
    stringBuffer.append(firstCellXStr);
    stringBuffer.append(TEXT_115);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_116);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_117);
    
				if (font !=null && font.length()!=0) {

    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid );
    
				}

    stringBuffer.append(TEXT_119);
    
		}else{

    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_123);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_124);
    
				if (font !=null && font.length()!=0) {

    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid );
    
				}

    stringBuffer.append(TEXT_126);
    
		}

    stringBuffer.append(TEXT_127);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_128);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_130);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_131);
    stringBuffer.append(column.getLabel().length());
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_133);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_134);
    stringBuffer.append(column.getLabel().length());
    stringBuffer.append(TEXT_135);
    
	}
		if(isDeleteEmptyFile){

    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    		}
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_139);
    
}

    stringBuffer.append(TEXT_140);
    	
		}else{ //version judgement /***excel 2007 xlsx*****/

    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_143);
    	 
		if(flushOnRow && (useStream || !isAppendFile)){

    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_145);
    stringBuffer.append(flushRowNum);
    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_148);
    		
		} 

    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_150);
    stringBuffer.append(sheetname);
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_152);
    stringBuffer.append(isAppendFile);
    stringBuffer.append(TEXT_153);
    stringBuffer.append(isAppendSheet);
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_155);
    stringBuffer.append(firstCellYAbsolute);
    stringBuffer.append(TEXT_156);
    stringBuffer.append(firstCellXStr);
    stringBuffer.append(TEXT_157);
    stringBuffer.append(firstCellYStr);
    stringBuffer.append(TEXT_158);
    stringBuffer.append(keepCellFormating);
    stringBuffer.append(TEXT_159);
    if(!useStream){
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_162);
    }else{
    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_164);
    }
    stringBuffer.append(TEXT_165);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_166);
    stringBuffer.append(font);
    stringBuffer.append(TEXT_167);
    
		if(isIncludeHeader){
			if(isDeleteEmptyFile){
		
    stringBuffer.append(TEXT_168);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    
			}
			
			if(!firstCellYAbsolute) {
		
    stringBuffer.append(TEXT_170);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_171);
    
			}
		
    stringBuffer.append(TEXT_172);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_173);
    
			for (int i = 0; i < columns.size(); i++) {
				IMetadataColumn column = columns.get(i);
		
    stringBuffer.append(TEXT_174);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_175);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_176);
    
			}
			if(isDeleteEmptyFile){
		
    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_178);
    		
			}
		
    stringBuffer.append(TEXT_179);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_180);
    
			if(!firstCellYAbsolute) {
		
    stringBuffer.append(TEXT_181);
    
			}
		}	
		
    
		}
    }
}

    stringBuffer.append(TEXT_182);
    return stringBuffer.toString();
  }
}
