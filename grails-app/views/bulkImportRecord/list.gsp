
<%@ page import="au.org.emii.aatams.bulk.BulkImportRecord" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bulkImportRecord.label', default: 'BulkImportRecord')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'bulkImportRecord.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="dstClass" title="${message(code: 'bulkImportRecord.dstClass.label', default: 'Dst Class')}" />
                        
                            <g:sortableColumn property="dstPk" title="${message(code: 'bulkImportRecord.dstPk.label', default: 'Dst Pk')}" />
                        
                            <th><g:message code="bulkImportRecord.bulkImport.label" default="Bulk Import" /></th>
                        
                            <g:sortableColumn property="srcModifiedDate" title="${message(code: 'bulkImportRecord.srcModifiedDate.label', default: 'Src Modified Date')}" />
                        
                            <g:sortableColumn property="srcPk" title="${message(code: 'bulkImportRecord.srcPk.label', default: 'Src Pk')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${bulkImportRecordInstanceList}" status="i" var="bulkImportRecordInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${bulkImportRecordInstance.id}">${fieldValue(bean: bulkImportRecordInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: bulkImportRecordInstance, field: "dstClass")}</td>
                        
                            <td>${fieldValue(bean: bulkImportRecordInstance, field: "dstPk")}</td>
                        
                            <td>${fieldValue(bean: bulkImportRecordInstance, field: "bulkImport")}</td>
                        
                            <td>${fieldValue(bean: bulkImportRecordInstance, field: "srcModifiedDate")}</td>
                        
                            <td>${fieldValue(bean: bulkImportRecordInstance, field: "srcPk")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${bulkImportRecordInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
