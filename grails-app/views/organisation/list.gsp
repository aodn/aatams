
<%@ page import="au.org.emii.aatams.Organisation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisation.label', default: 'Organisation')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>

    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:user>
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:user>
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
                        
                            <th></th>

                            <g:sortableColumn property="name" title="${message(code: 'organisation.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="department" title="${message(code: 'organisation.department.label', default: 'Department')}" />
                        
                            <g:sortableColumn property="phoneNumber" title="${message(code: 'organisation.phoneNumber.label', default: 'Phone Number')}" />
                        
                            <g:sortableColumn property="faxNumber" title="${message(code: 'organisation.faxNumber.label', default: 'Fax Number')}" />
                        
                            <g:sortableColumn property="streetAddress" title="${message(code: 'organisation.streetAddress.label', default: 'Street Address')}" />

                            <g:sortableColumn property="postalAddress" title="${message(code: 'organisation.postalAddress.label', default: 'Postal Address')}" />
                        
                            <shiro:hasRole name="SysAdmin">
                              <g:sortableColumn property="status" title="${message(code: 'organisation.status.label', default: 'Status')}" />
                              
                              <g:sortableColumn property="request.requester" title="${message(code: 'organisation.requester.label', default: 'Requester')}" />
                            </shiro:hasRole>
                            
                            <th>Projects</th>

                            <th>People</th>

                            </tr>
                    </thead>
                    <tbody>
                      <g:each in="${organisationInstanceList}" status="i" var="organisationInstance">
                          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                              <td class="rowButton"><g:link class="show" action="show" id="${organisationInstance.id}">.</g:link></td>
                      
                              <td>${fieldValue(bean: organisationInstance, field: "name")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "department")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "phoneNumber")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "faxNumber")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "streetAddress")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "postalAddress")}</td>

                              <shiro:hasRole name="SysAdmin">
                                <td>${fieldValue(bean: organisationInstance, field: "status")}</td>
                                
                                <td><g:link controller="person" action="show" id="${organisationInstance?.request?.requester?.id}">${fieldValue(bean: organisationInstance, field: "request")}</g:link></td>
                              </shiro:hasRole>
                              
                              <td>${fieldValue(bean: organisationInstance, field: "projects")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "people")}</td>

                          </tr>
                      </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${organisationInstanceTotal}" />
            </div>
        </div>
    </body>

</html>
