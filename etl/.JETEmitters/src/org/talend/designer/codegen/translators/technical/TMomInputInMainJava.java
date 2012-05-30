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

public class TMomInputInMainJava
{
  protected static String nl;
  public static synchronized TMomInputInMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TMomInputInMainJava result = new TMomInputInMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "\t";
  protected final String TEXT_2 = NL + "\t\t\t\t\t";
  protected final String TEXT_3 = ".from=";
  protected final String TEXT_4 = ";\t" + NL + "\t\t\t\t";
  protected final String TEXT_5 = NL + "\t\t\t\t\t";
  protected final String TEXT_6 = ".message=ParserUtils.parseTo_Document(msg_";
  protected final String TEXT_7 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_8 = NL + "\t\t\t\t\t";
  protected final String TEXT_9 = ".message=msg_";
  protected final String TEXT_10 = ";\t" + NL + "\t\t\t\t";
  protected final String TEXT_11 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_12 = ".";
  protected final String TEXT_13 = " = msg_";
  protected final String TEXT_14 = ".get";
  protected final String TEXT_15 = "(\"";
  protected final String TEXT_16 = "\");" + NL + "" + NL + "\t\t\t\t\t";
  protected final String TEXT_17 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_18 = ".";
  protected final String TEXT_19 = " = ParserUtils.parseTo_";
  protected final String TEXT_20 = "(msg_";
  protected final String TEXT_21 = ".getObject(\"";
  protected final String TEXT_22 = "\").toString());" + NL + "" + NL + "\t\t\t\t\t";
  protected final String TEXT_23 = "\t" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_24 = ".";
  protected final String TEXT_25 = " = message_";
  protected final String TEXT_26 = ".get";
  protected final String TEXT_27 = "();" + NL + "\t\t\t\t\t";
  protected final String TEXT_28 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_29 = ".";
  protected final String TEXT_30 = " = message_";
  protected final String TEXT_31 = ".get";
  protected final String TEXT_32 = "Property(";
  protected final String TEXT_33 = ");" + NL + "\t\t\t\t\t";
  protected final String TEXT_34 = "\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_35 = ".messageid=";
  protected final String TEXT_36 = ";" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}" + NL + "\t\t\t\t";
  protected final String TEXT_37 = "\t    " + NL + "\t";
  protected final String TEXT_38 = NL + "\t\t\t\t\t";
  protected final String TEXT_39 = ".from=";
  protected final String TEXT_40 = ";\t" + NL + "\t\t\t\t";
  protected final String TEXT_41 = NL + "\t\t\t\t\t";
  protected final String TEXT_42 = ".message=ParserUtils.parseTo_Document(msg_";
  protected final String TEXT_43 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_44 = NL + "\t\t\t\t\t";
  protected final String TEXT_45 = ".message=msg_";
  protected final String TEXT_46 = ";\t" + NL + "\t\t\t\t";
  protected final String TEXT_47 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_48 = ".";
  protected final String TEXT_49 = " = (";
  protected final String TEXT_50 = ")msg_";
  protected final String TEXT_51 = ".get(\"";
  protected final String TEXT_52 = "\");" + NL + "\t\t\t\t\t";
  protected final String TEXT_53 = NL + "\t\t\t\t\t\t";
  protected final String TEXT_54 = ".";
  protected final String TEXT_55 = " = inMessage";
  protected final String TEXT_56 = ".";
  protected final String TEXT_57 = ";" + NL + "\t\t\t\t\t";
  protected final String TEXT_58 = NL + "\t\t\t\t\t";
  protected final String TEXT_59 = ".messageid=new String(inMessage";
  protected final String TEXT_60 = ".messageId,\"ISO-8859-15\");\t" + NL + "\t\t\t\t";
  protected final String TEXT_61 = NL + "\t\t\t\t";
  protected final String TEXT_62 = ".";
  protected final String TEXT_63 = " = mqrfh2_";
  protected final String TEXT_64 = ".get";
  protected final String TEXT_65 = "();" + NL + "\t\t\t\t";
  protected final String TEXT_66 = NL + "\t\t\t\torg.talend.mq.headers.rfh2.McdArea mcd_";
  protected final String TEXT_67 = " = (org.talend.mq.headers.rfh2.McdArea)mqrfh2_";
  protected final String TEXT_68 = ".getArea(\"mcd\");" + NL + "\t\t\t\tif(mcd_";
  protected final String TEXT_69 = " != null) {";
  protected final String TEXT_70 = NL + "\t\t\t\t";
  protected final String TEXT_71 = ".";
  protected final String TEXT_72 = " = mcd_";
  protected final String TEXT_73 = ".get";
  protected final String TEXT_74 = "();";
  protected final String TEXT_75 = NL + "\t\t\t\t}";
  protected final String TEXT_76 = NL + "\t\t\t\torg.talend.mq.headers.rfh2.JmsArea jms_";
  protected final String TEXT_77 = " = (org.talend.mq.headers.rfh2.JmsArea)mqrfh2_";
  protected final String TEXT_78 = ".getArea(\"jms\");" + NL + "\t\t\t\tif (jms_";
  protected final String TEXT_79 = " != null ) {";
  protected final String TEXT_80 = NL + "\t\t\t\t";
  protected final String TEXT_81 = ".";
  protected final String TEXT_82 = " = jms_";
  protected final String TEXT_83 = ".get";
  protected final String TEXT_84 = "();";
  protected final String TEXT_85 = NL + "\t\t\t\t}";
  protected final String TEXT_86 = NL + "\t\t\t\torg.talend.mq.headers.rfh2.UsrArea usr_";
  protected final String TEXT_87 = " = (org.talend.mq.headers.rfh2.UsrArea)mqrfh2_";
  protected final String TEXT_88 = ".getArea(\"usr\");" + NL + "\t\t\t\tif (usr_";
  protected final String TEXT_89 = " != null) {";
  protected final String TEXT_90 = NL + "\t\t\t\t\t";
  protected final String TEXT_91 = ".";
  protected final String TEXT_92 = " = usr_";
  protected final String TEXT_93 = ".get";
  protected final String TEXT_94 = "Property(";
  protected final String TEXT_95 = ");";
  protected final String TEXT_96 = NL + "\t\t\t\t" + NL + "\t\t\t\t}";
  protected final String TEXT_97 = NL + NL + NL + "\t\t";
  protected final String TEXT_98 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	cid = cid.replaceAll("_In", "");
	
	String serverType = ElementParameterParser.getValue(node, "__SERVER__");
	
	String msgBobyType =  ElementParameterParser.getValue(node, "__MESSAGE_BODY_TYPE__");
	
	String useMsgId = ElementParameterParser.getValue(node, "__IS_USE_MESSAGE_ID__");
	String msgId = ElementParameterParser.getValue(node, "__MSG_ID__");
	
	boolean getJmsHeader =  ("true").equals(ElementParameterParser.getValue(node, "__GET_JMS_HEADER__"));
	List<Map<String,String>> jmsHeaders = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__JMS_HEADERS__");
	
	boolean getJmsProp =  ("true").equals(ElementParameterParser.getValue(node, "__GET_JMS_PROPERTIES__"));
	List<Map<String,String>> jmsProps = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__JMS_PROPERTIES__");
	
	boolean getMQMDField =  ("true").equals(ElementParameterParser.getValue(node, "__GET_MQMD_FIELDS__"));
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

	IMetadataTable metadata=null;
	List<IMetadataColumn> columns = null;
	List<IMetadataTable> metadatas = node.getMetadataList();
	if ((metadatas!=null)&&(metadatas.size()>0)) {
		metadata = metadatas.get(0);
		columns = metadata.getListColumns();
	}
	List< ? extends IConnection> conns = node.getOutgoingSortedConnections();

	if (("JBoss").equals(serverType)||("ActiveMQ").equals(serverType)) {
		String from = ElementParameterParser.getValue(node, "__FROM__");
		
		//out put
		for(IConnection conn:conns){
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
		 		String firstConnName = conn.getName();
		 		
		 		if ("Text".equals(msgBobyType) || "Bytes".equals(msgBobyType)) {
				
    stringBuffer.append(TEXT_2);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(from);
    stringBuffer.append(TEXT_4);
    
					if("id_Document".equals(metadata.getColumn("message").getTalendType())){
				
    stringBuffer.append(TEXT_5);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    
					} else {
				
    stringBuffer.append(TEXT_8);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    
					}
				} else {
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
						
    stringBuffer.append(TEXT_11);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(typeToGenerate);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_16);
    
						}else{
					
    stringBuffer.append(TEXT_17);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(typeToGenerate);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_22);
    
						}
					}
				}
				/*---------------------------------------------get message headers------------------------------------------------------*/
				if (getJmsHeader) {
					for(Map<String,String> header:jmsHeaders) {
					
    stringBuffer.append(TEXT_23);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(header.get("REFERENCE_COLUMN"));
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(header.get("JMS_HEADER_NAME"));
    stringBuffer.append(TEXT_27);
    				
					}
				}	
								
				/*---------------------------------------------4.set message headers------------------------------------------------------*/
				if (getJmsProp) {
					for(Map<String,String> prop:jmsProps) {
					
    stringBuffer.append(TEXT_28);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(prop.get("REFERENCE_COLUMN"));
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(prop.get("JMS_PROPERTIES_TYPE"));
    stringBuffer.append(TEXT_32);
    stringBuffer.append(prop.get("JMS_PROPERTIES_NAME"));
    stringBuffer.append(TEXT_33);
    				
					}
				}					


				if(("true").equals(useMsgId) && !"Map".equals(msgBobyType)){
				
    stringBuffer.append(TEXT_34);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(msgId);
    stringBuffer.append(TEXT_36);
    
				}
			}
		}	
		/*---------------------------------------------------------end 4----------------------------------------------------------------*/	
		
    stringBuffer.append(TEXT_37);
    
	} else { //server judgement   /***WebSphere MQ*****/
		String queue = ElementParameterParser.getValue(node, "__QUEUE__");

		for(IConnection conn:conns){
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
	 			String firstConnName = conn.getName();
		 		if ("Text".equals(msgBobyType) || "Bytes".equals(msgBobyType)) {
				
    stringBuffer.append(TEXT_38);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(queue);
    stringBuffer.append(TEXT_40);
    
					if("id_Document".equals(metadata.getColumn("message").getTalendType())){
				
    stringBuffer.append(TEXT_41);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    
					} else {
				
    stringBuffer.append(TEXT_44);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_46);
    
					}
				} else {
					for(IMetadataColumn column : columns) {
						String columType = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
						
    stringBuffer.append(TEXT_47);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(columType);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_52);
    
					}
				}
		
				/*---------------------------------------------get MQMD Fields------------------------------------------------------*/
			
				if (getMQMDField) {
					for(Map<String,String> field:mqmdFields) {
					
    stringBuffer.append(TEXT_53);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(field.get("REFERENCE_COLUMN"));
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_57);
    				
					}
				}
		
				if(("true").equals(useMsgId)&& !"Map".equals(msgBobyType)){
				
    stringBuffer.append(TEXT_58);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    
				}
				
				// extract the MQRFH2 header
				if (useMqHeader) {
					if (useFixedMqRFH2) {  // fix portion
						for(Map<String,String> field : mqrfh2FixedFields) {
				
    stringBuffer.append(TEXT_61);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(field.get("REFERENCE_COLUMN"));
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_65);
    
						}
					}
					if(useMqRFH2_mcd ) { // mcd folder

    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    
						for(Map<String,String> field : mqrfh2mcdFields) {

    stringBuffer.append(TEXT_70);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_71);
    stringBuffer.append(field.get("REFERENCE_COLUMN"));
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_74);
    
						}

    stringBuffer.append(TEXT_75);
    
					}
					if(useMqRFH2_jms ) { // jms folder

    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_79);
    
						for(Map<String,String> field : mqrfh2jmsFields) {

    stringBuffer.append(TEXT_80);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(field.get("REFERENCE_COLUMN"));
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_83);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_84);
    
						}

    stringBuffer.append(TEXT_85);
    
					}
					
					if(useMqRFH2_usr ) { //usr folder

    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    
						for(Map<String,String> field : mqrfh2usrFields) {
							for(IMetadataColumn column : columns) {
								if(column.getLabel().equals(field.get("REFERENCE_COLUMN"))) {
									String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), true);
									if(typeToGenerate.equals("Integer")) {
										typeToGenerate = "Int";
									}

    stringBuffer.append(TEXT_90);
    stringBuffer.append(firstConnName);
    stringBuffer.append(TEXT_91);
    stringBuffer.append(field.get("REFERENCE_COLUMN"));
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(typeToGenerate);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(field.get("MQMD_FIELD_NAME"));
    stringBuffer.append(TEXT_95);
    
									break;
								}
							}
						}

    stringBuffer.append(TEXT_96);
    
					}
					
				}
			}
		}// for
	}
	
    stringBuffer.append(TEXT_97);
    stringBuffer.append(TEXT_98);
    return stringBuffer.toString();
  }
}
