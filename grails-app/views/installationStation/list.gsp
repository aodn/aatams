
<%@ page import="au.org.emii.aatams.InstallationStation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'installationStation.label', default: 'InstallationStation')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
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
                            
                            <g:sortableColumn property="name" title="${message(code: 'installationStation.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="curtainPosition" title="${message(code: 'installationStation.curtainPosition.label', default: 'Curtain Position')}" />
                        
                            <th><g:message code="installationStation.location.label" default="Location" /></th>
                        
                            <g:sortableColumn property="installation" title="${message(code: 'installationStation.installation.label', default: 'Installation')}" />
                        
                            <g:sortableColumn property="installation.project" title="${message(code: 'installationStation.installation.project.label', default: 'Project')}" />

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${installationStationInstanceList}" status="i" var="installationStationInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${installationStationInstance.id}">.</g:link></td>
                    
                            <td>${fieldValue(bean: installationStationInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: installationStationInstance, field: "curtainPosition")}</td>
                        
                            <td>
                              <g:point name="scrambledLocation" 
                                       value="${installationStationInstance?.scrambledLocation}"/>
                            </td>
                        
                            <td><g:link controller="installation" action="show" id="${installationStationInstance?.installation?.id}">${fieldValue(bean: installationStationInstance?.installation, field: "name")}</g:link></td>

                            <td><g:link controller="project" action="show" id="${installationStationInstance?.installation?.project?.id}">${fieldValue(bean: installationStationInstance?.installation?.project, field: "name")}</g:link></td>
                    
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${installationStationInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
