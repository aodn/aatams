package org.talend.designer.codegen.translators.business.microsoft_ax;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.IConnection;
import java.util.List;

public class TMSAXInputEndJava
{
  protected static String nl;
  public static synchronized TMSAXInputEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TMSAXInputEndJava result = new TMSAXInputEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\t\t\t\trecord_";
  protected final String TEXT_3 = ".callMethod(\"Next\");" + NL + "\t\t\t\t}" + NL + "\t\t\t\taxapta3_";
  protected final String TEXT_4 = ".callMethod(\"Logoff\");" + NL + "\t\t\t";
  protected final String TEXT_5 = NL + "\t\t\t\t\tdynRec_";
  protected final String TEXT_6 = ".invoke(\"Next\");" + NL + "\t\t\t\t}" + NL + "\t\t\t\tnetBC_";
  protected final String TEXT_7 = ".invoke(\"Logoff\");" + NL + "\t\t\t";
  protected final String TEXT_8 = NL + "\t\t\tglobalMap.put(\"";
  protected final String TEXT_9 = "_NB_LINE\",nb_line_";
  protected final String TEXT_10 = ");" + NL + "\t\t";
  protected final String TEXT_11 = NL + "\t\t" + NL + "\t\t";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	
	String connectionType = ElementParameterParser.getValue(node, "__CONNECTION_TYPE__");

	List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
	List<IMetadataTable> metadatas = node.getMetadataList();
	if ((metadatas!=null)&&(metadatas.size()>0)) {
		IMetadataTable metadata = metadatas.get(0);
		if (metadata!=null) {
			if("DCOM".equals(connectionType)){
			
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_4);
    
			}else{
			
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    
			}
			
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    
		}
	}
	
    stringBuffer.append(TEXT_11);
    return stringBuffer.toString();
  }
}
