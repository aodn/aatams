package org.talend.designer.codegen.translators.file.input;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import java.util.Map;

public class TFileInputLDIFBeginJava
{
  protected static String nl;
  public static synchronized TFileInputLDIFBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileInputLDIFBeginJava result = new TFileInputLDIFBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "///////////////////////////////////" + NL + "        netscape.ldap.util.LDIF ldif_";
  protected final String TEXT_2 = " = new netscape.ldap.util.LDIF(";
  protected final String TEXT_3 = ");" + NL + "" + NL + "        String[] filters_";
  protected final String TEXT_4 = " = new String[] {         ";
  protected final String TEXT_5 = " " + NL + "\t\t\"";
  protected final String TEXT_6 = "\",";
  protected final String TEXT_7 = "        " + NL + "        };" + NL + "        " + NL + "        String[] filters_binary_";
  protected final String TEXT_8 = " = new String[] {         ";
  protected final String TEXT_9 = " " + NL + "\t\t\"";
  protected final String TEXT_10 = ";binary\",";
  protected final String TEXT_11 = "        " + NL + "        };" + NL + "        " + NL + "        String[] results_";
  protected final String TEXT_12 = " = null;" + NL + "\t\tList<List<byte[]>> resultsBinary_";
  protected final String TEXT_13 = " = null;" + NL + "        int nb_line_";
  protected final String TEXT_14 = " = 0;" + NL + "        " + NL + "        ///////////////////////////////////" + NL + "" + NL + "        for (netscape.ldap.util.LDIFRecord record_";
  protected final String TEXT_15 = " = ldif_";
  protected final String TEXT_16 = ".nextRecord(); record_";
  protected final String TEXT_17 = " != null; record_";
  protected final String TEXT_18 = " = ldif_";
  protected final String TEXT_19 = ".nextRecord()) {" + NL + "" + NL + "            results_";
  protected final String TEXT_20 = " = new String[";
  protected final String TEXT_21 = "];" + NL + "            resultsBinary_";
  protected final String TEXT_22 = " =  new java.util.ArrayList<List<byte[]>>();" + NL + "            " + NL + "            netscape.ldap.util.LDIFContent content_";
  protected final String TEXT_23 = " = record_";
  protected final String TEXT_24 = ".getContent();" + NL + "\t\t\tnetscape.ldap.LDAPAttribute[] attributes_";
  protected final String TEXT_25 = " = null;" + NL + "\t\t\t" + NL + "            switch (content_";
  protected final String TEXT_26 = ".getType()) {" + NL + "            " + NL + "            case netscape.ldap.util.LDIFContent.ATTRIBUTE_CONTENT:" + NL + "            " + NL + "                netscape.ldap.util.LDIFAttributeContent attrContent_";
  protected final String TEXT_27 = " = (netscape.ldap.util.LDIFAttributeContent) content_";
  protected final String TEXT_28 = ";" + NL + "                attributes_";
  protected final String TEXT_29 = " = attrContent_";
  protected final String TEXT_30 = ".getAttributes();" + NL + "                " + NL + "                for (int i_";
  protected final String TEXT_31 = " = 0; i_";
  protected final String TEXT_32 = " < filters_";
  protected final String TEXT_33 = ".length; i_";
  protected final String TEXT_34 = "++) {" + NL + "                " + NL + "\t\t\t\t\tresultsBinary_";
  protected final String TEXT_35 = ".add(new java.util.ArrayList<byte[]>());" + NL + "" + NL + "                    for (int j_";
  protected final String TEXT_36 = " = 0; j_";
  protected final String TEXT_37 = " < attributes_";
  protected final String TEXT_38 = ".length; j_";
  protected final String TEXT_39 = "++) {" + NL + "                    " + NL + "                        netscape.ldap.LDAPAttribute attribute_";
  protected final String TEXT_40 = " = attributes_";
  protected final String TEXT_41 = "[j_";
  protected final String TEXT_42 = "];" + NL + "                        " + NL + "\t\t\t\t\t\tif(\"dn\".equalsIgnoreCase(filters_";
  protected final String TEXT_43 = "[i_";
  protected final String TEXT_44 = "])){" + NL + "\t\t\t\t\t\t\tresults_";
  protected final String TEXT_45 = "[i_";
  protected final String TEXT_46 = "] = record_";
  protected final String TEXT_47 = ".getDN();" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t}else{" + NL + "\t\t\t\t\t\t" + NL + "                            if (filters_";
  protected final String TEXT_48 = "[i_";
  protected final String TEXT_49 = "].equalsIgnoreCase(attribute_";
  protected final String TEXT_50 = ".getName())" + NL + "                            || filters_binary_";
  protected final String TEXT_51 = "[i_";
  protected final String TEXT_52 = "].equalsIgnoreCase(attribute_";
  protected final String TEXT_53 = ".getName())) {" + NL + "                            " + NL + "                                byte[][] values_";
  protected final String TEXT_54 = " = attribute_";
  protected final String TEXT_55 = ".getByteValueArray();";
  protected final String TEXT_56 = NL + "\t\t\t\t\t\t\t\t\t\tif(attribute_";
  protected final String TEXT_57 = ".getBaseName().equalsIgnoreCase(\"";
  protected final String TEXT_58 = "\")){" + NL + "\t\t\t\t\t\t\t\t\t\t\tfor(byte[] byteValue_";
  protected final String TEXT_59 = " : values_";
  protected final String TEXT_60 = ") {" + NL + "\t\t                           \t\t\t\tString value_";
  protected final String TEXT_61 = " = null;" + NL + "\t\t\t\t\t\t\t\t\t\t\t\tif(";
  protected final String TEXT_62 = "){" + NL + "\t\t                             \t   \t\t\tvalue_";
  protected final String TEXT_63 = " = new String(byteValue_";
  protected final String TEXT_64 = ",";
  protected final String TEXT_65 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t}else{" + NL + "                                \t\t\t\t\tvalue_";
  protected final String TEXT_66 = " = netscape.ldap.util.LDIF.toPrintableString(byteValue_";
  protected final String TEXT_67 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t \t\t\t\t\t\t\t\t\t\t\tresultsBinary_";
  protected final String TEXT_68 = ".get(i_";
  protected final String TEXT_69 = ").add(value_";
  protected final String TEXT_70 = ".getBytes(";
  protected final String TEXT_71 = "));" + NL + "                                \t\t\t\t\tresults_";
  protected final String TEXT_72 = "[i_";
  protected final String TEXT_73 = "] = results_";
  protected final String TEXT_74 = "[i_";
  protected final String TEXT_75 = "] == null ? value_";
  protected final String TEXT_76 = " : results_";
  protected final String TEXT_77 = "[i_";
  protected final String TEXT_78 = "] + ";
  protected final String TEXT_79 = " + value_";
  protected final String TEXT_80 = ";" + NL + "                                \t\t\t}" + NL + "                                \t\t}";
  protected final String TEXT_81 = NL + "\t\t\t\t\t\t\t\t\t\tfor(byte[] byteValue_";
  protected final String TEXT_82 = " : values_";
  protected final String TEXT_83 = ") {" + NL + "\t                           \t\t\t\tString value_";
  protected final String TEXT_84 = " = netscape.ldap.util.LDIF.toPrintableString(byteValue_";
  protected final String TEXT_85 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\tresultsBinary_";
  protected final String TEXT_86 = ".get(i_";
  protected final String TEXT_87 = ").add(value_";
  protected final String TEXT_88 = ".getBytes(";
  protected final String TEXT_89 = "));" + NL + "                           \t\t\t\t\tresults_";
  protected final String TEXT_90 = "[i_";
  protected final String TEXT_91 = "] = results_";
  protected final String TEXT_92 = "[i_";
  protected final String TEXT_93 = "] == null ? value_";
  protected final String TEXT_94 = " : results_";
  protected final String TEXT_95 = "[i_";
  protected final String TEXT_96 = "] + ";
  protected final String TEXT_97 = " + value_";
  protected final String TEXT_98 = ";" + NL + "                            \t\t\t}";
  protected final String TEXT_99 = NL + "                            }" + NL + "                        }" + NL + "                    }" + NL + "" + NL + "                }" + NL + "                " + NL + "                break;" + NL + "" + NL + "            case netscape.ldap.util.LDIFContent.ADD_CONTENT:" + NL + "            " + NL + "                netscape.ldap.util.LDIFAddContent addContent_";
  protected final String TEXT_100 = " = (netscape.ldap.util.LDIFAddContent) content_";
  protected final String TEXT_101 = ";" + NL + "                attributes_";
  protected final String TEXT_102 = " = addContent_";
  protected final String TEXT_103 = ".getAttributes();" + NL + "                " + NL + "                for (int i_";
  protected final String TEXT_104 = " = 0; i_";
  protected final String TEXT_105 = " < filters_";
  protected final String TEXT_106 = ".length; i_";
  protected final String TEXT_107 = "++) {" + NL + "                " + NL + "\t\t\t\t\tresultsBinary_";
  protected final String TEXT_108 = ".add(new java.util.ArrayList<byte[]>());" + NL + "" + NL + "                    for (int j_";
  protected final String TEXT_109 = " = 0; j_";
  protected final String TEXT_110 = " < attributes_";
  protected final String TEXT_111 = ".length; j_";
  protected final String TEXT_112 = "++) {" + NL + "                    " + NL + "                        netscape.ldap.LDAPAttribute attribute_";
  protected final String TEXT_113 = " = attributes_";
  protected final String TEXT_114 = "[j_";
  protected final String TEXT_115 = "];" + NL + "                        " + NL + "\t\t\t\t\t\tif(\"dn\".equalsIgnoreCase(filters_";
  protected final String TEXT_116 = "[i_";
  protected final String TEXT_117 = "])){" + NL + "\t\t\t\t\t\t\tresults_";
  protected final String TEXT_118 = "[i_";
  protected final String TEXT_119 = "] = record_";
  protected final String TEXT_120 = ".getDN();" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t}else if(\"changetype\".equalsIgnoreCase(filters_";
  protected final String TEXT_121 = "[i_";
  protected final String TEXT_122 = "])){" + NL + "\t\t\t\t\t\t\tresults_";
  protected final String TEXT_123 = "[i_";
  protected final String TEXT_124 = "] = \"add\";" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t}else{" + NL + "\t\t\t\t\t\t" + NL + "                            if (filters_";
  protected final String TEXT_125 = "[i_";
  protected final String TEXT_126 = "].equalsIgnoreCase(attribute_";
  protected final String TEXT_127 = ".getName())" + NL + "                            || filters_binary_";
  protected final String TEXT_128 = "[i_";
  protected final String TEXT_129 = "].equalsIgnoreCase(attribute_";
  protected final String TEXT_130 = ".getName())) {" + NL + "                            " + NL + "                                byte[][] values_";
  protected final String TEXT_131 = " = attribute_";
  protected final String TEXT_132 = ".getByteValueArray();";
  protected final String TEXT_133 = NL + "\t\t\t\t\t\t\t\t\t\tif(attribute_";
  protected final String TEXT_134 = ".getBaseName().equalsIgnoreCase(\"";
  protected final String TEXT_135 = "\")){" + NL + "\t\t\t\t\t\t\t\t\t\t\tfor(byte[] byteValue_";
  protected final String TEXT_136 = " : values_";
  protected final String TEXT_137 = ") {" + NL + "\t\t                           \t\t\t\tString value_";
  protected final String TEXT_138 = " = null;" + NL + "\t\t\t\t\t\t\t\t\t\t\t\tif(";
  protected final String TEXT_139 = "){" + NL + "\t\t                             \t\t   \t\tvalue_";
  protected final String TEXT_140 = " = new String(byteValue_";
  protected final String TEXT_141 = ",";
  protected final String TEXT_142 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t}else{" + NL + "                                \t\t\t\t\tvalue_";
  protected final String TEXT_143 = " = netscape.ldap.util.LDIF.toPrintableString(byteValue_";
  protected final String TEXT_144 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\tresultsBinary_";
  protected final String TEXT_145 = ".get(i_";
  protected final String TEXT_146 = ").add(value_";
  protected final String TEXT_147 = ".getBytes(";
  protected final String TEXT_148 = "));" + NL + "                                \t\t\t\t\tresults_";
  protected final String TEXT_149 = "[i_";
  protected final String TEXT_150 = "] = results_";
  protected final String TEXT_151 = "[i_";
  protected final String TEXT_152 = "] == null ? value_";
  protected final String TEXT_153 = " : results_";
  protected final String TEXT_154 = "[i_";
  protected final String TEXT_155 = "] + ";
  protected final String TEXT_156 = " + value_";
  protected final String TEXT_157 = ";" + NL + "                                \t\t\t}" + NL + "                                \t\t}";
  protected final String TEXT_158 = NL + "\t\t\t\t\t\t\t\t\t\tfor(byte[] byteValue_";
  protected final String TEXT_159 = " : values_";
  protected final String TEXT_160 = ") {" + NL + "\t    \t                       \t\t\tString value_";
  protected final String TEXT_161 = " = netscape.ldap.util.LDIF.toPrintableString(byteValue_";
  protected final String TEXT_162 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\tresultsBinary_";
  protected final String TEXT_163 = ".get(i_";
  protected final String TEXT_164 = ").add(value_";
  protected final String TEXT_165 = ".getBytes(";
  protected final String TEXT_166 = "));" + NL + "            \t                \t\t\tresults_";
  protected final String TEXT_167 = "[i_";
  protected final String TEXT_168 = "] = results_";
  protected final String TEXT_169 = "[i_";
  protected final String TEXT_170 = "] == null ? value_";
  protected final String TEXT_171 = " : results_";
  protected final String TEXT_172 = "[i_";
  protected final String TEXT_173 = "] + ";
  protected final String TEXT_174 = " + value_";
  protected final String TEXT_175 = ";" + NL + "                \t            \t\t}";
  protected final String TEXT_176 = NL + "                            }" + NL + "                        }" + NL + "                    }" + NL + "" + NL + "                }                " + NL + "                break;" + NL + "                " + NL + "            case netscape.ldap.util.LDIFContent.MODIFICATION_CONTENT:" + NL + "" + NL + "\t\t\t\t\tnetscape.ldap.util.LDIFModifyContent modifyContent_";
  protected final String TEXT_177 = " = (netscape.ldap.util.LDIFModifyContent) content_";
  protected final String TEXT_178 = ";" + NL + "\t\t\t\t\tnetscape.ldap.LDAPModification[] modifications_";
  protected final String TEXT_179 = " = modifyContent_";
  protected final String TEXT_180 = ".getModifications();" + NL + "" + NL + "\t\t\t\t\tfor (int i_";
  protected final String TEXT_181 = " = 0; i_";
  protected final String TEXT_182 = " < filters_";
  protected final String TEXT_183 = ".length; i_";
  protected final String TEXT_184 = "++) {" + NL + "\t\t\t\t\t" + NL + "\t\t\t\t\t\tresultsBinary_";
  protected final String TEXT_185 = ".add(new java.util.ArrayList<byte[]>());" + NL + "" + NL + "\t\t\t\t\t\tfor (netscape.ldap.LDAPModification modification_";
  protected final String TEXT_186 = " : modifications_";
  protected final String TEXT_187 = ") {" + NL + "\t\t\t\t\t\t\tnetscape.ldap.LDAPAttribute attribute_";
  protected final String TEXT_188 = " = modification_";
  protected final String TEXT_189 = ".getAttribute();" + NL + "" + NL + "\t\t\t\t\t\t\tif (\"dn\".equalsIgnoreCase(filters_";
  protected final String TEXT_190 = "[i_";
  protected final String TEXT_191 = "])) {" + NL + "\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_192 = "[i_";
  protected final String TEXT_193 = "] = record_";
  protected final String TEXT_194 = ".getDN();" + NL + "" + NL + "\t\t\t\t\t\t\t} else if (\"changetype\".equalsIgnoreCase(filters_";
  protected final String TEXT_195 = "[i_";
  protected final String TEXT_196 = "])) {" + NL + "\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_197 = "[i_";
  protected final String TEXT_198 = "] = \"modify\";" + NL + "" + NL + "\t\t\t\t\t\t\t} else {" + NL + "" + NL + "\t\t\t\t\t\t\t\tif (filters_";
  protected final String TEXT_199 = "[i_";
  protected final String TEXT_200 = "].equalsIgnoreCase(attribute_";
  protected final String TEXT_201 = ".getName())" + NL + "\t\t\t\t\t\t\t\t|| filters_binary_";
  protected final String TEXT_202 = "[i_";
  protected final String TEXT_203 = "].equalsIgnoreCase(attribute_";
  protected final String TEXT_204 = ".getName())) {" + NL + "" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_205 = "\t\t\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t\tint op_";
  protected final String TEXT_206 = " = modification_";
  protected final String TEXT_207 = ".getOp();" + NL + "\t\t\t\t\t\t\t\t\tswitch(op_";
  protected final String TEXT_208 = "){" + NL + "\t\t\t\t\t\t\t\t\t\tcase netscape.ldap.LDAPModification.ADD: " + NL + "\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_209 = "[i_";
  protected final String TEXT_210 = "] = \"add\";" + NL + "\t\t\t\t\t\t\t\t\t\t\tbreak;" + NL + "\t\t\t\t\t\t\t\t\t\tcase netscape.ldap.LDAPModification.DELETE: " + NL + "\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_211 = "[i_";
  protected final String TEXT_212 = "] = \"delete\";" + NL + "\t\t\t\t\t\t\t\t\t\t\tbreak;" + NL + "\t\t\t\t\t\t\t\t\t\tcase netscape.ldap.LDAPModification.REPLACE:" + NL + "\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_213 = "[i_";
  protected final String TEXT_214 = "] = \"replace\";" + NL + "\t\t\t\t\t\t\t\t\t\t\tbreak;" + NL + "\t\t\t\t\t\t\t\t\t\tdefault:" + NL + "\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_215 = "[i_";
  protected final String TEXT_216 = "] = \"\";" + NL + "\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_217 = "\t\t\t\t\t\t\t\t\t" + NL + "" + NL + "\t\t\t\t\t\t\t\t\tbyte[][] values_";
  protected final String TEXT_218 = " = attribute_";
  protected final String TEXT_219 = ".getByteValueArray();" + NL + "                                " + NL + "                                \tboolean firstLoop_";
  protected final String TEXT_220 = " = true;";
  protected final String TEXT_221 = NL + "\t\t\t\t\t\t\t\t\t\t\tif(attribute_";
  protected final String TEXT_222 = ".getBaseName().equalsIgnoreCase(\"";
  protected final String TEXT_223 = "\")){" + NL + "\t\t\t                          \t \t\tfor(byte[] byteValue_";
  protected final String TEXT_224 = " : values_";
  protected final String TEXT_225 = ") {" + NL + "\t\t\t\t                        \t  \t \tString value_";
  protected final String TEXT_226 = " = null;" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\tif(";
  protected final String TEXT_227 = "){" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\t\tvalue_";
  protected final String TEXT_228 = " = new String(byteValue_";
  protected final String TEXT_229 = ",";
  protected final String TEXT_230 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\t}else{" + NL + "\t\t        \t                   \t    \t\t\tvalue_";
  protected final String TEXT_231 = " = netscape.ldap.util.LDIF.toPrintableString(byteValue_";
  protected final String TEXT_232 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\tresultsBinary_";
  protected final String TEXT_233 = ".get(i_";
  protected final String TEXT_234 = ").add(value_";
  protected final String TEXT_235 = ".getBytes(";
  protected final String TEXT_236 = "));" + NL + "\t                                \t\t\t\tif(firstLoop_";
  protected final String TEXT_237 = "){" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_238 = "[i_";
  protected final String TEXT_239 = "] = results_";
  protected final String TEXT_240 = "[i_";
  protected final String TEXT_241 = "] == null ? value_";
  protected final String TEXT_242 = " : results_";
  protected final String TEXT_243 = "[i_";
  protected final String TEXT_244 = "] + \":\" + value_";
  protected final String TEXT_245 = ";" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\t}else{" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_246 = "[i_";
  protected final String TEXT_247 = "] = results_";
  protected final String TEXT_248 = "[i_";
  protected final String TEXT_249 = "] + ";
  protected final String TEXT_250 = " + value_";
  protected final String TEXT_251 = ";" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t\tfirstLoop_";
  protected final String TEXT_252 = " = false;" + NL + "\t                          \t\t\t\t \t}" + NL + "                                \t\t\t}";
  protected final String TEXT_253 = NL + "\t                          \t \t\tfor(byte[] byteValue_";
  protected final String TEXT_254 = " : values_";
  protected final String TEXT_255 = ") {" + NL + "\t\t                        \t  \t \tString value_";
  protected final String TEXT_256 = " = netscape.ldap.util.LDIF.toPrintableString(byteValue_";
  protected final String TEXT_257 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\tresultsBinary_";
  protected final String TEXT_258 = ".get(i_";
  protected final String TEXT_259 = ").add(value_";
  protected final String TEXT_260 = ".getBytes(";
  protected final String TEXT_261 = "));" + NL + "                            \t\t\t\tif(firstLoop_";
  protected final String TEXT_262 = "){" + NL + "\t\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_263 = "[i_";
  protected final String TEXT_264 = "] = results_";
  protected final String TEXT_265 = "[i_";
  protected final String TEXT_266 = "] == null ? value_";
  protected final String TEXT_267 = " : results_";
  protected final String TEXT_268 = "[i_";
  protected final String TEXT_269 = "] + \":\" + value_";
  protected final String TEXT_270 = ";" + NL + "\t\t\t\t\t\t\t\t\t\t\t}else{" + NL + "\t\t\t\t\t\t\t\t\t\t\t\tresults_";
  protected final String TEXT_271 = "[i_";
  protected final String TEXT_272 = "] = results_";
  protected final String TEXT_273 = "[i_";
  protected final String TEXT_274 = "] + ";
  protected final String TEXT_275 = " + value_";
  protected final String TEXT_276 = ";" + NL + "\t\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t\tfirstLoop_";
  protected final String TEXT_277 = " = false;" + NL + "                      \t\t\t\t\t }";
  protected final String TEXT_278 = NL + NL + "\t\t\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t}" + NL + "" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t}                " + NL + "                break;" + NL + "                " + NL + "            case netscape.ldap.util.LDIFContent.DELETE_CONTENT:" + NL + "                //netscape.ldap.util.LDIFDeleteContent deleteContent_";
  protected final String TEXT_279 = " = (netscape.ldap.util.LDIFDeleteContent) content_";
  protected final String TEXT_280 = ";" + NL + "                for (int i_";
  protected final String TEXT_281 = " = 0; i_";
  protected final String TEXT_282 = " < filters_";
  protected final String TEXT_283 = ".length; i_";
  protected final String TEXT_284 = "++) {" + NL + "\t\t\t\t\tif(\"dn\".equalsIgnoreCase(filters_";
  protected final String TEXT_285 = "[i_";
  protected final String TEXT_286 = "])){" + NL + "\t\t\t\t\t\tresults_";
  protected final String TEXT_287 = "[i_";
  protected final String TEXT_288 = "] = record_";
  protected final String TEXT_289 = ".getDN();\t\t\t\t\t\t" + NL + "\t\t\t\t\t}else if(\"changetype\".equalsIgnoreCase(filters_";
  protected final String TEXT_290 = "[i_";
  protected final String TEXT_291 = "])){" + NL + "\t\t\t\t\t\tresults_";
  protected final String TEXT_292 = "[i_";
  protected final String TEXT_293 = "] = \"delete\";" + NL + "\t\t\t\t\t}                " + NL + "                }                " + NL + "                break;" + NL + "                " + NL + "\t\t\tcase netscape.ldap.util.LDIFContent.MODDN_CONTENT:" + NL + "\t\t\t\tnetscape.ldap.util.LDIFModDNContent moddnContent_";
  protected final String TEXT_294 = " = (netscape.ldap.util.LDIFModDNContent) content_";
  protected final String TEXT_295 = ";" + NL + "                for (int i_";
  protected final String TEXT_296 = " = 0; i_";
  protected final String TEXT_297 = " < filters_";
  protected final String TEXT_298 = ".length; i_";
  protected final String TEXT_299 = "++) {" + NL + "\t\t\t\t\tif(\"dn\".equalsIgnoreCase(filters_";
  protected final String TEXT_300 = "[i_";
  protected final String TEXT_301 = "])){" + NL + "\t\t\t\t\t\tresults_";
  protected final String TEXT_302 = "[i_";
  protected final String TEXT_303 = "] = record_";
  protected final String TEXT_304 = ".getDN();\t\t\t\t\t\t" + NL + "\t\t\t\t\t}else if(\"changetype\".equalsIgnoreCase(filters_";
  protected final String TEXT_305 = "[i_";
  protected final String TEXT_306 = "])){" + NL + "\t\t\t\t\t\tresults_";
  protected final String TEXT_307 = "[i_";
  protected final String TEXT_308 = "] = \"modrdn\";" + NL + "\t\t\t\t\t}else if(\"newrdn\".equalsIgnoreCase(filters_";
  protected final String TEXT_309 = "[i_";
  protected final String TEXT_310 = "])){" + NL + "\t\t\t\t\t\tresults_";
  protected final String TEXT_311 = "[i_";
  protected final String TEXT_312 = "] = moddnContent_";
  protected final String TEXT_313 = ".getRDN();" + NL + "\t\t\t\t\t}else if(\"deleteoldrdn\".equalsIgnoreCase(filters_";
  protected final String TEXT_314 = "[i_";
  protected final String TEXT_315 = "])){" + NL + "\t\t\t\t\t\tresults_";
  protected final String TEXT_316 = "[i_";
  protected final String TEXT_317 = "] = Boolean.toString(moddnContent_";
  protected final String TEXT_318 = ".getDeleteOldRDN());" + NL + "\t\t\t\t\t}else if(\"newsuperior\".equalsIgnoreCase(filters_";
  protected final String TEXT_319 = "[i_";
  protected final String TEXT_320 = "])){" + NL + "\t\t\t\t\t\tresults_";
  protected final String TEXT_321 = "[i_";
  protected final String TEXT_322 = "] = moddnContent_";
  protected final String TEXT_323 = ".getNewParent();" + NL + "\t\t\t\t\t}\t\t\t\t\t                " + NL + "                }" + NL + "\t\t\t\tbreak;" + NL + "\t\t\tdefault:                " + NL + "            }" + NL + "            nb_line_";
  protected final String TEXT_324 = "++;" + NL + "            " + NL + "            " + NL + "// for output";
  protected final String TEXT_325 = NL + "    \t\t";
  protected final String TEXT_326 = " = null;\t\t\t";
  protected final String TEXT_327 = NL + NL + "\t\t\tboolean whetherReject_";
  protected final String TEXT_328 = " = false;" + NL + "\t\t\t";
  protected final String TEXT_329 = " = new ";
  protected final String TEXT_330 = "Struct();" + NL + "\t\t\ttry {\t\t\t";
  protected final String TEXT_331 = NL + "\t\t\t\t\t\t" + NL + "\t\t\t" + NL + "\t\t\tif(";
  protected final String TEXT_332 = " < results_";
  protected final String TEXT_333 = ".length && results_";
  protected final String TEXT_334 = "[";
  protected final String TEXT_335 = "]!=null ){\t\t\t\t";
  protected final String TEXT_336 = NL + "\t\t\t\t\t";
  protected final String TEXT_337 = ".";
  protected final String TEXT_338 = " = results_";
  protected final String TEXT_339 = "[";
  protected final String TEXT_340 = "];";
  protected final String TEXT_341 = NL + "\t\t\t\t\t";
  protected final String TEXT_342 = ".";
  protected final String TEXT_343 = " = ParserUtils.parseTo_Date(results_";
  protected final String TEXT_344 = "[";
  protected final String TEXT_345 = "], ";
  protected final String TEXT_346 = ");";
  protected final String TEXT_347 = NL + "\t\t\t\t\t";
  protected final String TEXT_348 = ".";
  protected final String TEXT_349 = " = resultsBinary_";
  protected final String TEXT_350 = ".get(";
  protected final String TEXT_351 = ").get(0);";
  protected final String TEXT_352 = "\t\t\t\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_353 = ".";
  protected final String TEXT_354 = " = resultsBinary_";
  protected final String TEXT_355 = ".get(";
  protected final String TEXT_356 = ");";
  protected final String TEXT_357 = "\t\t\t\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_358 = ".";
  protected final String TEXT_359 = " = ParserUtils.parseTo_";
  protected final String TEXT_360 = "(results_";
  protected final String TEXT_361 = "[";
  protected final String TEXT_362 = "]);";
  protected final String TEXT_363 = NL + "\t\t\t" + NL + "\t\t\t} else { " + NL + "\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_364 = ".";
  protected final String TEXT_365 = " = ";
  protected final String TEXT_366 = ";" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\t";
  protected final String TEXT_367 = NL + "\t\t\t";
  protected final String TEXT_368 = " ";
  protected final String TEXT_369 = " = null; ";
  protected final String TEXT_370 = "\t\t\t" + NL + "\t\t\t" + NL + "    } catch (Exception e) {" + NL + "        whetherReject_";
  protected final String TEXT_371 = " = true;";
  protected final String TEXT_372 = NL + "            throw(e);";
  protected final String TEXT_373 = NL + "                    ";
  protected final String TEXT_374 = " = new ";
  protected final String TEXT_375 = "Struct();";
  protected final String TEXT_376 = NL + "                    ";
  protected final String TEXT_377 = ".";
  protected final String TEXT_378 = " = ";
  protected final String TEXT_379 = ".";
  protected final String TEXT_380 = ";";
  protected final String TEXT_381 = NL + "                ";
  protected final String TEXT_382 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_383 = ";";
  protected final String TEXT_384 = NL + "                ";
  protected final String TEXT_385 = " = null;";
  protected final String TEXT_386 = NL + "                System.err.println(e.getMessage());";
  protected final String TEXT_387 = NL + "                ";
  protected final String TEXT_388 = " = null;";
  protected final String TEXT_389 = NL + "            \t";
  protected final String TEXT_390 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_391 = ";";
  protected final String TEXT_392 = NL + "    }" + NL + "\t\t\t";
  protected final String TEXT_393 = NL + "\t\t";
  protected final String TEXT_394 = "if(!whetherReject_";
  protected final String TEXT_395 = ") { ";
  protected final String TEXT_396 = "      " + NL + "             if(";
  protected final String TEXT_397 = " == null){ " + NL + "            \t ";
  protected final String TEXT_398 = " = new ";
  protected final String TEXT_399 = "Struct();" + NL + "             }\t\t\t\t";
  protected final String TEXT_400 = NL + "\t    \t ";
  protected final String TEXT_401 = ".";
  protected final String TEXT_402 = " = ";
  protected final String TEXT_403 = ".";
  protected final String TEXT_404 = ";    \t\t\t\t";
  protected final String TEXT_405 = NL + "\t\t";
  protected final String TEXT_406 = " } ";
  protected final String TEXT_407 = "\t";
  protected final String TEXT_408 = NL + "///////////////////////////////////        ";
  protected final String TEXT_409 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
boolean useFieldOptions = ("true").equals(ElementParameterParser.getValue(node, "__USE_FIELD_OPTIONS__"));

List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
        // component id
        String cid = node.getUniqueName();
        String filename = ElementParameterParser.getValue(node,"__FILENAME__"); 
        String encoding = ElementParameterParser.getValue(node,"__ENCODING__");
    	String dieOnErrorStr = ElementParameterParser.getValue(node, "__DIE_ON_ERROR__");
		boolean dieOnError = (dieOnErrorStr!=null&&!("").equals(dieOnErrorStr))?("true").equals(dieOnErrorStr):false;               
        List<IMetadataColumn> listColumns = metadata.getListColumns();
        
        String addprefixStr = ElementParameterParser.getValue(node, "__ADDPREFIX__");
        boolean addprefix = (addprefixStr!=null&&!("").equals(addprefixStr))?("true").equals(addprefixStr):false;
        String valueSeparator = ElementParameterParser.getValue(node, "__VALUE_SEPARATOR__");
		List<Map<String, String>> textEncodingColumns = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__TEXTENCODING__");
		boolean isBinary = false;
		boolean isBase64 = false;

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(filename );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    
        for (IMetadataColumn column: listColumns) {

    stringBuffer.append(TEXT_5);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_6);
    
        }

    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    
        for (IMetadataColumn column: listColumns) {//fix bug TDI17707

    stringBuffer.append(TEXT_9);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_10);
    
        }

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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(listColumns.size() );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
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
    stringBuffer.append(cid );
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    
								if(useFieldOptions){
									for(Map<String, String> line:textEncodingColumns){// search in the configuration table
										String columnName = line.get("SCHEMA_COLUMN");
										isBase64 = "true".equals(line.get("BASE64"));

    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_61);
    stringBuffer.append(isBase64);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(valueSeparator);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    
	                                	isBase64 = false;
	                               	}
	                           	}else{

    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_88);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(valueSeparator);
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    
	                           	}

    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_102);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid );
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_132);
    
								if(useFieldOptions){
									for(Map<String, String> line:textEncodingColumns){// search in the configuration table
										String columnName = line.get("SCHEMA_COLUMN");
										isBase64 = "true".equals(line.get("BASE64"));

    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_134);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_138);
    stringBuffer.append(isBase64);
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_141);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_143);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_145);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_150);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(valueSeparator);
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_157);
    
                            			isBase64 = false;
                            		}
                           	 	}else{

    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_159);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_162);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_164);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_166);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_168);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_170);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_171);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_172);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_173);
    stringBuffer.append(valueSeparator);
    stringBuffer.append(TEXT_174);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_175);
    
                    	        }

    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_178);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_179);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_180);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_182);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_183);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_185);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_186);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_187);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_188);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_190);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_191);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_192);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_193);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_194);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_195);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_196);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_197);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_198);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_201);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_203);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_204);
    if(addprefix){
    stringBuffer.append(TEXT_205);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_210);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_213);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_214);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_215);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_216);
    }
    stringBuffer.append(TEXT_217);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_218);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_219);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_220);
    
									if(useFieldOptions){
										for(Map<String, String> line:textEncodingColumns){// search in the configuration table
											String columnName = line.get("SCHEMA_COLUMN");
											isBase64 = "true".equals(line.get("BASE64"));

    stringBuffer.append(TEXT_221);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_222);
    stringBuffer.append(columnName);
    stringBuffer.append(TEXT_223);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_224);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_226);
    stringBuffer.append(isBase64);
    stringBuffer.append(TEXT_227);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_228);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_229);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_232);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_233);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_235);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_236);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_237);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_239);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_240);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_241);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_242);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_243);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_244);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_245);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_247);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_248);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_249);
    stringBuffer.append(valueSeparator);
    stringBuffer.append(TEXT_250);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_251);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_252);
    
											isBase64 = false;
										}
                            		}else{

    stringBuffer.append(TEXT_253);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_254);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_255);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_256);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_257);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_259);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_260);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_261);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_262);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_263);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_264);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_265);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_266);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_268);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_269);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_270);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_271);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_272);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_273);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_274);
    stringBuffer.append(valueSeparator);
    stringBuffer.append(TEXT_275);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_276);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_277);
    
                            		}

    stringBuffer.append(TEXT_278);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_279);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_281);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_282);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_283);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_285);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_286);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_287);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_289);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_290);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_291);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_292);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_293);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_294);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_295);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_296);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_297);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_298);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_299);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_300);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_301);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_302);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_303);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_304);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_305);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_306);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_307);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_308);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_309);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_310);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_311);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_312);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_313);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_314);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_315);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_316);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_317);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_318);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_319);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_320);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_321);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_322);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_323);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_324);
    
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

    stringBuffer.append(TEXT_325);
    stringBuffer.append(connTemp.getName() );
    stringBuffer.append(TEXT_326);
    
    				}
    			}
    		}
    	}
    	
	String firstConnName = "";
	if (conns!=null) {//1
		if (conns.size()>0) {//2
		
			IConnection conn = conns.get(0); //the first connection
			firstConnName = conn.getName();		
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//3

				
    stringBuffer.append(TEXT_327);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_328);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_329);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_330);
    
			List<IMetadataColumn> columns=metadata.getListColumns();
			int columnSize = columns.size();
			for (int i=0;i<columnSize;i++) {//4
					IMetadataColumn column=columns.get(i);
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
			
    stringBuffer.append(TEXT_331);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_332);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_333);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_334);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_335);
    
					if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) { //String or Object

    stringBuffer.append(TEXT_336);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_337);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_338);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_339);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_340);
    
					} else if(javaType == JavaTypesManager.DATE) { //Date

    stringBuffer.append(TEXT_341);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_342);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_343);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_344);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_345);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_346);
    
					} else if(javaType == JavaTypesManager.BYTE_ARRAY) { //byte[]

    stringBuffer.append(TEXT_347);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_348);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_349);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_350);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_351);
    
					} else if(javaType == JavaTypesManager.LIST)  { // List<byte[]>

    stringBuffer.append(TEXT_352);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_353);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_354);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_355);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_356);
    
					} else  { //other

    stringBuffer.append(TEXT_357);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_358);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_359);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_360);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_361);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_362);
    
					}

    stringBuffer.append(TEXT_363);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_364);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_365);
    stringBuffer.append(JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate));
    stringBuffer.append(TEXT_366);
    			
			} //4
    stringBuffer.append(TEXT_367);
    if(rejectConnName.equals(firstConnName)) {
    stringBuffer.append(TEXT_368);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_369);
    }
    stringBuffer.append(TEXT_370);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_371);
    
        if (dieOnError) {
            
    stringBuffer.append(TEXT_372);
    
        } else {
            if(!("").equals(rejectConnName)&&!rejectConnName.equals(firstConnName)&&rejectColumnList != null && rejectColumnList.size() > 0) {

                
    stringBuffer.append(TEXT_373);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_374);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_375);
    
                for(IMetadataColumn column : metadata.getListColumns()) {
                    
    stringBuffer.append(TEXT_376);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_377);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_378);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_379);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_380);
    
                }
                
    stringBuffer.append(TEXT_381);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_382);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_383);
    stringBuffer.append(TEXT_384);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_385);
    
            } else if(("").equals(rejectConnName)){
                
    stringBuffer.append(TEXT_386);
    stringBuffer.append(TEXT_387);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_388);
    
            } else if(rejectConnName.equals(firstConnName)){
    stringBuffer.append(TEXT_389);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_390);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_391);
    }
        } 
        
    stringBuffer.append(TEXT_392);
    
		}//3
		
		
		if (conns.size()>0) {	
			boolean isFirstEnter = true;
			for (int i=0;i<conns.size();i++) {
				conn = conns.get(i);
				if ((conn.getName().compareTo(firstConnName)!=0)&&(conn.getName().compareTo(rejectConnName)!=0)&&(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA))) {

    stringBuffer.append(TEXT_393);
     if(isFirstEnter) {
    stringBuffer.append(TEXT_394);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_395);
     isFirstEnter = false; } 
    stringBuffer.append(TEXT_396);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_397);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_398);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_399);
    
			    	 for (IMetadataColumn column: metadata.getListColumns()) {

    stringBuffer.append(TEXT_400);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_401);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_402);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_403);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_404);
    
				 	}
				}
			}

    stringBuffer.append(TEXT_405);
     if(!isFirstEnter) {
    stringBuffer.append(TEXT_406);
     } 
    stringBuffer.append(TEXT_407);
    
		}
		
		
	}//2
	
}//1


    stringBuffer.append(TEXT_408);
    
  }
}  
 
    stringBuffer.append(TEXT_409);
    return stringBuffer.toString();
  }
}
