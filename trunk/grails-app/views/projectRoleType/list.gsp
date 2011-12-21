
<%@ page import="au.org.emii.aatams.ProjectRoleType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'projectRoleType.label', default: 'ProjectRoleType')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'projectRoleType.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="displayName" title="${message(code: 'projectRoleType.displayName.label', default: 'Display Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${projectRoleTypeInstanceList}" status="i" var="projectRoleTypeInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${projectRoleTypeInstance.id}">${fieldValue(bean: projectRoleTypeInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: projectRoleTypeInstance, field: "displayName")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${projectRoleTypeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
