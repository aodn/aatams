package org.talend.designer.codegen.translators.esb.webservices;

import org.talend.core.model.process.INode;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TESBConsumerBeginJava
{
  protected static String nl;
  public static synchronized TESBConsumerBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TESBConsumerBeginJava result = new TESBConsumerBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "javax.xml.namespace.QName serviceName_";
  protected final String TEXT_2 = " = null;" + NL + "javax.xml.namespace.QName portName_";
  protected final String TEXT_3 = " = null;" + NL + "org.talend.ws.helper.ServiceInvokerHelper serviceInvokerHelper_";
  protected final String TEXT_4 = " = null ;" + NL + "" + NL + "ESBConsumer consumer_";
  protected final String TEXT_5 = " = null;" + NL + "if (this.registry != null) {" + NL + "" + NL + "\tfinal java.util.Map<String, String> slCustomProps_";
  protected final String TEXT_6 = " = new java.util.HashMap<String, String>();";
  protected final String TEXT_7 = NL + "\t\tslCustomProps_";
  protected final String TEXT_8 = ".put(";
  protected final String TEXT_9 = ", ";
  protected final String TEXT_10 = ");" + NL + "\t";
  protected final String TEXT_11 = NL + NL + "\tconsumer_";
  protected final String TEXT_12 = " = this.registry.createConsumer(" + NL + "\t\tnew ESBEndpointInfo() {" + NL + "" + NL + "\t\t\t@SuppressWarnings(\"serial\")" + NL + "\t\t\tprivate java.util.Map<String, Object> props = new java.util.HashMap<String, Object>() {{" + NL + "\t\t\t\tput(\"wsdlURL\", ";
  protected final String TEXT_13 = ");" + NL + "\t\t\t\tput(\"dataFormat\", \"PAYLOAD\");" + NL + "\t\t\t\tput(\"portName\", \"{";
  protected final String TEXT_14 = "}";
  protected final String TEXT_15 = "\");" + NL + "\t\t\t\tput(\"serviceName\", \"{";
  protected final String TEXT_16 = "}";
  protected final String TEXT_17 = "\");" + NL + "\t\t\t\tput(\"defaultOperationName\", \"";
  protected final String TEXT_18 = "\");" + NL + "\t\t\t\tput(\"defaultOperationNameSpace\", \"\");" + NL + "\t\t\t\tput(\"soapAction\", \"";
  protected final String TEXT_19 = "\");" + NL + "\t\t\t\tput(\"publishedEndpointUrl\", ";
  protected final String TEXT_20 = ");" + NL + "\t\t\t\tput(\"COMMUNICATION_STYLE\", \"";
  protected final String TEXT_21 = "\");" + NL + "" + NL + "\t\t\t\t// use Service Locator" + NL + "\t\t\t\tput(\"useServiceLocator\", ";
  protected final String TEXT_22 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_23 = NL + "\t\t\t\t\tput(\"LocatorSelectionStrategy\", \"";
  protected final String TEXT_24 = "\");" + NL + "\t\t\t\t\tput(\"SL-PROPS\", slCustomProps_";
  protected final String TEXT_25 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_26 = NL + " \t\t\t\t// use Service Activity Monitor" + NL + "\t\t\t\tput(\"useServiceActivityMonitor\", ";
  protected final String TEXT_27 = ");" + NL + "" + NL + "\t\t\t\t";
  protected final String TEXT_28 = NL + "\t\t\t\t\tput(\"esbSecurity\", \"";
  protected final String TEXT_29 = "\");" + NL + "\t\t\t\t\tput(\"username\", ";
  protected final String TEXT_30 = ");" + NL + "\t\t\t\t\tput(\"password\", ";
  protected final String TEXT_31 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_32 = NL + "\t\t\t}};" + NL + "" + NL + "\t\t\tpublic String getEndpointUri() {" + NL + "\t\t\t\t// projectName + \"_\" + processName + \"_\" + componentName" + NL + "\t\t\t\treturn \"";
  protected final String TEXT_33 = "_";
  protected final String TEXT_34 = "_";
  protected final String TEXT_35 = "\";" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tpublic java.util.Map<String, Object> getEndpointProperties() {" + NL + "\t\t\t\treturn props;" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tpublic String getEndpointKey() {" + NL + "\t\t\t\treturn \"cxf\";" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "\t);" + NL + "} else {";
  protected final String TEXT_36 = NL + "// System.out.println(\"";
  protected final String TEXT_37 = "\");" + NL + "// System.out.println(\"";
  protected final String TEXT_38 = "\");" + NL + "// System.out.println(\"";
  protected final String TEXT_39 = "|";
  protected final String TEXT_40 = "\");" + NL + "" + NL + "class Util_";
  protected final String TEXT_41 = " {" + NL + "" + NL + "\tpublic final String LIST_SIZE_SYMBOL = \".size\";" + NL + "" + NL + "\t\tpublic final String LEFT_SQUARE_BRACKET = \"[\";" + NL + "" + NL + "\t\tpublic final String RIGHT_SQUARE_BRACKET = \"]\";" + NL + "" + NL + "\t\tpublic final String ALL_LIST_SYMBOL = \"[*]\";" + NL + "" + NL + "\t\tpublic Object getValue(java.util.Map<String, Object> map, String path) {" + NL + "\t\t\tif (path == null || \"\".equals(path)) {" + NL + "\t\t\t\treturn null;" + NL + "\t\t\t}" + NL + "\t\t\tif (map == null||map.isEmpty()) {" + NL + "\t\t\t\treturn null;" + NL + "\t\t\t}" + NL + "\t\t\tjava.util.List<String> paths = new java.util.ArrayList<String>();" + NL + "\t\t\tresolvePath(map, path, paths);" + NL + "\t\t\tif (paths.size() > 0) {" + NL + "\t\t\t\tif (path.indexOf(ALL_LIST_SYMBOL) == -1) {" + NL + "\t\t\t\t\treturn map.get(paths.get(0));" + NL + "\t\t\t\t} else {" + NL + "\t\t\t\t\tint size = paths.size();" + NL + "\t\t\t\t\tjava.util.List<Object> out = new java.util.ArrayList<Object>(size);" + NL + "\t\t\t\t\tfor (int i = 0; i < size; i++) {" + NL + "\t\t\t\t\t\tout.add(map.get(paths.get(i)));" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\treturn out;" + NL + "\t\t\t\t}" + NL + "\t\t\t} else {" + NL + "\t\t\t\treturn null;" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "" + NL + "\t\tpublic void resolveInputPath(java.util.Map<String, Object> inputMap) {" + NL + "\t\t\tjava.util.Map<String, Object> tempStoreMap = new java.util.HashMap<String, Object>();" + NL + "\t\t\tjava.util.List<String> tempRemovePath = new java.util.ArrayList<String>();" + NL + "\t\t\tfor (String key : inputMap.keySet()) {" + NL + "\t\t\t\tif (key.indexOf(ALL_LIST_SYMBOL) != -1) {" + NL + "\t\t\t\t\tString listHeadPath = key.substring(0, key.indexOf(ALL_LIST_SYMBOL));" + NL + "\t\t\t\t\tString listFootPath = key.substring(key.indexOf(ALL_LIST_SYMBOL) + ALL_LIST_SYMBOL.length());" + NL + "\t\t\t\t\tjava.util.List listElement = (java.util.List) inputMap.get(key);" + NL + "\t\t\t\t\tfor (int i = 0; i < listElement.size(); i++) {" + NL + "\t\t\t\t\t\ttempStoreMap.put(listHeadPath + LEFT_SQUARE_BRACKET + i + RIGHT_SQUARE_BRACKET + listFootPath, listElement.get(i));" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\ttempRemovePath.add(key);" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t\tinputMap.putAll(tempStoreMap);" + NL + "\t\t\tfor (String removePath : tempRemovePath) {" + NL + "\t\t\t\tinputMap.remove(removePath);" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "" + NL + "\t\tpublic void resolvePath(java.util.Map<String, Object> map, String path, java.util.List<String> paths) {" + NL + "\t\t\tString listHeadPath = \"\";" + NL + "\t\t\tString listFootPath = \"\";" + NL + "\t\t\tint size = 0;" + NL + "\t\t\tString tempPath = \"\";" + NL + "\t\t\tif (path.indexOf(ALL_LIST_SYMBOL) != -1) {" + NL + "\t\t\t\tlistHeadPath = path.substring(0, path.indexOf(ALL_LIST_SYMBOL));" + NL + "\t\t\t\tlistFootPath = path.substring(path.indexOf(ALL_LIST_SYMBOL) + ALL_LIST_SYMBOL.length());" + NL + "\t\t\t\tif (map.get(listHeadPath) == null && map.get(listHeadPath + LIST_SIZE_SYMBOL) != null) {" + NL + "\t\t\t\t\tsize = Integer.parseInt(map.get(listHeadPath + LIST_SIZE_SYMBOL).toString());" + NL + "\t\t\t\t\tfor (int i = 0; i < size; i++) {" + NL + "\t\t\t\t\t\ttempPath = listHeadPath + LEFT_SQUARE_BRACKET + i + RIGHT_SQUARE_BRACKET + listFootPath;" + NL + "\t\t\t\t\t\tif (tempPath.indexOf(ALL_LIST_SYMBOL) != -1) {" + NL + "\t\t\t\t\t\t\tresolvePath(map, tempPath, paths);" + NL + "\t\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\t\tpaths.add(tempPath);" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}" + NL + "\t\t\t} else {" + NL + "\t\t\t\t\tpaths.add(path);" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "" + NL + "\t\tpublic java.util.List<Object> normalize(String inputValue, String delimiter) {" + NL + "\t\t\tif (inputValue == null || \"\".equals(inputValue)) {" + NL + "\t\t\t\treturn null;" + NL + "\t\t\t}" + NL + "\t\t\tObject[] inputValues = inputValue.split(delimiter);" + NL + "\t\t\treturn java.util.Arrays.asList(inputValues);" + NL + "\t\t}" + NL + "" + NL + "\t\tpublic String denormalize(java.util.List inputValues, String delimiter) {" + NL + "\t\t\tif (inputValues == null||inputValues.isEmpty()) {" + NL + "\t\t\t\treturn null;" + NL + "\t\t\t}" + NL + "\t\t\tStringBuffer sb = new StringBuffer();" + NL + "\t\t\tfor (Object o : inputValues) {" + NL + "\t\t\t\tsb.append(String.valueOf(o));" + NL + "\t\t\t\tsb.append(delimiter);" + NL + "\t\t\t}" + NL + "\t\t\tif (sb.length() > 0) {" + NL + "\t\t\t\tsb.delete(sb.length() - delimiter.length(),sb.length());" + NL + "\t\t\t}" + NL + "\t\t\treturn sb.toString();" + NL + "\t\t}" + NL + "}" + NL + "\tSystem.setProperty(\"org.apache.commons.logging.Log\", \"org.apache.commons.logging.impl.NoOpLog\");" + NL + "\t//shade the log level for DynamicClientFactory.class" + NL + "\tjava.util.logging.Logger LOG_";
  protected final String TEXT_42 = " = org.apache.cxf.common.logging.LogUtils.getL7dLogger(org.apache.cxf.endpoint.dynamic.DynamicClientFactory.class);" + NL + "\tLOG_";
  protected final String TEXT_43 = ".setLevel(java.util.logging.Level.WARNING);" + NL + "" + NL + "\tUtil_";
  protected final String TEXT_44 = " util_";
  protected final String TEXT_45 = " = new Util_";
  protected final String TEXT_46 = "();" + NL + "" + NL + "\torg.talend.ws.helper.conf.ServiceHelperConfiguration config_";
  protected final String TEXT_47 = " = new org.talend.ws.helper.conf.ServiceHelperConfiguration();" + NL + "" + NL + "\tconfig_";
  protected final String TEXT_48 = ".setConnectionTimeout(Long.valueOf(";
  protected final String TEXT_49 = "));" + NL + "\tconfig_";
  protected final String TEXT_50 = ".setReceiveTimeout(Long.valueOf(";
  protected final String TEXT_51 = "));" + NL + "" + NL + "\tconfig_";
  protected final String TEXT_52 = ".setKeyStoreFile(System.getProperty(\"javax.net.ssl.keyStore\"));" + NL + "\tconfig_";
  protected final String TEXT_53 = ".setKeyStoreType(System.getProperty(\"javax.net.ssl.keyStoreType\"));" + NL + "\tconfig_";
  protected final String TEXT_54 = ".setKeyStorePwd(System.getProperty(\"javax.net.ssl.keyStorePassword\"));" + NL;
  protected final String TEXT_55 = NL + "\tSystem.setProperty(\"java.protocol.handler.pkgs\", \"com.sun.net.ssl.internal.www.protocol\");" + NL + "\tSystem.setProperty(\"javax.net.ssl.trustStore\", ";
  protected final String TEXT_56 = ");" + NL + "\tSystem.setProperty(\"javax.net.ssl.trustStorePassword\", ";
  protected final String TEXT_57 = ");";
  protected final String TEXT_58 = NL + "\tconfig_";
  protected final String TEXT_59 = ".setUsername(";
  protected final String TEXT_60 = ");" + NL + "\tconfig_";
  protected final String TEXT_61 = ".setPassword(";
  protected final String TEXT_62 = ");" + NL + "\t";
  protected final String TEXT_63 = NL + "\t\tconfig_";
  protected final String TEXT_64 = ".setBasicAuth(true);" + NL + "\t";
  protected final String TEXT_65 = NL + "\t";
  protected final String TEXT_66 = NL + "\t\tconfig_";
  protected final String TEXT_67 = ".setTokenAuth(true);" + NL + "\t";
  protected final String TEXT_68 = NL + "\tconfig_";
  protected final String TEXT_69 = ".setProxyServer(";
  protected final String TEXT_70 = ");" + NL + "\tconfig_";
  protected final String TEXT_71 = ".setProxyPort(";
  protected final String TEXT_72 = ");" + NL + "\tconfig_";
  protected final String TEXT_73 = ".setProxyUsername(";
  protected final String TEXT_74 = ");" + NL + "\tconfig_";
  protected final String TEXT_75 = ".setProxyPassword( ";
  protected final String TEXT_76 = ");";
  protected final String TEXT_77 = NL + NL + "\torg.talend.ws.helper.ServiceDiscoveryHelper serviceDiscoveryHelper_";
  protected final String TEXT_78 = " = null ;" + NL + "" + NL + "\tjava.net.URI uri_";
  protected final String TEXT_79 = " = new java.net.URI(";
  protected final String TEXT_80 = ");" + NL + "\tif (\"http\".equals(uri_";
  protected final String TEXT_81 = ".getScheme()) || \"https\".equals(uri_";
  protected final String TEXT_82 = ".getScheme())) {" + NL + "\t\tserviceInvokerHelper_";
  protected final String TEXT_83 = " = new org.talend.ws.helper.ServiceInvokerHelper(";
  protected final String TEXT_84 = ",config_";
  protected final String TEXT_85 = ",";
  protected final String TEXT_86 = ");" + NL + "\t} else {" + NL + "\t\tserviceDiscoveryHelper_";
  protected final String TEXT_87 = " = new org.talend.ws.helper.ServiceDiscoveryHelper(";
  protected final String TEXT_88 = ",";
  protected final String TEXT_89 = ");" + NL + "\t\tserviceInvokerHelper_";
  protected final String TEXT_90 = " = new org.talend.ws.helper.ServiceInvokerHelper(serviceDiscoveryHelper_";
  protected final String TEXT_91 = ",config_";
  protected final String TEXT_92 = ");" + NL + "\t}" + NL + "" + NL + "\tserviceName_";
  protected final String TEXT_93 = " = new javax.xml.namespace.QName(\"";
  protected final String TEXT_94 = "\", \"";
  protected final String TEXT_95 = "\");" + NL + "\tportName_";
  protected final String TEXT_96 = " = new javax.xml.namespace.QName(\"";
  protected final String TEXT_97 = "\", \"";
  protected final String TEXT_98 = "\");" + NL + "" + NL + "\tjava.util.Map<String,Object> inMap_";
  protected final String TEXT_99 = " = null;" + NL + "}";
  protected final String TEXT_100 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode) codeGenArgument.getArgument();
String cid = node.getUniqueName();

String projectName = codeGenArgument.getCurrentProjectName();
String processName = node.getProcess().getName();

String endpoint = ElementParameterParser.getValue(node,"__ENDPOINT__");

String serviceNS = ElementParameterParser.getValue(node,"__SERVICE_NS__");
String serviceName = ElementParameterParser.getValue(node,"__SERVICE_NAME__");
String portNS = ElementParameterParser.getValue(node,"__PORT_NS__");
String portName = ElementParameterParser.getValue(node,"__PORT_NAME__");

String methodTemp = ElementParameterParser.getValue(node,"__METHOD__");
String method = methodTemp.indexOf("(") != -1 ? methodTemp.substring(0, methodTemp.indexOf("(")) : methodTemp;

String soapAction = ElementParameterParser.getValue(node,"__SOAP_ACTION__");
String methodNS = ElementParameterParser.getValue(node,"__METHOD_NS__");

Boolean useSl = Boolean.valueOf(ElementParameterParser.getValue(node, "__SERVICE_LOCATOR__"));
String slStrategy = ElementParameterParser.getValue(node, "__SERVICE_LOCATOR_STRATEGY__");

Boolean useSam = Boolean.valueOf(ElementParameterParser.getValue(node, "__SERVICE_ACTIVITY_MONITOR__"));

Boolean useAuth = Boolean.valueOf(ElementParameterParser.getValue(node, "__NEED_AUTH__"));
String authType = ElementParameterParser.getValue(node,"__AUTH_TYPE__");
String username = ElementParameterParser.getValue(node,"__AUTH_USERNAME__");
String password = ElementParameterParser.getValue(node,"__AUTH_PASSWORD__");

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    	if (useSl) {
	List<Map<String, String>> customProperties = (List<Map<String,String>>)
			ElementParameterParser.getObjectValue(node, "__SERVICE_LOCATOR_CUSTOM_PROPERTIES__");
	for (int k = 0; k < customProperties.size(); k++) { 
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(customProperties.get(k).get("PROP_NAME"));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(customProperties.get(k).get("PROP_VALUE"));
    stringBuffer.append(TEXT_10);
     }
} 
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(portNS);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(portName);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(serviceNS);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(serviceName);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(method);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(soapAction);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(ElementParameterParser.getValue(node, "__ESB_ENDPOINT__"));
    stringBuffer.append(TEXT_20);
    stringBuffer.append(ElementParameterParser.getValue(node, "__COMMUNICATION_STYLE__"));
    stringBuffer.append(TEXT_21);
    stringBuffer.append(useSl);
    stringBuffer.append(TEXT_22);
     if (useSl) { 
    stringBuffer.append(TEXT_23);
    stringBuffer.append(slStrategy);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
     } 
    stringBuffer.append(TEXT_26);
    stringBuffer.append(useSam);
    stringBuffer.append(TEXT_27);
     if (useAuth) { 
    stringBuffer.append(TEXT_28);
    stringBuffer.append(authType);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(username);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(password);
    stringBuffer.append(TEXT_31);
     } 
    stringBuffer.append(TEXT_32);
    stringBuffer.append(projectName);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(processName);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    
boolean useProxy = ("true").equals(ElementParameterParser.getValue(node,"__USE_PROXY__"));
String proxyHost = ElementParameterParser.getValue(node,"__PROXY_HOST__");
String proxyPort = ElementParameterParser.getValue(node,"__PROXY_PORT__");
String proxyUser = ElementParameterParser.getValue(node,"__PROXY_USERNAME__");
String proxyPassword = ElementParameterParser.getValue(node,"__PROXY_PASSWORD__");

boolean needSSLtoTrustServer = ("true").equals(ElementParameterParser.getValue(node,"__NEED_SSL_TO_TRUSTSERVER__"));
String trustStoreFile = ElementParameterParser.getValue(node,"__SSL_TRUSTSERVER_TRUSTSTORE__");
String trustStorePassword = ElementParameterParser.getValue(node,"__SSL_TRUSTSERVER_PASSWORD__");

String connTimeoutStr = ElementParameterParser.getValue(node,"__CONNECTION_TIMEOUT__");
String connTimeoutSec = (connTimeoutStr != null && !("").equals(connTimeoutStr)) ? connTimeoutStr : "20";
long connTimeout = (long) (Double.valueOf(connTimeoutSec) * 1000);

String receiveTimeoutStr = ElementParameterParser.getValue(node,"__RECEIVE_TIMEOUT__");
String receiveTimeoutSec = (receiveTimeoutStr != null && !("").equals(receiveTimeoutStr)) ? receiveTimeoutStr : "20";
long receiveTimeout = (long) (Double.valueOf(receiveTimeoutSec) * 1000);

String tempPath = ElementParameterParser.getValue(node,"__TMPPATH__");
if ("".equals(tempPath)) { tempPath="\"\""; }

    stringBuffer.append(TEXT_36);
    stringBuffer.append(serviceName);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(portName);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(methodNS);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(soapAction);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(connTimeout);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(receiveTimeout);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
     if (needSSLtoTrustServer) { 
    stringBuffer.append(TEXT_55);
    stringBuffer.append(trustStoreFile);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(trustStorePassword);
    stringBuffer.append(TEXT_57);
     }

if (useAuth && ("BASIC".equals(authType) || "TOKEN".equals(authType))) { 
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(username);
    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_61);
    stringBuffer.append(password);
    stringBuffer.append(TEXT_62);
     if ("BASIC".equals(authType)) { 
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
     } 
    stringBuffer.append(TEXT_65);
     if ("TOKEN".equals(authType)) { 
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
     } 
     }

if (useProxy) { 
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_69);
    stringBuffer.append(proxyHost);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_71);
    stringBuffer.append(proxyPort);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(proxyUser);
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_75);
    stringBuffer.append(proxyPassword);
    stringBuffer.append(TEXT_76);
     } 
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    stringBuffer.append(tempPath);
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_88);
    stringBuffer.append(tempPath);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_93);
    stringBuffer.append(serviceNS);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(serviceName);
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_96);
    stringBuffer.append(portNS);
    stringBuffer.append(TEXT_97);
    stringBuffer.append(portName);
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_99);
    stringBuffer.append(TEXT_100);
    return stringBuffer.toString();
  }
}
