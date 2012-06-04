
<%@ page import="au.org.emii.aatams.bulk.BulkImport" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bulkImport.label', default: 'BulkImport')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'bulkImport.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="importFinishDate" title="${message(code: 'bulkImport.importFinishDate.label', default: 'Import Finish Date')}" />
                        
                            <g:sortableColumn property="importStartDate" title="${message(code: 'bulkImport.importStartDate.label', default: 'Import Start Date')}" />
                        
                            <th><g:message code="bulkImport.organisation.label" default="Organisation" /></th>
                        
                            <g:sortableColumn property="status" title="${message(code: 'bulkImport.status.label', default: 'Status')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${bulkImportInstanceList}" status="i" var="bulkImportInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${bulkImportInstance.id}">${fieldValue(bean: bulkImportInstance, field: "id")}</g:link></td>
                        
                            <td><joda:format value="${bulkImportInstance.importFinishDate}" /></td>
                        
                            <td><joda:format value="${bulkImportInstance.importStartDate}" /></td>
                        
                            <td>${fieldValue(bean: bulkImportInstance, field: "organisation")}</td>
                        
                            <td>${fieldValue(bean: bulkImportInstance, field: "status")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${bulkImportInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
