
<%@ page import="au.org.emii.aatams.AnimalMeasurement" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalMeasurement.label', default: 'AnimalMeasurement')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'animalMeasurement.id.label', default: 'Id')}" />
                        
                            <th><g:message code="animalMeasurement.type.label" default="Type" /></th>
                        
                            <g:sortableColumn property="value" title="${message(code: 'animalMeasurement.value.label', default: 'Value')}" />
                        
                            <th><g:message code="animalMeasurement.unit.label" default="Unit" /></th>
                        
                            <g:sortableColumn property="estimate" title="${message(code: 'animalMeasurement.estimate.label', default: 'Estimate')}" />
                        
                            <g:sortableColumn property="comments" title="${message(code: 'animalMeasurement.comments.label', default: 'Comments')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${animalMeasurementInstanceList}" status="i" var="animalMeasurementInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${animalMeasurementInstance.id}">${fieldValue(bean: animalMeasurementInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: animalMeasurementInstance, field: "type")}</td>
                        
                            <td>${fieldValue(bean: animalMeasurementInstance, field: "value")}</td>
                        
                            <td>${fieldValue(bean: animalMeasurementInstance, field: "unit")}</td>
                        
                            <td><g:formatBoolean boolean="${animalMeasurementInstance.estimate}" /></td>
                        
                            <td>${fieldValue(bean: animalMeasurementInstance, field: "comments")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${animalMeasurementInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
