package org.talend.designer.codegen.translators.internet.ftp;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.List;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;

public class TFTPFilePropertiesBeginJava
{
  protected static String nl;
  public static synchronized TFTPFilePropertiesBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFTPFilePropertiesBeginJava result = new TFTPFilePropertiesBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "  java.util.Properties props_";
  protected final String TEXT_2 = " = System.getProperties();" + NL + "  props_";
  protected final String TEXT_3 = ".put(\"socksProxyPort\", ";
  protected final String TEXT_4 = ");" + NL + "  props_";
  protected final String TEXT_5 = ".put(\"socksProxyHost\", ";
  protected final String TEXT_6 = ");" + NL + "  props_";
  protected final String TEXT_7 = ".put(\"java.net.socks.username\", ";
  protected final String TEXT_8 = ");" + NL + "  props_";
  protected final String TEXT_9 = ".put(\"java.net.socks.password\", ";
  protected final String TEXT_10 = ");    ";
  protected final String TEXT_11 = NL + "    class MyUserInfo_";
  protected final String TEXT_12 = " implements com.jcraft.jsch.UserInfo, com.jcraft.jsch.UIKeyboardInteractive {" + NL + "      String passphrase_";
  protected final String TEXT_13 = " = ";
  protected final String TEXT_14 = ";" + NL + "      public String getPassphrase() { return passphrase_";
  protected final String TEXT_15 = "; }" + NL + "      public String getPassword() { return null; } " + NL + "      public boolean promptPassword(String arg0) { return true; } " + NL + "      public boolean promptPassphrase(String arg0) { return true; } " + NL + "      public boolean promptYesNo(String arg0) { return true; } " + NL + "      public void showMessage(String arg0) { } " + NL + "" + NL + "      public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt," + NL + "        boolean[] echo) {" + NL + "        String[] password_";
  protected final String TEXT_16 = " = {";
  protected final String TEXT_17 = "};" + NL + "        return password_";
  protected final String TEXT_18 = ";" + NL + "      }" + NL + "    }; " + NL + "    final com.jcraft.jsch.UserInfo defaultUserInfo_";
  protected final String TEXT_19 = " = new MyUserInfo_";
  protected final String TEXT_20 = "();" + NL + "    com.jcraft.jsch.JSch jsch_";
  protected final String TEXT_21 = "=new com.jcraft.jsch.JSch(); " + NL;
  protected final String TEXT_22 = NL + "      jsch_";
  protected final String TEXT_23 = ".addIdentity(";
  protected final String TEXT_24 = ", defaultUserInfo_";
  protected final String TEXT_25 = ".getPassphrase());";
  protected final String TEXT_26 = NL + "    com.jcraft.jsch.Session session_";
  protected final String TEXT_27 = "=jsch_";
  protected final String TEXT_28 = ".getSession(";
  protected final String TEXT_29 = ", ";
  protected final String TEXT_30 = ", ";
  protected final String TEXT_31 = ");" + NL;
  protected final String TEXT_32 = " " + NL + "      session_";
  protected final String TEXT_33 = ".setPassword(";
  protected final String TEXT_34 = "); ";
  protected final String TEXT_35 = NL + "    session_";
  protected final String TEXT_36 = ".setUserInfo(defaultUserInfo_";
  protected final String TEXT_37 = "); " + NL + "    session_";
  protected final String TEXT_38 = ".connect();" + NL + "    com.jcraft.jsch. Channel channel_";
  protected final String TEXT_39 = "=session_";
  protected final String TEXT_40 = ".openChannel(\"sftp\");" + NL + "    channel_";
  protected final String TEXT_41 = ".connect();" + NL + "    com.jcraft.jsch.ChannelSftp c_";
  protected final String TEXT_42 = "=(com.jcraft.jsch.ChannelSftp)channel_";
  protected final String TEXT_43 = ";" + NL + "    c_";
  protected final String TEXT_44 = ".setFilenameEncoding(";
  protected final String TEXT_45 = ");";
  protected final String TEXT_46 = "    " + NL + "    com.jcraft.jsch.ChannelSftp c_";
  protected final String TEXT_47 = " = (com.jcraft.jsch.ChannelSftp)globalMap.get(\"";
  protected final String TEXT_48 = "\");" + NL + "    if(c_";
  protected final String TEXT_49 = ".getHome()!=null && !c_";
  protected final String TEXT_50 = ".getHome().equals(c_";
  protected final String TEXT_51 = ".pwd())){" + NL + "  \t\tc_";
  protected final String TEXT_52 = ".cd(c_";
  protected final String TEXT_53 = ".getHome());" + NL + "  \t}";
  protected final String TEXT_54 = " " + NL + "  String remoteDir_";
  protected final String TEXT_55 = " = ";
  protected final String TEXT_56 = ".replaceAll(\"\\\\\\\\\", \"/\");";
  protected final String TEXT_57 = NL + "  ";
  protected final String TEXT_58 = " = new ";
  protected final String TEXT_59 = "Struct();" + NL + "  String remoteFile_";
  protected final String TEXT_60 = " = ";
  protected final String TEXT_61 = " + \"/\" + ";
  protected final String TEXT_62 = ";" + NL + "  com.jcraft.jsch.SftpATTRS lstat_";
  protected final String TEXT_63 = " = c_";
  protected final String TEXT_64 = ".lstat(remoteFile_";
  protected final String TEXT_65 = ");" + NL + "" + NL + "  if (lstat_";
  protected final String TEXT_66 = " != null) {";
  protected final String TEXT_67 = NL + "    ";
  protected final String TEXT_68 = ".abs_path = (remoteFile_";
  protected final String TEXT_69 = ").replaceAll(\"//\", \"/\");";
  protected final String TEXT_70 = NL + "    ";
  protected final String TEXT_71 = ".dirname = ";
  protected final String TEXT_72 = ";";
  protected final String TEXT_73 = NL + "    ";
  protected final String TEXT_74 = ".basename = ";
  protected final String TEXT_75 = ";";
  protected final String TEXT_76 = NL + "    ";
  protected final String TEXT_77 = ".size = lstat_";
  protected final String TEXT_78 = ".getSize();";
  protected final String TEXT_79 = NL + "    ";
  protected final String TEXT_80 = ".mtime = (long)lstat_";
  protected final String TEXT_81 = ".getMTime();";
  protected final String TEXT_82 = NL + "    ";
  protected final String TEXT_83 = ".mtime_string = lstat_";
  protected final String TEXT_84 = ".getMtimeString();" + NL;
  protected final String TEXT_85 = NL + "      // Calculation of the Message Digest MD5" + NL + "      java.io.InputStream is_";
  protected final String TEXT_86 = " = c_";
  protected final String TEXT_87 = ".get(remoteFile_";
  protected final String TEXT_88 = ");" + NL + "      byte[] buffer_";
  protected final String TEXT_89 = " = new byte[8192];" + NL + "      int read_";
  protected final String TEXT_90 = " = 0;" + NL + "      java.security.MessageDigest dgs_";
  protected final String TEXT_91 = " = java.security.MessageDigest.getInstance(\"MD5\");" + NL + "" + NL + "      while ( (read_";
  protected final String TEXT_92 = " = is_";
  protected final String TEXT_93 = ".read(buffer_";
  protected final String TEXT_94 = ")) > 0) {" + NL + "        dgs_";
  protected final String TEXT_95 = ".update(buffer_";
  protected final String TEXT_96 = ", 0, read_";
  protected final String TEXT_97 = ");" + NL + "      }";
  protected final String TEXT_98 = NL + "      ";
  protected final String TEXT_99 = ".md5 =new java.math.BigInteger(1, dgs_";
  protected final String TEXT_100 = ".digest()).toString(16);" + NL + "      is_";
  protected final String TEXT_101 = ".close();";
  protected final String TEXT_102 = NL + "  }  ";
  protected final String TEXT_103 = NL + "  com.enterprisedt.net.ftp.FTPClient ftp_";
  protected final String TEXT_104 = " =null;" + NL;
  protected final String TEXT_105 = NL + "    ftp_";
  protected final String TEXT_106 = " = (com.enterprisedt.net.ftp.FTPClient)globalMap.get(\"";
  protected final String TEXT_107 = "\");";
  protected final String TEXT_108 = NL + "    ftp_";
  protected final String TEXT_109 = " = new com.enterprisedt.net.ftp.FTPClient();" + NL + "    ftp_";
  protected final String TEXT_110 = ".setRemoteHost(";
  protected final String TEXT_111 = ");" + NL + "    ftp_";
  protected final String TEXT_112 = ".setRemotePort(";
  protected final String TEXT_113 = ");" + NL;
  protected final String TEXT_114 = NL + "      ftp_";
  protected final String TEXT_115 = ".setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.ACTIVE);";
  protected final String TEXT_116 = NL + "      ftp_";
  protected final String TEXT_117 = ".setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.PASV);";
  protected final String TEXT_118 = NL + "    ftp_";
  protected final String TEXT_119 = ".setControlEncoding(";
  protected final String TEXT_120 = ");" + NL + "    ftp_";
  protected final String TEXT_121 = ".connect();" + NL + "    ftp_";
  protected final String TEXT_122 = ".login(";
  protected final String TEXT_123 = ", ";
  protected final String TEXT_124 = ");";
  protected final String TEXT_125 = NL + "  \tftp_";
  protected final String TEXT_126 = ".setType(com.enterprisedt.net.ftp.FTPTransferType.BINARY);";
  protected final String TEXT_127 = NL + "  String remoteDir_";
  protected final String TEXT_128 = " = ";
  protected final String TEXT_129 = ".replaceAll(\"\\\\\\\\\", \"/\");";
  protected final String TEXT_130 = NL + "  ";
  protected final String TEXT_131 = " = new ";
  protected final String TEXT_132 = "Struct();" + NL + "  String remoteFile_";
  protected final String TEXT_133 = " = ";
  protected final String TEXT_134 = " + \"/\" + ";
  protected final String TEXT_135 = ";" + NL + "" + NL + "  if (ftp_";
  protected final String TEXT_136 = ".exists(remoteFile_";
  protected final String TEXT_137 = ")) {";
  protected final String TEXT_138 = NL + "    ";
  protected final String TEXT_139 = ".abs_path = (remoteFile_";
  protected final String TEXT_140 = ").replaceAll(\"//\", \"/\");";
  protected final String TEXT_141 = NL + "    ";
  protected final String TEXT_142 = ".dirname = ";
  protected final String TEXT_143 = ";";
  protected final String TEXT_144 = NL + "    ";
  protected final String TEXT_145 = ".basename = ";
  protected final String TEXT_146 = ";";
  protected final String TEXT_147 = NL + "    ";
  protected final String TEXT_148 = ".size = ftp_";
  protected final String TEXT_149 = ".size(remoteFile_";
  protected final String TEXT_150 = ");";
  protected final String TEXT_151 = NL + "    ";
  protected final String TEXT_152 = ".mtime = ftp_";
  protected final String TEXT_153 = ".modtime(remoteFile_";
  protected final String TEXT_154 = ").getTime();";
  protected final String TEXT_155 = NL + "    ";
  protected final String TEXT_156 = ".mtime_string =ftp_";
  protected final String TEXT_157 = ".modtime(remoteFile_";
  protected final String TEXT_158 = ").toString();" + NL;
  protected final String TEXT_159 = NL + "      // Calculation of the Message Digest MD5" + NL + "      java.security.MessageDigest dgs_";
  protected final String TEXT_160 = " = java.security.MessageDigest.getInstance(\"MD5\");" + NL + "      dgs_";
  protected final String TEXT_161 = ".update(ftp_";
  protected final String TEXT_162 = ".get(remoteFile_";
  protected final String TEXT_163 = "));";
  protected final String TEXT_164 = NL + "      ";
  protected final String TEXT_165 = ".md5 =new java.math.BigInteger(1, dgs_";
  protected final String TEXT_166 = ".digest()).toString(16);";
  protected final String TEXT_167 = NL + "  }";
  protected final String TEXT_168 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
String host = ElementParameterParser.getValue(node, "__HOST__");
String port = ElementParameterParser.getValue(node, "__PORT__");
String username = ElementParameterParser.getValue(node, "__USERNAME__");
String password = ElementParameterParser.getValue(node, "__PASSWORD__");
String filename = ElementParameterParser.getValue(node, "__FILENAME__");
String remoteDir = ElementParameterParser.getValue(node, "__REMOTEDIR__");
String encoding = ElementParameterParser.getValue(node, "__ENCODING__");
String user = ElementParameterParser.getValue(node, "__USERNAME__");
String pass = ElementParameterParser.getValue(node, "__PASSWORD__");
String authMethod = ElementParameterParser.getValue(node, "__AUTH_METHOD__");
String privateKey = ElementParameterParser.getValue(node, "__PRIVATEKEY__");
String passPhrase = ElementParameterParser.getValue(node, "__PASSPHRASE__");  
boolean useProxy = ("true").equals(ElementParameterParser.getValue(node, "__USE_PROXY__"));
String proxyHost = ElementParameterParser.getValue(node, "__PROXY_HOST__");
String proxyPort = ElementParameterParser.getValue(node, "__PROXY_PORT__");
String proxyUser = ElementParameterParser.getValue(node, "__PROXY_USERNAME__");
String proxyPassword = ElementParameterParser.getValue(node, "__PROXY_PASSWORD__");
String connectMode = ElementParameterParser.getValue(node, "__CONNECT_MODE__");
String connection = ElementParameterParser.getValue(node, "__CONNECTION__");
String conn= "conn_" + connection;
String useExistingConn = ElementParameterParser.getValue(node, "__USE_EXISTING_CONNECTION__");
String mode = ElementParameterParser.getValue(node, "__MODE__");
boolean MD5 = new Boolean(ElementParameterParser.getValue(node, "__MD5__"));
boolean sftp = false;

if (("true").equals(useExistingConn)) {
  List<? extends INode> nodeList = node.getProcess().getGeneratingNodes();

  for (INode n : nodeList) {
    if (n.getUniqueName().equals(connection)) {
      sftp = ("true").equals(ElementParameterParser.getValue(n, "__SFTP__"));
    }
  }
} else {
  sftp = ("true").equals(ElementParameterParser.getValue(node, "__SFTP__"));
}

if (useProxy) {

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(proxyPort );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(proxyHost );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(proxyUser );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(proxyPassword );
    stringBuffer.append(TEXT_10);
    
}
String outputConnName = null;
List< ? extends IConnection> conns = node.getOutgoingSortedConnections();

if (conns != null) {
  if (conns.size() > 0) {
    for (int i = 0; i < conns.size(); i++) {
      IConnection connTemp = conns.get(i);
      if (connTemp.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
        outputConnName = connTemp.getName();
        break;
      }
    }
  }
}

if (outputConnName == null) {
  return "";
}

//The following part support the socks proxy for FTP and SFTP (Socks V4 or V5, they are all OK). 
//And it can not work with the FTP proxy directly, only support the socks proxy.
if (sftp) {

  if (("false").equals(useExistingConn)) {
  
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(passPhrase );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(pass);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    if (("PUBLICKEY").equals(authMethod)) {
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(privateKey );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    }
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(user);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(host);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(port);
    stringBuffer.append(TEXT_31);
    if (("PASSWORD").equals(authMethod)) {
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(pass);
    stringBuffer.append(TEXT_34);
    }
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_45);
    } else {
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(conn );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_53);
    }
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(remoteDir );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(TEXT_57);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(remoteDir );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(TEXT_67);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(remoteDir );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_75);
    stringBuffer.append(TEXT_76);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    if (MD5) {
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(TEXT_98);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    }
    stringBuffer.append(TEXT_102);
    
} else {

    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    if (("true").equals(useExistingConn)) {
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(conn );
    stringBuffer.append(TEXT_107);
    } else {
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(host );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(port );
    stringBuffer.append(TEXT_113);
    if (("ACTIVE").equals(connectMode)) {
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    } else {
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_117);
    }
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(username );
    stringBuffer.append(TEXT_123);
    stringBuffer.append(password );
    stringBuffer.append(TEXT_124);
    }
    
  //add feature 19709,add the "Transfer mode" option,
  //the default transfer mode is ASCII,we don't change the mode when ascii is choosed.
  if("binary".equalsIgnoreCase(mode)){
  
    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_126);
    
  }
  
    stringBuffer.append(TEXT_127);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(remoteDir );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(TEXT_130);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(remoteDir );
    stringBuffer.append(TEXT_134);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(TEXT_138);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_140);
    stringBuffer.append(TEXT_141);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_142);
    stringBuffer.append(remoteDir );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(TEXT_144);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_145);
    stringBuffer.append(filename);
    stringBuffer.append(TEXT_146);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_150);
    stringBuffer.append(TEXT_151);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(TEXT_155);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_157);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_158);
    if (MD5) {
    stringBuffer.append(TEXT_159);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_161);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_162);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(TEXT_164);
    stringBuffer.append(outputConnName );
    stringBuffer.append(TEXT_165);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_166);
    }
    stringBuffer.append(TEXT_167);
    
}

    stringBuffer.append(TEXT_168);
    return stringBuffer.toString();
  }
}
