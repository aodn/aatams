package org.talend.designer.codegen.translators.internet.ftp;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.List;
import java.util.Map;

public class TFTPGetBeginJava
{
  protected static String nl;
  public static synchronized TFTPGetBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TFTPGetBeginJava result = new TFTPGetBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "int nb_file_";
  protected final String TEXT_2 = " = 0;";
  protected final String TEXT_3 = NL + "  java.util.Properties props_";
  protected final String TEXT_4 = " = System.getProperties();" + NL + "  props_";
  protected final String TEXT_5 = ".put(\"socksProxyPort\", ";
  protected final String TEXT_6 = ");" + NL + "  props_";
  protected final String TEXT_7 = ".put(\"socksProxyHost\", ";
  protected final String TEXT_8 = ");" + NL + "  props_";
  protected final String TEXT_9 = ".put(\"java.net.socks.username\", ";
  protected final String TEXT_10 = ");" + NL + "  props_";
  protected final String TEXT_11 = ".put(\"java.net.socks.password\", ";
  protected final String TEXT_12 = ");        ";
  protected final String TEXT_13 = "  " + NL + "  final java.util.List<String> msg_";
  protected final String TEXT_14 = " = new java.util.ArrayList<String>();" + NL;
  protected final String TEXT_15 = NL + "    class MyUserInfo_";
  protected final String TEXT_16 = " implements com.jcraft.jsch.UserInfo, com.jcraft.jsch.UIKeyboardInteractive {" + NL + "      String passphrase_";
  protected final String TEXT_17 = " = ";
  protected final String TEXT_18 = ";" + NL + "" + NL + "      public String getPassphrase() { return passphrase_";
  protected final String TEXT_19 = "; }" + NL + "" + NL + "      public String getPassword() { return null; } " + NL + "" + NL + "      public boolean promptPassword(String arg0) { return true; } " + NL + "" + NL + "      public boolean promptPassphrase(String arg0) { return true; } " + NL + "" + NL + "      public boolean promptYesNo(String arg0) { return true; } " + NL + "" + NL + "      public void showMessage(String arg0) { } " + NL + "" + NL + "      public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt," + NL + "      boolean[] echo) {" + NL + "        String[] password_";
  protected final String TEXT_20 = " = {";
  protected final String TEXT_21 = "};" + NL + "        return password_";
  protected final String TEXT_22 = ";" + NL + "      }" + NL + "    }; " + NL + "" + NL + "    final com.jcraft.jsch.UserInfo defaultUserInfo_";
  protected final String TEXT_23 = " = new MyUserInfo_";
  protected final String TEXT_24 = "();";
  protected final String TEXT_25 = NL + NL + "  class MyProgressMonitor_";
  protected final String TEXT_26 = " implements com.jcraft.jsch.SftpProgressMonitor {" + NL + "" + NL + "    private long percent = -1;" + NL + "" + NL + "    public void init(int op, String src, String dest, long max) {}" + NL + "" + NL + "    public boolean count(long count) {return true;}" + NL + "" + NL + "    public void end() {}" + NL + "  } " + NL + "" + NL + "  class SFTPGetter_";
  protected final String TEXT_27 = " {" + NL + "" + NL + "    private com.jcraft.jsch.ChannelSftp cnlSFTP = null;" + NL + "" + NL + "    private com.jcraft.jsch.SftpProgressMonitor monitor = null;" + NL + "" + NL + "    private int count = 0;" + NL + "" + NL + "    public void getAllFiles(String remoteDirectory, String localDirectory) throws com.jcraft.jsch.SftpException {" + NL + "      " + NL + "      cnlSFTP.cd(remoteDirectory);" + NL + "      java.util.Vector sftpFiles = cnlSFTP.ls(\".\");" + NL + "" + NL + "      for (Object sftpFile : sftpFiles) {" + NL + "        com.jcraft.jsch.ChannelSftp.LsEntry lsEntry = (com.jcraft.jsch.ChannelSftp.LsEntry) sftpFile;" + NL + "        com.jcraft.jsch.SftpATTRS attrs = lsEntry.getAttrs();" + NL + "" + NL + "        if ((\".\").equals(lsEntry.getFilename()) || (\"..\").equals(lsEntry.getFilename())) {" + NL + "          continue;" + NL + "        }" + NL + "" + NL + "        if (attrs.isDir()) {" + NL + "          java.io.File localFile = new java.io.File(localDirectory + \"/\" + lsEntry.getFilename());" + NL + "" + NL + "          if (!localFile.exists()) {" + NL + "            localFile.mkdir();" + NL + "          }" + NL + "          getAllFiles(remoteDirectory + \"/\" + lsEntry.getFilename(), localDirectory + \"/\" + lsEntry.getFilename());" + NL + "          cnlSFTP.cd(remoteDirectory);" + NL + "        } else if (!attrs.isLink()) {" + NL + "          downloadFile(localDirectory, lsEntry.getFilename());" + NL + "        }" + NL + "      }" + NL + "    }" + NL + "" + NL + "    public void getFiles(String remoteDirectory, String localDirectory, String maskStr) throws com.jcraft.jsch.SftpException {" + NL + "      " + NL + "      cnlSFTP.cd(remoteDirectory);" + NL + "      java.util.Vector sftpFiles = cnlSFTP.ls(\".\");" + NL + "" + NL + "      for (Object sftpFile : sftpFiles) {" + NL + "        com.jcraft.jsch.ChannelSftp.LsEntry lsEntry = (com.jcraft.jsch.ChannelSftp.LsEntry) sftpFile;" + NL + "        com.jcraft.jsch.SftpATTRS attrs = lsEntry.getAttrs();" + NL + "" + NL + "        if ((\".\").equals(lsEntry.getFilename()) || (\"..\").equals(lsEntry.getFilename())) {" + NL + "          continue;" + NL + "        }" + NL + "" + NL + "        if (!attrs.isDir() && !attrs.isLink()) {" + NL + "" + NL + "          if (lsEntry.getFilename().matches(maskStr)) {" + NL + "            downloadFile(localDirectory, lsEntry.getFilename());" + NL + "          }" + NL + "        }" + NL + "      }" + NL + "    }" + NL + "" + NL + "    public void chdir(String path) throws com.jcraft.jsch.SftpException{" + NL + "      cnlSFTP.cd(path);" + NL + "    }" + NL + "" + NL + "    public String pwd() throws com.jcraft.jsch.SftpException{" + NL + "      return cnlSFTP.pwd();" + NL + "    }" + NL + "" + NL + "    private void downloadFile(String localFileName, String remoteFileName) throws com.jcraft.jsch.SftpException {" + NL + "" + NL + "      try {" + NL + "        cnlSFTP.get(remoteFileName, localFileName, monitor,";
  protected final String TEXT_28 = "          " + NL + "            com.jcraft.jsch.ChannelSftp.OVERWRITE";
  protected final String TEXT_29 = NL + "            com.jcraft.jsch.ChannelSftp.APPEND";
  protected final String TEXT_30 = NL + "            com.jcraft.jsch.ChannelSftp.RESUME";
  protected final String TEXT_31 = NL + "        );" + NL + "        msg_";
  protected final String TEXT_32 = ".add(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_33 = NL + "          System.out.println(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_34 = NL + "        globalMap.put(\"";
  protected final String TEXT_35 = "_CURRENT_STATUS\", \"File transfer OK.\");" + NL + "      } catch (com.jcraft.jsch.SftpException e){" + NL + "" + NL + "        if (e.id == com.jcraft.jsch.ChannelSftp.SSH_FX_FAILURE || e.id == com.jcraft.jsch.ChannelSftp.SSH_FX_BAD_MESSAGE) {" + NL + "          msg_";
  protected final String TEXT_36 = ".add(\"file [\" + remoteFileName + \"] downloaded unsuccessfully.\");" + NL + "          globalMap.put(\"";
  protected final String TEXT_37 = "_CURRENT_STATUS\", \"File transfer fail.\");" + NL + "        }" + NL + "        throw e;" + NL + "      }" + NL + "      count++;" + NL + "    }" + NL + "  }" + NL;
  protected final String TEXT_38 = NL + "    com.jcraft.jsch.ChannelSftp c_";
  protected final String TEXT_39 = " = (com.jcraft.jsch.ChannelSftp)globalMap.get(\"";
  protected final String TEXT_40 = "\");" + NL + "    if(c_";
  protected final String TEXT_41 = ".getHome()!=null && !c_";
  protected final String TEXT_42 = ".getHome().equals(c_";
  protected final String TEXT_43 = ".pwd())){" + NL + "  \t\tc_";
  protected final String TEXT_44 = ".cd(c_";
  protected final String TEXT_45 = ".getHome());" + NL + "  \t}";
  protected final String TEXT_46 = "    " + NL + "    com.jcraft.jsch.JSch jsch_";
  protected final String TEXT_47 = "=new com.jcraft.jsch.JSch(); " + NL;
  protected final String TEXT_48 = NL + "      jsch_";
  protected final String TEXT_49 = ".addIdentity(";
  protected final String TEXT_50 = ", defaultUserInfo_";
  protected final String TEXT_51 = ".getPassphrase());";
  protected final String TEXT_52 = NL + "    com.jcraft.jsch.Session session_";
  protected final String TEXT_53 = "=jsch_";
  protected final String TEXT_54 = ".getSession(";
  protected final String TEXT_55 = ", ";
  protected final String TEXT_56 = ", ";
  protected final String TEXT_57 = ");" + NL;
  protected final String TEXT_58 = " " + NL + "      session_";
  protected final String TEXT_59 = ".setPassword(";
  protected final String TEXT_60 = "); ";
  protected final String TEXT_61 = NL + NL + "    session_";
  protected final String TEXT_62 = ".setUserInfo(defaultUserInfo_";
  protected final String TEXT_63 = "); " + NL + "    session_";
  protected final String TEXT_64 = ".connect();" + NL + "    com.jcraft.jsch. Channel channel_";
  protected final String TEXT_65 = "=session_";
  protected final String TEXT_66 = ".openChannel(\"sftp\");" + NL + "    channel_";
  protected final String TEXT_67 = ".connect();" + NL + "    com.jcraft.jsch.ChannelSftp c_";
  protected final String TEXT_68 = "=(com.jcraft.jsch.ChannelSftp)channel_";
  protected final String TEXT_69 = ";" + NL + "    c_";
  protected final String TEXT_70 = ".setFilenameEncoding(";
  protected final String TEXT_71 = ");";
  protected final String TEXT_72 = NL + "  com.jcraft.jsch.SftpProgressMonitor monitor_";
  protected final String TEXT_73 = " = new MyProgressMonitor_";
  protected final String TEXT_74 = "();" + NL + "  SFTPGetter_";
  protected final String TEXT_75 = " getter_";
  protected final String TEXT_76 = " = new SFTPGetter_";
  protected final String TEXT_77 = "();" + NL + "  getter_";
  protected final String TEXT_78 = ".cnlSFTP = c_";
  protected final String TEXT_79 = ";" + NL + "  getter_";
  protected final String TEXT_80 = ".monitor = monitor_";
  protected final String TEXT_81 = ";" + NL + "  String remotedir_";
  protected final String TEXT_82 = " = ";
  protected final String TEXT_83 = ";" + NL + "  c_";
  protected final String TEXT_84 = ".cd(remotedir_";
  protected final String TEXT_85 = ");";
  protected final String TEXT_86 = "  " + NL + "  final com.enterprisedt.net.ftp.TransferCompleteStrings msg_";
  protected final String TEXT_87 = " = new com.enterprisedt.net.ftp.TransferCompleteStrings();" + NL + "" + NL + "  class FTPGetter_";
  protected final String TEXT_88 = " {" + NL + "    private com.enterprisedt.net.ftp.FTPClient ftpClient = null;" + NL + "    private int count = 0;" + NL + "" + NL + "    public void getAllFiles(String remoteDirectory, String localDirectory) throws java.io.IOException, " + NL + "      com.enterprisedt.net.ftp.FTPException, java.text.ParseException {" + NL + "" + NL + "      ftpClient.chdir(remoteDirectory);" + NL + "      String path = ftpClient.pwd();" + NL + "      String[] ftpFileNames = null;" + NL + "      com.enterprisedt.net.ftp.FTPFile[] ftpFiles = null;" + NL + "" + NL + "      try{" + NL + "        //use dir() for Bug9562 with FTP server in AS400" + NL + "        ftpFileNames = ftpClient.dir(null, false);" + NL + "        //Bug 13272, the same as getFiles()." + NL + "      } catch (com.enterprisedt.net.ftp.FTPException e){" + NL + "        ftpFileNames = null;" + NL + "        ftpFiles = ftpClient.dirDetails(\".\");" + NL + "      }" + NL + "" + NL + "      //if dirDetails(...) doesn't work, then use dir(...), distinguish file type by FTPException" + NL + "" + NL + "      if ((ftpFiles == null) && (ftpFileNames != null)){" + NL + "        //if the file is folder, catch the FTPException and recur" + NL + "        for (String ftpFileName : ftpFileNames){" + NL + "          try {" + NL + "            downloadFile(localDirectory + \"/\" + ftpFileName,ftpFileName);" + NL + "          } catch (com.enterprisedt.net.ftp.FTPException e) {" + NL + "            java.io.File localFile = new java.io.File(localDirectory + \"/\" + ftpFileName);" + NL + "            " + NL + "            if (!localFile.exists()) {" + NL + "              localFile.mkdir();" + NL + "            }" + NL + "            getAllFiles(path + \"/\" + ftpFileName, localDirectory + \"/\" + ftpFileName);" + NL + "            ftpClient.chdir(path);" + NL + "          }" + NL + "        }" + NL + "      } else {" + NL + "        for (com.enterprisedt.net.ftp.FTPFile ftpFile : ftpFiles) {" + NL + "" + NL + "          if (ftpFile.isDir()) {" + NL + "            if ((!(\".\").equals(ftpFile.getName())) && (!(\"..\").equals(ftpFile.getName()))) {" + NL + "              java.io.File localFile = new java.io.File(localDirectory + \"/\" + ftpFile.getName());" + NL + "" + NL + "              if (!localFile.exists()) {" + NL + "                localFile.mkdir();" + NL + "              }" + NL + "              getAllFiles(path + \"/\" + ftpFile.getName(), localDirectory + \"/\" + ftpFile.getName());" + NL + "              ftpClient.chdir(path);" + NL + "            }" + NL + "          } else if (!ftpFile.isLink()) {" + NL + "            downloadFile(localDirectory + \"/\" + ftpFile.getName(),ftpFile.getName());" + NL + "          }" + NL + "        }" + NL + "      }" + NL + "    }" + NL + "" + NL + "    public void getFiles(String remoteDirectory, String localDirectory, String maskStr) throws java.io.IOException," + NL + "      com.enterprisedt.net.ftp.FTPException, java.text.ParseException {" + NL + "      ftpClient.chdir(remoteDirectory);" + NL + "      String[] ftpFileNames = null;" + NL + "      com.enterprisedt.net.ftp.FTPFile[] ftpFiles = null;" + NL + "" + NL + "      try {" + NL + "        //use dir() for Bug9562 with FTP server in AS400 (the same way as getAllFiles())" + NL + "        ftpFileNames = ftpClient.dir(null, false);" + NL + "        //Bug 13272, if dir() throw exception, use dirDetails()." + NL + "      } catch (com.enterprisedt.net.ftp.FTPException e){" + NL + "        ftpFileNames = null;" + NL + "        ftpFiles = ftpClient.dirDetails(\".\");" + NL + "      }" + NL + "      //if dirDetails(...) doesn't work, then use dir(...), but can not distinguish file type" + NL + "" + NL + "      if ((ftpFiles == null) && (ftpFileNames != null)){" + NL + "        for (String ftpFileName : ftpFileNames) {" + NL + "          if (ftpFileName.matches(maskStr)) {" + NL + "            downloadFile(localDirectory + \"/\" + ftpFileName,ftpFileName);" + NL + "          }" + NL + "        }" + NL + "      } else{" + NL + "" + NL + "        for (com.enterprisedt.net.ftp.FTPFile ftpFile : ftpFiles) {" + NL + "" + NL + "          if (!ftpFile.isDir() && !ftpFile.isLink()) {" + NL + "            String fileName = ftpFile.getName();" + NL + "" + NL + "            if (fileName.matches(maskStr)) {" + NL + "              downloadFile(localDirectory + \"/\" + fileName,fileName);" + NL + "            }" + NL + "          }" + NL + "        }" + NL + "      }" + NL + "    }" + NL + "" + NL + "    public void chdir(String path) throws java.io.IOException,  com.enterprisedt.net.ftp.FTPException{" + NL + "      ftpClient.chdir(path);" + NL + "    }" + NL + "" + NL + "    public String pwd() throws java.io.IOException,  com.enterprisedt.net.ftp.FTPException{" + NL + "      return ftpClient.pwd();" + NL + "    }" + NL + "" + NL + "    private void downloadFile(String localFileName, String remoteFileName) throws java.io.IOException, com.enterprisedt.net.ftp.FTPException {" + NL + "      java.io.File localFile = new java.io.File(localFileName);" + NL + "" + NL + "      try {";
  protected final String TEXT_89 = NL + "          java.io.FileOutputStream fout = new java.io.FileOutputStream(localFile, true);" + NL + "          ftpClient.get(fout, remoteFileName);" + NL + "          msg_";
  protected final String TEXT_90 = ".add(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_91 = NL + "            System.out.println(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_92 = NL + "          globalMap.put(\"";
  protected final String TEXT_93 = "_CURRENT_STATUS\", \"File transfer OK.\");";
  protected final String TEXT_94 = NL + "          if (!localFile.exists()) {" + NL + "            ftpClient.get(localFileName, remoteFileName);" + NL + "            msg_";
  protected final String TEXT_95 = ".add(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_96 = NL + "              System.out.println(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_97 = NL + "            globalMap.put(\"";
  protected final String TEXT_98 = "_CURRENT_STATUS\", \"File transfer OK.\");" + NL + "          } else { " + NL + "            msg_";
  protected final String TEXT_99 = ".add(\"file [\"+ remoteFileName +\"] exit transmission.\");" + NL + "            globalMap.put(\"";
  protected final String TEXT_100 = "_CURRENT_STATUS\", \"No file transfered.\");" + NL + "          }";
  protected final String TEXT_101 = NL + "          ftpClient.get(localFileName, remoteFileName);" + NL + "          msg_";
  protected final String TEXT_102 = ".add(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_103 = NL + "            System.out.println(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_104 = NL + "          if (localFile.exists()) {" + NL + "            long ftpSize = ftpClient.size(remoteFileName);" + NL + "            long localSize = localFile.length();" + NL + "" + NL + "            if (ftpSize != localSize) {" + NL + "              ftpClient.get(localFileName, remoteFileName);" + NL + "              msg_";
  protected final String TEXT_105 = ".add(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_106 = NL + "              \tSystem.out.println(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_107 = NL + "              globalMap.put(\"";
  protected final String TEXT_108 = "_CURRENT_STATUS\", \"File transfer OK.\");" + NL + "            } else {" + NL + "              msg_";
  protected final String TEXT_109 = ".add(\"file [\"+ remoteFileName +\"] exit transmission.\");" + NL + "              globalMap.put(\"";
  protected final String TEXT_110 = "_CURRENT_STATUS\", \"No file transfered.\");" + NL + "            }" + NL + "          } else {" + NL + "            ftpClient.get(localFileName, remoteFileName);" + NL + "            msg_";
  protected final String TEXT_111 = ".add(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_112 = NL + "              System.out.println(\"file [\" + remoteFileName + \"] downloaded successfully.\");";
  protected final String TEXT_113 = NL + "            globalMap.put(\"";
  protected final String TEXT_114 = "_CURRENT_STATUS\", \"File transfer OK.\");" + NL + "          }";
  protected final String TEXT_115 = NL + "      } catch (com.enterprisedt.net.ftp.FTPException e) {" + NL + "        msg_";
  protected final String TEXT_116 = ".add(\"file [\" + remoteFileName + \"] downloaded unsuccessfully.\");" + NL + "        globalMap.put(\"";
  protected final String TEXT_117 = "_CURRENT_STATUS\", \"File transfer fail.\");" + NL + "        throw e;" + NL + "      }" + NL + "      count++;" + NL + "    }" + NL + "  }" + NL + "  com.enterprisedt.net.ftp.FTPClient ftp_";
  protected final String TEXT_118 = " = null;" + NL;
  protected final String TEXT_119 = NL + "    ftp_";
  protected final String TEXT_120 = " = (com.enterprisedt.net.ftp.FTPClient)globalMap.get(\"";
  protected final String TEXT_121 = "\");";
  protected final String TEXT_122 = "    " + NL + "    ftp_";
  protected final String TEXT_123 = " = new com.enterprisedt.net.ftp.FTPClient();" + NL + "    ftp_";
  protected final String TEXT_124 = ".setRemoteHost(";
  protected final String TEXT_125 = ");" + NL + "    ftp_";
  protected final String TEXT_126 = ".setRemotePort(";
  protected final String TEXT_127 = ");" + NL;
  protected final String TEXT_128 = NL + "      ftp_";
  protected final String TEXT_129 = ".setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.ACTIVE);";
  protected final String TEXT_130 = NL + "      ftp_";
  protected final String TEXT_131 = ".setConnectMode(com.enterprisedt.net.ftp.FTPConnectMode.PASV);";
  protected final String TEXT_132 = NL + "    ftp_";
  protected final String TEXT_133 = ".setControlEncoding(";
  protected final String TEXT_134 = ");" + NL + "    ftp_";
  protected final String TEXT_135 = ".connect();" + NL + "    ftp_";
  protected final String TEXT_136 = ".login(";
  protected final String TEXT_137 = ", ";
  protected final String TEXT_138 = ");  ";
  protected final String TEXT_139 = NL + "  msg_";
  protected final String TEXT_140 = ".clearAll();" + NL + "  FTPGetter_";
  protected final String TEXT_141 = " getter_";
  protected final String TEXT_142 = " = new FTPGetter_";
  protected final String TEXT_143 = "();" + NL + "  getter_";
  protected final String TEXT_144 = ".ftpClient = ftp_";
  protected final String TEXT_145 = ";" + NL + "  String remotedir_";
  protected final String TEXT_146 = " = ";
  protected final String TEXT_147 = ";" + NL + "  ftp_";
  protected final String TEXT_148 = ".chdir(remotedir_";
  protected final String TEXT_149 = ");";
  protected final String TEXT_150 = NL + "  class FTPSGetter_";
  protected final String TEXT_151 = " {" + NL + "    private it.sauronsoftware.ftp4j.FTPClient ftpClient = null;" + NL + "    private int count = 0;" + NL + "" + NL + "    public void getAllFiles(String remoteDirectory, String localDirectory)" + NL + "      throws IllegalStateException, IOException, java.io.FileNotFoundException," + NL + "      it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException, " + NL + "      it.sauronsoftware.ftp4j.FTPDataTransferException, it.sauronsoftware.ftp4j.FTPAbortedException, " + NL + "      it.sauronsoftware.ftp4j.FTPListParseException {" + NL + "" + NL + "      ftpClient.changeDirectory(remoteDirectory);" + NL + "      String path = ftpClient.currentDirectory();" + NL + "      it.sauronsoftware.ftp4j.FTPFile[] ftpFiles = null;" + NL + "      ftpFiles = ftpClient.list();" + NL + "" + NL + "      for (it.sauronsoftware.ftp4j.FTPFile ftpFile : ftpFiles) {" + NL + "" + NL + "        if (ftpFile.getType() == it.sauronsoftware.ftp4j.FTPFile.TYPE_DIRECTORY) {" + NL + "" + NL + "          if ((!(\".\").equals(ftpFile.getName())) && (!(\"..\").equals(ftpFile.getName()))) {" + NL + "            java.io.File localFile = new java.io.File(localDirectory + \"/\" + ftpFile.getName());" + NL + "" + NL + "            if (!localFile.exists()) {" + NL + "              localFile.mkdir();" + NL + "            }" + NL + "            getAllFiles(path + \"/\" + ftpFile.getName(), localDirectory + \"/\" + ftpFile.getName());" + NL + "            ftpClient.changeDirectory(path);" + NL + "          }" + NL + "        } else if (ftpFile.getType() != it.sauronsoftware.ftp4j.FTPFile.TYPE_LINK) {" + NL + "          downloadFile(localDirectory + \"/\" + ftpFile.getName(),ftpFile.getName());" + NL + "        }" + NL + "      }" + NL + "    }" + NL + "" + NL + "    public void getFiles(String remoteDirectory, String localDirectory, String maskStr) " + NL + "      throws IllegalStateException, IOException, java.io.FileNotFoundException," + NL + "      it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException, " + NL + "      it.sauronsoftware.ftp4j.FTPDataTransferException, it.sauronsoftware.ftp4j.FTPAbortedException, " + NL + "      it.sauronsoftware.ftp4j.FTPListParseException {" + NL + "" + NL + "      ftpClient.changeDirectory(remoteDirectory);" + NL + "      it.sauronsoftware.ftp4j.FTPFile[] ftpFiles = null;" + NL + "      ftpFiles = ftpClient.list(\".\");" + NL + "" + NL + "      for(it.sauronsoftware.ftp4j.FTPFile ftpFile : ftpFiles) {" + NL + "" + NL + "        if ((ftpFile.getType() != it.sauronsoftware.ftp4j.FTPFile.TYPE_DIRECTORY) && (ftpFile.getType() != it.sauronsoftware.ftp4j.FTPFile.TYPE_LINK)) {" + NL + "          String fileName = ftpFile.getName();" + NL + "" + NL + "          if (fileName.matches(maskStr)) {" + NL + "            downloadFile(localDirectory + \"/\" + fileName,fileName);" + NL + "          }" + NL + "        }" + NL + "      }" + NL + "    }" + NL + "" + NL + "    public void chdir(String path) " + NL + "      throws IllegalStateException, IOException, " + NL + "      it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException {" + NL + "      ftpClient.changeDirectory(path);" + NL + "    }" + NL + "" + NL + "    public String pwd() " + NL + "      throws IllegalStateException, IOException, " + NL + "      it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException {" + NL + "      return ftpClient.currentDirectory();" + NL + "    }" + NL + "" + NL + "    private void downloadFile(String localFileName, String remoteFileName) " + NL + "      throws IllegalStateException, java.io.FileNotFoundException, IOException, " + NL + "      it.sauronsoftware.ftp4j.FTPIllegalReplyException, it.sauronsoftware.ftp4j.FTPException, " + NL + "      it.sauronsoftware.ftp4j.FTPDataTransferException, it.sauronsoftware.ftp4j.FTPAbortedException {" + NL + "      " + NL + "      java.io.File localFile = new java.io.File(localFileName);" + NL + "      ftpClient.download(remoteFileName, localFile);" + NL + "      count++;" + NL + "    }" + NL + "  }" + NL;
  protected final String TEXT_152 = NL + NL + "    class MyTrust_";
  protected final String TEXT_153 = "{" + NL + "      private javax.net.ssl.TrustManager[] getTrustManagers() " + NL + "        throws java.security.KeyStoreException, java.security.NoSuchAlgorithmException, " + NL + "        java.security.cert.CertificateException, java.security.UnrecoverableKeyException," + NL + "        java.io.FileNotFoundException, java.io.IOException {" + NL + "" + NL + "        java.security.KeyStore ks = java.security.KeyStore.getInstance(\"JKS\");" + NL + "        ks.load(new java.io.FileInputStream(";
  protected final String TEXT_154 = "), ";
  protected final String TEXT_155 = ".toCharArray());" + NL + "        javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory.getInstance(javax.net.ssl.KeyManagerFactory.getDefaultAlgorithm());" + NL + "        tmf.init(ks);" + NL + "        return tmf.getTrustManagers();" + NL + "      }" + NL + "    }" + NL + "    MyTrust_";
  protected final String TEXT_156 = " myTrust_";
  protected final String TEXT_157 = "= null;";
  protected final String TEXT_158 = NL + "  javax.net.ssl.SSLContext sslContext = null;" + NL + "  javax.net.ssl.TrustManager[] trustManager = null;" + NL + "  javax.net.ssl.SSLSocketFactory sslSocketFactory = null;" + NL + "  it.sauronsoftware.ftp4j.FTPClient ftp_";
  protected final String TEXT_159 = " =null;" + NL + "  FTPSGetter_";
  protected final String TEXT_160 = " getter_";
  protected final String TEXT_161 = " = null;" + NL + "  String remotedir_";
  protected final String TEXT_162 = " = null;" + NL + "" + NL + "  try {";
  protected final String TEXT_163 = NL + "      sslContext = javax.net.ssl.SSLContext.getInstance(\"SSL\");" + NL + "      myTrust_";
  protected final String TEXT_164 = " = new MyTrust_";
  protected final String TEXT_165 = "();" + NL + "      trustManager = myTrust_";
  protected final String TEXT_166 = ".getTrustManagers();" + NL + "      sslContext.init(null, trustManager, new java.security.SecureRandom());" + NL + "      sslSocketFactory = sslContext.getSocketFactory();" + NL + "      ftp_";
  protected final String TEXT_167 = " = new it.sauronsoftware.ftp4j.FTPClient();" + NL + "      ftp_";
  protected final String TEXT_168 = ".setSSLSocketFactory(sslSocketFactory);";
  protected final String TEXT_169 = NL + "\t\tftp_";
  protected final String TEXT_170 = ".setSecurity(it.sauronsoftware.ftp4j.FTPClient.SECURITY_FTPS);";
  protected final String TEXT_171 = NL + "    \tftp_";
  protected final String TEXT_172 = ".setSecurity(it.sauronsoftware.ftp4j.FTPClient.SECURITY_FTPES);";
  protected final String TEXT_173 = NL + "      ftp_";
  protected final String TEXT_174 = ".connect(";
  protected final String TEXT_175 = ",";
  protected final String TEXT_176 = ");" + NL + "      ftp_";
  protected final String TEXT_177 = ".login(";
  protected final String TEXT_178 = ", ";
  protected final String TEXT_179 = ");";
  protected final String TEXT_180 = NL + "      ftp_";
  protected final String TEXT_181 = " = (it.sauronsoftware.ftp4j.FTPClient)globalMap.get(\"";
  protected final String TEXT_182 = "\");";
  protected final String TEXT_183 = NL + "    getter_";
  protected final String TEXT_184 = " = new FTPSGetter_";
  protected final String TEXT_185 = "();" + NL + "    getter_";
  protected final String TEXT_186 = ".ftpClient = ftp_";
  protected final String TEXT_187 = ";" + NL + "    remotedir_";
  protected final String TEXT_188 = " = ";
  protected final String TEXT_189 = ";" + NL + "    ftp_";
  protected final String TEXT_190 = ".changeDirectory(remotedir_";
  protected final String TEXT_191 = ");" + NL + "  } catch (java.lang.IllegalStateException e) {" + NL + "    e.printStackTrace();" + NL + "  } catch (java.io.IOException e) {" + NL + "    e.printStackTrace();" + NL + "  } catch (it.sauronsoftware.ftp4j.FTPIllegalReplyException e) {" + NL + "    e.printStackTrace();" + NL + "  } catch (it.sauronsoftware.ftp4j.FTPException e) {" + NL + "    e.printStackTrace();" + NL + "  }";
  protected final String TEXT_192 = NL + "java.util.List<String> maskList_";
  protected final String TEXT_193 = " = new java.util.ArrayList<String>();" + NL;
  protected final String TEXT_194 = "    " + NL + "  maskList_";
  protected final String TEXT_195 = ".add(";
  protected final String TEXT_196 = ");       ";
  protected final String TEXT_197 = "  " + NL + "    ftp_";
  protected final String TEXT_198 = ".setType(com.enterprisedt.net.ftp.FTPTransferType.BINARY);";
  protected final String TEXT_199 = "  " + NL + "    ftp_";
  protected final String TEXT_200 = ".setType(com.enterprisedt.net.ftp.FTPTransferType.ASCII);";
  protected final String TEXT_201 = NL + "String localdir_";
  protected final String TEXT_202 = "  = ";
  protected final String TEXT_203 = ";  " + NL + "//create folder if local direcotry (assigned by property) not exists" + NL + "java.io.File dirHandle_";
  protected final String TEXT_204 = " = new java.io.File(localdir_";
  protected final String TEXT_205 = ");" + NL + "" + NL + "if (!dirHandle_";
  protected final String TEXT_206 = ".exists()) {" + NL + "  dirHandle_";
  protected final String TEXT_207 = ".mkdirs();" + NL + "}" + NL + "String root_";
  protected final String TEXT_208 = " = getter_";
  protected final String TEXT_209 = ".pwd();" + NL + "" + NL + "for (String maskStr_";
  protected final String TEXT_210 = " : maskList_";
  protected final String TEXT_211 = ") { ";
  protected final String TEXT_212 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
String host = ElementParameterParser.getValue(node, "__HOST__");
String port = ElementParameterParser.getValue(node, "__PORT__");
String user = ElementParameterParser.getValue(node, "__USERNAME__");
String pass = ElementParameterParser.getValue(node, "__PASSWORD__");
String overwrite = ElementParameterParser.getValue(node, "__OVERWRITE__");
String sftpoverwrite =ElementParameterParser.getValue(node, "__SFTPOVERWRITE__");
String localdir = ElementParameterParser.getValue(node, "__LOCALDIR__");  
String remotedir = ElementParameterParser.getValue(node, "__REMOTEDIR__");
String encoding = ElementParameterParser.getValue(node, "__ENCODING__");
String authMethod = ElementParameterParser.getValue(node,"__AUTH_METHOD__");
String privateKey = ElementParameterParser.getValue(node,"__PRIVATEKEY__");
String passPhrase = ElementParameterParser.getValue(node,"__PASSPHRASE__");
List<Map<String, String>> files = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__FILES__");
boolean useProxy = ("true").equals(ElementParameterParser.getValue(node,"__USE_PROXY__"));
String proxyHost = ElementParameterParser.getValue(node,"__PROXY_HOST__");
String proxyPort = ElementParameterParser.getValue(node,"__PROXY_PORT__");
String proxyUser = ElementParameterParser.getValue(node,"__PROXY_USERNAME__");
String proxyPassword = ElementParameterParser.getValue(node,"__PROXY_PASSWORD__");
String keystoreFile = ElementParameterParser.getValue(node,"__KEYSTORE_FILE__");
String keystorePass = ElementParameterParser.getValue(node,"__KEYSTORE_PASS__");
boolean append = "true".equals(ElementParameterParser.getValue(node, "__APPEND__"));
String connectMode = ElementParameterParser.getValue(node,"__CONNECT_MODE__");
String connection = ElementParameterParser.getValue(node, "__CONNECTION__");
String useExistingConn = ElementParameterParser.getValue(node, "__USE_EXISTING_CONNECTION__");
boolean bPrintMsg = "true".equals(ElementParameterParser.getValue(node, "__PRINT_MESSAGE__"));
String securityMode = ElementParameterParser.getValue(node, "__SECURITY_MODE__");
boolean sftp = false;
boolean ftps = false;

if ("true".equals(useExistingConn)) {
  List<? extends INode> nodeList = node.getProcess().getGeneratingNodes();

  for(INode n : nodeList) {
    if (n.getUniqueName().equals(connection)) {
      sftp = ("true").equals(ElementParameterParser.getValue(n, "__SFTP__"));
      ftps = ("true").equals(ElementParameterParser.getValue(n, "__FTPS__"));
    }
  }
} else {
  sftp = ("true").equals(ElementParameterParser.getValue(node, "__SFTP__"));
  ftps = ("true").equals(ElementParameterParser.getValue(node, "__FTPS__"));
}

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_2);
    
//The following part support the socks proxy for FTP and SFTP (Socks V4 or V5, they are all OK). 
//And it can not work with the FTP proxy directly, only support the socks proxy.

if (useProxy){

    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(proxyPort );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(proxyHost );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(proxyUser );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(proxyPassword );
    stringBuffer.append(TEXT_12);
    
} 

if (sftp) { // *** sftp *** //

    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_14);
    
  if ("false".equals(useExistingConn)) {
  
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(passPhrase );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(pass);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    
  }
  
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    
          if (("overwrite").equals(sftpoverwrite)){
          
    stringBuffer.append(TEXT_28);
    
          } else if (("append").equals(sftpoverwrite)){
          
    stringBuffer.append(TEXT_29);
    
          } else if (("resume").equals(sftpoverwrite)){
          
    stringBuffer.append(TEXT_30);
    
          }
          
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    if (bPrintMsg) {
    stringBuffer.append(TEXT_33);
    }
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    
  if ("true".equals(useExistingConn)){
    String conn= "conn_" + connection;
    
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(conn );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_45);
    
  } else{
  
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    if ("PUBLICKEY".equals(authMethod)){
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(privateKey );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_51);
    }
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(user);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(host);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(port);
    stringBuffer.append(TEXT_57);
    if("PASSWORD".equals(authMethod)){
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(pass);
    stringBuffer.append(TEXT_60);
    }
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_71);
    
  }
  
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(remotedir);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    
} else if (!ftps) { // *** ftp *** //

    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_88);
    if (append) {
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    if (bPrintMsg) {
    stringBuffer.append(TEXT_91);
    }
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    
        } else if ("never".equals(overwrite)){
        
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_95);
    if (bPrintMsg) {
    stringBuffer.append(TEXT_96);
    }
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    
        } else if ("always".equals(overwrite)){
        
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_102);
    if (bPrintMsg) {
    stringBuffer.append(TEXT_103);
    }
    
        } else if ("size_differ".equals(overwrite)) {
        
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_105);
    if (bPrintMsg) {
    stringBuffer.append(TEXT_106);
    }
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_111);
    if (bPrintMsg) {
    stringBuffer.append(TEXT_112);
    }
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    
        }
        
    stringBuffer.append(TEXT_115);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
      
  if ("true".equals(useExistingConn)){
    String conn= "conn_" + connection;
    
    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_120);
    stringBuffer.append(conn );
    stringBuffer.append(TEXT_121);
    } else {
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_123);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_124);
    stringBuffer.append(host );
    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_126);
    stringBuffer.append(port );
    stringBuffer.append(TEXT_127);
    
    if (("ACTIVE").equals(connectMode)){
    
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    
    }else{
    
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    
    }
    
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(encoding);
    stringBuffer.append(TEXT_134);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(user );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(pass );
    stringBuffer.append(TEXT_138);
    } 
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_145);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(remotedir);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_149);
    
} else { // *** ftps *** //

    stringBuffer.append(TEXT_150);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_151);
      
  if ("false".equals(useExistingConn)){
  
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_153);
    stringBuffer.append(keystoreFile);
    stringBuffer.append(TEXT_154);
    stringBuffer.append(keystorePass);
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_157);
    
  }
  
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_159);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_162);
      
    if ("false".equals(useExistingConn)){
    
    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_164);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_166);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_168);
    
	if("IMPLICIT".equals(securityMode)){

    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_170);
    
	}else if("EXPLICIT".equals(securityMode)){

    stringBuffer.append(TEXT_171);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_172);
    
    }

    stringBuffer.append(TEXT_173);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_174);
    stringBuffer.append(host );
    stringBuffer.append(TEXT_175);
    stringBuffer.append(port );
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_177);
    stringBuffer.append(user );
    stringBuffer.append(TEXT_178);
    stringBuffer.append(pass );
    stringBuffer.append(TEXT_179);
      
    } else {
      String conn= "conn_" + connection;
      
    stringBuffer.append(TEXT_180);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(conn );
    stringBuffer.append(TEXT_182);
      
    }
    
    stringBuffer.append(TEXT_183);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_185);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_186);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_187);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_188);
    stringBuffer.append(remotedir);
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_190);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_191);
    
}
// *** share code *** //

    stringBuffer.append(TEXT_192);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_193);
    
for (int i = 0; i < files.size(); i++) {
  Map<String, String> line = files.get(i);

    stringBuffer.append(TEXT_194);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_195);
    stringBuffer.append( line.get("FILEMASK") );
    stringBuffer.append(TEXT_196);
    
}

if (!sftp && !ftps) { // *** ftp *** //
  if ("binary".equalsIgnoreCase(ElementParameterParser.getValue(node, "__MODE__"))) {
  
    stringBuffer.append(TEXT_197);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_198);
    
  }else {  
  
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    
  }
}

    stringBuffer.append(TEXT_201);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_202);
    stringBuffer.append(localdir);
    stringBuffer.append(TEXT_203);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_204);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_205);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_210);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_211);
    stringBuffer.append(TEXT_212);
    return stringBuffer.toString();
  }
}
