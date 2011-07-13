
<%@ page import="au.org.emii.aatams.Project" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasRole name="SysAdmin">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasRole>
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
                        
                            <th></th>

                            <g:sortableColumn property="name" title="${message(code: 'project.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'project.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="organisations" title="${message(code: 'project.organisations.label', default: 'Organisations')}" />

                            <g:sortableColumn property="principalInvestigator" title="${message(code: 'project.principalInvestigator.label', default: 'Principal Investigator')}" />
                        
                            <g:sortableColumn property="people" title="${message(code: 'project.people.label', default: 'People')}" />

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${projectInstanceList}" status="i" var="projectInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${projectInstance.id}"></g:link></td>
                    
                            <td>${fieldValue(bean: projectInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: projectInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: projectInstance, field: "organisations")}</td>

                            <td>${fieldValue(bean: projectInstance, field: "principalInvestigators")}</td>

                            <td>${fieldValue(bean: projectInstance, field: "people")}</td>
                            
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${projectInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
