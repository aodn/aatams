
<%@ page import="au.org.emii.aatams.ReceiverRecovery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverRecovery.label', default: 'ReceiverRecovery')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'receiverRecovery.id.label', default: 'Id')}" />
                        
                            <th><g:message code="receiverRecovery.deployment.label" default="Deployment" /></th>
                        
                            <th><g:message code="receiverRecovery.download.label" default="Download" /></th>
                        
                            <g:sortableColumn property="location" title="${message(code: 'receiverRecovery.location.label', default: 'Location')}" />
                        
                            <g:sortableColumn property="recoveryDate" title="${message(code: 'receiverRecovery.recoveryDate.label', default: 'Recovery Date')}" />
                        
                            <th><g:message code="receiverRecovery.status.label" default="Status" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverRecoveryInstanceList}" status="i" var="receiverRecoveryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${receiverRecoveryInstance.id}">${fieldValue(bean: receiverRecoveryInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: receiverRecoveryInstance, field: "deployment")}</td>
                        
                            <td>${fieldValue(bean: receiverRecoveryInstance, field: "download")}</td>
                        
                            <td>${fieldValue(bean: receiverRecoveryInstance, field: "location")}</td>
                        
                            <td><g:formatDate date="${receiverRecoveryInstance.recoveryDate}" /></td>
                        
                            <td>${fieldValue(bean: receiverRecoveryInstance, field: "status")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${receiverRecoveryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
