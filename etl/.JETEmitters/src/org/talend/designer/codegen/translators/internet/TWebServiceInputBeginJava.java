package org.talend.designer.codegen.translators.internet;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.Map;
import java.util.List;
import org.talend.core.model.utils.TalendTextUtils;

public class TWebServiceInputBeginJava
{
  protected static String nl;
  public static synchronized TWebServiceInputBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TWebServiceInputBeginJava result = new TWebServiceInputBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "/////////////////////////////////// " + NL + "\t        System.setProperty(\"org.apache.commons.logging.Log\", \"org.apache.commons.logging.impl.NoOpLog\");" + NL + "\t        Object[] params_";
  protected final String TEXT_3 = " = new Object[] {" + NL + "\t        ";
  protected final String TEXT_4 = ", " + NL + "\t        ";
  protected final String TEXT_5 = ",      " + NL + "\t\t\t\t";
  protected final String TEXT_6 = NL + "\t\t    \t    ";
  protected final String TEXT_7 = "," + NL + "\t\t\t\t";
  protected final String TEXT_8 = "      " + NL + "\t        };" + NL + "\t\t\t";
  protected final String TEXT_9 = "   " + NL + "\t\t        System.setProperty(\"java.protocol.handler.pkgs\", \"com.sun.net.ssl.internal.www.protocol\");" + NL + "\t\t" + NL + "\t\t\t\tSystem.setProperty(\"javax.net.ssl.trustStore\", ";
  protected final String TEXT_10 = ");" + NL + "\t\t\t\tSystem.setProperty(\"javax.net.ssl.trustStorePassword\", ";
  protected final String TEXT_11 = ");" + NL + "\t\t\t";
  protected final String TEXT_12 = "        \t   " + NL + "        \t\torg.talend.DynamicInvoker.setAuth(true, ";
  protected final String TEXT_13 = ", ";
  protected final String TEXT_14 = "); \t\t" + NL + "\t\t\t";
  protected final String TEXT_15 = NL + "\t\t\t\torg.talend.DynamicInvoker.setWINAuth(true, ";
  protected final String TEXT_16 = ", ";
  protected final String TEXT_17 = "); \t  \t\t" + NL + "\t\t\t";
  protected final String TEXT_18 = "  " + NL + "\t\t\t\torg.talend.DynamicInvoker.setHttpProxy(true, ";
  protected final String TEXT_19 = ", ";
  protected final String TEXT_20 = ", ";
  protected final String TEXT_21 = ", ";
  protected final String TEXT_22 = "); " + NL + "\t\t\t";
  protected final String TEXT_23 = " " + NL + "\t\t\torg.talend.DynamicInvoker.setTimeOut(";
  protected final String TEXT_24 = ");" + NL + "\t\t" + NL + "\t \t\torg.talend.DynamicInvoker.main(params_";
  protected final String TEXT_25 = ");" + NL + "\t \t\tjava.util.Map result_";
  protected final String TEXT_26 = " = org.talend.DynamicInvoker.getResult();" + NL + "\t        " + NL + "\t        Object[] results_";
  protected final String TEXT_27 = " = null;" + NL + "\t\t    int nb_line_";
  protected final String TEXT_28 = " = 0;" + NL + "\t\t" + NL + "\t        for (Object key_";
  protected final String TEXT_29 = " : result_";
  protected final String TEXT_30 = ".keySet()) {" + NL + "        " + NL + "\t           results_";
  protected final String TEXT_31 = " = new Object[";
  protected final String TEXT_32 = "];" + NL + "\t            " + NL + "\t           Object value_";
  protected final String TEXT_33 = " = result_";
  protected final String TEXT_34 = ".get(key_";
  protected final String TEXT_35 = ");" + NL + "\t           if (value_";
  protected final String TEXT_36 = " instanceof Object[]){" + NL + "\t                Object[] objArr_";
  protected final String TEXT_37 = " = (Object[])value_";
  protected final String TEXT_38 = ";" + NL + "\t                int len_";
  protected final String TEXT_39 = " = Math.min(objArr_";
  protected final String TEXT_40 = ".length, results_";
  protected final String TEXT_41 = ".length);" + NL + "\t                int k_";
  protected final String TEXT_42 = " = 0;" + NL + "\t                for (int i_";
  protected final String TEXT_43 = " = 0; i_";
  protected final String TEXT_44 = " < len_";
  protected final String TEXT_45 = "; i_";
  protected final String TEXT_46 = "++ ) {" + NL + "\t                    results_";
  protected final String TEXT_47 = "[k_";
  protected final String TEXT_48 = "] = objArr_";
  protected final String TEXT_49 = "[k_";
  protected final String TEXT_50 = "];" + NL + "\t                }" + NL + "                " + NL + "\t\t        } else if (value_";
  protected final String TEXT_51 = " instanceof java.util.List) {" + NL + "\t\t        \tjava.util.List list_";
  protected final String TEXT_52 = " = (java.util.List)value_";
  protected final String TEXT_53 = ";" + NL + "\t\t            int len_";
  protected final String TEXT_54 = " = Math.min(list_";
  protected final String TEXT_55 = ".size(), results_";
  protected final String TEXT_56 = ".length);" + NL + "\t\t            int k_";
  protected final String TEXT_57 = " = 0;" + NL + "\t\t            for (java.util.Iterator iter_";
  protected final String TEXT_58 = " = list_";
  protected final String TEXT_59 = ".iterator(); iter_";
  protected final String TEXT_60 = ".hasNext() && k_";
  protected final String TEXT_61 = " < len_";
  protected final String TEXT_62 = "; k_";
  protected final String TEXT_63 = "++) {" + NL + "\t\t            \tresults_";
  protected final String TEXT_64 = "[k_";
  protected final String TEXT_65 = "] = iter_";
  protected final String TEXT_66 = ".next();" + NL + "\t\t            }" + NL + "                                " + NL + "           \t\t} else if (value_";
  protected final String TEXT_67 = " instanceof java.util.Map) {                " + NL + "\t\t\t\t\tjava.util.Map map_";
  protected final String TEXT_68 = " = (java.util.Map)value_";
  protected final String TEXT_69 = ";" + NL + "\t                java.util.Collection values_";
  protected final String TEXT_70 = " = map_";
  protected final String TEXT_71 = ".values();" + NL + "\t                int len_";
  protected final String TEXT_72 = " = Math.min(values_";
  protected final String TEXT_73 = ".size(), results_";
  protected final String TEXT_74 = ".length);" + NL + "\t                int k_";
  protected final String TEXT_75 = " = 0;" + NL + "\t                for (java.util.Iterator iter_";
  protected final String TEXT_76 = " = values_";
  protected final String TEXT_77 = ".iterator(); iter_";
  protected final String TEXT_78 = ".hasNext() && k_";
  protected final String TEXT_79 = " < len_";
  protected final String TEXT_80 = "; k_";
  protected final String TEXT_81 = "++) {" + NL + "\t                    results_";
  protected final String TEXT_82 = "[k_";
  protected final String TEXT_83 = "] = iter_";
  protected final String TEXT_84 = ".next();" + NL + "\t                }" + NL + "" + NL + "            \t} else if (value_";
  protected final String TEXT_85 = " instanceof org.w3c.dom.Element) {" + NL + "                \tresults_";
  protected final String TEXT_86 = "[0] = org.apache.axis.utils.XMLUtils.ElementToString((org.w3c.dom.Element)value_";
  protected final String TEXT_87 = ");" + NL + "                " + NL + "\t            }else if (value_";
  protected final String TEXT_88 = " instanceof org.apache.axis.types.Schema){" + NL + "\t                org.apache.axis.types.Schema schema_";
  protected final String TEXT_89 = " = (org.apache.axis.types.Schema) value_";
  protected final String TEXT_90 = ";" + NL + "\t                org.apache.axis.message.MessageElement[] _any_";
  protected final String TEXT_91 = " = schema_";
  protected final String TEXT_92 = ".get_any();" + NL + "\t                for (int s_";
  protected final String TEXT_93 = " = 0; s_";
  protected final String TEXT_94 = " < _any_";
  protected final String TEXT_95 = ".length; s_";
  protected final String TEXT_96 = "++) {" + NL + "\t                    results_";
  protected final String TEXT_97 = "[s_";
  protected final String TEXT_98 = "] = _any_";
  protected final String TEXT_99 = "[s_";
  protected final String TEXT_100 = "].toString();" + NL + "\t                }\t" + NL + "\t                " + NL + "\t            } else {" + NL + "\t\t\t\t    results_";
  protected final String TEXT_101 = "[0] = value_";
  protected final String TEXT_102 = ";" + NL + "\t            }" + NL + "            " + NL + "\t\t";
  protected final String TEXT_103 = NL + "\t        Object[] results_";
  protected final String TEXT_104 = " = null;" + NL + "\t\t    int nb_line_";
  protected final String TEXT_105 = " = 0;" + NL + "\t\t    " + NL + "\t\t    org.talend.TalendJobHTTPClientConfiguration clientConfig_";
  protected final String TEXT_106 = " = new org.talend.TalendJobHTTPClientConfiguration();" + NL + "\t\t\tclientConfig_";
  protected final String TEXT_107 = ".setTimeout(";
  protected final String TEXT_108 = ");" + NL + "\t\t\t" + NL + "\t        org.talend.TalendJob talendJob_";
  protected final String TEXT_109 = " = new org.talend.TalendJobProxy(";
  protected final String TEXT_110 = ");" + NL + "\t\t\ttalendJob_";
  protected final String TEXT_111 = ".setClientConfig(clientConfig_";
  protected final String TEXT_112 = ");\t        " + NL + "\t        String[][] runJob_";
  protected final String TEXT_113 = " = talendJob_";
  protected final String TEXT_114 = ".runJob(new String[]{" + NL + "\t\t\t";
  protected final String TEXT_115 = NL + "        \t\t";
  protected final String TEXT_116 = "," + NL + "\t\t\t";
  protected final String TEXT_117 = " " + NL + "        \t});" + NL + "\t        for (String[] item_";
  protected final String TEXT_118 = " : runJob_";
  protected final String TEXT_119 = ") {" + NL + "\t\t\t\tresults_";
  protected final String TEXT_120 = " = item_";
  protected final String TEXT_121 = ";" + NL + "\t\t";
  protected final String TEXT_122 = "            " + NL + "\t" + NL + "        nb_line_";
  protected final String TEXT_123 = "++;" + NL + "\t       " + NL + "// for output";
  protected final String TEXT_124 = NL + NL + "\t\t\t";
  protected final String TEXT_125 = NL + "\t\t\t\t\t\t" + NL + "\t\t\t" + NL + "\t\t\tif(";
  protected final String TEXT_126 = " < results_";
  protected final String TEXT_127 = ".length && results_";
  protected final String TEXT_128 = "[";
  protected final String TEXT_129 = "]!=null){\t\t\t\t";
  protected final String TEXT_130 = NL + "\t\t\t\t\t";
  protected final String TEXT_131 = ".";
  protected final String TEXT_132 = " = results_";
  protected final String TEXT_133 = "[";
  protected final String TEXT_134 = "];\t\t\t\t\t";
  protected final String TEXT_135 = NL + "\t\t\t\t\t";
  protected final String TEXT_136 = ".";
  protected final String TEXT_137 = " = results_";
  protected final String TEXT_138 = "[";
  protected final String TEXT_139 = "].toString();";
  protected final String TEXT_140 = "\t\t\t\t\t" + NL + "\t\t\t\t\tif(results_";
  protected final String TEXT_141 = "[";
  protected final String TEXT_142 = "] instanceof java.util.GregorianCalendar) {" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_143 = ".";
  protected final String TEXT_144 = " = ((java.util.GregorianCalendar)results_";
  protected final String TEXT_145 = "[";
  protected final String TEXT_146 = "]).getTime();" + NL + "                   \t}else{" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_147 = ".";
  protected final String TEXT_148 = " = ParserUtils.parseTo_Date(results_";
  protected final String TEXT_149 = "[";
  protected final String TEXT_150 = "].toString(), ";
  protected final String TEXT_151 = ");" + NL + "\t\t\t\t\t}";
  protected final String TEXT_152 = NL + "\t\t\t\t\t";
  protected final String TEXT_153 = ".";
  protected final String TEXT_154 = " = results_";
  protected final String TEXT_155 = "[";
  protected final String TEXT_156 = "].toString().getBytes();" + NL + "\t";
  protected final String TEXT_157 = "\t\t\t\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_158 = ".";
  protected final String TEXT_159 = " = ParserUtils.parseTo_";
  protected final String TEXT_160 = "(results_";
  protected final String TEXT_161 = "[";
  protected final String TEXT_162 = "].toString());";
  protected final String TEXT_163 = NL + "\t\t\t" + NL + "\t\t\t} else { " + NL + "\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_164 = ".";
  protected final String TEXT_165 = " = ";
  protected final String TEXT_166 = ";" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\t";
  protected final String TEXT_167 = NL + "      \t\t\t";
  protected final String TEXT_168 = ".";
  protected final String TEXT_169 = " = ";
  protected final String TEXT_170 = ".";
  protected final String TEXT_171 = ";" + NL + "\t\t\t\t ";
  protected final String TEXT_172 = NL + "///////////////////////////////////       ";
  protected final String TEXT_173 = NL + " ";
  protected final String TEXT_174 = NL;
  protected final String TEXT_175 = NL + "\tint nb_line_";
  protected final String TEXT_176 = " = 0;" + NL + "\t";
  protected final String TEXT_177 = NL + "    ";
  protected final String TEXT_178 = NL + "    " + NL + "    nb_line_";
  protected final String TEXT_179 = "++;";
  protected final String TEXT_180 = NL;
  protected final String TEXT_181 = " ";
  protected final String TEXT_182 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
// component id
String cid = node.getUniqueName();

if(("false").equals(ElementParameterParser.getValue(node,"__ADVANCED_USE__"))) {
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
     
        List<IMetadataColumn> listColumns = metadata.getListColumns(); 
        
        List<Map<String, String>> params = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node,"__PARAMS__");
        
        boolean needAuth = ("true").equals(ElementParameterParser.getValue(node,"__NEED_AUTH__"));
        String username = ElementParameterParser.getValue(node,"__AUTH_USERNAME__");
        String password = ElementParameterParser.getValue(node,"__AUTH_PASSWORD__");
        
        boolean winAuth = ("true").equals(ElementParameterParser.getValue(node,"__WIN_AUTH__"));
        String domain = ElementParameterParser.getValue(node,"__DOMAIN__");
        
        boolean useProxy = ("true").equals(ElementParameterParser.getValue(node,"__UES_PROXY__"));
        String proxuHost = ElementParameterParser.getValue(node,"__PROXY_HOST__");
        String proxyPort = ElementParameterParser.getValue(node,"__PROXY_PORT__");
        String proxuUser = ElementParameterParser.getValue(node,"__PROXY_USERNAME__");
        String proxyPassword = ElementParameterParser.getValue(node,"__PROXY_PASSWORD__");
        
        boolean needSSLtoTrustServer = ("true").equals(ElementParameterParser.getValue(node,"__NEED_SSL_TO_TRUSTSERVER__"));
        String trustStoreFile = ElementParameterParser.getValue(node,"__SSL_TRUSTSERVER_TRUSTSTORE__");
        String trustStorePassword = ElementParameterParser.getValue(node,"__SSL_TRUSTSERVER_PASSWORD__");
        
        
        String timeoutStr = ElementParameterParser.getValue(node,"__TIMEOUT__");
		String timeout = (timeoutStr!=null&&!("").equals(timeoutStr))?timeoutStr:"20";
		
        String endpoint = ElementParameterParser.getValue(node,"__ENDPOINT__");
        String method = ElementParameterParser.getValue(node,"__METHOD__");
        if(method != null && !("\"runJob\"").equals(method.trim())) {

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(endpoint );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(method );
    stringBuffer.append(TEXT_5);
    
		        for (int i = 0; i < params.size(); i++) {
		            Map<String, String> line = params.get(i);
				
    stringBuffer.append(TEXT_6);
    stringBuffer.append( line.get("VALUE") );
    stringBuffer.append(TEXT_7);
    
		        }
				
    stringBuffer.append(TEXT_8);
     
			if (needSSLtoTrustServer) {
			
    stringBuffer.append(TEXT_9);
    stringBuffer.append(trustStoreFile );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(trustStorePassword );
    stringBuffer.append(TEXT_11);
     
			}
			if (needAuth&&!winAuth) {
			
    stringBuffer.append(TEXT_12);
    stringBuffer.append(username );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(password );
    stringBuffer.append(TEXT_14);
    
  			}
  			if (needAuth&&winAuth){
  				String domain_username = "\""+TalendTextUtils.removeQuotes(domain)+"\\\\"+TalendTextUtils.removeQuotes(username)+"\"";
				
    stringBuffer.append(TEXT_15);
    stringBuffer.append(domain_username );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(password );
    stringBuffer.append(TEXT_17);
    
  			}
			if (useProxy) {
			
    stringBuffer.append(TEXT_18);
    stringBuffer.append(proxuHost );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(proxyPort );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(proxuUser );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(proxyPassword);
    stringBuffer.append(TEXT_22);
    
  			}
			
    stringBuffer.append(TEXT_23);
    stringBuffer.append(timeout );
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
    stringBuffer.append(listColumns.size()==0? 1 : listColumns.size());
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid );
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid );
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_102);
    } else { 
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_107);
    stringBuffer.append(timeout );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_109);
    stringBuffer.append(endpoint );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_114);
    
	        for (int i = 0; i < params.size(); i++) {
	            Map<String, String> line = params.get(i);
				
    stringBuffer.append(TEXT_115);
    stringBuffer.append( line.get("VALUE") );
    stringBuffer.append(TEXT_116);
    
        	}
			
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_121);
    }
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_123);
    
	List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
	String firstConnName = "";
	if (conns!=null) {//1
		if (conns.size()>0) {//2
		
			IConnection conn = conns.get(0); //the first connection
			firstConnName = conn.getName();			
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//3

				
    stringBuffer.append(TEXT_124);
    
			List<IMetadataColumn> columns=metadata.getListColumns();
			int columnSize = columns.size();
			for (int i=0;i<columnSize;i++) {//4
					IMetadataColumn column=columns.get(i);
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
			
    stringBuffer.append(TEXT_125);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_127);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_129);
    
					if(javaType == JavaTypesManager.OBJECT){//Object

    stringBuffer.append(TEXT_130);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_134);
    
					}else if(javaType == JavaTypesManager.STRING) { //String 

    stringBuffer.append(TEXT_135);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_138);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_139);
    
					} else if(javaType == JavaTypesManager.DATE) { //Date

    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_142);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_145);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_146);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_147);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_150);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_151);
    
					} else if(javaType == JavaTypesManager.BYTE_ARRAY) { //byte[]

    stringBuffer.append(TEXT_152);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_156);
    
					} else  { //other

    stringBuffer.append(TEXT_157);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_158);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_159);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_161);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_162);
    
					}

    stringBuffer.append(TEXT_163);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_164);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_165);
    stringBuffer.append(JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate));
    stringBuffer.append(TEXT_166);
    			
			} //4
		}//3
		
		
		if (conns.size()>1) {
			for (int i=1;i<conns.size();i++) {
				IConnection conn2 = conns.get(i);
				if ((conn2.getName().compareTo(firstConnName)!=0)&&(conn2.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA))) {
			    	for (IMetadataColumn column: metadata.getListColumns()) {
    stringBuffer.append(TEXT_167);
    stringBuffer.append(conn2.getName() );
    stringBuffer.append(TEXT_168);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(firstConnName );
    stringBuffer.append(TEXT_170);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_171);
    
				 	}
				}
			}
		}
		
		
	}//2
	
}//1

    stringBuffer.append(TEXT_172);
    
  }
}  
 
    stringBuffer.append(TEXT_173);
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}else{//the following is the use the wsdl2java
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    stringBuffer.append(TEXT_174);
    
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
        String code = ElementParameterParser.getValue(node, "__CODE__");
        
        // we give a default value to prevComponentName so that no error
        // occur when the user tries to generated Java code while no input
        // component was linked to our tJavaRow.        
        String outputRowName = new String("output_row");
        
        List< ? extends IConnection> outConns = node.getOutgoingConnections();
        if (outConns != null && !outConns.isEmpty()) {
            IConnection outConn = outConns.get(0);
            outputRowName = outConn.getName();
        }

        // In case the user would make some tricky Java things, he can use
        // the arrays as a whole.

        code = code.replaceAll(
            "output_row",
            outputRowName
        );


    stringBuffer.append(TEXT_175);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_176);
    stringBuffer.append(TEXT_177);
    stringBuffer.append(code);
    stringBuffer.append(TEXT_178);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_179);
    
    }
}

    stringBuffer.append(TEXT_180);
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  }

    stringBuffer.append(TEXT_181);
    stringBuffer.append(TEXT_182);
    return stringBuffer.toString();
  }
}
