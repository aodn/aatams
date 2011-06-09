
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
                        
<!--                            <g:sortableColumn property="id" title="${message(code: 'organisation.id.label', default: 'Id')}" />-->
                        
                            <g:sortableColumn property="name" title="${message(code: 'organisation.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="phoneNumber" title="${message(code: 'organisation.phoneNumber.label', default: 'Phone Number')}" />
                        
                            <g:sortableColumn property="faxNumber" title="${message(code: 'organisation.faxNumber.label', default: 'Fax Number')}" />
                        
                            <g:sortableColumn property="postalAddress" title="${message(code: 'organisation.postalAddress.label', default: 'Postal Address')}" />
                        
                            <g:sortableColumn property="status" title="${message(code: 'organisation.status.label', default: 'Status')}" />
                        
                            <g:sortableColumn property="projects" title="${message(code: 'organisation.projects.label', default: 'Projects')}" />

                            <g:sortableColumn property="people" title="${message(code: 'organisation.people.label', default: 'People')}" />

                            </tr>
                    </thead>
                    <tbody>
                      <g:each in="${organisationInstanceList}" status="i" var="organisationInstance">
                          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

  <!--                            <td><g:link action="show" id="${organisationInstance.id}">${fieldValue(bean: organisationInstance, field: "id")}</g:link></td>-->
  <!--                            <td>${fieldValue(bean: organisationInstance, field: "name")}</td>-->

                              <td><g:link action="show" id="${organisationInstance.id}">${fieldValue(bean: organisationInstance, field: "name")}</g:link></td>

                              <td>${fieldValue(bean: organisationInstance, field: "phoneNumber")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "faxNumber")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "postalAddress")}</td>

                              <td>${fieldValue(bean: organisationInstance, field: "status")}</td>

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
