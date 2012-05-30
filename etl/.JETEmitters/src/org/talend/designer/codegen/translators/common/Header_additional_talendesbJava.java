package org.talend.designer.codegen.translators.common;

import java.util.Vector;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.ElementParameterParser;

public class Header_additional_talendesbJava
{
  protected static String nl;
  public static synchronized Header_additional_talendesbJava create(String lineSeparator)
  {
    nl = lineSeparator;
    Header_additional_talendesbJava result = new Header_additional_talendesbJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\tinterface ESBProviderCallbackTalendJobInner extends ESBProviderCallback {" + NL + "\t\tvoid setCustomProperties(java.util.Map<String, String> props);" + NL + "\t\tvoid sendFault(Throwable e);" + NL + "\t\tvoid sendBusinessFault(String faultString, org.dom4j.Document faultDetail);" + NL + "\t} ";
  protected final String TEXT_2 = NL + NL + "\tprivate ESBEndpointRegistry registry = null;" + NL + "\tprivate ESBProviderCallback callback = null;" + NL + "" + NL + "\tpublic void setEndpointRegistry(ESBEndpointRegistry registry) {";
  protected final String TEXT_3 = "\t\tthis.registry = registry;";
  protected final String TEXT_4 = NL + "\t}" + NL + "" + NL + "\tpublic void setProviderCallback(ESBProviderCallback callback) {";
  protected final String TEXT_5 = "\t\tthis.callback = callback;";
  protected final String TEXT_6 = NL + "\t}" + NL + NL;
  protected final String TEXT_7 = NL + "\tpublic ESBEndpointInfo getEndpoint() {" + NL + "\t\treturn new ESBEndpointInfo() {" + NL + "\t\t\t@SuppressWarnings(\"serial\")" + NL + "\t\t\tprivate java.util.Map<String, Object> props = new java.util.HashMap<String, Object>() {{" + NL + "\t\t\t\t// \"request-response\" or \"one-way\"" + NL + "\t\t\t\tput(\"COMMUNICATION_STYLE\", \"";
  protected final String TEXT_8 = "\");" + NL + "\t\t\t\tput(\"dataFormat\", \"PAYLOAD\");" + NL + "\t\t\t\tput(\"portName\", \"{";
  protected final String TEXT_9 = "}";
  protected final String TEXT_10 = "\");" + NL + "\t\t\t\tput(\"serviceName\", \"{";
  protected final String TEXT_11 = "}";
  protected final String TEXT_12 = "\");" + NL + "\t\t\t\tput(\"defaultOperationName\", \"";
  protected final String TEXT_13 = "\");" + NL + "\t\t\t\tput(\"defaultOperationNameSpace\", \"";
  protected final String TEXT_14 = "\");" + NL + "\t\t\t\tput(\"publishedEndpointUrl\", \"";
  protected final String TEXT_15 = "\");" + NL + "\t\t\t}};" + NL + "" + NL + "\t\t\tpublic String getEndpointKey() {" + NL + "\t\t\t\treturn \"cxf\";" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tpublic String getEndpointUri() {" + NL + "\t\t\t\t// projectName + \"_\" + processName" + NL + "\t\t\t\treturn \"";
  protected final String TEXT_16 = "_";
  protected final String TEXT_17 = "\";" + NL + "\t\t\t}" + NL + "" + NL + "\t\t\tpublic java.util.Map<String, Object> getEndpointProperties() {" + NL + "\t\t\t\treturn props;" + NL + "\t\t\t}" + NL + "\t\t};" + NL + "\t}" + NL + "" + NL + "" + NL + "" + NL + "" + NL + "" + NL + "/**" + NL + " * queued message exchange" + NL + " */" + NL + "public class QueuedExchangeContextImpl<T> {" + NL + "" + NL + "\t/**" + NL + "\t * Exchange timeout in seconds" + NL + "\t */" + NL + "\tprivate static final long EXCHANGE_TIMEOUT = 50;" + NL + "" + NL + "\tprivate final java.util.concurrent.Exchanger<Exception> exceptionExchange =" + NL + "\t\tnew java.util.concurrent.Exchanger<Exception>();" + NL + "\tprivate final java.util.concurrent.CountDownLatch latch =" + NL + "\t\tnew java.util.concurrent.CountDownLatch(1);" + NL + "" + NL + "\tprivate final T input;" + NL + "" + NL + "\tprivate T output = null;" + NL + "\tprivate Throwable fault = null;" + NL + "" + NL + "\tpublic QueuedExchangeContextImpl(T inMsg) {" + NL + "\t\tthis.input = inMsg;" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * Don't forget to call this method when you are done" + NL + "\t * with processing of the {@link QueuedExchangeContext}" + NL + "\t */" + NL + "\tpublic void release() throws Exception {" + NL + "\t\tlatch.countDown();" + NL + "\t\tException exception;" + NL + "\t\ttry {" + NL + "\t\t\texception = exceptionExchange.exchange(null, EXCHANGE_TIMEOUT," + NL + "\t\t\t\t\tjava.util.concurrent.TimeUnit.SECONDS);" + NL + "\t\t} catch (InterruptedException e) {" + NL + "\t\t\tthrow new Exception(e);" + NL + "\t\t} catch (java.util.concurrent.TimeoutException e) {" + NL + "\t\t\tthrow new Exception(e);" + NL + "\t\t}" + NL + "\t\tif (exception != null) {" + NL + "\t\t\tthrow exception;" + NL + "\t\t}" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * This operation have to be called on the Web Service" + NL + "\t * thread to send response if required" + NL + "\t *" + NL + "\t * @throws InterruptedException" + NL + "\t */" + NL + "\tpublic void completeQueuedProcessing() throws InterruptedException {" + NL + "\t\texceptionExchange.exchange(null);" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * @throws InterruptedException" + NL + "\t */" + NL + "\tvoid waitForRelease(long timeout, java.util.concurrent.TimeUnit unit)" + NL + "\t\t\tthrows InterruptedException {" + NL + "\t\tlatch.await(timeout, unit);" + NL + "\t}" + NL + "" + NL + "\tpublic T getInputMessage() {" + NL + "\t\treturn input;" + NL + "\t}" + NL + "" + NL + "\tpublic void serveOutputMessage(T response) {" + NL + "\t\toutput = response;" + NL + "\t}" + NL + "" + NL + "\tpublic void serveFault(Throwable fault) {" + NL + "\t\tthis.fault = fault;" + NL + "\t}" + NL + "" + NL + "\tpublic boolean isFault() {" + NL + "\t\treturn fault != null;" + NL + "\t}" + NL + "" + NL + "\tpublic T getResponse() {" + NL + "\t\treturn output;" + NL + "\t}" + NL + "" + NL + "\tpublic Throwable getFault() {" + NL + "\t\treturn fault;" + NL + "\t}" + NL + "}" + NL + "" + NL + "/**" + NL + " * message exchange controller" + NL + " */" + NL + "public class QueuedMessageHandlerImpl<T> implements ESBProviderCallback {" + NL + "\tprivate final int MAX_QUEUE_SIZE = 1000;" + NL + "" + NL + "\tprivate final int WAIT_TIMEOUT_SECONDS = 120;" + NL + "" + NL + "\tprivate final java.util.concurrent.BlockingQueue<QueuedExchangeContextImpl<?>> queue =" + NL + "\t\tnew java.util.concurrent.LinkedBlockingQueue<QueuedExchangeContextImpl<?>>(MAX_QUEUE_SIZE);" + NL + "" + NL + "\t/**" + NL + "\t * This method add a newly created" + NL + "\t * {@link QueuedExchangeContextImpl} into the internal" + NL + "\t * blocking queue where consumer thread is waiting for it." + NL + "\t * Then it waits until the {@link QueuedExchangeContextImpl}" + NL + "\t * will be completed for request-response operations" + NL + "\t */" + NL + "\tpublic QueuedExchangeContextImpl<T> invoke(T request) {" + NL + "\t\tQueuedExchangeContextImpl<T> context =" + NL + "\t\t\tnew QueuedExchangeContextImpl<T>(request);" + NL + "\t\tboolean inserted = queue.offer(context);" + NL + "\t\tif (!inserted) {" + NL + "\t\t\ttry {" + NL + "\t\t\t\tcontext.release();" + NL + "\t\t\t} catch (Exception e) {" + NL + "\t\t\t\te.printStackTrace();" + NL + "\t\t\t}" + NL + "\t\t\t// context.serveFault(\"job pool overflow exceed\", null);" + NL + "\t\t\tthrow new RuntimeException(" + NL + "\t\t\t\t\t\"Can't queue request, queue size of \"" + NL + "\t\t\t\t\t\t\t+ MAX_QUEUE_SIZE + \" is exceeded\");" + NL + "\t\t} else {" + NL + "\t\t\ttry {" + NL + "\t\t\t\tcontext.waitForRelease(WAIT_TIMEOUT_SECONDS," + NL + "\t\t\t\t\t\tjava.util.concurrent.TimeUnit.SECONDS);" + NL + "\t\t\t} catch (InterruptedException ie) {" + NL + "\t\t\t\t// context.serveFault(\"job execution timeout\", ie);" + NL + "\t\t\t\tthrow new RuntimeException(" + NL + "\t\t\t\t\t\t\"job execution timeout: \" + ie.getMessage());" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "\t\treturn context;" + NL + "\t}" + NL + "" + NL + "\tQueuedExchangeContextImpl<T> currentExchangeContext;" + NL + "" + NL + "\tpublic T getRequest() throws ESBJobInterruptedException {" + NL + "\t\tcurrentExchangeContext = null;" + NL + "\t\ttry {" + NL + "\t\t\tcurrentExchangeContext = (QueuedExchangeContextImpl<T>) queue.take();" + NL + "\t\t} catch (InterruptedException e) {" + NL + "\t\t\t// e.printStackTrace();" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "\t\t}" + NL + "\t\treturn currentExchangeContext.getInputMessage();" + NL + "\t}" + NL + "" + NL + "\tpublic void sendResponse(Object output) {" + NL + "\t\tif (null == currentExchangeContext) {" + NL + "\t\t\tthrow new RuntimeException(\"sendResponse() invoked before getRequest()\");" + NL + "\t\t}" + NL + "" + NL + "\t\tif (output instanceof Throwable) {" + NL + "\t\t\t// fault" + NL + "\t\t\tcurrentExchangeContext.serveFault((Throwable) output);" + NL + "\t\t} else {" + NL + "\t\t\t// response" + NL + "\t\t\tcurrentExchangeContext.serveOutputMessage((T) output);" + NL + "\t\t}" + NL + "" + NL + "\t\ttry {" + NL + "\t\t\tcurrentExchangeContext.release();" + NL + "\t\t} catch (Exception e) {" + NL + "\t\t\t// e.printStackTrace();" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "\t\t}" + NL + "\t}" + NL + "}" + NL + "" + NL + "/**" + NL + " * web service provider implementation" + NL + " */";
  protected final String TEXT_18 = NL + "@javax.jws.WebService(" + NL + "\t\tname = \"";
  protected final String TEXT_19 = "PortType\"," + NL + "\t\ttargetNamespace = \"";
  protected final String TEXT_20 = "\"" + NL + "\t)";
  protected final String TEXT_21 = NL + "@javax.jws.soap.SOAPBinding(" + NL + "\t\tparameterStyle = javax.jws.soap.SOAPBinding.ParameterStyle.BARE" + NL + "\t)" + NL + "@javax.xml.ws.ServiceMode(" + NL + "\t\tvalue = javax.xml.ws.Service.Mode.PAYLOAD" + NL + "\t)" + NL + "@javax.xml.ws.WebServiceProvider(";
  protected final String TEXT_22 = NL + "\t\twsdlLocation=\"";
  protected final String TEXT_23 = "\",";
  protected final String TEXT_24 = NL + "\t\ttargetNamespace=\"";
  protected final String TEXT_25 = "\"," + NL + "\t\tserviceName=\"";
  protected final String TEXT_26 = "\"," + NL + "\t\tportName=\"";
  protected final String TEXT_27 = "\"" + NL + "\t)" + NL + "public class ESBProvider_";
  protected final String TEXT_28 = " implements" + NL + "\t\tjavax.xml.ws.Provider<javax.xml.transform.Source> {" + NL + "" + NL + "\tprivate final javax.xml.ws.WebServiceProvider annotation =" + NL + "\t\t\tESBProvider_";
  protected final String TEXT_29 = ".class.getAnnotation(" + NL + "\t\t\t\t\tjavax.xml.ws.WebServiceProvider.class);" + NL + "\tprivate final String TARGET_NS = annotation.targetNamespace();" + NL + "\tprivate final String SERVICE_NAME = annotation.serviceName();" + NL + "\tprivate final String PORT_NAME = annotation.portName();" + NL + "" + NL + "\tprivate final javax.xml.namespace.QName serviceName =" + NL + "\t\t\tnew javax.xml.namespace.QName(TARGET_NS, SERVICE_NAME);" + NL + "\tprivate final javax.xml.namespace.QName portName =" + NL + "\t\t\tnew javax.xml.namespace.QName(TARGET_NS, PORT_NAME);" + NL + "\tprivate final javax.xml.namespace.QName operationName =" + NL + "\t\t\tnew javax.xml.namespace.QName(\"";
  protected final String TEXT_30 = "\", \"";
  protected final String TEXT_31 = "\");" + NL + "" + NL + "\tprivate javax.xml.transform.TransformerFactory factory =" + NL + "\t\t\tjavax.xml.transform.TransformerFactory.newInstance();" + NL + "\tprivate QueuedMessageHandlerImpl<org.dom4j.Document> messageHandler;" + NL + "" + NL + "\t@javax.annotation.Resource()" + NL + "\tprivate javax.xml.ws.WebServiceContext context;" + NL + "" + NL + "\tpublic ESBProvider_";
  protected final String TEXT_32 = "(" + NL + "\t\t\tQueuedMessageHandlerImpl<org.dom4j.Document> messageHandler) {" + NL + "\t\tthis.messageHandler = messageHandler;" + NL + "\t}" + NL;
  protected final String TEXT_33 = NL + "\t@javax.jws.Oneway()";
  protected final String TEXT_34 = NL + "\tpublic javax.xml.transform.Source invoke(javax.xml.transform.Source request) {" + NL + "" + NL + "\t\ttry {" + NL + "//\t\t\tjavax.xml.namespace.QName operation = (javax.xml.namespace.QName) context" + NL + "//\t\t\t\t\t.getMessageContext().get(javax.xml.ws.handler.MessageContext.WSDL_OPERATION);" + NL + "//\t\t\tjavax.xml.namespace.QName port = (javax.xml.namespace.QName) context" + NL + "//\t\t\t\t\t.getMessageContext().get(javax.xml.ws.handler.MessageContext.WSDL_PORT);" + NL + "//\t\t\tjavax.xml.namespace.QName service = (javax.xml.namespace.QName) context" + NL + "//\t\t\t\t\t.getMessageContext().get(javax.xml.ws.handler.MessageContext.WSDL_SERVICE);" + NL + "//\t\t\tif (serviceName.equals(service) && portName.equals(port) && operationName.equals(operation)) {" + NL + "//\t\t\t\t// System.out.println(\"request to implemented operation\");" + NL + "//\t\t\t} else {" + NL + "//\t\t\t\tthrow new RuntimeException(\"operation not implemented by job\");" + NL + "//\t\t\t}" + NL + "" + NL + "\t\t\t// fix for unsupported xmlns=\"\" declaration processing over dom4j implementation" + NL + "\t\t\t//\t\t// old version:" + NL + "\t\t\t//\t\t// org.dom4j.io.DocumentResult docResult = new org.dom4j.io.DocumentResult();" + NL + "\t\t\t//\t\t// factory.newTransformer().transform(request, docResult);" + NL + "\t\t\t//\t\t// org.dom4j.Document requestDoc = docResult.getDocument();" + NL + "\t\t\t// new version:" + NL + "\t\t\tjava.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();" + NL + "\t\t\tfactory.newTransformer().transform(request, new javax.xml.transform.stream.StreamResult(os));" + NL + "\t\t\torg.dom4j.Document requestDoc = new org.dom4j.io.SAXReader().read(" + NL + "\t\t\t\t\tnew java.io.ByteArrayInputStream(os.toByteArray()));" + NL + "\t\t\t// end of fix" + NL + "" + NL + "\t\t\t// System.out.println(\"request: \" + requestDoc.asXML());" + NL + "" + NL + "\t\t\tQueuedExchangeContextImpl<org.dom4j.Document> messageExchange =" + NL + "\t\t\t\t\tmessageHandler.invoke(requestDoc);" + NL + "" + NL + "\t\t\ttry {";
  protected final String TEXT_35 = NL + "\t\t\t\treturn null;";
  protected final String TEXT_36 = NL + "\t\t\t\tif (messageExchange.isFault()) {" + NL + "\t\t\t\t\tthrow messageExchange.getFault();" + NL + "\t\t\t\t} else {" + NL + "\t\t\t\t\torg.dom4j.Document responseDoc = messageExchange.getResponse();" + NL + "\t\t\t\t\tif (null == responseDoc) {" + NL + "\t\t\t\t\t\tthrow new RuntimeException(\"no response provided by Talend job\");" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\t// System.out.println(\"response: \" + responseDoc.asXML());" + NL + "" + NL + "\t\t\t\t\t// fix for unsupported xmlns=\"\" declaration processing over dom4j implementation" + NL + "\t\t\t\t\t//\t\t// old version:" + NL + "\t\t\t\t\t//\t\t// return new org.dom4j.io.DocumentSource(responseDoc);" + NL + "\t\t\t\t\t// new version:" + NL + "\t\t\t\t\tjava.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();" + NL + "\t\t\t\t\tnew org.dom4j.io.XMLWriter(baos).write(responseDoc);" + NL + "\t\t\t\t\treturn new javax.xml.transform.stream.StreamSource(" + NL + "\t\t\t\t\t\t\tnew java.io.ByteArrayInputStream(baos.toByteArray()));" + NL + "\t\t\t\t\t// end of fix" + NL + "\t\t\t\t}";
  protected final String TEXT_37 = NL + "\t\t\t} finally {" + NL + "\t\t\t\tmessageExchange.completeQueuedProcessing();" + NL + "\t\t\t}" + NL + "" + NL + "\t\t} catch (RuntimeException ex) {" + NL + "\t\t\tthrow ex;" + NL + "\t\t} catch (Throwable ex) {" + NL + "\t\t\tex.printStackTrace();" + NL + "\t\t\tthrow new RuntimeException(ex);" + NL + "\t\t}" + NL + "\t}" + NL + "}" + NL + "" + NL + "public class ESBProviderCallbackTalendJobWrapper_";
  protected final String TEXT_38 = " implements ESBProviderCallbackTalendJobInner {" + NL + "" + NL + "\tprivate final String TNS = ESBProvider_";
  protected final String TEXT_39 = ".class.getAnnotation(" + NL + "\t\t\tjavax.xml.ws.WebServiceProvider.class).targetNamespace();" + NL + "" + NL + "\tprivate ESBProviderCallback esbProviderCallback;" + NL + "\tprivate java.util.Map<String, String> customProperty;" + NL + "" + NL + "\tpublic ESBProviderCallbackTalendJobWrapper_";
  protected final String TEXT_40 = "(ESBProviderCallback callback) {" + NL + "\t\tesbProviderCallback = callback;" + NL + "\t}" + NL + "" + NL + "\tpublic Object getRequest() throws ESBJobInterruptedException {" + NL + "\t\treturn esbProviderCallback.getRequest();" + NL + "\t}" + NL + "" + NL + "\tpublic void setCustomProperties(java.util.Map<String, String> props) {" + NL + "\t\tcustomProperty = props;" + NL + "\t}" + NL + "" + NL + "\tpublic void sendResponse(Object response) {" + NL + "\t\tesbProviderCallback.sendResponse(wrapOutput(response));" + NL + "\t}" + NL + "" + NL + "\tpublic void sendFault(Throwable error) {" + NL + "\t\tRuntimeException talendJobError;" + NL + "\t\tif (error instanceof RuntimeException) {" + NL + "\t\t\ttalendJobError = (RuntimeException) error;" + NL + "\t\t} else {" + NL + "\t\t\ttalendJobError = new RuntimeException(" + NL + "\t\t\t\t\t\"Talend job execution error\", error);" + NL + "\t\t}" + NL + "\t\tesbProviderCallback.sendResponse(talendJobError);" + NL + "\t}" + NL + "" + NL + "\tpublic void sendBusinessFault(String faultString," + NL + "\t\t\torg.dom4j.Document faultDetail) {" + NL + "" + NL + "\t\ttry {" + NL + "\t\t\tjavax.xml.soap.SOAPFactory soapFactory =" + NL + "\t\t\t\tjavax.xml.soap.SOAPFactory.newInstance();" + NL + "\t\t\tjavax.xml.soap.SOAPFault soapFault = soapFactory.createFault(" + NL + "\t\t\t\t\tfaultString, new javax.xml.namespace.QName(TNS, \"businessFault\"));" + NL + "\t\t\tif (null != faultDetail) {" + NL + "\t\t\t\t// System.out.println(\"business fault details: \" + faultDoc.asXML());" + NL + "\t\t\t\t// A special version of DOMWriter that does not write xmlns:foo attributes" + NL + "\t\t\t\torg.dom4j.io.DOMWriter writer = new org.dom4j.io.DOMWriter() {" + NL + "\t\t\t\t\tprotected void writeNamespace(org.w3c.dom.Element domElement, org.dom4j.Namespace namespace) {" + NL + "\t\t\t\t\t\t// Do nothing" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t};" + NL + "\t\t\t\torg.w3c.dom.Document faultDetailDom = writer.write(faultDetail);" + NL + "\t\t\t\tsoapFault.addDetail().appendChild(" + NL + "\t\t\t\t\t\tsoapFault.getOwnerDocument().importNode(" + NL + "\t\t\t\t\t\t\t\tfaultDetailDom.getDocumentElement(), true));" + NL + "\t\t\t}" + NL + "\t\t\tesbProviderCallback.sendResponse(" + NL + "\t\t\t\t\twrapOutput(new javax.xml.ws.soap.SOAPFaultException(soapFault)));" + NL + "\t\t} catch (Exception e) {" + NL + "\t\t\tthis.sendFault(e);" + NL + "\t\t}" + NL + "\t}" + NL + "" + NL + "\tprivate Object wrapOutput(Object output) {" + NL + "\t\tif (esbProviderCallback instanceof QueuedMessageHandlerImpl) {" + NL + "\t\t\treturn output;" + NL + "\t\t}" + NL + "\t\treturn wrapPayload(output, customProperty);" + NL + "\t}" + NL + "}" + NL + "" + NL + "class HandlerThread_";
  protected final String TEXT_41 = " extends Thread {" + NL + "" + NL + "\tprivate javax.xml.ws.Endpoint endpoint;" + NL + "\tQueuedMessageHandlerImpl<org.dom4j.Document> handler;" + NL + "" + NL + "\tString endpointUrl = \"";
  protected final String TEXT_42 = "\";" + NL + "" + NL + "\tpublic HandlerThread_";
  protected final String TEXT_43 = "(" + NL + "\t\t\tQueuedMessageHandlerImpl<org.dom4j.Document> handler) {" + NL + "\t\tthis.handler = handler;" + NL + "\t}" + NL + "" + NL + "\tpublic void run() {" + NL;
  protected final String TEXT_44 = NL + "\t\t// test for busy" + NL + "\t\tjava.net.ServerSocket ss = null;" + NL + "\t\ttry {" + NL + "\t\t\tjava.net.URL endpointURL = new java.net.URL(endpointUrl);" + NL + "\t\t\tString host = endpointURL.getHost();" + NL + "\t\t\tint port = endpointURL.getPort();" + NL + "\t\t\tif (\"localhost\".equals(host) || host.startsWith(\"127.0.0\")) {" + NL + "\t\t\t\ttry {" + NL + "\t\t\t\t\tss = new java.net.ServerSocket(port);" + NL + "\t\t\t\t} catch (IOException e) {" + NL + "\t\t\t\t\t// rethrow exception" + NL + "\t\t\t\t\tthrow new IllegalArgumentException(" + NL + "\t\t\t\t\t\t\t\"Cannot start provider with uri: \" + endpointUrl + \". Port \" + port + \" already in use.\");" + NL + "\t\t\t\t} finally {" + NL + "\t\t\t\t\tif (ss != null) {" + NL + "\t\t\t\t\t\ttry {" + NL + "\t\t\t\t\t\t\tss.close();" + NL + "\t\t\t\t\t\t} catch (IOException e) {" + NL + "\t\t\t\t\t\t\t// ignore" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}" + NL + "\t\t\t\ttry {" + NL + "\t\t\t\t\t// ok, let's doublecheck for silent listeners" + NL + "\t\t\t\t\tjava.net.Socket cs = new java.net.Socket(host, port);" + NL + "\t\t\t\t\t// if succeed - somebody silently listening, fail!" + NL + "\t\t\t\t\tcs.close();" + NL + "\t\t\t\t\t// rethrow exception" + NL + "\t\t\t\t\tthrow new IllegalArgumentException(" + NL + "\t\t\t\t\t\t\t\"Cannot start provider with uri: \" + endpointUrl + \". Port \" + port + \" already in use.\");" + NL + "\t\t\t\t} catch (IOException e) {" + NL + "\t\t\t\t\t//ok, nobody listens, proceed" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "\t\t} catch (java.net.MalformedURLException e) {" + NL + "\t\t\t// rethrow exception" + NL + "\t\t\tthrow new IllegalArgumentException(" + NL + "\t\t\t\t\t\"Cannot start provider with uri: \" + endpointUrl + \". Malformed URL.\");" + NL + "\t\t}" + NL + "" + NL + "\t\t// null - WSDL value should be used" + NL + "\t\tendpoint = javax.xml.ws.Endpoint.publish(/* null */ endpointUrl, new ESBProvider_";
  protected final String TEXT_45 = "(handler));" + NL + "" + NL + "\t\tSystem.out.println(\"web service [endpoint: \" + endpointUrl + \"] published\");" + NL + "\t}" + NL + "" + NL + "\tpublic void stopEndpoint() {" + NL + "\t\tif (null != endpoint) {" + NL + "\t\t\tendpoint.stop();" + NL + "\t\t\tSystem.out.println(\"web service [endpoint: \" + endpointUrl + \"] unpublished\");" + NL + "\t\t}" + NL + "\t}" + NL + "}" + NL + NL + NL + NL + NL;
  protected final String TEXT_46 = NL + "\tpublic ESBEndpointInfo getEndpoint() {" + NL + "\t\treturn null;" + NL + "\t}";
  protected final String TEXT_47 = NL + NL + "\tprivate Object wrapPayload(Object payload, Object customProperties) {" + NL + "\t\tjava.util.Map<String, Object> outputWrapped = new java.util.HashMap<String, Object>();" + NL + "\t\toutputWrapped.put(\"PAYLOAD\", payload);" + NL + "\t\tif (null != customProperties) {" + NL + "\t\t\toutputWrapped.put(\"SAM-PROPS\", customProperties);" + NL + "\t\t}" + NL + "\t\treturn outputWrapped;" + NL + "\t}" + NL;
  protected final String TEXT_48 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
Vector v = (Vector) codeGenArgument.getArgument();
IProcess process = (IProcess) v.get(0);

boolean actAsProvider = !process.getNodesOfType("tESBProviderRequestLoop").isEmpty();
boolean actAsConsumer = !process.getNodesOfType("tESBConsumer").isEmpty();

if (actAsProvider || actAsConsumer
	|| !process.getNodesOfType("tESBProviderResponse").isEmpty()
	|| !process.getNodesOfType("tESBProviderFault").isEmpty()) { 
    stringBuffer.append(TEXT_1);
    
}

if (actAsProvider || actAsConsumer) { 
    stringBuffer.append(TEXT_2);
     if (actAsConsumer) { 
    stringBuffer.append(TEXT_3);
     } 
    stringBuffer.append(TEXT_4);
     if (actAsProvider) { 
    stringBuffer.append(TEXT_5);
     } 
    stringBuffer.append(TEXT_6);
     if (actAsProvider) {
	// Web Service Provider
	INode tESBProviderRequestNode = process.getNodesOfType("tESBProviderRequestLoop").get(0);
	String tESBProviderRequestNodeCid = tESBProviderRequestNode.getUniqueName();
	tESBProviderRequestNodeCid = tESBProviderRequestNodeCid.replaceAll("_Loop", "");

	String projectName = codeGenArgument.getCurrentProjectName();
	String processName = process.getName();

	String endpointUrl = ElementParameterParser.getValue(tESBProviderRequestNode, "__ENDPOINT_URI__");

	String wsdlLocation = ElementParameterParser.getValue(tESBProviderRequestNode, "__WSDL_LOCATION__");

	String serviceNS = ElementParameterParser.getValue(tESBProviderRequestNode, "__SERVICE_NS__");
	String serviceName = ElementParameterParser.getValue(tESBProviderRequestNode, "__SERVICE_NAME__");
	if ("--DEFAULT--".equals(serviceName)) {
		serviceName = projectName + "_" + processName;
	}

	String portNS = ElementParameterParser.getValue(tESBProviderRequestNode, "__PORT_NS__");
	String portName = ElementParameterParser.getValue(tESBProviderRequestNode, "__PORT_NAME__");
	if ("--DEFAULT--".equals(portName)) {
		portName = serviceName + "_Port";
	}

	String operationName = ElementParameterParser.getValue(tESBProviderRequestNode, "__OPERATION_NAME__");
	String operationNS = ElementParameterParser.getValue(tESBProviderRequestNode, "__OPERATION_NS__");
	if (null == operationNS || operationNS.isEmpty()) {
		operationNS = serviceNS;
	}

	boolean isOneWay = (process.getNodesOfType("tESBProviderFault").isEmpty()
		&& process.getNodesOfType("tESBProviderResponse").isEmpty());
	
    stringBuffer.append(TEXT_7);
    stringBuffer.append((isOneWay)?"one-way":"request-response");
    stringBuffer.append(TEXT_8);
    stringBuffer.append(portNS);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(portName);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(serviceNS);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(serviceName);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(operationName);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(operationNS);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(endpointUrl);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(projectName);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(processName);
    stringBuffer.append(TEXT_17);
     if (null == wsdlLocation || wsdlLocation.trim().isEmpty()) { 
    stringBuffer.append(TEXT_18);
    stringBuffer.append(serviceName);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(serviceNS);
    stringBuffer.append(TEXT_20);
     } 
    stringBuffer.append(TEXT_21);
     if (null != wsdlLocation && !wsdlLocation.trim().isEmpty()) { 
    stringBuffer.append(TEXT_22);
    stringBuffer.append(wsdlLocation);
    stringBuffer.append(TEXT_23);
     } 
    stringBuffer.append(TEXT_24);
    stringBuffer.append(serviceNS);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(serviceName);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(portName);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_28);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(operationNS);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(operationName);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_32);
     if (isOneWay) { 
    stringBuffer.append(TEXT_33);
     } 
    stringBuffer.append(TEXT_34);
     if (isOneWay) { 
    stringBuffer.append(TEXT_35);
     } else { 
    stringBuffer.append(TEXT_36);
     } 
    stringBuffer.append(TEXT_37);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(endpointUrl);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_43);
    
	//http://jira.talendforge.org/browse/TESB-3603 Clean up default port codes
	//String defaultPort = (String) System.getProperties().get("wsHttpPort");
	//if (null == defaultPort || defaultPort.trim().isEmpty()) {
	//	defaultPort = "8088";
	//}

    stringBuffer.append(TEXT_44);
    stringBuffer.append(tESBProviderRequestNodeCid);
    stringBuffer.append(TEXT_45);
     } else { // end if (actAsProvider) 
    stringBuffer.append(TEXT_46);
     } 
    stringBuffer.append(TEXT_47);
     } // end if (actAsProvider || actAsConsumer) 
    stringBuffer.append(TEXT_48);
    return stringBuffer.toString();
  }
}
