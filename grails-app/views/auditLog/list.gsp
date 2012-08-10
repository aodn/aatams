
<%@ page import="au.org.emii.aatams.AuditLog" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'auditLog.label', default: 'Activity')}" />
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
                            
                            <g:sortableColumn property="dateCreated" title="${message(code: 'auditLog.action.label', default: 'Date')}" />
                            
                            <g:sortableColumn property="description" title="${message(code: 'auditLog.description.label', default: 'Item')}" />
                        
                            <g:sortableColumn property="action" title="${message(code: 'auditLog.action.label', default: 'Action')}" />
                        
		                    <shiro:hasRole name="SysAdmin">
                                <g:sortableColumn property="person" title="${message(code: 'auditLog.person.label', default: 'Person')}" />
		                    </shiro:hasRole>
                            
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${auditLogInstanceList}" status="i" var="auditLogInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton">show if entity still exists</td>
                    
                            <td>${fieldValue(bean: auditLogInstance, field: "dateCreated")}</td>
                            
                            <td>${fieldValue(bean: auditLogInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: auditLogInstance, field: "action")}</td>
                        
                            <shiro:hasRole name="SysAdmin">
                                <td>${fieldValue(bean: auditLogInstance, field: "person")}</td>
                            </shiro:hasRole>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${auditLogInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
