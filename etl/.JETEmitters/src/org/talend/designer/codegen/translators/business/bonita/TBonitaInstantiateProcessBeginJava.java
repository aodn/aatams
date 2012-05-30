package org.talend.designer.codegen.translators.business.bonita;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.List;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;

public class TBonitaInstantiateProcessBeginJava
{
  protected static String nl;
  public static synchronized TBonitaInstantiateProcessBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TBonitaInstantiateProcessBeginJava result = new TBonitaInstantiateProcessBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\tSystem.setProperty(\"BONITA_HOME\", ";
  protected final String TEXT_3 = ");";
  protected final String TEXT_4 = NL + "\tSystem.setProperty(\"org.ow2.bonita.environment\", ";
  protected final String TEXT_5 = " );";
  protected final String TEXT_6 = NL + "\tSystem.setProperty(\"java.security.auth.login.config\", ";
  protected final String TEXT_7 = " );" + NL + "\tSystem.setProperty(\"java.util.logging.config.file\", new java.io.File(";
  protected final String TEXT_8 = ").toURI().toURL().toString());" + NL + "\t" + NL + "\t" + NL + "\torg.ow2.bonita.facade.RuntimeAPI runtimeAPI_";
  protected final String TEXT_9 = " = org.ow2.bonita.util.AccessorUtil.getAPIAccessor().getRuntimeAPI();" + NL + "\tjavax.security.auth.login.LoginContext loginContext_";
  protected final String TEXT_10 = " = null;" + NL + "\torg.ow2.bonita.facade.uuid.ProcessDefinitionUUID processID_";
  protected final String TEXT_11 = " =null;" + NL + "\t" + NL + "\tString processInstanceUUID_";
  protected final String TEXT_12 = " = null;" + NL + "\tjava.util.Map<String, Object> parameters_";
  protected final String TEXT_13 = "=new java.util.HashMap<String, Object>();" + NL + "\t" + NL + "\ttry {" + NL + "\t\t\t" + NL + "\t\tloginContext_";
  protected final String TEXT_14 = " = new javax.security.auth.login.LoginContext(";
  protected final String TEXT_15 = ", new org.ow2.bonita.util.SimpleCallbackHandler(";
  protected final String TEXT_16 = ", ";
  protected final String TEXT_17 = "));" + NL + "\t\tloginContext_";
  protected final String TEXT_18 = ".login();";
  protected final String TEXT_19 = NL + "\t\t\tprocessID_";
  protected final String TEXT_20 = " = new org.ow2.bonita.facade.uuid.ProcessDefinitionUUID(";
  protected final String TEXT_21 = ");";
  protected final String TEXT_22 = NL + "\t\t\tprocessID_";
  protected final String TEXT_23 = " = new org.ow2.bonita.facade.uuid.ProcessDefinitionUUID(";
  protected final String TEXT_24 = ", ";
  protected final String TEXT_25 = ");";
  protected final String TEXT_26 = NL + "\t} catch (javax.security.auth.login.LoginException le_";
  protected final String TEXT_27 = ") {//just login exception";
  protected final String TEXT_28 = NL + "\t\tthrow le_";
  protected final String TEXT_29 = ";\t\t";
  protected final String TEXT_30 = NL + "\t\tSystem.err.println(le_";
  protected final String TEXT_31 = ".getCause().getMessage());";
  protected final String TEXT_32 = NL + "}";
  protected final String TEXT_33 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	
	String processID = ElementParameterParser.getValue(node, "__PROCESS_ID__");
	String userName = ElementParameterParser.getValue(node, "__USERNAME__");
	String password = ElementParameterParser.getValue(node, "__PASSWORD__");

	String use_process_id = ElementParameterParser.getValue(node, "__USE_PROCESS_ID__");

	String process_name = ElementParameterParser.getValue(node, "__PROCESS_NAME__");
	String process_version = ElementParameterParser.getValue(node, "__PROCESS_VERSION__");
	
	boolean dieOnError = ("true").equals(ElementParameterParser.getValue(node, "__DIE_ON_ERROR__"));
	
	String bonitaEnvironmentFile = ElementParameterParser.getValue(node, "__BONITA_ENVIRONMENT_FILE__");
	String jassFile = ElementParameterParser.getValue(node, "__JASS_STANDARD_FILE__");
	String loggingFile = ElementParameterParser.getValue(node, "__LOGGING_FILE__");
	String loginModule = ElementParameterParser.getValue(node, "__LOGIN_MODULE__");
	String dbVersion = ElementParameterParser.getValue(node, "__DB_VERSION__");
	String bonitaHome = ElementParameterParser.getValue(node, "__BONITA_HOME__");
	
	if("BONITA_561".equals(dbVersion)){

    stringBuffer.append(TEXT_2);
    stringBuffer.append(bonitaHome);
    stringBuffer.append(TEXT_3);
    
	}else if("BONITA_531".equals(dbVersion)||"BONITA_523".equals(dbVersion)){

    stringBuffer.append(TEXT_4);
    stringBuffer.append(bonitaEnvironmentFile);
    stringBuffer.append(TEXT_5);
    
	}

    stringBuffer.append(TEXT_6);
    stringBuffer.append(jassFile);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(loggingFile);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(loginModule);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(userName);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(password);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_18);
    
		if("true".equals(use_process_id)) {

    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(processID);
    stringBuffer.append(TEXT_21);
    
		} else {

    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(process_name);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(process_version);
    stringBuffer.append(TEXT_25);
    
		}

    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    
	if (dieOnError) {

    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_29);
    
	} else {

    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_31);
    	
	}

    stringBuffer.append(TEXT_32);
    stringBuffer.append(TEXT_33);
    return stringBuffer.toString();
  }
}
