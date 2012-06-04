

<%@ page import="au.org.emii.aatams.bulk.BulkImportRecord" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bulkImportRecord.label', default: 'BulkImportRecord')}" />
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
            <g:hasErrors bean="${bulkImportRecordInstance}">
            <div class="errors">
                <g:renderErrors bean="${bulkImportRecordInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${bulkImportRecordInstance?.id}" />
                <g:hiddenField name="version" value="${bulkImportRecordInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dstClass"><g:message code="bulkImportRecord.dstClass.label" default="Dst Class" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportRecordInstance, field: 'dstClass', 'errors')}">
                                    <input type="text" id="dstClass" name="dstClass" value="${fieldValue(bean:bulkImportRecordInstance,field:'dstClass')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dstPk"><g:message code="bulkImportRecord.dstPk.label" default="Dst Pk" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportRecordInstance, field: 'dstPk', 'errors')}">
                                    <input type="text" id="dstPk" name="dstPk" value="${fieldValue(bean:bulkImportRecordInstance,field:'dstPk')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="bulkImport"><g:message code="bulkImportRecord.bulkImport.label" default="Bulk Import" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportRecordInstance, field: 'bulkImport', 'errors')}">
                                    <g:select optionKey="id" from="${au.org.emii.aatams.bulk.BulkImport.list()}" name="bulkImport.id" value="${bulkImportRecordInstance?.bulkImport?.id}" ></g:select>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="srcModifiedDate"><g:message code="bulkImportRecord.srcModifiedDate.label" default="Src Modified Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportRecordInstance, field: 'srcModifiedDate', 'errors')}">
                                    <joda:dateTimePicker name="srcModifiedDate" value="${bulkImportRecordInstance?.srcModifiedDate}" ></joda:dateTimePicker>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="srcPk"><g:message code="bulkImportRecord.srcPk.label" default="Src Pk" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportRecordInstance, field: 'srcPk', 'errors')}">
                                    <input type="text" id="srcPk" name="srcPk" value="${fieldValue(bean:bulkImportRecordInstance,field:'srcPk')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="srcTable"><g:message code="bulkImportRecord.srcTable.label" default="Src Table" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportRecordInstance, field: 'srcTable', 'errors')}">
                                    <input type="text" id="srcTable" name="srcTable" value="${fieldValue(bean:bulkImportRecordInstance,field:'srcTable')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="bulkImportRecord.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bulkImportRecordInstance, field: 'type', 'errors')}">
                                    <g:select  from="${au.org.emii.aatams.bulk.BulkImportRecordType?.values()}" value="${bulkImportRecordInstance?.type}" name="type" ></g:select>
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
