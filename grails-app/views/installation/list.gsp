
<%@ page import="au.org.emii.aatams.Installation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'installation.label', default: 'Installation')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'installation.id.label', default: 'Id')}" />
                        
                            <th><g:message code="installation.configuration.label" default="Configuration" /></th>
                        
                            <g:sortableColumn property="latOffset" title="${message(code: 'installation.latOffset.label', default: 'Lat Offset')}" />
                        
                            <g:sortableColumn property="lonOffset" title="${message(code: 'installation.lonOffset.label', default: 'Lon Offset')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'installation.name.label', default: 'Name')}" />
                        
                            <th><g:message code="installation.project.label" default="Project" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${installationInstanceList}" status="i" var="installationInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${installationInstance.id}">${fieldValue(bean: installationInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: installationInstance, field: "configuration")}</td>
                        
                            <td>${fieldValue(bean: installationInstance, field: "latOffset")}</td>
                        
                            <td>${fieldValue(bean: installationInstance, field: "lonOffset")}</td>
                        
                            <td>${fieldValue(bean: installationInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: installationInstance, field: "project")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${installationInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
