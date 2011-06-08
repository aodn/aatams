
<%@ page import="au.org.emii.aatams.upload.ProcessedUploadFile" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'processedUploadFile.label', default: 'ProcessedUploadFile')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'processedUploadFile.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="errMsg" title="${message(code: 'processedUploadFile.errMsg.label', default: 'Err Msg')}" />
                        
                            <g:sortableColumn property="status" title="${message(code: 'processedUploadFile.status.label', default: 'Status')}" />
                        
                            <th><g:message code="processedUploadFile.uFile.label" default="UF ile" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${processedUploadFileInstanceList}" status="i" var="processedUploadFileInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${processedUploadFileInstance.id}">${fieldValue(bean: processedUploadFileInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: processedUploadFileInstance, field: "errMsg")}</td>
                        
                            <td>${fieldValue(bean: processedUploadFileInstance, field: "status")}</td>
                        
                            <td>${fieldValue(bean: processedUploadFileInstance, field: "uFile")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${processedUploadFileInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
