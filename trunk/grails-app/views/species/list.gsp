
<%@ page import="au.org.emii.aatams.Species" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'species.label', default: 'Species')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'species.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'species.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="embargoDate" title="${message(code: 'species.embargoDate.label', default: 'Embargo Date')}" />

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${speciesInstanceList}" status="i" var="speciesInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${speciesInstance.id}">${fieldValue(bean: speciesInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: speciesInstance, field: "name")}</td>
                        
                            <td><g:formatDate date="${speciesInstance.embargoDate}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${speciesInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
