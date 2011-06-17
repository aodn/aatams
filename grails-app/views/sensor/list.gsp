
<%@ page import="au.org.emii.aatams.Sensor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'sensor.label', default: 'Sensor')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'sensor.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="codeName" title="${message(code: 'sensor.codeName.label', default: 'Code Name')}" />
                        
                            <g:sortableColumn property="serialNumber" title="${message(code: 'sensor.serialNumber.label', default: 'Serial Number')}" />
                        
                            <g:sortableColumn property="embargoDate" title="${message(code: 'sensor.embargoDate.label', default: 'Embargo Date')}" />
                        
                            <th><g:message code="sensor.status.label" default="Status" /></th>
                        
                            <g:sortableColumn property="codeMap" title="${message(code: 'sensor.codeMap.label', default: 'Code Map')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${sensorInstanceList}" status="i" var="sensorInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${sensorInstance.id}">${fieldValue(bean: sensorInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "codeName")}</td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "serialNumber")}</td>
                        
                            <td><g:formatDate date="${sensorInstance.embargoDate}" /></td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "status")}</td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "codeMap")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${sensorInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
