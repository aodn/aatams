package org.talend.designer.codegen.translators.internet;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import java.util.Map;
import java.util.List;

public class TSOAPMainJava
{
  protected static String nl;
  public static synchronized TSOAPMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TSOAPMainJava result = new TSOAPMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\tif(true)" + NL + "\t\tthrow new Exception(\"There is no incoming connection or the incoming schema is empty\");";
  protected final String TEXT_3 = NL + "\tclass TalendPrivilegedAction implements java.security.PrivilegedAction {" + NL + "\t\tString soapVersion;";
  protected final String TEXT_4 = NL + "\t\t\t";
  protected final String TEXT_5 = "Struct ";
  protected final String TEXT_6 = ";" + NL + "" + NL + "\t\t\tpublic TalendPrivilegedAction(";
  protected final String TEXT_7 = "Struct rowArg, String soapVersionArg) {" + NL + "\t\t\t\tthis.";
  protected final String TEXT_8 = " = rowArg;" + NL + "\t\t\t\tthis.soapVersion = soapVersionArg;" + NL + "\t\t\t}";
  protected final String TEXT_9 = NL + "\t\t\tpublic TalendPrivilegedAction(String soapVersionArg) {" + NL + "\t\t\t\tthis.soapVersion = soapVersionArg;" + NL + "\t\t\t}";
  protected final String TEXT_10 = NL + "\t\t\tString document;" + NL + "\t\t\tpublic Object run(){" + NL + "\t\t\t\ttry{" + NL + "\t\t\t\t\tthis.document = soapUtil_";
  protected final String TEXT_11 = ".extractContentAsDocument(org.talend.soap.SOAPUtil.";
  protected final String TEXT_12 = ",";
  protected final String TEXT_13 = ",";
  protected final String TEXT_14 = ",";
  protected final String TEXT_15 = ");" + NL + "\t\t\t\t}catch(Exception ex){" + NL + "\t\t\t\t\tex.printStackTrace();" + NL + "\t\t\t\t}" + NL + "\t\t\t\treturn null;" + NL + "\t\t\t}";
  protected final String TEXT_16 = NL + "\t\t\tpublic Object run(){" + NL + "\t\t\t\ttry{" + NL + "\t\t\t\t\tsoapUtil_";
  protected final String TEXT_17 = ".invokeSOAP(org.talend.soap.SOAPUtil.";
  protected final String TEXT_18 = ",";
  protected final String TEXT_19 = ",";
  protected final String TEXT_20 = ",";
  protected final String TEXT_21 = ");" + NL + "\t\t\t\t}catch(Exception ex){" + NL + "\t\t\t\t\tex.printStackTrace();" + NL + "\t\t\t\t}" + NL + "\t\t\t\treturn null;" + NL + "\t\t\t}";
  protected final String TEXT_22 = NL + "\t}";
  protected final String TEXT_23 = NL + "final String soapVersion_";
  protected final String TEXT_24 = " = org.talend.soap.SOAPUtil.SOAP12;";
  protected final String TEXT_25 = NL + "final String soapVersion_";
  protected final String TEXT_26 = " = org.talend.soap.SOAPUtil.SOAP11;";
  protected final String TEXT_27 = NL + "\tjavax.security.auth.Subject subject_";
  protected final String TEXT_28 = " = (javax.security.auth.Subject)globalMap.get(\"kerberos_subject_";
  protected final String TEXT_29 = "\");" + NL + "\tif(subject_";
  protected final String TEXT_30 = "==null){" + NL + "\t\tSystem.err.println(\"Subject for Kerberos is null!\");" + NL + "\t}" + NL + "\tTalendPrivilegedAction talendPrivilegedAction_";
  protected final String TEXT_31 = " = new TalendPrivilegedAction(";
  protected final String TEXT_32 = "soapVersion_";
  protected final String TEXT_33 = ");" + NL + "\tjavax.security.auth.Subject.doAs(subject_";
  protected final String TEXT_34 = ",talendPrivilegedAction_";
  protected final String TEXT_35 = ");";
  protected final String TEXT_36 = NL + "\tString document_";
  protected final String TEXT_37 = " = talendPrivilegedAction_";
  protected final String TEXT_38 = ".document;";
  protected final String TEXT_39 = NL;
  protected final String TEXT_40 = NL + "\t\tString document_";
  protected final String TEXT_41 = " = soapUtil_";
  protected final String TEXT_42 = ".extractContentAsDocument(org.talend.soap.SOAPUtil.";
  protected final String TEXT_43 = ",";
  protected final String TEXT_44 = ",";
  protected final String TEXT_45 = ",";
  protected final String TEXT_46 = ");";
  protected final String TEXT_47 = NL + "\t\tsoapUtil_";
  protected final String TEXT_48 = ".invokeSOAP(org.talend.soap.SOAPUtil.";
  protected final String TEXT_49 = ",";
  protected final String TEXT_50 = ",";
  protected final String TEXT_51 = ",";
  protected final String TEXT_52 = ");";
  protected final String TEXT_53 = NL + NL + "// for output";
  protected final String TEXT_54 = "\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_55 = " = new ";
  protected final String TEXT_56 = "Struct();" + NL + "\t\t\t\t";
  protected final String TEXT_57 = NL + "\t\t\t\t\t";
  protected final String TEXT_58 = ".Soap = ParserUtils.parseTo_Document(document_";
  protected final String TEXT_59 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_60 = NL + "\t\t\t\t\t";
  protected final String TEXT_61 = ".Header = soapUtil_";
  protected final String TEXT_62 = ".getReHeaderMessage();" + NL + "\t\t\t\t\tif(soapUtil_";
  protected final String TEXT_63 = ".hasFault()){" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_64 = ".Fault = soapUtil_";
  protected final String TEXT_65 = ".getReFaultMessage();" + NL + "\t\t\t\t\t}else{" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_66 = ".Body = soapUtil_";
  protected final String TEXT_67 = ".getReBodyMessage();" + NL + "\t\t\t\t\t}";
  protected final String TEXT_68 = NL + "        " + NL;
  protected final String TEXT_69 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();

String endpoint = ElementParameterParser.getValue(node,"__ENDPOINT__");
String action = ElementParameterParser.getValue(node,"__ACTION__");
String soapMessageStr = ElementParameterParser.getValue(node,"__SOAPMESSAGE__");
soapMessageStr = soapMessageStr.replaceAll("[\r\n]", " ");
        
String soapVersion = ElementParameterParser.getValue(node,"__SOAP_VERSION__");

boolean useKerberos = ("true").equals(ElementParameterParser.getValue(node,"__USE_KERBEROS__"));
String kerberosConfiguration = ElementParameterParser.getValue(node,"__KERBEROS_CONFIGURATION__");
boolean useMessageFromSchema = "true".equals(ElementParameterParser.getValue(node, "__USE_MESSAGE_FROM_SCHEMA__"));
boolean outputDocument = "true".equals(ElementParameterParser.getValue(node, "__OUTPUT_DOCUMENT__"));
String soapMessageColumn = ElementParameterParser.getValue(node,"__SOAPMESSAGE_FROM_SCHEMA__");
String connName = null;
boolean schemaEmpty = false;
if (node.getIncomingConnections().size() > 0) {
	IConnection conn = node.getIncomingConnections().get(0);
	connName = conn.getName();
	schemaEmpty = conn.getMetadataTable().getListColumns().size()==0;
}
if (useMessageFromSchema && (connName==null || schemaEmpty)) {

    stringBuffer.append(TEXT_2);
    
} else {
	if(useKerberos){

    stringBuffer.append(TEXT_3);
    
		if(useMessageFromSchema) {

    stringBuffer.append(TEXT_4);
    stringBuffer.append(connName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(connName);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(connName);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(connName);
    stringBuffer.append(TEXT_8);
    
		} else {

    stringBuffer.append(TEXT_9);
    
		}
		if(outputDocument){

    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(soapVersion.toUpperCase());
    stringBuffer.append(TEXT_12);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(action);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(useMessageFromSchema?connName+"."+soapMessageColumn+".toString()":soapMessageStr);
    stringBuffer.append(TEXT_15);
    
		}else{

    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(soapVersion.toUpperCase());
    stringBuffer.append(TEXT_18);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(action);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(useMessageFromSchema?connName+"."+soapMessageColumn+".toString()":soapMessageStr);
    stringBuffer.append(TEXT_21);
    
		}

    stringBuffer.append(TEXT_22);
    if("Soap12".equals(soapVersion)){
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    }else{
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    }
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(kerberosConfiguration);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(useMessageFromSchema?connName+", ":"");
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    
	if(outputDocument){

    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    
	}

    stringBuffer.append(TEXT_39);
    
}else{
	if(outputDocument){

    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(soapVersion.toUpperCase());
    stringBuffer.append(TEXT_43);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(action);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(useMessageFromSchema?connName+"."+soapMessageColumn+".toString()":soapMessageStr);
    stringBuffer.append(TEXT_46);
    
	}else{

    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(soapVersion.toUpperCase());
    stringBuffer.append(TEXT_49);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(action);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(useMessageFromSchema?connName+"."+soapMessageColumn+".toString()":soapMessageStr);
    stringBuffer.append(TEXT_52);
    
	}
}

    stringBuffer.append(TEXT_53);
    
List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
if (conns!=null) {//1
	if (conns.size()>0) {//2
		IConnection conn = conns.get(0); //the first connection
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {//3

    stringBuffer.append(TEXT_54);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_56);
    if(outputDocument){
    stringBuffer.append(TEXT_57);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    }else{
    stringBuffer.append(TEXT_60);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
    				}
			}//3
		}//2
	}//1
} // if (!useMessageFromSchema || connName!=null) {

    stringBuffer.append(TEXT_68);
    stringBuffer.append(TEXT_69);
    return stringBuffer.toString();
  }
}
