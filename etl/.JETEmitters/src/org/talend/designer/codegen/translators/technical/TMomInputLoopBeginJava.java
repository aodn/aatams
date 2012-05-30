package org.talend.designer.codegen.translators.technical;

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

public class TMomInputLoopBeginJava
{
  protected static String nl;
  public static synchronized TMomInputLoopBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TMomInputLoopBeginJava result = new TMomInputLoopBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "\t";
  protected final String TEXT_2 = NL + "\t\t\tjava.util.Hashtable props_";
  protected final String TEXT_3 = "=new java.util.Hashtable();" + NL + "\t\t\tprops_";
  protected final String TEXT_4 = ".put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,\"org.jnp.interfaces.NamingContextFactory\");" + NL + "\t\t\tprops_";
  protected final String TEXT_5 = ".put(javax.naming.Context.PROVIDER_URL, ";
  protected final String TEXT_6 = "+\":\"+";
  protected final String TEXT_7 = ");" + NL + "\t\t\tprops_";
  protected final String TEXT_8 = ".put(\"java.naming.rmi.security.manager\", \"yes\");" + NL + "\t\t\tprops_";
  protected final String TEXT_9 = ".put(javax.naming.Context.URL_PKG_PREFIXES, \"org.jboss.naming\");" + NL + "\t\t\tprops_";
  protected final String TEXT_10 = ".put(\"java.naming.factory.url.pkgs\",\"org.jboss.naming:org.jnp.interfaces\");\t" + NL + "\t\t\t" + NL + "\t\t\tjavax.naming.Context context_";
  protected final String TEXT_11 = "=new javax.naming.InitialContext(props_";
  protected final String TEXT_12 = ");" + NL + "\t\t\tjavax.jms.ConnectionFactory factory_";
  protected final String TEXT_13 = "=(javax.jms.ConnectionFactory)context_";
  protected final String TEXT_14 = ".lookup(\"ConnectionFactory\");\t" + NL + "\t\t";
  protected final String TEXT_15 = NL + "\t\t\tString url_";
  protected final String TEXT_16 = " = \"tcp://\"+";
  protected final String TEXT_17 = "+\":\"+";
  protected final String TEXT_18 = ";" + NL + "\t\t\t";
  protected final String TEXT_19 = NL + "\t\t\t\torg.apache.activemq.broker.BrokerService broker_";
  protected final String TEXT_20 = " = new org.apache.activemq.broker.BrokerService();" + NL + "\t\t\t\tbroker_";
  protected final String TEXT_21 = ".setUseJmx(true);" + NL + "\t\t\t\tbroker_";
  protected final String TEXT_22 = ".addConnector(url_";
  protected final String TEXT_23 = ");" + NL + "\t\t\t\tbroker_";
  protected final String TEXT_24 = ".start();" + NL + "\t\t\t";
  protected final String TEXT_25 = NL + "\t\t\tSystem.out.println(\"Connecting to URL: \" + url_";
  protected final String TEXT_26 = ");" + NL + "\t\t\tSystem.out.println(\"Consuming \" + (";
  protected final String TEXT_27 = " ? \"topic\" : \"queue\") + \": \" + ";
  protected final String TEXT_28 = ");" + NL + "\t\t\t" + NL + "\t\t\torg.apache.activemq.ActiveMQConnectionFactory factory_";
  protected final String TEXT_29 = " = new org.apache.activemq.ActiveMQConnectionFactory(url_";
  protected final String TEXT_30 = ");" + NL + "\t\t";
  protected final String TEXT_31 = "\t\t" + NL + "\t\t";
  protected final String TEXT_32 = NL + "\t\t\tjavax.jms.Connection connection_";
  protected final String TEXT_33 = " = factory_";
  protected final String TEXT_34 = ".createConnection();" + NL + "\t\t";
  protected final String TEXT_35 = NL + "\t\t\tjavax.jms.Connection connection_";
  protected final String TEXT_36 = " = factory_";
  protected final String TEXT_37 = ".createConnection(";
  protected final String TEXT_38 = ",";
  protected final String TEXT_39 = ");" + NL + "\t\t";
  protected final String TEXT_40 = NL + "\t\tconnection_";
  protected final String TEXT_41 = ".start();" + NL + "\t\tjavax.jms.Session session_";
  protected final String TEXT_42 = " = connection_";
  protected final String TEXT_43 = ".createSession(";
  protected final String TEXT_44 = ", javax.jms.Session.";
  protected final String TEXT_45 = ");" + NL + "\t\tjavax.jms.Destination des_";
  protected final String TEXT_46 = " = null;" + NL + "\t\t";
  protected final String TEXT_47 = NL + "\t\t\tdes_";
  protected final String TEXT_48 = " = session_";
  protected final String TEXT_49 = ".createQueue(";
  protected final String TEXT_50 = ");" + NL + "\t\t";
  protected final String TEXT_51 = NL + "\t    \tdes_";
  protected final String TEXT_52 = " = session_";
  protected final String TEXT_53 = ".createTopic(";
  protected final String TEXT_54 = ");" + NL + "\t\t";
  protected final String TEXT_55 = NL + "\t\tjavax.jms.MessageProducer replyProducer_";
  protected final String TEXT_56 = " = session_";
  protected final String TEXT_57 = ".createProducer(null);" + NL + "\t\treplyProducer_";
  protected final String TEXT_58 = ".setDeliveryMode(javax.jms.DeliveryMode.NON_PERSISTENT);" + NL + "\t\t\t" + NL + "\t\tjavax.jms.MessageConsumer consumer_";
  protected final String TEXT_59 = " = session_";
  protected final String TEXT_60 = ".createConsumer(des_";
  protected final String TEXT_61 = ");" + NL + "\t\t" + NL + "\t\t";
  protected final String TEXT_62 = "\t\t\t\t\t" + NL + "\t\t\tSystem.out.println(\"Ready to receive message\");" + NL + "\t\t\tSystem.out.println(\"Waiting...\");\t" + NL + "\t\t\tjavax.jms.Message message_";
  protected final String TEXT_63 = ";" + NL + "\t\t" + NL + "\t\t\twhile((message_";
  protected final String TEXT_64 = "=consumer_";
  protected final String TEXT_65 = ".receive())!=null){" + NL + "\t\t";
  protected final String TEXT_66 = NL + "\t\t\t\tSystem.out.println(\"We will consume messages while they continue to be delivered \");" + NL + "\t\t\t\tjavax.jms.Message message_";
  protected final String TEXT_67 = ";" + NL + "\t\t\t\twhile ((message_";
  protected final String TEXT_68 = " = consumer_";
  protected final String TEXT_69 = ".receive()) != null) {" + NL + "\t\t\t";
  protected final String TEXT_70 = NL + "\t\t\t\tint maxMsg_";
  protected final String TEXT_71 = " = ";
  protected final String TEXT_72 = ";" + NL + "\t\t\t\tSystem.out.println(\"We are about to wait until we consume: \" + maxMsg_";
  protected final String TEXT_73 = " + \" message(s) then we will shutdown\");" + NL + "\t\t\t\tfor (int i_";
  protected final String TEXT_74 = " = 0; i_";
  protected final String TEXT_75 = " < maxMsg_";
  protected final String TEXT_76 = " ;) {" + NL + "\t\t\t        javax.jms.Message message_";
  protected final String TEXT_77 = " = consumer_";
  protected final String TEXT_78 = ".receive();" + NL + "\t\t\t        if (message_";
  protected final String TEXT_79 = " != null) {" + NL + "\t\t            \ti_";
  protected final String TEXT_80 = "++;" + NL + "\t\t\t";
  protected final String TEXT_81 = NL + "\t\t\t    System.out.println(\"We will wait for messages within: \" + ";
  protected final String TEXT_82 = "*1000 + \" ms, and then we will shutdown\");" + NL + "\t\t\t    javax.jms.Message message_";
  protected final String TEXT_83 = ";" + NL + "\t\t\t    while ((message_";
  protected final String TEXT_84 = " = consumer_";
  protected final String TEXT_85 = ".receive(";
  protected final String TEXT_86 = "*1000)) != null) {" + NL + "\t\t\t";
  protected final String TEXT_87 = NL + "\t\t\tif (message_";
  protected final String TEXT_88 = " instanceof javax.jms.MapMessage) {" + NL + "\t\t\t\tjavax.jms.MapMessage txtMsg_";
  protected final String TEXT_89 = " = (javax.jms.MapMessage) message_";
  protected final String TEXT_90 = ";" + NL + "\t\t\t\tString msg_";
  protected final String TEXT_91 = " = txtMsg_";
  protected final String TEXT_92 = ".getString(";
  protected final String TEXT_93 = ");" + NL + "\t\t\t\tif(msg_";
  protected final String TEXT_94 = " !=null){" + NL + "" + NL + "\t\t";
  protected final String TEXT_95 = NL + "\t\t\t\tjavax.jms.TextMessage txtMsg_";
  protected final String TEXT_96 = " = (javax.jms.TextMessage) message_";
  protected final String TEXT_97 = ";" + NL + "\t\t\t\tString msg_";
  protected final String TEXT_98 = " = txtMsg_";
  protected final String TEXT_99 = ".getText();" + NL + "\t\t\t" + NL + "\t\t\t";
  protected final String TEXT_100 = NL + "\t\t\t\tjavax.jms.BytesMessage bytesMsg_";
  protected final String TEXT_101 = " = (javax.jms.BytesMessage) message_";
  protected final String TEXT_102 = ";" + NL + "\t\t\t\tbyte[] bytesMsgBody_";
  protected final String TEXT_103 = " = new byte[(int)bytesMsg_";
  protected final String TEXT_104 = ".getBodyLength()];" + NL + "\t\t\t\tbytesMsg_";
  protected final String TEXT_105 = ".readBytes(bytesMsgBody_";
  protected final String TEXT_106 = ");" + NL + "\t\t\t\tString msg_";
  protected final String TEXT_107 = " = new String(bytesMsgBody_";
  protected final String TEXT_108 = ");" + NL + "\t\t\t";
  protected final String TEXT_109 = NL + "\t\t\t\tjavax.jms.MapMessage  msg_";
  protected final String TEXT_110 = "  = (javax.jms.MapMessage) message_";
  protected final String TEXT_111 = ";" + NL + "\t\t\t";
  protected final String TEXT_112 = NL + "\t" + NL + "\t";
  protected final String TEXT_113 = NL + "\t\tjava.util.Hashtable properties";
  protected final String TEXT_114 = "=new java.util.Hashtable();" + NL + "\t\tproperties";
  protected final String TEXT_115 = ".put(\"hostname\", ";
  protected final String TEXT_116 = ");" + NL + "\t\tproperties";
  protected final String TEXT_117 = ".put(\"port\", Integer.valueOf(";
  protected final String TEXT_118 = "));" + NL + "\t\tproperties";
  protected final String TEXT_119 = ".put(\"channel\", ";
  protected final String TEXT_120 = ");" + NL + "\t\tproperties";
  protected final String TEXT_121 = ".put(\"CCSID\", new Integer(1208));" + NL + "\t\tproperties";
  protected final String TEXT_122 = ".put(\"transport\",\"MQSeries\");" + NL + "\t\t";
  protected final String TEXT_123 = NL + "\t\t\tcom.ibm.mq.MQEnvironment.sslCipherSuite = \"";
  protected final String TEXT_124 = "\";" + NL + "\t\t";
  protected final String TEXT_125 = NL + "\t\t\tproperties";
  protected final String TEXT_126 = ".put(\"userID\",";
  protected final String TEXT_127 = ");" + NL + "\t\t\tproperties";
  protected final String TEXT_128 = ".put(\"password\",";
  protected final String TEXT_129 = ");" + NL + "\t\t";
  protected final String TEXT_130 = NL + NL + "\t\tcom.ibm.mq.MQQueueManager qMgr";
  protected final String TEXT_131 = "=null;" + NL + "\t\tcom.ibm.mq.MQQueue remoteQ";
  protected final String TEXT_132 = "=null;" + NL + "\t\t";
  protected final String TEXT_133 = NL + "\t\t\tString msgId_";
  protected final String TEXT_134 = " = ";
  protected final String TEXT_135 = ";" + NL + "\t    \tif (msgId_";
  protected final String TEXT_136 = " != null & !(\"\").equals(msgId_";
  protected final String TEXT_137 = ")) {" + NL + "\t\t\t\tString padding_";
  protected final String TEXT_138 = " = new String();" + NL + "\t\t       \tint padlen_";
  protected final String TEXT_139 = " = 24;" + NL + "\t\t " + NL + "\t\t       \tint len_";
  protected final String TEXT_140 = " = Math.abs(padlen_";
  protected final String TEXT_141 = ") - msgId_";
  protected final String TEXT_142 = ".toString().length();" + NL + "\t\t       \tif (len_";
  protected final String TEXT_143 = " > 0) {" + NL + "\t\t        \tfor (int i = 0 ; i < len_";
  protected final String TEXT_144 = " ; i++) {" + NL + "\t\t           \t\tpadding_";
  protected final String TEXT_145 = " = padding_";
  protected final String TEXT_146 = " + \" \";" + NL + "\t\t         \t}" + NL + "\t\t        \tmsgId_";
  protected final String TEXT_147 = " = msgId_";
  protected final String TEXT_148 = " + padding_";
  protected final String TEXT_149 = ";" + NL + "\t\t        }" + NL + "\t\t\t}" + NL + "\t\t";
  protected final String TEXT_150 = NL + "\t\ttry{" + NL + "\t" + NL + "\t\t\tint openOptions";
  protected final String TEXT_151 = "=com.ibm.mq.MQC.MQOO_INPUT_SHARED | com.ibm.mq.MQC.MQOO_FAIL_IF_QUIESCING | com.ibm.mq.MQC.MQOO_INQUIRE";
  protected final String TEXT_152 = " | com.ibm.mq.MQC.MQOO_BROWSE";
  protected final String TEXT_153 = ";" + NL + "\t\t" + NL + "\t\t\tcom.ibm.mq.MQGetMessageOptions gmo";
  protected final String TEXT_154 = "=new com.ibm.mq.MQGetMessageOptions();" + NL + "\t\t\t";
  protected final String TEXT_155 = NL + "\t\t\t\tgmo";
  protected final String TEXT_156 = ".options=gmo";
  protected final String TEXT_157 = ".options+com.ibm.mq.MQC.MQGMO_BROWSE_FIRST;" + NL + "\t\t\t\tint browseCursor_";
  protected final String TEXT_158 = " = 0;" + NL + "\t\t\t";
  protected final String TEXT_159 = NL + "\t\t\t\tgmo";
  protected final String TEXT_160 = ".options=gmo";
  protected final String TEXT_161 = ".options+com.ibm.mq.MQC.MQGMO_SYNCPOINT;" + NL + "\t\t\t";
  protected final String TEXT_162 = NL + "\t\t\tgmo";
  protected final String TEXT_163 = ".options=gmo";
  protected final String TEXT_164 = ".options+com.ibm.mq.MQC.";
  protected final String TEXT_165 = "MQGMO_NO_WAIT";
  protected final String TEXT_166 = "MQGMO_WAIT";
  protected final String TEXT_167 = ";" + NL + "\t\t\tgmo";
  protected final String TEXT_168 = ".options=gmo";
  protected final String TEXT_169 = ".options+com.ibm.mq.MQC.MQGMO_FAIL_IF_QUIESCING;" + NL + "\t\t\tgmo";
  protected final String TEXT_170 = ".waitInterval=com.ibm.mq.MQC.MQWI_UNLIMITED;" + NL + "\t\t\tcom.ibm.mq.MQException.log = null;" + NL + "\t\t\tboolean flag";
  protected final String TEXT_171 = "=true;" + NL + "\t\t" + NL + "\t\t\tqMgr";
  protected final String TEXT_172 = "=new com.ibm.mq.MQQueueManager(";
  protected final String TEXT_173 = ",properties";
  protected final String TEXT_174 = ");" + NL + "\t\t\tremoteQ";
  protected final String TEXT_175 = "=qMgr";
  protected final String TEXT_176 = ".accessQueue(";
  protected final String TEXT_177 = ",openOptions";
  protected final String TEXT_178 = ");" + NL + "\t" + NL + "\t\t\t";
  protected final String TEXT_179 = NL + "\t\t\t\tif(Integer.valueOf(remoteQ";
  protected final String TEXT_180 = ".getCurrentDepth()).equals(0))" + NL + "\t\t\t\t{" + NL + "\t\t\t\t\tflag";
  protected final String TEXT_181 = "= false;" + NL + "\t\t\t\t} \t" + NL + "\t\t\t";
  protected final String TEXT_182 = "\t" + NL + "\t\t\tSystem.out.println(\"Ready to receive message\");" + NL + "\t\t\tSystem.out.println(\"Waiting...\");\t" + NL + "\t\t\twhile(flag";
  protected final String TEXT_183 = "){" + NL + "\t\t\t\t";
  protected final String TEXT_184 = NL + "\t\t\t\t\tif(Integer.valueOf(remoteQ";
  protected final String TEXT_185 = ".getCurrentDepth()).equals(1))" + NL + "\t\t\t\t\t{" + NL + "\t\t\t\t\t\tflag";
  protected final String TEXT_186 = "= false;" + NL + "\t\t\t\t\t} \t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_187 = NL + "\t\t\t\t";
  protected final String TEXT_188 = NL + "\t\t\t\t\tif(browseCursor_";
  protected final String TEXT_189 = " > 0){" + NL + "\t\t\t\t\t\tgmo";
  protected final String TEXT_190 = ".options=com.ibm.mq.MQC.MQGMO_BROWSE_NEXT; " + NL + "\t\t\t\t\t\tgmo";
  protected final String TEXT_191 = ".options=gmo";
  protected final String TEXT_192 = ".options+com.ibm.mq.MQC.";
  protected final String TEXT_193 = "MQGMO_NO_WAIT";
  protected final String TEXT_194 = "MQGMO_WAIT";
  protected final String TEXT_195 = ";" + NL + "\t\t\t\t\t\tgmo";
  protected final String TEXT_196 = ".options=gmo";
  protected final String TEXT_197 = ".options+com.ibm.mq.MQC.MQGMO_FAIL_IF_QUIESCING;" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\tbrowseCursor_";
  protected final String TEXT_198 = "++;" + NL + "\t\t\t\t";
  protected final String TEXT_199 = NL + "\t\t\t\tcom.ibm.mq.MQMessage inMessage";
  protected final String TEXT_200 = "=new com.ibm.mq.MQMessage();" + NL + "\t\t\t\t";
  protected final String TEXT_201 = NL + "\t\t\t\t\tinMessage";
  protected final String TEXT_202 = ".messageId = msgId_";
  protected final String TEXT_203 = ".getBytes(\"ISO-8859-15\");" + NL + "\t\t\t\t";
  protected final String TEXT_204 = "\t\t" + NL + "\t\t\t\t\ttry{" + NL + "\t\t\t\t\t\tremoteQ";
  protected final String TEXT_205 = ".get(inMessage";
  protected final String TEXT_206 = ",gmo";
  protected final String TEXT_207 = ");" + NL + "\t\t\t\t\t}catch (com.ibm.mq.MQException me_";
  protected final String TEXT_208 = ") {   " + NL + "\t\t\t            if (me_";
  protected final String TEXT_209 = ".reasonCode == com.ibm.mq.MQException.MQRC_NO_MSG_AVAILABLE) {" + NL + "\t\t\t            \tbreak;   " + NL + "\t\t\t            }else{" + NL + "\t\t\t            \tthrow me_";
  protected final String TEXT_210 = ";" + NL + "\t\t\t            }   " + NL + "\t     \t\t\t}" + NL + "\t\t\t\t";
  protected final String TEXT_211 = NL + "\t\t\t\t\tremoteQ";
  protected final String TEXT_212 = ".get(inMessage";
  protected final String TEXT_213 = ",gmo";
  protected final String TEXT_214 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_215 = NL + "\t\t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_216 = "\t\t\t\t" + NL + "\t\t\t\torg.talend.mq.headers.rfh2.MQRFH2 mqrfh2_";
  protected final String TEXT_217 = " = new org.talend.mq.headers.rfh2.MQRFH2(inMessage";
  protected final String TEXT_218 = ");" + NL + "\t\t\t\t" + NL + "\t\t\t\t";
  protected final String TEXT_219 = NL + NL + "\t\t\t\t";
  protected final String TEXT_220 = NL + "\t\t    \t\tString msg_";
  protected final String TEXT_221 = "=inMessage";
  protected final String TEXT_222 = ".readStringOfByteLength(inMessage";
  protected final String TEXT_223 = ".getDataLength());" + NL + "\t\t\t\t";
  protected final String TEXT_224 = NL + "\t\t\t\t\tbyte[] bytesMsgBody_";
  protected final String TEXT_225 = " = new byte[inMessage";
  protected final String TEXT_226 = ".getDataLength()];" + NL + "\t\t\t\t\tinMessage";
  protected final String TEXT_227 = ".readFully(bytesMsgBody_";
  protected final String TEXT_228 = ");" + NL + "\t\t\t\t\tString msg_";
  protected final String TEXT_229 = " = new String(bytesMsgBody_";
  protected final String TEXT_230 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_231 = NL + "\t\t\t\t\tjava.util.Map msg_";
  protected final String TEXT_232 = " = (java.util.Map)inMessage";
  protected final String TEXT_233 = ".readObject();\t\t\t  " + NL + "\t\t\t\t";
  protected final String TEXT_234 = NL + "\t\t\t\t\tqMgr";
  protected final String TEXT_235 = ".commit();" + NL + "\t\t\t\t";
  protected final String TEXT_236 = NL + NL + NL + "\t\t";
  protected final String TEXT_237 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	cid = cid.replaceAll("_Loop", "");
	String serverType=ElementParameterParser.getValue(node, "__SERVER__");
	String host=ElementParameterParser.getValue(node, "__SERVERADDRESS__");
	String port=ElementParameterParser.getValue(node, "__SERVERPORT__");
	String kListen=ElementParameterParser.getValue(node, "__KEEPLISTENING__");
	String msgBobyType =  ElementParameterParser.getValue(node, "__MESSAGE_BODY_TYPE__");
	String msgId=ElementParameterParser.getValue(node, "__MSG_ID__");
	String useMsgId=ElementParameterParser.getValue(node, "__IS_USE_MESSAGE_ID__");
	String from=ElementParameterParser.getValue(node, "__FROM__");
	String msgType = ElementParameterParser.getValue(node, "__MSGTYPE__");
	
	boolean transacted = "true".equals(ElementParameterParser.getValue(node, "__IS_TRANSACTED__"));
	String acknowledgmentMode = ElementParameterParser.getValue(node, "__ACKNOWLEDGMENT_MODE__");
	
	String dbuser= ElementParameterParser.getValue(node, "__USER__");
	String dbpwd= ElementParameterParser.getValue(node, "__PASS__");
	
	boolean useMqHeader = "true".equals(ElementParameterParser.getValue(node, "__USE_MQ_HEADER__"));
    	
	IMetadataTable metadata=null;
	List<IMetadataColumn> columns = null;
	List<IMetadataTable> metadatas = node.getMetadataList();
	if ((metadatas!=null)&&(metadatas.size()>0)) {
		metadata = metadatas.get(0);
		columns = metadata.getListColumns();
	}
	if (("JBoss").equals(serverType)||("ActiveMQ").equals(serverType)) {
	
		/*---------------------------------------1.initial jms connection factry---------------------------------*/ 
		if(("JBoss").equals(serverType) ){ // server judgement
		
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(host);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(port);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
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
    		
		}else if(("ActiveMQ").equals(serverType)){
			boolean startServer = ("true").equals(ElementParameterParser.getValue(node, "__STARTSERVER__"));
			
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(host);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(port);
    stringBuffer.append(TEXT_18);
    
			if(startServer){
			
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    
			}
			
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append("Topic".equals(msgType));
    stringBuffer.append(TEXT_27);
    stringBuffer.append(from);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    
		}
		
		/*---------------------------------------2.create Queue Or Topic from connection ---------------------------------*/ 
		
    stringBuffer.append(TEXT_31);
    	
		if(dbuser == null || ("\"\"").equals(dbuser) || ("").equals(dbuser)) {
		
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    
		} else {
		
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(dbuser);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(dbpwd);
    stringBuffer.append(TEXT_39);
    
		}
		
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(transacted);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(acknowledgmentMode);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    
		if (("Queue").equals(msgType)) {
		
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(from );
    stringBuffer.append(TEXT_50);
    
		} else {
		
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(from );
    stringBuffer.append(TEXT_54);
    
		}
		
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
    		
		/*---------------------------------------3.recevice message form server ---------------------------------*/ 	
		if(("JBoss").equals(serverType) ){ 
		
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    		
		}else if(("ActiveMQ").equals(serverType)){
			boolean useMax = ("true").equals(ElementParameterParser.getValue(node,"__USEMAX__"));
			String maxiumMessages = ElementParameterParser.getValue(node,"__MAXMSG__");
			String receiveTimeOut = ElementParameterParser.getValue(node,"__TIMEOUT__");
			if(("true").equals(kListen)){
			
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    
			}else if (useMax) {
			
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(maxiumMessages );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    
			}else {
			
    stringBuffer.append(TEXT_81);
    stringBuffer.append(receiveTimeOut);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(receiveTimeOut);
    stringBuffer.append(TEXT_86);
    
			}
		}
		/*-----------------------------------------------------------4.judge message body type---------------------------------------*/
		if(("true").equals(useMsgId) && !"Map".equals(msgBobyType)){
		
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(msgId);
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    
		}else{
			if ("Text".equals(msgBobyType)) {
			
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_99);
    
			} else if ("Bytes".equals(msgBobyType)) {
			
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_102);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_108);
    
			} else if ("Map".equals(msgBobyType)) {
			
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_111);
    
			}
		}
		
    stringBuffer.append(TEXT_112);
    
	} else { //server judgement   /***WebSphere MQ*****/
		String channel=ElementParameterParser.getValue(node, "__CHANNEL__");
		String qm=ElementParameterParser.getValue(node, "__QM__");
		String queue = ElementParameterParser.getValue(node, "__QUEUE__");
		boolean isRollback = ("true").equals(ElementParameterParser.getValue(node, "__ROLLBACK__"));
		boolean isCommit = ("true").equals(ElementParameterParser.getValue(node, "__COMMIT__"));
		boolean isBrowse = ("true").equals(ElementParameterParser.getValue(node,"__BROWSE__"));
		boolean needSSLCipher = ("true").equals(ElementParameterParser.getValue(node,"__SET_MQ_SSL_CIPHER__"));
		String sslCipher = ElementParameterParser.getValue(node, "__MQ_SSL_CIPHER__");
		isCommit = isCommit && !isBrowse;
		isBrowse = isBrowse && !isRollback && !isCommit;
		
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_115);
    stringBuffer.append(host);
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(port);
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_119);
    stringBuffer.append(channel);
    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_122);
    
		if(needSSLCipher){
		
    stringBuffer.append(TEXT_123);
    stringBuffer.append(sslCipher);
    stringBuffer.append(TEXT_124);
    
		}
		if(!(dbuser == null) && !("\"\"").equals(dbuser) && !("").equals(dbuser)) {
		
    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_126);
    stringBuffer.append(dbuser);
    stringBuffer.append(TEXT_127);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_128);
    stringBuffer.append(dbpwd);
    stringBuffer.append(TEXT_129);
    
		}
		
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_132);
    
		if(("true").equals(useMsgId)){
		
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_134);
    stringBuffer.append(msgId);
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_143);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_145);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_149);
    
		}
		
    stringBuffer.append(TEXT_150);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_151);
    if(isBrowse){
    stringBuffer.append(TEXT_152);
    }
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_154);
    if(isBrowse){
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_157);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_158);
    }else{
    stringBuffer.append(TEXT_159);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    }
    stringBuffer.append(TEXT_162);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_164);
    if(("false").equals(kListen)){
    stringBuffer.append(TEXT_165);
    }else{
    stringBuffer.append(TEXT_166);
    }
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
    stringBuffer.append(qm);
    stringBuffer.append(TEXT_173);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_174);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_175);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_176);
    stringBuffer.append(queue);
    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_178);
    
			if(("false").equals(kListen)) {
			
    stringBuffer.append(TEXT_179);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_180);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_181);
    
			}	
			
    stringBuffer.append(TEXT_182);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_183);
    
				if(("false").equals(kListen)){
				
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_185);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_186);
    
				}
				
    stringBuffer.append(TEXT_187);
    
				if(isBrowse){
				
    stringBuffer.append(TEXT_188);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_190);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_191);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_192);
    if(("false").equals(kListen)){
    stringBuffer.append(TEXT_193);
    }else{
    stringBuffer.append(TEXT_194);
    }
    stringBuffer.append(TEXT_195);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_196);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_197);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_198);
    
				}
				
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_200);
    
				if(("true").equals(useMsgId)&& !"Map".equals(msgBobyType)){
				
    stringBuffer.append(TEXT_201);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_203);
    
				}
				if(("false").equals(kListen) && ("true").equals(useMsgId) && !"Map".equals(msgBobyType) ){
				
    stringBuffer.append(TEXT_204);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_205);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_209);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_210);
    
				} else {
				
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_212);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_213);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_214);
    
				}
				
    stringBuffer.append(TEXT_215);
    
				if (useMqHeader) {
				
    stringBuffer.append(TEXT_216);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_217);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_218);
    }
    stringBuffer.append(TEXT_219);
    		
				if ("Text".equals(msgBobyType)) {
				
    stringBuffer.append(TEXT_220);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_221);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_222);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_223);
    
				} else if ("Bytes".equals(msgBobyType)) {
				
    stringBuffer.append(TEXT_224);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_227);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_228);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_230);
    
				} else if ("Map".equals(msgBobyType)) {
				
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_232);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_233);
    
				}
				if(isCommit){
				
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_235);
    
				}
				
	}
	
    stringBuffer.append(TEXT_236);
    stringBuffer.append(TEXT_237);
    return stringBuffer.toString();
  }
}
