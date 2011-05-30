
<%@ page import="au.org.emii.aatams.Surgery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'surgery.label', default: 'Surgery')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'surgery.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="comments" title="${message(code: 'surgery.comments.label', default: 'Comments')}" />
                        
                            <th><g:message code="surgery.release.label" default="Release" /></th>
                        
                            <th><g:message code="surgery.surgeon.label" default="Surgeon" /></th>
                        
                            <g:sortableColumn property="sutures" title="${message(code: 'surgery.sutures.label', default: 'Sutures')}" />
                        
                            <th><g:message code="surgery.tag.label" default="Tag" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${surgeryInstanceList}" status="i" var="surgeryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${surgeryInstance.id}">${fieldValue(bean: surgeryInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: surgeryInstance, field: "comments")}</td>
                        
                            <td>${fieldValue(bean: surgeryInstance, field: "release")}</td>
                        
                            <td>${fieldValue(bean: surgeryInstance, field: "surgeon")}</td>
                        
                            <td><g:formatBoolean boolean="${surgeryInstance.sutures}" /></td>
                        
                            <td>${fieldValue(bean: surgeryInstance, field: "tag")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${surgeryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
