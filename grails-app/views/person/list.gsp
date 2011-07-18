
<%@ page import="au.org.emii.aatams.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
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
                        
                            <td/>
                            
                            <g:sortableColumn property="name" title="${message(code: 'person.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="username" title="${message(code: 'secUser.username.label', default: 'Username')}" />

                            <g:sortableColumn property="phoneNumber" title="${message(code: 'person.phoneNumber.label', default: 'Phone Number')}" />
                        
                            <g:sortableColumn property="emailAddress" title="${message(code: 'person.emailAddress.label', default: 'Email Address')}" />
                            
                            <g:sortableColumn property="organisation" title="${message(code: 'person.organisation.label', default: 'Organisation')}" />

                            <g:sortableColumn property="projects" title="${message(code: 'person.projects.label', default: 'Projects')}" />
                            
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${personInstanceList}" status="i" var="personInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${personInstance.id}"></g:link></td>
                    
                            <td>${fieldValue(bean: personInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: personInstance, field: "username")}</td>

                            <td>${fieldValue(bean: personInstance, field: "phoneNumber")}</td>
                        
                            <td>${fieldValue(bean: personInstance, field: "emailAddress")}</td>
                        
                            <td><g:link controller="organisation" action="show" id="${personInstance?.organisation?.id}">${fieldValue(bean: personInstance, field: "organisation")}</g:link></td>
                        
                            <td>${fieldValue(bean: personInstance, field: "projects")}</td>

                            </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${personInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
