

<%@ page import="au.org.emii.aatams.ReceiverDownloadFile" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
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
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${receiverDownloadFileInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverDownloadFileInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${receiverDownloadFileInstance?.id}" />
                <g:hiddenField name="version" value="${receiverDownloadFileInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="receiverDownloadFile.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'type', 'errors')}">
                                    <g:select  from="${au.org.emii.aatams.ReceiverDownloadFileType?.values()}" value="${receiverDownloadFileInstance?.type}" name="type" ></g:select>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="path"><g:message code="receiverDownloadFile.path.label" default="Path" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'path', 'errors')}">
                                    <input type="text" id="path" name="path" value="${fieldValue(bean:receiverDownloadFileInstance,field:'path')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="errMsg"><g:message code="receiverDownloadFile.errMsg.label" default="Err Msg" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'errMsg', 'errors')}">
                                    <input type="text" id="errMsg" name="errMsg" value="${fieldValue(bean:receiverDownloadFileInstance,field:'errMsg')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="importDate"><g:message code="receiverDownloadFile.importDate.label" default="Import Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'importDate', 'errors')}">
                                    <g:datePicker name="importDate" value="${receiverDownloadFileInstance?.importDate}" ></g:datePicker>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="receiverDownloadFile.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'name', 'errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:receiverDownloadFileInstance,field:'name')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="requestingUser"><g:message code="receiverDownloadFile.requestingUser.label" default="Requesting User" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'requestingUser', 'errors')}">
                                    <g:select optionKey="id" from="${au.org.emii.aatams.Person.list()}" name="requestingUser.id" value="${receiverDownloadFileInstance?.requestingUser?.id}" ></g:select>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="status"><g:message code="receiverDownloadFile.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'status', 'errors')}">
                                    <g:select  from="${au.org.emii.aatams.FileProcessingStatus?.values()}" value="${receiverDownloadFileInstance?.status}" name="status" ></g:select>
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
