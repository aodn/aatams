package org.talend.designer.codegen.translators.technical;

import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.ElementParameterParser;

public class TMomInputLoopEndJava
{
  protected static String nl;
  public static synchronized TMomInputLoopEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TMomInputLoopEndJava result = new TMomInputLoopEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "\t";
  protected final String TEXT_2 = NL + "\t\t\tmessage_";
  protected final String TEXT_3 = ".acknowledge();" + NL + "\t\t" + NL + "\t\t";
  protected final String TEXT_4 = NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t";
  protected final String TEXT_5 = NL + "\t\t\t\tThread.sleep(";
  protected final String TEXT_6 = "*1000);" + NL + "\t\t\t";
  protected final String TEXT_7 = NL + "\t\t\t\tbreak;" + NL + "\t\t\t";
  protected final String TEXT_8 = NL + "\t\t\t\t}" + NL + "\t\t\t";
  protected final String TEXT_9 = NL + "\t\t" + NL + "\t\t}" + NL + " \t\tSystem.out.println(\"Closing connection\");" + NL + "        consumer_";
  protected final String TEXT_10 = ".close();" + NL + "        session_";
  protected final String TEXT_11 = ".close();" + NL + "        connection_";
  protected final String TEXT_12 = ".close();" + NL + "\t";
  protected final String TEXT_13 = NL + "\t\t" + NL + "\t\t\t}\t" + NL + "\t\t\tSystem.out.println(\"Closing connection\");" + NL + "\t\t\tremoteQ";
  protected final String TEXT_14 = ".close();" + NL + "\t\t\t";
  protected final String TEXT_15 = NL + "\t\t\t\tqMgr";
  protected final String TEXT_16 = ".backout();" + NL + "\t\t\t";
  protected final String TEXT_17 = NL + "\t\t\tqMgr";
  protected final String TEXT_18 = ".disconnect();" + NL + "\t\t" + NL + "\t\t}catch(com.ibm.mq.MQException ex){" + NL + "\t\t\tSystem.out.println(\"A WebSphere MQ error occurred : Completion code \" + ex.completionCode + \" Reason code \" + ex.reasonCode);" + NL + "\t\t\tremoteQ";
  protected final String TEXT_19 = ".close();" + NL + "\t\t}" + NL + "\t";
  protected final String TEXT_20 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	cid = cid.replaceAll("_Loop", "");
	String serverType=ElementParameterParser.getValue(node, "__SERVER__");
	String useMsgId=ElementParameterParser.getValue(node, "__IS_USE_MESSAGE_ID__");
	String kListen=ElementParameterParser.getValue(node, "__KEEPLISTENING__");
	String timeOut=ElementParameterParser.getValue(node, "__TIMEOUT__");
	String acknowledgmentMode = ElementParameterParser.getValue(node, "__ACKNOWLEDGMENT_MODE__");
	String msgBobyType =  ElementParameterParser.getValue(node, "__MESSAGE_BODY_TYPE__");
	
	if (("JBoss").equals(serverType) || ("ActiveMQ").equals(serverType)) {
		if ("CLIENT_ACKNOWLEDGE".equals(acknowledgmentMode)) {
		
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    	
		}
		if(("true").equals(useMsgId) && !"Map".equals(msgBobyType)){
		
    stringBuffer.append(TEXT_4);
    
		}
		if(("JBoss").equals(serverType)){
			if("true".equals(kListen)){
			
    stringBuffer.append(TEXT_5);
    stringBuffer.append(timeOut);
    stringBuffer.append(TEXT_6);
    		
			}else{
			
    stringBuffer.append(TEXT_7);
    			
			}
		}else if(("ActiveMQ").equals(serverType)){
			if (("true").equals(ElementParameterParser.getValue(node,"__USEMAX__"))) {
			
    stringBuffer.append(TEXT_8);
    
			}
		}
		
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    		
	}else{
		boolean isCommit = ("true").equals(ElementParameterParser.getValue(node, "__COMMIT__"));
		boolean isBrowse = ("true").equals(ElementParameterParser.getValue(node,"__BROWSE__"));
		boolean isRollback = ("true").equals(ElementParameterParser.getValue(node, "__ROLLBACK__"));
		isRollback = isRollback && ("false".equals(kListen)) && !isCommit && !isBrowse;
		
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_14);
    
			if(isRollback){
			
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_16);
    
			}
			
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    
	}
	
    stringBuffer.append(TEXT_20);
    return stringBuffer.toString();
  }
}
