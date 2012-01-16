
<%@ page import="au.org.emii.aatams.Sensor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tag.label', default: 'Tag')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /> (or Sensor)</g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
            <g:listFilter name="sensor" />
            
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <td/>
                            
                            <th><g:message code="transmitterId.label" default="ID" /></th>
                            
                            <g:sortableColumn property="tag.model" title="${message(code: 'tag.model.label', default: 'Model')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="tag.serialNumber" title="${message(code: 'tag.serialNumber.label', default: 'Serial Number')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="tag.project.name" title="${message(code: 'tag.project.name.label', default: 'Project')}" params="${executedFilter}"/>

                            <g:sortableColumn property="tag.codeMap" title="${message(code: 'tag.codeMap.label', default: 'Code Map')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="pingCode" title="${message(code: 'pingCode.label', default: 'Ping ID Code')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="transmitterType" title="${message(code: 'transmitterType.label', default: 'Transmitter Type')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="tag.status" title="${message(code: 'tag.status.label', default: 'Status')}" params="${executedFilter}"/>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${entityList}" status="i" var="sensorInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton">
                              <g:link class="show" controller="tag" action="show" id="${sensorInstance.tag.id}"></g:link>
                            </td>
                            
                            <td>${fieldValue(bean: sensorInstance, field: "transmitterId")}</td>
                            
                            <td>${fieldValue(bean: sensorInstance.tag, field: "model")}</td>
                            
                            <td>${fieldValue(bean: sensorInstance.tag, field: "serialNumber")}</td>
                        
                            <td><g:link controller="project" action="show" id="${sensorInstance?.tag?.project?.id}">${fieldValue(bean: sensorInstance?.tag, field: "project")}</g:link></td>

                            <td>${fieldValue(bean: sensorInstance.tag, field: "codeMap")}</td>
                        
                            <td>${sensorInstance.pingCode}</td>

                            <td>${fieldValue(bean: sensorInstance, field: "transmitterType")}</td>
                        
                            <td>${fieldValue(bean: sensorInstance.tag, field: "status")}</td>

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
