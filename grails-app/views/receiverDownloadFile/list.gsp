
<%@ page import="au.org.emii.aatams.FileProcessingStatus; au.org.emii.aatams.ReceiverDownloadFile" %>
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
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" controller="detection" action="create"><g:message code="default.upload.label" args="['Tag Detection']" /></g:link></span>
              <span class="menuButton"><g:link class="create" controller="receiverEvent" action="create"><g:message code="default.upload.label" args="['Receiver Event']" /></g:link></span>
            </shiro:hasPermission>
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
                              <g:column property="path" title="${message(code: 'receiverDownloadFile.path.label', default: 'Path')}" />
                            </shiro:hasRole>
                            
                            <g:sortableColumn property="status" title="${message(code: 'receiverDownloadFile.status.label', default: 'Status')}" />
                            
                            <g:sortableColumn property="errMsg" title="${message(code: 'receiverDownloadFile.errMsg.label', default: 'Err Msg')}" />
                        
                            <g:sortableColumn property="importDate" title="${message(code: 'receiverDownloadFile.importDate.label', default: 'Import Date')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'receiverDownloadFile.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="requestingUser" title="${message(code: 'receiverDownloadFile.requestingUser.label', default: 'Uploader')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDownloadFileInstanceList}" status="i" var="receiverDownloadFileInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td class="rowButton">
                                <g:if test="${(receiverDownloadFileInstance.status != FileProcessingStatus.DELETING)}">
                                    <g:link class="show" action="show" id="${receiverDownloadFileInstance.id}">.</g:link>
                                </g:if>
                            </td>

                            <td>${fieldValue(bean: receiverDownloadFileInstance, field: "type")}</td>
                        
                            <shiro:hasRole name="SysAdmin">
                              <td>${fieldValue(bean: receiverDownloadFileInstance, field: "path")}</td>
                            </shiro:hasRole>
                        
                            <td>${receiverDownloadFileInstance?.status?.encodeAsHTML()}</td>
                        
                            <td>${fieldValue(bean: receiverDownloadFileInstance, field: "errMsg")}</td>
                        
                            <td><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${receiverDownloadFileInstance.importDate}" /></td>
                        
                            <td>${fieldValue(bean: receiverDownloadFileInstance, field: "name")}</td>
                        
                            <td><g:link controller="person" action="show" id="${receiverDownloadFileInstance?.requestingUser?.id}">${receiverDownloadFileInstance?.requestingUser?.encodeAsHTML()}</g:link></td>
                        
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
