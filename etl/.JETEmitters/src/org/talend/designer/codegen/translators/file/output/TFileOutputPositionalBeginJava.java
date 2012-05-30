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
import org.talend.core.model.utils.NodeUtil;
import java.util.List;
import java.util.Map;

public class TFileOutputPositionalBeginJava
{
  protected static String nl;
  public static synchronized TFileOutputPositionalBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileOutputPositionalBeginJava result = new TFileOutputPositionalBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\t\troutines.system.Dynamic dynamic_";
  protected final String TEXT_3 = " = (routines.system.Dynamic)globalMap.get(\"";
  protected final String TEXT_4 = "\");" + NL + "\t\t\tint maxColumnCount_";
  protected final String TEXT_5 = " = dynamic_";
  protected final String TEXT_6 = ".getColumnCount();" + NL + "\t\t\tdynamic_";
  protected final String TEXT_7 = ".clearColumnValues();";
  protected final String TEXT_8 = NL + "\t\tint nb_line_";
  protected final String TEXT_9 = " = 0;" + NL + "\t\t";
  protected final String TEXT_10 = NL + "class Arrays_";
  protected final String TEXT_11 = "{" + NL + "    public byte[] copyOfRange(byte[] original, int from, int to) {" + NL + "        int newLength = to - from;" + NL + "        if (newLength < 0)" + NL + "            throw new IllegalArgumentException(from + \" > \" + to);" + NL + "        byte[] copy = new byte[newLength];" + NL + "        System.arraycopy(original, from, copy, 0," + NL + "                         Math.min(original.length - from, newLength));" + NL + "        return copy;" + NL + "    }" + NL + "}" + NL + "byte[] byteArray_";
  protected final String TEXT_12 = " = new byte[0];" + NL + "Arrays_";
  protected final String TEXT_13 = " arrays_";
  protected final String TEXT_14 = " = new Arrays_";
  protected final String TEXT_15 = "();";
  protected final String TEXT_16 = "\t\t" + NL + "\t\tclass PositionUtil_";
  protected final String TEXT_17 = "{" + NL + "\t\t";
  protected final String TEXT_18 = NL + "              void writeHeader_";
  protected final String TEXT_19 = "(String tempStringB";
  protected final String TEXT_20 = ",int tempLengthB";
  protected final String TEXT_21 = ",final ";
  protected final String TEXT_22 = " out";
  protected final String TEXT_23 = ",byte[] byteArray_";
  protected final String TEXT_24 = ",Arrays_";
  protected final String TEXT_25 = " arrays_";
  protected final String TEXT_26 = ")throws IOException,java.io.UnsupportedEncodingException{" + NL + "              " + NL + "                 int tempLengthM";
  protected final String TEXT_27 = "= 0;";
  protected final String TEXT_28 = NL + "    \t\t\t//get  and format output String begin";
  protected final String TEXT_29 = NL + "\t\t\t\t\t\troutines.system.Dynamic dynamic_";
  protected final String TEXT_30 = " = (routines.system.Dynamic)globalMap.get(\"";
  protected final String TEXT_31 = "\");" + NL + "\t\t\t\t\t\tint maxColumnCount_";
  protected final String TEXT_32 = " = dynamic_";
  protected final String TEXT_33 = ".getColumnCount();" + NL + "\t\t\t\t\t\tfor (int i=0;i<maxColumnCount_";
  protected final String TEXT_34 = ";i++) {" + NL + "\t\t\t\t\t\t\troutines.system.DynamicMetadata dynamicMetadataColumn_";
  protected final String TEXT_35 = " = dynamic_";
  protected final String TEXT_36 = ".getColumnMetadata(i);" + NL + "\t\t\t\t\t\t\tint currentFieldLength_";
  protected final String TEXT_37 = " = dynamicMetadataColumn_";
  protected final String TEXT_38 = ".getLength();" + NL + "\t\t\t\t\t\t\ttempStringB";
  protected final String TEXT_39 = " = dynamicMetadataColumn_";
  protected final String TEXT_40 = ".getName();";
  protected final String TEXT_41 = NL + "    \t\t\t\t\t\t\ttempLengthB";
  protected final String TEXT_42 = " = tempStringB";
  protected final String TEXT_43 = ".getBytes(";
  protected final String TEXT_44 = ").length;";
  protected final String TEXT_45 = NL + "    \t\t\t\t\t\t\ttempLengthB";
  protected final String TEXT_46 = " = tempStringB";
  protected final String TEXT_47 = ".length();";
  protected final String TEXT_48 = NL + "\t    \t\t\t\t\tif(tempLengthB";
  protected final String TEXT_49 = "<currentFieldLength_";
  protected final String TEXT_50 = "){";
  protected final String TEXT_51 = NL + "\t\t\t    \t\t\t\t\tfor(int j=0;j<currentFieldLength_";
  protected final String TEXT_52 = "-tempLengthB";
  protected final String TEXT_53 = ";j++){" + NL + "\t\t\t    \t\t\t\t\t\ttempStringB";
  protected final String TEXT_54 = "= tempStringB";
  protected final String TEXT_55 = " + ";
  protected final String TEXT_56 = ";" + NL + "\t\t\t    \t\t\t\t\t}";
  protected final String TEXT_57 = NL + "\t\t\t\t\t\t\t\t\tfor(int j=0;j<currentFieldLength_";
  protected final String TEXT_58 = "-tempLengthB";
  protected final String TEXT_59 = ";j++){" + NL + "\t\t\t    \t\t\t\t\t\ttempStringB";
  protected final String TEXT_60 = "= ";
  protected final String TEXT_61 = " + tempStringB";
  protected final String TEXT_62 = ";" + NL + "\t\t\t    \t\t\t\t\t}";
  protected final String TEXT_63 = NL + "\t\t\t\t\t\t\t\t\tint temp_";
  protected final String TEXT_64 = " = currentFieldLength_";
  protected final String TEXT_65 = "-tempLengthB";
  protected final String TEXT_66 = ";" + NL + "\t\t\t\t\t\t\t\t\tfor(int j=0;j<temp_";
  protected final String TEXT_67 = "/2;j++){" + NL + "\t\t\t    \t\t\t\t\t\ttempStringB";
  protected final String TEXT_68 = "= ";
  protected final String TEXT_69 = " + tempStringB";
  protected final String TEXT_70 = " + ";
  protected final String TEXT_71 = ";" + NL + "\t\t\t    \t\t\t\t\t}" + NL + "\t\t\t    \t\t\t\t\tif(temp_";
  protected final String TEXT_72 = "%2==1){" + NL + "\t\t\t    \t\t\t\t\t\ttempStringB";
  protected final String TEXT_73 = " = tempStringB";
  protected final String TEXT_74 = " + ";
  protected final String TEXT_75 = ";" + NL + "\t\t\t    \t\t\t\t\t}";
  protected final String TEXT_76 = NL + "\t\t\t\t\t\t\t}else if(tempLengthB";
  protected final String TEXT_77 = " > currentFieldLength_";
  protected final String TEXT_78 = "){";
  protected final String TEXT_79 = NL + "                    \t\t\t\t\tbyteArray_";
  protected final String TEXT_80 = "=arrays_";
  protected final String TEXT_81 = ".copyOfRange(tempStringB";
  protected final String TEXT_82 = ".getBytes(";
  protected final String TEXT_83 = "),tempLengthB";
  protected final String TEXT_84 = " - currentFieldLength_";
  protected final String TEXT_85 = ",tempLengthB";
  protected final String TEXT_86 = ");" + NL + "\t                    \t\t\t\ttempStringB";
  protected final String TEXT_87 = " = new String(byteArray_";
  protected final String TEXT_88 = ",";
  protected final String TEXT_89 = ");";
  protected final String TEXT_90 = NL + "\t\t\t\t\t\t\t\t\t\ttempStringB";
  protected final String TEXT_91 = " = tempStringB";
  protected final String TEXT_92 = ".substring(tempLengthB";
  protected final String TEXT_93 = "-currentFieldLength_";
  protected final String TEXT_94 = ");";
  protected final String TEXT_95 = NL + "\t\t\t\t\t\t\t\t\tint begin";
  protected final String TEXT_96 = "=(tempLengthB";
  protected final String TEXT_97 = "-currentFieldLength_";
  protected final String TEXT_98 = ")/2;";
  protected final String TEXT_99 = NL + "\t\t\t\t\t\t\t\t\t\tbyteArray_";
  protected final String TEXT_100 = "=arrays_";
  protected final String TEXT_101 = ".copyOfRange(tempStringB";
  protected final String TEXT_102 = ".getBytes(";
  protected final String TEXT_103 = "),begin";
  protected final String TEXT_104 = ",begin";
  protected final String TEXT_105 = " + currentFieldLength_";
  protected final String TEXT_106 = ");" + NL + "\t\t                    \t\t\ttempStringB";
  protected final String TEXT_107 = " = new String(byteArray_";
  protected final String TEXT_108 = ",";
  protected final String TEXT_109 = ");";
  protected final String TEXT_110 = NL + "    \t                    \t\t\ttempStringB";
  protected final String TEXT_111 = " = tempStringB";
  protected final String TEXT_112 = ".substring(begin";
  protected final String TEXT_113 = ", begin";
  protected final String TEXT_114 = "+currentFieldLength_";
  protected final String TEXT_115 = ");";
  protected final String TEXT_116 = NL + "\t                    \t\t\t\tbyteArray_";
  protected final String TEXT_117 = "=arrays_";
  protected final String TEXT_118 = ".copyOfRange(tempStringB";
  protected final String TEXT_119 = ".getBytes(";
  protected final String TEXT_120 = "),0,currentFieldLength_";
  protected final String TEXT_121 = ");" + NL + "                    \t\t\t\t\ttempStringB";
  protected final String TEXT_122 = " = new String(byteArray_";
  protected final String TEXT_123 = ",";
  protected final String TEXT_124 = ");";
  protected final String TEXT_125 = NL + "                    \t\t\t\t\ttempStringB";
  protected final String TEXT_126 = " = tempStringB";
  protected final String TEXT_127 = ".substring(0, currentFieldLength_";
  protected final String TEXT_128 = ");";
  protected final String TEXT_129 = NL + "\t\t\t\t\t\t\t}" + NL + "\t    \t\t\t\t\tout";
  protected final String TEXT_130 = ".write(tempStringB";
  protected final String TEXT_131 = ");" + NL + "\t\t\t\t\t\t}";
  protected final String TEXT_132 = "\t\t\t\t" + NL + "    \t\t\ttempStringB";
  protected final String TEXT_133 = "=\"";
  protected final String TEXT_134 = "\";" + NL + "    \t\t\t";
  protected final String TEXT_135 = NL + "    \t\t\ttempLengthB";
  protected final String TEXT_136 = "=tempStringB";
  protected final String TEXT_137 = ".getBytes(";
  protected final String TEXT_138 = ").length;" + NL + "    \t\t\t";
  protected final String TEXT_139 = NL + "    \t\t\ttempLengthB";
  protected final String TEXT_140 = "=tempStringB";
  protected final String TEXT_141 = ".length();" + NL + "    \t\t\t";
  protected final String TEXT_142 = NL + "    \t\t\t" + NL + "            \tif (tempLengthB";
  protected final String TEXT_143 = " > ";
  protected final String TEXT_144 = ") {";
  protected final String TEXT_145 = NL + "                    \t\tbyteArray_";
  protected final String TEXT_146 = "=arrays_";
  protected final String TEXT_147 = ".copyOfRange(tempStringB";
  protected final String TEXT_148 = ".getBytes(";
  protected final String TEXT_149 = "),tempLengthB";
  protected final String TEXT_150 = " - ";
  protected final String TEXT_151 = ",tempLengthB";
  protected final String TEXT_152 = ");" + NL + "\t                    \ttempStringB";
  protected final String TEXT_153 = " = new String(byteArray_";
  protected final String TEXT_154 = ",";
  protected final String TEXT_155 = ");";
  protected final String TEXT_156 = NL + "\t                        tempStringB";
  protected final String TEXT_157 = " = tempStringB";
  protected final String TEXT_158 = ".substring(tempLengthB";
  protected final String TEXT_159 = "-";
  protected final String TEXT_160 = ");";
  protected final String TEXT_161 = NL + "                        int begin";
  protected final String TEXT_162 = "=(tempLengthB";
  protected final String TEXT_163 = "-";
  protected final String TEXT_164 = ")/2;";
  protected final String TEXT_165 = NL + "\t\t\t\t\t\t\tbyteArray_";
  protected final String TEXT_166 = "=arrays_";
  protected final String TEXT_167 = ".copyOfRange(tempStringB";
  protected final String TEXT_168 = ".getBytes(";
  protected final String TEXT_169 = "),begin";
  protected final String TEXT_170 = ",begin";
  protected final String TEXT_171 = " + ";
  protected final String TEXT_172 = ");" + NL + "\t\t                    tempStringB";
  protected final String TEXT_173 = " = new String(byteArray_";
  protected final String TEXT_174 = ",";
  protected final String TEXT_175 = ");";
  protected final String TEXT_176 = NL + "    \t                    tempStringB";
  protected final String TEXT_177 = " = tempStringB";
  protected final String TEXT_178 = ".substring(begin";
  protected final String TEXT_179 = ", begin";
  protected final String TEXT_180 = "+";
  protected final String TEXT_181 = ");";
  protected final String TEXT_182 = NL + "\t                    \tbyteArray_";
  protected final String TEXT_183 = "=arrays_";
  protected final String TEXT_184 = ".copyOfRange(tempStringB";
  protected final String TEXT_185 = ".getBytes(";
  protected final String TEXT_186 = "),0,";
  protected final String TEXT_187 = ");" + NL + "                    \t\ttempStringB";
  protected final String TEXT_188 = " = new String(byteArray_";
  protected final String TEXT_189 = ",";
  protected final String TEXT_190 = ");";
  protected final String TEXT_191 = NL + "                    \t\ttempStringB";
  protected final String TEXT_192 = " = tempStringB";
  protected final String TEXT_193 = ".substring(0, ";
  protected final String TEXT_194 = ");";
  protected final String TEXT_195 = NL + "                }else if(tempLengthB";
  protected final String TEXT_196 = "<";
  protected final String TEXT_197 = "){" + NL + "                    StringBuilder result";
  protected final String TEXT_198 = " = new StringBuilder();";
  protected final String TEXT_199 = NL + "                        result";
  protected final String TEXT_200 = ".append(tempStringB";
  protected final String TEXT_201 = ");" + NL + "                        for(int i";
  protected final String TEXT_202 = "=0; i";
  protected final String TEXT_203 = "< ";
  protected final String TEXT_204 = "-tempLengthB";
  protected final String TEXT_205 = "; i";
  protected final String TEXT_206 = "++){" + NL + "                            result";
  protected final String TEXT_207 = ".append(";
  protected final String TEXT_208 = ");" + NL + "                        }" + NL + "                        tempStringB";
  protected final String TEXT_209 = " = result";
  protected final String TEXT_210 = ".toString();";
  protected final String TEXT_211 = NL + "                        for(int i";
  protected final String TEXT_212 = "=0; i";
  protected final String TEXT_213 = "< ";
  protected final String TEXT_214 = "-tempLengthB";
  protected final String TEXT_215 = "; i";
  protected final String TEXT_216 = "++){" + NL + "                            result";
  protected final String TEXT_217 = ".append(";
  protected final String TEXT_218 = ");" + NL + "                        }" + NL + "                        result";
  protected final String TEXT_219 = ".append(tempStringB";
  protected final String TEXT_220 = ");" + NL + "                        tempStringB";
  protected final String TEXT_221 = " = result";
  protected final String TEXT_222 = ".toString();";
  protected final String TEXT_223 = NL + "                        int temp";
  protected final String TEXT_224 = "= (";
  protected final String TEXT_225 = "-tempLengthB";
  protected final String TEXT_226 = ")/2;" + NL + "                        for(int i";
  protected final String TEXT_227 = "=0;i";
  protected final String TEXT_228 = "<temp";
  protected final String TEXT_229 = ";i";
  protected final String TEXT_230 = "++){" + NL + "                            result";
  protected final String TEXT_231 = ".append(";
  protected final String TEXT_232 = ");" + NL + "                        }" + NL + "                        result";
  protected final String TEXT_233 = ".append(tempStringB";
  protected final String TEXT_234 = ");" + NL + "                        for(int i";
  protected final String TEXT_235 = "=0;i";
  protected final String TEXT_236 = "<temp";
  protected final String TEXT_237 = ";i";
  protected final String TEXT_238 = "++){" + NL + "                            result";
  protected final String TEXT_239 = ".append(";
  protected final String TEXT_240 = ");" + NL + "                        }" + NL + "                        if((temp";
  protected final String TEXT_241 = "+temp";
  protected final String TEXT_242 = ")!=(";
  protected final String TEXT_243 = "-tempLengthB";
  protected final String TEXT_244 = ")){" + NL + "                            result";
  protected final String TEXT_245 = ".append(";
  protected final String TEXT_246 = ");" + NL + "                        }" + NL + "                        tempStringB";
  protected final String TEXT_247 = " = result";
  protected final String TEXT_248 = ".toString();";
  protected final String TEXT_249 = "       " + NL + "                }" + NL + "                //get  and format output String end" + NL + "    \t\t\tout";
  protected final String TEXT_250 = ".write(tempStringB";
  protected final String TEXT_251 = ");";
  protected final String TEXT_252 = NL + "              }";
  protected final String TEXT_253 = NL + "              }  ";
  protected final String TEXT_254 = NL + "\t\t\t\t  void setValue_";
  protected final String TEXT_255 = "(final ";
  protected final String TEXT_256 = "Struct ";
  protected final String TEXT_257 = ",StringBuilder sb_";
  protected final String TEXT_258 = ",String tempStringM";
  protected final String TEXT_259 = ",int tempLengthM";
  protected final String TEXT_260 = ",byte[] byteArray_";
  protected final String TEXT_261 = ",Arrays_";
  protected final String TEXT_262 = " arrays_";
  protected final String TEXT_263 = ")throws IOException,java.io.UnsupportedEncodingException{";
  protected final String TEXT_264 = NL + "\t\t\t\t\t\troutines.system.Dynamic dynamic_map_";
  protected final String TEXT_265 = " =(routines.system.Dynamic)globalMap.get(\"";
  protected final String TEXT_266 = "\");" + NL + "\t\t\t\t\t\troutines.system.Dynamic dynamic_";
  protected final String TEXT_267 = " = ";
  protected final String TEXT_268 = ".";
  protected final String TEXT_269 = ";//" + NL + "\t\t\t\t\t\tint maxColumnCount_";
  protected final String TEXT_270 = " = dynamic_map_";
  protected final String TEXT_271 = ".getColumnCount();" + NL + "\t\t\t\t\t\tString temp_";
  protected final String TEXT_272 = "= \"\";" + NL + "\t\t\t\t\t\tfor (int i=0;i<maxColumnCount_";
  protected final String TEXT_273 = ";i++) {" + NL + "\t\t\t\t\t\t\troutines.system.DynamicMetadata metadata_";
  protected final String TEXT_274 = " = dynamic_map_";
  protected final String TEXT_275 = ".getColumnMetadata(i);" + NL + "\t\t\t\t\t\t\ttemp_";
  protected final String TEXT_276 = " = String.valueOf(dynamic_";
  protected final String TEXT_277 = ".getColumnValue(i));" + NL + "\t\t\t\t\t\t\tString type_";
  protected final String TEXT_278 = " = metadata_";
  protected final String TEXT_279 = ".getType();" + NL + "\t\t\t\t\t\t\tif(\"id_BigDecimal\".equals(type_";
  protected final String TEXT_280 = ")){" + NL + "\t\t\t\t\t\t\t\tint precision_";
  protected final String TEXT_281 = " = metadata_";
  protected final String TEXT_282 = ".getPrecision();" + NL + "\t\t\t\t\t\t\t\tif(precision_";
  protected final String TEXT_283 = " !=0){" + NL + "\t\t\t\t\t\t\t\t\ttemp_";
  protected final String TEXT_284 = " = (new BigDecimal(temp_";
  protected final String TEXT_285 = ")).setScale(precision_";
  protected final String TEXT_286 = ",java.math.RoundingMode.HALF_UP).toPlainString();" + NL + "\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t}";
  protected final String TEXT_287 = "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\tif(\"id_BigDecimal\".equals(type_";
  protected final String TEXT_288 = ") || \"id_Short\".equals(type_";
  protected final String TEXT_289 = ") || \"id_Integer\".equals(type_";
  protected final String TEXT_290 = ") || \"id_Double\".equals(type_";
  protected final String TEXT_291 = ") || \"id_Float\".equals(type_";
  protected final String TEXT_292 = ") || \"id_Long\".equals(type_";
  protected final String TEXT_293 = ")){" + NL + "\t\t\t\t\t\t\t\ttemp_";
  protected final String TEXT_294 = " = FormatterUtils.format_Number(temp_";
  protected final String TEXT_295 = ", ";
  protected final String TEXT_296 = ", ";
  protected final String TEXT_297 = ");" + NL + "\t\t\t\t\t\t\t}";
  protected final String TEXT_298 = NL + "\t\t\t\t\t\t\tint columnLength_";
  protected final String TEXT_299 = " = metadata_";
  protected final String TEXT_300 = ".getLength();";
  protected final String TEXT_301 = NL + "    \t\t\t\t\t\t\tint temp_length_";
  protected final String TEXT_302 = " = temp_";
  protected final String TEXT_303 = ".getBytes(";
  protected final String TEXT_304 = ").length;";
  protected final String TEXT_305 = NL + "    \t\t\t\t\t\t\tint temp_length_";
  protected final String TEXT_306 = " = temp_";
  protected final String TEXT_307 = ".length();";
  protected final String TEXT_308 = NL + "\t\t\t\t\t\t\tif(temp_length_";
  protected final String TEXT_309 = " < columnLength_";
  protected final String TEXT_310 = "){";
  protected final String TEXT_311 = NL + "\t\t\t\t\t\t\t\t\tsb_";
  protected final String TEXT_312 = ".append(temp_";
  protected final String TEXT_313 = ");" + NL + "\t\t\t    \t\t\t\t\tfor(int j=0;j<columnLength_";
  protected final String TEXT_314 = "-temp_length_";
  protected final String TEXT_315 = ";j++){" + NL + "\t\t\t    \t\t\t\t\t\tsb_";
  protected final String TEXT_316 = ".append(";
  protected final String TEXT_317 = ");" + NL + "\t\t\t    \t\t\t\t\t}";
  protected final String TEXT_318 = NL + "\t\t\t    \t\t\t\t\tfor(int j=0;j<columnLength_";
  protected final String TEXT_319 = "-temp_length_";
  protected final String TEXT_320 = ";j++){" + NL + "\t\t\t    \t\t\t\t\t\tsb_";
  protected final String TEXT_321 = ".append(";
  protected final String TEXT_322 = ");" + NL + "\t\t\t    \t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\tsb_";
  protected final String TEXT_323 = ".append(temp_";
  protected final String TEXT_324 = ");";
  protected final String TEXT_325 = NL + "\t\t\t\t\t\t\t\t\tint tempNum_";
  protected final String TEXT_326 = " = columnLength_";
  protected final String TEXT_327 = "-temp_length_";
  protected final String TEXT_328 = ";" + NL + "\t\t\t\t\t\t\t\t\tfor(int j=0;j<tempNum_";
  protected final String TEXT_329 = "/2;j++){" + NL + "\t\t\t    \t\t\t\t\t\tsb_";
  protected final String TEXT_330 = ".append(";
  protected final String TEXT_331 = ");" + NL + "\t\t\t    \t\t\t\t\t}" + NL + "\t\t\t    \t\t\t\t\tsb_";
  protected final String TEXT_332 = ".append(temp_";
  protected final String TEXT_333 = ");" + NL + "\t\t\t\t\t\t\t\t\tfor(int j=0;j<tempNum_";
  protected final String TEXT_334 = "/2;j++){" + NL + "\t\t\t    \t\t\t\t\t\tsb_";
  protected final String TEXT_335 = ".append(";
  protected final String TEXT_336 = ");" + NL + "\t\t\t    \t\t\t\t\t}" + NL + "\t\t\t    \t\t\t\t\tif(tempNum_";
  protected final String TEXT_337 = "%2==1){" + NL + "\t\t\t    \t\t\t\t\t\tsb_";
  protected final String TEXT_338 = ".append(";
  protected final String TEXT_339 = ");" + NL + "\t\t\t    \t\t\t\t\t}";
  protected final String TEXT_340 = NL + "\t\t\t\t\t\t\t}else{";
  protected final String TEXT_341 = NL + "\t\t\t                        sb_";
  protected final String TEXT_342 = ".append(temp_";
  protected final String TEXT_343 = ");";
  protected final String TEXT_344 = NL + "\t\t\t                    \t\tbyteArray_";
  protected final String TEXT_345 = "=arrays_";
  protected final String TEXT_346 = ".copyOfRange(temp_";
  protected final String TEXT_347 = ".getBytes(";
  protected final String TEXT_348 = "),temp_length_";
  protected final String TEXT_349 = " - columnLength_";
  protected final String TEXT_350 = ",temp_length_";
  protected final String TEXT_351 = ");" + NL + "\t\t\t\t                    \tsb_";
  protected final String TEXT_352 = ".append(new String(byteArray_";
  protected final String TEXT_353 = ",";
  protected final String TEXT_354 = "));";
  protected final String TEXT_355 = NL + "\t\t\t    \t                    sb_";
  protected final String TEXT_356 = ".append(temp_";
  protected final String TEXT_357 = ".substring(temp_length_";
  protected final String TEXT_358 = "-columnLength_";
  protected final String TEXT_359 = "));";
  protected final String TEXT_360 = NL + "\t\t\t                        int begin";
  protected final String TEXT_361 = "=(temp_length_";
  protected final String TEXT_362 = "-columnLength_";
  protected final String TEXT_363 = ")/2;";
  protected final String TEXT_364 = NL + "\t\t\t\t\t\t\t\t\t\tbyteArray_";
  protected final String TEXT_365 = "=arrays_";
  protected final String TEXT_366 = ".copyOfRange(temp_";
  protected final String TEXT_367 = ".getBytes(";
  protected final String TEXT_368 = "),begin";
  protected final String TEXT_369 = ",begin";
  protected final String TEXT_370 = "+columnLength_";
  protected final String TEXT_371 = ");" + NL + "\t\t\t\t                    \tsb_";
  protected final String TEXT_372 = ".append(new String(byteArray_";
  protected final String TEXT_373 = ",";
  protected final String TEXT_374 = "));";
  protected final String TEXT_375 = NL + "\t\t\t    \t                    sb_";
  protected final String TEXT_376 = ".append(temp_";
  protected final String TEXT_377 = ".substring(begin";
  protected final String TEXT_378 = ", begin";
  protected final String TEXT_379 = "+columnLength_";
  protected final String TEXT_380 = "));";
  protected final String TEXT_381 = NL + "\t\t\t                    \t\tbyteArray_";
  protected final String TEXT_382 = "=arrays_";
  protected final String TEXT_383 = ".copyOfRange(temp_";
  protected final String TEXT_384 = ".getBytes(";
  protected final String TEXT_385 = "),0,columnLength_";
  protected final String TEXT_386 = ");" + NL + "\t\t\t                    \t\tsb_";
  protected final String TEXT_387 = ".append(new String(byteArray_";
  protected final String TEXT_388 = ",";
  protected final String TEXT_389 = "));";
  protected final String TEXT_390 = NL + "\t\t\t\t\t\t\t\t\t\tsb_";
  protected final String TEXT_391 = ".append(temp_";
  protected final String TEXT_392 = ".substring(0, columnLength_";
  protected final String TEXT_393 = "));";
  protected final String TEXT_394 = NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t}";
  protected final String TEXT_395 = NL + "\t\t\t\t//get  and format output String begin" + NL + "    \t\t\ttempStringM";
  protected final String TEXT_396 = "=";
  protected final String TEXT_397 = NL + "\t\t\t\t\tString.valueOf(";
  protected final String TEXT_398 = ".";
  protected final String TEXT_399 = ")";
  protected final String TEXT_400 = NL + "\t\t\t\t\t(";
  protected final String TEXT_401 = ".";
  protected final String TEXT_402 = " == null) ? " + NL + "\t\t\t\t\t\"\": ";
  protected final String TEXT_403 = "FormatterUtils.format_Date(";
  protected final String TEXT_404 = ".";
  protected final String TEXT_405 = ", ";
  protected final String TEXT_406 = ")";
  protected final String TEXT_407 = "java.nio.charset.Charset.defaultCharset().decode(java.nio.ByteBuffer.wrap(";
  protected final String TEXT_408 = ".";
  protected final String TEXT_409 = ")).toString()";
  protected final String TEXT_410 = ".";
  protected final String TEXT_411 = NL + "        \t\t\t\t\t\t\t";
  protected final String TEXT_412 = NL + "        \t\t\t\t\t\t\tFormatterUtils.format_Number(";
  protected final String TEXT_413 = ".toPlainString(), ";
  protected final String TEXT_414 = ", ";
  protected final String TEXT_415 = ")\t\t\t\t\t" + NL + "        \t\t\t\t\t\t\t";
  protected final String TEXT_416 = NL + "        \t\t\t\t\t\t\tFormatterUtils.format_Number(String.valueOf(";
  protected final String TEXT_417 = ".";
  protected final String TEXT_418 = "), ";
  protected final String TEXT_419 = ", ";
  protected final String TEXT_420 = ")\t\t\t\t\t\t" + NL + "        \t\t\t\t\t\t\t";
  protected final String TEXT_421 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_422 = ".toPlainString()\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_423 = "String.valueOf(";
  protected final String TEXT_424 = ".";
  protected final String TEXT_425 = ")";
  protected final String TEXT_426 = " ;" + NL + "\t\t\t\t";
  protected final String TEXT_427 = NL + "    \t\t\ttempLengthM";
  protected final String TEXT_428 = "=tempStringM";
  protected final String TEXT_429 = ".getBytes(";
  protected final String TEXT_430 = ").length;" + NL + "    \t\t\t";
  protected final String TEXT_431 = NL + "    \t\t\ttempLengthM";
  protected final String TEXT_432 = "=tempStringM";
  protected final String TEXT_433 = ".length();" + NL + "    \t\t\t";
  protected final String TEXT_434 = NL + "    \t\t\t" + NL + "            \tif (tempLengthM";
  protected final String TEXT_435 = " >=";
  protected final String TEXT_436 = ") {";
  protected final String TEXT_437 = NL + "                        sb_";
  protected final String TEXT_438 = ".append(tempStringM";
  protected final String TEXT_439 = ");";
  protected final String TEXT_440 = NL + "                    \t\tbyteArray_";
  protected final String TEXT_441 = "=arrays_";
  protected final String TEXT_442 = ".copyOfRange(tempStringM";
  protected final String TEXT_443 = ".getBytes(";
  protected final String TEXT_444 = "),tempLengthM";
  protected final String TEXT_445 = " - ";
  protected final String TEXT_446 = ",tempLengthM";
  protected final String TEXT_447 = ");" + NL + "\t                    \tsb_";
  protected final String TEXT_448 = ".append(new String(byteArray_";
  protected final String TEXT_449 = ",";
  protected final String TEXT_450 = "));";
  protected final String TEXT_451 = NL + "    \t                    sb_";
  protected final String TEXT_452 = ".append(tempStringM";
  protected final String TEXT_453 = ".substring(tempLengthM";
  protected final String TEXT_454 = "-";
  protected final String TEXT_455 = "));";
  protected final String TEXT_456 = NL + "                        int begin";
  protected final String TEXT_457 = "=(tempLengthM";
  protected final String TEXT_458 = "-";
  protected final String TEXT_459 = ")/2;";
  protected final String TEXT_460 = NL + "\t\t\t\t\t\t\tbyteArray_";
  protected final String TEXT_461 = "=arrays_";
  protected final String TEXT_462 = ".copyOfRange(tempStringM";
  protected final String TEXT_463 = ".getBytes(";
  protected final String TEXT_464 = "),begin";
  protected final String TEXT_465 = ",begin";
  protected final String TEXT_466 = "+";
  protected final String TEXT_467 = ");" + NL + "\t                    \tsb_";
  protected final String TEXT_468 = ".append(new String(byteArray_";
  protected final String TEXT_469 = ",";
  protected final String TEXT_470 = "));";
  protected final String TEXT_471 = NL + "    \t                    sb_";
  protected final String TEXT_472 = ".append(tempStringM";
  protected final String TEXT_473 = ".substring(begin";
  protected final String TEXT_474 = ", begin";
  protected final String TEXT_475 = "+";
  protected final String TEXT_476 = "));";
  protected final String TEXT_477 = NL + "                    \t\tbyteArray_";
  protected final String TEXT_478 = "=arrays_";
  protected final String TEXT_479 = ".copyOfRange(tempStringM";
  protected final String TEXT_480 = ".getBytes(";
  protected final String TEXT_481 = "),0,";
  protected final String TEXT_482 = ");" + NL + "                    \t\tsb_";
  protected final String TEXT_483 = ".append(new String(byteArray_";
  protected final String TEXT_484 = ",";
  protected final String TEXT_485 = "));";
  protected final String TEXT_486 = NL + "                    \t\tsb_";
  protected final String TEXT_487 = ".append(tempStringM";
  protected final String TEXT_488 = ".substring(0, ";
  protected final String TEXT_489 = "));";
  protected final String TEXT_490 = NL + "                }else if(tempLengthM";
  protected final String TEXT_491 = "<";
  protected final String TEXT_492 = "){" + NL + "                   ";
  protected final String TEXT_493 = NL + "                        sb_";
  protected final String TEXT_494 = ".append(tempStringM";
  protected final String TEXT_495 = ");" + NL + "                        for(int i_";
  protected final String TEXT_496 = "=0; i_";
  protected final String TEXT_497 = "< ";
  protected final String TEXT_498 = "-tempLengthM";
  protected final String TEXT_499 = "; i_";
  protected final String TEXT_500 = "++){" + NL + "                            sb_";
  protected final String TEXT_501 = ".append(";
  protected final String TEXT_502 = ");" + NL + "                        }" + NL + "                        ";
  protected final String TEXT_503 = NL + "                        for(int i_";
  protected final String TEXT_504 = "=0; i_";
  protected final String TEXT_505 = "< ";
  protected final String TEXT_506 = "-tempLengthM";
  protected final String TEXT_507 = "; i_";
  protected final String TEXT_508 = "++){" + NL + "                            sb_";
  protected final String TEXT_509 = ".append(";
  protected final String TEXT_510 = ");" + NL + "                        }" + NL + "                        sb_";
  protected final String TEXT_511 = ".append(tempStringM";
  protected final String TEXT_512 = ");" + NL + "                        ";
  protected final String TEXT_513 = NL + "                        int temp";
  protected final String TEXT_514 = "= (";
  protected final String TEXT_515 = "-tempLengthM";
  protected final String TEXT_516 = ")/2;" + NL + "                        for(int i_";
  protected final String TEXT_517 = "=0;i_";
  protected final String TEXT_518 = "<temp";
  protected final String TEXT_519 = ";i_";
  protected final String TEXT_520 = "++){" + NL + "                            sb_";
  protected final String TEXT_521 = ".append(";
  protected final String TEXT_522 = ");" + NL + "                        }" + NL + "                        sb_";
  protected final String TEXT_523 = ".append(tempStringM";
  protected final String TEXT_524 = ");" + NL + "                        for(int i=temp";
  protected final String TEXT_525 = "+tempLengthM";
  protected final String TEXT_526 = ";i<";
  protected final String TEXT_527 = ";i++){" + NL + "                            sb_";
  protected final String TEXT_528 = ".append(";
  protected final String TEXT_529 = ");" + NL + "                        }" + NL + "" + NL + "                        ";
  protected final String TEXT_530 = "       " + NL + "                }" + NL + "                //get  and format output String end\t\t\t\t" + NL + "\t\t\t";
  protected final String TEXT_531 = NL + "                 }";
  protected final String TEXT_532 = NL + "                   }";
  protected final String TEXT_533 = NL + "\t\t" + NL + "\t\t}" + NL + "\t\t" + NL + "\t\tPositionUtil_";
  protected final String TEXT_534 = " positionUtil_";
  protected final String TEXT_535 = "=new PositionUtil_";
  protected final String TEXT_536 = "();" + NL + "\t\t";
  protected final String TEXT_537 = NL + "\t\tString fileNewName_";
  protected final String TEXT_538 = " = ";
  protected final String TEXT_539 = ";" + NL + "\t\tjava.io.File createFile";
  protected final String TEXT_540 = " = new java.io.File(fileNewName_";
  protected final String TEXT_541 = ");";
  protected final String TEXT_542 = NL + "        //create directory only if not exists" + NL + "        java.io.File parentFile_";
  protected final String TEXT_543 = " = createFile";
  protected final String TEXT_544 = ".getParentFile();" + NL + "        if(parentFile_";
  protected final String TEXT_545 = " != null && !parentFile_";
  protected final String TEXT_546 = ".exists()) {" + NL + "            parentFile_";
  protected final String TEXT_547 = ".mkdirs();" + NL + "        }";
  protected final String TEXT_548 = NL + "        String fullName_";
  protected final String TEXT_549 = " = null;" + NL + "        String extension_";
  protected final String TEXT_550 = " = null;" + NL + "        String directory_";
  protected final String TEXT_551 = " = null;" + NL + "        if((fileNewName_";
  protected final String TEXT_552 = ".indexOf(\"/\") != -1)) {" + NL + "            if(fileNewName_";
  protected final String TEXT_553 = ".lastIndexOf(\".\") < fileNewName_";
  protected final String TEXT_554 = ".lastIndexOf(\"/\")) {" + NL + "                fullName_";
  protected final String TEXT_555 = " = fileNewName_";
  protected final String TEXT_556 = ";" + NL + "                extension_";
  protected final String TEXT_557 = " = \"\";" + NL + "            } else {" + NL + "                fullName_";
  protected final String TEXT_558 = " = fileNewName_";
  protected final String TEXT_559 = ".substring(0, fileNewName_";
  protected final String TEXT_560 = ".lastIndexOf(\".\"));" + NL + "                extension_";
  protected final String TEXT_561 = " = fileNewName_";
  protected final String TEXT_562 = ".substring(fileNewName_";
  protected final String TEXT_563 = ".lastIndexOf(\".\"));" + NL + "            }           " + NL + "            directory_";
  protected final String TEXT_564 = " = fileNewName_";
  protected final String TEXT_565 = ".substring(0, fileNewName_";
  protected final String TEXT_566 = ".lastIndexOf(\"/\"));            " + NL + "        } else {" + NL + "            if(fileNewName_";
  protected final String TEXT_567 = ".lastIndexOf(\".\") != -1) {" + NL + "                fullName_";
  protected final String TEXT_568 = " = fileNewName_";
  protected final String TEXT_569 = ".substring(0, fileNewName_";
  protected final String TEXT_570 = ".lastIndexOf(\".\"));" + NL + "                extension_";
  protected final String TEXT_571 = " = fileNewName_";
  protected final String TEXT_572 = ".substring(fileNewName_";
  protected final String TEXT_573 = ".lastIndexOf(\".\"));" + NL + "            } else {" + NL + "                fullName_";
  protected final String TEXT_574 = " = fileNewName_";
  protected final String TEXT_575 = ";" + NL + "                extension_";
  protected final String TEXT_576 = " = \"\";" + NL + "            }" + NL + "            directory_";
  protected final String TEXT_577 = " = \"\";" + NL + "        }" + NL + "\t\tString zipName_";
  protected final String TEXT_578 = " = fullName_";
  protected final String TEXT_579 = " + \".zip\";" + NL + "\t    java.util.zip.ZipOutputStream zipOut_";
  protected final String TEXT_580 = "=new java.util.zip.ZipOutputStream(" + NL + "\t    \t\t\tnew java.io.BufferedOutputStream(new java.io.FileOutputStream(zipName_";
  protected final String TEXT_581 = ")));" + NL + "\t    zipOut_";
  protected final String TEXT_582 = ".putNextEntry(new java.util.zip.ZipEntry(createFile";
  protected final String TEXT_583 = ".getName()));" + NL + "\t\tfinal ";
  protected final String TEXT_584 = " out";
  protected final String TEXT_585 = " = new ";
  protected final String TEXT_586 = "(new java.io.OutputStreamWriter(zipOut_";
  protected final String TEXT_587 = ",";
  protected final String TEXT_588 = "));";
  protected final String TEXT_589 = NL + "\t\tfinal ";
  protected final String TEXT_590 = " out";
  protected final String TEXT_591 = " = new ";
  protected final String TEXT_592 = "(new java.io.OutputStreamWriter(" + NL + "        \t\tnew java.io.FileOutputStream(";
  protected final String TEXT_593 = ", ";
  protected final String TEXT_594 = "),";
  protected final String TEXT_595 = "));";
  protected final String TEXT_596 = NL + "\t    java.util.zip.ZipOutputStream zipOut_";
  protected final String TEXT_597 = "=new java.util.zip.ZipOutputStream(" + NL + "\t    \t\t\tnew java.io.BufferedOutputStream(";
  protected final String TEXT_598 = "));" + NL + "\t    zipOut_";
  protected final String TEXT_599 = ".putNextEntry(new java.util.zip.ZipEntry(\"TalendOutputPositional\"));" + NL + "\t\tjava.io.OutputStreamWriter outWriter_";
  protected final String TEXT_600 = " = new java.io.OutputStreamWriter(zipOut_";
  protected final String TEXT_601 = ",";
  protected final String TEXT_602 = ");" + NL + "\t\tfinal ";
  protected final String TEXT_603 = " out";
  protected final String TEXT_604 = " = new ";
  protected final String TEXT_605 = "(outWriter_";
  protected final String TEXT_606 = ");";
  protected final String TEXT_607 = NL + "\t\tjava.io.OutputStreamWriter outWriter_";
  protected final String TEXT_608 = " = new java.io.OutputStreamWriter(";
  protected final String TEXT_609 = ",";
  protected final String TEXT_610 = ");" + NL + "\t\tfinal ";
  protected final String TEXT_611 = " out";
  protected final String TEXT_612 = " = new ";
  protected final String TEXT_613 = "(outWriter_";
  protected final String TEXT_614 = ");  ";
  protected final String TEXT_615 = NL + "\t\tif(createFile";
  protected final String TEXT_616 = ".length()==0){";
  protected final String TEXT_617 = NL + "    \t\t" + NL + "    \t\tString tempStringB";
  protected final String TEXT_618 = "=null;" + NL + "    \t\tint tempLengthB";
  protected final String TEXT_619 = "=0;";
  protected final String TEXT_620 = NL + "            positionUtil_";
  protected final String TEXT_621 = ".writeHeader_";
  protected final String TEXT_622 = "(tempStringB";
  protected final String TEXT_623 = ",tempLengthB";
  protected final String TEXT_624 = ",out";
  protected final String TEXT_625 = ",byteArray_";
  protected final String TEXT_626 = ",arrays_";
  protected final String TEXT_627 = ");";
  protected final String TEXT_628 = NL + "    \t\tout";
  protected final String TEXT_629 = ".write(";
  protected final String TEXT_630 = ");";
  protected final String TEXT_631 = NL + "    \t}";
  protected final String TEXT_632 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();

List<IMetadataTable> metadatas = node.getMetadataList();

 List<? extends IConnection> listInConns = node.getIncomingConnections();
    String sInConnName = null;
    IConnection inConn = null;
    List<IMetadataColumn> listInColumns = null;
    
    if (listInConns != null && listInConns.size() > 0) {
      IConnection inConnTemp = listInConns.get(0);
      sInConnName = inConnTemp.getName();
      if (inConnTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
      	inConn = inConnTemp;
        listInColumns = inConnTemp.getMetadataTable().getListColumns();
      }
	}
   
   String inConnName = null;
   
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
    
    	String cid = node.getUniqueName();
    	
    	String encoding = ElementParameterParser.getValue(node, "__ENCODING__");
        if (encoding!=null) {
            if (("").equals(encoding)) {
                encoding = "undef";
            }
        }
        
        String separator = ElementParameterParser.getValue(
            node,
            "__ROWSEPARATOR__"
        );
        
        String filename = ElementParameterParser.getValue(
            node,
            "__FILENAME__"
        );
		
		boolean useByte = ("true").equals(ElementParameterParser.getValue(node, "__USE_BYTE__"));
		
        boolean isIncludeHeader = ("true").equals(ElementParameterParser.getValue(node,"__INCLUDEHEADER__"));

        boolean isAppend = ("true").equals(ElementParameterParser.getValue(node,"__APPEND__"));
        
		boolean useStream = ("true").equals(ElementParameterParser.getValue(node,"__USESTREAM__"));
		String outStream = ElementParameterParser.getValue(node,"__STREAMNAME__");
        
        String advancedSeparatorStr = ElementParameterParser.getValue(node, "__ADVANCED_SEPARATOR__");
		boolean advancedSeparator = (advancedSeparatorStr!=null&&!("").equals(advancedSeparatorStr))?("true").equals(advancedSeparatorStr):false;
		String thousandsSeparator = ElementParameterParser.getValueWithJavaType(node, "__THOUSANDS_SEPARATOR__", JavaTypesManager.CHARACTER);
		String decimalSeparator = ElementParameterParser.getValueWithJavaType(node, "__DECIMAL_SEPARATOR__", JavaTypesManager.CHARACTER);        
 
        List<Map<String, String>> formats =
            (List<Map<String,String>>)ElementParameterParser.getObjectValue(
                node,
                "__FORMATS__"
            );
        
        boolean compress = ("true").equals(ElementParameterParser.getValue(node,"__COMPRESS__"));
        
        boolean isInRowMode = ("true").equals(ElementParameterParser.getValue(node,"__ROW_MODE__"));
        String writerClass = null;
    	if(isInRowMode){
    		writerClass = "routines.system.BufferedOutput";
    	}else{
    		writerClass = "java.io.BufferedWriter";
    	}
		boolean hasDynamic = metadata.isDynamicSchema();
		String dynamic = ElementParameterParser.getValue(node, "__DYNAMIC__");
		boolean useExistingDynamic = "true".equals(ElementParameterParser.getValue(node, "__USE_EXISTING_DYNAMIC__"));
        String dyn = dynamic+"_DYNAMIC";
		if(useExistingDynamic){

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
    class FindConnStartConn{
		IConnection findStartConn(IConnection conn){
			INode node = conn.getSource();
			if(node.isSubProcessStart() || !(NodeUtil.isDataAutoPropagated(node))){
				return conn;
			}
			List<? extends IConnection> listInConns = node.getIncomingConnections();
			IConnection inConnTemp = null;
			if (listInConns != null && listInConns.size() > 0) {
              inConnTemp = listInConns.get(0);
              if (inConnTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
                return findStartConn(inConnTemp);
              }
        	}
        	return null;
		}
	}
	if(inConn != null){
		FindConnStartConn finder = new FindConnStartConn();
    	IConnection startConn = finder.findStartConn(inConn);
    	if(startConn!=null){
    		inConnName = startConn.getName();
    	}
	}

    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    
		if(useByte){

    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    
		}

    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    
		    List<IMetadataColumn> columns = metadata.getListColumns();
    		int sizeColumns = columns.size();
    		if(isIncludeHeader){
    		for (int i = 0; i < sizeColumns; i++) {
    			IMetadataColumn column = columns.get(i);
    			Map<String, String> format=formats.get(i);
    			if(i%100==0){

    stringBuffer.append(TEXT_18);
    stringBuffer.append(i/100);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
     if(useByte){ 
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
     } 
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    
                }

    stringBuffer.append(TEXT_28);
    
				if("id_Dynamic".equals(column.getTalendType())){
					if(useExistingDynamic){

    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(dyn);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    
	    					if(useByte){

    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_44);
    
    						}else{

    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    
    						}

    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    
								if (("\'L\'").equals(format.get("ALIGN"))) {

    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_56);
    
								} else if (("\'R\'").equals(format.get("ALIGN"))) {

    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    
								} else {

    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_75);
    
								}

    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_78);
    
                    			if (("\'A\'").equals(format.get("KEEP"))) {
                    			} else if (("\'R\'").equals(format.get("KEEP"))) {
									if(useByte){

    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_89);
    
                        			}else{

    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_94);
    
									}
								} else if (("\'M\'").equals(format.get("KEEP"))) {

    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_98);
    
									if(useByte){

    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_102);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_109);
    
									}else{

    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_115);
    
	                    			}
                    			} else {
                    				if(useByte){

    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_123);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_124);
    
                    				}else{

    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_127);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_128);
    									}
                    			}

    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    
					}
				}else{//not dynamic begin

    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_134);
    if(useByte){
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_138);
    }else{
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    }
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_144);
    
                    if (("\'A\'").equals(format.get("KEEP"))) {
                    } else if (("\'R\'").equals(format.get("KEEP"))) {
						if(useByte){

    stringBuffer.append(TEXT_145);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_150);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_155);
    
                        }else{

    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_157);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_159);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_160);
    
						}
                    } else if (("\'M\'").equals(format.get("KEEP"))) {

    stringBuffer.append(TEXT_161);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_162);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_164);
    
						if(useByte){

    stringBuffer.append(TEXT_165);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_166);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_168);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_170);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_171);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_172);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_173);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_174);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_175);
    
						}else{

    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_178);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_179);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_180);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_181);
    
	                    }
                    } else {
                    	if(useByte){

    stringBuffer.append(TEXT_182);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_183);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_185);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_186);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_187);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_188);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_190);
    
                    	}else{

    stringBuffer.append(TEXT_191);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_192);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_193);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_194);
    						}
                    }

    stringBuffer.append(TEXT_195);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_196);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_197);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_198);
    
                    if (("\'L\'").equals(format.get("ALIGN"))) {

    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_201);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_203);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_204);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_205);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_210);
    
                    } else if (("\'R\'").equals(format.get("ALIGN"))) {

    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_213);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_214);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_215);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_216);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_217);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_218);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_219);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_220);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_221);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_222);
    
                    } else {

    stringBuffer.append(TEXT_223);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_224);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_227);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_228);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_232);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_233);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_236);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_237);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_239);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_240);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_241);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_242);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_243);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_244);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_245);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_246);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_247);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_248);
    
                    } 

    stringBuffer.append(TEXT_249);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_250);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_251);
    
			}//not dynamic end
	          if((i+1)%100==0){

    stringBuffer.append(TEXT_252);
    
              }
    	}
    		  if(sizeColumns>0&&(sizeColumns%100)>0){

    stringBuffer.append(TEXT_253);
    
              }
        }	

    
	  	List< ? extends IConnection> conns = node.getIncomingConnections();
	    for (IConnection conn : conns) {
		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
           for (int i = 0; i < sizeColumns; i++) {
				IMetadataColumn column = columns.get(i);
				Map<String,String> format=formats.get(i);
				JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
				String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
				if(i%100==0){

    stringBuffer.append(TEXT_254);
    stringBuffer.append(i/100);
    stringBuffer.append(TEXT_255);
    stringBuffer.append(inConnName!=null?inConnName:conn.getName() );
    stringBuffer.append(TEXT_256);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_257);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_259);
    stringBuffer.append(cid );
     if(useByte){ 
    stringBuffer.append(TEXT_260);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_261);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_262);
    stringBuffer.append(cid );
     } 
    stringBuffer.append(TEXT_263);
    
                }
                if("id_Dynamic".equals(column.getTalendType())){
					if(useExistingDynamic){

    stringBuffer.append(TEXT_264);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_265);
    stringBuffer.append(dyn);
    stringBuffer.append(TEXT_266);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_268);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_269);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_270);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_271);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_272);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_273);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_274);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_275);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_276);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_277);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_278);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_279);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_281);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_282);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_283);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_285);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_286);
    
						if(advancedSeparator){

    stringBuffer.append(TEXT_287);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_289);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_290);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_291);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_292);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_293);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_294);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_295);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_296);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_297);
    
						}

    stringBuffer.append(TEXT_298);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_299);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_300);
    
							if(useByte){

    stringBuffer.append(TEXT_301);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_302);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_303);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_304);
    
    						}else{

    stringBuffer.append(TEXT_305);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_306);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_307);
    
    						}

    stringBuffer.append(TEXT_308);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_309);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_310);
    
								if (("\'L\'").equals(format.get("ALIGN"))) {

    stringBuffer.append(TEXT_311);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_312);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_313);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_314);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_315);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_316);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_317);
    
								} else if (("\'R\'").equals(format.get("ALIGN"))) {

    stringBuffer.append(TEXT_318);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_319);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_320);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_321);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_322);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_323);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_324);
    
								} else {

    stringBuffer.append(TEXT_325);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_326);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_327);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_328);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_329);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_330);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_332);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_333);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_334);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_335);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_336);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_337);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_338);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_339);
    
								}

    stringBuffer.append(TEXT_340);
    
			                    if (("\'A\'").equals(format.get("KEEP"))) {

    stringBuffer.append(TEXT_341);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_342);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_343);
    
			                    } else if (("\'R\'").equals(format.get("KEEP"))) {
			                    	 if(useByte){

    stringBuffer.append(TEXT_344);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_345);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_346);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_347);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_348);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_349);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_350);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_351);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_352);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_353);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_354);
    
			                         }else{

    stringBuffer.append(TEXT_355);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_356);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_357);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_358);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_359);
    
			                    	 }
			                    } else if (("\'M\'").equals(format.get("KEEP"))) {

    stringBuffer.append(TEXT_360);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_361);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_362);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_363);
    
									if(useByte){

    stringBuffer.append(TEXT_364);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_365);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_366);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_367);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_368);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_369);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_370);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_371);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_372);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_373);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_374);
    
									}else{

    stringBuffer.append(TEXT_375);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_376);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_377);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_378);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_379);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_380);
    
			                    	}
			                    } else {
			                    	if(useByte){

    stringBuffer.append(TEXT_381);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_382);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_383);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_384);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_385);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_386);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_387);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_388);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_389);
    
			                    	}else{

    stringBuffer.append(TEXT_390);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_391);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_392);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_393);
    
									}
								}

    stringBuffer.append(TEXT_394);
    
					}
				}else{

    stringBuffer.append(TEXT_395);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_396);
    				
				if(JavaTypesManager.isJavaPrimitiveType( column.getTalendType(), column.isNullable()) ) {
    stringBuffer.append(TEXT_397);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_398);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_399);
    					
				} else {
    stringBuffer.append(TEXT_400);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_401);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_402);
    
					if(javaType == JavaTypesManager.DATE && patternValue!=null){
					
    stringBuffer.append(TEXT_403);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_404);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_405);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_406);
    
					}else if(javaType == JavaTypesManager.BYTE_ARRAY){
					
    stringBuffer.append(TEXT_407);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_408);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_409);
    
					}else if(javaType == JavaTypesManager.STRING){
					
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_410);
    stringBuffer.append(column.getLabel() );
    
					} else if(advancedSeparator && JavaTypesManager.isNumberType(javaType, column.isNullable())) { 
							
    stringBuffer.append(TEXT_411);
     if(javaType == JavaTypesManager.BIGDECIMAL) {
    stringBuffer.append(TEXT_412);
    stringBuffer.append(column.getPrecision() == null? conn.getName() + "." + column.getLabel() : conn.getName() + "." + column.getLabel() + ".setScale(" + column.getPrecision() + ", java.math.RoundingMode.HALF_UP)" );
    stringBuffer.append(TEXT_413);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_414);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_415);
     } else { 
    stringBuffer.append(TEXT_416);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_417);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_418);
    stringBuffer.append( thousandsSeparator );
    stringBuffer.append(TEXT_419);
    stringBuffer.append( decimalSeparator );
    stringBuffer.append(TEXT_420);
     } 
    stringBuffer.append(TEXT_421);
    
					}else if (javaType == JavaTypesManager.BIGDECIMAL) {
					
    stringBuffer.append(column.getPrecision() == null? conn.getName() + "." + column.getLabel() : conn.getName() + "." + column.getLabel() + ".setScale(" + column.getPrecision() + ", java.math.RoundingMode.HALF_UP)" );
    stringBuffer.append(TEXT_422);
     }else{
					
    stringBuffer.append(TEXT_423);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_424);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_425);
    
					}
				}
    stringBuffer.append(TEXT_426);
    if(useByte){
    stringBuffer.append(TEXT_427);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_428);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_429);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_430);
    }else{
    stringBuffer.append(TEXT_431);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_432);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_433);
    }
    stringBuffer.append(TEXT_434);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_435);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_436);
    
                    if (("\'A\'").equals(format.get("KEEP"))) {
    stringBuffer.append(TEXT_437);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_438);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_439);
    
                    } else if (("\'R\'").equals(format.get("KEEP"))) {
                    	 if(useByte){
    stringBuffer.append(TEXT_440);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_441);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_442);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_443);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_444);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_445);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_446);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_447);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_448);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_449);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_450);
    
                         }else{

    stringBuffer.append(TEXT_451);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_452);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_453);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_454);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_455);
    
                    	 }
                    } else if (("\'M\'").equals(format.get("KEEP"))) {

    stringBuffer.append(TEXT_456);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_457);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_458);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_459);
    
						if(useByte){

    stringBuffer.append(TEXT_460);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_461);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_462);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_463);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_464);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_465);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_466);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_467);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_468);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_469);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_470);
    
						}else{

    stringBuffer.append(TEXT_471);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_472);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_473);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_474);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_475);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_476);
    
                    	}
                    } else {
                    	if(useByte){

    stringBuffer.append(TEXT_477);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_478);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_479);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_480);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_481);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_482);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_483);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_484);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_485);
    
                    	}else{

    stringBuffer.append(TEXT_486);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_487);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_488);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_489);
    
                    	}
                    }
    stringBuffer.append(TEXT_490);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_491);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_492);
    
                    if (("\'L\'").equals(format.get("ALIGN"))) {
                    
    stringBuffer.append(TEXT_493);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_494);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_495);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_496);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_497);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_498);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_499);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_500);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_501);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_502);
    
                    } else if (("\'R\'").equals(format.get("ALIGN"))) {
    stringBuffer.append(TEXT_503);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_504);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_505);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_506);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_507);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_508);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_509);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_510);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_511);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_512);
    
                    } else {
    stringBuffer.append(TEXT_513);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_514);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_515);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_516);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_517);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_518);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_519);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_520);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_521);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_522);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_523);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_524);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_525);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_526);
    stringBuffer.append(format.get("SIZE"));
    stringBuffer.append(TEXT_527);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_528);
    stringBuffer.append(format.get("PADDING_CHAR"));
    stringBuffer.append(TEXT_529);
    
                    } 
    stringBuffer.append(TEXT_530);
    
				if((i+1)%100==0){

    stringBuffer.append(TEXT_531);
    
				}
				}//other columns (not dynamic)	end
			}
		}
                if(sizeColumns>0&&(sizeColumns%100)>0){

    stringBuffer.append(TEXT_532);
    
                 }
          }

    stringBuffer.append(TEXT_533);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_534);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_535);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_536);
    
		if(!useStream){// the part of file path

    stringBuffer.append(TEXT_537);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_538);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_539);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_540);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_541);
    
			if(("true").equals(ElementParameterParser.getValue(node,"__CREATE__"))){

    stringBuffer.append(TEXT_542);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_543);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_544);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_545);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_546);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_547);
    
			}
			if(compress && !isAppend){// compress the dest file

    stringBuffer.append(TEXT_548);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_549);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_550);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_551);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_552);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_553);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_554);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_555);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_556);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_557);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_558);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_559);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_560);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_561);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_562);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_563);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_564);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_565);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_566);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_567);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_568);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_569);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_570);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_571);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_572);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_573);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_574);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_575);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_576);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_577);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_578);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_579);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_580);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_581);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_582);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_583);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_584);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_585);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_586);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_587);
    stringBuffer.append( encoding);
    stringBuffer.append(TEXT_588);
    
			}else{

    stringBuffer.append(TEXT_589);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_590);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_591);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_592);
    stringBuffer.append(filename );
    stringBuffer.append(TEXT_593);
    stringBuffer.append( isAppend);
    stringBuffer.append(TEXT_594);
    stringBuffer.append( encoding);
    stringBuffer.append(TEXT_595);
    
			}
		}else{ //the part of the output stream
			if(compress && !isAppend){// compress the dest output stream

    stringBuffer.append(TEXT_596);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_597);
    stringBuffer.append(outStream );
    stringBuffer.append(TEXT_598);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_599);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_600);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_601);
    stringBuffer.append( encoding);
    stringBuffer.append(TEXT_602);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_603);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_604);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_605);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_606);
    
			}else{

    stringBuffer.append(TEXT_607);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_608);
    stringBuffer.append(outStream );
    stringBuffer.append(TEXT_609);
    stringBuffer.append( encoding);
    stringBuffer.append(TEXT_610);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_611);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_612);
    stringBuffer.append(writerClass );
    stringBuffer.append(TEXT_613);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_614);
    
			}
		}
		
		if(isIncludeHeader){      	
			if(!useStream){

    stringBuffer.append(TEXT_615);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_616);
    
			}
			

    stringBuffer.append(TEXT_617);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_618);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_619);
    
    		for (int i = 0; i < sizeColumns; i++) {
    			IMetadataColumn column = columns.get(i);
    			Map<String, String> format=formats.get(i);
    			if(i%100==0){

    stringBuffer.append(TEXT_620);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_621);
    stringBuffer.append(i/100);
    stringBuffer.append(TEXT_622);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_623);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_624);
    stringBuffer.append(cid );
     if(useByte){ 
    stringBuffer.append(TEXT_625);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_626);
    stringBuffer.append(cid );
     }
    stringBuffer.append(TEXT_627);
    
                }
            }

    stringBuffer.append(TEXT_628);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_629);
    stringBuffer.append(separator);
    stringBuffer.append(TEXT_630);
    
			if(!useStream){

    stringBuffer.append(TEXT_631);
    
			}
		}
    }
}

    stringBuffer.append(TEXT_632);
    return stringBuffer.toString();
  }
}
