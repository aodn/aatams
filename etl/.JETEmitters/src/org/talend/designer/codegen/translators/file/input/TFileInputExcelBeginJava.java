package org.talend.designer.codegen.translators.file.input;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.List;
import java.util.Map;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;

public class TFileInputExcelBeginJava
{
  protected static String nl;
  public static synchronized TFileInputExcelBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileInputExcelBeginJava result = new TFileInputExcelBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t";
  protected final String TEXT_2 = NL + NL + "\t\t\tclass RegexUtil_";
  protected final String TEXT_3 = " {" + NL + "\t\t\t\t" + NL + "\t\t    \tpublic java.util.List<jxl.Sheet> getSheets(jxl.Workbook workbook, String oneSheetName, boolean useRegex) {" + NL + "\t\t\t        " + NL + "\t\t\t        java.util.List<jxl.Sheet> list = new java.util.ArrayList<jxl.Sheet>();" + NL + "\t\t\t        " + NL + "\t\t\t        if(useRegex){//this part process the regex issue" + NL + "\t\t\t        \t" + NL + "\t\t\t\t        jxl.Sheet[] sheets = workbook.getSheets();" + NL + "\t\t\t\t        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(oneSheetName);" + NL + "\t\t\t\t        for (int i = 0; i < sheets.length; i++) {" + NL + "\t\t\t\t            String sheetName = sheets[i].getName();" + NL + "\t\t\t\t            java.util.regex.Matcher matcher = pattern.matcher(sheetName);" + NL + "\t\t\t\t            if (matcher.matches()) {" + NL + "\t\t\t\t            \tjxl.Sheet sheet = workbook.getSheet(sheetName);" + NL + "\t\t\t\t            \tif(sheet != null){" + NL + "\t\t\t\t                \tlist.add(sheet);" + NL + "\t\t\t\t                }\t" + NL + "\t\t\t\t            }" + NL + "\t\t\t\t        }" + NL + "\t\t\t\t        " + NL + "\t\t\t        }else{\t" + NL + "\t\t\t        \tjxl.Sheet sheet = workbook.getSheet(oneSheetName);" + NL + "\t\t            \tif(sheet != null){" + NL + "\t\t                \tlist.add(sheet);" + NL + "\t\t                }" + NL + "\t\t\t        \t" + NL + "\t\t\t        }" + NL + "\t\t\t        " + NL + "\t\t\t        return list;" + NL + "\t\t\t    }" + NL + "\t\t    \t" + NL + "\t\t\t    public java.util.List<jxl.Sheet> getSheets(jxl.Workbook workbook, int index, boolean useRegex) {" + NL + "\t\t\t    \tjava.util.List<jxl.Sheet> list =  new java.util.ArrayList<jxl.Sheet>();" + NL + "\t\t\t    \tjxl.Sheet sheet = workbook.getSheet(index);" + NL + "\t            \tif(sheet != null){" + NL + "\t                \tlist.add(sheet);" + NL + "\t                }" + NL + "\t\t\t    \treturn list;" + NL + "\t\t\t    }" + NL + "\t\t\t    " + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\t" + NL + "\t\tRegexUtil_";
  protected final String TEXT_4 = " regexUtil_";
  protected final String TEXT_5 = " = new RegexUtil_";
  protected final String TEXT_6 = "();" + NL + "\t\tfinal jxl.WorkbookSettings workbookSettings_";
  protected final String TEXT_7 = " = new jxl.WorkbookSettings();" + NL + "\t\tworkbookSettings_";
  protected final String TEXT_8 = ".setDrawingsDisabled(true);";
  protected final String TEXT_9 = NL + "\t\tworkbookSettings_";
  protected final String TEXT_10 = ".setCellValidationDisabled(true);";
  protected final String TEXT_11 = NL + "\t\tworkbookSettings_";
  protected final String TEXT_12 = ".setSuppressWarnings(true);";
  protected final String TEXT_13 = "\t\t" + NL + "        workbookSettings_";
  protected final String TEXT_14 = ".setEncoding(";
  protected final String TEXT_15 = ");" + NL + "        " + NL + "        Object source_";
  protected final String TEXT_16 = " =";
  protected final String TEXT_17 = ";" + NL + "        final jxl.Workbook workbook_";
  protected final String TEXT_18 = ";" + NL + "        " + NL + "        if(source_";
  protected final String TEXT_19 = " instanceof java.io.InputStream){" + NL + "        \tworkbook_";
  protected final String TEXT_20 = " = jxl.Workbook.getWorkbook(new java.io.BufferedInputStream((java.io.InputStream)source_";
  protected final String TEXT_21 = "), workbookSettings_";
  protected final String TEXT_22 = ");" + NL + "        }else if(source_";
  protected final String TEXT_23 = " instanceof String){" + NL + "        \tworkbook_";
  protected final String TEXT_24 = " = jxl.Workbook.getWorkbook(new java.io.BufferedInputStream(new java.io.FileInputStream(" + NL + "        \t\t\t\t\t\t\tsource_";
  protected final String TEXT_25 = ".toString())), workbookSettings_";
  protected final String TEXT_26 = ");" + NL + "        }else{" + NL + "        \tworkbook_";
  protected final String TEXT_27 = " = null;" + NL + "        \tthrow new Exception(\"The data source should be specified as Inputstream or File Path!\");" + NL + "        }" + NL + "        try {";
  protected final String TEXT_28 = NL + "\t\tjava.util.List<jxl.Sheet> sheetList_";
  protected final String TEXT_29 = " = java.util.Arrays.<jxl.Sheet> asList(workbook_";
  protected final String TEXT_30 = ".getSheets());";
  protected final String TEXT_31 = NL + "\t\tjava.util.List<jxl.Sheet> sheetList_";
  protected final String TEXT_32 = " = new java.util.ArrayList<jxl.Sheet>();";
  protected final String TEXT_33 = NL + "        sheetList_";
  protected final String TEXT_34 = ".addAll(regexUtil_";
  protected final String TEXT_35 = ".getSheets(workbook_";
  protected final String TEXT_36 = ", ";
  protected final String TEXT_37 = ", ";
  protected final String TEXT_38 = "));";
  protected final String TEXT_39 = NL + "        if(sheetList_";
  protected final String TEXT_40 = ".size() <= 0){" + NL + "        \tthrow new RuntimeException(\"Special sheets not exist!\");" + NL + "        }" + NL + "        " + NL + "        int nb_line_";
  protected final String TEXT_41 = " = 0;            " + NL + "" + NL + "        int begin_line_";
  protected final String TEXT_42 = " = ";
  protected final String TEXT_43 = "0";
  protected final String TEXT_44 = ";" + NL + "        " + NL + "        int footer_input_";
  protected final String TEXT_45 = " = ";
  protected final String TEXT_46 = "0";
  protected final String TEXT_47 = ";" + NL + "        " + NL + "        int end_line_";
  protected final String TEXT_48 = "=0;" + NL + "        for(jxl.Sheet sheet_";
  protected final String TEXT_49 = ":sheetList_";
  protected final String TEXT_50 = "){" + NL + "        \tend_line_";
  protected final String TEXT_51 = "+=sheet_";
  protected final String TEXT_52 = ".getRows();" + NL + "        }" + NL + "        end_line_";
  protected final String TEXT_53 = " -= footer_input_";
  protected final String TEXT_54 = ";" + NL + "        int limit_";
  protected final String TEXT_55 = " = ";
  protected final String TEXT_56 = "-1";
  protected final String TEXT_57 = ";" + NL + "        int start_column_";
  protected final String TEXT_58 = " = ";
  protected final String TEXT_59 = "0";
  protected final String TEXT_60 = "-1";
  protected final String TEXT_61 = ";" + NL + "        int end_column_";
  protected final String TEXT_62 = " = sheetList_";
  protected final String TEXT_63 = ".get(0).getColumns();";
  protected final String TEXT_64 = NL + "        Integer lastColumn_";
  protected final String TEXT_65 = " = ";
  protected final String TEXT_66 = ";" + NL + "        if(lastColumn_";
  protected final String TEXT_67 = "!=null){" + NL + "        \tend_column_";
  protected final String TEXT_68 = " = lastColumn_";
  protected final String TEXT_69 = ".intValue();" + NL + "        }";
  protected final String TEXT_70 = NL + "        jxl.Cell[] row_";
  protected final String TEXT_71 = " = null;" + NL + "        jxl.Sheet sheet_";
  protected final String TEXT_72 = " = sheetList_";
  protected final String TEXT_73 = ".get(0);" + NL + "        int rowCount_";
  protected final String TEXT_74 = " = 0;" + NL + "        int sheetIndex_";
  protected final String TEXT_75 = " = 0;" + NL + "        int currentRows_";
  protected final String TEXT_76 = " = sheetList_";
  protected final String TEXT_77 = ".get(0).getRows();" + NL + "        " + NL + "        //for the number format" + NL + "        java.text.DecimalFormat df_";
  protected final String TEXT_78 = " = new java.text.DecimalFormat(\"#.####################################\");" + NL + "\t\tchar separatorChar_";
  protected final String TEXT_79 = " = df_";
  protected final String TEXT_80 = ".getDecimalFormatSymbols().getDecimalSeparator();" + NL + "        " + NL + "        for(int i_";
  protected final String TEXT_81 = " = begin_line_";
  protected final String TEXT_82 = "; i_";
  protected final String TEXT_83 = " < end_line_";
  protected final String TEXT_84 = "; i_";
  protected final String TEXT_85 = "++){" + NL + "        " + NL + "        \tint emptyColumnCount_";
  protected final String TEXT_86 = " = 0;" + NL + "" + NL + "        \tif (limit_";
  protected final String TEXT_87 = " != -1 && nb_line_";
  protected final String TEXT_88 = " >= limit_";
  protected final String TEXT_89 = ") {" + NL + "        \t\tbreak;" + NL + "        \t}" + NL + "        \t" + NL + "            while (i_";
  protected final String TEXT_90 = " >= rowCount_";
  protected final String TEXT_91 = " + currentRows_";
  protected final String TEXT_92 = ") {" + NL + "                rowCount_";
  protected final String TEXT_93 = " += currentRows_";
  protected final String TEXT_94 = ";" + NL + "                sheet_";
  protected final String TEXT_95 = " = sheetList_";
  protected final String TEXT_96 = ".get(++sheetIndex_";
  protected final String TEXT_97 = ");" + NL + "                currentRows_";
  protected final String TEXT_98 = " = sheet_";
  protected final String TEXT_99 = ".getRows();" + NL + "            }";
  protected final String TEXT_100 = NL + "            if (rowCount_";
  protected final String TEXT_101 = " <= i_";
  protected final String TEXT_102 = ") {" + NL + "                row_";
  protected final String TEXT_103 = " = sheet_";
  protected final String TEXT_104 = ".getRow(i_";
  protected final String TEXT_105 = " - rowCount_";
  protected final String TEXT_106 = ");" + NL + "            }";
  protected final String TEXT_107 = NL + "            if (rowCount_";
  protected final String TEXT_108 = " <= i_";
  protected final String TEXT_109 = " && i_";
  protected final String TEXT_110 = " - rowCount_";
  protected final String TEXT_111 = " >= begin_line_";
  protected final String TEXT_112 = " && currentRows_";
  protected final String TEXT_113 = " - footer_input_";
  protected final String TEXT_114 = " > i_";
  protected final String TEXT_115 = " - rowCount_";
  protected final String TEXT_116 = ") {" + NL + "                row_";
  protected final String TEXT_117 = " = sheet_";
  protected final String TEXT_118 = ".getRow(i_";
  protected final String TEXT_119 = " - rowCount_";
  protected final String TEXT_120 = ");" + NL + "            }else{" + NL + "            \tcontinue;" + NL + "            }";
  protected final String TEXT_121 = "               " + NL + "        \tglobalMap.put(\"";
  protected final String TEXT_122 = "_CURRENT_SHEET\",sheet_";
  protected final String TEXT_123 = ".getName());";
  protected final String TEXT_124 = NL + "    \t\t";
  protected final String TEXT_125 = " = null;\t\t\t";
  protected final String TEXT_126 = NL + "\t\t\tString[] temp_row_";
  protected final String TEXT_127 = " = new String[";
  protected final String TEXT_128 = "];" + NL + "\t\t\tint actual_end_column_";
  protected final String TEXT_129 = " = end_column_";
  protected final String TEXT_130 = " >\trow_";
  protected final String TEXT_131 = ".length ? row_";
  protected final String TEXT_132 = ".length : end_column_";
  protected final String TEXT_133 = ";" + NL + "\t\t\tfor(int i=0;i<";
  protected final String TEXT_134 = ";i++){" + NL + "\t\t\t\t" + NL + "\t\t\t\tif(i + start_column_";
  protected final String TEXT_135 = " < actual_end_column_";
  protected final String TEXT_136 = "){" + NL + "\t\t\t\t" + NL + "\t\t\t\t  jxl.Cell cell_";
  protected final String TEXT_137 = " = row_";
  protected final String TEXT_138 = "[i + start_column_";
  protected final String TEXT_139 = "];" + NL + "\t\t\t\t  " + NL + "\t\t\t\t  if (";
  protected final String TEXT_140 = " && jxl.CellType.NUMBER == cell_";
  protected final String TEXT_141 = ".getType()){\t\t\t\t   " + NL + "\t\t\t\t  \ttemp_row_";
  protected final String TEXT_142 = "[i] = String.valueOf(((jxl.NumberCell)cell_";
  protected final String TEXT_143 = ").getValue());" + NL + "\t\t\t\t\tString content = cell_";
  protected final String TEXT_144 = ".getContents();" + NL + "\t\t\t\t\tif(content!=null && content.indexOf(separatorChar_";
  protected final String TEXT_145 = ")==-1 && (temp_row_";
  protected final String TEXT_146 = "[i].indexOf(\"E\")==-1)) {" + NL + "\t\t\t\t\t\ttemp_row_";
  protected final String TEXT_147 = "[i] = content;" + NL + "\t\t\t\t\t} else {" + NL + "    \t\t\t\t\tString literal = temp_row_";
  protected final String TEXT_148 = "[i];" + NL + "    \t\t\t\t\tif(literal!=null) {" + NL + "    \t\t\t\t\t\tliteral = df_";
  protected final String TEXT_149 = ".format(((jxl.NumberCell)cell_";
  protected final String TEXT_150 = ").getValue());" + NL + "    \t\t\t\t\t}" + NL + "    \t\t\t\t\ttemp_row_";
  protected final String TEXT_151 = "[i] = literal;" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t  } else{" + NL + "\t\t\t\t    temp_row_";
  protected final String TEXT_152 = "[i] = cell_";
  protected final String TEXT_153 = ".getContents();" + NL + "\t\t\t\t  }" + NL + "\t\t\t\t\t" + NL + "\t\t\t\t}else{" + NL + "\t\t\t\t\ttemp_row_";
  protected final String TEXT_154 = "[i]=\"\";" + NL + "\t\t\t\t}\t\t\t\t\t\t\t\t" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\tboolean whetherReject_";
  protected final String TEXT_155 = " = false;" + NL + "\t\t\t";
  protected final String TEXT_156 = " = new ";
  protected final String TEXT_157 = "Struct();" + NL + "\t\t\tint curColNum_";
  protected final String TEXT_158 = " = -1;" + NL + "\t\t\tString curColName_";
  protected final String TEXT_159 = " = \"\";" + NL + "\t\t\ttry {\t\t\t";
  protected final String TEXT_160 = "\t\t\t\t\t" + NL + "\t\t\tif(temp_row_";
  protected final String TEXT_161 = "[";
  protected final String TEXT_162 = "]";
  protected final String TEXT_163 = ".length() > 0) {" + NL + "\t\t\t\tcurColNum_";
  protected final String TEXT_164 = "=";
  protected final String TEXT_165 = " + start_column_";
  protected final String TEXT_166 = " + 1;" + NL + "\t\t\t\tcurColName_";
  protected final String TEXT_167 = " = \"";
  protected final String TEXT_168 = "\";";
  protected final String TEXT_169 = NL + "\t\t\t";
  protected final String TEXT_170 = ".";
  protected final String TEXT_171 = " = temp_row_";
  protected final String TEXT_172 = "[";
  protected final String TEXT_173 = "]";
  protected final String TEXT_174 = ";";
  protected final String TEXT_175 = "\t\t" + NL + "\t\t\tif(";
  protected final String TEXT_176 = "<actual_end_column_";
  protected final String TEXT_177 = "){" + NL + "\t\t\t\ttry{" + NL + "\t\t\t\t\tjava.util.Date dateGMT_";
  protected final String TEXT_178 = " = ((jxl.DateCell)row_";
  protected final String TEXT_179 = "[";
  protected final String TEXT_180 = " + start_column_";
  protected final String TEXT_181 = "]).getDate();" + NL + "\t\t\t\t\t";
  protected final String TEXT_182 = ".";
  protected final String TEXT_183 = " = new java.util.Date(dateGMT_";
  protected final String TEXT_184 = ".getTime() - java.util.TimeZone.getDefault().getOffset(dateGMT_";
  protected final String TEXT_185 = ".getTime()));" + NL + "\t\t\t\t}catch(Exception e){" + NL + "\t\t\t\t\t";
  protected final String TEXT_186 = NL + "\t\t\t\t\tthrow new RuntimeException(\"The cell format is not Date in ( Row. \"+(nb_line_";
  protected final String TEXT_187 = "+1)+ \" and ColumnNum. \" + curColNum_";
  protected final String TEXT_188 = " + \" )\");" + NL + "\t\t\t\t}" + NL + "\t\t\t}";
  protected final String TEXT_189 = NL + "\t\t";
  protected final String TEXT_190 = ".";
  protected final String TEXT_191 = " = ParserUtils.parseTo_";
  protected final String TEXT_192 = "(ParserUtils.parseTo_Number(temp_row_";
  protected final String TEXT_193 = "[";
  protected final String TEXT_194 = "]";
  protected final String TEXT_195 = ", ";
  protected final String TEXT_196 = ", ";
  protected final String TEXT_197 = "));";
  protected final String TEXT_198 = "\t\t\t\t\t\t\t" + NL + "\t\t\t";
  protected final String TEXT_199 = ".";
  protected final String TEXT_200 = " = temp_row_";
  protected final String TEXT_201 = "[";
  protected final String TEXT_202 = "]";
  protected final String TEXT_203 = ".getBytes(";
  protected final String TEXT_204 = ");" + NL + "\t";
  protected final String TEXT_205 = NL + "\t\t\t";
  protected final String TEXT_206 = ".";
  protected final String TEXT_207 = " = ParserUtils.parseTo_";
  protected final String TEXT_208 = "(temp_row_";
  protected final String TEXT_209 = "[";
  protected final String TEXT_210 = "]";
  protected final String TEXT_211 = ");";
  protected final String TEXT_212 = "\t\t\t\t\t" + NL + "\t\t\t}else {";
  protected final String TEXT_213 = NL + "\t\t\t\tthrow new RuntimeException(\"Value is empty for column : '";
  protected final String TEXT_214 = "' in '";
  protected final String TEXT_215 = "' connection, value is invalid or this column should be nullable or have a default value.\");";
  protected final String TEXT_216 = NL + "\t\t\t\t";
  protected final String TEXT_217 = ".";
  protected final String TEXT_218 = " = ";
  protected final String TEXT_219 = ";" + NL + "\t\t\t\temptyColumnCount_";
  protected final String TEXT_220 = "++;";
  protected final String TEXT_221 = NL + "\t\t}";
  protected final String TEXT_222 = NL + "\t\t\t\t\t";
  protected final String TEXT_223 = " ";
  protected final String TEXT_224 = " = null; ";
  protected final String TEXT_225 = NL;
  protected final String TEXT_226 = NL + "        if(emptyColumnCount_";
  protected final String TEXT_227 = " == ";
  protected final String TEXT_228 = "){" + NL + "        \tbreak; //if meet the empty row, there will break the iterate." + NL + "        }";
  protected final String TEXT_229 = "  " + NL + "\t\t\t\t\t" + NL + "    } catch (Exception e) {" + NL + "        whetherReject_";
  protected final String TEXT_230 = " = true;";
  protected final String TEXT_231 = NL + "            throw(e);";
  protected final String TEXT_232 = NL + "                    ";
  protected final String TEXT_233 = " = new ";
  protected final String TEXT_234 = "Struct();";
  protected final String TEXT_235 = NL + "                    ";
  protected final String TEXT_236 = ".";
  protected final String TEXT_237 = " = ";
  protected final String TEXT_238 = ".";
  protected final String TEXT_239 = ";";
  protected final String TEXT_240 = NL + "                ";
  protected final String TEXT_241 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_242 = "+ \" column: \" + curColName_";
  protected final String TEXT_243 = " + \" (No. \" + curColNum_";
  protected final String TEXT_244 = " + \")\";";
  protected final String TEXT_245 = NL + "                ";
  protected final String TEXT_246 = " = null;";
  protected final String TEXT_247 = NL + "                System.err.println(e.getMessage());";
  protected final String TEXT_248 = NL + "                ";
  protected final String TEXT_249 = " = null;";
  protected final String TEXT_250 = NL + "            \t";
  protected final String TEXT_251 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_252 = "+ \" column: \" + curColName_";
  protected final String TEXT_253 = " + \" (No. \" + curColNum_";
  protected final String TEXT_254 = " + \")\";";
  protected final String TEXT_255 = NL + "    }\t\t\t\t\t" + NL + "\t\t\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_256 = NL + "\t\t";
  protected final String TEXT_257 = "if(!whetherReject_";
  protected final String TEXT_258 = ") { ";
  protected final String TEXT_259 = "      " + NL + "             if(";
  protected final String TEXT_260 = " == null){ " + NL + "            \t ";
  protected final String TEXT_261 = " = new ";
  protected final String TEXT_262 = "Struct();" + NL + "             }\t\t\t\t";
  protected final String TEXT_263 = NL + "\t    \t ";
  protected final String TEXT_264 = ".";
  protected final String TEXT_265 = " = ";
  protected final String TEXT_266 = ".";
  protected final String TEXT_267 = ";    \t\t\t\t";
  protected final String TEXT_268 = NL + "\t\t";
  protected final String TEXT_269 = " } ";
  protected final String TEXT_270 = "\t";
  protected final String TEXT_271 = NL + "\t\t\torg.apache.log4j.Logger logger_";
  protected final String TEXT_272 = " = org.apache.log4j.Logger.getLogger(\"org.openxml4j.opc\");" + NL + "\t        logger_";
  protected final String TEXT_273 = ".setLevel(org.apache.log4j.Level.WARN);" + NL + "\t\t\tclass RegexUtil_";
  protected final String TEXT_274 = " {" + NL + "\t\t\t\t" + NL + "\t\t    \tpublic java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> getSheets(org.apache.poi.xssf.usermodel.XSSFWorkbook workbook, String oneSheetName, boolean useRegex) {" + NL + "\t\t\t        " + NL + "\t\t\t        java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> list = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();" + NL + "\t\t\t        " + NL + "\t\t\t        if(useRegex){//this part process the regex issue" + NL + "\t\t\t        \t" + NL + "\t\t\t\t        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(oneSheetName);" + NL + "\t\t\t\t        for (org.apache.poi.xssf.usermodel.XSSFSheet sheet : workbook) {" + NL + "\t\t\t\t            String sheetName = sheet.getSheetName();" + NL + "\t\t\t\t            java.util.regex.Matcher matcher = pattern.matcher(sheetName);" + NL + "\t\t\t\t            if (matcher.matches()) {" + NL + "\t\t\t\t            \tif(sheet != null){" + NL + "\t\t\t\t                \tlist.add(sheet);" + NL + "\t\t\t\t                }\t" + NL + "\t\t\t\t            }" + NL + "\t\t\t\t        }" + NL + "\t\t\t\t        " + NL + "\t\t\t        }else{\t" + NL + "\t\t\t        \torg.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.getSheet(oneSheetName);" + NL + "\t\t            \tif(sheet != null){" + NL + "\t\t                \tlist.add(sheet);" + NL + "\t\t                }" + NL + "\t\t\t        \t" + NL + "\t\t\t        }" + NL + "\t\t\t        " + NL + "\t\t\t        return list;" + NL + "\t\t\t    }" + NL + "\t\t    \t" + NL + "\t\t\t    public java.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> getSheets(org.apache.poi.xssf.usermodel.XSSFWorkbook workbook, int index, boolean useRegex) {" + NL + "\t\t\t    \tjava.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> list =  new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();" + NL + "\t\t\t    \torg.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.getSheetAt(index);" + NL + "\t            \tif(sheet != null){" + NL + "\t                \tlist.add(sheet);" + NL + "\t                }" + NL + "\t\t\t    \treturn list;" + NL + "\t\t\t    }" + NL + "\t\t\t    " + NL + "\t\t\t}" + NL + "\t\tRegexUtil_";
  protected final String TEXT_275 = " regexUtil_";
  protected final String TEXT_276 = " = new RegexUtil_";
  protected final String TEXT_277 = "();" + NL + "\t\t" + NL + "\t\tObject source_";
  protected final String TEXT_278 = " = ";
  protected final String TEXT_279 = ";" + NL + "\t\torg.apache.poi.xssf.usermodel.XSSFWorkbook workbook_";
  protected final String TEXT_280 = " = null;" + NL + "\t\t" + NL + "\t\tif(source_";
  protected final String TEXT_281 = " instanceof java.io.InputStream || source_";
  protected final String TEXT_282 = " instanceof String){" + NL + "\t\t\tworkbook_";
  protected final String TEXT_283 = " = new org.apache.poi.xssf.usermodel.XSSFWorkbook(";
  protected final String TEXT_284 = ");" + NL + "\t\t}else{" + NL + "\t\t\tworkbook_";
  protected final String TEXT_285 = " = null;" + NL + "\t\t\tthrow new Exception(\"The data source should be specified as Inputstream or File Path!\");" + NL + "\t\t}" + NL + "\t\ttry {" + NL + "\t\t";
  protected final String TEXT_286 = NL + "    \tjava.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> sheetList_";
  protected final String TEXT_287 = " = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();" + NL + "    \tfor(org.apache.poi.xssf.usermodel.XSSFSheet sheet_";
  protected final String TEXT_288 = ":workbook_";
  protected final String TEXT_289 = "){" + NL + "    \t\tsheetList_";
  protected final String TEXT_290 = ".add(sheet_";
  protected final String TEXT_291 = ");" + NL + "    \t}";
  protected final String TEXT_292 = NL + "\t\tjava.util.List<org.apache.poi.xssf.usermodel.XSSFSheet> sheetList_";
  protected final String TEXT_293 = " = new java.util.ArrayList<org.apache.poi.xssf.usermodel.XSSFSheet>();";
  protected final String TEXT_294 = NL + "        sheetList_";
  protected final String TEXT_295 = ".addAll(regexUtil_";
  protected final String TEXT_296 = ".getSheets(workbook_";
  protected final String TEXT_297 = ", ";
  protected final String TEXT_298 = ", ";
  protected final String TEXT_299 = "));";
  protected final String TEXT_300 = NL + "    \tif(sheetList_";
  protected final String TEXT_301 = ".size() <= 0){" + NL + "            throw new RuntimeException(\"Special sheets not exist!\");" + NL + "        }" + NL + "\t\t" + NL + "\t\tint nb_line_";
  protected final String TEXT_302 = " = 0;            " + NL + "" + NL + "        int begin_line_";
  protected final String TEXT_303 = " = ";
  protected final String TEXT_304 = "0";
  protected final String TEXT_305 = ";" + NL + "        " + NL + "        int footer_input_";
  protected final String TEXT_306 = " = ";
  protected final String TEXT_307 = "0";
  protected final String TEXT_308 = ";" + NL + "        " + NL + "        int end_line_";
  protected final String TEXT_309 = "=0;" + NL + "        for(org.apache.poi.xssf.usermodel.XSSFSheet sheet_";
  protected final String TEXT_310 = ":sheetList_";
  protected final String TEXT_311 = "){" + NL + "        \tend_line_";
  protected final String TEXT_312 = "+=(sheet_";
  protected final String TEXT_313 = ".getLastRowNum()+1);" + NL + "        }" + NL + "        end_line_";
  protected final String TEXT_314 = " -= footer_input_";
  protected final String TEXT_315 = ";" + NL + "        int limit_";
  protected final String TEXT_316 = " = ";
  protected final String TEXT_317 = "-1";
  protected final String TEXT_318 = ";" + NL + "        int start_column_";
  protected final String TEXT_319 = " = ";
  protected final String TEXT_320 = "0";
  protected final String TEXT_321 = "-1";
  protected final String TEXT_322 = ";" + NL + "        int end_column_";
  protected final String TEXT_323 = " = -1;";
  protected final String TEXT_324 = "       " + NL + "        Integer lastColumn_";
  protected final String TEXT_325 = " = ";
  protected final String TEXT_326 = ";" + NL + "        if(lastColumn_";
  protected final String TEXT_327 = "!=null){" + NL + "        \tend_column_";
  protected final String TEXT_328 = " = lastColumn_";
  protected final String TEXT_329 = ".intValue();" + NL + "        }        ";
  protected final String TEXT_330 = ";" + NL + "        " + NL + "        org.apache.poi.xssf.usermodel.XSSFRow row_";
  protected final String TEXT_331 = " = null;" + NL + "        org.apache.poi.xssf.usermodel.XSSFSheet sheet_";
  protected final String TEXT_332 = " = sheetList_";
  protected final String TEXT_333 = ".get(0);" + NL + "        int rowCount_";
  protected final String TEXT_334 = " = 0;" + NL + "        int sheetIndex_";
  protected final String TEXT_335 = " = 0;" + NL + "        int currentRows_";
  protected final String TEXT_336 = " = (sheetList_";
  protected final String TEXT_337 = ".get(0).getLastRowNum()+1);" + NL + "\t\t" + NL + "\t\t//for the number format        " + NL + "        java.text.DecimalFormat df_";
  protected final String TEXT_338 = " = new java.text.DecimalFormat(\"#.####################################\");" + NL + "        char decimalChar_";
  protected final String TEXT_339 = " = df_";
  protected final String TEXT_340 = ".getDecimalFormatSymbols().getDecimalSeparator();" + NL + "        org.apache.poi.hssf.usermodel.HSSFDataFormatter dataFormat_";
  protected final String TEXT_341 = "=new org.apache.poi.hssf.usermodel.HSSFDataFormatter();" + NL + "\t\tjava.text.NumberFormat numberFormat_";
  protected final String TEXT_342 = "=java.text.NumberFormat.getInstance();" + NL + "        " + NL + "        for(int i_";
  protected final String TEXT_343 = " = begin_line_";
  protected final String TEXT_344 = "; i_";
  protected final String TEXT_345 = " < end_line_";
  protected final String TEXT_346 = "; i_";
  protected final String TEXT_347 = "++){" + NL + "       " + NL + "        \tint emptyColumnCount_";
  protected final String TEXT_348 = " = 0;" + NL + "" + NL + "        \tif (limit_";
  protected final String TEXT_349 = " != -1 && nb_line_";
  protected final String TEXT_350 = " >= limit_";
  protected final String TEXT_351 = ") {" + NL + "        \t\tbreak;" + NL + "        \t}" + NL + "        \t" + NL + "            while (i_";
  protected final String TEXT_352 = " >= rowCount_";
  protected final String TEXT_353 = " + currentRows_";
  protected final String TEXT_354 = ") {" + NL + "                rowCount_";
  protected final String TEXT_355 = " += currentRows_";
  protected final String TEXT_356 = ";" + NL + "                sheet_";
  protected final String TEXT_357 = " = sheetList_";
  protected final String TEXT_358 = ".get(++sheetIndex_";
  protected final String TEXT_359 = ");" + NL + "                currentRows_";
  protected final String TEXT_360 = " = (sheet_";
  protected final String TEXT_361 = ".getLastRowNum()+1);" + NL + "            }" + NL + "            globalMap.put(\"";
  protected final String TEXT_362 = "_CURRENT_SHEET\",sheet_";
  protected final String TEXT_363 = ".getSheetName());";
  protected final String TEXT_364 = NL + "            if (rowCount_";
  protected final String TEXT_365 = " <= i_";
  protected final String TEXT_366 = ") {" + NL + "                row_";
  protected final String TEXT_367 = " = sheet_";
  protected final String TEXT_368 = ".getRow(i_";
  protected final String TEXT_369 = " - rowCount_";
  protected final String TEXT_370 = ");" + NL + "            }";
  protected final String TEXT_371 = NL + "            if (rowCount_";
  protected final String TEXT_372 = " <= i_";
  protected final String TEXT_373 = " && i_";
  protected final String TEXT_374 = " - rowCount_";
  protected final String TEXT_375 = " >= begin_line_";
  protected final String TEXT_376 = " && currentRows_";
  protected final String TEXT_377 = " - footer_input_";
  protected final String TEXT_378 = " > i_";
  protected final String TEXT_379 = " - rowCount_";
  protected final String TEXT_380 = ") {" + NL + "                row_";
  protected final String TEXT_381 = " = sheet_";
  protected final String TEXT_382 = ".getRow(i_";
  protected final String TEXT_383 = " - rowCount_";
  protected final String TEXT_384 = ");" + NL + "            }else{" + NL + "            \tcontinue;" + NL + "            }";
  protected final String TEXT_385 = "          ";
  protected final String TEXT_386 = NL + "\t\t    ";
  protected final String TEXT_387 = " = null;\t\t\t";
  protected final String TEXT_388 = NL + "\t\t\tString[] temp_row_";
  protected final String TEXT_389 = " = new String[";
  protected final String TEXT_390 = "];";
  protected final String TEXT_391 = "\t\t\t\t" + NL + "\t\t\t\tList<Boolean> datelist_";
  protected final String TEXT_392 = " = new java.util.ArrayList<Boolean>();" + NL + "\t\t\t\tList<String> patternlist_";
  protected final String TEXT_393 = " = new java.util.ArrayList<String>();";
  protected final String TEXT_394 = NL + "\t\t\t\t\t\t\tdatelist_";
  protected final String TEXT_395 = ".add(";
  protected final String TEXT_396 = ");" + NL + "\t\t\t\t\t\t\tpatternlist_";
  protected final String TEXT_397 = ".add(";
  protected final String TEXT_398 = ");";
  protected final String TEXT_399 = NL + "\t\t\tint excel_end_column_";
  protected final String TEXT_400 = ";" + NL + "\t\t\tif(row_";
  protected final String TEXT_401 = "==null){" + NL + "\t\t\t\texcel_end_column_";
  protected final String TEXT_402 = "=0;" + NL + "\t\t\t}else{" + NL + "\t\t\t\texcel_end_column_";
  protected final String TEXT_403 = "=row_";
  protected final String TEXT_404 = ".getLastCellNum();" + NL + "\t\t\t}" + NL + "\t\t\tint actual_end_column_";
  protected final String TEXT_405 = ";" + NL + "\t\t\tif(end_column_";
  protected final String TEXT_406 = " == -1){" + NL + "\t\t\t\tactual_end_column_";
  protected final String TEXT_407 = " = excel_end_column_";
  protected final String TEXT_408 = ";" + NL + "\t\t\t}" + NL + "\t\t\telse{" + NL + "\t\t\t\tactual_end_column_";
  protected final String TEXT_409 = " = end_column_";
  protected final String TEXT_410 = " >\texcel_end_column_";
  protected final String TEXT_411 = " ? excel_end_column_";
  protected final String TEXT_412 = " : end_column_";
  protected final String TEXT_413 = ";" + NL + "\t\t\t}" + NL + "\t\t\tfor(int i=0;i<";
  protected final String TEXT_414 = ";i++){" + NL + "\t\t\t\tif(i + start_column_";
  protected final String TEXT_415 = " < actual_end_column_";
  protected final String TEXT_416 = "){" + NL + "\t\t\t\t\torg.apache.poi.ss.usermodel.Cell cell_";
  protected final String TEXT_417 = " = row_";
  protected final String TEXT_418 = ".getCell(i + start_column_";
  protected final String TEXT_419 = ");" + NL + "\t\t\t\t\tif(cell_";
  protected final String TEXT_420 = "!=null){" + NL + "\t\t\t\t\tswitch (cell_";
  protected final String TEXT_421 = ".getCellType()) {" + NL + "                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:" + NL + "                            temp_row_";
  protected final String TEXT_422 = "[i] = cell_";
  protected final String TEXT_423 = ".getRichStringCellValue().getString();" + NL + "                            break;" + NL + "                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:" + NL + "                            if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell_";
  protected final String TEXT_424 = ")) {";
  protected final String TEXT_425 = NL + "                    \t        \tif(datelist_";
  protected final String TEXT_426 = ".get(i)){" + NL + "                        \t        \ttemp_row_";
  protected final String TEXT_427 = "[i] = FormatterUtils.format_Date(cell_";
  protected final String TEXT_428 = ".getDateCellValue(),patternlist_";
  protected final String TEXT_429 = ".get(i));" + NL + "                            \t   \t} else{" + NL + "                                \t\ttemp_row_";
  protected final String TEXT_430 = "[i] = cell_";
  protected final String TEXT_431 = ".getDateCellValue().toString();" + NL + "                                \t}";
  protected final String TEXT_432 = NL + "\t\t\t\t\t\t\t\t\ttemp_row_";
  protected final String TEXT_433 = "[i] =cell_";
  protected final String TEXT_434 = ".getDateCellValue().toString();";
  protected final String TEXT_435 = NL + "                            } else {" + NL + "                                temp_row_";
  protected final String TEXT_436 = "[i] = df_";
  protected final String TEXT_437 = ".format(cell_";
  protected final String TEXT_438 = ".getNumericCellValue());" + NL + "                            }" + NL + "                            break;" + NL + "                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:" + NL + "                            temp_row_";
  protected final String TEXT_439 = "[i] =String.valueOf(cell_";
  protected final String TEXT_440 = ".getBooleanCellValue());" + NL + "                            break;" + NL + "                        case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:" + NL + "        \t\t\t\t\tswitch (cell_";
  protected final String TEXT_441 = ".getCachedFormulaResultType()) {" + NL + "                                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:" + NL + "                                    temp_row_";
  protected final String TEXT_442 = "[i] = cell_";
  protected final String TEXT_443 = ".getRichStringCellValue().getString();" + NL + "                                    break;" + NL + "                                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:" + NL + "                                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell_";
  protected final String TEXT_444 = ")) {";
  protected final String TEXT_445 = NL + "                    \t        \t\t\tif(datelist_";
  protected final String TEXT_446 = ".get(i)){" + NL + "                        \t        \t\t\ttemp_row_";
  protected final String TEXT_447 = "[i] = FormatterUtils.format_Date(cell_";
  protected final String TEXT_448 = ".getDateCellValue(),patternlist_";
  protected final String TEXT_449 = ".get(i));" + NL + "                            \t   \t\t\t} else{" + NL + "                                \t\t\t\ttemp_row_";
  protected final String TEXT_450 = "[i] =cell_";
  protected final String TEXT_451 = ".getDateCellValue().toString();" + NL + "                                \t\t\t}";
  protected final String TEXT_452 = NL + "\t\t\t\t\t\t\t\t\t\t\ttemp_row_";
  protected final String TEXT_453 = "[i] =cell_";
  protected final String TEXT_454 = ".getDateCellValue().toString();";
  protected final String TEXT_455 = NL + "                                    } else {" + NL + "                                         temp_row_";
  protected final String TEXT_456 = "[i] = df_";
  protected final String TEXT_457 = ".format(numberFormat_";
  protected final String TEXT_458 = ".parse(dataFormat_";
  protected final String TEXT_459 = ".formatCellValue(cell_";
  protected final String TEXT_460 = ",workbook_";
  protected final String TEXT_461 = ".getCreationHelper().createFormulaEvaluator())));" + NL + "                                    }" + NL + "                                    break;" + NL + "                                case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:" + NL + "                                    temp_row_";
  protected final String TEXT_462 = "[i] =String.valueOf(cell_";
  protected final String TEXT_463 = ".getBooleanCellValue());" + NL + "                                    break;" + NL + "                                default:" + NL + "                            \t\ttemp_row_";
  protected final String TEXT_464 = "[i] = \"\";" + NL + "                            }" + NL + "                            break;" + NL + "                        default:" + NL + "                            temp_row_";
  protected final String TEXT_465 = "[i] = \"\";" + NL + "                        }" + NL + "                \t}" + NL + "                \telse{" + NL + "                \t\ttemp_row_";
  protected final String TEXT_466 = "[i]=\"\";" + NL + "                \t}" + NL + "\t\t\t\t\t" + NL + "\t\t\t\t}else{" + NL + "\t\t\t\t\ttemp_row_";
  protected final String TEXT_467 = "[i]=\"\";" + NL + "\t\t\t\t}\t\t\t\t\t\t\t\t" + NL + "\t\t\t}" + NL + "\t\t\tboolean whetherReject_";
  protected final String TEXT_468 = " = false;" + NL + "\t\t\t";
  protected final String TEXT_469 = " = new ";
  protected final String TEXT_470 = "Struct();" + NL + "\t\t\tint curColNum_";
  protected final String TEXT_471 = " = -1;" + NL + "\t\t\tString curColName_";
  protected final String TEXT_472 = " = \"\";" + NL + "\t\t\ttry{";
  protected final String TEXT_473 = NL + "\t\t\tif(temp_row_";
  protected final String TEXT_474 = "[";
  protected final String TEXT_475 = "]";
  protected final String TEXT_476 = ".length() > 0) {" + NL + "\t\t\t\tcurColNum_";
  protected final String TEXT_477 = "=";
  protected final String TEXT_478 = " + start_column_";
  protected final String TEXT_479 = " + 1;" + NL + "\t\t\t\tcurColName_";
  protected final String TEXT_480 = " = \"";
  protected final String TEXT_481 = "\";" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_482 = "\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_483 = ".";
  protected final String TEXT_484 = " = temp_row_";
  protected final String TEXT_485 = "[";
  protected final String TEXT_486 = "]";
  protected final String TEXT_487 = ";";
  protected final String TEXT_488 = "\t\t" + NL + "\t\t\t\tif(";
  protected final String TEXT_489 = "<actual_end_column_";
  protected final String TEXT_490 = "){" + NL + "\t\t\t\t\ttry{" + NL + "\t\t\t\t\t\tif(row_";
  protected final String TEXT_491 = ".getCell(";
  protected final String TEXT_492 = "+ start_column_";
  protected final String TEXT_493 = ").getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC && org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(row_";
  protected final String TEXT_494 = ".getCell(";
  protected final String TEXT_495 = "+ start_column_";
  protected final String TEXT_496 = "))){" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_497 = ".";
  protected final String TEXT_498 = " = row_";
  protected final String TEXT_499 = ".getCell(";
  protected final String TEXT_500 = "+ start_column_";
  protected final String TEXT_501 = ").getDateCellValue();" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\telse{" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_502 = ".";
  protected final String TEXT_503 = " = ParserUtils.parseTo_Date(temp_row_";
  protected final String TEXT_504 = "[";
  protected final String TEXT_505 = "]";
  protected final String TEXT_506 = ", ";
  protected final String TEXT_507 = ");" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t}catch(Exception e){" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_508 = NL + "\t\t\t\t\t\tthrow new RuntimeException(\"The cell format is not Date in ( Row. \"+(nb_line_";
  protected final String TEXT_509 = "+1)+ \" and ColumnNum. \" + curColNum_";
  protected final String TEXT_510 = " + \" )\");" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}" + NL + "\t\t\t\t";
  protected final String TEXT_511 = NL + "\t\t\t\t";
  protected final String TEXT_512 = ".";
  protected final String TEXT_513 = " = ParserUtils.parseTo_";
  protected final String TEXT_514 = "(ParserUtils.parseTo_Number(temp_row_";
  protected final String TEXT_515 = "[";
  protected final String TEXT_516 = "]";
  protected final String TEXT_517 = ", ";
  protected final String TEXT_518 = ", ";
  protected final String TEXT_519 = "));";
  protected final String TEXT_520 = NL + "\t\t\t\t";
  protected final String TEXT_521 = ".";
  protected final String TEXT_522 = " = ParserUtils.parseTo_";
  protected final String TEXT_523 = "(ParserUtils.parseTo_Number(temp_row_";
  protected final String TEXT_524 = "[";
  protected final String TEXT_525 = "]";
  protected final String TEXT_526 = ", null, '.'==decimalChar_";
  protected final String TEXT_527 = " ? null : decimalChar_";
  protected final String TEXT_528 = "));";
  protected final String TEXT_529 = "\t\t\t\t\t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_530 = ".";
  protected final String TEXT_531 = " = temp_row_";
  protected final String TEXT_532 = "[";
  protected final String TEXT_533 = "]";
  protected final String TEXT_534 = ".getBytes(";
  protected final String TEXT_535 = ");";
  protected final String TEXT_536 = NL + "\t\t\t\t";
  protected final String TEXT_537 = ".";
  protected final String TEXT_538 = " = ParserUtils.parseTo_";
  protected final String TEXT_539 = "(temp_row_";
  protected final String TEXT_540 = "[";
  protected final String TEXT_541 = "]";
  protected final String TEXT_542 = ");";
  protected final String TEXT_543 = NL + "\t\t\t}else{";
  protected final String TEXT_544 = NL + "\t\t\t\t\tthrow new RuntimeException(\"Value is empty for column : '";
  protected final String TEXT_545 = "' in '";
  protected final String TEXT_546 = "' connection, value is invalid or this column should be nullable or have a default value.\");";
  protected final String TEXT_547 = NL + "\t\t\t\t";
  protected final String TEXT_548 = ".";
  protected final String TEXT_549 = " = ";
  protected final String TEXT_550 = ";" + NL + "\t\t\t\temptyColumnCount_";
  protected final String TEXT_551 = "++;";
  protected final String TEXT_552 = NL + "\t\t\t}";
  protected final String TEXT_553 = " ";
  protected final String TEXT_554 = " = null; ";
  protected final String TEXT_555 = NL;
  protected final String TEXT_556 = NL + "        if(emptyColumnCount_";
  protected final String TEXT_557 = " == ";
  protected final String TEXT_558 = "){" + NL + "        \tbreak; //if meet the empty row, there will break the iterate." + NL + "        }";
  protected final String TEXT_559 = "  " + NL + "\t\t\t}catch(Exception e){" + NL + "\t\t\twhetherReject_";
  protected final String TEXT_560 = " = true;";
  protected final String TEXT_561 = NL + "\t            throw(e);";
  protected final String TEXT_562 = NL + "\t\t\t\t\t";
  protected final String TEXT_563 = " = new ";
  protected final String TEXT_564 = "Struct();";
  protected final String TEXT_565 = NL + "\t\t\t\t\t";
  protected final String TEXT_566 = ".";
  protected final String TEXT_567 = " = ";
  protected final String TEXT_568 = ".";
  protected final String TEXT_569 = ";";
  protected final String TEXT_570 = NL + "\t\t\t\t\t";
  protected final String TEXT_571 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_572 = "+ \" column: \" + curColName_";
  protected final String TEXT_573 = " + \" (No. \" + curColNum_";
  protected final String TEXT_574 = " + \")\";" + NL + "\t\t\t\t\t";
  protected final String TEXT_575 = " = null;";
  protected final String TEXT_576 = NL + "\t\t\t\t\t System.err.println(e.getMessage());" + NL + "\t\t\t\t\t ";
  protected final String TEXT_577 = " = null;";
  protected final String TEXT_578 = NL + "\t\t\t\t\t";
  protected final String TEXT_579 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_580 = "+ \" column: \" + curColName_";
  protected final String TEXT_581 = " + \" (No. \" + curColNum_";
  protected final String TEXT_582 = " + \")\";";
  protected final String TEXT_583 = NL + "\t\t\t}\t" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_584 = NL + "\t\t";
  protected final String TEXT_585 = "if(!whetherReject_";
  protected final String TEXT_586 = ") { ";
  protected final String TEXT_587 = "      " + NL + "             if(";
  protected final String TEXT_588 = " == null){ " + NL + "            \t ";
  protected final String TEXT_589 = " = new ";
  protected final String TEXT_590 = "Struct();" + NL + "             }\t\t\t\t";
  protected final String TEXT_591 = NL + "\t    \t ";
  protected final String TEXT_592 = ".";
  protected final String TEXT_593 = " = ";
  protected final String TEXT_594 = ".";
  protected final String TEXT_595 = ";    \t\t\t\t";
  protected final String TEXT_596 = NL + "\t\t";
  protected final String TEXT_597 = " } ";
  protected final String TEXT_598 = "\t";
  protected final String TEXT_599 = NL;
  protected final String TEXT_600 = NL;
  protected final String TEXT_601 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
	IMetadataTable metadata = metadatas.get(0);
	if (metadata!=null) {
		boolean version07 = ("true").equals(ElementParameterParser.getValue(node,"__VERSION_2007__"));
	
		String fileName = ElementParameterParser.getValue(node,"__FILENAME__");
	
    	String header = ElementParameterParser.getValue(node, "__HEADER__");
    	String limit = ElementParameterParser.getValue(node, "__LIMIT__");
    	String footer = ElementParameterParser.getValue(node, "__FOOTER__");
    	String firstColumn = ElementParameterParser.getValue(node, "__FIRST_COLUMN__");
    	String lastColumn = ElementParameterParser.getValue(node, "__LAST_COLUMN__");
    	String dieOnErrorStr = ElementParameterParser.getValue(node, "__DIE_ON_ERROR__");
		boolean dieOnError = (dieOnErrorStr!=null&&!("").equals(dieOnErrorStr))?("true").equals(dieOnErrorStr):false;
		String encoding = ElementParameterParser.getValue(node,"__ENCODING__");
		
		String allSheets = ElementParameterParser.getValue(node, "__ALL_SHEETS__");
		boolean isAllSheets = (allSheets!=null&&!("").equals(allSheets))?("true").equals(allSheets):false;
		List<Map<String, String>> sheetNameList = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__SHEETLIST__");
		
		String advancedSeparatorStr = ElementParameterParser.getValue(node, "__ADVANCED_SEPARATOR__");
		boolean advancedSeparator = (advancedSeparatorStr!=null&&!("").equals(advancedSeparatorStr))?("true").equals(advancedSeparatorStr):false;
		String thousandsSeparator = ElementParameterParser.getValueWithJavaType(node, "__THOUSANDS_SEPARATOR__", JavaTypesManager.CHARACTER);
		String decimalSeparator = ElementParameterParser.getValueWithJavaType(node, "__DECIMAL_SEPARATOR__", JavaTypesManager.CHARACTER);
		
		boolean affect = ("true").equals(ElementParameterParser.getValue(node,"__AFFECT_EACH_SHEET__"));
		boolean stopOnEmptyRow = ("true").equals(ElementParameterParser.getValue(node,"__STOPREAD_ON_EMPTYROW__"));
		
		List<Map<String, String>> trimSelects = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__TRIMSELECT__");
		String isTrimAllStr = ElementParameterParser.getValue(node,"__TRIMALL__");
		boolean isTrimAll = (isTrimAllStr!=null&&!("").equals(isTrimAllStr))?("true").equals(isTrimAllStr):true;
		
		if(!version07){//version judgement
			boolean bReadRealValue = ("true").equals(ElementParameterParser.getValue(node, "__READ_REAL_VALUE__"));
			boolean notNeedValidateOnCell = !("false").equals(ElementParameterParser.getValue(node,"__NOVALIDATE_ON_CELL__"));//make wizard work
			boolean suppressWarn = !("false").equals(ElementParameterParser.getValue(node,"__SUPPRESS_WARN__"));//make wizard work

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_6);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_8);
    
		if(notNeedValidateOnCell==true){

    stringBuffer.append(TEXT_9);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_10);
    
		}
		if(suppressWarn ==true){

    stringBuffer.append(TEXT_11);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_12);
    
		}

    stringBuffer.append(TEXT_13);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(fileName);
    stringBuffer.append(TEXT_17);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_27);
          
		if(isAllSheets){

    stringBuffer.append(TEXT_28);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_30);
    
		}else{

    stringBuffer.append(TEXT_31);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_32);
    
			for(Map<String, String> tmp:sheetNameList){

    stringBuffer.append(TEXT_33);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(tmp.get("SHEETNAME"));
    stringBuffer.append(TEXT_37);
    stringBuffer.append((tmp.get("USE_REGEX")!=null&&!"".equals(tmp.get("USE_REGEX")))?"true".equals(tmp.get("USE_REGEX")):false);
    stringBuffer.append(TEXT_38);
    
			}
		}

    stringBuffer.append(TEXT_39);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_40);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_42);
    if(("").equals(header.trim())){
    stringBuffer.append(TEXT_43);
    }else{
    stringBuffer.append( header );
    }
    stringBuffer.append(TEXT_44);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_45);
    if(("").equals(footer.trim())){
    stringBuffer.append(TEXT_46);
    }else{
    stringBuffer.append(footer);
    }
    stringBuffer.append(TEXT_47);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_50);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_55);
    if(("").equals(limit.trim())){
    stringBuffer.append(TEXT_56);
    }else{
    stringBuffer.append(limit);
    }
    stringBuffer.append(TEXT_57);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_58);
    if(("").equals(firstColumn.trim())){
    stringBuffer.append(TEXT_59);
    }else{
    stringBuffer.append(firstColumn);
    stringBuffer.append(TEXT_60);
    }
    stringBuffer.append(TEXT_61);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_63);
    if(lastColumn!=null && !("").equals(lastColumn.trim())){
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(lastColumn);
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    }
    stringBuffer.append(TEXT_70);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_71);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_76);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_83);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_84);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_88);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_90);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_92);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_94);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_99);
    
	if(!affect){

    stringBuffer.append(TEXT_100);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_102);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_103);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_106);
    
	}else{

    stringBuffer.append(TEXT_107);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_109);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_115);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_116);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_117);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_119);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_120);
    
    }

    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_123);
    
//begin
//
	List< ? extends IConnection> conns = node.getOutgoingSortedConnections();

    String rejectConnName = "";
    List<? extends IConnection> rejectConns = node.getOutgoingConnections("REJECT");
    if(rejectConns != null && rejectConns.size() > 0) {
        IConnection rejectConn = rejectConns.get(0);
        rejectConnName = rejectConn.getName();
    }
    List<IMetadataColumn> rejectColumnList = null;
    IMetadataTable metadataTable = node.getMetadataFromConnector("REJECT");
    if(metadataTable != null) {
        rejectColumnList = metadataTable.getListColumns();      
    }

    	if (conns!=null) {
    		if (conns.size()>0) {
    			for (int i=0;i<conns.size();i++) {
    				IConnection connTemp = conns.get(i);
    				if (connTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {

    stringBuffer.append(TEXT_124);
    stringBuffer.append(connTemp.getName() );
    stringBuffer.append(TEXT_125);
    
    				}
    			}
    		}
    	}
    	
		String firstConnName = "";
		if (conns!=null) {
			if (conns.size()>0) {
				IConnection conn = conns.get(0);
				firstConnName = conn.getName();
				List<IMetadataColumn> listColumns = metadata.getListColumns();
				int size = listColumns.size();
				if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
//
//end
    stringBuffer.append(TEXT_126);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_127);
    stringBuffer.append(listColumns.size());
    stringBuffer.append(TEXT_128);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_132);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(size);
    stringBuffer.append(TEXT_134);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_135);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_137);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_138);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(bReadRealValue);
    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_141);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_143);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_145);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_150);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_151);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_153);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_156);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_157);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_159);
    
//start
//
					for (int i=0; i<size; i++) {
						IMetadataColumn column = listColumns.get(i);
						String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
						JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
						String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
//
//end
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_161);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_162);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_164);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_165);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_166);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_168);
    
//start
//

						if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {
//
//end
    stringBuffer.append(TEXT_169);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_170);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_171);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_172);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_173);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_174);
    		
//start
			} else if(javaType == JavaTypesManager.DATE) {
//
//end
    stringBuffer.append(TEXT_175);
    stringBuffer.append( i);
    stringBuffer.append(TEXT_176);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_178);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_179);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_180);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_182);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_183);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_185);
     // for bug TDI-19404 
    stringBuffer.append(TEXT_186);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_187);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_188);
    
//start			
			}else if(advancedSeparator && JavaTypesManager.isNumberType(javaType)) { 

    stringBuffer.append(TEXT_189);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_190);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_191);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_192);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_193);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_194);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_195);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_196);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_197);
    
					} else if(javaType == JavaTypesManager.BYTE_ARRAY) { 
	
    stringBuffer.append(TEXT_198);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_201);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_202);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_203);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_204);
    
			} else {
//
//end
    stringBuffer.append(TEXT_205);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_207);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_210);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_211);
    
//start
//
						}
//
//end
    stringBuffer.append(TEXT_212);
    
//start
//
						String defaultValue = JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate, column.getDefault());
						if(defaultValue == null) {
//
//end
    stringBuffer.append(TEXT_213);
    stringBuffer.append( column.getLabel() );
    stringBuffer.append(TEXT_214);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_215);
    
//start
//
						} else {
//
//end
    stringBuffer.append(TEXT_216);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_217);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_218);
    stringBuffer.append(defaultValue);
    stringBuffer.append(TEXT_219);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_220);
    
//start
//
						}
//
//end
    stringBuffer.append(TEXT_221);
    
//start
//
					}
    stringBuffer.append(TEXT_222);
    if(rejectConnName.equals(firstConnName)) {
    stringBuffer.append(TEXT_223);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_224);
    }
    stringBuffer.append(TEXT_225);
    
	if(stopOnEmptyRow){

    stringBuffer.append(TEXT_226);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_227);
    stringBuffer.append(size );
    stringBuffer.append(TEXT_228);
    
	}

    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_230);
    
        if (dieOnError) {
            
    stringBuffer.append(TEXT_231);
    
        } else {
            if(!("").equals(rejectConnName)&&!rejectConnName.equals(firstConnName)&&rejectColumnList != null && rejectColumnList.size() > 0) {

                
    stringBuffer.append(TEXT_232);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_233);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_234);
    
                for(IMetadataColumn column : metadata.getListColumns()) {
                    
    stringBuffer.append(TEXT_235);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_236);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_237);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_238);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_239);
    
                }
                
    stringBuffer.append(TEXT_240);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_241);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_242);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_243);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_244);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_246);
    
            } else if(("").equals(rejectConnName)){
                
    stringBuffer.append(TEXT_247);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_249);
    
            } else if(rejectConnName.equals(firstConnName)){
    stringBuffer.append(TEXT_250);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_251);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_252);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_253);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_254);
    }
        } 
        
    stringBuffer.append(TEXT_255);
    
				}
			}
		if (conns.size()>0) {	
			boolean isFirstEnter = true;
			for (int i=0;i<conns.size();i++) {
				IConnection conn = conns.get(i);
				if ((conn.getName().compareTo(firstConnName)!=0)&&(conn.getName().compareTo(rejectConnName)!=0)&&(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA))) {

    stringBuffer.append(TEXT_256);
     if(isFirstEnter) {
    stringBuffer.append(TEXT_257);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_258);
     isFirstEnter = false; } 
    stringBuffer.append(TEXT_259);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_260);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_261);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_262);
    
			    	 for (IMetadataColumn column: metadata.getListColumns()) {

    stringBuffer.append(TEXT_263);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_264);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_265);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_266);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_267);
    
				 	}
				}
			}

    stringBuffer.append(TEXT_268);
     if(!isFirstEnter) {
    stringBuffer.append(TEXT_269);
     } 
    stringBuffer.append(TEXT_270);
    
		}
		}

    	
		}//version judgement /***excel 2007 xlsx*****/
		else{

    stringBuffer.append(TEXT_271);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_272);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_273);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_274);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_275);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_276);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_277);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_278);
    stringBuffer.append(fileName);
    stringBuffer.append(TEXT_279);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_281);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_282);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_283);
    stringBuffer.append(fileName);
    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_285);
    
		if(isAllSheets){

    stringBuffer.append(TEXT_286);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_287);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_289);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_290);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_291);
    
		}else{

    stringBuffer.append(TEXT_292);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_293);
    
			for(Map<String, String> tmp:sheetNameList){

    stringBuffer.append(TEXT_294);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_295);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_296);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_297);
    stringBuffer.append(tmp.get("SHEETNAME"));
    stringBuffer.append(TEXT_298);
    stringBuffer.append((tmp.get("USE_REGEX")!=null&&!"".equals(tmp.get("USE_REGEX")))?"true".equals(tmp.get("USE_REGEX")):false);
    stringBuffer.append(TEXT_299);
    
			}
		}

    stringBuffer.append(TEXT_300);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_301);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_302);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_303);
    if(("").equals(header.trim())){
    stringBuffer.append(TEXT_304);
    }else{
    stringBuffer.append( header );
    }
    stringBuffer.append(TEXT_305);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_306);
    if(("").equals(footer.trim())){
    stringBuffer.append(TEXT_307);
    }else{
    stringBuffer.append(footer);
    }
    stringBuffer.append(TEXT_308);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_309);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_310);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_311);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_312);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_313);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_314);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_315);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_316);
    if(("").equals(limit.trim())){
    stringBuffer.append(TEXT_317);
    }else{
    stringBuffer.append(limit);
    }
    stringBuffer.append(TEXT_318);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_319);
    if(("").equals(firstColumn.trim())){
    stringBuffer.append(TEXT_320);
    }else{
    stringBuffer.append(firstColumn);
    stringBuffer.append(TEXT_321);
    }
    stringBuffer.append(TEXT_322);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_323);
    if(lastColumn!=null && !("").equals(lastColumn.trim())){
    stringBuffer.append(TEXT_324);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_325);
    stringBuffer.append(lastColumn);
    stringBuffer.append(TEXT_326);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_327);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_328);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_329);
    }
    stringBuffer.append(TEXT_330);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_331);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_332);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_333);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_334);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_335);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_336);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_337);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_338);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_339);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_340);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_341);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_342);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_343);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_344);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_345);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_346);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_347);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_348);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_349);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_350);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_351);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_352);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_353);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_354);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_355);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_356);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_357);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_358);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_359);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_360);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_361);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_362);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_363);
    
	if(!affect){

    stringBuffer.append(TEXT_364);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_365);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_366);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_367);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_368);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_369);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_370);
    
	}else{

    stringBuffer.append(TEXT_371);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_372);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_373);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_374);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_375);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_376);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_377);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_378);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_379);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_380);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_381);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_382);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_383);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_384);
    
    }

    stringBuffer.append(TEXT_385);
    
		List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
		String rejectConnName = "";
		List<? extends IConnection> rejectConns = node.getOutgoingConnections("REJECT");
		if(rejectConns != null && rejectConns.size() > 0) {
			IConnection rejectConn = rejectConns.get(0);
			rejectConnName = rejectConn.getName();
		}
		List<IMetadataColumn> rejectColumnList = null;
		IMetadataTable metadataTable = node.getMetadataFromConnector("REJECT");
		if(metadataTable != null) {
			 rejectColumnList = metadataTable.getListColumns();      
		}
		if (conns!=null) {
			if (conns.size()>0) {
		    	for (int i=0;i<conns.size();i++) {
		    		IConnection connTemp = conns.get(i);
		    		if (connTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {

    stringBuffer.append(TEXT_386);
    stringBuffer.append(connTemp.getName() );
    stringBuffer.append(TEXT_387);
    
		    		}
		    	}
		    }
		}
		List<Map<String, String>> dateSelect = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__DATESELECT__");
		boolean converDatetoString = ("true").equals(ElementParameterParser.getValue(node, "__CONVERTDATETOSTRING__"));
		String firstConnName = "";
		if (conns!=null) {//3	 
			if (conns.size()>0) {//4
				IConnection conn = conns.get(0);
				firstConnName = conn.getName();
				List<IMetadataColumn> listColumns = metadata.getListColumns();
				int size = listColumns.size();
				if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//5

    stringBuffer.append(TEXT_388);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_389);
    stringBuffer.append(listColumns.size());
    stringBuffer.append(TEXT_390);
    
			if(converDatetoString){

    stringBuffer.append(TEXT_391);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_392);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_393);
    	
				for(IMetadataColumn column:listColumns){
					for(Map<String, String> line:dateSelect){// search in the date table
						String columnName = line.get("SCHEMA_COLUMN");				
						if(column.getLabel().equals(columnName)){

    stringBuffer.append(TEXT_394);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_395);
    stringBuffer.append(line.get("CONVERTDATE"));
    stringBuffer.append(TEXT_396);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_397);
    stringBuffer.append(line.get("PATTERN"));
    stringBuffer.append(TEXT_398);
    
						}
					}
				}	
			}

    stringBuffer.append(TEXT_399);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_400);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_401);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_402);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_403);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_404);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_405);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_406);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_407);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_408);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_409);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_410);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_411);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_412);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_413);
    stringBuffer.append(size);
    stringBuffer.append(TEXT_414);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_415);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_416);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_417);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_418);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_419);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_420);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_421);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_422);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_423);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_424);
    
								if(converDatetoString){

    stringBuffer.append(TEXT_425);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_426);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_427);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_428);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_429);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_430);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_431);
    
								}else{

    stringBuffer.append(TEXT_432);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_433);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_434);
    
								}

    stringBuffer.append(TEXT_435);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_436);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_437);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_438);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_439);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_440);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_441);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_442);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_443);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_444);
    
										if(converDatetoString){

    stringBuffer.append(TEXT_445);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_446);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_447);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_448);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_449);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_450);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_451);
    
										}else{

    stringBuffer.append(TEXT_452);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_453);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_454);
    
										}

    stringBuffer.append(TEXT_455);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_456);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_457);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_458);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_459);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_460);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_461);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_462);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_463);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_464);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_465);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_466);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_467);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_468);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_469);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_470);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_471);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_472);
    
					for (int i=0; i<size; i++) {//5
						IMetadataColumn column = listColumns.get(i);
						String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
						JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
						String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();

    stringBuffer.append(TEXT_473);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_474);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_475);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_476);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_477);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_478);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_479);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_480);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_481);
    		
						if (javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_482);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_483);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_484);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_485);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_486);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_487);
    		
						} else if(javaType == JavaTypesManager.DATE) {						

    stringBuffer.append(TEXT_488);
    stringBuffer.append( i);
    stringBuffer.append(TEXT_489);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_490);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_491);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_492);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_493);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_494);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_495);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_496);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_497);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_498);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_499);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_500);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_501);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_502);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_503);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_504);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_505);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_506);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_507);
     // for bug TDI-19404 
    stringBuffer.append(TEXT_508);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_509);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_510);
    		
						}else if(JavaTypesManager.isNumberType(javaType)) { 
							if(advancedSeparator) {

    stringBuffer.append(TEXT_511);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_512);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_513);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_514);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_515);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_516);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_517);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_518);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_519);
    
							} else {

    stringBuffer.append(TEXT_520);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_521);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_522);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_523);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_524);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_525);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_526);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_527);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_528);
    
							}
						} else if(javaType == JavaTypesManager.BYTE_ARRAY) { 

    stringBuffer.append(TEXT_529);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_530);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_531);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_532);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_533);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_534);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_535);
    
						} else {

    stringBuffer.append(TEXT_536);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_537);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_538);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_539);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_540);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_541);
    stringBuffer.append((isTrimAll || (!trimSelects.isEmpty() && ("true").equals(trimSelects.get(i).get("TRIM"))))?".trim()":"" );
    stringBuffer.append(TEXT_542);
    
						}

    stringBuffer.append(TEXT_543);
    
						String defaultValue = JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate, column.getDefault());
						if(defaultValue == null) {

    stringBuffer.append(TEXT_544);
    stringBuffer.append( column.getLabel() );
    stringBuffer.append(TEXT_545);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_546);
    
						} else {

    stringBuffer.append(TEXT_547);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_548);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_549);
    stringBuffer.append(defaultValue);
    stringBuffer.append(TEXT_550);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_551);
    
						}

    stringBuffer.append(TEXT_552);
    
					}

    if(rejectConnName.equals(firstConnName)) {
    stringBuffer.append(TEXT_553);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_554);
    }
    stringBuffer.append(TEXT_555);
    
	if(stopOnEmptyRow){

    stringBuffer.append(TEXT_556);
    stringBuffer.append( cid );
    stringBuffer.append(TEXT_557);
    stringBuffer.append(size );
    stringBuffer.append(TEXT_558);
    
	}

    stringBuffer.append(TEXT_559);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_560);
    
		        if (dieOnError) {

    stringBuffer.append(TEXT_561);
    
		        }
		        else{
					if(!("").equals(rejectConnName)&&!rejectConnName.equals(firstConnName)&&rejectColumnList != null && rejectColumnList.size() > 0) {//15

    stringBuffer.append(TEXT_562);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_563);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_564);
    
						for(IMetadataColumn column : metadata.getListColumns()) {//16

    stringBuffer.append(TEXT_565);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_566);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_567);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_568);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_569);
    
					    }//16

    stringBuffer.append(TEXT_570);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_571);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_572);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_573);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_574);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_575);
    
					} else if(("").equals(rejectConnName)){

    stringBuffer.append(TEXT_576);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_577);
    
					} else if(rejectConnName.equals(firstConnName)){

    stringBuffer.append(TEXT_578);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_579);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_580);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_581);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_582);
    
					}//15
				}

    stringBuffer.append(TEXT_583);
    
				}
			}
		if (conns.size()>0) {	
			boolean isFirstEnter = true;
			for (int i=0;i<conns.size();i++) {
				IConnection conn = conns.get(i);
				if ((conn.getName().compareTo(firstConnName)!=0)&&(conn.getName().compareTo(rejectConnName)!=0)&&(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA))) {

    stringBuffer.append(TEXT_584);
     if(isFirstEnter) {
    stringBuffer.append(TEXT_585);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_586);
     isFirstEnter = false; } 
    stringBuffer.append(TEXT_587);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_588);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_589);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_590);
    
			    	 for (IMetadataColumn column: metadata.getListColumns()) {

    stringBuffer.append(TEXT_591);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_592);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_593);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_594);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_595);
    
				 	}
				}
			}

    stringBuffer.append(TEXT_596);
     if(!isFirstEnter) {
    stringBuffer.append(TEXT_597);
     } 
    stringBuffer.append(TEXT_598);
    
		}
		}

    
		}//end version judgement

    stringBuffer.append(TEXT_599);
    
	}
}
//
//end
    stringBuffer.append(TEXT_600);
    stringBuffer.append(TEXT_601);
    return stringBuffer.toString();
  }
}
