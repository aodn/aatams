
<%@ page import="au.org.emii.aatams.Receiver" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiver.label', default: 'Receiver')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'receiver.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="codeName" title="${message(code: 'receiver.codeName.label', default: 'Code Name')}" />
                        
                            <g:sortableColumn property="serialNumber" title="${message(code: 'receiver.serialNumber.label', default: 'Serial Number')}" />
                        
                            <g:sortableColumn property="embargoDate" title="${message(code: 'receiver.embargoDate.label', default: 'Embargo Date')}" />
                        
                            <th><g:message code="receiver.status.label" default="Status" /></th>
                        
                            <th><g:message code="receiver.model.label" default="Model" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverInstanceList}" status="i" var="receiverInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${receiverInstance.id}">${fieldValue(bean: receiverInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: receiverInstance, field: "codeName")}</td>
                        
                            <td>${fieldValue(bean: receiverInstance, field: "serialNumber")}</td>
                        
                            <td><g:formatDate date="${receiverInstance.embargoDate}" /></td>
                        
                            <td>${fieldValue(bean: receiverInstance, field: "status")}</td>
                        
                            <td>${fieldValue(bean: receiverInstance, field: "model")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${receiverInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
