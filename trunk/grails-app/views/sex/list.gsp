
<%@ page import="au.org.emii.aatams.Sex" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'sex.label', default: 'Sex')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'sex.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="sex" title="${message(code: 'sex.sex.label', default: 'Sex')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${sexInstanceList}" status="i" var="sexInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${sexInstance.id}">${fieldValue(bean: sexInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: sexInstance, field: "sex")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${sexInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
