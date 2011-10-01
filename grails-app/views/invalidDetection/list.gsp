
<%@ page import="au.org.emii.aatams.detection.InvalidDetection" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'invalidDetection.label', default: 'InvalidDetection')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'invalidDetection.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="receiverName" title="${message(code: 'invalidDetection.receiverName.label', default: 'Receiver Name')}" />
                        
                            <g:sortableColumn property="transmitterId" title="${message(code: 'invalidDetection.transmitterId.label', default: 'Transmitter ID')}" />

                            <g:sortableColumn property="transmitterName" title="${message(code: 'invalidDetection.transmitterName.label', default: 'Transmitter Name')}" />
                        
                            <g:sortableColumn property="transmitterSerialNumber" title="${message(code: 'invalidDetection.transmitterSerialNumber.label', default: 'Transmitter Serial Number')}" />
                        
                            <g:sortableColumn property="sensorValue" title="${message(code: 'invalidDetection.sensorValue.label', default: 'Sensor Value')}" />
                        
                            <g:sortableColumn property="sensorUnit" title="${message(code: 'invalidDetection.sensorUnit.label', default: 'Sensor Unit')}" />
                        
                            <g:sortableColumn property="stationName" title="${message(code: 'invalidDetection.stationName.label', default: 'Station Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${invalidDetectionInstanceList}" status="i" var="invalidDetectionInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${invalidDetectionInstance.id}">${fieldValue(bean: invalidDetectionInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: invalidDetectionInstance, field: "receiverName")}</td>

                            <td>${fieldValue(bean: invalidDetectionInstance, field: "transmitterId")}</td>
                        
                            <td>${fieldValue(bean: invalidDetectionInstance, field: "transmitterName")}</td>
                        
                            <td>${fieldValue(bean: invalidDetectionInstance, field: "transmitterSerialNumber")}</td>
                        
                            <td>${fieldValue(bean: invalidDetectionInstance, field: "sensorValue")}</td>
                        
                            <td>${fieldValue(bean: invalidDetectionInstance, field: "sensorUnit")}</td>
                        
                            <td>${fieldValue(bean: invalidDetectionInstance, field: "stationName")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${invalidDetectionInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
