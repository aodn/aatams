package org.talend.designer.codegen.translators.system;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.EConnectionType;
import java.util.List;
import java.util.Map;

public class TSystemBeginJava
{
  protected static String nl;
  public static synchronized TSystemBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TSystemBeginJava result = new TSystemBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\tString[] command_";
  protected final String TEXT_3 = " = new String[";
  protected final String TEXT_4 = "];";
  protected final String TEXT_5 = NL + "\t\t\tcommand_";
  protected final String TEXT_6 = "[";
  protected final String TEXT_7 = "] = ";
  protected final String TEXT_8 = ";";
  protected final String TEXT_9 = NL + "final java.util.Vector<String> output_";
  protected final String TEXT_10 = " = new java.util.Vector<String>();";
  protected final String TEXT_11 = NL + "Runtime runtime_";
  protected final String TEXT_12 = " = Runtime.getRuntime();" + NL + "" + NL + "String[] env_";
  protected final String TEXT_13 = "= null;" + NL + "java.util.Map<String,String> envMap_";
  protected final String TEXT_14 = "= System.getenv();" + NL + "java.util.Map<String,String> envMapClone_";
  protected final String TEXT_15 = "= new java.util.HashMap();" + NL + "envMapClone_";
  protected final String TEXT_16 = ".putAll(envMap_";
  protected final String TEXT_17 = ");" + NL;
  protected final String TEXT_18 = NL + "envMapClone_";
  protected final String TEXT_19 = ".put(";
  protected final String TEXT_20 = ", ";
  protected final String TEXT_21 = ");";
  protected final String TEXT_22 = NL + "env_";
  protected final String TEXT_23 = "= new String[envMapClone_";
  protected final String TEXT_24 = ".size()];" + NL + "int i_";
  protected final String TEXT_25 = "= 0;" + NL + "for (String envKey : (java.util.Set<String>) envMapClone_";
  protected final String TEXT_26 = ".keySet()) {" + NL + "    env_";
  protected final String TEXT_27 = "[i_";
  protected final String TEXT_28 = "++]= envKey + \"=\" + envMapClone_";
  protected final String TEXT_29 = ".get(envKey);" + NL + "}";
  protected final String TEXT_30 = NL + "final Process ps_";
  protected final String TEXT_31 = " = runtime_";
  protected final String TEXT_32 = ".exec(";
  protected final String TEXT_33 = " ";
  protected final String TEXT_34 = " ";
  protected final String TEXT_35 = "command_";
  protected final String TEXT_36 = " ";
  protected final String TEXT_37 = ",env_";
  protected final String TEXT_38 = ",new java.io.File(";
  protected final String TEXT_39 = "));";
  protected final String TEXT_40 = NL + "final Process ps_";
  protected final String TEXT_41 = " = runtime_";
  protected final String TEXT_42 = ".exec(";
  protected final String TEXT_43 = " ";
  protected final String TEXT_44 = " ";
  protected final String TEXT_45 = "command_";
  protected final String TEXT_46 = " ";
  protected final String TEXT_47 = ",env_";
  protected final String TEXT_48 = ");";
  protected final String TEXT_49 = NL + NL + "globalMap.remove(\"";
  protected final String TEXT_50 = "_OUTPUT\");" + NL + "globalMap.remove(\"";
  protected final String TEXT_51 = "_ERROROUTPUT\");" + NL + "" + NL + "Thread normal_";
  protected final String TEXT_52 = " = new Thread() {" + NL + "\tpublic void run() {" + NL + "\t\ttry {" + NL + "\t\t\tjava.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(ps_";
  protected final String TEXT_53 = ".getInputStream()));" + NL + "\t\t\tString line = \"\";" + NL + "\t\t\ttry {" + NL + "\t\t\t\twhile((line = reader.readLine()) != null) {";
  protected final String TEXT_54 = NL + "\t\t\t\t\tSystem.out.println(line);";
  protected final String TEXT_55 = NL + "\t\t\t\t\tif (globalMap.get(\"";
  protected final String TEXT_56 = "_OUTPUT\") != null) {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_57 = "_OUTPUT\", (String)globalMap.get(\"";
  protected final String TEXT_58 = "_OUTPUT\")+\"\\n\"+line);" + NL + "\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_59 = "_OUTPUT\", line);" + NL + "\t\t\t\t\t}";
  protected final String TEXT_60 = NL + "        System.out.println(line);" + NL + "        " + NL + "        \tif (globalMap.get(\"";
  protected final String TEXT_61 = "_OUTPUT\") != null) {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_62 = "_OUTPUT\", (String)globalMap.get(\"";
  protected final String TEXT_63 = "_OUTPUT\")+\"\\n\"+line);" + NL + "\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_64 = "_OUTPUT\", line);" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\t" + NL + "    \t ";
  protected final String TEXT_65 = NL + "\t\t\t\t\toutput_";
  protected final String TEXT_66 = ".add(line);";
  protected final String TEXT_67 = NL + "\t\t\t\t}" + NL + "\t\t\t} finally {" + NL + "\t\t\t\treader.close();" + NL + "\t\t\t}" + NL + "\t\t} catch(java.io.IOException ioe) {" + NL + "\t\t\tioe.printStackTrace();" + NL + "\t\t}" + NL + "\t}" + NL + "};" + NL + "normal_";
  protected final String TEXT_68 = ".start();" + NL + "" + NL + "Thread error_";
  protected final String TEXT_69 = " = new Thread() {" + NL + "\tpublic void run() {" + NL + "\t\ttry {" + NL + "\t\t\tjava.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(ps_";
  protected final String TEXT_70 = ".getErrorStream()));" + NL + "\t\t\tString line = \"\";" + NL + "\t\t\ttry {" + NL + "\t\t\t\twhile((line = reader.readLine()) != null) {";
  protected final String TEXT_71 = NL + "\t\t\t\t\tSystem.err.println(line);";
  protected final String TEXT_72 = NL + "\t\t\t\t\tif (globalMap.get(\"";
  protected final String TEXT_73 = "_ERROROUTPUT\") != null) {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_74 = "_ERROROUTPUT\", (String)globalMap.get(\"";
  protected final String TEXT_75 = "_ERROROUTPUT\")+\"\\n\"+line);" + NL + "\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_76 = "_ERROROUTPUT\", line);" + NL + "\t\t\t\t\t}";
  protected final String TEXT_77 = NL + "        System.err.println(line);" + NL + "        " + NL + "        \tif (globalMap.get(\"";
  protected final String TEXT_78 = "_ERROROUTPUT\") != null) {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_79 = "_ERROROUTPUT\", (String)globalMap.get(\"";
  protected final String TEXT_80 = "_ERROROUTPUT\")+\"\\n\"+line);" + NL + "\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\tglobalMap.put(\"";
  protected final String TEXT_81 = "_ERROROUTPUT\", line);" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\t" + NL + "    \t ";
  protected final String TEXT_82 = NL + "\t\t\t\t\toutput_";
  protected final String TEXT_83 = ".add(line);";
  protected final String TEXT_84 = NL + "\t\t\t\t}" + NL + "\t\t\t} finally {" + NL + "\t\t\t\treader.close();" + NL + "\t\t\t}" + NL + "\t\t} catch(java.io.IOException ioe) {" + NL + "\t\t\tioe.printStackTrace();" + NL + "\t\t}" + NL + "\t}" + NL + "};" + NL + "error_";
  protected final String TEXT_85 = ".start();" + NL + "" + NL + "ps_";
  protected final String TEXT_86 = ".waitFor();" + NL + "normal_";
  protected final String TEXT_87 = ".join(10000);" + NL + "error_";
  protected final String TEXT_88 = ".join(10000);" + NL;
  protected final String TEXT_89 = NL + "\t\t\t\tfor(String tmp_";
  protected final String TEXT_90 = ":output_";
  protected final String TEXT_91 = "){";
  protected final String TEXT_92 = NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_93 = ".";
  protected final String TEXT_94 = " = tmp_";
  protected final String TEXT_95 = ";\t\t\t\t";
  protected final String TEXT_96 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	
	String command = ElementParameterParser.getValue(node, "__COMMAND__");
	
	String outputAction  = ElementParameterParser.getValue(node, "__OUTPUT__");
	
	String errorOuput  = ElementParameterParser.getValue(node, "__ERROROUTPUT__");
	
	
	boolean ifDir = ("true").equals(ElementParameterParser.getValue(node, "__ROOTDIR__"));
	
	String rootDir  = ElementParameterParser.getValue(node, "__DIRECTORY__").replace("\\","/");
	
	List<Map<String, String>> params = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node,"__PARAMS__");
	boolean single_command = ("true").equals(ElementParameterParser.getValue(node, "__USE_SINGLE_COMMAND__"));
	
	boolean array_command = ("true").equals(ElementParameterParser.getValue(node, "__USE_ARRAY_COMMAND__"));
	
	if(array_command){
		List<Map<String,String>> list_data = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node,"__ARRAY_COMMAND__");

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(list_data.size());
    stringBuffer.append(TEXT_4);
    
		int command_array_index = 0;
		for(Map<String,String> map : list_data){

    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(command_array_index++);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(map.get("VALUE"));
    stringBuffer.append(TEXT_8);
    
		}
	}
	
		
	// To launch an internal command to cmd.exe (XP), you have to call :
	// cmd.exe /c dir (for example)

	if (("NORMAL_OUTPUT").equals(outputAction)||("NORMAL_OUTPUT").equals(errorOuput)) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
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
    
    for (int i = 0; i < params.size(); i++) {
        Map<String, String> line = params.get(i);

    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append( line.get("NAME") );
    stringBuffer.append(TEXT_20);
    stringBuffer.append( line.get("VALUE") );
    stringBuffer.append(TEXT_21);
    
    }

    if (params.size() > 0) {

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
    
    }

if(ifDir){
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
     if(single_command) { 
    stringBuffer.append(TEXT_33);
    stringBuffer.append( command);
    stringBuffer.append(TEXT_34);
     } else { 
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
     } 
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(rootDir);
    stringBuffer.append(TEXT_39);
      }else{
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
     if(single_command) { 
    stringBuffer.append(TEXT_43);
    stringBuffer.append( command);
    stringBuffer.append(TEXT_44);
     } else { 
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
     } 
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
      }
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    
	if (("OUTPUT_TO_CONSOLE").equals(outputAction)) {

    stringBuffer.append(TEXT_54);
    
	} else if (("RETRIEVE_OUTPUT").equals(outputAction)) {

    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    
	} else if(("OUTPUT_TO_CONSOLE_AND_RETRIVE_OUTPUT").equals(outputAction)){

    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    
   } else if (("NORMAL_OUTPUT").equals(outputAction)) {

    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_66);
    
	}

    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_70);
    
	if (("OUTPUT_TO_CONSOLE").equals(errorOuput)) {

    stringBuffer.append(TEXT_71);
    
	} else if (("RETRIEVE_OUTPUT").equals(errorOuput)) {

    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
    
	}else if(("OUTPUT_TO_CONSOLE_AND_RETRIVE_OUTPUT").equals(errorOuput)){

    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
     
	}else if (("NORMAL_OUTPUT").equals(errorOuput)) {

    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    
	}

    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_88);
    
if(("NORMAL_OUTPUT").equals(outputAction)||("NORMAL_OUTPUT").equals(errorOuput)){
	List<IMetadataTable> metadatas = node.getMetadataList();
    if ((metadatas!=null)&&(metadatas.size()>0)) {
    	IMetadataTable metadata = metadatas.get(0);
    	if (metadata!=null) {
    		List<IMetadataColumn> columns=metadata.getListColumns();
    		List<? extends IConnection> conns = node.getOutgoingConnections(EConnectionType.FLOW_MAIN);
    		if (conns!=null && conns.size()>0) {
				IConnection conn = conns.get(0);  

    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
					for(IMetadataColumn column:columns) {
						if("outputline".equals(column.getLabel())){

    stringBuffer.append(TEXT_92);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_93);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    
						}
					}
			}
		}
	}
}

    stringBuffer.append(TEXT_96);
    return stringBuffer.toString();
  }
}
