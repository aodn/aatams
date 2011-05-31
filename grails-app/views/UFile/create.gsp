

<%@ page import="com.lucastex.grails.fileuploader.UFile" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'UFile.label', default: 'UFile')}" />
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
            <g:hasErrors bean="${UFileInstance}">
            <div class="errors">
                <g:renderErrors bean="${UFileInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="size"><g:message code="UFile.size.label" default="Size" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: UFileInstance, field: 'size', 'errors')}">
                                    <g:textField name="size" value="${fieldValue(bean: UFileInstance, field: 'size')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="path"><g:message code="UFile.path.label" default="Path" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: UFileInstance, field: 'path', 'errors')}">
                                    <g:textField name="path" value="${UFileInstance?.path}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="UFile.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: UFileInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${UFileInstance?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="extension"><g:message code="UFile.extension.label" default="Extension" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: UFileInstance, field: 'extension', 'errors')}">
                                    <g:textField name="extension" value="${UFileInstance?.extension}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateUploaded"><g:message code="UFile.dateUploaded.label" default="Date Uploaded" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: UFileInstance, field: 'dateUploaded', 'errors')}">
                                    <g:datePicker name="dateUploaded" precision="day" value="${UFileInstance?.dateUploaded}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="downloads"><g:message code="UFile.downloads.label" default="Downloads" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: UFileInstance, field: 'downloads', 'errors')}">
                                    <g:textField name="downloads" value="${fieldValue(bean: UFileInstance, field: 'downloads')}" />

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
