
<%@ page import="au.org.emii.aatams.ReceiverDownloadFile" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile')}" />
        <g:set var="projectId" value="${receiverDownloadFileInstance?.receiverDownload?.receiverRecovery?.deployment?.station?.installation?.project?.id}" />
               
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasRole name="SysAdmin">
              <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            </shiro:hasRole>
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
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadFileInstance, field: "name")}</td>
                            
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.receiverDownload.receiverRecovery.label" default="Receiver Recovery" /></td>
                            <td valign="top" class="value">
                              <g:link controller="receiverRecovery" 
                                      action="show" 
                                      id="${receiverDownloadFileInstance?.receiverDownload?.receiverRecovery?.id}">${receiverDownloadFileInstance?.receiverDownload?.receiverRecovery?.encodeAsHTML()}</g:link>
                            </td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.type.label" default="Type" /></td>
                            
                            <td valign="top" class="value">${receiverDownloadFileInstance?.type?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.status.label" default="Status" /></td>
                            
                            <td valign="top" class="value">${receiverDownloadFileInstance?.status?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownloadFile.errMsg.label" default="Error" /></td>
                            
                            <td valign="top" class="value">${receiverDownloadFileInstance?.errMsg}</td>
                            
                        </tr>
                    
                        <shiro:hasRole name="SysAdmin">
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="receiverDownloadFile.path.label" default="Path" /></td>

                              <td valign="top" class="value">${fieldValue(bean: receiverDownloadFileInstance, field: "path")}</td>

                          </tr>
                        </shiro:hasRole>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${receiverDownloadFileInstance?.id}" />
                    <g:hiddenField name="projectId" value="${projectId}" />
                    
                    <shiro:hasPermission permission="project:${projectId}:read">
                      <span class="button"><g:actionSubmit class="download" action="download" value="${message(code: 'default.button.download.label', default: 'Download')}" /></span>
                    </shiro:hasPermission>
                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </g:form>
            </div>
        </div>
    </body>
</html>
