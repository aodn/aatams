
<%@ page import="au.org.emii.aatams.bulk.BulkImportRecord" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bulkImportRecord.label', default: 'BulkImportRecord')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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
                            <td valign="top" class="name"><g:message code="bulkImportRecord.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: bulkImportRecordInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="bulkImportRecord.dstClass.label" default="Dst Class" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: bulkImportRecordInstance, field: "dstClass")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="bulkImportRecord.dstPk.label" default="Dst Pk" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: bulkImportRecordInstance, field: "dstPk")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="bulkImportRecord.bulkImport.label" default="Bulk Import" /></td>
                            
                            <td valign="top" class="value"><g:link controller="bulkImport" action="show" id="${bulkImportRecordInstance?.bulkImport?.id}">${bulkImportRecordInstance?.bulkImport?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="bulkImportRecord.srcModifiedDate.label" default="Src Modified Date" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: bulkImportRecordInstance, field: "srcModifiedDate")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="bulkImportRecord.srcPk.label" default="Src Pk" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: bulkImportRecordInstance, field: "srcPk")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="bulkImportRecord.srcTable.label" default="Src Table" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: bulkImportRecordInstance, field: "srcTable")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="bulkImportRecord.type.label" default="Type" /></td>
                            
                            <td valign="top" class="value">${bulkImportRecordInstance?.type?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${bulkImportRecordInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
