
<%@ page import="au.org.emii.aatams.MeasurementUnit" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'measurementUnit.label', default: 'MeasurementUnit')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'measurementUnit.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="unit" title="${message(code: 'measurementUnit.unit.label', default: 'Unit')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${measurementUnitInstanceList}" status="i" var="measurementUnitInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${measurementUnitInstance.id}">${fieldValue(bean: measurementUnitInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: measurementUnitInstance, field: "unit")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${measurementUnitInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
