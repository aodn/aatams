
<%@ page import="au.org.emii.aatams.SystemRole" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'systemRole.label', default: 'SystemRole')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'systemRole.id.label', default: 'Id')}" />
                        
                            <th><g:message code="systemRole.roleType.label" default="Role Type" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${systemRoleInstanceList}" status="i" var="systemRoleInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${systemRoleInstance.id}">${fieldValue(bean: systemRoleInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: systemRoleInstance, field: "roleType")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${systemRoleInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
