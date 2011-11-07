
<%@ page import="au.org.emii.aatams.DetectionSurgery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'detectionSurgery.label', default: 'DetectionSurgery')}" />
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
                        
                            <th/>
                        
                            <th><g:message code="detectionSurgery.detection.label" default="Detection" /></th>
                        
                            <th><g:message code="detectionSurgery.surgery.label" default="Surgery" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${detectionSurgeryInstanceList}" status="i" var="detectionSurgeryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${detectionSurgeryInstance.id}">.</g:link></td>
                        
                            <td>${fieldValue(bean: detectionSurgeryInstance, field: "detection")}</td>
                        
                            <td>${fieldValue(bean: detectionSurgeryInstance, field: "surgery")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${detectionSurgeryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
