package org.talend.designer.codegen.translators.business_intelligence.jasper;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.List;
import java.util.ArrayList;

public class TJasperOutputExecMainJava
{
  protected static String nl;
  public static synchronized TJasperOutputExecMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TJasperOutputExecMainJava result = new TJasperOutputExecMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t" + NL + "    net.sf.jasperreports.engine.xml.JRXmlDigester digester_";
  protected final String TEXT_2 = " = new net.sf.jasperreports.engine.xml.JRXmlDigester();" + NL + "    net.sf.jasperreports.engine.xml.JRXmlDigesterFactory.configureDigester(digester_";
  protected final String TEXT_3 = ");" + NL + "    net.sf.jasperreports.engine.xml.JRXmlLoader xmlLoader_";
  protected final String TEXT_4 = " = new net.sf.jasperreports.engine.xml.JRXmlLoader(" + NL + "            digester_";
  protected final String TEXT_5 = ");" + NL + "    net.sf.jasperreports.engine.design.JasperDesign jasperDesign_";
  protected final String TEXT_6 = " = xmlLoader_";
  protected final String TEXT_7 = NL + "            .loadXML(new java.io.FileInputStream(";
  protected final String TEXT_8 = "));" + NL + "    jasperDesign_";
  protected final String TEXT_9 = ".setLanguage(net.sf.jasperreports.engine.design.JasperDesign.LANGUAGE_JAVA);" + NL + "    net.sf.jasperreports.engine.JasperCompileManager.compileReportToFile(jasperDesign_";
  protected final String TEXT_10 = ", jasperFile_";
  protected final String TEXT_11 = ");" + NL + "" + NL + "    java.util.Map<String, Object> hm_";
  protected final String TEXT_12 = " = new java.util.HashMap<String, Object>();" + NL + "    " + NL + "        java.util.Locale reportLocale_";
  protected final String TEXT_13 = "=null;";
  protected final String TEXT_14 = NL + "\t\treportLocale_";
  protected final String TEXT_15 = "=java.util.Locale.";
  protected final String TEXT_16 = ";";
  protected final String TEXT_17 = NL + "\t\treportLocale_";
  protected final String TEXT_18 = "=java.util.Locale.getDefault();";
  protected final String TEXT_19 = NL + "    hm_";
  protected final String TEXT_20 = ".put(net.sf.jasperreports.engine.JRParameter.REPORT_LOCALE, reportLocale_";
  protected final String TEXT_21 = ");" + NL + "" + NL + "    net.sf.jasperreports.engine.data.JRCsvDataSource ds_";
  protected final String TEXT_22 = " = new net.sf.jasperreports.engine.data.JRCsvDataSource(" + NL + "            new java.io.File(sourceFile_";
  protected final String TEXT_23 = "),";
  protected final String TEXT_24 = ");" + NL + "" + NL + "    ds_";
  protected final String TEXT_25 = ".setFieldDelimiter(',');" + NL + "    ds_";
  protected final String TEXT_26 = ".setRecordDelimiter(";
  protected final String TEXT_27 = ");" + NL + "    ds_";
  protected final String TEXT_28 = ".setUseFirstRowAsHeader(true);" + NL + "    net.sf.jasperreports.engine.JasperPrint print_";
  protected final String TEXT_29 = " = net.sf.jasperreports.engine.JasperFillManager.fillReport(" + NL + "            jasperFile_";
  protected final String TEXT_30 = ", hm_";
  protected final String TEXT_31 = ", ds_";
  protected final String TEXT_32 = ");" + NL;
  protected final String TEXT_33 = NL + "\tnet.sf.jasperreports.engine.JRExporter exporter_";
  protected final String TEXT_34 = " = new net.sf.jasperreports.engine.export.JRHtmlExporter();\t\t";
  protected final String TEXT_35 = NL + "\tnet.sf.jasperreports.engine.JRExporter exporter_";
  protected final String TEXT_36 = " = new net.sf.jasperreports.engine.export.JRPdfExporter();\t\t";
  protected final String TEXT_37 = NL + "\tnet.sf.jasperreports.engine.JRExporter exporter_";
  protected final String TEXT_38 = " = new net.sf.jasperreports.engine.export.JRXlsExporter();\t\t";
  protected final String TEXT_39 = NL + "\tnet.sf.jasperreports.engine.JRExporter exporter_";
  protected final String TEXT_40 = " = new net.sf.jasperreports.engine.export.JRRtfExporter();\t\t";
  protected final String TEXT_41 = NL + "\tnet.sf.jasperreports.engine.JRExporter exporter_";
  protected final String TEXT_42 = " = new net.sf.jasperreports.engine.export.JRTextExporter();\t\t" + NL + "\texporter_";
  protected final String TEXT_43 = ".setParameter(net.sf.jasperreports.engine.export.JRTextExporterParameter.PAGE_WIDTH, new Float(\"255\"));" + NL + "    exporter_";
  protected final String TEXT_44 = ".setParameter(net.sf.jasperreports.engine.export.JRTextExporterParameter.CHARACTER_WIDTH, new Float(\"10\"));" + NL + "    exporter_";
  protected final String TEXT_45 = ".setParameter(net.sf.jasperreports.engine.export.JRTextExporterParameter.PAGE_HEIGHT, new Float(\"61\"));" + NL + "    exporter_";
  protected final String TEXT_46 = ".setParameter(net.sf.jasperreports.engine.export.JRTextExporterParameter.CHARACTER_HEIGHT, new Float(\"20\"));        ";
  protected final String TEXT_47 = NL + "\tnet.sf.jasperreports.engine.JRExporter exporter_";
  protected final String TEXT_48 = " = new net.sf.jasperreports.engine.export.oasis.JROdtExporter();\t\t";
  protected final String TEXT_49 = NL + "\texporter_";
  protected final String TEXT_50 = ".setParameter(net.sf.jasperreports.engine.JRExporterParameter.CHARACTER_ENCODING, ";
  protected final String TEXT_51 = ");" + NL + "    exporter_";
  protected final String TEXT_52 = ".setParameter(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_FILE_NAME, reportFile_";
  protected final String TEXT_53 = ");" + NL + "    exporter_";
  protected final String TEXT_54 = ".setParameter(net.sf.jasperreports.engine.JRExporterParameter.JASPER_PRINT, print_";
  protected final String TEXT_55 = ");" + NL + "    exporter_";
  protected final String TEXT_56 = ".exportReport();";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
String filename = ElementParameterParser.getValue(node,"__JRXML_FILE__");
String reportType = ElementParameterParser.getValue(node,"__REPORT_TYPE__");
String recordDelimiter = ElementParameterParser.getValue(node, "__RECORD_DELIMITER__");
boolean isSpecifyLocale="true".equals(ElementParameterParser.getValue(node,"__SPECIFY_LOCALE__"));
String reportLocale=ElementParameterParser.getValue(node,"__REPORT_LOCALE__");
String encoding = ElementParameterParser.getValue(node,"__ENCODING__");


    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(filename);
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
    
	if(isSpecifyLocale){

    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(reportLocale);
    stringBuffer.append(TEXT_16);
    
	}else{

    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_18);
    
	}

    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(recordDelimiter);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    
			if(reportType.equalsIgnoreCase("html")){

    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    
			}else if(reportType.equalsIgnoreCase("pdf")){

    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_36);
    
			}else if(reportType.equalsIgnoreCase("xls")){

    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    
			}else if(reportType.equalsIgnoreCase("rtf")){

    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    
			}else if(reportType.equalsIgnoreCase("txt")){

    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_46);
    
			}else if(reportType.equalsIgnoreCase("odt")){

    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    
			}

    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    return stringBuffer.toString();
  }
}
