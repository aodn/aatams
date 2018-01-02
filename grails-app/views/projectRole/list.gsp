
<%@ page import="au.org.emii.aatams.ProjectRole" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'projectRole.label', default: 'ProjectRole')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'projectRole.id.label', default: 'Id')}" />
                        
                            <th><g:message code="projectRole.person.label" default="Person" /></th>
                        
                            <th><g:message code="projectRole.project.label" default="Project" /></th>
                        
                            <th><g:message code="projectRole.roleType.label" default="Role Type" /></th>

                            <g:sortableColumn property="access" title="${message(code: 'projectRole.access.label', default: 'Access')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${projectRoleInstanceList}" status="i" var="projectRoleInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${projectRoleInstance.id}">${fieldValue(bean: projectRoleInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: projectRoleInstance, field: "person")}</td>
                        
                            <td>${fieldValue(bean: projectRoleInstance, field: "project")}</td>
                        
                            <td>${fieldValue(bean: projectRoleInstance, field: "roleType")}</td>

                            <td>${fieldValue(bean: projectRoleInstance, field: "access")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${projectRoleInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
