
<%@ page import="au.org.emii.aatams.AnimalRelease" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalRelease.label', default: 'AnimalRelease')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'animalRelease.id.label', default: 'Id')}" />
                        
                            <th><g:message code="animalRelease.animal.label" default="Animal" /></th>
                        
                            <g:sortableColumn property="captureDateTime" title="${message(code: 'animalRelease.captureDateTime.label', default: 'Capture Date Time')}" />
                        
                            <g:sortableColumn property="captureLocality" title="${message(code: 'animalRelease.captureLocality.label', default: 'Capture Locality')}" />
                        
                            <g:sortableColumn property="captureLocation" title="${message(code: 'animalRelease.captureLocation.label', default: 'Capture Location')}" />
                        
                            <g:sortableColumn property="comments" title="${message(code: 'animalRelease.comments.label', default: 'Comments')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${animalReleaseInstanceList}" status="i" var="animalReleaseInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${animalReleaseInstance.id}">${fieldValue(bean: animalReleaseInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: animalReleaseInstance, field: "animal")}</td>
                        
                            <td><g:formatDate date="${animalReleaseInstance.captureDateTime}" /></td>
                        
                            <td>${fieldValue(bean: animalReleaseInstance, field: "captureLocality")}</td>
                        
                            <td>${fieldValue(bean: animalReleaseInstance, field: "captureLocation")}</td>
                        
                            <td>${fieldValue(bean: animalReleaseInstance, field: "comments")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${animalReleaseInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
