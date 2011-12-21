
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
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
            <g:listFilter name="installation" />

            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <td/>
                            
                            <g:sortableColumn property="name" 
                                              title="${message(code: 'installation.name.label', default: 'Name')}" 
                                              params="${executedFilter}" />
                        
                            <th><g:message code="installation.configuration.label" default="Configuration" /></th>
                        
                            <g:sortableColumn property="project.name" title="${message(code: 'installation.project.label', default: 'Project')}" params="${executedFilter}" />
                        
                            <th><g:message code="installation.stationCount.label" default="No. Stations" /></th>
                            
                            <th><g:message code="installation.stations.label" default="Stations" /></th>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${entityList}" status="i" var="installationInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${installationInstance.id}">.</g:link></td>
                    
                            <td>${fieldValue(bean: installationInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: installationInstance, field: "configuration")}</td>
                        
                            <td>${fieldValue(bean: installationInstance, field: "project")}</td>
                        
                            <td>${installationInstance?.stations.size()}</td>
                            
                            <td>${installationInstance?.stations}</td>

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
