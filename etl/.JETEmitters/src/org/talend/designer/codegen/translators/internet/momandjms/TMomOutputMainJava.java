package org.talend.designer.codegen.translators.internet.momandjms;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import java.util.Map;

public class TMomOutputMainJava
{
  protected static String nl;
  public static synchronized TMomOutputMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TMomOutputMainJava result = new TMomOutputMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\t\t\t\t\tString msgID_";
  protected final String TEXT_3 = " = ";
  protected final String TEXT_4 = ".";
  protected final String TEXT_5 = ";" + NL + "\t\t\t\t\t\tjavax.jms.MapMessage message_";
  protected final String TEXT_6 = " = session_";
  protected final String TEXT_7 = ".createMapMessage();";
  protected final String TEXT_8 = NL + "\t\t\t\t\t\tString msgBody_";
  protected final String TEXT_9 = " = String.valueOf(";
  protected final String TEXT_10 = ".";
  protected final String TEXT_11 = ");";
  protected final String TEXT_12 = NL + "\t\t\t\t\t\t\tjavax.jms.TextMessage message_";
  protected final String TEXT_13 = " = session_";
  protected final String TEXT_14 = ".createTextMessage( msgBody_";
  protected final String TEXT_15 = ");";
  protected final String TEXT_16 = NL + "\t\t\t\t\t\t\t message_";
  protected final String TEXT_17 = ".setString(msgID_";
  protected final String TEXT_18 = ",msgBody_";
  protected final String TEXT_19 = ");";
  protected final String TEXT_20 = NL + "\t\t\t\t\t\tString msgBody_";
  protected final String TEXT_21 = " = String.valueOf(";
  protected final String TEXT_22 = ".";
  protected final String TEXT_23 = ");";
  protected final String TEXT_24 = NL + "\t\t\t\t\t\t\tjavax.jms.BytesMessage message_";
  protected final String TEXT_25 = " = session_";
  protected final String TEXT_26 = ".createBytesMessage();" + NL + "\t\t\t\t\t\t\tmessage_";
  protected final String TEXT_27 = ".writeBytes(msgBody_";
  protected final String TEXT_28 = ".getBytes());";
  protected final String TEXT_29 = NL + "\t\t\t\t\t\t\tmessage_";
  protected final String TEXT_30 = ".setBytes(msgID_";
  protected final String TEXT_31 = ",msgBody_";
  protected final String TEXT_32 = ".getBytes());";
  protected final String TEXT_33 = NL + "\t\t\t\t\t\tjavax.jms.MapMessage message_";
  protected final String TEXT_34 = " = session_";
  protected final String TEXT_35 = ".createMapMessage();";
  protected final String TEXT_36 = NL + "\t\t\t\t\t\t\tmessage_";
  protected final String TEXT_37 = ".set";
  protected final String TEXT_38 = "(\"";
  protected final String TEXT_39 = "\",";
  protected final String TEXT_40 = ".";
  protected final String TEXT_41 = ");\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_42 = NL + "\t\t\t\t\t\t\tmessage_";
  protected final String TEXT_43 = ".setString(\"";
  protected final String TEXT_44 = "\",String.valueOf(";
  protected final String TEXT_45 = ".";
  protected final String TEXT_46 = "));\t" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_47 = NL + "\t\t\t\t\t\t\tmessage_";
  protected final String TEXT_48 = ".set";
  protected final String TEXT_49 = "(";
  protected final String TEXT_50 = ");";
  protected final String TEXT_51 = NL + "\t\t\t\t\t\t\tmessage_";
  protected final String TEXT_52 = ".set";
  protected final String TEXT_53 = "Property(";
  protected final String TEXT_54 = ", ";
  protected final String TEXT_55 = ");";
  protected final String TEXT_56 = NL + NL + "\t\t\t\t\t\tproducer_";
  protected final String TEXT_57 = ".send(message_";
  protected final String TEXT_58 = ");";
  protected final String TEXT_59 = NL + "\t\t\t\t\tcom.ibm.mq.MQMessage message_";
  protected final String TEXT_60 = " = new com.ibm.mq.MQMessage();";
  protected final String TEXT_61 = NL + "\t\t\t\t\t\tmessage_";
  protected final String TEXT_62 = ".format = ";
  protected final String TEXT_63 = ";";
  protected final String TEXT_64 = "\t";
  protected final String TEXT_65 = NL + "\t\t\t\t\t\tString msgID_";
  protected final String TEXT_66 = " = ";
  protected final String TEXT_67 = ".";
  protected final String TEXT_68 = ";" + NL + "\t\t\t\t\t\tif (msgID_";
  protected final String TEXT_69 = " != null & !(\"\").equals(msgID_";
  protected final String TEXT_70 = ")) {" + NL + "\t\t\t\t\t\t\tString padding = new String();" + NL + "\t\t\t\t\t       \tint padlen = 24;" + NL + "\t\t\t\t\t " + NL + "\t\t\t\t\t       \tint len = Math.abs(padlen) - msgID_";
  protected final String TEXT_71 = ".toString().length();" + NL + "\t\t\t\t\t       \tif (len > 0) {" + NL + "\t\t\t\t\t        \tfor (int i = 0 ; i < len ; i++) {" + NL + "\t\t\t\t\t           \t\tpadding = padding + \" \";" + NL + "\t\t\t\t\t         \t}" + NL + "\t\t\t\t\t        \tmsgID_";
  protected final String TEXT_72 = " = msgID_";
  protected final String TEXT_73 = " + padding;" + NL + "\t\t\t\t\t        }" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\tmessage_";
  protected final String TEXT_74 = ".messageId = msgID_";
  protected final String TEXT_75 = ".getBytes(\"ISO-8859-15\");";
  protected final String TEXT_76 = NL + "\t\t\t\t\t\t\tmessage_";
  protected final String TEXT_77 = ".";
  protected final String TEXT_78 = " = ";
  protected final String TEXT_79 = ";";
  protected final String TEXT_80 = NL + "\t\t\t\t\t\t\torg.talend.mq.headers.rfh2.MQRFH2 mqrfh2_";
  protected final String TEXT_81 = " = new org.talend.mq.headers.rfh2.MQRFH2();" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_82 = NL + "\t\t\t\t\t\t\tmqrfh2_";
  protected final String TEXT_83 = ".set";
  protected final String TEXT_84 = "(";
  protected final String TEXT_85 = ");";
  protected final String TEXT_86 = NL + "\t\t\t\t\t\t\torg.talend.mq.headers.rfh2.McdArea mcd_";
  protected final String TEXT_87 = " = new org.talend.mq.headers.rfh2.McdArea();";
  protected final String TEXT_88 = " " + NL + "\t\t\t\t\t\t\tmcd_";
  protected final String TEXT_89 = ".set";
  protected final String TEXT_90 = "(";
  protected final String TEXT_91 = ");";
  protected final String TEXT_92 = NL + "\t\t\t\t\t\t\tmqrfh2_";
  protected final String TEXT_93 = ".addArea(mcd_";
  protected final String TEXT_94 = ");";
  protected final String TEXT_95 = NL + "\t\t\t\t\t\t\torg.talend.mq.headers.rfh2.JmsArea jms_";
  protected final String TEXT_96 = " = new org.talend.mq.headers.rfh2.JmsArea();";
  protected final String TEXT_97 = " " + NL + "\t\t\t\t\t\t\tjms_";
  protected final String TEXT_98 = ".set";
  protected final String TEXT_99 = "(";
  protected final String TEXT_100 = ");";
  protected final String TEXT_101 = NL + "\t\t\t\t\t\t\tmqrfh2_";
  protected final String TEXT_102 = ".addArea(jms_";
  protected final String TEXT_103 = ");";
  protected final String TEXT_104 = NL + "\t\t\t\t\t\t\torg.talend.mq.headers.rfh2.UsrArea usr_";
  protected final String TEXT_105 = " = new org.talend.mq.headers.rfh2.UsrArea();" + NL + "\t\t\t\t\t\t\tjava.util.Map<String, Object> map_usr_";
  protected final String TEXT_106 = " = new java.util.HashMap<String, Object>();";
  protected final String TEXT_107 = " " + NL + "\t\t\t\t\t\t\tmap_usr_";
  protected final String TEXT_108 = ".put(";
  protected final String TEXT_109 = ",";
  protected final String TEXT_110 = ");";
  protected final String TEXT_111 = NL + "\t\t\t\t\t\t\torg.talend.mq.util.TalendMQUtil.setDefinedPropertiesToUser(usr_";
  protected final String TEXT_112 = ",map_usr_";
  protected final String TEXT_113 = ");" + NL + "\t\t\t\t\t\t\tmqrfh2_";
  protected final String TEXT_114 = ".addArea(usr_";
  protected final String TEXT_115 = ");";
  protected final String TEXT_116 = NL + "\t\t\t\t\t\t\tmqrfh2_";
  protected final String TEXT_117 = ".toMessage(message_";
  protected final String TEXT_118 = ");";
  protected final String TEXT_119 = NL + "\t\t\t\t\t\tString msgBody_";
  protected final String TEXT_120 = " = String.valueOf(";
  protected final String TEXT_121 = ".";
  protected final String TEXT_122 = ");" + NL + "\t\t\t\t\t\tmessage_";
  protected final String TEXT_123 = ".writeString(msgBody_";
  protected final String TEXT_124 = ");";
  protected final String TEXT_125 = NL + "\t\t\t\t\t\tString msgBody_";
  protected final String TEXT_126 = " = String.valueOf(";
  protected final String TEXT_127 = ".";
  protected final String TEXT_128 = ");" + NL + "\t\t\t\t\t\tmessage_";
  protected final String TEXT_129 = ".write(msgBody_";
  protected final String TEXT_130 = ".getBytes());";
  protected final String TEXT_131 = NL + "\t\t\t\t\t\tjava.util.Map msgBody_";
  protected final String TEXT_132 = " = new java.util.HashMap();";
  protected final String TEXT_133 = NL + "\t\t\t\t\t\t\tmsgBody_";
  protected final String TEXT_134 = ".put(\"";
  protected final String TEXT_135 = "\",";
  protected final String TEXT_136 = ".";
  protected final String TEXT_137 = ");";
  protected final String TEXT_138 = NL + "\t\t\t\t    \tmessage_";
  protected final String TEXT_139 = ".writeObject(msgBody_";
  protected final String TEXT_140 = ");" + NL + "\t\t\t\t  ";
  protected final String TEXT_141 = NL + "\t\t\t\t\tremoteQ_";
  protected final String TEXT_142 = ".put(message_";
  protected final String TEXT_143 = ", opM_";
  protected final String TEXT_144 = ");";
  protected final String TEXT_145 = NL + "\t" + NL + "" + NL + "" + NL + "\t\t";
  protected final String TEXT_146 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	String serverType=ElementParameterParser.getValue(node, "__SERVER__");
	String useMsgId=ElementParameterParser.getValue(node, "__IS_USE_MESSAGE_ID__");
	boolean useMQFormat = ("true").equals(ElementParameterParser.getValue(node, "__USE_FORMAT__"));
	String wsMQFormat = ElementParameterParser.getValue(node, "__WS_MQ_FORMAT__");
	
	String msgBobyType =  ElementParameterParser.getValue(node, "__MESSAGE_BODY_TYPE__");
	
	boolean setJmsHeader =  ("true").equals(ElementParameterParser.getValue(node, "__SET_JMS_HEADER__"));
	List<Map<String,String>> jmsHeaders = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__JMS_HEADERS__");
	
	boolean setJmsProp =  ("true").equals(ElementParameterParser.getValue(node, "__SET_JMS_PROPERTIES__"));
	List<Map<String,String>> jmsProps = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__JMS_PROPERTIES__");

	boolean setMQMDField =  ("true").equals(ElementParameterParser.getValue(node, "__SET_MQMD_FIELDS__"));
	List<Map<String,String>> mqmdFields = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__MQMD_FIELDS__");

	boolean useMqHeader = "true".equals(ElementParameterParser.getValue(node, "__USE_MQ_HEADER__"));
	boolean useFixedMqRFH2 = "true".equals(ElementParameterParser.getValue(node, "__USE_FIX_MQRFH2__"));
	List<Map<String,String>> mqrfh2FixedFields = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__MQRFH2_FIXED_FIELD__");

	boolean useMqRFH2_mcd = "true".equals(ElementParameterParser.getValue(node, "__USE_MQRFH2_MCD__"));
	List<Map<String,String>> mqrfh2mcdFields = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__MQRFH2_MCD_FIELD__");

	boolean useMqRFH2_jms = "true".equals(ElementParameterParser.getValue(node, "__USE_MQRFH2_JMS__"));
	List<Map<String,String>> mqrfh2jmsFields = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__MQRFH2_JMS_FIELD__");

	boolean useMqRFH2_usr = "true".equals(ElementParameterParser.getValue(node, "__USE_MQRFH2_USR__"));
	List<Map<String,String>> mqrfh2usrFields = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__MQRFH2_USR_FIELD__");

	List<IMetadataTable> metadatas = node.getMetadataList();
	if ((metadatas!=null)&&(metadatas.size()>0)) {
		IMetadataTable metadata = metadatas.get(0);
		List<IMetadataColumn> columns = metadata.getListColumns();
		List< ? extends IConnection> conns = node.getIncomingConnections();
		if((conns!=null)&&(conns.size()>0)) {
			IConnection conn = conns.get(0);
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
			
				if (("JBoss").equals(serverType) || ("ActiveMQ").equals(serverType)) {
				
					/*-------------------1.is use message id.this functions just use map message type-------------------------------------*/
					if(("true").equals(useMsgId)){

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(((IMetadataColumn)columns.get(1)).getLabel() );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    
					}
					
					/*--------------------------2.judge message body type----------------------------------------------------------------*/
					if ("Text".equals(msgBobyType)) {

    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(((IMetadataColumn)columns.get(0)).getLabel());
    stringBuffer.append(TEXT_11);
    
						if(!("true").equals(useMsgId)){

    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_15);
    
						} else {

    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    
						}
					} else if ("Bytes".equals(msgBobyType)) {
						IMetadataColumn column = (IMetadataColumn)columns.get(0);
						String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());

    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(((IMetadataColumn)columns.get(0)).getLabel());
    stringBuffer.append(TEXT_23);
    
						if(!("true").equals(useMsgId)){

    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_28);
    
						} else {

    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    
						}
					} else if ("Map".equals(msgBobyType)) {

    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    
						for(IMetadataColumn column : columns) {
							 String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
							  if(("byte[]").equals(typeToGenerate)) {
					                typeToGenerate = "Bytes";
					            }else if(("Character").equals(typeToGenerate)) {
					            	 typeToGenerate = "Char";
					            }else if(("Integer").equals(typeToGenerate)) {
					            	 typeToGenerate = "Int";
					            } else if(("Java.util.Date").equals(typeToGenerate)||"BigDecimal".equals(typeToGenerate)
					            			||"List".equals(typeToGenerate)) {
					            	 typeToGenerate = "Object";
					            }else {
					                typeToGenerate = typeToGenerate.substring(0,1).toUpperCase()+typeToGenerate.substring(1);
					            }
					            if(!"Document".equals(typeToGenerate)){

    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(typeToGenerate);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_39);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_41);
    
								}else{

    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_44);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_46);
    
								}
						}
					} 
					/*---------------------------------------------3.set message headers------------------------------------------------------*/
					
					if (setJmsHeader) {
						for(Map<String,String> header:jmsHeaders) {

    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(header.get("JMS_HEADER_NAME"));
    stringBuffer.append(TEXT_49);
    stringBuffer.append(header.get("JMS_HEADER_VALUE"));
    stringBuffer.append(TEXT_50);
    				
						}
					}
					
					/*---------------------------------------------4.set message headers------------------------------------------------------*/
					
					if (setJmsProp) {
						for(Map<String,String> prop:jmsProps) {

    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(prop.get("JMS_PROPERTIES_TYPE"));
    stringBuffer.append(TEXT_53);
    stringBuffer.append(prop.get("JMS_PROPERTIES_NAME"));
    stringBuffer.append(TEXT_54);
    stringBuffer.append(prop.get("JMS_PROPERTIES_VALUE"));
    stringBuffer.append(TEXT_55);
    				
						}
					}
					
					/*---------------------------------------------5.send message to server------------------------------------------------------*/

    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    
				} else {//server judgement   /***WebSphere MQ*****/

    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
     
					if(useMQFormat) {

    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(wsMQFormat);
    stringBuffer.append(TEXT_63);
    
					}

    stringBuffer.append(TEXT_64);
    
					if(("true").equals(useMsgId) && !"Map".equals(msgBobyType)){

    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_66);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(((IMetadataColumn)columns.get(1)).getLabel() );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_75);
    
					}
										
					/*---------------------------------------------set MQMD Fields------------------------------------------------------*/
					
					if (setMQMDField) {
						for(Map<String,String> field:mqmdFields) {

    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_77);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_78);
    stringBuffer.append(field.get("MQMD_FIELD_VALUE"));
    stringBuffer.append(TEXT_79);
    				
						}
					}
					// include the header: MQRFH2
					if (useMqHeader) {

    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    
						if (useFixedMqRFH2 ) {
							for(Map<String,String> field : mqrfh2FixedFields) {

    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_84);
    stringBuffer.append(field.get("VALUE"));
    stringBuffer.append(TEXT_85);
    
							}
						}
						if(useMqRFH2_mcd ) { // mcd folder

    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    
							for(Map<String,String> field : mqrfh2mcdFields) {

    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_90);
    stringBuffer.append(field.get("VALUE"));
    stringBuffer.append(TEXT_91);
    
							}

    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    
						}
						if(useMqRFH2_jms ) { //jms folder

    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    
							for(Map<String,String> field : mqrfh2jmsFields) {

    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_99);
    stringBuffer.append(field.get("VALUE"));
    stringBuffer.append(TEXT_100);
    
							}

    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_102);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_103);
    
						}
						if(useMqRFH2_usr ) { //usr folder

    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    
							for(Map<String,String> field : mqrfh2usrFields) {

    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_109);
    stringBuffer.append(field.get("VALUE"));
    stringBuffer.append(TEXT_110);
    
							}

    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    
						}

    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_118);
    
					}
					
					if ("Text".equals(msgBobyType)) {

    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_120);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(((IMetadataColumn)columns.get(0)).getLabel());
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_123);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_124);
    
					} else if ("Bytes".equals(msgBobyType)) {

    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_126);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_127);
    stringBuffer.append(((IMetadataColumn)columns.get(0)).getLabel());
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_130);
    
					} else if ("Map".equals(msgBobyType)) {

    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_132);
    
						for(IMetadataColumn column : columns) {

    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_134);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_135);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_137);
    
						}

    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_140);
    
					}

    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_143);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_144);
    
				}
			}
		}
	}  

    stringBuffer.append(TEXT_145);
    stringBuffer.append(TEXT_146);
    return stringBuffer.toString();
  }
}
