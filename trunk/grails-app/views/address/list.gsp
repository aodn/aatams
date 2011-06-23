
<%@ page import="au.org.emii.aatams.Address" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'address.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="streetAddress" title="${message(code: 'address.streetAddress.label', default: 'Street Address')}" />
                        
                            <g:sortableColumn property="suburbTown" title="${message(code: 'address.suburbTown.label', default: 'Suburb Town')}" />
                        
                            <g:sortableColumn property="state" title="${message(code: 'address.state.label', default: 'State')}" />
                        
                            <g:sortableColumn property="postcode" title="${message(code: 'address.postcode.label', default: 'Postcode')}" />
                        
                            <g:sortableColumn property="country" title="${message(code: 'address.country.label', default: 'Country')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${addressInstanceList}" status="i" var="addressInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${addressInstance.id}">${fieldValue(bean: addressInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: addressInstance, field: "streetAddress")}</td>
                        
                            <td>${fieldValue(bean: addressInstance, field: "suburbTown")}</td>
                        
                            <td>${fieldValue(bean: addressInstance, field: "state")}</td>
                        
                            <td>${fieldValue(bean: addressInstance, field: "postcode")}</td>
                        
                            <td>${fieldValue(bean: addressInstance, field: "country")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${addressInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
