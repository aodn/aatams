
<%@ page import="au.org.emii.aatams.Tag" %>
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'tag.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="codeName" title="${message(code: 'tag.codeName.label', default: 'Code Name')}" />
                        
                            <g:sortableColumn property="serialNumber" title="${message(code: 'tag.serialNumber.label', default: 'Serial Number')}" />
                        
                            <g:sortableColumn property="embargoDate" title="${message(code: 'tag.embargoDate.label', default: 'Embargo Date')}" />
                        
                            <g:sortableColumn property="codeMap" title="${message(code: 'tag.codeMap.label', default: 'Code Map')}" />
                        
                            <g:sortableColumn property="pingCode" title="${message(code: 'tag.pingCode.label', default: 'Ping Code')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${tagInstanceList}" status="i" var="tagInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${tagInstance.id}">${fieldValue(bean: tagInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: tagInstance, field: "codeName")}</td>
                        
                            <td>${fieldValue(bean: tagInstance, field: "serialNumber")}</td>
                        
                            <td><g:formatDate date="${tagInstance.embargoDate}" /></td>
                        
                            <td>${fieldValue(bean: tagInstance, field: "codeMap")}</td>
                        
                            <td>${fieldValue(bean: tagInstance, field: "pingCode")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${tagInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
