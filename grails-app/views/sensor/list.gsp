
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
                        
                            <th><g:message code="sensor.tag.label" default="Tag" /></th>
                        
                            <g:sortableColumn property="codeMap" title="${message(code: 'sensor.codeMap.label', default: 'Code Map')}" />
                        
                            <g:sortableColumn property="pingCode" title="${message(code: 'sensor.pingCode.label', default: 'Ping Code')}" />
                        
                            <th><g:message code="sensor.transmitterType.label" default="Transmitter Type" /></th>
                        
                            <g:sortableColumn property="unit" title="${message(code: 'sensor.unit.label', default: 'Unit')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${sensorInstanceList}" status="i" var="sensorInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${sensorInstance.id}">${fieldValue(bean: sensorInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "tag")}</td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "codeMap")}</td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "pingCode")}</td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "transmitterType")}</td>
                        
                            <td>${fieldValue(bean: sensorInstance, field: "unit")}</td>
                        
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
