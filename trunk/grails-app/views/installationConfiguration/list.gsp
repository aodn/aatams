
<%@ page import="au.org.emii.aatams.InstallationConfiguration" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'installationConfiguration.label', default: 'InstallationConfiguration')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'installationConfiguration.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="type" title="${message(code: 'installationConfiguration.type.label', default: 'Type')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${installationConfigurationInstanceList}" status="i" var="installationConfigurationInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${installationConfigurationInstance.id}">${fieldValue(bean: installationConfigurationInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: installationConfigurationInstance, field: "type")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${installationConfigurationInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
