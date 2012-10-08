
<%@ page import="au.org.emii.aatams.FileProcessingStatus" %>
<%@ page import="au.org.emii.aatams.ReceiverDownloadFile" %>
<%@ page import="au.org.emii.aatams.ReceiverDownloadFileType" %>
<%@ page import="au.org.emii.aatams.detection.InvalidDetectionReason" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        <script type="text/javascript" src="${resource(dir:'js', file:'receiverDownloadProgress.js')}"></script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" controller="detection" action="create"><g:message code="default.upload.label" args="['Tag Detection']" /></g:link></span>
              <span class="menuButton"><g:link class="create" controller="receiverEvent" action="create"><g:message code="default.upload.label" args="['Receiver Event']" /></g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.type.label" default="Type" /></td>
                            <td valign="top" class="value">${receiverDownloadFileInstance?.type?.encodeAsHTML()}</td>
                        </tr>
                    
                        <shiro:hasRole name="SysAdmin">
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.path.label" default="Path" /></td>
                              <td valign="top" class="value">${fieldValue(bean: receiverDownloadFileInstance, field: "path")}</td>
                          </tr>
                        </shiro:hasRole>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.errMsg.label" default="Err Msg" /></td>
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadFileInstance, field: "errMsg")}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.importDate.label" default="Import Date" /></td>
                            <td valign="top" class="value"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${receiverDownloadFileInstance?.importDate}" /></td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.name.label" default="Name" /></td>
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadFileInstance, field: "name")}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.requestingUser.label" default="Uploader" /></td>
                            <td valign="top" class="value"><g:link controller="person" action="show" id="${receiverDownloadFileInstance?.requestingUser?.id}">${receiverDownloadFileInstance?.requestingUser?.encodeAsHTML()}</g:link></td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.status.label" default="Status" /></td>
                            <td valign="top" class="value">${receiverDownloadFileInstance?.status?.encodeAsHTML()}</td>
                        </tr>
                    
                        <g:hiddenField name="status" value="${receiverDownloadFileInstance?.status}"/>
                        <g:hiddenField name="receiverDownloadId" value="${receiverDownloadFileInstance?.id}"/>
                        
                        <g:if test="${ (receiverDownloadFileInstance.status == FileProcessingStatus.PROCESSING) }">
	                        <tr class="prop">
	                            <td valign="top" class="name"><g:message code="receiverDownloadFile.percentComplete.label" default="Progress" /></td>
	                            <td valign="top" class="value"><div id="progressbar"></div></td>
	                        </tr>
                        </g:if>
                        
                        <g:if test="${   (receiverDownloadFileInstance.status != FileProcessingStatus.PROCESSING) 
                                      && (   (receiverDownloadFileInstance.type == ReceiverDownloadFileType.DETECTIONS_CSV)
                                          || (receiverDownloadFileInstance.type == ReceiverDownloadFileType.CSIRO_DETECTIONS_CSV))}">
                                      
                          <!-- Total -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.totalDetections.label" default="Total Detections" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.totalDetectionCount()}</td>
                          </tr>

                          <!-- Num valid -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.validCount.label" default="Valid Count" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.validDetectionCount()}</td>
                          </tr>

                          <!-- Num invalid -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.invalidCount.label" default="Invalid Count" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.invalidDetectionCount()}</td>
                          </tr>

                          <!-- Num duplicate -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.duplicateCount.label" default="Duplicate Count" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.invalidDetectionCount(InvalidDetectionReason.DUPLICATE)}</td>
                          </tr>

                          <!-- Num unknown receiver -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.unknownReceiverCount.label" default="Unknown Receiver Count" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.invalidDetectionCount(InvalidDetectionReason.UNKNOWN_RECEIVER)}</td>
                          </tr>

                          <!-- unknown receivers -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.unknownReceivers.label" default="Unknown Receivers" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.unknownReceivers()}</td>
                          </tr>

                          <!-- Num no deployment -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.noDeploymentsCount.label" default="No Deployment at Time  Count" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.invalidDetectionCount(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME)}</td>
                          </tr>

                          <!-- Num no recovery -->
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.noRecoveryCount.label" default="No Recovery at Time Count" /></td>
                              <td valign="top" class="value">${receiverDownloadFileInstance?.invalidDetectionCount(InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME)}</td>
                          </tr>
                          
                        </g:if>
                        

                    </tbody>
                </table>
            </div>
            <div class="buttons">
              
                <g:form>
                    <g:hiddenField name="id" value="${receiverDownloadFileInstance?.id}" />
                    <shiro:user>
                      <span class="button"><g:actionSubmit class="download" action="download" value="${message(code: 'default.button.download.label', default: 'Download')}" /></span>
                    </shiro:user>
                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>              
                </g:form>
            </div>
        </div>
    </body>
</html>
