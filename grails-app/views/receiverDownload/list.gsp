
<%@ page import="au.org.emii.aatams.ReceiverDownload" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownload.label', default: 'ReceiverDownload')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'receiverDownload.id.label', default: 'Id')}" />
                        
                            <th><g:message code="receiverDownload.receiverRecovery.label" default="Receiver Recovery" /></th>
                        
                            <g:sortableColumn property="downloadDateTime" title="${message(code: 'receiverDownload.downloadDateTime.label', default: 'Download Date')}" />
                        
                            <g:sortableColumn property="clockDrift" title="${message(code: 'receiverDownload.clockDrift.label', default: 'Clock Drift')}" />
                        
                            <g:sortableColumn property="pingCount" title="${message(code: 'receiverDownload.pingCount.label', default: 'Ping Count')}" />
                        
                            <g:sortableColumn property="detectionCount" title="${message(code: 'receiverDownload.detectionCount.label', default: 'Detection Count')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDownloadInstanceList}" status="i" var="receiverDownloadInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${receiverDownloadInstance.id}">${fieldValue(bean: receiverDownloadInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: receiverDownloadInstance, field: "receiverRecovery")}</td>
                        
                            <td><joda:format value="${receiverDownloadInstance.downloadDateTime}" /></td>
                        
                            <td>${fieldValue(bean: receiverDownloadInstance, field: "clockDrift")}</td>
                        
                            <td>${fieldValue(bean: receiverDownloadInstance, field: "pingCount")}</td>
                        
                            <td>${fieldValue(bean: receiverDownloadInstance, field: "detectionCount")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${receiverDownloadInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
