

<%@ page import="au.org.emii.aatams.bulk.BulkImport" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bulkImport.label', default: 'BulkImport')}" />
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
            <g:hasErrors bean="${bulkImportInstance}">
            <div class="errors">
                <g:renderErrors bean="${bulkImportInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${bulkImportInstance?.id}" />
                <g:hiddenField name="version" value="${bulkImportInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="importFinishDate"><g:message code="bulkImport.importFinishDate.label" default="Import Finish Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportInstance, field: 'importFinishDate', 'errors')}">
                                    <g:datePicker name="importFinishDate" value="${bulkImportInstance?.importFinishDate}" noSelection="['':'']"></g:datePicker>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="importStartDate"><g:message code="bulkImport.importStartDate.label" default="Import Start Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportInstance, field: 'importStartDate', 'errors')}">
                                    <g:datePicker name="importStartDate" value="${bulkImportInstance?.importStartDate}" ></g:datePicker>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="organisation"><g:message code="bulkImport.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportInstance, field: 'organisation', 'errors')}">
                                    <g:select optionKey="id" from="${au.org.emii.aatams.Organisation.list()}" name="organisation.id" value="${bulkImportInstance?.organisation?.id}" ></g:select>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="records"><g:message code="bulkImport.records.label" default="Records" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportInstance, field: 'records', 'errors')}">
                                    
<ul>
<g:each var="r" in="${bulkImportInstance?.records?}">
    <li><g:link controller="bulkImportRecord" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="bulkImportRecord" params="['bulkImport.id':bulkImportInstance?.id]" action="create">Add BulkImportRecord</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="status"><g:message code="bulkImport.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportInstance, field: 'status', 'errors')}">
                                    <g:select  from="${au.org.emii.aatams.bulk.BulkImportStatus?.values()}" value="${bulkImportInstance?.status}" name="status" ></g:select>
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
