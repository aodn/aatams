package org.talend.designer.codegen.translators.file.output;

import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class TFileOutputEBCDICBeginJava
{
  protected static String nl;
  public static synchronized TFileOutputEBCDICBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFileOutputEBCDICBeginJava result = new TFileOutputEBCDICBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "java.io.File file_";
  protected final String TEXT_3 = " = new java.io.File(";
  protected final String TEXT_4 = "); ";
  protected final String TEXT_5 = NL + "//create directory only if not exists" + NL + " java.io.File parentFile_";
  protected final String TEXT_6 = " = file_";
  protected final String TEXT_7 = ".getParentFile();  " + NL + " \tif(parentFile_";
  protected final String TEXT_8 = " != null && !parentFile_";
  protected final String TEXT_9 = ".exists()) {  " + NL + " \t   parentFile_";
  protected final String TEXT_10 = ".mkdirs();" + NL + " \t} ";
  protected final String TEXT_11 = NL + "  \tint nb_line_";
  protected final String TEXT_12 = " = 0;" + NL + "    java.io.FileOutputStream fOut_";
  protected final String TEXT_13 = " = new java.io.FileOutputStream(";
  protected final String TEXT_14 = ");";
  protected final String TEXT_15 = NL + "    // write file" + NL + "    javax.xml.bind.JAXBContext jaxbContextW_";
  protected final String TEXT_16 = " = javax.xml.bind.JAXBContext.newInstance(\"net.sf.cobol2j\");" + NL + "    javax.xml.bind.Unmarshaller unmarshallerW_";
  protected final String TEXT_17 = " = jaxbContextW_";
  protected final String TEXT_18 = ".createUnmarshaller();" + NL + "    Object oW_";
  protected final String TEXT_19 = " = unmarshallerW_";
  protected final String TEXT_20 = ".unmarshal(new java.io.FileInputStream(";
  protected final String TEXT_21 = "));" + NL + "    net.sf.cobol2j.FileFormat fFW_";
  protected final String TEXT_22 = " = (net.sf.cobol2j.FileFormat) oW_";
  protected final String TEXT_23 = ";" + NL + "    net.sf.cobol2j.RecordWriter rwriter_";
  protected final String TEXT_24 = " = new net.sf.cobol2j.RecordWriter(fOut_";
  protected final String TEXT_25 = ", fFW_";
  protected final String TEXT_26 = " );";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
    CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
    INode node = (INode)codeGenArgument.getArgument();
    String cid = node.getUniqueName();	

    stringBuffer.append(TEXT_1);
    
	String filename = ElementParameterParser.getValue(node,"__FILENAME__");
	String copybook = ElementParameterParser.getValue(node,"__COPYBOOK__");
	String customSetOriginalLengthStr = ElementParameterParser.getValue(node,"__NO_X2CJ_FILE__");
	boolean customSetOriginalLength = (customSetOriginalLengthStr!=null&&!("").equals(customSetOriginalLengthStr))?("true").equals(customSetOriginalLengthStr):true;

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(filename );
    stringBuffer.append(TEXT_4);
    
if(("true").equals(ElementParameterParser.getValue(node,"__CREATE__"))){

    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    
}

    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append( filename );
    stringBuffer.append(TEXT_14);
    
if(!customSetOriginalLength){//------11111

    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append( copybook );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    
}

    return stringBuffer.toString();
  }
}
