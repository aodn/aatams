

<%@ page import="au.org.emii.aatams.upload.ProcessedUploadFile" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${processedUploadFileInstance}">
            <div class="errors">
                <g:renderErrors bean="${processedUploadFileInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="errMsg"><g:message code="processedUploadFile.errMsg.label" default="Err Msg" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: processedUploadFileInstance, field: 'errMsg', 'errors')}">
                                    <g:textField name="errMsg" value="${processedUploadFileInstance?.errMsg}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="status"><g:message code="processedUploadFile.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: processedUploadFileInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${au.org.emii.aatams.upload.FileProcessingStatus?.values()}" keys="${au.org.emii.aatams.upload.FileProcessingStatus?.values()*.name()}" value="${processedUploadFileInstance?.status?.name()}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="uFile"><g:message code="processedUploadFile.uFile.label" default="UF ile" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: processedUploadFileInstance, field: 'uFile', 'errors')}">
                                    <g:select name="uFile.id" from="${com.lucastex.grails.fileuploader.UFile.list()}" optionKey="id" value="${processedUploadFileInstance?.uFile?.id}"  />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
