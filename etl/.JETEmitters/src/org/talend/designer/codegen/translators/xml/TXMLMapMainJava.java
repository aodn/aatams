package org.talend.designer.codegen.translators.xml;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.designer.xmlmap.XmlMapComponent;
import org.talend.designer.xmlmap.model.emf.xmlmap.XmlMapData;
import org.eclipse.emf.common.util.EList;
import org.talend.designer.xmlmap.generation.GenerationManagerFactory;
import org.talend.designer.xmlmap.generation.GenerationManager;
import org.talend.core.model.process.BlockCode;
import org.talend.designer.xmlmap.generation.HashedMetadataTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.VarTable;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputXmlTree;
import org.talend.designer.xmlmap.model.emf.xmlmap.TreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.OutputTreeNode;
import org.talend.designer.xmlmap.model.emf.xmlmap.Connection;
import org.talend.designer.xmlmap.model.emf.xmlmap.LookupConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.FilterConnection;
import org.talend.designer.xmlmap.model.emf.xmlmap.NodeType;
import org.talend.designer.xmlmap.model.emf.xmlmap.InputLoopNodesTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.utils.NodeUtil;
import org.talend.core.model.metadata.IMetadataColumn;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;

/**
 * add by wliu
 */
public class TXMLMapMainJava {

  protected static String nl;
  public static synchronized TXMLMapMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TXMLMapMainJava result = new TXMLMapMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL;
  protected final String TEXT_3 = NL + "\tboolean ";
  protected final String TEXT_4 = " = false;" + NL + "\tboolean ";
  protected final String TEXT_5 = " = false;" + NL + "\tboolean ";
  protected final String TEXT_6 = " = false;" + NL + "\tboolean isMatchDocRow";
  protected final String TEXT_7 = " = false;" + NL + "\t  " + NL + "\t";
  protected final String TEXT_8 = NL + "\t\t\t\t    GenerateDocument_";
  protected final String TEXT_9 = " gen_Doc_";
  protected final String TEXT_10 = "_";
  protected final String TEXT_11 = " = new GenerateDocument_";
  protected final String TEXT_12 = "();" + NL + "\t\t\t\t\t";
  protected final String TEXT_13 = "_tmp.";
  protected final String TEXT_14 = " = null;" + NL + "\t\t\t\t\t";
  protected final String TEXT_15 = NL + "\t\t";
  protected final String TEXT_16 = NL + "\t\t\t\tString xPathLoop_";
  protected final String TEXT_17 = " = \"";
  protected final String TEXT_18 = "\";" + NL + "\t\t\t\torg.dom4j.Document doc_";
  protected final String TEXT_19 = " = ";
  protected final String TEXT_20 = ".";
  protected final String TEXT_21 = ".getDocument(); " + NL + "\t\t\t\t";
  protected final String TEXT_22 = NL + "\t\t\t\t\t//old version, find NS from doc" + NL + "\t\t\t\t\tnsTool_";
  protected final String TEXT_23 = ".countNSMap(doc_";
  protected final String TEXT_24 = ".getRootElement());" + NL + "\t\t\t\t\tjava.util.HashMap<String,String> xmlNameSpaceMap_";
  protected final String TEXT_25 = " = nsTool_";
  protected final String TEXT_26 = ".xmlNameSpaceMap;" + NL + "\t\t\t\t\torg.dom4j.XPath x_";
  protected final String TEXT_27 = " = doc_";
  protected final String TEXT_28 = ".createXPath(nsTool_";
  protected final String TEXT_29 = ".addDefaultNSPrefix(xPathLoop_";
  protected final String TEXT_30 = ",xPathLoop_";
  protected final String TEXT_31 = "));  " + NL + "\t\t\t\t\tx_";
  protected final String TEXT_32 = ".setNamespaceURIs(xmlNameSpaceMap_";
  protected final String TEXT_33 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_34 = NL + "\t\t\t\t\tjava.util.HashMap<String,String> xmlNameSpaceMap_";
  protected final String TEXT_35 = " = new java.util.HashMap<String,String>();" + NL + "\t\t\t\t\t";
  protected final String TEXT_36 = NL + "\t\t\t\t\torg.dom4j.XPath x_";
  protected final String TEXT_37 = " = doc_";
  protected final String TEXT_38 = ".createXPath(xPathLoop_";
  protected final String TEXT_39 = ");" + NL + "\t\t\t\t\tx_";
  protected final String TEXT_40 = ".setNamespaceURIs(xmlNameSpaceMap_";
  protected final String TEXT_41 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_42 = NL + NL + "\t\t\t    java.util.List<org.dom4j.tree.AbstractNode> nodeList_";
  protected final String TEXT_43 = " = (java.util.List<org.dom4j.tree.AbstractNode>)x_";
  protected final String TEXT_44 = ".selectNodes(doc_";
  protected final String TEXT_45 = ");\t" + NL + "\t\t\t    String str_";
  protected final String TEXT_46 = " = null;" + NL + "\t\t\t    org.dom4j.Node node_";
  protected final String TEXT_47 = " = null;" + NL + "" + NL + "\t\t\t\tfor (org.dom4j.tree.AbstractNode temp_";
  protected final String TEXT_48 = ": nodeList_";
  protected final String TEXT_49 = ") { // G_TXM_M_001" + NL + "\t\t\t    \tnb_line_";
  protected final String TEXT_50 = "++;" + NL + "\t\t\t    \t";
  protected final String TEXT_51 = " = false;" + NL + "\t\t\t    \t";
  protected final String TEXT_52 = " = false;" + NL + "\t\t\t\t\t";
  protected final String TEXT_53 = "=false;" + NL + "\t\t\t\t    isMatchDocRow";
  protected final String TEXT_54 = " = false;" + NL + "\t\t\t\t\t" + NL + "\t\t\t    \ttreeNodeAPI_";
  protected final String TEXT_55 = ".clear();" + NL + "\t\t\t\t\t";
  protected final String TEXT_56 = NL + "\t\t\t\t\t\torg.dom4j.XPath xTmp";
  protected final String TEXT_57 = "_";
  protected final String TEXT_58 = " = temp_";
  protected final String TEXT_59 = ".createXPath(nsTool_";
  protected final String TEXT_60 = ".addDefaultNSPrefix(\"";
  protected final String TEXT_61 = "\",xPathLoop_";
  protected final String TEXT_62 = "));" + NL + "\t\t\t\t\t    ";
  protected final String TEXT_63 = NL + "\t\t\t\t\t   \torg.dom4j.XPath xTmp";
  protected final String TEXT_64 = "_";
  protected final String TEXT_65 = " = temp_";
  protected final String TEXT_66 = ".createXPath(\"";
  protected final String TEXT_67 = "\");" + NL + "\t\t\t\t\t    ";
  protected final String TEXT_68 = NL + "\t\t\t\t\t    xTmp";
  protected final String TEXT_69 = "_";
  protected final String TEXT_70 = ".setNamespaceURIs(xmlNameSpaceMap_";
  protected final String TEXT_71 = ");" + NL + "\t\t\t\t\t    Object obj";
  protected final String TEXT_72 = "_";
  protected final String TEXT_73 = " = xTmp";
  protected final String TEXT_74 = "_";
  protected final String TEXT_75 = ".evaluate(temp_";
  protected final String TEXT_76 = ");" + NL + "\t\t\t\t\t    if(obj";
  protected final String TEXT_77 = "_";
  protected final String TEXT_78 = " instanceof String || obj";
  protected final String TEXT_79 = "_";
  protected final String TEXT_80 = " instanceof Number){" + NL + "\t\t\t\t\t    \tstr_";
  protected final String TEXT_81 = " = String.valueOf(obj";
  protected final String TEXT_82 = "_";
  protected final String TEXT_83 = ");" + NL + "\t\t\t\t\t    \ttreeNodeAPI_";
  protected final String TEXT_84 = ".put(\"";
  protected final String TEXT_85 = "\", str_";
  protected final String TEXT_86 = ");" + NL + "\t\t\t\t\t    }else{" + NL + "\t\t\t\t\t    \tnode_";
  protected final String TEXT_87 = " = xTmp";
  protected final String TEXT_88 = "_";
  protected final String TEXT_89 = ".selectSingleNode(temp_";
  protected final String TEXT_90 = ");" + NL + "\t\t\t\t\t    \ttreeNodeAPI_";
  protected final String TEXT_91 = ".put(\"";
  protected final String TEXT_92 = "\", node_";
  protected final String TEXT_93 = "!=null ? xTmp";
  protected final String TEXT_94 = "_";
  protected final String TEXT_95 = ".valueOf(temp_";
  protected final String TEXT_96 = ") : null);" + NL + "\t\t\t\t\t    }" + NL + "\t\t\t\t\t";
  protected final String TEXT_97 = NL;
  protected final String TEXT_98 = NL + NL + "\t\t";
  protected final String TEXT_99 = "\t";
  protected final String TEXT_100 = NL + "\t\t\t\t///////////////////////////////////////////////" + NL + "\t\t\t\t//Starting Lookup Table \"";
  protected final String TEXT_101 = "\" " + NL + "\t\t\t\t///////////////////////////////////////////////" + NL + "\t\t\t\t//tHash_Lookup_";
  protected final String TEXT_102 = ".initGet();" + NL + "\t\t\t\t";
  protected final String TEXT_103 = NL + "\t\t\t\t\t\tjava.util.HashMap<String,String> xmlNameSpaceMap_";
  protected final String TEXT_104 = " = new java.util.HashMap<String,String>();" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_105 = NL + "\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_106 = "HashKey.documentLookupResult.clear();" + NL + "\t\t\t\t\t\tjava.util.Map<String,String> xpathToPatternMap_";
  protected final String TEXT_107 = " = new java.util.HashMap<String,String>();" + NL + "\t\t\t\t\t\tjava.util.Map<String,String> xpathToTypeMap_";
  protected final String TEXT_108 = " = new java.util.HashMap<String,String>();" + NL + "\t\t\t\t    ";
  protected final String TEXT_109 = NL + "\t\t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_110 = NL + "\t\t\t\t\tboolean forceLoop";
  protected final String TEXT_111 = " = false;" + NL + "\t\t\t\t\t";
  protected final String TEXT_112 = " ";
  protected final String TEXT_113 = "ObjectFromLookup = null;" + NL + "\t\t\t\t\tboolean hasResultFromLookupCache";
  protected final String TEXT_114 = " = false;" + NL + "\t\t\t\t\tjava.util.Map<String,Object> cacheResult";
  protected final String TEXT_115 = " = new java.util.HashMap<String,Object>();" + NL + "\t\t\t\t";
  protected final String TEXT_116 = NL + "\t\t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_117 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_118 = "HashKey = new ";
  protected final String TEXT_119 = "Struct() {" + NL + "\t\t\t\t\t\t\t{" + NL + "\t\t\t\t\t\t\t\tthis.loopKey = \"";
  protected final String TEXT_120 = "\";" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\tpublic int hashCode() {" + NL + "\t\t\t\t\t\t\t\tif(this.hashCodeDirty){" + NL + "\t\t\t\t\t\t\t\t\tfinal int prime = PRIME;" + NL + "\t\t\t\t\t\t\t\t\tint result = DEFAULT_HASHCODE;" + NL + "\t\t\t\t\t";
  protected final String TEXT_121 = NL + "\t\t\t\t        \t\t\tresult = prime * result + (this.";
  protected final String TEXT_122 = " ? 1231 : 1237);" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_123 = NL + "\t\t\t\t\t\t\t\t\tresult = prime * result + (int) this.";
  protected final String TEXT_124 = ";" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_125 = NL + "\t\t\t\t\t\t\t\tresult = prime * result + java.util.Arrays.hashCode(this.";
  protected final String TEXT_126 = ");" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_127 = NL + "\t\t\t\t\t\t\t\tresult = prime * result + ((this.";
  protected final String TEXT_128 = " == null) ? 0 : this.";
  protected final String TEXT_129 = ".hashCode());" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_130 = NL + "\t\t\t\t\t\t\t\t\tresult = prime * result + ((this.loopKey == null) ? 0 : this.loopKey.hashCode());" + NL + "\t\t\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t\tthis.hashCode = result;" + NL + "    \t\t\t\t\t\t\t\tthis.hashCodeDirty = false;\t" + NL + "\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\treturn this.hashCode;" + NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\tpublic boolean equals(Object obj) {" + NL + "\t\t\t\t\t\t\t\tif (this == obj) return true;" + NL + "\t\t\t\t\t\t\t\tif (obj == null) return false;" + NL + "\t\t\t\t\t\t\t\tif(!(obj instanceof ";
  protected final String TEXT_131 = "Struct)) return false;" + NL + "\t\t\t\t\t\t\t\tfinal ";
  protected final String TEXT_132 = "Struct other = (";
  protected final String TEXT_133 = "Struct) obj;" + NL + "\t\t\t\t\t\t\t\tif(!this.loopKey.equals(other.loopKey)) return false;";
  protected final String TEXT_134 = NL + "\t\t\t\t\t\t\t\t\t\tif (this.";
  protected final String TEXT_135 = " != other.";
  protected final String TEXT_136 = ") " + NL + "\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_137 = NL + "\t\t\t\t\t\t\t\t\t\tif(!java.util.Arrays.equals(this.";
  protected final String TEXT_138 = ", other.";
  protected final String TEXT_139 = ")) {" + NL + "\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_140 = NL + "\t\t\t\t\t\t\t\t\t\tif (this.";
  protected final String TEXT_141 = " == null) {" + NL + "\t\t\t\t\t\t\t\t\t\t\tif (other.";
  protected final String TEXT_142 = " != null) " + NL + "\t\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t\t} else if (!this.";
  protected final String TEXT_143 = ".equals(other.";
  protected final String TEXT_144 = ")) " + NL + "\t\t\t\t\t\t\t\t\t\t\treturn false;" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_145 = NL + "\t\t\t\t\t\t\t\treturn true;" + NL + "\t\t\t\t\t\t\t} " + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t};" + NL + "\t\t\t\t\t";
  protected final String TEXT_146 = NL + "\t\t   \t\t\t\tList<Object> lookupCacheKey";
  protected final String TEXT_147 = " = new java.util.ArrayList<Object>();" + NL + "\t\t   \t\t\t";
  protected final String TEXT_148 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_149 = "HashKey.documentLookupResult.put(\"";
  protected final String TEXT_150 = "\",";
  protected final String TEXT_151 = ");" + NL + "\t\t\t\t\t\t\txpathToPatternMap_";
  protected final String TEXT_152 = ".put(\"";
  protected final String TEXT_153 = "\",";
  protected final String TEXT_154 = ");" + NL + "\t\t\t\t\t\t\txpathToTypeMap_";
  protected final String TEXT_155 = ".put(\"";
  protected final String TEXT_156 = "\",\"";
  protected final String TEXT_157 = "\");" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_158 = NL + "\t\t\t\t\t\t\t\tlookupCacheKey";
  protected final String TEXT_159 = ".add(";
  protected final String TEXT_160 = ");" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_161 = NL + "\t\t\t\t\t   \t";
  protected final String TEXT_162 = "HashKey.";
  protected final String TEXT_163 = " = ";
  protected final String TEXT_164 = ";" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_165 = "HashKey.hashCodeDirty = true;" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_166 = NL + "\t\t\t\t\t\tlookupCacheKey";
  protected final String TEXT_167 = ".add(";
  protected final String TEXT_168 = "HashKey);" + NL + "\t\t\t\t\t";
  protected final String TEXT_169 = NL + "\t\t\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_170 = NL + "\t\t\t\t\t\tif(!";
  protected final String TEXT_171 = "){//TD120" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_172 = NL + "\t\t\t\t\t\t\t\t\ttHash_Lookup_Cache_";
  protected final String TEXT_173 = ".lookup(lookupCacheKey";
  protected final String TEXT_174 = ");" + NL + "\t\t  \t\t\t\t\t\t\tif(tHash_Lookup_Cache_";
  protected final String TEXT_175 = ".hasNext()) {" + NL + "\t  \t\t\t\t\t\t\t\t\thasResultFromLookupCache";
  protected final String TEXT_176 = " = true;" + NL + "\t\t  \t\t\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_177 = NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_178 = "Process(globalMap);" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_179 = NL + "\t\t\t\t\t\t\t\t\ttHash_Lookup_";
  protected final String TEXT_180 = " = " + NL + "\t\t\t\t\t\t\t\t\t\t(org.talend.designer.components.lookup.persistent.Persistent";
  protected final String TEXT_181 = "LookupManager<";
  protected final String TEXT_182 = "Struct>) " + NL + "\t\t\t\t\t\t\t\t\t\t\t((org.talend.designer.components.lookup.persistent.Persistent";
  protected final String TEXT_183 = "LookupManager<";
  protected final String TEXT_184 = "Struct>) " + NL + "\t\t\t\t\t\t\t\t\t\t\t\t globalMap.get( \"tHash_Lookup_";
  protected final String TEXT_185 = "\" ));" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_186 = NL + "\t\t\t\t\t\t\t\t\ttHash_Lookup_";
  protected final String TEXT_187 = " = " + NL + "\t\t\t\t\t\t\t\t\t(org.talend.designer.components.lookup.memory.AdvancedMemoryLookup<";
  protected final String TEXT_188 = "Struct>) " + NL + "\t\t\t\t\t\t\t\t\t\t((org.talend.designer.components.lookup.memory.AdvancedMemoryLookup<";
  protected final String TEXT_189 = "Struct>) " + NL + "\t\t\t\t\t\t\t\t\t\t\tglobalMap.get( \"tHash_Lookup_";
  protected final String TEXT_190 = "\" ));" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_191 = NL + "\t\t\t\t\t    \t\t\ttHash_Lookup_";
  protected final String TEXT_192 = ".initGet();" + NL + "\t\t\t\t\t    \t\t";
  protected final String TEXT_193 = NL + "\t\t\t\t\t    \t\t\ttHash_Lookup_";
  protected final String TEXT_194 = ".lookup( ";
  protected final String TEXT_195 = "HashKey );" + NL + "\t\t\t\t\t    \t\t";
  protected final String TEXT_196 = NL + "\t\t\t\t\t    \t\t\ttHash_Lookup_";
  protected final String TEXT_197 = ".lookup();" + NL + "\t\t\t\t\t    \t\t";
  protected final String TEXT_198 = NL + "\t\t\t\t\t\t    \t\t}" + NL + "\t\t\t\t\t\t\t    ";
  protected final String TEXT_199 = NL + "\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_200 = NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_201 = NL + "\t\t\t\t\t\t\t\t\ttHash_Lookup_";
  protected final String TEXT_202 = ".lookup( ";
  protected final String TEXT_203 = "HashKey );" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_204 = NL + "\t\t\t\t\t\t\t\t\ttHash_Lookup_";
  protected final String TEXT_205 = ".lookup();" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_206 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_207 = NL + "\t\t\t\t\t\t\tif(hasResultFromLookupCache";
  protected final String TEXT_208 = "  || tHash_Lookup_";
  protected final String TEXT_209 = ".hasNext()){" + NL + "\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_210 = NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_211 = " = true;" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_212 = " = true;" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_213 = NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_214 = " = ";
  protected final String TEXT_215 = "Default;" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_216 = NL + "\t\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_217 = NL + "\t\t\t\t\t\t\t\tforceLoop";
  protected final String TEXT_218 = " = true;" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_219 = NL + "\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t}//TD120" + NL + "\t\t\t\t   " + NL + "\t\t\t\t   \t\t";
  protected final String TEXT_220 = NL + "\t\t\t\t   \t\t\tif(";
  protected final String TEXT_221 = ") {" + NL + "\t\t\t\t   \t\t\t\tforceLoop";
  protected final String TEXT_222 = " = true;" + NL + "\t\t\t\t   \t\t\t}" + NL + "\t\t\t\t   \t\t";
  protected final String TEXT_223 = NL + "\t\t\t\t   " + NL + "\t\t\t\t   \t\t";
  protected final String TEXT_224 = "Struct fromLookup_";
  protected final String TEXT_225 = " = null;" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_226 = " = ";
  protected final String TEXT_227 = "Default;" + NL + "\t\t\t\t   " + NL + "\t\t\t\t   \t\t";
  protected final String TEXT_228 = NL + "\t\t\t\t\t    if(hasResultFromLookupCache";
  protected final String TEXT_229 = ") {//TD10086" + NL + "\t\t\t\t\t    \t";
  protected final String TEXT_230 = NL + "\t\t\t\t\t    \tjava.util.Map<String,Object> cacheResult_";
  protected final String TEXT_231 = " = tHash_Lookup_Cache_";
  protected final String TEXT_232 = ".next();" + NL + "\t\t\t\t\t    \t\t";
  protected final String TEXT_233 = NL + "\t\t\t\t\t    \t\t\ttreeNodeAPI_";
  protected final String TEXT_234 = ".put(\"";
  protected final String TEXT_235 = "\",StringUtils.valueOf(cacheResult_";
  protected final String TEXT_236 = ".get(\"";
  protected final String TEXT_237 = "\")));" + NL + "\t\t\t\t\t    \t\t";
  protected final String TEXT_238 = NL + "\t\t\t\t    \t\t\t";
  protected final String TEXT_239 = " = (";
  protected final String TEXT_240 = ")cacheResult_";
  protected final String TEXT_241 = ".get(\"";
  protected final String TEXT_242 = "\");" + NL + "\t\t\t\t\t    \t";
  protected final String TEXT_243 = NL + "\t\t\t\t\t    } else if(!";
  protected final String TEXT_244 = ") {" + NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_245 = NL + "\t\t\t\t\t\t   \tisMatchDocRow";
  protected final String TEXT_246 = " = false;" + NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_247 = NL + "\t\t\t\t\t\t\twhile (tHash_Lookup_";
  protected final String TEXT_248 = ".hasNext()) {//TD119" + NL + "\t\t\t\t\t\t\t\tfromLookup_";
  protected final String TEXT_249 = " = null;" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_250 = " = ";
  protected final String TEXT_251 = "Default;" + NL + "\t\t\t\t\t\t\t\tfromLookup_";
  protected final String TEXT_252 = " = tHash_Lookup_";
  protected final String TEXT_253 = ".next();" + NL + "\t\t\t\t\t\t\t\tif (fromLookup_";
  protected final String TEXT_254 = " != null) {" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_255 = NL + "\t\t\t\t\t\t\t\t\t//begin to lookup Document Object (case 1:lookup doc key exists),(case 2:lookup doc key not exists but lookup line doc output exist)" + NL + "\t\t\t\t\t\t\t\t\tDocument lookupDoc_";
  protected final String TEXT_256 = " = fromLookup_";
  protected final String TEXT_257 = ".";
  protected final String TEXT_258 = ";" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_259 = NL + "\t\t\t\t\t\t\t\t\tboolean quit";
  protected final String TEXT_260 = " = false;" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_261 = NL + "\t\t\t\t\t\t\t\t\tjava.util.Map<String,String> xPathAsOutput_";
  protected final String TEXT_262 = " = new java.util.HashMap<String,String>();" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_263 = NL + "\t\t\t\t\t\t\t\t\txPathAsOutput_";
  protected final String TEXT_264 = ".put(\"";
  protected final String TEXT_265 = "\",\"";
  protected final String TEXT_266 = "\");" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_267 = NL + "\t\t\t\t\t\t\t\t\tList<java.util.Map<String,Object>> result_";
  protected final String TEXT_268 = " = lookupDoc_";
  protected final String TEXT_269 = ".LookupDocument(\"";
  protected final String TEXT_270 = "\",";
  protected final String TEXT_271 = ",";
  protected final String TEXT_272 = "HashKey.documentLookupResult," + NL + "\t\t\t\t\t\t\t\t\t\txPathAsOutput_";
  protected final String TEXT_273 = ",xmlNameSpaceMap_";
  protected final String TEXT_274 = ",xpathToTypeMap_";
  protected final String TEXT_275 = ",xpathToPatternMap_";
  protected final String TEXT_276 = ",\"";
  protected final String TEXT_277 = "\");" + NL + "\t\t\t\t\t\t\t\t\tif(result_";
  protected final String TEXT_278 = " == null || result_";
  protected final String TEXT_279 = ".size() == 0) {" + NL + "\t\t\t\t\t\t\t\t\t\t// not find" + NL + "\t\t\t\t\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\t\t\t\t\t//find and cache it,now only memory,inner join and unique match mode" + NL + "\t\t\t\t\t\t\t\t\t\t//once find,not reject." + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_280 = NL + "\t\t\t\t\t\t\t\t\t\tisMatchDocRow";
  protected final String TEXT_281 = " = true;" + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_282 = NL + "\t\t\t\t\t\t\t\t\t\tfor(java.util.Map<String,Object> docRow_";
  protected final String TEXT_283 = " : result_";
  protected final String TEXT_284 = ") {" + NL + "\t\t\t\t\t\t\t\t\t\t\tfor(String path_";
  protected final String TEXT_285 = " : docRow_";
  protected final String TEXT_286 = ".keySet()) {" + NL + "\t\t\t\t\t\t\t\t\t\t\t\ttreeNodeAPI_";
  protected final String TEXT_287 = ".put(path_";
  protected final String TEXT_288 = ", StringUtils.valueOf(docRow_";
  protected final String TEXT_289 = ".get(path_";
  protected final String TEXT_290 = ")));" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_291 = NL + "\t\t\t\t\t\t\t\t\t\t\t\tcacheResult";
  protected final String TEXT_292 = ".put(path_";
  protected final String TEXT_293 = ",StringUtils.valueOf(docRow_";
  protected final String TEXT_294 = ".get(path_";
  protected final String TEXT_295 = ")));" + NL + "\t\t\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_296 = NL + "\t\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_297 = NL + "\t\t\t\t\t\t\t\t\t\tquit";
  protected final String TEXT_298 = " = true;" + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_299 = NL + "\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_300 = NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_301 = " = fromLookup_";
  protected final String TEXT_302 = ";" + NL + "\t\t\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_303 = NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_304 = NL + "\t\t\t\t\t\t\t\t\t\tif(isMatchDocRow";
  protected final String TEXT_305 = ") {" + NL + "\t\t\t\t\t\t\t\t\t\t\tcacheResult";
  protected final String TEXT_306 = ".put(\"";
  protected final String TEXT_307 = "\",";
  protected final String TEXT_308 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\ttHash_Lookup_Cache_";
  protected final String TEXT_309 = ".put(lookupCacheKey";
  protected final String TEXT_310 = ",cacheResult";
  protected final String TEXT_311 = "); " + NL + "\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_312 = NL + "\t\t\t\t\t\t\t\t\t\tcacheResult";
  protected final String TEXT_313 = ".put(\"";
  protected final String TEXT_314 = "\",";
  protected final String TEXT_315 = "); " + NL + "\t\t\t\t\t\t\t\t\t\ttHash_Lookup_Cache_";
  protected final String TEXT_316 = ".put(lookupCacheKey";
  protected final String TEXT_317 = ",cacheResult";
  protected final String TEXT_318 = "); " + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_319 = NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_320 = NL + "\t\t\t\t\t\t\t\t\t" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_321 = NL + "\t\t\t\t\t\t\t\t\t\tif(quit";
  protected final String TEXT_322 = ") {//for first match,once find in document,no need to continue to lookup." + NL + "\t\t\t\t\t\t\t\t\t\t\tbreak;" + NL + "\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_323 = NL + "\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t}//TD119" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_324 = NL + "\t\t\t\t\t\t   \t";
  protected final String TEXT_325 = " = !isMatchDocRow";
  protected final String TEXT_326 = " && !hasResultFromLookupCache";
  protected final String TEXT_327 = ";" + NL + "\t\t\t\t\t\t    ";
  protected final String TEXT_328 = " = ";
  protected final String TEXT_329 = ";" + NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_330 = NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_331 = NL + "\t\t\t\t\t\t  \tif(!isMatchDocRow";
  protected final String TEXT_332 = " && !hasResultFromLookupCache";
  protected final String TEXT_333 = ") {" + NL + "\t\t\t\t\t\t  \t\t";
  protected final String TEXT_334 = " = ";
  protected final String TEXT_335 = "Default;" + NL + "\t\t\t\t\t\t  \t}" + NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_336 = NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_337 = NL + "\t\t\t\t\t\t  \t\t//do nothing" + NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_338 = NL + "\t\t\t\t  \t\t}//TD10086" + NL + "\t\t\t\t  \t\t" + NL + "\t\t\t\t  \t\t";
  protected final String TEXT_339 = NL + "\t\t\t\t  \t\t\tif(!";
  protected final String TEXT_340 = " &&" + NL + "\t\t\t\t  \t\t\t\t!(";
  protected final String TEXT_341 = ")" + NL + "\t\t\t\t  \t\t\t) {" + NL + "\t\t\t\t  \t\t\t\t";
  protected final String TEXT_342 = " = ";
  protected final String TEXT_343 = "Default;" + NL + "\t\t\t\t  \t\t\t\t" + NL + "\t\t\t\t  \t\t\t\t";
  protected final String TEXT_344 = " " + NL + "\t\t\t\t  \t\t\t\ttreeNodeAPI_";
  protected final String TEXT_345 = ".put(\"";
  protected final String TEXT_346 = "\",null);" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_347 = NL + "\t\t\t\t  \t\t\t\t" + NL + "\t\t\t\t  \t\t\t\t";
  protected final String TEXT_348 = NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_349 = " = true;\t\t\t  \t\t\t\t" + NL + "\t\t\t\t  \t\t\t\t";
  protected final String TEXT_350 = NL + "\t\t\t\t  \t\t\t}" + NL + "\t\t\t\t  \t\t";
  protected final String TEXT_351 = NL + "\t\t\t\t  \t\t" + NL + "\t\t\t\t  \t\t//////////////////////////////////////////////////////////////////////////////////////////////" + NL + "\t\t\t\t  \t\t";
  protected final String TEXT_352 = NL + "\t\t\t\t  \t\tboolean fromCache";
  protected final String TEXT_353 = " = hasResultFromLookupCache";
  protected final String TEXT_354 = ";" + NL + "\t\t\t\t  \t\tList<java.util.Map<String,Object>> multipleResultSet";
  protected final String TEXT_355 = " = new java.util.ArrayList<java.util.Map<String,Object>>();" + NL + "\t\t\t\t  \t\t//the var for cache the ";
  protected final String TEXT_356 = "Struct" + NL + "\t\t\t\t  \t\tjava.util.Map<String,Object> oneRow_";
  protected final String TEXT_357 = " = null;" + NL + "\t\t\t\t\t\tif(hasResultFromLookupCache";
  protected final String TEXT_358 = ") {" + NL + "\t\t\t\t\t    \t";
  protected final String TEXT_359 = NL + "\t\t\t\t\t    \tjava.util.Map<String,Object> cacheResult_";
  protected final String TEXT_360 = " = tHash_Lookup_Cache_";
  protected final String TEXT_361 = ".next();" + NL + "\t\t\t\t\t    \tmultipleResultSet";
  protected final String TEXT_362 = ".add(cacheResult_";
  protected final String TEXT_363 = ");" + NL + "\t\t\t\t\t    \t";
  protected final String TEXT_364 = NL + "\t\t\t\t\t    } else if(!";
  protected final String TEXT_365 = ") {" + NL + "\t\t\t\t\t    \t";
  protected final String TEXT_366 = NL + "\t\t\t\t\t\t   \tisMatchDocRow";
  protected final String TEXT_367 = " = false;" + NL + "\t\t\t\t\t\t  \t";
  protected final String TEXT_368 = NL + "\t\t\t\t\t    \twhile (tHash_Lookup_";
  protected final String TEXT_369 = ".hasNext()) {" + NL + "\t\t\t\t\t    \t\tfromLookup_";
  protected final String TEXT_370 = " = null;" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_371 = " = ";
  protected final String TEXT_372 = "Default;" + NL + "\t\t\t\t\t\t\t\tfromLookup_";
  protected final String TEXT_373 = " = tHash_Lookup_";
  protected final String TEXT_374 = ".next();" + NL + "\t\t\t\t\t\t\t\tif(fromLookup_";
  protected final String TEXT_375 = " != null) {" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_376 = NL + "\t\t\t\t\t\t\t\t\tDocument lookupDoc_";
  protected final String TEXT_377 = " = fromLookup_";
  protected final String TEXT_378 = ".";
  protected final String TEXT_379 = ";" + NL + "\t\t\t\t\t\t\t\t\tjava.util.Map<String,String> xPathAsOutput_";
  protected final String TEXT_380 = " = new java.util.HashMap<String,String>();" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_381 = NL + "\t\t\t\t\t\t\t\t\t\txPathAsOutput_";
  protected final String TEXT_382 = ".put(\"";
  protected final String TEXT_383 = "\",\"";
  protected final String TEXT_384 = "\");" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_385 = NL + "\t\t\t\t\t\t\t\t\tList<java.util.Map<String,Object>> result_";
  protected final String TEXT_386 = " = lookupDoc_";
  protected final String TEXT_387 = ".LookupDocument(\"";
  protected final String TEXT_388 = "\",";
  protected final String TEXT_389 = ",";
  protected final String TEXT_390 = "HashKey.documentLookupResult," + NL + "\t\t\t\t\t\t\t\t\t\txPathAsOutput_";
  protected final String TEXT_391 = ",xmlNameSpaceMap_";
  protected final String TEXT_392 = ",xpathToTypeMap_";
  protected final String TEXT_393 = ",xpathToPatternMap_";
  protected final String TEXT_394 = ",\"";
  protected final String TEXT_395 = "\");" + NL + "\t\t\t\t\t\t\t\t\tif(result_";
  protected final String TEXT_396 = " == null || result_";
  protected final String TEXT_397 = ".size() == 0) {" + NL + "\t\t\t\t\t\t\t\t\t\t//do nothing" + NL + "\t\t\t\t\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_398 = NL + "\t\t\t\t\t\t\t\t\t\tisMatchDocRow";
  protected final String TEXT_399 = " = true;" + NL + "\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_400 = NL + "\t\t\t\t\t\t\t\t\t\tfor(java.util.Map<String,Object> docRow_";
  protected final String TEXT_401 = " : result_";
  protected final String TEXT_402 = ") {" + NL + "\t\t\t\t\t\t\t\t\t\t\tdocRow_";
  protected final String TEXT_403 = ".put(\"";
  protected final String TEXT_404 = "\",fromLookup_";
  protected final String TEXT_405 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\tmultipleResultSet";
  protected final String TEXT_406 = ".add(docRow_";
  protected final String TEXT_407 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_408 = NL + "\t\t\t\t\t\t\t\t\t\t\tfor(String path_";
  protected final String TEXT_409 = " : docRow_";
  protected final String TEXT_410 = ".keySet()) {" + NL + "\t\t\t\t\t\t\t\t\t\t\t\tcacheResult";
  protected final String TEXT_411 = ".put(path_";
  protected final String TEXT_412 = ",StringUtils.valueOf(docRow_";
  protected final String TEXT_413 = ".get(path_";
  protected final String TEXT_414 = ")));" + NL + "\t\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t\t\tcacheResult";
  protected final String TEXT_415 = ".put(\"";
  protected final String TEXT_416 = "\",fromLookup_";
  protected final String TEXT_417 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\ttHash_Lookup_Cache_";
  protected final String TEXT_418 = ".put(lookupCacheKey";
  protected final String TEXT_419 = ",cacheResult";
  protected final String TEXT_420 = ");" + NL + "\t\t\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_421 = NL + "\t\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t\t\t";
  protected final String TEXT_422 = NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_423 = "\t" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_424 = " = fromLookup_";
  protected final String TEXT_425 = ";" + NL + "\t\t\t\t\t\t\t\t\t//construct the resultset for mutiple lookup when no document lookup." + NL + "\t\t\t\t\t\t\t\t\toneRow_";
  protected final String TEXT_426 = " = new java.util.HashMap<String, Object>();" + NL + "\t\t\t\t\t\t\t\t\toneRow_";
  protected final String TEXT_427 = ".put(\"";
  protected final String TEXT_428 = "\",";
  protected final String TEXT_429 = ");" + NL + "\t\t\t\t\t\t\t\t\tmultipleResultSet";
  protected final String TEXT_430 = ".add(oneRow_";
  protected final String TEXT_431 = ");" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_432 = NL + "\t\t\t\t\t\t\t\t}" + NL + "\t\t\t\t\t    \t}//end while" + NL + "\t\t\t\t\t    \t" + NL + "\t\t\t\t\t    }//end else if" + NL + "\t\t\t\t\t    //now not support cache all_matches lookup" + NL + "\t\t\t\t\t    ";
  protected final String TEXT_433 = NL + "\t\t\t\t\t    \tforceLoop";
  protected final String TEXT_434 = " = !isMatchDocRow";
  protected final String TEXT_435 = " && !hasResultFromLookupCache";
  protected final String TEXT_436 = ";" + NL + "\t\t\t\t\t    ";
  protected final String TEXT_437 = NL + "\t\t\t\t\t    " + NL + "\t\t\t\t\t    ";
  protected final String TEXT_438 = NL + "\t\t\t\t\t   \t";
  protected final String TEXT_439 = " = !isMatchDocRow";
  protected final String TEXT_440 = " && !hasResultFromLookupCache";
  protected final String TEXT_441 = ";" + NL + "\t\t\t\t\t    ";
  protected final String TEXT_442 = " = ";
  protected final String TEXT_443 = ";" + NL + "\t\t\t\t\t    forceLoop";
  protected final String TEXT_444 = " = ";
  protected final String TEXT_445 = ";" + NL + "\t\t\t\t\t  \t";
  protected final String TEXT_446 = NL + "\t\t\t\t\t    " + NL + "\t\t\t\t\t    java.util.Iterator<java.util.Map<String,Object>> iter";
  protected final String TEXT_447 = " = multipleResultSet";
  protected final String TEXT_448 = ".iterator();" + NL + "\t\t\t\t\t    while(iter";
  protected final String TEXT_449 = ".hasNext() || forceLoop";
  protected final String TEXT_450 = ") { // G_TM_M_002" + NL + "\t\t\t\t\t    \t";
  protected final String TEXT_451 = " = ";
  protected final String TEXT_452 = "Default;" + NL + "\t\t\t\t\t    \t";
  protected final String TEXT_453 = "Struct tempLookup";
  protected final String TEXT_454 = " = null; " + NL + "\t\t\t\t\t    \tif(!forceLoop";
  protected final String TEXT_455 = ") {" + NL + "\t\t\t\t\t\t    \tjava.util.Map<String,Object> oneRow";
  protected final String TEXT_456 = " = iter";
  protected final String TEXT_457 = ".next();" + NL + "\t\t\t\t\t\t   \t\t";
  protected final String TEXT_458 = NL + "\t\t\t\t\t\t    \t\t\ttreeNodeAPI_";
  protected final String TEXT_459 = ".put(\"";
  protected final String TEXT_460 = "\",StringUtils.valueOf(oneRow";
  protected final String TEXT_461 = ".get(\"";
  protected final String TEXT_462 = "\")));" + NL + "\t\t\t\t\t\t    \t";
  protected final String TEXT_463 = NL + "\t\t\t\t\t    \t\ttempLookup";
  protected final String TEXT_464 = " = (";
  protected final String TEXT_465 = ")oneRow";
  protected final String TEXT_466 = ".get(\"";
  protected final String TEXT_467 = "\");" + NL + "\t\t\t\t\t    \t\tif(tempLookup";
  protected final String TEXT_468 = "!=null) {" + NL + "\t\t\t\t\t    \t\t\t";
  protected final String TEXT_469 = " = tempLookup";
  protected final String TEXT_470 = ";" + NL + "\t\t\t\t\t    \t\t}" + NL + "\t\t\t\t\t    \t\t" + NL + "\t\t\t\t\t    \t\t";
  protected final String TEXT_471 = NL + "\t\t\t\t\t    \t\tif(!";
  protected final String TEXT_472 = " &&" + NL + "\t\t\t\t  \t\t\t\t\t!(";
  protected final String TEXT_473 = ")" + NL + "\t\t\t\t  \t\t\t\t) {" + NL + "\t\t\t\t  \t\t\t\t\t";
  protected final String TEXT_474 = " = ";
  protected final String TEXT_475 = "Default;" + NL + "\t\t\t\t  \t\t\t\t" + NL + "\t\t\t\t  \t\t\t\t\t";
  protected final String TEXT_476 = " " + NL + "\t\t\t\t  \t\t\t\t\ttreeNodeAPI_";
  protected final String TEXT_477 = ".put(\"";
  protected final String TEXT_478 = "\",null);" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_479 = NL + "\t\t\t\t  \t\t\t\t" + NL + "\t\t\t\t  \t\t\t\t\t";
  protected final String TEXT_480 = NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_481 = " = true;\t\t\t  \t\t\t\t" + NL + "\t\t\t\t  \t\t\t\t\t";
  protected final String TEXT_482 = NL + "\t\t\t\t  \t\t\t\t}" + NL + "\t\t\t\t\t    \t\t";
  protected final String TEXT_483 = NL + "\t\t\t\t    \t\t}" + NL + "\t\t\t\t    \t\tforceLoop";
  protected final String TEXT_484 = " = false;" + NL + "\t\t\t\t  \t\t";
  protected final String TEXT_485 = NL + "\t\t\t\t  \t\t/////////////////////////////////////////////////////////////////////////////////////////////////" + NL + "\t\t\t\t  \t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_486 = NL + "\t\t\t\t";
  protected final String TEXT_487 = NL + "        { // start of Var scope" + NL + "        " + NL + "\t        // ###############################" + NL + "        \t// # Vars tables" + NL + "        ";
  protected final String TEXT_488 = NL + "\t\t\t\t\t";
  protected final String TEXT_489 = NL + "\t\t// ###############################" + NL + "\t\t// # Output tables";
  protected final String TEXT_490 = NL;
  protected final String TEXT_491 = NL;
  protected final String TEXT_492 = NL + "\t\tif(nb_line_";
  protected final String TEXT_493 = " == nodeList_";
  protected final String TEXT_494 = ".size()) {";
  protected final String TEXT_495 = NL + "\t\t\t\t\t";
  protected final String TEXT_496 = " = ";
  protected final String TEXT_497 = "_save;";
  protected final String TEXT_498 = NL + "\t\t}";
  protected final String TEXT_499 = NL;

	class XMLMapUtil {
	
		private XmlMapData getXmlMapData(INode node) {
		
			XmlMapData xmlMapData = (XmlMapData)ElementParameterParser.getObjectValueXMLTree(node);
			
			return xmlMapData;
		}
		
		private List<IConnection> filterAndSort(INode node) {
			
			XmlMapData xmlMapData= getXmlMapData(node);
			EList<InputXmlTree> inputTables = xmlMapData.getInputTrees();
			final List<String> tableNames = new ArrayList<String>();
			HashMap<String, InputXmlTree> hNameToInputXmlTree = new HashMap<String, InputXmlTree>();
			
			for(InputXmlTree inputTable : inputTables){
				hNameToInputXmlTree.put(inputTable.getName(), inputTable);
				tableNames.add(inputTable.getName());
			}
			
			List<IConnection> inputConnections = new ArrayList<IConnection>();
			
			for(IConnection conn : node.getIncomingConnections()){
				if(hNameToInputXmlTree.get(conn.getName()) != null){
					inputConnections.add(conn);
				}
			}
			
			java.util.Collections.sort(inputConnections,new java.util.Comparator<IConnection>() {
				public int compare(IConnection conn1, IConnection conn2) {
					return tableNames.indexOf(conn1.getName()) - tableNames.indexOf(conn2.getName());
				}	
			});
			
			return inputConnections;
		}
	
		public String createSignature(INode node, boolean force) {
			String toReturn = "";
			
			List<IConnection> inputConnections = filterAndSort(node);
			
			for(IConnection conn : inputConnections) {
				if (conn.getLineStyle().equals(EConnectionType.FLOW_MAIN) || conn.getLineStyle().equals(EConnectionType.FLOW_MERGE) || conn.getLineStyle().equals(EConnectionType.FLOW_REF)) {
					if ((force)|| conn.getLineStyle().equals(EConnectionType.FLOW_REF) ||(conn.getSource().isSubProcessStart() || !(NodeUtil.isDataAutoPropagated(conn.getSource())))) {
						toReturn+=", "+conn.getName() + "Struct " + conn.getName();
					} else {
						toReturn+=", "+getConnectionType(conn.getSource())+"Struct " + conn.getName();
					}
				}
			}
    		return toReturn;
    	}
    	
    	public String getConnectionType(INode node) {
    		for(IConnection conn : node.getIncomingConnections()) {
				if (conn.getLineStyle().equals(EConnectionType.FLOW_MAIN) || conn.getLineStyle().equals(EConnectionType.FLOW_MERGE) || conn.getLineStyle().equals(EConnectionType.FLOW_REF)) {
					if ((conn.getLineStyle().equals(EConnectionType.FLOW_REF) || conn.getSource().isSubProcessStart() || !(NodeUtil.isDataAutoPropagated(conn.getSource())))) {
						return conn.getName();
					} else {
						return getConnectionType(conn.getSource());
					}
				} else if (conn.getLineStyle().equals(EConnectionType.ITERATE)||conn.getLineStyle().equals(EConnectionType.ON_ROWS_END)) {
					if(node.getOutgoingConnections() != null && node.getOutgoingConnections().size() > 0) {
						return node.getOutgoingConnections().get(0).getName();
					}
				}
			}
			return "";
    	}
    	
    	public boolean tableHasADocument(EList<OutputTreeNode> outputTableEntries) {
    		for (OutputTreeNode outputTableEntry : outputTableEntries) {
    			if(("id_Document").equals(outputTableEntry.getType())){
    				return true;
    			}
    		}
    		return false;
    	}
	}

	class MatchXmlTreeExpr {
		String cid = null;
		java.util.Map<String, String> pathTypeMap = new java.util.HashMap<String, String>(); // Map<input table xpath, typeToGenerate>
		java.util.Map<String, String> pathPatternMap = new java.util.HashMap<String, String>(); // Map<input table xpath, Pattern>
		java.util.Map<String, String> pathTypeToGenMap = new java.util.HashMap<String, String>(); // Map<"/root/name","String">
		
		//only for main input table
		public MatchXmlTreeExpr(TreeNode node, String cid) {
			this.cid = cid;
			init(node);
		}
		
		//for main and all lookup tables
		public MatchXmlTreeExpr(List<TreeNode> nodes, String cid) {
			this.cid = cid;
			for(TreeNode node : nodes) {
				init(node);
			}
		}
		
		//NO TYPE CHECK 
		public MatchXmlTreeExpr(String cid) {
			this.cid = cid;
		}
		
		private void init(TreeNode node) {
			if(node.getOutgoingConnections().size()>0 || node.getLookupOutgoingConnections().size()>0 || node.getFilterOutGoingConnections().size()>0){
				String talendType = node.getType();
				JavaType javaType = JavaTypesManager.getJavaTypeFromId(talendType);
				String typeToGenerate = JavaTypesManager.getTypeToGenerate(talendType, node.isNullable());
				String patternValue = node.getPattern() == null || node.getPattern().trim().length() == 0 ? null : node.getPattern();
				
				pathTypeMap.put(node.getXpath(), talendType);
				pathPatternMap.put(node.getXpath(), patternValue);
				pathTypeToGenMap.put(node.getXpath(), typeToGenerate);
			}
    		
    		for(TreeNode tmpNode : node.getChildren()) {
    			init(tmpNode);
    		}
		}
		
		String generateExprCode(String expression){
			StringBuilder strBuilder = new StringBuilder();
			if(expression==null || ("").equals(expression)) {
				return "";
			}
	
			String currentExpression = expression;
			String tmpXpath = "";
			java.util.regex.Pattern expressionFromDoc = java.util.regex.Pattern.compile("\\[.*\\..*:.*\\]");
			java.util.regex.Matcher matcherFromDoc;
			
			boolean end = false;
			
			if(expression.indexOf("[")>-1) {
				strBuilder.append(expression.substring(0, expression.indexOf("[")));
				currentExpression = currentExpression.substring(currentExpression.indexOf("["), currentExpression.length());
				
				while(currentExpression.length()>0 && !end) {
					expression = this.getXPathExpression(currentExpression);
					currentExpression = currentExpression.substring(expression.length(), currentExpression.length());
					matcherFromDoc = expressionFromDoc.matcher(expression);
					if(matcherFromDoc.matches()) {
						tmpXpath = expression.substring(1, expression.length()-1);
						if("id_String".equals(pathTypeMap.get(tmpXpath)) || "id_Object".equals(pathTypeMap.get(tmpXpath))){
							strBuilder.append("treeNodeAPI_"+cid+".get_String(\""+ tmpXpath + "\")");
						} else if("id_Date".equals(pathTypeMap.get(tmpXpath))) {
							strBuilder.append("treeNodeAPI_"+cid+".get_Date(\""+ tmpXpath + "\" , " + pathPatternMap.get(tmpXpath) + ")");
						} else if("id_byte[]".equals(pathTypeMap.get(tmpXpath))) {
							strBuilder.append("treeNodeAPI_"+cid+".get_Bytes(\""+ tmpXpath + "\")");
						} else {
							if(pathTypeToGenMap.get(tmpXpath)!=null) {
								strBuilder.append("treeNodeAPI_"+cid+".get_" + pathTypeToGenMap.get(tmpXpath) + "(\""+ tmpXpath + "\")");
							} else {
								strBuilder.append("treeNodeAPI_"+cid+".get_String(\""+ tmpXpath + "\")");
							}
						}				
					} else {
						strBuilder.append(expression);
					}
					if(currentExpression.indexOf("[")>-1) {
						strBuilder.append(currentExpression.substring(0, currentExpression.indexOf("[")));
						currentExpression = currentExpression.substring(currentExpression.indexOf("["), currentExpression.length());
					} else {
						strBuilder.append(currentExpression);
						end=true;
					}
				
				}
			} else {
				strBuilder.append(expression);
			}
			return strBuilder.toString();
		}
		
		private String getXPathExpression(String currentExpression) {
			int count = 0;
			int i = 0;
			for(;i<currentExpression.length();i++) {
				char c = currentExpression.charAt(i);
				if('[' == c) {
					count++;
				}
				if(']' == c) {
					if(count==1) {
						break;
					}
					count--;
				}
			}
			return currentExpression.substring(0,i+1);
		}
		
	}
	
	class XPathHelper{
		int dPrefixCount = 0;
		Map<String,String> nsMapping = new HashMap<String,String>();
		Map<String,String> dPrefixMapping = new HashMap<String,String>();
		TreeNode rootNode;
		boolean isOptional = false; // use to judge if the loop node is mandotary in the source file.
		TreeNode loopNode;
		String finalLoopNodeXPath;
		Map<String,String> outNodesXPath;
		Map<String,String> lookupInputNodesXPath;
		
		Map<String,String> xpathToPattern = new HashMap<String,String>();
		Map<String,String> xpathToType = new HashMap<String,String>();
		
		public Map<String,String> getXpathToPatternMap(){
			return xpathToPattern;
		}
		
		public Map<String,String> getXpathToTypeMap(){
			return xpathToType;
		}
		
		public XPathHelper(TreeNode rootNode){
			this(rootNode,false);
		}
		
		public XPathHelper(TreeNode rootNode,boolean isMultiLoop){
			this.rootNode = rootNode;
			collectionNS(rootNode, nsMapping, dPrefixMapping, null);
			if(!isMultiLoop) {
				//set loop node
				setInputLoopNode(findLoopNode(rootNode));
			} else {
				//do nothing
			}
			
		}
				
		public void setInputLoopNode(TreeNode inputLoopNode) {
			if(inputLoopNode==null){
				return;
			}
			loopNode = inputLoopNode;
			isOptional = loopNode.isOptional();
			
			
			List<TreeNode> outNodes = new ArrayList<TreeNode>(); 
			//find all output node(out & lookup out)
			findOutputNodes(rootNode, outNodes);
			
			//build xpath with prefix
			finalLoopNodeXPath = buildXPathWithPrefix(loopNode, dPrefixMapping);
			
			outNodesXPath = new HashMap<String,String>();
			buildXPathWithPrefix(outNodes, dPrefixMapping, outNodesXPath);
			
			//build xpath for the node has lookup input connection
			List<TreeNode> lookupInputNodes = new ArrayList<TreeNode>();
			findLookupInputNodes(rootNode, lookupInputNodes);
			lookupInputNodesXPath = new HashMap<String,String>();
			buildXPathWithPrefix(lookupInputNodes, dPrefixMapping, lookupInputNodesXPath);
		}
		
		public boolean hasLoopNode(){
			if(loopNode == null){
				return false;
			}
			return true;
		}
		public boolean hasDefinedNS(){
			if(nsMapping.size()==0){
				return false;
			}
			return true;
		}
		
		/**
		 * use to judge if the loop is mandotary
		 * 
		 */
		public boolean isLoopOptional() {
			return isOptional;
		}
		
		public Map<String,String> getOutNodesXPath(){
			return outNodesXPath;
		}
		
		public Map<String,String> getLookupInputNodesXPath(){
			return lookupInputNodesXPath;
		}
		
		public String buildNSMapping(String name){
			StringBuilder sb = new StringBuilder();
			for (Object key : nsMapping.keySet()) { 
			    Object val = nsMapping.get(key);
			    sb.append(name+".put(\""+key+"\",\""+val+"\");"); 
			} 
			return sb.toString();
		}
		
		public String getLoopNodeXPath(){
			return finalLoopNodeXPath;
		}
		
		private void buildXPathWithPrefix(List<TreeNode> nodes, Map<String,String> dPrefixMapping, Map<String,String> nodesXPath){
			String loopNodeXPath = loopNode.getXpath();
			for(TreeNode node: nodes){
				String currentNodeXPath = node.getXpath();
				String typeToGenerate = JavaTypesManager.getTypeToGenerate(node.getType(), node.isNullable()); 
				xpathToType.put(currentNodeXPath,typeToGenerate);
				xpathToPattern.put(currentNodeXPath,node.getPattern());
				
				if(currentNodeXPath==null || ("").equals(currentNodeXPath)){
				}else if(loopNodeXPath.equals(currentNodeXPath)){
					nodesXPath.put(currentNodeXPath, ".");
				}else{
					String nodeXPathWithPrefix = buildXPathWithPrefix(node, dPrefixMapping);
					if(nodeXPathWithPrefix.startsWith(finalLoopNodeXPath)){
						String relativeXPath = nodeXPathWithPrefix.substring(finalLoopNodeXPath.length() + 1);
						nodesXPath.put(currentNodeXPath, relativeXPath);
					}else{
						StringBuilder relativeXPath = new StringBuilder();
						String tmp = finalLoopNodeXPath;
						
						while(!nodeXPathWithPrefix.startsWith(tmp)){
							int index = tmp.lastIndexOf("/");
							if(index<0){ break; }
							tmp = tmp.substring(0,index);
							relativeXPath.append("../");
						}
						if(tmp.lastIndexOf("/") < 0 ){
							System.out.println("Loop Path is not set or loop Path is invalid");
						}else{
							relativeXPath.append(nodeXPathWithPrefix.substring(tmp.length() + 1));
						}
						relativeXPath.toString();
						nodesXPath.put(currentNodeXPath, relativeXPath.toString());
					}
				}
			}
		} 
		
		private String buildXPathWithPrefix(TreeNode node, Map<String,String> dPrefixMapping){
			String xpath = getXPath(node);
			StringBuilder finalXPath = new StringBuilder();
			return buildXPathWithPrefix(finalXPath, xpath, dPrefixMapping);
			
		}
		private String buildXPathWithPrefix(StringBuilder finalXPath, String xpath, Map<String,String> dPrefixMapping){
			List<String> nodePaths = new ArrayList<String>();
			
			while(xpath.lastIndexOf("/") != -1){
				nodePaths.add(xpath);
				xpath = xpath.substring(0,xpath.lastIndexOf("/"));
			}
			
			for(int i=nodePaths.size()-1; i>=0; i--){
				String nodePath = nodePaths.get(i);
				String prefix = dPrefixMapping.get(nodePath);
				String nodeName = nodePath.substring(nodePath.lastIndexOf("/")+1);
				
				finalXPath.append("/");
				if(prefix != null && !"".equals(prefix)){
					finalXPath.append(prefix);
					finalXPath.append(":");
					finalXPath.append(nodeName);
				}else{
					finalXPath.append(nodeName);
				}
			}
			
			return finalXPath.toString();
		}
		
		private void findOutputNodes(TreeNode currentNode, List<TreeNode> outNodes){
			if(currentNode.getOutgoingConnections().size()>0 || currentNode.getLookupOutgoingConnections().size()>0 || currentNode.getFilterOutGoingConnections().size()>0){
				outNodes.add(currentNode);
			}
			for(TreeNode childNode : currentNode.getChildren()) {
				if(childNode.getNodeType() == NodeType.ELEMENT || childNode.getNodeType() == NodeType.ATTRIBUT){
					findOutputNodes(childNode, outNodes);
				}
			}
		}
		
		private void findLookupInputNodes(TreeNode currentNode, List<TreeNode> lookupInputNodes){
			if(currentNode.getLookupIncomingConnections().size()>0){
				lookupInputNodes.add(currentNode);
			}
			for(TreeNode childNode : currentNode.getChildren()) {
				if(childNode.getNodeType() == NodeType.ELEMENT || childNode.getNodeType() == NodeType.ATTRIBUT){
					findLookupInputNodes(childNode, lookupInputNodes);
				}
			}
		}
		
		private void collectionNS(TreeNode currentNode, Map<String,String> nsMapping, Map<String,String> dPrefixMapping, String parentDPrefix){
			EList<TreeNode> childNodes = currentNode.getChildren();
			for(TreeNode childNode : childNodes){
				if(childNode.getNodeType() == NodeType.NAME_SPACE){
					if(childNode.getName() != null && !"(default)".equals(childNode.getName())){
						nsMapping.put(childNode.getName(),childNode.getDefaultValue());
					}else{
						String defaultPrefix = findVaildDefaultPrefix();
						nsMapping.put(defaultPrefix,childNode.getDefaultValue());
						//dPrefixMapping.put(getXPath(currentNode),defaultPrefix);
						parentDPrefix = defaultPrefix;
					}
				}
			}
			if(!currentNode.getName().contains(":")){
				if(parentDPrefix != null && !"".equals(parentDPrefix)){
					dPrefixMapping.put(getXPath(currentNode),parentDPrefix);
				}
			}else{
				//parentDPrefix = null;
			}
			for(TreeNode childNode : childNodes){
				if(childNode.getNodeType() == NodeType.ELEMENT){
					collectionNS(childNode, nsMapping, dPrefixMapping, parentDPrefix);
				}
			}
		}
		
		private String findVaildDefaultPrefix(){
			String prefix = "TPrefix"+dPrefixCount;
			dPrefixCount++;
			if(nsMapping.get(prefix)==null || "".equals(nsMapping.get(prefix))){
				return prefix;
			}else{
				return findVaildDefaultPrefix();
			}
		}
		
		private TreeNode findLoopNode(TreeNode node){
			if(node == null){
				return null;
			}
			if(node.isLoop()) {
				return node;
			}
			for(TreeNode childNode : node.getChildren()) {
				if(childNode.getNodeType() == NodeType.ELEMENT){
					TreeNode resultNode = findLoopNode(childNode);
					if(resultNode!=null){
						return resultNode;
					}
				}
			}
			
			return null;
		}
		
		private String getXPath(TreeNode node){
			String uiXPath = node.getXpath();
			return uiXPath.substring(uiXPath.indexOf(":")+1);
		}
	}

	class TreeUtil{
		//get all nodes whose expression is editable;
		void getAllLeaf(TreeNode node,List<TreeNode> result) {
			EList<TreeNode> children = node.getChildren();
			if(children==null || children.size() == 0) {
				result.add(node);//leaf is editable
			} else {
				boolean editableAtExpression = true;
				for(TreeNode child : children) {
					if(child!=null) {
						//attribute and namespace are not treat as subnode , so the expression of treeNode should be editable.
						if(NodeType.ATTRIBUT != child.getNodeType() && NodeType.NAME_SPACE != child.getNodeType()) {
							editableAtExpression = false;
						}
						getAllLeaf(child,result);
					}
				}
				
				if(editableAtExpression) {
					result.add(node);
				}
			}
		}
		
		void getAllLoopNodes(OutputTreeNode node,List<OutputTreeNode> result) {
			if(node == null) {
				return;
			}
			if(node.isLoop()){
				result.add(node);
				return;
			}
			for(TreeNode child : node.getChildren()){
				getAllLoopNodes((OutputTreeNode)child,result);
			}
		}
		
		boolean isRootLoop(TreeNode root) {
			if(root == null) return false;
			List<TreeNode> children = root.getChildren();
			if(children == null || children.size() == 0) {
				return false;
			}
			TreeNode realRoot = children.get(0);
			return realRoot.isLoop();
		}
	}
	
	static class XMLOrderUtil {
		
		int groupCount = 0;
		
		int getGroupCount(OutputTreeNode rootNode) {
			groupCount = 0;
			countGroupNode(rootNode);
			return groupCount;
		}
		
		static int getCurrOrder(OutputTreeNode currNode) {
			int currOrder = 0;
			if(isGroupOrLoopInMain(currNode)) {
				OutputTreeNode parent = (OutputTreeNode)currNode.eContainer();
				if(parent!=null) {
					EList<TreeNode> children = parent.getChildren();
					//the order not containing namespace and attribute
					for(TreeNode child : children) {
						NodeType nodeType = child.getNodeType();
						if(nodeType != NodeType.ELEMENT) {
							continue;
						}
						if(currNode.equals(child)) {
							break;
						} 
						currOrder++;
					}
				}
			}
			return currOrder;
		}
		
		static int getCurrPos(OutputTreeNode currNode) {
			int currPos = 0;
			if(isGroupOrLoopInMain(currNode)) {
				OutputTreeNode parent = (OutputTreeNode)currNode.eContainer();
				OutputTreeNode tmpNode = parent;
				while(tmpNode!=null && isGroupOrLoopInMain(tmpNode)){
    				currPos++;
    				tmpNode = (OutputTreeNode)tmpNode.eContainer();
    			}
			}
			return currPos;
		}
		
		private static boolean isGroupOrLoopInMain(OutputTreeNode currNode) {
			return currNode.isMain() && (currNode.isGroup() || currNode.isLoop());
		}
		
		private void countGroupNode(OutputTreeNode node) {
			EList<TreeNode> children = node.getChildren();
			
			if(children==null || children.size() == 0) {
				//it is impossible that leaf is Group.
				
			} else {
				//branch maybe Group
				if(node.isGroup()) {
					groupCount++;
				}
				
				for(TreeNode child : children) {
					if(child!=null) {
						countGroupNode((OutputTreeNode)child);
					}
				}
			}
		}
		
		
		
	}
	
	public INode searchSubProcessStartNode(IConnection connection) {
       	INode source = connection.getSource();
    	INode subprocessStartNode = null;
    	if(source != null) {
			String searchedComponentName = source.getUniqueName();
			List<? extends INode> generatedNodes = source.getProcess().getGeneratingNodes();
			for(INode loopNode : generatedNodes) {
				if(loopNode.getUniqueName().equals(searchedComponentName)) {
					subprocessStartNode = loopNode.getSubProcessStartNode(false);
				}
			}
		}
		return subprocessStartNode;
	}
	
	public List<InputLoopNodesTable> getValidInputLoopNodesTables(List<InputLoopNodesTable> inputLoopNodesTablesWithUnValid) {
    	List<InputLoopNodesTable> inputLoopNodesTables = new ArrayList<InputLoopNodesTable>();
    	if(inputLoopNodesTablesWithUnValid == null) return inputLoopNodesTables;
		for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTablesWithUnValid) {
			if(inputLoopNodesTable!=null && inputLoopNodesTable.getInputloopnodes()!=null && inputLoopNodesTable.getInputloopnodes().size()>0)
				inputLoopNodesTables.add(inputLoopNodesTable);
		}
		return inputLoopNodesTables;
    }
	
    public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	XmlMapComponent node = (XmlMapComponent) codeGenArgument.getArgument();
	GenerationManager gm =  (GenerationManager)node.initGenerationManager();
	String cid = node.getUniqueName();
	
	String uniqueNameComponent = cid.replaceAll("_TXMLMAP_IN", "");
	uniqueNameComponent = uniqueNameComponent.replaceAll("_TXMLMAP_OUT", "");
	
	boolean checkingSyntax = codeGenArgument.isCheckingSyntax();
	String cr = "\n";
	
	XmlMapData xmlMapData=(XmlMapData)ElementParameterParser.getObjectValueXMLTree(node);

	boolean isVirtual = false;
	int closeWhileCountForMultiResult = 0;
	String loopKey = null;
	List<InputLoopNodesTable> allInputLoopNodesTables = null;
	boolean parallelLoopExistInCurrentOutputTable = false;
	boolean parallelRelationExistBetweenAllOutputTables = false;
	TreeNode documentNodeInMainInputTable = null;
	ArrayList<OutputXmlTree> outputTablesSortedByReject = null;
	
	LoopHelper loopHelper = null;
	
	
	EList<InputXmlTree> inputTablesWithInvalid = xmlMapData.getInputTrees();
	EList<OutputXmlTree> outputTables = xmlMapData.getOutputTrees();
	EList<VarTable> varsTables = xmlMapData.getVarTables();

	String rejectedInnerJoin = "rejectedInnerJoin_" + cid;
	String rejectedDocInnerJoin = "rejectedDocInnerJoin_" + cid;
	String rejected = "rejected_" + cid;
	String mainRowRejected = "mainRowRejected_" + cid;
	
//	boolean isLookup = false;
	boolean isXpathFromLookup = true;
	boolean hasOutgoingConnection = false;//to output or another lookup
	boolean hasDocumentInMainInputTable = false;
	boolean atLeastOneInputTableWithInnerJoin = false;
	boolean isPlainNode = true;
//	boolean isPlainNodeInLookup = false;
	boolean hasValidLookupTables = false;
	boolean hasDocumentGlobal = false;
	
//	boolean hasMainFilter = false;
//	boolean mainFilterAlreadyDone = false;
	
	List<IConnection> outputConnections = (List<IConnection>) node.getOutgoingConnections();
	Map<String, IConnection> nameToOutputConnection = new HashMap<String, IConnection>();
    for (IConnection connection : outputConnections) {
	  		nameToOutputConnection.put(connection.getName(), connection);
	}
    
	List<IConnection> inputConnections = (List<IConnection>) node.getIncomingConnections();
	HashMap<String, IConnection> hNameToConnection = new HashMap<String, IConnection>();
	for(IConnection connection : inputConnections){
		 hNameToConnection.put(connection.getName(), connection);
	}
	
	List<InputXmlTree> inputTables = new ArrayList<InputXmlTree>();
	for(int i=0; i<inputTablesWithInvalid.size(); i++){
		InputXmlTree  currentTree = inputTablesWithInvalid.get(i);
		if(hNameToConnection.get(currentTree.getName()) != null){
			inputTables.add(currentTree);
		}
	}
	
	boolean hasDocumentInAnyLookupTable = false;
	
	int lstSizeInputs = inputTables.size();
	for(int i = 1; i<lstSizeInputs; i++){
		InputXmlTree inputTable = (InputXmlTree)inputTables.get(i);
		EList<TreeNode> treeNodes = inputTable.getNodes();
		if(treeNodes!=null) {
			for(TreeNode treeNode : treeNodes) {
				if("id_Document".equals(treeNode.getType())) {
					hasDocumentInAnyLookupTable = true;
					break;
				}
			}
		}
	}
	
//	HashMap<String, TreeNode> hInputTableNodes = new HashMap<String, TreeNode>();
	int sizeInputTables = inputTables.size();
	InputXmlTree mainInputTable = null;  // the main input table
	
//	String mainInputTableName = null;
//	IConnection realMainConnection = null;
//	java.util.Map<String, String> xpathTypeMap = null;
//	String str  = "";
//	TreeNode currentMainNode = null;
//	boolean findNext = false;

//	String nextLookupName = null;
//	List<IMetadataColumn> nextLookupColumnsKeys = new ArrayList<IMetadataColumn>();
 
//	boolean hasPersistentLookup = false;
//	int indexLastPersistentSortedTable = -1;
	 
	ArrayList<InputXmlTree> inputTablesWithInnerJoin = new ArrayList<InputXmlTree>();
	if(inputConnections==null || inputConnections.size() < 1) {
		return "";
	}
	
    stringBuffer.append(TEXT_3);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( rejectedDocInnerJoin );
    stringBuffer.append(TEXT_5);
    stringBuffer.append( mainRowRejected );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    
	if(inputTables==null || inputTables.size()==0) return "";
	for(IConnection conn : inputConnections) { // find the input main table
		InputXmlTree inputTable = inputTables.get(0);
		EConnectionType connectionType = conn.getLineStyle();
		if (connectionType == EConnectionType.FLOW_MAIN) {
			if(!inputTable.isLookup()) { // lookup is used to sign it is lookup table
    			mainInputTable = inputTable;
    			break;
			}
		}
	}
	if(mainInputTable != null) {
		
		List<TreeNode> allNodes = new ArrayList<TreeNode>();
		for(InputXmlTree inputTable : inputTables) {
			allNodes.addAll(inputTable.getNodes());
		}
		
		MatchXmlTreeExpr matchXmlTreeExpr = new MatchXmlTreeExpr(allNodes,cid);
		for(OutputXmlTree outputTable : outputTables) {
			String outputTableName = outputTable.getName();
			if(nameToOutputConnection.get(outputTableName) == null) {
				continue;
			}
			for(OutputTreeNode outputNode: outputTable.getNodes()) {
				if(("id_Document").equals(outputNode.getType())){
				
    stringBuffer.append(TEXT_8);
    stringBuffer.append(outputTableName );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(outputTableName );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(outputTableName );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(outputTableName );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(outputNode.getName());
    stringBuffer.append(TEXT_14);
    
					break;
				} // if(docuemnt)
			}// for(outputNode)
		} // for (outputXMLTree)
		
    stringBuffer.append(TEXT_15);
    
		String mainInputName = mainInputTable.getName();
		for(TreeNode tmpNode : mainInputTable.getNodes()){
			boolean isInnerJoin = true;
//			currentMainNode = tmpNode;

			if(tmpNode.getType().equals("id_Document")){
				isPlainNode = false;
			}
			
			if(("id_Document").equals(tmpNode.getType())) { // find the type:document node.
				hasDocumentInMainInputTable = true;
				//find the loop path
				XPathHelper xpathHelper = new XPathHelper(tmpNode);
				
				String loopPath = xpathHelper.getLoopNodeXPath();
				
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(loopPath );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(mainInputName );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(tmpNode.getName() );
    stringBuffer.append(TEXT_21);
    if(!xpathHelper.hasDefinedNS()){
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_33);
    }else{
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(xpathHelper.buildNSMapping("xmlNameSpaceMap"+"_"+cid));
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    }
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_51);
    stringBuffer.append( rejectedDocInnerJoin );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(mainRowRejected);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    
					Map<String,String> outNodesXPath = xpathHelper.getOutNodesXPath();
					int i = 0;
					for (Object key : outNodesXPath.keySet()) { 
					    Object val = outNodesXPath.get(key);
						if(!xpathHelper.hasDefinedNS()){
						
    stringBuffer.append(TEXT_56);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    stringBuffer.append(val );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    }else{
    stringBuffer.append(TEXT_63);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_66);
    stringBuffer.append(val);
    stringBuffer.append(TEXT_67);
    }
    stringBuffer.append(TEXT_68);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_71);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_76);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_78);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(key );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(key );
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_93);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_96);
    
						i++;
					}
	   		}// if(document)
		}// for()
		
    stringBuffer.append(TEXT_97);
    
	//	System.out.println("size of vartable begin:" + varTables.size());
	TreeUtil treeUtil = new TreeUtil();
	for (int iInputTable = 0; iInputTable < sizeInputTables; iInputTable++) {
		hasOutgoingConnection = false;//reset the var for every lookup table 
		InputXmlTree nextInputTable = inputTables.get(iInputTable);
		if(!atLeastOneInputTableWithInnerJoin) {
			if(nextInputTable.isInnerJoin()) {
				atLeastOneInputTableWithInnerJoin = true;
			}
		}

    stringBuffer.append(TEXT_98);
    stringBuffer.append(TEXT_99);
    
	EConnectionType connectionType = null;
	if (nextInputTable != null && nextInputTable.isLookup()) {//TD132 
		IConnection connection =  hNameToConnection.get(nextInputTable.getName());

		if(connection == null){
			continue;
		}else{//TD131
			connectionType = connection.getLineStyle();
			
			if(connectionType == EConnectionType.FLOW_REF) {//TD130
				IMetadataTable metadataTable = connection.getMetadataTable();
				String tableName = nextInputTable.getName();
				String lookupMode = nextInputTable.getLookupMode();
				boolean isCacheOrReloadLookupMode = org.talend.designer.xmlmap.model.tree.LOOKUP_MODE.CACHE_OR_RELOAD.name().equals(lookupMode);
				boolean isOneOfReloadLookupMode = isCacheOrReloadLookupMode ||
					   org.talend.designer.xmlmap.model.tree.LOOKUP_MODE.RELOAD.name().equals(lookupMode);
				INode lookupSubProcessStartNode = searchSubProcessStartNode(connection);
			  	String lookupSubProcessStartNodeName = lookupSubProcessStartNode.getUniqueName();
				boolean isInnerJoin = false;
				if (nextInputTable.isInnerJoin()) {
					isInnerJoin = true;
			    	inputTablesWithInnerJoin.add(nextInputTable);
			 	}
				
    stringBuffer.append(TEXT_100);
    stringBuffer.append( nextInputTable );
    stringBuffer.append(TEXT_101);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_102);
    
				String filtersConditions = nextInputTable.getExpressionFilter();
				String filtersConditionsFormatted = "";
				boolean hasExpressionFilter = nextInputTable.isActivateExpressionFilter() && filtersConditions != null && !("").equals(filtersConditions.trim());
				if(hasExpressionFilter && matchXmlTreeExpr != null) {
					filtersConditionsFormatted = matchXmlTreeExpr.generateExprCode(filtersConditions);
				}

				String expressionKey = "";
				List<IMetadataColumn> listColumns = metadataTable.getListColumns();
				ArrayList<String> keysNames = new ArrayList<String>();
				ArrayList<String> keysValues = new ArrayList<String>();
				ArrayList<String> keysTypes = new ArrayList<String>();
				EList<TreeNode> externalTreeNodes = nextInputTable.getNodes();
				
				boolean findFromBasicType = false;
				boolean findFromDocumentType = false;
				String documentColumn = null;
				boolean hasOutputFromLookupDoc = false;//to output or another lookup from Document
				Map<String,String> outNodesXPath = new HashMap<String,String>();//output contain(1.lookup table to output table 2.lookup table to lookup table)
				Map<String,String> lookupInputNodesXPath = new HashMap<String,String>();
				String loopXPath = "";
				boolean isLoopOptional = false;
				boolean isMultipleResult = false;
				boolean existXpathKeyFromCurrentLookup = false;
				boolean hasExpressionKey = false;
				//cache the type and pattern information for sub columns in Document column
				Map<String,String> xpathToPatternMap = null;
				Map<String,String> xpathToTypeMap = null;
				
				boolean unvalidLookupKeyExistForHash = false;
				List<IMetadataColumn> validLookupKeyFlatColumns = new ArrayList<IMetadataColumn>();
				
				for(int j = 0; j < listColumns.size(); j++){
					IMetadataColumn column = listColumns.get(j);
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
				    String columnName = column.getLabel();
				    TreeNode  externalInputTableEntry = externalTreeNodes.get(j);
				    XPathHelper xpathHelper;
				    if("Document".equals(typeToGenerate)) {
				    	xpathHelper = new XPathHelper(externalInputTableEntry);
						loopXPath = xpathHelper.getLoopNodeXPath();
						isLoopOptional = xpathHelper.isLoopOptional();
						outNodesXPath = xpathHelper.getOutNodesXPath();
						lookupInputNodesXPath = xpathHelper.getLookupInputNodesXPath();
						xpathToPatternMap = xpathHelper.getXpathToPatternMap();
						xpathToTypeMap = xpathHelper.getXpathToTypeMap();
						
    stringBuffer.append(TEXT_103);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_104);
    stringBuffer.append(xpathHelper.buildNSMapping("xmlNameSpaceMap"+"_"+tableName));
    stringBuffer.append(TEXT_105);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_107);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_108);
    
				    }
				    List<TreeNode> allLeaf = new ArrayList<TreeNode>();
				    treeUtil.getAllLeaf(externalInputTableEntry,allLeaf); 
				    
				    for(TreeNode leaf : allLeaf) { 
					    if (leaf != null) {
				    		if("Document".equals(typeToGenerate)) {
				    			documentColumn = columnName;
				    			EList<Connection> outputs = leaf.getOutgoingConnections();
				    			EList<LookupConnection> lookupOutputs = leaf.getLookupOutgoingConnections();
				    			EList<FilterConnection> filterOutputs = leaf.getFilterOutGoingConnections();
					    		if((outputs!=null && outputs.size()!=0) || (lookupOutputs!=null && lookupOutputs.size()!=0) || (filterOutputs!=null && filterOutputs.size()!=0)) {
					    			hasOutputFromLookupDoc = true;
					    			hasOutgoingConnection = true;
					    		}
				    		}
				    	  	if(leaf.getOutgoingConnections().size() > 0 || leaf.getLookupOutgoingConnections().size() > 0 || leaf.getFilterOutGoingConnections().size() > 0){
						    	hasOutgoingConnection = true;
						    }
						    
				    	  	expressionKey = leaf.getExpression();
							if (expressionKey != null && !"".equals(expressionKey.trim())) {
							
								//filter for valid lookup key
								if(hasDocumentInMainInputTable && (parallelLoopExistInCurrentOutputTable || parallelRelationExistBetweenAllOutputTables)) {
									if(!loopHelper.validLookupKey(leaf)) {
										continue;
									} else {
										if(!"Document".equals(typeToGenerate)) {
											validLookupKeyFlatColumns.add(column);
										}
									}
								}
								
								hasExpressionKey = true;
							    String resultExpression = null;
							    if("Document".equals(typeToGenerate)) {
							    	findFromDocumentType = true;
							    	keysNames.add(leaf.getXpath());
							    } else {
							    	keysNames.add(columnName);
							    	findFromBasicType = true;
							    }
							    if(matchXmlTreeExpr != null){
							    	resultExpression = matchXmlTreeExpr.generateExprCode(expressionKey);
							    }
							    if(resultExpression != null && !("").equals(resultExpression)){
							    	keysValues.add(resultExpression);
							    	if(resultExpression.indexOf("/") != -1) {
							    		existXpathKeyFromCurrentLookup = true;
							    	}
							    } else {
							    	keysValues.add(expressionKey);
							    }
							    keysTypes.add(typeToGenerate);
							    boolean javaPrimitiveKeyColumn = JavaTypesManager.isJavaPrimitiveType(typeToGenerate);
							}
					    }
					}
				}
				
				/////////////////////////////////////////////////////
				//set unvalidLookupKeyExistForHash
				if(hasDocumentInMainInputTable && (parallelLoopExistInCurrentOutputTable || parallelRelationExistBetweenAllOutputTables)) {
					LoopHelper nestLoopHelper = new LoopHelper(documentNodeInMainInputTable,outputTablesSortedByReject,inputTables);
					for(int ii=0;ii<allInputLoopNodesTables.size();ii++) {
						InputLoopNodesTable oneInputLoopNodesTable = allInputLoopNodesTables.get(ii);
						List<TreeNode> oneInputLoopNodes = oneInputLoopNodesTable.getInputloopnodes();
						nestLoopHelper.initForOneOutputTableLoop(oneInputLoopNodes);
						if(nextInputTable!=null && nextInputTable.isLookup()) {
							EList<TreeNode> treeNodes = nextInputTable.getNodes();
							for(TreeNode treeNode : treeNodes) {
								//only check the flat column,because hash only ready for flat column
								if(!nestLoopHelper.validLookupKey(treeNode)) {
									unvalidLookupKeyExistForHash = true;
								}
								
							}
							
						}
					}
				}
				/////////////////////////////////////////////////////
				
				isXpathFromLookup = existXpathKeyFromCurrentLookup;
				String[] aKeysNames = keysNames.toArray(new String[0]);
				String[] aKeysValues = keysValues.toArray(new String[0]);
				String className = tableName + "Struct";
				
				hasValidLookupTables = true;
				
				String matchingMode = nextInputTable.getMatchingMode();
				if(matchingMode == null) {
					if(aKeysValues.length > 0) {
						matchingMode = "UNIQUE_MATCH";
					} else {
						matchingMode = "ALL_ROWS";
					}
				} else {
					if(aKeysValues.length == 0) {
						matchingMode = "ALL_ROWS";
					}
				}
				
				boolean isAllRows = "ALL_ROWS".equals(matchingMode);
				boolean isDocumentAllRows = isAllRows && documentColumn!=null;
				
				if("ALL_ROWS".equals(matchingMode) || "ALL_MATCHES".equals(matchingMode)) {
					isMultipleResult = true;
				}
				
    stringBuffer.append(TEXT_109);
    if(hasOutgoingConnection || hasExpressionKey || isAllRows){ 
    stringBuffer.append(TEXT_110);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_111);
    stringBuffer.append( className );
    stringBuffer.append(TEXT_112);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_113);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_115);
    } 
    stringBuffer.append(TEXT_116);
    
				if(aKeysValues!=null){//TD129
				////////////////////////////////////////////////////////////////////////
					if(unvalidLookupKeyExistForHash) {
					
    stringBuffer.append(TEXT_117);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_118);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(loopKey);
    stringBuffer.append(TEXT_120);
    
						for(IMetadataColumn column : validLookupKeyFlatColumns) {
							String columnName = column.getLabel();
    						JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());

							if (JavaTypesManager.isJavaPrimitiveType(column.getTalendType(), column.isNullable())) {
							
							 	String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
							 	if(javaType == JavaTypesManager.BOOLEAN) {
								
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_122);
    
								} else {	
								
    stringBuffer.append(TEXT_123);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_124);
    
								}
								
							} else if(javaType == JavaTypesManager.BYTE_ARRAY) {
		
								
    stringBuffer.append(TEXT_125);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_126);
    
							
							} else {
							
    stringBuffer.append(TEXT_127);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_129);
    
							}
						}
					
    stringBuffer.append(TEXT_130);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_131);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_132);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_133);
    	
								for(IMetadataColumn column : validLookupKeyFlatColumns) {
									String columnName = column.getLabel();
									JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
				
									if (JavaTypesManager.isJavaPrimitiveType(column.getTalendType(), column.isNullable())) {
									
    stringBuffer.append(TEXT_134);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_135);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_136);
    
									} else if(javaType == JavaTypesManager.BYTE_ARRAY) {
									
    stringBuffer.append(TEXT_137);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_138);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_139);
    
									} else {
									
    stringBuffer.append(TEXT_140);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_141);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_142);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(columnName );
    stringBuffer.append(TEXT_144);
    
									}
				
								}

    stringBuffer.append(TEXT_145);
    
					}
					///////////////////////////////////////////////////////////////////////
				 	if(isCacheOrReloadLookupMode) {
		   			
    stringBuffer.append(TEXT_146);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_147);
    	
		  			} 
					for (int iKeyName = 0; iKeyName < aKeysNames.length; iKeyName++) {
						String typeToGenerate = keysTypes.get(iKeyName);
						boolean javaPrimitiveKeyColumn = JavaTypesManager.isJavaPrimitiveType(typeToGenerate);
			   			if("Document".equals(typeToGenerate)) {
			   				String xpath = aKeysNames[iKeyName];
			   				String relativeXPath = lookupInputNodesXPath.get(xpath);
						
    stringBuffer.append(TEXT_148);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(relativeXPath);
    stringBuffer.append(TEXT_150);
    stringBuffer.append(aKeysValues[iKeyName] );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_152);
    stringBuffer.append(relativeXPath);
    stringBuffer.append(TEXT_153);
    stringBuffer.append(xpathToPatternMap.get(xpath));
    stringBuffer.append(TEXT_154);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_155);
    stringBuffer.append(relativeXPath);
    stringBuffer.append(TEXT_156);
    stringBuffer.append(xpathToTypeMap.get(xpath));
    stringBuffer.append(TEXT_157);
    					
							if(isCacheOrReloadLookupMode) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_159);
    stringBuffer.append(aKeysValues[iKeyName] );
    stringBuffer.append(TEXT_160);
    
							}
			   			} else {
						
    stringBuffer.append(TEXT_161);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_162);
    stringBuffer.append( aKeysNames[iKeyName] );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(aKeysValues[iKeyName] );
    stringBuffer.append(TEXT_164);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_165);
    		
				   		}
					}// end for aKeysNames
					
					if(findFromBasicType && isCacheOrReloadLookupMode) {
					
    stringBuffer.append(TEXT_166);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_167);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_168);
    	
					} 
					
    stringBuffer.append(TEXT_169);
    
					if(hasOutgoingConnection || hasExpressionKey || isAllRows){
					
    stringBuffer.append(TEXT_170);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_171);
    if(isOneOfReloadLookupMode) {//TD110
								if(isCacheOrReloadLookupMode) {
							
    stringBuffer.append(TEXT_172);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_173);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_174);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_175);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_176);
    
								}
								
    stringBuffer.append(TEXT_177);
    stringBuffer.append( lookupSubProcessStartNodeName );
    stringBuffer.append(TEXT_178);
    
					            if(nextInputTable.isPersistent()) {
    stringBuffer.append(TEXT_179);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_180);
    stringBuffer.append( isAllRows ? "" : "Sorted" );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_182);
    stringBuffer.append( isAllRows ? "" : "Sorted" );
    stringBuffer.append(TEXT_183);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_184);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_185);
    
							    } else {
							    
    stringBuffer.append(TEXT_186);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_187);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_188);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_189);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_190);
    
							    }
							    
    stringBuffer.append(TEXT_191);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_192);
     if(findFromBasicType || isAllRows) { 
    stringBuffer.append(TEXT_193);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_194);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_195);
     } else if(findFromDocumentType) { 
    stringBuffer.append(TEXT_196);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_197);
     } 
						    	if(isCacheOrReloadLookupMode) {
						    	
    stringBuffer.append(TEXT_198);
    	
							    }
							}//TD110
							
    stringBuffer.append(TEXT_199);
     if(!isOneOfReloadLookupMode) {
    stringBuffer.append(TEXT_200);
    if(findFromBasicType || isAllRows) {
    stringBuffer.append(TEXT_201);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_202);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_203);
     } else if(findFromDocumentType) { 
    stringBuffer.append(TEXT_204);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_205);
     } 
    stringBuffer.append(TEXT_206);
     } 
    stringBuffer.append(TEXT_207);
    stringBuffer.append(tableName );
    stringBuffer.append(TEXT_208);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_209);
     if(isInnerJoin) {//inner join,not find and should reject data(not Document)
    stringBuffer.append(TEXT_210);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_211);
    stringBuffer.append( rejectedDocInnerJoin );
    stringBuffer.append(TEXT_212);
     } else {//left outter join should keep main table data,not reject anytime.
    stringBuffer.append(TEXT_213);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_214);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_215);
     } 
    stringBuffer.append(TEXT_216);
    if(isMultipleResult) {
    stringBuffer.append(TEXT_217);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_218);
    }
    stringBuffer.append(TEXT_219);
    if(isMultipleResult) {
    stringBuffer.append(TEXT_220);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_221);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_222);
     } 
    stringBuffer.append(TEXT_223);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_224);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_225);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_226);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_227);
     if(!isMultipleResult) {
    stringBuffer.append(TEXT_228);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_229);
    if(isCacheOrReloadLookupMode) {
    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_231);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_232);
     for (Object key : outNodesXPath.keySet()) { 
    stringBuffer.append(TEXT_233);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_236);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_237);
     } 
    stringBuffer.append(TEXT_238);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_239);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_240);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_241);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_242);
     } 
    stringBuffer.append(TEXT_243);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_244);
     if(findFromDocumentType) {
    stringBuffer.append(TEXT_245);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_246);
     } 
    stringBuffer.append(TEXT_247);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_249);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_250);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_251);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_252);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_253);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_254);
     if(findFromDocumentType || hasOutputFromLookupDoc) {
    stringBuffer.append(TEXT_255);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_256);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_257);
    stringBuffer.append(documentColumn);
    stringBuffer.append(TEXT_258);
     if("FIRST_MATCH".equals(matchingMode)) {
    stringBuffer.append(TEXT_259);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_260);
     } 
    stringBuffer.append(TEXT_261);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_262);
    
								for (Object key : outNodesXPath.keySet()) { 
									Object val = outNodesXPath.get(key);
								
    stringBuffer.append(TEXT_263);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_264);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_265);
    stringBuffer.append(val);
    stringBuffer.append(TEXT_266);
    
								}
								
    stringBuffer.append(TEXT_267);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_268);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_269);
    stringBuffer.append(loopXPath);
    stringBuffer.append(TEXT_270);
    stringBuffer.append(isLoopOptional );
    stringBuffer.append(TEXT_271);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_272);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_273);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_274);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_275);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_276);
    stringBuffer.append(matchingMode);
    stringBuffer.append(TEXT_277);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_278);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_279);
     if(findFromDocumentType) { 
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_281);
     } 
    stringBuffer.append(TEXT_282);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_283);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_285);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_286);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_287);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_289);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_290);
    if(isCacheOrReloadLookupMode) {
    stringBuffer.append(TEXT_291);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_292);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_293);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_294);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_295);
     } 
    stringBuffer.append(TEXT_296);
     if("FIRST_MATCH".equals(matchingMode)) {
    stringBuffer.append(TEXT_297);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_298);
     } 
    stringBuffer.append(TEXT_299);
     } 
    stringBuffer.append(TEXT_300);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_301);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_302);
    if(isCacheOrReloadLookupMode) {
    stringBuffer.append(TEXT_303);
    if(findFromDocumentType) {
    stringBuffer.append(TEXT_304);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_305);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_306);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_307);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_308);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_309);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_310);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_311);
     } else { 
    stringBuffer.append(TEXT_312);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_313);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_314);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_315);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_316);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_317);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_318);
     } 
    stringBuffer.append(TEXT_319);
     } 
    stringBuffer.append(TEXT_320);
     if("FIRST_MATCH".equals(matchingMode) && (findFromDocumentType || hasOutputFromLookupDoc)) {
    stringBuffer.append(TEXT_321);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_322);
     } 
    stringBuffer.append(TEXT_323);
     if(isInnerJoin && findFromDocumentType) {
    stringBuffer.append(TEXT_324);
    stringBuffer.append( rejectedDocInnerJoin );
    stringBuffer.append(TEXT_325);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_326);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_327);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_328);
    stringBuffer.append( rejectedDocInnerJoin );
    stringBuffer.append(TEXT_329);
     } 
    stringBuffer.append(TEXT_330);
     if(!isInnerJoin && findFromDocumentType) { 
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_332);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_333);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_334);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_335);
     } 
    stringBuffer.append(TEXT_336);
     if(hasOutputFromLookupDoc && !findFromDocumentType) {//for the case : not lookup doc,but doc output exists
    stringBuffer.append(TEXT_337);
     } 
    stringBuffer.append(TEXT_338);
     if(hasExpressionFilter) {
    stringBuffer.append(TEXT_339);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_340);
    stringBuffer.append(filtersConditionsFormatted );
    stringBuffer.append(TEXT_341);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_342);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_343);
    for (String key : outNodesXPath.keySet()) {
    stringBuffer.append(TEXT_344);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_345);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_346);
    }
    stringBuffer.append(TEXT_347);
     if(isInnerJoin) {
    stringBuffer.append(TEXT_348);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_349);
     } 
    stringBuffer.append(TEXT_350);
    
				  			}
				  		} 
				  		
    stringBuffer.append(TEXT_351);
    if(isMultipleResult) {//TD114
    stringBuffer.append(TEXT_352);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_353);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_354);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_355);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_356);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_357);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_358);
    if(isCacheOrReloadLookupMode) {
    stringBuffer.append(TEXT_359);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_360);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_361);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_362);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_363);
     } 
    stringBuffer.append(TEXT_364);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_365);
     if(findFromDocumentType) {
    stringBuffer.append(TEXT_366);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_367);
     } 
    stringBuffer.append(TEXT_368);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_369);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_370);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_371);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_372);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_373);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_374);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_375);
     if(findFromDocumentType || hasOutputFromLookupDoc || isDocumentAllRows) {//TA110
    stringBuffer.append(TEXT_376);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_377);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_378);
    stringBuffer.append(documentColumn);
    stringBuffer.append(TEXT_379);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_380);
    
									for (Object key : outNodesXPath.keySet()) { 
										Object val = outNodesXPath.get(key);
									
    stringBuffer.append(TEXT_381);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_382);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_383);
    stringBuffer.append(val);
    stringBuffer.append(TEXT_384);
    
									}
									
    stringBuffer.append(TEXT_385);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_386);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_387);
    stringBuffer.append(loopXPath);
    stringBuffer.append(TEXT_388);
    stringBuffer.append(isLoopOptional );
    stringBuffer.append(TEXT_389);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_390);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_391);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_392);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_393);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_394);
    stringBuffer.append(matchingMode);
    stringBuffer.append(TEXT_395);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_396);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_397);
     if(findFromDocumentType) { 
    stringBuffer.append(TEXT_398);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_399);
     } 
    stringBuffer.append(TEXT_400);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_401);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_402);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_403);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_404);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_405);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_406);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_407);
    if(isCacheOrReloadLookupMode) {
    stringBuffer.append(TEXT_408);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_409);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_410);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_411);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_412);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_413);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_414);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_415);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_416);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_417);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_418);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_419);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_420);
     } 
    stringBuffer.append(TEXT_421);
     }//TA110 
    stringBuffer.append(TEXT_422);
    if(!findFromDocumentType && !isDocumentAllRows){
    stringBuffer.append(TEXT_423);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_424);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_425);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_426);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_427);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_428);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_429);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_430);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_431);
    }
    stringBuffer.append(TEXT_432);
     if(findFromDocumentType && !isInnerJoin) {
    stringBuffer.append(TEXT_433);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_434);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_435);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_436);
     } 
    stringBuffer.append(TEXT_437);
     if(findFromDocumentType && isInnerJoin) {
    stringBuffer.append(TEXT_438);
    stringBuffer.append( rejectedDocInnerJoin );
    stringBuffer.append(TEXT_439);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_440);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_441);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_442);
    stringBuffer.append( rejectedDocInnerJoin );
    stringBuffer.append(TEXT_443);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_444);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_445);
     } 
    stringBuffer.append(TEXT_446);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_447);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_448);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_449);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_450);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_451);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_452);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_453);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_454);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_455);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_456);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_457);
     for (Object key : outNodesXPath.keySet()) { 
    stringBuffer.append(TEXT_458);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_459);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_460);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_461);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_462);
     } 
    stringBuffer.append(TEXT_463);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_464);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_465);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_466);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_467);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_468);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_469);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_470);
     if(hasExpressionFilter) {
    stringBuffer.append(TEXT_471);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_472);
    stringBuffer.append(filtersConditionsFormatted );
    stringBuffer.append(TEXT_473);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_474);
    stringBuffer.append(tableName);
    stringBuffer.append(TEXT_475);
    for (String key : outNodesXPath.keySet()) {
    stringBuffer.append(TEXT_476);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_477);
    stringBuffer.append(key);
    stringBuffer.append(TEXT_478);
    }
    stringBuffer.append(TEXT_479);
     if(isInnerJoin) {
    stringBuffer.append(TEXT_480);
    stringBuffer.append( rejectedInnerJoin );
    stringBuffer.append(TEXT_481);
     } 
    stringBuffer.append(TEXT_482);
     } 
    stringBuffer.append(TEXT_483);
    stringBuffer.append( tableName );
    stringBuffer.append(TEXT_484);
     
				  			if(isVirtual) {
				  				closeWhileCountForMultiResult++;
				  			} else {
				  				gm.addBlocksCodeToClose(new BlockCode("close loop of lookup '" + tableName +"' // G_TM_M_002"));
				  			}
				  		}//TD114 
				  		
    stringBuffer.append(TEXT_485);
    				
					}
					
    stringBuffer.append(TEXT_486);
    
				} //TD129 end of expression key checking
			}//TD130
		}//TD131
	} else {
		isXpathFromLookup = false;
	}//TD132
	
    
	} // end for lookup tables
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // VARIABLES
        // 
        
        
    stringBuffer.append(TEXT_487);
    
        
        for (VarTable varsTable : varsTables) {
            EList<VarNode> varsTableEntries = varsTable.getNodes();
            if (varsTableEntries == null) {
                continue;
            }
            String varsTableName = varsTable.getName();
            String instanceVarName = varsTableName + "__" + uniqueNameComponent;
            String className = instanceVarName + "__Struct";

            
    stringBuffer.append( cr + className + " " + varsTableName + " = " + instanceVarName + ";" );
    
            
            for (VarNode varsTableEntry : varsTableEntries) {
                String varsColumnName = varsTableEntry.getName();
                String varExpression = varsTableEntry.getExpression();
                if (varExpression == null || varExpression.trim().length() == 0) {
                    varExpression = JavaTypesManager.getDefaultValueFromJavaIdType(varsTableEntry.getType(),
                            varsTableEntry.isNullable());
                }
                
				String resultExpression = "";
				if(matchXmlTreeExpr != null)
					resultExpression = matchXmlTreeExpr.generateExprCode(varExpression);
				if(resultExpression!=null && !("").equals(resultExpression)){

    stringBuffer.append(TEXT_488);
    stringBuffer.append( cr + gm.getGeneratedCodeTableColumnVariable(uniqueNameComponent, varsTableName, varsColumnName, false) + " = " + resultExpression
                        + ";" );
    
				}
            }
        }
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // /////////////////////////////////////////////////////////////////////////////////////////////////////
        // OUTPUTS
        // 
        

    stringBuffer.append(TEXT_489);
    

		StringBuilder sb = new StringBuilder();
		
		outputTablesSortedByReject = new ArrayList<OutputXmlTree>(outputTables);
		// sorting outputs : rejects tables after not rejects table
		Collections.sort(outputTablesSortedByReject, new Comparator<OutputXmlTree>() {

			public int compare(OutputXmlTree o1, OutputXmlTree o2) {
				if (o1.isReject() != o2.isReject()) {
					if (o1.isReject()) {
						return 1;
					} else {
						return -1;
					}
				}
				if (o1.isRejectInnerJoin() != o2.isRejectInnerJoin()) {
					if (o1.isRejectInnerJoin()) {
						return 1;
					} else {
						return -1;
					}
				}
				return 0;
			}

		});
		
		boolean lastValueReject = false;
        boolean oneFilterForNotRejectTable = false;
        boolean allNotRejectTablesHaveFilter = true;
        boolean atLeastOneReject = false;
//        boolean atLeastOneRejectInnerJoin = false;
        boolean closeTestInnerJoinConditionsBracket = false;

        for (IConnection outputConnection : outputConnections) {
            nameToOutputConnection.put(outputConnection.getName(), outputConnection);
        }

        int lstSizeOutputs = outputTablesSortedByReject.size();
        // ///////////////////////////////////////////////////////////////////
        // init of allNotRejectTablesHaveFilter and atLeastOneReject
        String lastValidOutputTableName = null;
		
		for (int i = 0; i < lstSizeOutputs; i++) {
            OutputXmlTree outputTable = (OutputXmlTree) outputTablesSortedByReject.get(i);

            String outputTableName = outputTable.getName();

//            if (outputTable.isRejectInnerJoin()) {
//               atLeastOneRejectInnerJoin = true;
//            }
            EList<OutputTreeNode> columnEntries = outputTable.getNodes();
            boolean hasFilter = outputTable.isActivateExpressionFilter() && outputTable.getExpressionFilter() != null && !("").equals(outputTable.getExpressionFilter().trim());
            if (columnEntries != null && columnEntries.size() > 0) {
                if (!hasFilter && !(outputTable.isReject() || outputTable.isRejectInnerJoin())) {
                    if(!outputTable.isErrorReject()){
                    	allNotRejectTablesHaveFilter = false;
                    }
                }
                if (outputTable.isReject()) {
                    atLeastOneReject = true;
                }
            }
			IConnection outputConnection = nameToOutputConnection.get(outputTableName);            
			if (outputConnection != null) {
				sb.append(cr + outputTableName + " = null;");
			}
			if(checkingSyntax 
			|| !checkingSyntax && outputConnection != null) {
				lastValidOutputTableName = outputTableName;
			}
			

        }
        // ///////////////////////////////////////////////////////////////////
		sb.append(cr);

        if (allNotRejectTablesHaveFilter && atLeastOneReject) {
            // write rejected = false;
            sb.append(cr + "boolean " + rejected + " = true;");
        }

        // write conditions for inner join reject
        if (lastValidOutputTableName != null && hasValidLookupTables && lstSizeOutputs > 0 && atLeastOneInputTableWithInnerJoin) {
            sb.append(cr + "if(");
            sb.append("!" + rejectedInnerJoin);
            sb.append(" ) {");
            closeTestInnerJoinConditionsBracket = true;
        }

        // ///////////////////////////////////////////////////////////////////
        // run through output tables list for generating intilization of outputs arrays
        int dummyVarCounter = 0;
        for (int indexCurrentTable = 0; indexCurrentTable < lstSizeOutputs; indexCurrentTable++) {
            OutputXmlTree outputTable = (OutputXmlTree) outputTablesSortedByReject.get(indexCurrentTable);
            EList<OutputTreeNode> outputTableEntries = outputTable.getNodes();
            String outputTableName = outputTable.getName();
            
            boolean connectionExists = true;
            IConnection outputConnection =null;
            outputConnection = nameToOutputConnection.get(outputTableName);
            
            HashedMetadataTable hashedMetadataTable = null;
            if (outputTableEntries == null || outputConnection == null) {
                connectionExists = false;
            } else {
            	hashedMetadataTable = new HashedMetadataTable(outputConnection.getMetadataTable());
            }

            boolean currentIsReject = outputTable.isReject();
            boolean currentIsRejectInnerJoin = outputTable.isRejectInnerJoin();

            boolean hasExpressionFilter = outputTable.isActivateExpressionFilter() && outputTable.getExpressionFilter() != null && !("").equals(outputTable.getExpressionFilter().trim());
            boolean rejectValueHasJustChanged = lastValueReject != (currentIsReject || currentIsRejectInnerJoin);
            oneFilterForNotRejectTable = !(currentIsReject || currentIsRejectInnerJoin) && hasExpressionFilter;

            if (rejectValueHasJustChanged) {
                if (closeTestInnerJoinConditionsBracket) {
                    sb.append(cr + "} // closing inner join bracket (1)");
                    if (atLeastOneReject && allNotRejectTablesHaveFilter) {
                        sb.append(cr + " else {");
                        sb.append(cr + rejected + " = false;");
                        sb.append(cr + "} // closing else inner join bracket (1)");
                    }
                    closeTestInnerJoinConditionsBracket = false;
                }
            }

            // No connection matching and no checking errors
            if (connectionExists || checkingSyntax) {
	            if (rejectValueHasJustChanged) {
	                sb.append(cr + "// ###### START REJECTS ##### ");
	            }

	            // write filters conditions and code to execute
	            if (!currentIsReject && !currentIsRejectInnerJoin || rejectValueHasJustChanged
	                    && oneFilterForNotRejectTable || currentIsReject && allNotRejectTablesHaveFilter
	                    || currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin || checkingSyntax) {

	                boolean closeFilterOrRejectBracket = false;
	                if (currentIsReject || currentIsRejectInnerJoin) {
	                    sb.append(cr + cr + "// # Output reject table : '" + outputTableName + "'");
	                } else {
	                    sb.append(cr + cr + "// # Output table : '" + outputTableName + "'");
	                }
	                if (hasExpressionFilter || currentIsReject || currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin) {
	                    sb.append(cr + "// # Filter conditions ");
						String ifConditions = "if( ";
	                    String rejectedTests = null;
	                    if (allNotRejectTablesHaveFilter && atLeastOneReject && currentIsReject && currentIsRejectInnerJoin
	                            && atLeastOneInputTableWithInnerJoin) {
	                        rejectedTests = rejected + " || " + rejectedInnerJoin;
	                        if (hasExpressionFilter) {
	                            rejectedTests = "(" + rejectedTests + ")";
	                        }
	                    } else if (allNotRejectTablesHaveFilter && atLeastOneReject && currentIsReject) {
	                        rejectedTests = rejected;
	                    } else if (currentIsRejectInnerJoin && atLeastOneInputTableWithInnerJoin) {
	                        rejectedTests = rejectedInnerJoin;
	                    }
	                    String filtersConditions = null;
	                    filtersConditions = outputTable.getExpressionFilter();
						if(filtersConditions != null && !("".equals(filtersConditions.trim())) && (filtersConditions.trim().length()>0)) {
							String filtersConditionsFormatted = "";
							if(matchXmlTreeExpr != null)
								filtersConditionsFormatted = matchXmlTreeExpr.generateExprCode(filtersConditions);
							if (rejectedTests == null) {
								ifConditions += cr + cr + filtersConditionsFormatted  + cr + cr;
							} else {
								ifConditions += rejectedTests + " && (" + cr + cr + filtersConditionsFormatted + cr + cr +")";
							}
						} else {
							if (rejectedTests != null) {
								ifConditions += rejectedTests;
							}
						}
	                    
	                    ifConditions += " ) {";
	
	                    sb.append(cr).append(ifConditions);
	
	                    closeFilterOrRejectBracket = true;
	                    if (allNotRejectTablesHaveFilter && !(currentIsReject || currentIsRejectInnerJoin)
	                            && atLeastOneReject) {
	                        sb.append(cr + rejected + " = false;");
	                    }
	                }

	                if (outputTableEntries != null && (!currentIsReject && !currentIsRejectInnerJoin || currentIsReject || currentIsRejectInnerJoin
	                        && atLeastOneInputTableWithInnerJoin || checkingSyntax)) {
						boolean hasDocument = false;
	                    for (OutputTreeNode outputTableEntry : outputTableEntries) {
	                        String outputColumnName = outputTableEntry.getName();
	                        IMetadataColumn metadataColumnFromConn = null;
	                        if(connectionExists) {
		                        metadataColumnFromConn = hashedMetadataTable.getColumn(outputColumnName);
		                    }
	                        
	                        String outputExpression = outputTableEntry.getExpression();
							String resultExpression = "";
							if(matchXmlTreeExpr != null)
								resultExpression = matchXmlTreeExpr.generateExprCode(outputExpression);
	                        if (resultExpression == null || resultExpression.trim().length() == 0) {
	                        	if(metadataColumnFromConn == null) {
		                            resultExpression = JavaTypesManager.getDefaultValueFromJavaIdType(outputTableEntry
	    	                                .getType(), outputTableEntry.isNullable());
	    	                    } else {
		                            resultExpression = JavaTypesManager.getDefaultValueFromJavaIdType(metadataColumnFromConn
	    	                                .getTalendType(), metadataColumnFromConn.isNullable());
	    	                    }
	                        } else {
	                        
	                        	if(metadataColumnFromConn == null) {
									continue;
								}	                        
	                        
	                        }
		
	                        String assignationVar = null;
	                        if (connectionExists) {                       
	                        	assignationVar = gm.getGeneratedCodeTableColumnVariable(uniqueNameComponent, outputTableName + "_tmp",outputColumnName, false);
	                        } else {
	                        	if(metadataColumnFromConn == null) {
		                            assignationVar = JavaTypesManager.getTypeToGenerate(outputTableEntry.getType(),
	                                    outputTableEntry.isNullable())
	                                    + " dummyVar" + (dummyVarCounter++);
	                            } else {
		                            assignationVar = JavaTypesManager.getTypeToGenerate(metadataColumnFromConn
	    	                                .getTalendType(), metadataColumnFromConn.isNullable())
	                                    + " dummyVar" + (dummyVarCounter++);
	                            }
	                        }
    	                    String expression = assignationVar + " = " + resultExpression + ";";
	
							if(("id_Document").equals(outputTableEntry.getType())){
								hasDocument = true;
								hasDocumentGlobal = true;
								String typeToGenerate = JavaTypesManager.getTypeToGenerate(outputTableEntry.getType(), outputTableEntry.isNullable());
								sb.append(cr + cr +"gen_Doc_"+outputTableName+"_"+uniqueNameComponent+".generateElements(");
								if(hasDocumentInMainInputTable || hasDocumentInAnyLookupTable) {
									sb.append("treeNodeAPI_"+uniqueNameComponent+",");
								}
								sb.append(rejectedDocInnerJoin);
								for (InputXmlTree inputTable: inputTables) {
									sb.append(","+inputTable.getName());
								}
								for (VarTable var : varsTables) {
									sb.append(","+var.getName());
								}

								sb.append(");" + cr + cr);
								sb.append("if("+assignationVar + " == null){" + cr);
								sb.append(assignationVar + " = new " + typeToGenerate + "();" + cr);
								sb.append(assignationVar + ".setDocument(gen_Doc_"+outputTableName+"_"+uniqueNameComponent+".getDocument());" + cr);
								
								sb.append("}" + cr);
							} else {
								sb.append(cr).append(expression);
							}
	                    } // for entries
	
	                    if (connectionExists) {
							if(hasDocument && !isPlainNode) {
								sb.append("if(nb_line_"+uniqueNameComponent+">=nodeList_"+uniqueNameComponent+".size())");
							}
							sb.append(cr + outputTableName + " = " + outputTableName + "_tmp;");
							if(hasDocument)
								sb.append(cr + outputTableName + "_save = " + outputTableName + "_tmp;");
							
							
	                    }
	                        
	                                                
	                }
					if (closeFilterOrRejectBracket) {
						sb.append(cr + "} // closing filter/reject");
					}
				}
				lastValueReject = currentIsReject || currentIsRejectInnerJoin;
			}

			// System.out.println("Output table: (2)" + outputTableName);

			boolean isLastTable = indexCurrentTable == lstSizeOutputs - 1;
			if (closeTestInnerJoinConditionsBracket 
				&& (isLastTable || !checkingSyntax && (outputTableName.equals(lastValidOutputTableName) || lastValidOutputTableName == null))) {
				sb.append(cr + "}  // closing inner join bracket (2)");
				closeTestInnerJoinConditionsBracket = false;
			}

		} // for output tables
			
		sb.append(cr + "// ###############################");

		
		sb.append(cr);
		sb.append(cr + "} // end of Var scope");



		sb.append(cr + cr + rejectedInnerJoin + " = false;");
		
		sb.append(cr);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    stringBuffer.append(TEXT_490);
    stringBuffer.append( sb.toString());
    stringBuffer.append(TEXT_491);
    
	}
	
	if((!isPlainNode && !hasDocumentGlobal) || (hasDocumentGlobal && !isPlainNode)) {
		gm.addBlocksCodeToClose(new BlockCode("G_TXM_M_001 close"));

    stringBuffer.append(TEXT_492);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_493);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_494);
    
			boolean flat = true;
			for (OutputXmlTree table : outputTables) {        
				EList<OutputTreeNode> tableEntries = table.getNodes();
				if (tableEntries == null || nameToOutputConnection.get(table.getName()) == null) {
					continue;
				}
				for(OutputTreeNode tableEntry : tableEntries) {
					if(("id_Document").equals(tableEntry.getType())){
						flat = false;
						break;
					}
				}
				
				String tableName1 = table.getName();
            
				String instanceVarName = tableName1 + "__" + node.getUniqueName();
				String className = tableName1 + "Struct";
				if(!flat) {
            

    stringBuffer.append(TEXT_495);
    stringBuffer.append( tableName1 );
    stringBuffer.append(TEXT_496);
    stringBuffer.append( tableName1 );
    stringBuffer.append(TEXT_497);
    
				}
			}

    stringBuffer.append(TEXT_498);
    
	}

    stringBuffer.append(TEXT_499);
    return stringBuffer.toString();
  }
    class LoopHelper {
	
		//all used input loops(if the input loop is no used in any output table,we treat as no loop node)
		List<TreeNode> inputLoopsInAllOutputTables = new ArrayList<TreeNode>();
		
		//all output tables
		List<OutputXmlTree> outputTables;
		
		//all input tables
		List<InputXmlTree> inputTables;
		
		TreeNode inputRootOfMainInputTableDocument;
		
		public LoopHelper(TreeNode inputRoot,List<OutputXmlTree> outputTables,List<InputXmlTree> inputTables) {
			this.inputRootOfMainInputTableDocument = inputRoot;
			this.outputTables = outputTables;
			this.inputTables = inputTables;
			
			for(OutputXmlTree outputTable : outputTables) {
				List<InputLoopNodesTable> inputLoopNodesTables = getValidInputLoopNodesTables(outputTable.getInputLoopNodesTables());
				for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTables) {
					List<TreeNode> inputLoopNodes = inputLoopNodesTable.getInputloopnodes();
					for(TreeNode node : inputLoopNodes) {
						if(!inputLoopsInAllOutputTables.contains(node)) {
							inputLoopsInAllOutputTables.add(node);
						}
					}
					
				}
			}
		}
		
		//all input loops used by current output table
		List<TreeNode> inputLoopsInCurrentOutputTable;
		
		//for output mapping belong to unique input loop
		public Map<TreeNode,TreeNode> outputNodeToLoopNode;
		
		//need a var for lookup output mapping belong to input loop
		
		boolean parallelLoopExistInCurrentOutputTable;
		
		boolean nestedLoopExistInCurrentOutputTable;
		
		//set current output table and init
		public void initForOneOutputTable(OutputXmlTree currentOutputTable) {
			inputLoopsInCurrentOutputTable = new ArrayList<TreeNode>();
			parallelLoopExistInCurrentOutputTable = false;
			nestedLoopExistInCurrentOutputTable = false;
			outputNodeToLoopNode = new HashMap<TreeNode,TreeNode>();
			
			List<InputLoopNodesTable> inputLoopNodesTables = getInputLoopNodesTables(currentOutputTable);
			if(inputLoopNodesTables.size() > 1) {
				parallelLoopExistInCurrentOutputTable = true;
			}
			for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTables) {
				List<TreeNode> inputLoopNodes = inputLoopNodesTable.getInputloopnodes();
				if(inputLoopNodes.size() > 1) {
					nestedLoopExistInCurrentOutputTable = true;
				}
				for(TreeNode inputLoopNode : inputLoopNodes) {
					if(!inputLoopsInCurrentOutputTable.contains(inputLoopNode)) {
						inputLoopsInCurrentOutputTable.add(inputLoopNode);
					}
				}
			}
			if(parallelLoopExistInCurrentOutputTable || nestedLoopExistInCurrentOutputTable) {
				setLoopMapping();
			}
		}
		
		private List<InputLoopNodesTable> getInputLoopNodesTables(OutputXmlTree currentOutputTable) {
			List<InputLoopNodesTable> result = new ArrayList<InputLoopNodesTable>();
			
			List<OutputTreeNode> loopNodes = new ArrayList<OutputTreeNode>();
			boolean docExist = false;
			for(OutputTreeNode currentNode : currentOutputTable.getNodes()) {
				if("id_Document".equals(currentNode.getType())) {
					getAllLoopNodes(currentNode,loopNodes);
					docExist = true;
				}
			}
			
			if(docExist) {
				for(OutputTreeNode loop : loopNodes) {
					if(loop.getInputLoopNodesTable()!=null) {
						result.add(loop.getInputLoopNodesTable());
					}
				}
			} else {//only flat column exist
				result.addAll(getValidInputLoopNodesTables(currentOutputTable.getInputLoopNodesTables()));
			}
			
			return result;
		}
		
		List<TreeNode> currentInputLoopNodes;
		//filter for lookup table
		List<TreeNode> noUsedInputLoopsForCurrentOutputTableLoop;
		List<TreeNode> noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop;
		//set current output loop of current output table  and init
		public void initForOneOutputTableLoop(List<TreeNode> inputLoopNodes) {
			noUsedInputLoopsForCurrentOutputTableLoop = new ArrayList<TreeNode>();
			noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop = new ArrayList<TreeNode>();
			currentInputLoopNodes = inputLoopNodes;
			for(TreeNode inputLoopNode : inputLoopsInAllOutputTables) {
				if(!currentInputLoopNodes.contains(inputLoopNode)) {
					noUsedInputLoopsForCurrentOutputTableLoop.add(inputLoopNode);
					insideOfLoop(inputLoopNode);
				}
			}
		}
		
		private void insideOfLoop(TreeNode loopNode) {
			if(asOutput(loopNode)) {
				noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop.add(loopNode);
			}
			for(TreeNode childNode : loopNode.getChildren()) {
				insideOfLoop(childNode);
			}
		}
	
		/**
		 * used when parallel loop exist in current output table or  parallel relation exist between all output tables
		 * @param nodeOfLookupTable
		 * @return
		 */
		public boolean validLookupKey(TreeNode nodeOfLookupTable) {
			List<LookupConnection> lookupIncomings = nodeOfLookupTable.getLookupIncomingConnections();
			if(lookupIncomings.size()>0) {
				LookupConnection lookupIncoming = lookupIncomings.get(0);
				TreeNode source = (TreeNode)lookupIncoming.getSource();
				return !noUsedLookupNodesForAllLookupTableToCurrentOutputTableLoop.contains(source);
			}
			return true;
		}
		
		/**
		 * for the output mapping
		 */
		private void setLoopMapping() {
			for(TreeNode inputLoop : inputLoopsInCurrentOutputTable) {
				initInsideOfLoop(inputLoop,inputLoop);
			}
			initOutsideOfLoops(this.inputRootOfMainInputTableDocument);
		}
		
		private void initInsideOfLoop(TreeNode loopNode,TreeNode node) {
			if(asOutput(node)) {
				outputNodeToLoopNode.put(node, loopNode);
			}
			for(TreeNode childNode : node.getChildren()) {
				initInsideOfLoop(loopNode,childNode);
			}
		}
		
		private void initOutsideOfLoops(TreeNode node) {
			if(node.isLoop()) {
				return;
			}
			if(asOutput(node)) {
				outputNodeToLoopNode.put(node,null);
			}
			for(TreeNode childNode : node.getChildren()) {
				initOutsideOfLoops(childNode);
			}
		}
		
		//for output mapping 
		public boolean belongToCurrentLoop(TreeNode inputLoopNode,String xpath,boolean isMainInputLoopInNestLoop) {
			if(parallelLoopExistInCurrentOutputTable || nestedLoopExistInCurrentOutputTable) {
				for(Map.Entry<TreeNode, TreeNode> entry : outputNodeToLoopNode.entrySet()) {
					TreeNode outputNode = entry.getKey();
					TreeNode loopNode = entry.getValue();
					if(xpath.equals(outputNode.getXpath())) {
						if(isMainInputLoopInNestLoop) {
							if(loopNode == null) {//outside of any input loop
								return true;
							}
							return loopNode == inputLoopNode;
						} else {
							if(loopNode == null) {
								return false;
							}
							return loopNode == inputLoopNode;
						}
					}
				}
			}
			return true;
		}
		
		private boolean asOutput(TreeNode currentNode) {
			return currentNode.getOutgoingConnections().size()>0 || currentNode.getLookupOutgoingConnections().size()>0 || currentNode.getFilterOutGoingConnections().size()>0;
		}
		
		private List<InputLoopNodesTable> getValidInputLoopNodesTables(List<InputLoopNodesTable> inputLoopNodesTablesWithUnValid) {
	    	List<InputLoopNodesTable> inputLoopNodesTables = new ArrayList<InputLoopNodesTable>();
	    	if(inputLoopNodesTablesWithUnValid == null) return inputLoopNodesTables;
			for(InputLoopNodesTable inputLoopNodesTable : inputLoopNodesTablesWithUnValid) {
				if(inputLoopNodesTable!=null && inputLoopNodesTable.getInputloopnodes()!=null && inputLoopNodesTable.getInputloopnodes().size()>0)
					inputLoopNodesTables.add(inputLoopNodesTable);
			}
			return inputLoopNodesTables;
	    }
		
		private void getAllLoopNodes(OutputTreeNode node,List<OutputTreeNode> result) {
			if(node == null) {
				return;
			}
			if(node.isLoop()){
				result.add(node);
				return;
			}
			for(TreeNode child : node.getChildren()){
				getAllLoopNodes((OutputTreeNode)child,result);
			}
		}
		
	}
}
