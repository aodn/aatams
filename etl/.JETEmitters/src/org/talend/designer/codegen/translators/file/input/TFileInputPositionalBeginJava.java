package org.talend.designer.codegen.translators.file.input;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.utils.TalendTextUtils;
import java.util.List;
import java.util.Map;

public class TFileInputPositionalBeginJava
{
  protected static String nl;
  public static synchronized TFileInputPositionalBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileInputPositionalBeginJava result = new TFileInputPositionalBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\t\t\troutines.system.Dynamic dynamic_";
  protected final String TEXT_3 = " = (routines.system.Dynamic)globalMap.get(\"";
  protected final String TEXT_4 = "\");" + NL + "\t\t\t\tint maxColumnCount_";
  protected final String TEXT_5 = " = dynamic_";
  protected final String TEXT_6 = ".getColumnCount();" + NL + "\t\t\t\tdynamic_";
  protected final String TEXT_7 = ".clearColumnValues();";
  protected final String TEXT_8 = NL + "int nb_line_";
  protected final String TEXT_9 = " = 0;" + NL + "int footer_";
  protected final String TEXT_10 = "  = ";
  protected final String TEXT_11 = ";" + NL + "int nb_limit_";
  protected final String TEXT_12 = " = ";
  protected final String TEXT_13 = ";" + NL;
  protected final String TEXT_14 = NL + "class Arrays_";
  protected final String TEXT_15 = "{" + NL + "    public byte[] copyOfRange(byte[] original, int from, int to) {" + NL + "        int newLength = to - from;" + NL + "        if (newLength < 0)" + NL + "            throw new IllegalArgumentException(from + \" > \" + to);" + NL + "        byte[] copy = new byte[newLength];" + NL + "        System.arraycopy(original, from, copy, 0," + NL + "                         Math.min(original.length - from, newLength));" + NL + "        return copy;" + NL + "    }" + NL + "}" + NL + "Arrays_";
  protected final String TEXT_16 = " arrays_";
  protected final String TEXT_17 = " = new Arrays_";
  protected final String TEXT_18 = "();";
  protected final String TEXT_19 = NL + NL + NL + "class PositionalUtil_";
  protected final String TEXT_20 = "{";
  protected final String TEXT_21 = NL + "                  void setValue_";
  protected final String TEXT_22 = "(";
  protected final String TEXT_23 = "Struct ";
  protected final String TEXT_24 = ",int[] begins_";
  protected final String TEXT_25 = ",int[] ends_";
  protected final String TEXT_26 = ",int rowLen_";
  protected final String TEXT_27 = ",";
  protected final String TEXT_28 = "byte[] byteArray_";
  protected final String TEXT_29 = ",Arrays_";
  protected final String TEXT_30 = " arrays_";
  protected final String TEXT_31 = ",";
  protected final String TEXT_32 = "String column_";
  protected final String TEXT_33 = ",String row_";
  protected final String TEXT_34 = ")throws Exception {             ";
  protected final String TEXT_35 = NL + "    if(begins_";
  protected final String TEXT_36 = "[";
  protected final String TEXT_37 = "] < rowLen_";
  protected final String TEXT_38 = "){";
  protected final String TEXT_39 = NL + "\t\tbyteArray_";
  protected final String TEXT_40 = "=arrays_";
  protected final String TEXT_41 = ".copyOfRange(row_";
  protected final String TEXT_42 = ".getBytes(";
  protected final String TEXT_43 = "),begins_";
  protected final String TEXT_44 = "[";
  protected final String TEXT_45 = "],rowLen_";
  protected final String TEXT_46 = ");" + NL + "    \tcolumn_";
  protected final String TEXT_47 = " = TalendString.talendTrim(new String(byteArray_";
  protected final String TEXT_48 = ",";
  protected final String TEXT_49 = "), ";
  protected final String TEXT_50 = ", ";
  protected final String TEXT_51 = ");";
  protected final String TEXT_52 = NL + "\t\tcolumn_";
  protected final String TEXT_53 = " = TalendString.talendTrim(row_";
  protected final String TEXT_54 = ".substring(begins_";
  protected final String TEXT_55 = "[";
  protected final String TEXT_56 = "]), ";
  protected final String TEXT_57 = ", ";
  protected final String TEXT_58 = ");";
  protected final String TEXT_59 = NL + "    }else{" + NL + "    \tcolumn_";
  protected final String TEXT_60 = " = \"\";" + NL + "    }";
  protected final String TEXT_61 = NL + "\tif(begins_";
  protected final String TEXT_62 = "[";
  protected final String TEXT_63 = "] < rowLen_";
  protected final String TEXT_64 = "){" + NL + "        if(ends_";
  protected final String TEXT_65 = "[";
  protected final String TEXT_66 = "] <= rowLen_";
  protected final String TEXT_67 = "){" + NL + "        \tbyteArray_";
  protected final String TEXT_68 = " = arrays_";
  protected final String TEXT_69 = ".copyOfRange(row_";
  protected final String TEXT_70 = ".getBytes(";
  protected final String TEXT_71 = "),begins_";
  protected final String TEXT_72 = "[";
  protected final String TEXT_73 = "],ends_";
  protected final String TEXT_74 = "[";
  protected final String TEXT_75 = "]);" + NL + "        }else{" + NL + "        \tbyteArray_";
  protected final String TEXT_76 = " = arrays_";
  protected final String TEXT_77 = ".copyOfRange(row_";
  protected final String TEXT_78 = ".getBytes(";
  protected final String TEXT_79 = "),begins_";
  protected final String TEXT_80 = "[";
  protected final String TEXT_81 = "],rowLen_";
  protected final String TEXT_82 = ");" + NL + "\t\t}" + NL + "\t\tcolumn_";
  protected final String TEXT_83 = " = TalendString.talendTrim(new String(byteArray_";
  protected final String TEXT_84 = ",";
  protected final String TEXT_85 = "), ";
  protected final String TEXT_86 = ", ";
  protected final String TEXT_87 = ");" + NL + "    }else{" + NL + "    \tcolumn_";
  protected final String TEXT_88 = " = \"\";" + NL + "    }";
  protected final String TEXT_89 = NL + "\tif(begins_";
  protected final String TEXT_90 = "[";
  protected final String TEXT_91 = "] < rowLen_";
  protected final String TEXT_92 = "){" + NL + "        if(ends_";
  protected final String TEXT_93 = "[";
  protected final String TEXT_94 = "] <= rowLen_";
  protected final String TEXT_95 = "){" + NL + "        \tcolumn_";
  protected final String TEXT_96 = " = TalendString.talendTrim(row_";
  protected final String TEXT_97 = ".substring(begins_";
  protected final String TEXT_98 = "[";
  protected final String TEXT_99 = "], ends_";
  protected final String TEXT_100 = "[";
  protected final String TEXT_101 = "]), ";
  protected final String TEXT_102 = ", ";
  protected final String TEXT_103 = ");" + NL + "        }else{" + NL + "        \tcolumn_";
  protected final String TEXT_104 = " = TalendString.talendTrim(row_";
  protected final String TEXT_105 = ".substring(begins_";
  protected final String TEXT_106 = "[";
  protected final String TEXT_107 = "]), ";
  protected final String TEXT_108 = ", ";
  protected final String TEXT_109 = ");" + NL + "\t\t}" + NL + "    }else{" + NL + "    \tcolumn_";
  protected final String TEXT_110 = " = \"\";" + NL + "    }";
  protected final String TEXT_111 = NL + "\tcolumn_";
  protected final String TEXT_112 = " = column_";
  protected final String TEXT_113 = ".trim();";
  protected final String TEXT_114 = NL + "\t";
  protected final String TEXT_115 = ".";
  protected final String TEXT_116 = " = column_";
  protected final String TEXT_117 = ";";
  protected final String TEXT_118 = NL + "\t\t\t\t\t\t\troutines.system.Dynamic dynamic_";
  protected final String TEXT_119 = " = (routines.system.Dynamic)globalMap.get(\"";
  protected final String TEXT_120 = "\");" + NL + "\t\t\t\t\t\t\tdynamic_";
  protected final String TEXT_121 = ".clearColumnValues();" + NL + "\t\t\t\t\t\t\tint maxColumnCount_";
  protected final String TEXT_122 = " = dynamic_";
  protected final String TEXT_123 = ".getColumnCount();" + NL + "\t\t\t\t\t\t\tint substringBegin";
  protected final String TEXT_124 = " = begins_";
  protected final String TEXT_125 = "[begins_";
  protected final String TEXT_126 = ".length-1]; int substringEnd";
  protected final String TEXT_127 = " =0;" + NL + "\t\t\t\t\t\t\tfor (int i=0;i<maxColumnCount_";
  protected final String TEXT_128 = ";i++) {" + NL + "\t\t\t\t\t\t\t\troutines.system.DynamicMetadata dynamicMetadataColumn_";
  protected final String TEXT_129 = " = dynamic_";
  protected final String TEXT_130 = ".getColumnMetadata(i);" + NL + "\t\t\t\t\t\t\t\tint currentFieldLength_";
  protected final String TEXT_131 = " = dynamicMetadataColumn_";
  protected final String TEXT_132 = ".getLength();" + NL + "\t\t\t\t\t\t\t\tsubstringEnd";
  protected final String TEXT_133 = " = substringBegin";
  protected final String TEXT_134 = "+currentFieldLength_";
  protected final String TEXT_135 = ";" + NL + "\t\t\t\t\t\t\t\tif(substringEnd";
  protected final String TEXT_136 = " > rowLen_";
  protected final String TEXT_137 = "){" + NL + "\t\t\t\t\t\t\t\t\tsubstringEnd";
  protected final String TEXT_138 = " = rowLen_";
  protected final String TEXT_139 = ";" + NL + "\t\t\t\t\t\t\t\t}";
  protected final String TEXT_140 = NL + "\t\t\t\t\t\t\t\t\tbyte[] temp_byteArray_";
  protected final String TEXT_141 = " = arrays_";
  protected final String TEXT_142 = ".copyOfRange(row_";
  protected final String TEXT_143 = ".getBytes(";
  protected final String TEXT_144 = "),substringBegin";
  protected final String TEXT_145 = ", substringEnd";
  protected final String TEXT_146 = ");" + NL + "\t\t\t\t\t\t\t\t\tString currentColumnValue_";
  protected final String TEXT_147 = " = new String(temp_byteArray_";
  protected final String TEXT_148 = ",";
  protected final String TEXT_149 = ");\t\t\t\t\t\t";
  protected final String TEXT_150 = NL + "\t\t\t\t\t\t\t\t\tString currentColumnValue_";
  protected final String TEXT_151 = " = row_";
  protected final String TEXT_152 = ".substring(substringBegin";
  protected final String TEXT_153 = ", substringEnd";
  protected final String TEXT_154 = ");";
  protected final String TEXT_155 = NL + "\t\t\t\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_156 = " = TalendString.talendTrim(currentColumnValue_";
  protected final String TEXT_157 = ", ";
  protected final String TEXT_158 = ", ";
  protected final String TEXT_159 = ");";
  protected final String TEXT_160 = NL + "\t\t\t\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_161 = " = currentColumnValue_";
  protected final String TEXT_162 = ".trim();";
  protected final String TEXT_163 = NL + "\t\t\t\t\t\t\t\t\tString type_";
  protected final String TEXT_164 = " = dynamicMetadataColumn_";
  protected final String TEXT_165 = ".getType();" + NL + "\t\t\t\t\t\t\t\t\tif(\"id_Short\".equals(type_";
  protected final String TEXT_166 = ") || \"id_Integer\".equals(type_";
  protected final String TEXT_167 = ") || \"id_Double\".equals(type_";
  protected final String TEXT_168 = ") || \"id_Float\".equals(type_";
  protected final String TEXT_169 = ") || \"id_Long\".equals(type_";
  protected final String TEXT_170 = ") || \"id_BigDecimal\".equals(type_";
  protected final String TEXT_171 = ")){" + NL + "\t\t\t\t\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_172 = " = ParserUtils.parseTo_Number(currentColumnValue_";
  protected final String TEXT_173 = ", ";
  protected final String TEXT_174 = ", ";
  protected final String TEXT_175 = ");" + NL + "\t\t\t\t\t\t\t\t\t}";
  protected final String TEXT_176 = NL + "\t\t\t\t\t\t\t\tdynamic_";
  protected final String TEXT_177 = ".addColumnValue(currentColumnValue_";
  protected final String TEXT_178 = ");" + NL + "\t\t\t\t\t\t\t\tsubstringBegin";
  protected final String TEXT_179 = " = substringEnd";
  protected final String TEXT_180 = "; " + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_181 = ".";
  protected final String TEXT_182 = " = dynamic_";
  protected final String TEXT_183 = ";";
  protected final String TEXT_184 = NL + "\tif(column_";
  protected final String TEXT_185 = ".length() > 0) {";
  protected final String TEXT_186 = NL + "\t\t";
  protected final String TEXT_187 = ".";
  protected final String TEXT_188 = " = ParserUtils.parseTo_Date(column_";
  protected final String TEXT_189 = ", ";
  protected final String TEXT_190 = ",false);";
  protected final String TEXT_191 = NL + "\t\t";
  protected final String TEXT_192 = ".";
  protected final String TEXT_193 = " = ParserUtils.parseTo_Date(column_";
  protected final String TEXT_194 = ", ";
  protected final String TEXT_195 = ");";
  protected final String TEXT_196 = NL + "\t\t";
  protected final String TEXT_197 = ".";
  protected final String TEXT_198 = " = ParserUtils.parseTo_";
  protected final String TEXT_199 = "(ParserUtils.parseTo_Number(column_";
  protected final String TEXT_200 = ", ";
  protected final String TEXT_201 = ", ";
  protected final String TEXT_202 = "));";
  protected final String TEXT_203 = NL + "\t\t";
  protected final String TEXT_204 = ".";
  protected final String TEXT_205 = " = column_";
  protected final String TEXT_206 = ".getBytes(";
  protected final String TEXT_207 = ");";
  protected final String TEXT_208 = NL + "\t\t";
  protected final String TEXT_209 = ".";
  protected final String TEXT_210 = " = ParserUtils.parseTo_";
  protected final String TEXT_211 = "(column_";
  protected final String TEXT_212 = ");";
  protected final String TEXT_213 = NL + "    }else{" + NL + "    \t";
  protected final String TEXT_214 = ".";
  protected final String TEXT_215 = " = ";
  protected final String TEXT_216 = ";" + NL + "    }";
  protected final String TEXT_217 = NL + "                    }";
  protected final String TEXT_218 = NL + "                }";
  protected final String TEXT_219 = NL + "                     void setValue_";
  protected final String TEXT_220 = "(";
  protected final String TEXT_221 = "Struct ";
  protected final String TEXT_222 = ",String[] columnValue";
  protected final String TEXT_223 = ")throws Exception{";
  protected final String TEXT_224 = NL + "\t                    ";
  protected final String TEXT_225 = ".";
  protected final String TEXT_226 = " = columnValue";
  protected final String TEXT_227 = "[";
  protected final String TEXT_228 = "];";
  protected final String TEXT_229 = NL + "\t                    if(columnValue";
  protected final String TEXT_230 = "[";
  protected final String TEXT_231 = "].length() > 0) {";
  protected final String TEXT_232 = "\t" + NL + "\t\t                            ";
  protected final String TEXT_233 = ".";
  protected final String TEXT_234 = " = ParserUtils.parseTo_Date(columnValue";
  protected final String TEXT_235 = "[";
  protected final String TEXT_236 = "], ";
  protected final String TEXT_237 = ",false);";
  protected final String TEXT_238 = NL + "\t\t                            ";
  protected final String TEXT_239 = ".";
  protected final String TEXT_240 = " = ParserUtils.parseTo_Date(columnValue";
  protected final String TEXT_241 = "[";
  protected final String TEXT_242 = "], ";
  protected final String TEXT_243 = ");\t";
  protected final String TEXT_244 = NL + "\t\t                        ";
  protected final String TEXT_245 = ".";
  protected final String TEXT_246 = " = ParserUtils.parseTo_";
  protected final String TEXT_247 = "(ParserUtils.parseTo_Number(columnValue";
  protected final String TEXT_248 = "[";
  protected final String TEXT_249 = "], ";
  protected final String TEXT_250 = ", ";
  protected final String TEXT_251 = "));";
  protected final String TEXT_252 = "\t" + NL + "\t\t                        ";
  protected final String TEXT_253 = ".";
  protected final String TEXT_254 = " = columnValue";
  protected final String TEXT_255 = "[";
  protected final String TEXT_256 = "].getBytes(";
  protected final String TEXT_257 = ");";
  protected final String TEXT_258 = "\t" + NL + "\t\t                        ";
  protected final String TEXT_259 = ".";
  protected final String TEXT_260 = " = ParserUtils.parseTo_";
  protected final String TEXT_261 = "(columnValue";
  protected final String TEXT_262 = "[";
  protected final String TEXT_263 = "].trim());";
  protected final String TEXT_264 = NL + "                        }else{" + NL + "        \t                ";
  protected final String TEXT_265 = ".";
  protected final String TEXT_266 = " = ";
  protected final String TEXT_267 = ";" + NL + "                        }";
  protected final String TEXT_268 = NL;
  protected final String TEXT_269 = NL + "                     }";
  protected final String TEXT_270 = NL;
  protected final String TEXT_271 = NL + "                     }";
  protected final String TEXT_272 = NL;
  protected final String TEXT_273 = NL + "}" + NL + "" + NL + "PositionalUtil_";
  protected final String TEXT_274 = " positionalUtil_";
  protected final String TEXT_275 = "=new PositionalUtil_";
  protected final String TEXT_276 = "();" + NL;
  protected final String TEXT_277 = NL + NL + "int[] sizes_";
  protected final String TEXT_278 = " = new int[";
  protected final String TEXT_279 = "];" + NL + "int[] begins_";
  protected final String TEXT_280 = " = new int[";
  protected final String TEXT_281 = "];" + NL + "int[] ends_";
  protected final String TEXT_282 = " = new int[";
  protected final String TEXT_283 = "];";
  protected final String TEXT_284 = NL + "sizes_";
  protected final String TEXT_285 = "[";
  protected final String TEXT_286 = "] = ";
  protected final String TEXT_287 = ";";
  protected final String TEXT_288 = NL + "sizes_";
  protected final String TEXT_289 = "[";
  protected final String TEXT_290 = "] = ";
  protected final String TEXT_291 = ";\t\t\t\t";
  protected final String TEXT_292 = NL + "begins_";
  protected final String TEXT_293 = "[";
  protected final String TEXT_294 = "] = 0;" + NL + "ends_";
  protected final String TEXT_295 = "[";
  protected final String TEXT_296 = "] = sizes_";
  protected final String TEXT_297 = "[";
  protected final String TEXT_298 = "];";
  protected final String TEXT_299 = NL + "begins_";
  protected final String TEXT_300 = "[";
  protected final String TEXT_301 = "] = begins_";
  protected final String TEXT_302 = "[";
  protected final String TEXT_303 = "] + sizes_";
  protected final String TEXT_304 = "[";
  protected final String TEXT_305 = "];" + NL + "ends_";
  protected final String TEXT_306 = "[";
  protected final String TEXT_307 = "] = -1;";
  protected final String TEXT_308 = NL + "begins_";
  protected final String TEXT_309 = "[";
  protected final String TEXT_310 = "] = begins_";
  protected final String TEXT_311 = "[";
  protected final String TEXT_312 = "] + sizes_";
  protected final String TEXT_313 = "[";
  protected final String TEXT_314 = "];" + NL + "ends_";
  protected final String TEXT_315 = "[";
  protected final String TEXT_316 = "] = ends_";
  protected final String TEXT_317 = "[";
  protected final String TEXT_318 = "] + sizes_";
  protected final String TEXT_319 = "[";
  protected final String TEXT_320 = "];";
  protected final String TEXT_321 = NL + "Object filename_";
  protected final String TEXT_322 = " = ";
  protected final String TEXT_323 = ";" + NL + "java.io.BufferedReader in_";
  protected final String TEXT_324 = " = null;" + NL + "org.talend.fileprocess.delimited.RowParser reader_";
  protected final String TEXT_325 = " = null; ";
  protected final String TEXT_326 = NL + "java.util.zip.ZipInputStream zis_";
  protected final String TEXT_327 = " = null;" + NL + "try {" + NL + "\tif(filename_";
  protected final String TEXT_328 = " instanceof java.io.InputStream){" + NL + "\t\tzis_";
  protected final String TEXT_329 = " = new java.util.zip.ZipInputStream(new java.io.BufferedInputStream((java.io.InputStream)filename_";
  protected final String TEXT_330 = "));" + NL + "\t}else{" + NL + "\t\tzis_";
  protected final String TEXT_331 = " = new java.util.zip.ZipInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(String.valueOf(filename_";
  protected final String TEXT_332 = "))));" + NL + "\t}" + NL + "} catch(Exception e) {" + NL + "\t";
  protected final String TEXT_333 = NL + "\tthrow e;" + NL + "\t";
  protected final String TEXT_334 = NL + "\tSystem.err.println(e.getMessage());" + NL + "\t";
  protected final String TEXT_335 = NL + "}" + NL + "java.util.zip.ZipEntry entry_";
  protected final String TEXT_336 = " = null;" + NL + "while (true) {" + NL + "\ttry {" + NL + "\t\tentry_";
  protected final String TEXT_337 = " = zis_";
  protected final String TEXT_338 = ".getNextEntry();" + NL + "\t} catch(Exception e) {" + NL + "\t\t";
  protected final String TEXT_339 = NL + "\t\tthrow e;" + NL + "\t\t";
  protected final String TEXT_340 = NL + "\t\tSystem.err.println(e.getMessage());" + NL + "\t\tbreak;" + NL + "\t\t";
  protected final String TEXT_341 = NL + "\t}" + NL + "\tif(entry_";
  protected final String TEXT_342 = " == null) {" + NL + "\t\tbreak;" + NL + "\t}" + NL + "\tif(entry_";
  protected final String TEXT_343 = ".isDirectory()){ //directory" + NL + "\t\tcontinue;" + NL + "\t}" + NL + "\tString row_";
  protected final String TEXT_344 = " = null;" + NL + "\tint rowLen_";
  protected final String TEXT_345 = " = 0;" + NL + "\t";
  protected final String TEXT_346 = NL + "\tbyte[] byteArray_";
  protected final String TEXT_347 = " = new byte[0];" + NL + "\t";
  protected final String TEXT_348 = NL + "\tString column_";
  protected final String TEXT_349 = " = null;" + NL + "\t";
  protected final String TEXT_350 = NL + "\tbyte[][] byteArray_";
  protected final String TEXT_351 = " = new byte[";
  protected final String TEXT_352 = "][];" + NL + "\t";
  protected final String TEXT_353 = NL + "\tString[] columnValue";
  protected final String TEXT_354 = "=new String[";
  protected final String TEXT_355 = "];" + NL + "\t";
  protected final String TEXT_356 = NL + "\ttry {//TD110 begin" + NL + "\t\tin_";
  protected final String TEXT_357 = " = new java.io.BufferedReader(new java.io.InputStreamReader(zis_";
  protected final String TEXT_358 = ", ";
  protected final String TEXT_359 = "));";
  protected final String TEXT_360 = NL + "String row_";
  protected final String TEXT_361 = " = null;" + NL + "int rowLen_";
  protected final String TEXT_362 = " = 0;";
  protected final String TEXT_363 = NL + "byte[] byteArray_";
  protected final String TEXT_364 = " = new byte[0];";
  protected final String TEXT_365 = NL + "String column_";
  protected final String TEXT_366 = " = null;";
  protected final String TEXT_367 = NL + "byte[][] byteArray_";
  protected final String TEXT_368 = " = new byte[";
  protected final String TEXT_369 = "][];";
  protected final String TEXT_370 = NL + "String[] columnValue";
  protected final String TEXT_371 = "=new String[";
  protected final String TEXT_372 = "];";
  protected final String TEXT_373 = " " + NL + "try {//TD110 begin" + NL + "\tif(filename_";
  protected final String TEXT_374 = " instanceof java.io.InputStream){" + NL + "\t\tin_";
  protected final String TEXT_375 = " = " + NL + "\t\t\t\tnew java.io.BufferedReader(new java.io.InputStreamReader((java.io.InputStream)filename_";
  protected final String TEXT_376 = ", ";
  protected final String TEXT_377 = "));" + NL + "\t}else{" + NL + "\t\tin_";
  protected final String TEXT_378 = " = " + NL + "\t\t\tnew java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(String.valueOf(filename_";
  protected final String TEXT_379 = ")), ";
  protected final String TEXT_380 = "));" + NL + "\t}" + NL;
  protected final String TEXT_381 = NL + "int rowLength_";
  protected final String TEXT_382 = " = 0;";
  protected final String TEXT_383 = NL + "rowLength_";
  protected final String TEXT_384 = " += sizes_";
  protected final String TEXT_385 = "[";
  protected final String TEXT_386 = "];";
  protected final String TEXT_387 = NL + "rowLength_";
  protected final String TEXT_388 = " += ";
  protected final String TEXT_389 = ";";
  protected final String TEXT_390 = NL + "reader_";
  protected final String TEXT_391 = " = new org.talend.fileprocess.delimited.RowParser(in_";
  protected final String TEXT_392 = ", rowLength_";
  protected final String TEXT_393 = ");";
  protected final String TEXT_394 = NL + "reader_";
  protected final String TEXT_395 = " = new org.talend.fileprocess.delimited.RowParser(in_";
  protected final String TEXT_396 = ", ";
  protected final String TEXT_397 = ", ";
  protected final String TEXT_398 = ");";
  protected final String TEXT_399 = NL + "reader_";
  protected final String TEXT_400 = ".setSafetySwitch(";
  protected final String TEXT_401 = ");" + NL + "reader_";
  protected final String TEXT_402 = ".skipHeaders(";
  protected final String TEXT_403 = ");" + NL + "if(footer_";
  protected final String TEXT_404 = " > 0){" + NL + "\tint available_";
  protected final String TEXT_405 = " = (int)reader_";
  protected final String TEXT_406 = ".getAvailableRowCount(footer_";
  protected final String TEXT_407 = ");" + NL + "\treader_";
  protected final String TEXT_408 = ".close();" + NL + "\tif(filename_";
  protected final String TEXT_409 = " instanceof java.io.InputStream){" + NL + "\t\tin_";
  protected final String TEXT_410 = " = new java.io.BufferedReader(new java.io.InputStreamReader((java.io.InputStream)filename_";
  protected final String TEXT_411 = ", ";
  protected final String TEXT_412 = "));" + NL + "\t}else{" + NL + "\t\tin_";
  protected final String TEXT_413 = " = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(String.valueOf(filename_";
  protected final String TEXT_414 = ")), ";
  protected final String TEXT_415 = "));" + NL + "\t}";
  protected final String TEXT_416 = NL + "\treader_";
  protected final String TEXT_417 = " = new org.talend.fileprocess.delimited.RowParser(in_";
  protected final String TEXT_418 = ", rowLength_";
  protected final String TEXT_419 = ");";
  protected final String TEXT_420 = NL + "\treader_";
  protected final String TEXT_421 = " = new org.talend.fileprocess.delimited.RowParser(in_";
  protected final String TEXT_422 = ", ";
  protected final String TEXT_423 = ", ";
  protected final String TEXT_424 = ");";
  protected final String TEXT_425 = "\t" + NL + "\treader_";
  protected final String TEXT_426 = ".skipHeaders(";
  protected final String TEXT_427 = ");" + NL + "\t" + NL + "\tif ( nb_limit_";
  protected final String TEXT_428 = " >= 0 ){" + NL + "\t\tnb_limit_";
  protected final String TEXT_429 = " = ( nb_limit_";
  protected final String TEXT_430 = " > available_";
  protected final String TEXT_431 = ") ? available_";
  protected final String TEXT_432 = " : nb_limit_";
  protected final String TEXT_433 = ";" + NL + "\t}else{" + NL + "\t\tnb_limit_";
  protected final String TEXT_434 = " = available_";
  protected final String TEXT_435 = ";" + NL + "\t}" + NL + "}" + NL + "" + NL + "} catch(Exception e) {//TD110 end" + NL + "\t";
  protected final String TEXT_436 = NL + "\tthrow e;" + NL + "\t";
  protected final String TEXT_437 = NL + "\tSystem.err.println(e.getMessage());" + NL + "\t";
  protected final String TEXT_438 = NL + "}" + NL;
  protected final String TEXT_439 = NL + "\t\tString arrFieldSeparator";
  protected final String TEXT_440 = "[] = \"";
  protected final String TEXT_441 = "\".split(\",\");";
  protected final String TEXT_442 = "\t\t" + NL + "\t\tString arrFieldSeparator";
  protected final String TEXT_443 = "[] = ";
  protected final String TEXT_444 = ".split(\",\");";
  protected final String TEXT_445 = " " + NL + "" + NL + "Integer star_";
  protected final String TEXT_446 = " = Integer.valueOf(-1);\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "Integer[] tmpAry_";
  protected final String TEXT_447 = " = new Integer[arrFieldSeparator";
  protected final String TEXT_448 = ".length];\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "for (int i = 0; i < arrFieldSeparator";
  protected final String TEXT_449 = ".length; i++ ){\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "\tif ((\"*\").equals(arrFieldSeparator";
  protected final String TEXT_450 = "[i])) {\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "\t\ttmpAry_";
  protected final String TEXT_451 = "[i] = star_";
  protected final String TEXT_452 = ";\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "\t}else{\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "\t\ttmpAry_";
  protected final String TEXT_453 = "[i] = Integer.parseInt(arrFieldSeparator";
  protected final String TEXT_454 = "[i]);\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "\t}\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + NL + "}\t";
  protected final String TEXT_455 = NL + "while (nb_limit_";
  protected final String TEXT_456 = " != 0 && reader_";
  protected final String TEXT_457 = "!=null && reader_";
  protected final String TEXT_458 = ".readRecord()) {" + NL + "\trow_";
  protected final String TEXT_459 = " = reader_";
  protected final String TEXT_460 = ".getRowRecord();";
  protected final String TEXT_461 = NL + "\trowLen_";
  protected final String TEXT_462 = " = row_";
  protected final String TEXT_463 = ".getBytes(";
  protected final String TEXT_464 = ").length;";
  protected final String TEXT_465 = NL + "\trowLen_";
  protected final String TEXT_466 = " = row_";
  protected final String TEXT_467 = ".length();";
  protected final String TEXT_468 = NL + "    \t\t";
  protected final String TEXT_469 = " = null;\t\t\t";
  protected final String TEXT_470 = NL + "\t\t\t" + NL + "\t\t\tboolean whetherReject_";
  protected final String TEXT_471 = " = false;" + NL + "\t\t\t";
  protected final String TEXT_472 = " = new ";
  protected final String TEXT_473 = "Struct();" + NL + "\t\t\ttry {" + NL + "\t\t\t" + NL + "\t\t\t";
  protected final String TEXT_474 = NL + "                  positionalUtil_";
  protected final String TEXT_475 = ".setValue_";
  protected final String TEXT_476 = "(";
  protected final String TEXT_477 = ",begins_";
  protected final String TEXT_478 = ",ends_";
  protected final String TEXT_479 = ",rowLen_";
  protected final String TEXT_480 = ",";
  protected final String TEXT_481 = "byteArray_";
  protected final String TEXT_482 = ",arrays_";
  protected final String TEXT_483 = ",";
  protected final String TEXT_484 = "column_";
  protected final String TEXT_485 = ",row_";
  protected final String TEXT_486 = ");          ";
  protected final String TEXT_487 = NL + "\tint substringBegin";
  protected final String TEXT_488 = "=0,substringEnd";
  protected final String TEXT_489 = "=0;" + NL + "\tint[]begin_end_";
  protected final String TEXT_490 = "=new int[2];" + NL + "\t";
  protected final String TEXT_491 = "\t" + NL + "\t\t\t\tcolumnValue";
  protected final String TEXT_492 = "[";
  protected final String TEXT_493 = "]=\"\";";
  protected final String TEXT_494 = " " + NL + "\t\t\t\tdynamic_";
  protected final String TEXT_495 = ".clearColumnValues();" + NL + "\t\t\t\tfor (int i=0;i<maxColumnCount_";
  protected final String TEXT_496 = ";i++) {" + NL + "\t\t\t\t\troutines.system.DynamicMetadata dynamicMetadataColumn_";
  protected final String TEXT_497 = " = dynamic_";
  protected final String TEXT_498 = ".getColumnMetadata(i);" + NL + "\t\t\t\t\tint currentFieldLength_";
  protected final String TEXT_499 = " = dynamicMetadataColumn_";
  protected final String TEXT_500 = ".getLength();" + NL + "\t\t\t\t\tsubstringEnd";
  protected final String TEXT_501 = " = substringBegin";
  protected final String TEXT_502 = "+currentFieldLength_";
  protected final String TEXT_503 = ";" + NL + "\t\t\t\t\tif(substringEnd";
  protected final String TEXT_504 = " > rowLen_";
  protected final String TEXT_505 = "){" + NL + "\t\t\t\t\t\tsubstringEnd";
  protected final String TEXT_506 = " = rowLen_";
  protected final String TEXT_507 = ";" + NL + "\t\t\t\t\t}";
  protected final String TEXT_508 = NL + "\t\t\t\t\t\tbyte[] temp_byteArray_";
  protected final String TEXT_509 = "= arrays_";
  protected final String TEXT_510 = ".copyOfRange(row_";
  protected final String TEXT_511 = ".getBytes(";
  protected final String TEXT_512 = "),substringBegin";
  protected final String TEXT_513 = ", substringEnd";
  protected final String TEXT_514 = ");" + NL + "\t\t\t\t\t\tString currentColumnValue_";
  protected final String TEXT_515 = " = new String(temp_byteArray_";
  protected final String TEXT_516 = ",";
  protected final String TEXT_517 = ");\t\t\t\t\t\t";
  protected final String TEXT_518 = NL + "\t\t\t\t\t\tString currentColumnValue_";
  protected final String TEXT_519 = " = row_";
  protected final String TEXT_520 = ".substring(substringBegin";
  protected final String TEXT_521 = ", substringEnd";
  protected final String TEXT_522 = ");";
  protected final String TEXT_523 = "\t\t\t" + NL + "\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_524 = " = currentColumnValue_";
  protected final String TEXT_525 = ".trim();";
  protected final String TEXT_526 = " " + NL + "\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_527 = " = TalendString.talendTrim(currentColumnValue_";
  protected final String TEXT_528 = ", ";
  protected final String TEXT_529 = ", ";
  protected final String TEXT_530 = ");";
  protected final String TEXT_531 = NL + "\t\t\t\t\t\tString type_";
  protected final String TEXT_532 = " = dynamicMetadataColumn_";
  protected final String TEXT_533 = ".getType();" + NL + "\t\t\t\t\t\tif(\"id_Short\".equals(type_";
  protected final String TEXT_534 = ") || \"id_Integer\".equals(type_";
  protected final String TEXT_535 = ") || \"id_Double\".equals(type_";
  protected final String TEXT_536 = ") || \"id_Float\".equals(type_";
  protected final String TEXT_537 = ") || \"id_Long\".equals(type_";
  protected final String TEXT_538 = ") || \"id_BigDecimal\".equals(type_";
  protected final String TEXT_539 = ")){" + NL + "\t\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_540 = " = ParserUtils.parseTo_Number(currentColumnValue_";
  protected final String TEXT_541 = ", ";
  protected final String TEXT_542 = ", ";
  protected final String TEXT_543 = ");" + NL + "\t\t\t\t\t\t}";
  protected final String TEXT_544 = NL + "\t\t\t\t\tdynamic_";
  protected final String TEXT_545 = ".addColumnValue(currentColumnValue_";
  protected final String TEXT_546 = ");" + NL + "\t\t\t\t\tsubstringBegin";
  protected final String TEXT_547 = " = substringEnd";
  protected final String TEXT_548 = ";" + NL + "\t\t\t\t}";
  protected final String TEXT_549 = NL + "\t\t    if(substringBegin";
  protected final String TEXT_550 = " >= rowLen_";
  protected final String TEXT_551 = "){" + NL + "\t\t    \tcolumnValue";
  protected final String TEXT_552 = "[";
  protected final String TEXT_553 = "]= \"\";" + NL + "    \t\t}else{";
  protected final String TEXT_554 = NL + "        \t\t\t \tsubstringEnd";
  protected final String TEXT_555 = "=rowLen_";
  protected final String TEXT_556 = ";";
  protected final String TEXT_557 = NL + "        \t\t\t\tsubstringEnd";
  protected final String TEXT_558 = " = substringEnd";
  protected final String TEXT_559 = " + ";
  protected final String TEXT_560 = ";" + NL + "        " + NL + "\t\t\t\t        if(substringEnd";
  protected final String TEXT_561 = " > rowLen_";
  protected final String TEXT_562 = "){" + NL + "\t\t\t\t        \tsubstringEnd";
  protected final String TEXT_563 = " = rowLen_";
  protected final String TEXT_564 = ";" + NL + "\t\t\t\t    \t}";
  protected final String TEXT_565 = NL + "\t\t\t\t\tbyteArray_";
  protected final String TEXT_566 = "[";
  protected final String TEXT_567 = "] = arrays_";
  protected final String TEXT_568 = ".copyOfRange(row_";
  protected final String TEXT_569 = ".getBytes(";
  protected final String TEXT_570 = "),substringBegin";
  protected final String TEXT_571 = ", substringEnd";
  protected final String TEXT_572 = ");" + NL + "\t\t\t\t\tcolumnValue";
  protected final String TEXT_573 = "[";
  protected final String TEXT_574 = "] = new String(byteArray_";
  protected final String TEXT_575 = "[";
  protected final String TEXT_576 = "],";
  protected final String TEXT_577 = ");";
  protected final String TEXT_578 = NL + "        \t\t\tcolumnValue";
  protected final String TEXT_579 = "[";
  protected final String TEXT_580 = "] = row_";
  protected final String TEXT_581 = ".substring(substringBegin";
  protected final String TEXT_582 = ", substringEnd";
  protected final String TEXT_583 = ");";
  protected final String TEXT_584 = NL + "\t\t\t\t\t\tcolumnValue";
  protected final String TEXT_585 = "[";
  protected final String TEXT_586 = "] = columnValue";
  protected final String TEXT_587 = "[";
  protected final String TEXT_588 = "].trim();";
  protected final String TEXT_589 = "    \t" + NL + "        \t\t\tsubstringBegin";
  protected final String TEXT_590 = " = substringEnd";
  protected final String TEXT_591 = ";" + NL + "\t\t\t}";
  protected final String TEXT_592 = NL + "\t\tfor (int i_";
  protected final String TEXT_593 = " = 0; i_";
  protected final String TEXT_594 = " < ";
  protected final String TEXT_595 = "; i_";
  protected final String TEXT_596 = "++) {" + NL + "\t\t\tif (i_";
  protected final String TEXT_597 = " >= arrFieldSeparator";
  protected final String TEXT_598 = ".length ){" + NL + "\t\t\t\tcolumnValue";
  protected final String TEXT_599 = "[i_";
  protected final String TEXT_600 = "]=\"\";" + NL + "\t\t\t\tcontinue;" + NL + "\t\t\t}" + NL + "\t\t\t ";
  protected final String TEXT_601 = NL + "\t\t\t\tif(i_";
  protected final String TEXT_602 = "==";
  protected final String TEXT_603 = "-1){" + NL + "\t\t\t\t\tdynamic_";
  protected final String TEXT_604 = ".clearColumnValues();" + NL + "\t\t\t \t\tfor (int i=0;i<maxColumnCount_";
  protected final String TEXT_605 = ";i++) {" + NL + "\t\t\t\t\t\troutines.system.DynamicMetadata dynamicMetadataColumn_";
  protected final String TEXT_606 = " = dynamic_";
  protected final String TEXT_607 = ".getColumnMetadata(i);" + NL + "\t\t\t\t\t\tint currentFieldLength_";
  protected final String TEXT_608 = " = dynamicMetadataColumn_";
  protected final String TEXT_609 = ".getLength();" + NL + "\t\t\t\t\t\tsubstringEnd";
  protected final String TEXT_610 = " = substringBegin";
  protected final String TEXT_611 = "+currentFieldLength_";
  protected final String TEXT_612 = ";" + NL + "\t\t\t\t\t\tif(substringEnd";
  protected final String TEXT_613 = " > rowLen_";
  protected final String TEXT_614 = "){" + NL + "\t\t\t\t\t\t\tsubstringEnd";
  protected final String TEXT_615 = " = rowLen_";
  protected final String TEXT_616 = ";" + NL + "\t\t\t\t\t\t}";
  protected final String TEXT_617 = NL + "\t\t\t\t\t\t\tbyte[] temp_byteArray_";
  protected final String TEXT_618 = " = arrays_";
  protected final String TEXT_619 = ".copyOfRange(row_";
  protected final String TEXT_620 = ".getBytes(";
  protected final String TEXT_621 = "),substringBegin";
  protected final String TEXT_622 = ", substringEnd";
  protected final String TEXT_623 = ");" + NL + "\t\t\t\t\t\t\tString currentColumnValue_";
  protected final String TEXT_624 = " = new String(temp_byteArray_";
  protected final String TEXT_625 = ",";
  protected final String TEXT_626 = ");\t\t\t\t\t\t";
  protected final String TEXT_627 = NL + "\t\t\t\t\t\t\tString currentColumnValue_";
  protected final String TEXT_628 = " = row_";
  protected final String TEXT_629 = ".substring(substringBegin";
  protected final String TEXT_630 = ", substringEnd";
  protected final String TEXT_631 = ");";
  protected final String TEXT_632 = NL + "\t\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_633 = " = TalendString.talendTrim(currentColumnValue_";
  protected final String TEXT_634 = ", ";
  protected final String TEXT_635 = ", ";
  protected final String TEXT_636 = ");";
  protected final String TEXT_637 = NL + "\t\t\t\t\t\t\tString type_";
  protected final String TEXT_638 = " = dynamicMetadataColumn_";
  protected final String TEXT_639 = ".getType();" + NL + "\t\t\t\t\t\t\tif(\"id_Short\".equals(type_";
  protected final String TEXT_640 = ") || \"id_Integer\".equals(type_";
  protected final String TEXT_641 = ") || \"id_Double\".equals(type_";
  protected final String TEXT_642 = ") || \"id_Float\".equals(type_";
  protected final String TEXT_643 = ") || \"id_Long\".equals(type_";
  protected final String TEXT_644 = ") || \"id_BigDecimal\".equals(type_";
  protected final String TEXT_645 = ")){" + NL + "\t\t\t\t\t\t\t\tcurrentColumnValue_";
  protected final String TEXT_646 = " = ParserUtils.parseTo_Number(currentColumnValue_";
  protected final String TEXT_647 = ", ";
  protected final String TEXT_648 = ", ";
  protected final String TEXT_649 = ");" + NL + "\t\t\t\t\t\t\t}";
  protected final String TEXT_650 = NL + "\t\t\t\t\t\tdynamic_";
  protected final String TEXT_651 = ".addColumnValue(currentColumnValue_";
  protected final String TEXT_652 = ");" + NL + "\t\t\t\t\t\tsubstringBegin";
  protected final String TEXT_653 = " = substringEnd";
  protected final String TEXT_654 = "; " + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}";
  protected final String TEXT_655 = NL + "\t\t    if (substringBegin";
  protected final String TEXT_656 = " >= rowLen_";
  protected final String TEXT_657 = ") {" + NL + "\t\t    \tcolumnValue";
  protected final String TEXT_658 = "[i_";
  protected final String TEXT_659 = "] = \"\";" + NL + "\t\t    } else{" + NL + "\t\t    " + NL + "\t\t\t\tif ((star_";
  protected final String TEXT_660 = ").equals(tmpAry_";
  protected final String TEXT_661 = "[i_";
  protected final String TEXT_662 = "])){" + NL + "\t\t\t\t\tsubstringEnd";
  protected final String TEXT_663 = " = rowLen_";
  protected final String TEXT_664 = ";" + NL + "\t\t\t\t} else{" + NL + "\t    \t\t\tsubstringEnd";
  protected final String TEXT_665 = " = substringEnd";
  protected final String TEXT_666 = " +tmpAry_";
  protected final String TEXT_667 = "[i_";
  protected final String TEXT_668 = "];" + NL + "\t\t\t        if(substringEnd";
  protected final String TEXT_669 = " > rowLen_";
  protected final String TEXT_670 = ")" + NL + "\t\t\t        \tsubstringEnd";
  protected final String TEXT_671 = " = rowLen_";
  protected final String TEXT_672 = ";" + NL + "\t\t\t\t}";
  protected final String TEXT_673 = NL + "\t\t\t\tbyteArray_";
  protected final String TEXT_674 = "[i_";
  protected final String TEXT_675 = "] = arrays_";
  protected final String TEXT_676 = ".copyOfRange(row_";
  protected final String TEXT_677 = ".getBytes(";
  protected final String TEXT_678 = "),substringBegin";
  protected final String TEXT_679 = ", substringEnd";
  protected final String TEXT_680 = ");" + NL + "\t\t\t\tcolumnValue";
  protected final String TEXT_681 = "[i_";
  protected final String TEXT_682 = "] = new String(byteArray_";
  protected final String TEXT_683 = "[i_";
  protected final String TEXT_684 = "],";
  protected final String TEXT_685 = ");";
  protected final String TEXT_686 = NL + "\t        \tcolumnValue";
  protected final String TEXT_687 = "[i_";
  protected final String TEXT_688 = "] = row_";
  protected final String TEXT_689 = ".substring(substringBegin";
  protected final String TEXT_690 = ", substringEnd";
  protected final String TEXT_691 = ");\t";
  protected final String TEXT_692 = NL + "\t        \tif (";
  protected final String TEXT_693 = ")" + NL + "\t        \t\tcolumnValue";
  protected final String TEXT_694 = "[i_";
  protected final String TEXT_695 = "] = columnValue";
  protected final String TEXT_696 = "[i_";
  protected final String TEXT_697 = "].trim();" + NL + "\t        \t" + NL + "\t        \tsubstringBegin";
  protected final String TEXT_698 = " = substringEnd";
  protected final String TEXT_699 = ";" + NL + "\t        }" + NL + "\t\t}" + NL + "\t";
  protected final String TEXT_700 = NL + "   positionalUtil_";
  protected final String TEXT_701 = ".setValue_";
  protected final String TEXT_702 = "(";
  protected final String TEXT_703 = ",columnValue";
  protected final String TEXT_704 = ");";
  protected final String TEXT_705 = NL + "\t\t\t";
  protected final String TEXT_706 = ".";
  protected final String TEXT_707 = "=dynamic_";
  protected final String TEXT_708 = ";";
  protected final String TEXT_709 = NL + "        \t\t\t";
  protected final String TEXT_710 = " ";
  protected final String TEXT_711 = " = null; ";
  protected final String TEXT_712 = "        \t\t\t" + NL + "        \t\t\t" + NL + "    } catch (Exception e) {" + NL + "        whetherReject_";
  protected final String TEXT_713 = " = true;";
  protected final String TEXT_714 = NL + "            throw(e);";
  protected final String TEXT_715 = NL + "                    ";
  protected final String TEXT_716 = " = new ";
  protected final String TEXT_717 = "Struct();";
  protected final String TEXT_718 = NL + "                    ";
  protected final String TEXT_719 = ".";
  protected final String TEXT_720 = " = ";
  protected final String TEXT_721 = ".";
  protected final String TEXT_722 = ";";
  protected final String TEXT_723 = NL + "                ";
  protected final String TEXT_724 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_725 = ";";
  protected final String TEXT_726 = NL + "                ";
  protected final String TEXT_727 = " = null;";
  protected final String TEXT_728 = NL + "                System.err.println(e.getMessage());";
  protected final String TEXT_729 = NL + "                ";
  protected final String TEXT_730 = " = null;";
  protected final String TEXT_731 = NL + "            \t";
  protected final String TEXT_732 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_733 = ";";
  protected final String TEXT_734 = NL + "    }" + NL + "        \t\t\t" + NL + "        \t\t\t";
  protected final String TEXT_735 = NL + "\t\t";
  protected final String TEXT_736 = "if(!whetherReject_";
  protected final String TEXT_737 = ") { ";
  protected final String TEXT_738 = "      " + NL + "             if(";
  protected final String TEXT_739 = " == null){ " + NL + "            \t ";
  protected final String TEXT_740 = " = new ";
  protected final String TEXT_741 = "Struct();" + NL + "             }\t\t\t\t";
  protected final String TEXT_742 = NL + "\t    \t ";
  protected final String TEXT_743 = ".";
  protected final String TEXT_744 = " = ";
  protected final String TEXT_745 = ".";
  protected final String TEXT_746 = ";    \t\t\t\t";
  protected final String TEXT_747 = NL + "\t\t";
  protected final String TEXT_748 = " } ";
  protected final String TEXT_749 = "\t";
  protected final String TEXT_750 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	
	List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
	List<? extends IConnection> connsFlow = node.getOutgoingConnections("FLOW");

    String rejectConnName = "";
    List<? extends IConnection> rejectConns = node.getOutgoingConnections("REJECT");
	List<IMetadataTable> metadatas = node.getMetadataList();
	if ((metadatas!=null)&&(metadatas.size()>0)) {
		IMetadataTable metadata = metadatas.get(0);
		if (metadata!=null) {
			String rowSeparator = ElementParameterParser.getValue(node, "__ROWSEPARATOR__");
			
			List<Map<String, String>> formats =
            (List<Map<String,String>>)ElementParameterParser.getObjectValue(
                node,
                "__FORMATS__"
            );
            
            List<Map<String, String>> trimSelects =
            (List<Map<String,String>>)ElementParameterParser.getObjectValue(
                node,
                "__TRIMSELECT__"
            );
            
            String pattern1 = ElementParameterParser.getValue(node, "__PATTERN__");
            
            boolean useByte = ("true").equals(ElementParameterParser.getValue(node, "__USE_BYTE__"));
            boolean advanced = ("true").equals(ElementParameterParser.getValue(node, "__ADVANCED_OPTION__"));
            
			String filename = ElementParameterParser.getValueWithUIFieldKey(node,"__FILENAME__", "FILENAME");
			
			String trimAll = ElementParameterParser.getValue(node,"__TRIMALL__");
			boolean isTrimAll = true;
			if(trimAll != null && ("false").equals(trimAll)){
				isTrimAll = false;
			}
			
   			String encoding = ElementParameterParser.getValue(node,"__ENCODING__");
   			
    		String header = ElementParameterParser.getValue(node, "__HEADER__");
    		
    		String footer = ElementParameterParser.getValue(node, "__FOOTER__");
    		
    		String limit = ElementParameterParser.getValue(node, "__LIMIT__");    		
    		if ("".equals(limit.trim())) limit = "-1";
    		
    		String removeEmptyRow = ElementParameterParser.getValue(node, "__REMOVE_EMPTY_ROW__");
    		
        	String dieOnErrorStr = ElementParameterParser.getValue(node, "__DIE_ON_ERROR__");
    		boolean dieOnError = (dieOnErrorStr!=null&&!("").equals(dieOnErrorStr))?("true").equals(dieOnErrorStr):false;
    		
    		//need to process rows longger than 100 000 characters, the property SafetySwitch(in talend_file_enhanced_20070724.jar) should be sent to false.(the default is true)
    		//that means if check the option(true), the logic value of bSafetySwitch should be changed to false (XOR with 'true')
    		boolean bSafetySwitch = (("true").equals(ElementParameterParser.getValue(node, "__PROCESS_LONG_ROW__")) ^ true);    		
    		String advancedSeparatorStr = ElementParameterParser.getValue(node, "__ADVANCED_SEPARATOR__");
    		boolean advancedSeparator = (advancedSeparatorStr!=null&&!("").equals(advancedSeparatorStr))?("true").equals(advancedSeparatorStr):false;
    		String thousandsSeparator = ElementParameterParser.getValueWithJavaType(node, "__THOUSANDS_SEPARATOR__", JavaTypesManager.CHARACTER);
    		String decimalSeparator = ElementParameterParser.getValueWithJavaType(node, "__DECIMAL_SEPARATOR__", JavaTypesManager.CHARACTER);    		  
			
			String checkDateStr = ElementParameterParser.getValue(node,"__CHECK_DATE__");
			boolean checkDate = (checkDateStr!=null&&!("").equals(checkDateStr))?("true").equals(checkDateStr):false;
			
			boolean uncompress = ("true").equals(ElementParameterParser.getValue(node,"__UNCOMPRESS__"));
			
    		if(("").equals(header)){
    			header = "0";
    		}
    			
    		if(("").equals(footer)){
    			footer = "0";
    		}
    		
    		boolean useStar = false;
    		
    		String pattern=TalendTextUtils.removeQuotes(pattern1);
    		String[] positions=(pattern.trim()).split(",");
    		for(int i=0;i<positions.length;i++){
            	if(("").equals(positions[i])){
               	 	positions[i]="0";
            	}
            	if(("*").equals(positions[i])){
            		useStar = true;
            	}
            }
			boolean hasDynamic = metadata.isDynamicSchema();
			String dynamic = ElementParameterParser.getValue(node, "__DYNAMIC__");
			boolean useExistingDynamic = "true".equals(ElementParameterParser.getValue(node, "__USE_EXISTING_DYNAMIC__"));
            String dyn = dynamic+"_DYNAMIC";
			if(useExistingDynamic){
				useStar = true;

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(dyn);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    
			}

    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(footer);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(limit);
    stringBuffer.append(TEXT_13);
    
		if(useByte){

    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    
		}

    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    
String firstConnName = "";
if (conns!=null) {
		if (conns.size()>0) {
			IConnection conn = conns.get(0);
			firstConnName = conn.getName();
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
                List<IMetadataColumn> listColumns = metadata.getListColumns();
				int sizeListColumns = listColumns.size();
    			if(advanced){// custom part start
    				for (int valueN=0; valueN<sizeListColumns; valueN++) {
    					String paddingChar = formats.get(valueN).get("PADDING_CHAR");
    					String align = formats.get(valueN).get("ALIGN");
    					if(("'L'").equals(align)){
    						align = "-1";
    					}else if(("'C'").equals(align)){
    						align = "0";
    					}else{
    						align = "1";
    					}
    					if(valueN%100==0){

    stringBuffer.append(TEXT_21);
    stringBuffer.append((valueN/100));
    stringBuffer.append(TEXT_22);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
     if(useByte){ 
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
     } 
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    
}
    					if(valueN == sizeListColumns - 1 && useStar){ //last column

    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_38);
    
							if(useByte){

    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_51);
    
							}else{

    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_58);
    
							}

    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    
						}else{//other column
							if(useByte){

    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_88);
    
							}else{

    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_102);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(valueN );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    
							}
						}
						if(isTrimAll || ("true").equals(trimSelects.get(valueN).get("TRIM"))){

    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    
						}
					IMetadataColumn column = listColumns.get(valueN);
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
					if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_114);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_115);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_117);
    
					} else if("id_Dynamic".equals(column.getTalendType())){
						if(useExistingDynamic){

    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(dyn);
    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_123);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_124);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_127);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_134);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_139);
    
								if(useByte){

    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_145);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_149);
    
								}else{

    stringBuffer.append(TEXT_150);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_154);
    
								}
								if(advanced){

    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_157);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_158);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_159);
    
								}
								if(isTrimAll || (hasDynamic &&(("true").equals(trimSelects.get(trimSelects.size()-1).get("TRIM"))))){

    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_162);
    
								}
								if(advancedSeparator) { 

    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid);
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
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_170);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_171);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_172);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_173);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_174);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_175);
    
								}

    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_178);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_179);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_180);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_182);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_183);
    
						}
					} else {

    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_185);
    
						if(javaType == JavaTypesManager.DATE) {
							if(checkDate) {

    stringBuffer.append(TEXT_186);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_187);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_188);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_190);
    
							} else {

    stringBuffer.append(TEXT_191);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_192);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_193);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_194);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_195);
    
							}
						}else if(advancedSeparator && JavaTypesManager.isNumberType(javaType, column.isNullable())) { 

    stringBuffer.append(TEXT_196);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_197);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_198);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_201);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_202);
    
					}else if(javaType == JavaTypesManager.BYTE_ARRAY) {

    stringBuffer.append(TEXT_203);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_204);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_205);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_207);
    
							} else {

    stringBuffer.append(TEXT_208);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_210);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    
							}

    stringBuffer.append(TEXT_213);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_214);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_215);
    stringBuffer.append(JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate));
    stringBuffer.append(TEXT_216);
    					
					}
					if((valueN+1)%100==0){

    stringBuffer.append(TEXT_217);
                      }
				}//end for_
				if(sizeListColumns>0&&(sizeListColumns%100)>0){

    stringBuffer.append(TEXT_218);
    
				}
	}//custom end
    else{//custom not check
				for (int valueN=0; valueN<sizeListColumns; valueN++) {	
				    if(valueN%100==0){

    stringBuffer.append(TEXT_219);
    stringBuffer.append((valueN/100));
    stringBuffer.append(TEXT_220);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_221);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_222);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_223);
    
                    }	

    
                    IMetadataColumn column = listColumns.get(valueN);
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
					if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_224);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_227);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_228);
    
					}else if("id_Dynamic".equals(column.getTalendType())){
						
					} else {

    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_230);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_231);
    
					        if(javaType == JavaTypesManager.DATE) {
								if(checkDate) {

    stringBuffer.append(TEXT_232);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_233);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_236);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_237);
    
								} else {

    stringBuffer.append(TEXT_238);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_239);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_240);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_241);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_242);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_243);
    
								}
							}else if(advancedSeparator && JavaTypesManager.isNumberType(javaType, column.isNullable())) { 

    stringBuffer.append(TEXT_244);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_245);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_246);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_247);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_248);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_249);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_250);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_251);
    
					        }else if(javaType == JavaTypesManager.BYTE_ARRAY) {

    stringBuffer.append(TEXT_252);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_253);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_254);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_255);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_256);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_257);
    
							} else {

    stringBuffer.append(TEXT_258);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_259);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_260);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_261);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_262);
    stringBuffer.append(valueN);
    stringBuffer.append(TEXT_263);
    
							}

    stringBuffer.append(TEXT_264);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_265);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_266);
    stringBuffer.append(JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate));
    stringBuffer.append(TEXT_267);
    
        			}

    stringBuffer.append(TEXT_268);
    
             if((valueN+1)%100==0){

    stringBuffer.append(TEXT_269);
    
             }
         }

    stringBuffer.append(TEXT_270);
    
            if(sizeListColumns>0&&(sizeListColumns%100)>0){

    stringBuffer.append(TEXT_271);
    
             }

    stringBuffer.append(TEXT_272);
              }//custom not check
		}
	}
}

    stringBuffer.append(TEXT_273);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_274);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_275);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_276);
    
		if(advanced){

    stringBuffer.append(TEXT_277);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_278);
    stringBuffer.append(formats.size() );
    stringBuffer.append(TEXT_279);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_280);
    stringBuffer.append(formats.size() );
    stringBuffer.append(TEXT_281);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_282);
    stringBuffer.append(formats.size() );
    stringBuffer.append(TEXT_283);
    
			for(int i = 0; i < formats.size(); i++){ 
				if(i == formats.size() - 1 && !(("").equals(rowSeparator) || ("\"\"").equals(rowSeparator))){
					if(("*").equals(formats.get(i).get("SIZE"))){ 
						useStar = true;
					}

    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_285);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_286);
    stringBuffer.append(useStar ? -1 :  formats.get(i).get("SIZE") );
    stringBuffer.append(TEXT_287);
    
				}else{

    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_289);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_290);
    stringBuffer.append(formats.get(i).get("SIZE") );
    stringBuffer.append(TEXT_291);
    
				}
			}
			for(int i = 0; i < formats.size(); i++){ 
				if(i == 0){

    stringBuffer.append(TEXT_292);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_293);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_294);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_295);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_296);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_297);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_298);
    
				}else if(i == formats.size() - 1 && useStar){

    stringBuffer.append(TEXT_299);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_300);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_301);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_302);
    stringBuffer.append(i-1 );
    stringBuffer.append(TEXT_303);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_304);
    stringBuffer.append(i-1 );
    stringBuffer.append(TEXT_305);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_306);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_307);
    
				}else{

    stringBuffer.append(TEXT_308);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_309);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_310);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_311);
    stringBuffer.append(i-1 );
    stringBuffer.append(TEXT_312);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_313);
    stringBuffer.append(i-1 );
    stringBuffer.append(TEXT_314);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_315);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_316);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_317);
    stringBuffer.append(i-1 );
    stringBuffer.append(TEXT_318);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_319);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_320);
    
				}
			}
		}//end if(advanced)

    stringBuffer.append(TEXT_321);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_322);
    stringBuffer.append(filename );
    stringBuffer.append(TEXT_323);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_324);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_325);
    
		if(uncompress){

    stringBuffer.append(TEXT_326);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_327);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_328);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_329);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_330);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_332);
     if(dieOnError) {
    stringBuffer.append(TEXT_333);
     } else { 
    stringBuffer.append(TEXT_334);
     } 
    stringBuffer.append(TEXT_335);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_336);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_337);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_338);
     if(dieOnError) {
    stringBuffer.append(TEXT_339);
     } else { 
    stringBuffer.append(TEXT_340);
     } 
    stringBuffer.append(TEXT_341);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_342);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_343);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_344);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_345);
    
	if(advanced){
		if(useByte){
	
    stringBuffer.append(TEXT_346);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_347);
    
	}
	
    stringBuffer.append(TEXT_348);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_349);
    
	}else{
		if(useByte){
	
    stringBuffer.append(TEXT_350);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_351);
    stringBuffer.append(metadata.getListColumns().size());
    stringBuffer.append(TEXT_352);
    
		}
	
    stringBuffer.append(TEXT_353);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_354);
    stringBuffer.append(metadata.getListColumns().size());
    stringBuffer.append(TEXT_355);
    
	}
	
    stringBuffer.append(TEXT_356);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_357);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_358);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_359);
    
		}else{

    stringBuffer.append(TEXT_360);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_361);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_362);
    
if(advanced){
	if(useByte){

    stringBuffer.append(TEXT_363);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_364);
    
}

    stringBuffer.append(TEXT_365);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_366);
    
}else{
	if(useByte){

    stringBuffer.append(TEXT_367);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_368);
    stringBuffer.append(metadata.getListColumns().size());
    stringBuffer.append(TEXT_369);
    
	}

    stringBuffer.append(TEXT_370);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_371);
    stringBuffer.append(metadata.getListColumns().size());
    stringBuffer.append(TEXT_372);
    
}

    stringBuffer.append(TEXT_373);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_374);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_375);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_376);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_377);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_378);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_379);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_380);
    
		}
			if(("").equals(rowSeparator) || ("\"\"").equals(rowSeparator) ){

    stringBuffer.append(TEXT_381);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_382);
    
				if(advanced){ 
					for(int i = 0; i < formats.size(); i++){ 

    stringBuffer.append(TEXT_383);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_384);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_385);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_386);
    
					}
				}else{
    				for(int i=0;i<positions.length;i++){

    stringBuffer.append(TEXT_387);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_388);
    stringBuffer.append(positions[i] );
    stringBuffer.append(TEXT_389);
    
        			}
				}

    stringBuffer.append(TEXT_390);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_391);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_392);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_393);
    
			}else{

    stringBuffer.append(TEXT_394);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_395);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_396);
    stringBuffer.append(rowSeparator );
    stringBuffer.append(TEXT_397);
    stringBuffer.append(removeEmptyRow );
    stringBuffer.append(TEXT_398);
    
			}

    stringBuffer.append(TEXT_399);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_400);
    stringBuffer.append(bSafetySwitch);
    stringBuffer.append(TEXT_401);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_402);
    stringBuffer.append(header );
    stringBuffer.append(TEXT_403);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_404);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_405);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_406);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_407);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_408);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_409);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_410);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_411);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_412);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_413);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_414);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_415);
    
			if(("").equals(rowSeparator) || ("\"\"").equals(rowSeparator) ){

    stringBuffer.append(TEXT_416);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_417);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_418);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_419);
    
			}else{

    stringBuffer.append(TEXT_420);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_421);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_422);
    stringBuffer.append(rowSeparator );
    stringBuffer.append(TEXT_423);
    stringBuffer.append(removeEmptyRow );
    stringBuffer.append(TEXT_424);
    
			}

    stringBuffer.append(TEXT_425);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_426);
    stringBuffer.append(header );
    stringBuffer.append(TEXT_427);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_428);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_429);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_430);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_431);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_432);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_433);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_434);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_435);
     if(dieOnError) {
    stringBuffer.append(TEXT_436);
     } else { 
    stringBuffer.append(TEXT_437);
     } 
    stringBuffer.append(TEXT_438);
    
if ( !(java.util.regex.Pattern.compile("[0-9]*").matcher(positions[0]).matches() )){
		if(pattern1.startsWith("\"")){

    stringBuffer.append(TEXT_439);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_440);
    stringBuffer.append(positions[0] );
    stringBuffer.append(TEXT_441);
     
		}else{

    stringBuffer.append(TEXT_442);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_443);
    stringBuffer.append(positions[0]);
    stringBuffer.append(TEXT_444);
     
		}

    stringBuffer.append(TEXT_445);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_446);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_447);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_448);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_449);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_450);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_451);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_452);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_453);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_454);
    
}	

    stringBuffer.append(TEXT_455);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_456);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_457);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_458);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_459);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_460);
    
	if(useByte){

    stringBuffer.append(TEXT_461);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_462);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_463);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_464);
    
	}else{

    stringBuffer.append(TEXT_465);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_466);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_467);
    
	}

    
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

    stringBuffer.append(TEXT_468);
    stringBuffer.append(connTemp.getName() );
    stringBuffer.append(TEXT_469);
    
    				}
    			}
    		}
    	}
    	
	if (conns!=null) {
		if (conns.size()>0) {
			IConnection conn = conns.get(0);
			firstConnName = conn.getName();
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
    stringBuffer.append(TEXT_470);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_471);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_472);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_473);
    
    			if(advanced){
    				List<IMetadataColumn> listColumns = metadata.getListColumns();
   				   int sizeListColumns = listColumns.size();
    				for (int valueN=0; valueN<sizeListColumns; valueN++) {
    					String paddingChar = formats.get(valueN).get("PADDING_CHAR");
    					String align = formats.get(valueN).get("ALIGN");
    					if(("'L'").equals(align)){
    						align = "-1";
    					}else if(("'C'").equals(align)){
    						align = "0";
    					}else{
    						align = "1";
    					}
                   if(valueN%100==0){

    stringBuffer.append(TEXT_474);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_475);
    stringBuffer.append((valueN/100));
    stringBuffer.append(TEXT_476);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_477);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_478);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_479);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_480);
     if(useByte){ 
    stringBuffer.append(TEXT_481);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_482);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_483);
     } 
    stringBuffer.append(TEXT_484);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_485);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_486);
    
                  }
				}
			}else{// end if(advance)

    stringBuffer.append(TEXT_487);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_488);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_489);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_490);
    
	///////////////////
	//Branch: first item is a numberic, not '((String)globalMap.get("global_variable"))' or 'context.Separator'
	if ( java.util.regex.Pattern.compile("[0-9]*").matcher(positions[0]).matches() ){
	///////////////////
	
		for(int i=0;i <	metadata.getListColumns().size();i++){
			if(i >=positions.length){

    stringBuffer.append(TEXT_491);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_492);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_493);
    
				continue;
			}
			if(i==(metadata.getListColumns().size()-1) && useExistingDynamic){
				String paddingChar = formats.get(i).get("PADDING_CHAR");
				String align = formats.get(i).get("ALIGN");

    stringBuffer.append(TEXT_494);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_495);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_496);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_497);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_498);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_499);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_500);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_501);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_502);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_503);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_504);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_505);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_506);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_507);
    
					if(useByte){

    stringBuffer.append(TEXT_508);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_509);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_510);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_511);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_512);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_513);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_514);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_515);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_516);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_517);
    
					}else{

    stringBuffer.append(TEXT_518);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_519);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_520);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_521);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_522);
    
					}
					if(isTrimAll || (hasDynamic &&(("true").equals(trimSelects.get(trimSelects.size()-1).get("TRIM"))))){

    stringBuffer.append(TEXT_523);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_524);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_525);
    
					}
					if(advanced){

    stringBuffer.append(TEXT_526);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_527);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_528);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_529);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_530);
    
					}
					if(advancedSeparator) { 

    stringBuffer.append(TEXT_531);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_532);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_533);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_534);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_535);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_536);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_537);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_538);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_539);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_540);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_541);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_542);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_543);
    
					}

    stringBuffer.append(TEXT_544);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_545);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_546);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_547);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_548);
    
			}

    stringBuffer.append(TEXT_549);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_550);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_551);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_552);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_553);
    
					if(("*").equals(positions[i])){

    stringBuffer.append(TEXT_554);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_555);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_556);
    
					} else{

    stringBuffer.append(TEXT_557);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_558);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_559);
    stringBuffer.append(positions[i]);
    stringBuffer.append(TEXT_560);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_561);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_562);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_563);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_564);
    
					}
					if(useByte){

    stringBuffer.append(TEXT_565);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_566);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_567);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_568);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_569);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_570);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_571);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_572);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_573);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_574);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_575);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_576);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_577);
    
					}else{

    stringBuffer.append(TEXT_578);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_579);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_580);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_581);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_582);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_583);
     
					}
       				if(isTrimAll || ("true").equals(trimSelects.get(i).get("TRIM"))){

    stringBuffer.append(TEXT_584);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_585);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_586);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_587);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_588);
    
					}

    stringBuffer.append(TEXT_589);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_590);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_591);
    
	
		}//for(...)
				
	///////////////
	} else{

    stringBuffer.append(TEXT_592);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_593);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_594);
    stringBuffer.append(metadata.getListColumns().size());
    stringBuffer.append(TEXT_595);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_596);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_597);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_598);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_599);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_600);
    
			if(useExistingDynamic){
				String paddingChar = formats.get(formats.size()-1).get("PADDING_CHAR");
				String align = formats.get(formats.size()-1).get("ALIGN");

    stringBuffer.append(TEXT_601);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_602);
    stringBuffer.append(metadata.getListColumns().size());
    stringBuffer.append(TEXT_603);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_604);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_605);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_606);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_607);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_608);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_609);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_610);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_611);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_612);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_613);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_614);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_615);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_616);
    
						if(useByte){

    stringBuffer.append(TEXT_617);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_618);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_619);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_620);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_621);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_622);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_623);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_624);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_625);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_626);
    
						}else{

    stringBuffer.append(TEXT_627);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_628);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_629);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_630);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_631);
    
						}
						if(advanced){

    stringBuffer.append(TEXT_632);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_633);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_634);
    stringBuffer.append(paddingChar );
    stringBuffer.append(TEXT_635);
    stringBuffer.append(align );
    stringBuffer.append(TEXT_636);
    
						}
						if(advancedSeparator) { 

    stringBuffer.append(TEXT_637);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_638);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_639);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_640);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_641);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_642);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_643);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_644);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_645);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_646);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_647);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_648);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_649);
    
						}

    stringBuffer.append(TEXT_650);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_651);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_652);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_653);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_654);
    
			}

    stringBuffer.append(TEXT_655);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_656);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_657);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_658);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_659);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_660);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_661);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_662);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_663);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_664);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_665);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_666);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_667);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_668);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_669);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_670);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_671);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_672);
    
		if(useByte){

    stringBuffer.append(TEXT_673);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_674);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_675);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_676);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_677);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_678);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_679);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_680);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_681);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_682);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_683);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_684);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_685);
    
		}else{

    stringBuffer.append(TEXT_686);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_687);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_688);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_689);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_690);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_691);
    
		}

    stringBuffer.append(TEXT_692);
    stringBuffer.append(isTrimAll);
    stringBuffer.append(TEXT_693);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_694);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_695);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_696);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_697);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_698);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_699);
    
	}
	///////////////
	List<IMetadataColumn> listColumns = metadata.getListColumns();
	int sizeListColumns = listColumns.size();
	for (int valueN=0; valueN<sizeListColumns; valueN++) {	
		if(valueN%100==0){

    stringBuffer.append(TEXT_700);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_701);
    stringBuffer.append((valueN/100));
    stringBuffer.append(TEXT_702);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_703);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_704);
    
	    
	    }
				
        }
		if(useExistingDynamic && !advanced){

    stringBuffer.append(TEXT_705);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_706);
    stringBuffer.append(metadata.getDynamicColumn().getLabel());
    stringBuffer.append(TEXT_707);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_708);
    		}
    }

    stringBuffer.append(TEXT_709);
    if(rejectConnName.equals(firstConnName)) {
    stringBuffer.append(TEXT_710);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_711);
    }
    stringBuffer.append(TEXT_712);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_713);
    
        if (dieOnError) {
            
    stringBuffer.append(TEXT_714);
    
        } else {
            if(!("").equals(rejectConnName)&&!rejectConnName.equals(firstConnName)&&rejectColumnList != null && rejectColumnList.size() > 0) {

                
    stringBuffer.append(TEXT_715);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_716);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_717);
    
                for(IMetadataColumn column : metadata.getListColumns()) {
                    
    stringBuffer.append(TEXT_718);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_719);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_720);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_721);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_722);
    
                }
                
    stringBuffer.append(TEXT_723);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_724);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_725);
    stringBuffer.append(TEXT_726);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_727);
    
            } else if(("").equals(rejectConnName)){
                
    stringBuffer.append(TEXT_728);
    stringBuffer.append(TEXT_729);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_730);
    
            } else if(rejectConnName.equals(firstConnName)){
    stringBuffer.append(TEXT_731);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_732);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_733);
    }
        } 
        
    stringBuffer.append(TEXT_734);
            			
        		}
		if (conns.size()>0) {	
			boolean isFirstEnter = true;
			for (int i=0;i<conns.size();i++) {
				conn = conns.get(i);
				if ((conn.getName().compareTo(firstConnName)!=0)&&(conn.getName().compareTo(rejectConnName)!=0)&&(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA))) {

    stringBuffer.append(TEXT_735);
     if(isFirstEnter) {
    stringBuffer.append(TEXT_736);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_737);
     isFirstEnter = false; } 
    stringBuffer.append(TEXT_738);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_739);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_740);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_741);
    
			    	 for (IMetadataColumn column: metadata.getListColumns()) {

    stringBuffer.append(TEXT_742);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_743);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_744);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_745);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_746);
    
				 	}
				}
			}

    stringBuffer.append(TEXT_747);
     if(!isFirstEnter) {
    stringBuffer.append(TEXT_748);
     } 
    stringBuffer.append(TEXT_749);
    
		}
        	}
		}
	}
}

    stringBuffer.append(TEXT_750);
    return stringBuffer.toString();
  }
}
