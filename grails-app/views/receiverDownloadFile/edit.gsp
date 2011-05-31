

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
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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
                                  <label for="path"><g:message code="receiverDownloadFile.path.label" default="Path" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'path', 'errors')}">
                                    <g:textField name="path" value="${receiverDownloadFileInstance?.path}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="receiverDownloadFile.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'type', 'errors')}">
                                    <g:select name="type" from="${au.org.emii.aatams.ReceiverDownloadFileType?.values()}" keys="${au.org.emii.aatams.ReceiverDownloadFileType?.values()*.name()}" value="${receiverDownloadFileInstance?.type?.name()}"  />

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
