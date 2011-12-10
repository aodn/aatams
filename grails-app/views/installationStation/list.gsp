
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
            
            <g:listFilter name="installationStation" />
            
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <td/>
                            
                            <g:sortableColumn property="name" title="${message(code: 'installationStation.name.label', default: 'Name')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="curtainPosition" title="${message(code: 'installationStation.curtainPosition.label', default: 'Curtain Position')}" params="${executedFilter}"/>
                        
                            <th><g:message code="installationStation.location.label" default="Location" /></th>
                        
                            <g:sortableColumn property="installation" title="${message(code: 'installationStation.installation.label', default: 'Installation')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="installation.project" title="${message(code: 'installationStation.installation.project.label', default: 'Project')}" params="${executedFilter}"/>

                            <th><g:message code="installationStation.active.label" default="Active" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${entityList}" status="i" var="installationStationInstance">
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
                    
                            <td><g:formatBoolean boolean="${installationStationInstance?.active}" true="yes" false="no" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${total}" params="${executedFilter}"/>
            </div>
        </div>
    </body>
</html>
