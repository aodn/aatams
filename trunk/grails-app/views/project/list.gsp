
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
            <shiro:user>
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:user>
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
                        
                            <th><g:message code="project.organisations.label" default="Organisations" />

                            <th><g:message code="project.principalInvestigators.label" default="Principal Investigator" />
                        
                            <th><g:message code="project.people.label" default="People" />

                            <shiro:hasRole name="SysAdmin">
                              <g:sortableColumn property="status" title="${message(code: 'project.status.label', default: 'Status')}" />
                              
                              <g:sortableColumn property="requestingUser" title="${message(code: 'project.requester.label', default: 'Requester')}" />
                            </shiro:hasRole>
                            
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${projectInstanceList}" status="i" var="projectInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${projectInstance.id}">.</g:link></td>
                    
                            <td>${fieldValue(bean: projectInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: projectInstance, field: "organisations")}</td>

                            <td>${fieldValue(bean: projectInstance, field: "principalInvestigators")}</td>

                            <td>${fieldValue(bean: projectInstance, field: "people")}</td>
                            
                            <shiro:hasRole name="SysAdmin">
                                <td>${fieldValue(bean: projectInstance, field: "status")}</td>
                                
                                <td><g:link controller="person" action="show" id="${projectInstance?.requestingUser?.id}">${fieldValue(bean: projectInstance, field: "requestingUser")}</g:link></td>
                            </shiro:hasRole>
                              
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
