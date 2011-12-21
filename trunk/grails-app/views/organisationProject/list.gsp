
<%@ page import="au.org.emii.aatams.OrganisationProject" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisationProject.label', default: 'OrganisationProject')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'organisationProject.id.label', default: 'Id')}" />
                        
                            <th><g:message code="organisationProject.organisation.label" default="Organisation" /></th>
                        
                            <th><g:message code="organisationProject.project.label" default="Project" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${organisationProjectInstanceList}" status="i" var="organisationProjectInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${organisationProjectInstance.id}">${fieldValue(bean: organisationProjectInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: organisationProjectInstance, field: "organisation")}</td>
                        
                            <td>${fieldValue(bean: organisationProjectInstance, field: "project")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${organisationProjectInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
