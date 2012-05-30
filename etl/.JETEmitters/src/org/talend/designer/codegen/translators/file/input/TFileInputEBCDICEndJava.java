package org.talend.designer.codegen.translators.file.input;

import java.util.List;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.IConnection;

public class TFileInputEBCDICEndJava
{
  protected static String nl;
  public static synchronized TFileInputEBCDICEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileInputEBCDICEndJava result = new TFileInputEBCDICEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\t\t}" + NL + "\t\t}";
  protected final String TEXT_2 = NL + "\t\t\t\t}" + NL + "\t\t\t\tfs_";
  protected final String TEXT_3 = ".close();\t";
  protected final String TEXT_4 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
    CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String customSetOriginalLengthStr = ElementParameterParser.getValue(node,"__NO_X2CJ_FILE__");
	String cid = node.getUniqueName();
	boolean customSetOriginalLength = (customSetOriginalLengthStr!=null&&!("").equals(customSetOriginalLengthStr))?("true").equals(customSetOriginalLengthStr):true;
	if(!customSetOriginalLength){

    stringBuffer.append(TEXT_1);
    
	}else{
		List< ? extends IConnection> conns = node.getOutgoingSortedConnections();
			if(conns!=null && conns.size()>0){

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    
			}
	}

    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
