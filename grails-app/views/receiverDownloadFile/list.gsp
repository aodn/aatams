
<%@ page import="au.org.emii.aatams.ReceiverDownloadFile" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
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
                        
                            <td/>
                        
                            <g:sortableColumn property="type" title="${message(code: 'receiverDownloadFile.type.label', default: 'Type')}" />
                        
                            <shiro:hasRole name="SysAdmin">
                              <g:sortableColumn property="path" title="${message(code: 'receiverDownloadFile.path.label', default: 'Path')}" />
                            </shiro:hasRole>
                            
                            <g:sortableColumn property="errMsg" title="${message(code: 'receiverDownloadFile.errMsg.label', default: 'Err Msg')}" />
                        
                            <g:sortableColumn property="importDate" title="${message(code: 'receiverDownloadFile.importDate.label', default: 'Import Date')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'receiverDownloadFile.name.label', default: 'Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDownloadFileInstanceList}" status="i" var="receiverDownloadFileInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${receiverDownloadFileInstance.id}">.</g:link></td>
                        
                            <td>${fieldValue(bean: receiverDownloadFileInstance, field: "type")}</td>
                        
                            <shiro:hasRole name="SysAdmin">
                              <td>${fieldValue(bean: receiverDownloadFileInstance, field: "path")}</td>
                            </shiro:hasRole>
                        
                            <td>${fieldValue(bean: receiverDownloadFileInstance, field: "errMsg")}</td>
                        
                            <td><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${receiverDownloadFileInstance.importDate}" /></td>
                        
                            <td>${fieldValue(bean: receiverDownloadFileInstance, field: "name")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${receiverDownloadFileInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
