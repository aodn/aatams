package org.talend.designer.codegen.translators.data_quality;

import java.util.List;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class TSchemaComplianceCheckMainJava
{
  protected static String nl;
  public static synchronized TSchemaComplianceCheckMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TSchemaComplianceCheckMainJava result = new TSchemaComplianceCheckMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t";
  protected final String TEXT_2 = " = null;";
  protected final String TEXT_3 = NL + "\trsvUtil_";
  protected final String TEXT_4 = ".setRowValue_";
  protected final String TEXT_5 = "(";
  protected final String TEXT_6 = ");";
  protected final String TEXT_7 = NL + "\tif (rsvUtil_";
  protected final String TEXT_8 = ".ifPassedThrough) {";
  protected final String TEXT_9 = NL + "\t\t";
  protected final String TEXT_10 = " = new ";
  protected final String TEXT_11 = "Struct();";
  protected final String TEXT_12 = NL + "\t\t";
  protected final String TEXT_13 = ".";
  protected final String TEXT_14 = " = ";
  protected final String TEXT_15 = ".";
  protected final String TEXT_16 = ";";
  protected final String TEXT_17 = NL + "\t}";
  protected final String TEXT_18 = NL + "\tif (!rsvUtil_";
  protected final String TEXT_19 = ".ifPassedThrough) {" + NL + "\t\t";
  protected final String TEXT_20 = " = new ";
  protected final String TEXT_21 = "Struct();";
  protected final String TEXT_22 = NL + "\t\t";
  protected final String TEXT_23 = ".";
  protected final String TEXT_24 = " = ";
  protected final String TEXT_25 = ".";
  protected final String TEXT_26 = ";";
  protected final String TEXT_27 = NL + "\t\t";
  protected final String TEXT_28 = ".errorCode = String.valueOf(rsvUtil_";
  protected final String TEXT_29 = ".resultErrorCodeThrough);" + NL + "\t\t";
  protected final String TEXT_30 = ".errorMessage = rsvUtil_";
  protected final String TEXT_31 = ".resultErrorMessageThrough;" + NL + "\t}";
  protected final String TEXT_32 = NL + "\trsvUtil_";
  protected final String TEXT_33 = ".reset();";
  protected final String TEXT_34 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();

	/*in shema:*/
	List<? extends IConnection> listInConns = node.getIncomingConnections();
	String sInConnName = null;
	List<IMetadataColumn> listInColumns = null;

	if (listInConns != null && listInConns.size() > 0) {
		IConnection inConn = listInConns.get(0);
		sInConnName = inConn.getName();
		if (inConn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
			listInColumns = inConn.getMetadataTable().getListColumns();
		}
	}

	/*out shema(include reject and main):*/
	List<? extends IConnection> listOutConns = node.getOutgoingSortedConnections();
	String sRejectConnName = null;

	if (sInConnName != null && listInColumns != null && listInColumns.size() > 0) { //CONDITION_00100 START
		for (IConnection outConn : listOutConns) { //LOOP_00100 START
			if (outConn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) { //CONDITION_001001 START
				if ("REJECT".equals(outConn.getConnectorName())){
					sRejectConnName = outConn.getName();
				}

    stringBuffer.append(TEXT_1);
    stringBuffer.append(outConn.getName());
    stringBuffer.append(TEXT_2);
    
			} //CONDITION_001001 STOP
		} //LOOP_00100 END
		int inputColumnSize = listInColumns.size();
		for (int i = 0; i < inputColumnSize; i++ ) { //LOOP_00101 START
			if(i % 100 == 0){ //CONDITION_001002 START

    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append( (i/100) );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(sInConnName );
    stringBuffer.append(TEXT_6);
    
			} //CONDITION_001002 STOP
		} //LOOP_00101 STOP
		if (listOutConns != null && listOutConns.size() > 0) { //CONDITION_001003 START

    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    
			for (IConnection conn : listOutConns) { //LOOP_00102 START
				if (sRejectConnName == null || (sRejectConnName != null && !sRejectConnName.equals(conn.getName()))){ //CONDITION_0010031 START

    stringBuffer.append(TEXT_9);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_11);
    
					for (IMetadataColumn inColumn : listInColumns) { //LOOP_001021 START

    stringBuffer.append(TEXT_12);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_13);
    stringBuffer.append(inColumn.getLabel());
    stringBuffer.append(TEXT_14);
    stringBuffer.append(sInConnName);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(inColumn.getLabel());
    stringBuffer.append(TEXT_16);
    
					} //LOOP_001021 STOP
				} //CONDITION_0010031 STOP
			} //LOOP_00102 STOP

    stringBuffer.append(TEXT_17);
    
		} //CONDITION_001003 STOP

		if (sRejectConnName != null) { //CONDITION_001004 START

    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(sRejectConnName );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(sRejectConnName );
    stringBuffer.append(TEXT_21);
    
			for (IMetadataColumn inColumn : listInColumns) { //LOOP_00103 START

    stringBuffer.append(TEXT_22);
    stringBuffer.append(sRejectConnName);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(inColumn.getLabel());
    stringBuffer.append(TEXT_24);
    stringBuffer.append(sInConnName);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(inColumn.getLabel());
    stringBuffer.append(TEXT_26);
    
			} //LOOP_00103 STOP

    stringBuffer.append(TEXT_27);
    stringBuffer.append(sRejectConnName);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(sRejectConnName);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    
		} //CONDITION_001004 STOP
	} //CONDITION_00100 STOP

    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(TEXT_34);
    return stringBuffer.toString();
  }
}
