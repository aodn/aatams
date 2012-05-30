package org.talend.designer.codegen.translators.data_quality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.utils.NodeUtil;

public class TSchemaComplianceCheckBeginJava
{
  protected static String nl;
  public static synchronized TSchemaComplianceCheckBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TSchemaComplianceCheckBeginJava result = new TSchemaComplianceCheckBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\ttry {" + NL + "\t\tif(";
  protected final String TEXT_2 = NL + "\t\t";
  protected final String TEXT_3 = ".";
  protected final String TEXT_4 = " != null  && (!\"\".equals(";
  protected final String TEXT_5 = ".";
  protected final String TEXT_6 = ")) ";
  protected final String TEXT_7 = NL + "\t\t";
  protected final String TEXT_8 = ".";
  protected final String TEXT_9 = " != null";
  protected final String TEXT_10 = NL + "\t\ttrue";
  protected final String TEXT_11 = NL + "\t\t) {";
  protected final String TEXT_12 = NL + "\t\t\tif(!(\"true\".equals(";
  protected final String TEXT_13 = ".";
  protected final String TEXT_14 = ") || \"false\".equals(";
  protected final String TEXT_15 = ".";
  protected final String TEXT_16 = "))){" + NL + "\t\t\t\tthrow new Exception(\"Wrong Boolean type!\");" + NL + "\t\t\t}";
  protected final String TEXT_17 = NL + "\t\t\tif(";
  protected final String TEXT_18 = ".";
  protected final String TEXT_19 = ".toCharArray().length != 1){" + NL + "\t\t\t\tthrow new Exception(\"Wrong Character type!\");" + NL + "\t\t\t}";
  protected final String TEXT_20 = NL + "\t\t\t";
  protected final String TEXT_21 = " tester_";
  protected final String TEXT_22 = " = new ";
  protected final String TEXT_23 = "(";
  protected final String TEXT_24 = ".";
  protected final String TEXT_25 = ");";
  protected final String TEXT_26 = NL + "\t\t\t";
  protected final String TEXT_27 = " tester_";
  protected final String TEXT_28 = " = new ";
  protected final String TEXT_29 = "();";
  protected final String TEXT_30 = NL + "\t\t\t";
  protected final String TEXT_31 = " tester_";
  protected final String TEXT_32 = " = ";
  protected final String TEXT_33 = ".valueOf(";
  protected final String TEXT_34 = ".";
  protected final String TEXT_35 = ");";
  protected final String TEXT_36 = NL + "\t\t}" + NL + "\t} catch(Exception e) {" + NL + "\t\tifPassedThrough = false;" + NL + "\t\terrorCodeThrough += 2;" + NL + "\t\terrorMessageThrough += \"|wrong type\";" + NL + "\t}";
  protected final String TEXT_37 = NL + "\tif (";
  protected final String TEXT_38 = ".";
  protected final String TEXT_39 = " != null){";
  protected final String TEXT_40 = NL + "\t\thandleBigdecimalPrecision((";
  protected final String TEXT_41 = ".";
  protected final String TEXT_42 = ").toPlainString(), ";
  protected final String TEXT_43 = ", ";
  protected final String TEXT_44 = ");";
  protected final String TEXT_45 = NL + "\t\thandleBigdecimalPrecision(String.valueOf(";
  protected final String TEXT_46 = ".";
  protected final String TEXT_47 = "), ";
  protected final String TEXT_48 = ", ";
  protected final String TEXT_49 = ");";
  protected final String TEXT_50 = NL + "\t\tifPassedThrough = ifPassedThrough?ifPassed:false;" + NL + "\t\terrorCodeThrough += errorCode;" + NL + "\t\terrorMessageThrough += errorMessage;" + NL + "\t}";
  protected final String TEXT_51 = NL + "\tif (";
  protected final String TEXT_52 = NL + "\t";
  protected final String TEXT_53 = ".";
  protected final String TEXT_54 = " != null  && (!\"\".equals(";
  protected final String TEXT_55 = ".";
  protected final String TEXT_56 = ")) ";
  protected final String TEXT_57 = " " + NL + "\t";
  protected final String TEXT_58 = ".";
  protected final String TEXT_59 = " != null";
  protected final String TEXT_60 = " " + NL + "\ttrue";
  protected final String TEXT_61 = NL + "\t) {";
  protected final String TEXT_62 = NL + "\t\tif( ";
  protected final String TEXT_63 = ".";
  protected final String TEXT_64 = ".length() > ";
  protected final String TEXT_65 = " )" + NL + "\t\t\t";
  protected final String TEXT_66 = ".";
  protected final String TEXT_67 = " = ";
  protected final String TEXT_68 = ".";
  protected final String TEXT_69 = ".substring(0, ";
  protected final String TEXT_70 = ");";
  protected final String TEXT_71 = NL + "\t\ttmpContentThrough = String.valueOf(";
  protected final String TEXT_72 = ".";
  protected final String TEXT_73 = ");";
  protected final String TEXT_74 = NL + "\t\ttmpContentThrough = ";
  protected final String TEXT_75 = ".";
  protected final String TEXT_76 = ".toString();";
  protected final String TEXT_77 = NL + "\t\tif (tmpContentThrough.length() > ";
  protected final String TEXT_78 = ")" + NL + "\t\t\t";
  protected final String TEXT_79 = ".";
  protected final String TEXT_80 = " = ";
  protected final String TEXT_81 = ".";
  protected final String TEXT_82 = ".substring(0, ";
  protected final String TEXT_83 = ");";
  protected final String TEXT_84 = NL + "\t\tif (";
  protected final String TEXT_85 = ".";
  protected final String TEXT_86 = ".length() > ";
  protected final String TEXT_87 = ") {" + NL + "\t\t\tifPassedThrough = false;" + NL + "\t\t\terrorCodeThrough += 8;" + NL + "\t\t\terrorMessageThrough += \"|exceed max length\";" + NL + "\t\t}";
  protected final String TEXT_88 = NL + "\t\ttmpContentThrough = String.valueOf(";
  protected final String TEXT_89 = ".";
  protected final String TEXT_90 = ");";
  protected final String TEXT_91 = NL + "\t\ttmpContentThrough = ";
  protected final String TEXT_92 = ".";
  protected final String TEXT_93 = ".toString();  ";
  protected final String TEXT_94 = NL + NL + "\t\tif (tmpContentThrough.length() > ";
  protected final String TEXT_95 = ") {" + NL + "\t\t\tifPassedThrough = false;" + NL + "\t\t\terrorCodeThrough += 8;" + NL + "\t\t\terrorMessageThrough += \"|exceed max length\";" + NL + "\t\t}";
  protected final String TEXT_96 = NL + "\t}";
  protected final String TEXT_97 = NL + "\tifPassedThrough = false;" + NL + "\terrorCodeThrough += 2;" + NL + "\terrorMessageThrough += \"|Date format not defined\";";
  protected final String TEXT_98 = NL + "\ttry{" + NL + "\t\tif (";
  protected final String TEXT_99 = NL + "\t\t";
  protected final String TEXT_100 = ".";
  protected final String TEXT_101 = " != null  && (!\"\".equals(";
  protected final String TEXT_102 = ".";
  protected final String TEXT_103 = ")) ";
  protected final String TEXT_104 = " " + NL + "\t\t";
  protected final String TEXT_105 = ".";
  protected final String TEXT_106 = " != null";
  protected final String TEXT_107 = " " + NL + "\t\ttrue";
  protected final String TEXT_108 = NL + "\t\t){";
  protected final String TEXT_109 = NL + "\t\t\tif (!TalendDate.isDate((";
  protected final String TEXT_110 = ".";
  protected final String TEXT_111 = ").toString(), ";
  protected final String TEXT_112 = "))" + NL + "\t\t\t\tthrow new IllegalArgumentException(\"Data format not matches\");";
  protected final String TEXT_113 = NL + "\t\t\tFastDateParser.getInstance(";
  protected final String TEXT_114 = ", false).parse(";
  protected final String TEXT_115 = ".";
  protected final String TEXT_116 = ");            ";
  protected final String TEXT_117 = NL + "\t\t}" + NL + "\t} catch(Exception e){" + NL + "\t\tifPassedThrough = false;" + NL + "\t\terrorCodeThrough += 2;" + NL + "\t\terrorMessageThrough += \"|wrong DATE pattern or wrong DATE data\";" + NL + "\t}";
  protected final String TEXT_118 = NL + "\tifPassedThrough = false;" + NL + "\terrorCodeThrough += 2;" + NL + "\terrorMessageThrough += \"|wrong DATE pattern or wrong DATE data\";";
  protected final String TEXT_119 = NL + "\tifPassedThrough = false;" + NL + "\terrorCodeThrough += 2;" + NL + "\terrorMessageThrough += \"|The TYPE of inputting data is error. (one of OBJECT, STRING, DATE)\";";
  protected final String TEXT_120 = NL + "\t// validate nullable (empty as null)" + NL + "\tif ((";
  protected final String TEXT_121 = ".";
  protected final String TEXT_122 = " == null) || (\"\".equals(";
  protected final String TEXT_123 = ".";
  protected final String TEXT_124 = "))) {";
  protected final String TEXT_125 = NL + "\t// validate nullable" + NL + "\tif (";
  protected final String TEXT_126 = ".";
  protected final String TEXT_127 = " == null) {";
  protected final String TEXT_128 = NL + "\t// validate nullable (empty as null)" + NL + "\tif ((";
  protected final String TEXT_129 = ".";
  protected final String TEXT_130 = " == null) || (\"\".equals(";
  protected final String TEXT_131 = ".";
  protected final String TEXT_132 = "))) {";
  protected final String TEXT_133 = NL + "\t// validate nullable (empty as null)" + NL + "\tif ((";
  protected final String TEXT_134 = ".";
  protected final String TEXT_135 = " == null) || (\"\".equals(";
  protected final String TEXT_136 = ".";
  protected final String TEXT_137 = "))) {";
  protected final String TEXT_138 = NL + "\t// validate nullable" + NL + "\tif (";
  protected final String TEXT_139 = ".";
  protected final String TEXT_140 = " == null) {";
  protected final String TEXT_141 = NL + "\t\tifPassedThrough = false;" + NL + "\t\terrorCodeThrough += 4;" + NL + "\t\terrorMessageThrough += \"|empty or null\";" + NL + "\t}";
  protected final String TEXT_142 = NL + "\tclass RowSetValueUtil_";
  protected final String TEXT_143 = " {" + NL + "" + NL + "\t\tboolean ifPassedThrough = true;" + NL + "\t\tint errorCodeThrough = 0;" + NL + "\t\tString errorMessageThrough = \"\";" + NL + "\t\tint resultErrorCodeThrough = 0;" + NL + "\t\tString resultErrorMessageThrough = \"\";" + NL + "\t\tString tmpContentThrough = null;" + NL + "" + NL + "\t\tboolean ifPassed = true;" + NL + "\t\tint errorCode = 0;" + NL + "\t\tString errorMessage = \"\";" + NL + "" + NL + "\t\tvoid handleBigdecimalPrecision(String data, int iPrecision, int maxLength){" + NL + "\t\t\t//number of digits before the decimal point(ignoring frontend zeroes)" + NL + "\t\t\tint len1 = 0;" + NL + "\t\t\tint len2 = 0;" + NL + "\t\t\tifPassed = true;" + NL + "\t\t\terrorCode = 0;" + NL + "\t\t\terrorMessage = \"\";" + NL + "\t\t\tif(data.startsWith(\"-\")){" + NL + "\t\t\t\tdata = data.substring(1);" + NL + "\t\t\t}" + NL + "\t\t\tdata = org.apache.commons.lang.StringUtils.stripStart(data, \"0\");" + NL + "" + NL + "\t\t\tif(data.indexOf(\".\") >= 0){" + NL + "\t\t\t\tlen1 = data.indexOf(\".\");" + NL + "\t\t\t\tdata = org.apache.commons.lang.StringUtils.stripEnd(data, \"0\");" + NL + "\t\t\t\tlen2 = data.length() - (len1 + 1);" + NL + "\t\t\t}else{" + NL + "\t\t\t\tlen1 = data.length();" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tif (iPrecision < len2) {" + NL + "\t\t\t\tifPassed = false;" + NL + "\t\t\t\terrorCode += 8;" + NL + "\t\t\t\terrorMessage += \"|precision Non-matches\";" + NL + "\t\t\t} else if (maxLength < len1 + iPrecision) {" + NL + "\t\t\t\tifPassed = false;" + NL + "\t\t\t\terrorCode += 8;" + NL + "\t\t\t\terrorMessage += \"|invalid Length setting is unsuitable for Precision\";" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "" + NL + "\t\tint handleErrorCode(int errorCode, int resultErrorCode){" + NL + "\t\t\tif (errorCode > 0) {" + NL + "\t\t\t\tif (resultErrorCode > 0) {" + NL + "\t\t\t\t\tresultErrorCode = 16;" + NL + "\t\t\t\t} else {" + NL + "\t\t\t\t\tresultErrorCode = errorCode;" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t\treturn resultErrorCode;" + NL + "\t\t}" + NL + "" + NL + "\t\tString handleErrorMessage(String errorMessage, String resultErrorMessage, String columnLabel){" + NL + "\t\t\tif (errorMessage.length() > 0) {" + NL + "\t\t\t\tif (resultErrorMessage.length() > 0) {" + NL + "\t\t\t\t\tresultErrorMessage += \";\"+ errorMessage.replaceFirst(\"\\\\|\", columnLabel);" + NL + "\t\t\t\t} else {" + NL + "\t\t\t\t\tresultErrorMessage = errorMessage.replaceFirst(\"\\\\|\", columnLabel);" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t\treturn resultErrorMessage;" + NL + "\t\t}" + NL + "" + NL + "\t\tvoid reset(){" + NL + "\t\t\tifPassedThrough = true;" + NL + "\t\t\terrorCodeThrough = 0;" + NL + "\t\t\terrorMessageThrough = \"\";" + NL + "\t\t\tresultErrorCodeThrough = 0;" + NL + "\t\t\tresultErrorMessageThrough = \"\";" + NL + "\t\t\ttmpContentThrough = null;" + NL + "" + NL + "\t\t\tifPassed = true;" + NL + "\t\t\terrorCode = 0;" + NL + "\t\t\terrorMessage = \"\";" + NL + "\t\t}" + NL;
  protected final String TEXT_144 = NL + "\t\tvoid setRowValue_";
  protected final String TEXT_145 = "(";
  protected final String TEXT_146 = "Struct ";
  protected final String TEXT_147 = ") {";
  protected final String TEXT_148 = NL + "\t\t\tresultErrorCodeThrough = handleErrorCode(errorCodeThrough,resultErrorCodeThrough);" + NL + "\t\t\terrorCodeThrough = 0;" + NL + "\t\t\tresultErrorMessageThrough = handleErrorMessage(errorMessageThrough,resultErrorMessageThrough,\"";
  protected final String TEXT_149 = ":\");" + NL + "\t\t\terrorMessageThrough = \"\";";
  protected final String TEXT_150 = NL + "\t\t}";
  protected final String TEXT_151 = NL + "\t\t}";
  protected final String TEXT_152 = NL + "\t}" + NL + "\tRowSetValueUtil_";
  protected final String TEXT_153 = " rsvUtil_";
  protected final String TEXT_154 = " = new RowSetValueUtil_";
  protected final String TEXT_155 = "();";
  protected final String TEXT_156 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	final INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();

	/*in shema:*/
	List<? extends IConnection> listInConns = node.getIncomingConnections();
	String sInConnName = null;
	IConnection inConn = null;
	List<IMetadataColumn> listInColumns = null;

	if (listInConns != null && listInConns.size() > 0) {
		IConnection inConnTemp = listInConns.get(0);
		sInConnName = inConnTemp.getName();
		if(inConnTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
			inConn = inConnTemp;
			listInColumns = inConnTemp.getMetadataTable().getListColumns();
		}
	}

	String inConnName = null;
	
	class FindConnStartConn{
		IConnection findStartConn(IConnection conn){
			INode node = conn.getSource();
			if(node.isSubProcessStart() || !(NodeUtil.isDataAutoPropagated(node))){
				return conn;
			}
			List<? extends IConnection> listInConns = node.getIncomingConnections();
			IConnection inConnTemp = null;
			if(listInConns != null && listInConns.size() > 0) {
				inConnTemp = listInConns.get(0);
				if(inConnTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)){
					return findStartConn(inConnTemp);
				}
			}
			return null;
		}
	}

	if(inConn != null){
		FindConnStartConn finder = new FindConnStartConn();
		IConnection startConn = finder.findStartConn(inConn);
		if(startConn!=null){
			inConnName = startConn.getName();
		}
	}
	
	/* get the schema of itself (maybe no output flow)*/
	List<IMetadataColumn> listColumsToTest = node.getMetadataList().get(0).getListColumns();

	String anotherChecked = ElementParameterParser.getValue(node, "__CHECK_ANOTHER__");
	String checkAll = ElementParameterParser.getValue(node, "__CHECK_ALL__");
	final boolean bIsTrim = "true".equals(ElementParameterParser.getValue(node, "__SUB_STRING__"));
	final boolean useFasteDateChecker = "true".equals(ElementParameterParser.getValue(node, "__FAST_DATE_CHECK__"));
	final boolean emptyIsNull = "true".equals(ElementParameterParser.getValue(node, "__EMPTY_IS_NULL__"));
	final boolean allEmptyAreNull = "true".equals(ElementParameterParser.getValue(node, "__ALL_EMPTY_ARE_NULL__"));

	class SchemaChecker { //CLASS SCHEMACHECKER START
		boolean anotherChecked = "true".equals(ElementParameterParser.getValue(node, "__CHECK_ANOTHER__"));

		public void  testDataType(boolean _bNullable, String _sInConnName, IMetadataColumn metadataColumn, String typeSelected, String cid) { //METHOD_TESTDATATYPE START
			JavaType javaType = JavaTypesManager.getJavaTypeFromId(metadataColumn.getTalendType());
			boolean isPrimitive = JavaTypesManager.isJavaPrimitiveType( javaType, metadataColumn.isNullable());
			String colName = metadataColumn.getLabel();

			if (javaType == JavaTypesManager.OBJECT || javaType == JavaTypesManager.STRING) { //CONDITION_00100 START

    stringBuffer.append(TEXT_1);
    
				if (_bNullable){ //CONDITION_00110 START

    stringBuffer.append(TEXT_2);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_6);
    
				}else if(!isPrimitive){ //CONDITION_00110 ELSE IF

    stringBuffer.append(TEXT_7);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_9);
    
				}else{ //CONDITION_00110 ELSE

    stringBuffer.append(TEXT_10);
    
				} //CONDITION_00110 STOP

    stringBuffer.append(TEXT_11);
    
				if(typeSelected.equals("Boolean") ) { //CONDITION_00120 START

    stringBuffer.append(TEXT_12);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_16);
    
				} else if(typeSelected.equals("Character")) { //CONDITION_00120 ELSE IF

    stringBuffer.append(TEXT_17);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_19);
    
				} else if(typeSelected.equals("BigDecimal")) { //CONDITION_00120 ELSE IF

    stringBuffer.append(TEXT_20);
    stringBuffer.append(typeSelected);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(typeSelected);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_25);
    
				} else if(typeSelected.equals("Object")){ //CONDITION_00120 ELSE IF

    stringBuffer.append(TEXT_26);
    stringBuffer.append(typeSelected);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(typeSelected);
    stringBuffer.append(TEXT_29);
    
				} else { //CONDITION_00120 ELSE

    stringBuffer.append(TEXT_30);
    stringBuffer.append(typeSelected);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(typeSelected);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_35);
    
				} //CONDITION_00120 STOP

    stringBuffer.append(TEXT_36);
    
			} //CONDITION_00100 STOP
		} //METHOD_TESTDATATYPE STOP

		public void testPrecision(int _maxLength, int iPrecision, String _sInConnName, IMetadataColumn metadataColumn, String typeSelected, String cid) { //METHOD_TESTPRECISION START
			JavaType javaType = JavaTypesManager.getJavaTypeFromId(metadataColumn.getTalendType());
			String colName = metadataColumn.getLabel();
			boolean needCheck = false;
			if(anotherChecked) {
				if("BigDecimal".equalsIgnoreCase(typeSelected)) {
					needCheck = true;
				}
			} else if (javaType == JavaTypesManager.BIGDECIMAL) {
				/* NULLable, in case input value is Null, do nothing... 
				Non-NULLable, 
					(1) in case input value is Non-null, go into...; 
					(2) in case input value is Null, do nothing and warning by NULL-CHECKER.
				*/
				/*
					if precision value is not empty or Null, checking "Precision" at first, if passed then checking "Length"
				*/
				needCheck = true;
			}
			if(needCheck) { //CONDITION_00130 START

    stringBuffer.append(TEXT_37);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_39);
    
				if(javaType == JavaTypesManager.BIGDECIMAL) { //CONDITION_00131 START

    stringBuffer.append(TEXT_40);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(iPrecision);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(_maxLength);
    stringBuffer.append(TEXT_44);
    
				} else {  //CONDITION_00131 ELSE

    stringBuffer.append(TEXT_45);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_46);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(iPrecision);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(_maxLength);
    stringBuffer.append(TEXT_49);
    
				}  //CONDITION_00131 STOP

    stringBuffer.append(TEXT_50);
    
			} //CONDITION_00130 STOP
		} //METHOD_TESTPRECISION STOP

		public void testDataLength(boolean _bNullable, String _sInConnName, IMetadataColumn metadataColumn, int maxLength, String cid) { //METHOD_TESTDATALENGTH START
			JavaType javaType = JavaTypesManager.getJavaTypeFromId(metadataColumn.getTalendType());
			boolean isPrimitive = JavaTypesManager.isJavaPrimitiveType(javaType, metadataColumn.isNullable());
			boolean bIsStringType = (javaType == JavaTypesManager.STRING), bIsIntegerType = (javaType == JavaTypesManager.INTEGER);
			String colName = metadataColumn.getLabel();

			if (maxLength > 0 && ( bIsStringType || bIsIntegerType )){ //CONDITION_00140 START

    stringBuffer.append(TEXT_51);
    
				if (_bNullable){ //CONDITION_00141 START

    stringBuffer.append(TEXT_52);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_56);
    
				}else if (!isPrimitive){ //CONDITION_00141 ELSE IF

    stringBuffer.append(TEXT_57);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_59);
    
				}else { //CONDITION_00141 ELSE

    stringBuffer.append(TEXT_60);
    
				} //CONDITION_00141 STOP

    stringBuffer.append(TEXT_61);
    
				if ( bIsTrim ){ //CONDITION_00142 START
					if (bIsStringType) { //CONDITION_001421 START

    stringBuffer.append(TEXT_62);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(maxLength);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_66);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_67);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_68);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_69);
    stringBuffer.append(maxLength);
    stringBuffer.append(TEXT_70);
    
					} else if ( bIsIntegerType ){//CONDITION_001421 ELSE IF
						String generatedType = JavaTypesManager.getTypeToGenerate(metadataColumn.getTalendType(), metadataColumn.isNullable());
						if ("int".equals(generatedType)) { //CONDITION_0014211 START

    stringBuffer.append(TEXT_71);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_73);
    
						} else{ //CONDITION_0014211 ELSE

    stringBuffer.append(TEXT_74);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_75);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_76);
    
						} //CONDITION_0014211 STOP

    stringBuffer.append(TEXT_77);
    stringBuffer.append(maxLength);
    stringBuffer.append(TEXT_78);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(maxLength);
    stringBuffer.append(TEXT_83);
    
					} //CONDITION_001421 STOP
				} else{ //CONDITION_00142 ELSE
					if (bIsStringType) { //CONDITION_001422 START

    stringBuffer.append(TEXT_84);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_85);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_86);
    stringBuffer.append(maxLength);
    stringBuffer.append(TEXT_87);
    
					} else if (bIsIntegerType) { //CONDITION_001422 ELSE IF
						String generatedType = JavaTypesManager.getTypeToGenerate(metadataColumn.getTalendType(), metadataColumn.isNullable());
						if ("int".equals(generatedType)) { //CONDITION_0014221 START

    stringBuffer.append(TEXT_88);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_90);
    
						} else { //CONDITION_0014221 ELSE

    stringBuffer.append(TEXT_91);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_93);
    
						} //CONDITION_0014221 STOP

    stringBuffer.append(TEXT_94);
    stringBuffer.append(maxLength);
    stringBuffer.append(TEXT_95);
    
					}//CONDITION_001422 STOP
				} //CONDITION_00142 STOP

    stringBuffer.append(TEXT_96);
     
			} //CONDITION_00140 STOP
		} //METHOD_TESTDATALENGTH STOP

		public void testDate(boolean _bNullable, String _sInConnName, IMetadataColumn metadataColumn, String pattern, String cid) { //METHOD_TESTDATE START
			JavaType javaType = JavaTypesManager.getJavaTypeFromId(metadataColumn.getTalendType());
			boolean isPrimitive = JavaTypesManager.isJavaPrimitiveType( javaType, metadataColumn.isNullable());
			String colName = metadataColumn.getLabel();

			if ("".equals(pattern)){ //CONDITION_00150 START

    stringBuffer.append(TEXT_97);
    
			} else { //CONDITION_00150 ELSE
				if (javaType == JavaTypesManager.OBJECT || javaType == JavaTypesManager.STRING) { //CONDITION_00151 START

    stringBuffer.append(TEXT_98);
    
					if (_bNullable){ //CONDITION_001511 START

    stringBuffer.append(TEXT_99);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_100);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_101);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_102);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_103);
    
					}else if (!isPrimitive){ //CONDITION_001511 ELSE IF

    stringBuffer.append(TEXT_104);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_105);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_106);
    
					}else { //CONDITION_001511 ELSE

    stringBuffer.append(TEXT_107);
    
					} //CONDITION_001511 STOP

    stringBuffer.append(TEXT_108);
    
					if (!useFasteDateChecker) { //CONDITION_001512 START

    stringBuffer.append(TEXT_109);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_110);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_111);
    stringBuffer.append(pattern);
    stringBuffer.append(TEXT_112);
    
					} else { //CONDITION_001512 ELSE

    stringBuffer.append(TEXT_113);
    stringBuffer.append(pattern);
    stringBuffer.append(TEXT_114);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_115);
    stringBuffer.append(colName);
    stringBuffer.append(TEXT_116);
    
					} //CONDITION_001512 STOP

    stringBuffer.append(TEXT_117);
    
				// date type need check also (some inputting data not legal, beacause original data is not suite with pattern and has be converted)
				} else if (javaType == JavaTypesManager.DATE){ //CONDITION_00151 ELSE IF
					if (!metadataColumn.getPattern().equals(pattern)){ //CONDITION_001513 START

    stringBuffer.append(TEXT_118);
    
					} //CONDITION_001513 STOP
				} else{ //CONDITION_00151 ELSE

    stringBuffer.append(TEXT_119);
    
				} //CONDITION_00151 STOP
			} //CONDITION_00150 STOP
		} //METHOD_TESTDATE STOP

		public void testNull(String _sInConnName, IMetadataColumn metadataColumn, String cid, List<Map<String, String>> list){ //METHOD_TESTNULL START
			List<String> listEmptyAsNull = new ArrayList<String>();
			for(Map<String, String> map : list){
				if("true".equals(map.get("EMPTY_NULL"))){
					listEmptyAsNull.add(map.get("SCHEMA_COLUMN"));
				}
			}

			boolean isPrimitive = JavaTypesManager.isJavaPrimitiveType(metadataColumn.getTalendType(), metadataColumn.isNullable());
			if (!isPrimitive){ //CONDITION_00160 START
				if(emptyIsNull && !allEmptyAreNull){ //CONDITION_001601 START - for the migration task
					if(listEmptyAsNull.contains(metadataColumn.getLabel())){ //CONDITION_0016011 START

    stringBuffer.append(TEXT_120);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_121);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_122);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_123);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_124);
    
					}else{ //CONDITION_0016011 ELSE

    stringBuffer.append(TEXT_125);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_126);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_127);
    
					} //CONDITION_0016011 STOP
				}else{ //CONDITION_001601 ELSE
					if(allEmptyAreNull){ //CONDITION_0016012 START

    stringBuffer.append(TEXT_128);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_129);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_130);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_131);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_132);
    
					}else if(listEmptyAsNull.contains(metadataColumn.getLabel())){ //CONDITION_0016012 ELSE IF

    stringBuffer.append(TEXT_133);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_134);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_135);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_136);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_137);
    
					}else{ //CONDITION_0016012 ELSE

    stringBuffer.append(TEXT_138);
    stringBuffer.append(_sInConnName);
    stringBuffer.append(TEXT_139);
    stringBuffer.append(metadataColumn.getLabel());
    stringBuffer.append(TEXT_140);
    
					} //CONDITION_0016012 STOP
				} //CONDITION_001601 STOP

    stringBuffer.append(TEXT_141);
    
			} //CONDITION_00160 STOP
		} //METHOD_TESTNULL STOP
	} //CLASS SCHEMACHECKER STOP

	SchemaChecker checker = new SchemaChecker();    
	List<Map<String, String>> listCheckedColumns = (List<Map<String, String>>)ElementParameterParser.getObjectValue(node, "__CHECKCOLS__");
	boolean bNeedReferSchema = false;

	if ("true".equals(anotherChecked)){
		if (node.getMetadataFromConnector("OTHER") != null)
			listColumsToTest = node.getMetadataFromConnector("OTHER").getListColumns();
	} else if ("true".equals(checkAll)){
		;
	} else{
		bNeedReferSchema = true;
	}


    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_143);
    
	for (IMetadataColumn inColumn : listInColumns) { //LOOP_00100 START
		int iInColIndex = listInColumns.indexOf(inColumn);
		if(iInColIndex % 100 == 0){ //CONDITION_00170 START

    stringBuffer.append(TEXT_144);
    stringBuffer.append((iInColIndex/100) );
    stringBuffer.append(TEXT_145);
    stringBuffer.append(inConnName!=null?inConnName:sInConnName );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(sInConnName );
    stringBuffer.append(TEXT_147);
    
		} //CONDITION_00170 STOP
		// when using another schema, it's size may less than listInColumns
		if (iInColIndex >= listColumsToTest.size()){
			break;
		}

		Object pre_iPrecision = null;
		String sInColumnName = inColumn.getLabel(), sTestColName = null, sTestColType = null, sTestColPattern = null;
		boolean bNullable = true, bMaxLenLimited = true;
		/* use setting of tSchemaComplianceCheck schema (it is synchronize with inputting schema, but length value can be different) */
		Object pre_maxLength = listColumsToTest.get(iInColIndex).getLength();
		int maxLength = (pre_maxLength == null) ? 0 : Integer.parseInt(pre_maxLength.toString());
		IMetadataColumn schemaColumn = null;

		if (bNeedReferSchema) {
			Map<String, String> checkedColumn = listCheckedColumns.get(iInColIndex);
			sTestColName = checkedColumn.get("SCHEMA_COLUMN");
			sTestColType = checkedColumn.get("SELECTED_TYPE");
			sTestColPattern = checkedColumn.get("DATEPATTERN");
			bNullable = "true".equals(checkedColumn.get("NULLABLE"));
			bMaxLenLimited = "true".equals(checkedColumn.get("MAX_LENGTH"));
		} else{
			schemaColumn = listColumsToTest.get(iInColIndex);
			sTestColName = schemaColumn.getLabel();
			sTestColType = JavaTypesManager.getTypeToGenerate(schemaColumn.getTalendType(), true);
			sTestColPattern = schemaColumn.getPattern();
			bNullable = schemaColumn.isNullable();
			pre_iPrecision = schemaColumn.getPrecision();
		}

		// NULL checking
		if (!bNullable){
			List<Map<String, String>> list = (List<Map<String, String>>)ElementParameterParser.getObjectValue(node, "__EMPTY_NULL_TABLE__");
			checker.testNull(sInConnName, inColumn, cid, list);
		}

		// type checking
		if (sTestColType != null){
			if (sTestColType.indexOf("Date") >= 0){
				checker.testDate(bNullable, sInConnName, inColumn, sTestColPattern, cid); 
			} else{
				checker.testDataType(bNullable, sInConnName, inColumn, sTestColType, cid);
			}
		}

		// length checking
		if (bMaxLenLimited){
			checker.testDataLength(bNullable, sInConnName, "true".equals(anotherChecked)?schemaColumn:inColumn, maxLength, cid);
		}

		// precision checking
		if (pre_iPrecision != null){
			checker.testPrecision(maxLength, Integer.parseInt(pre_iPrecision.toString()), sInConnName, inColumn, sTestColType, cid);
		}

    stringBuffer.append(TEXT_148);
    stringBuffer.append(inColumn.getLabel());
    stringBuffer.append(TEXT_149);
    
		if((iInColIndex + 1) % 100 == 0){ //CONDITION_00171 START

    stringBuffer.append(TEXT_150);
    
		} //CONDITION_00171 STOP
	} //LOOP_00100 STOP
	if(listInColumns.size() > 0 && listInColumns.size() % 100 > 0){ //CONDITION_00180 START

    stringBuffer.append(TEXT_151);
    
	} //CONDITION_00180 STOP

    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(TEXT_156);
    return stringBuffer.toString();
  }
}
