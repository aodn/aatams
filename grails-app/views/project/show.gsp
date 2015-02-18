<%@ page import="au.org.emii.aatams.Project" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:user>
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:user>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="project.name.label" default="Name" /></td>

                            <td valign="top" class="value">${fieldValue(bean: projectInstance, field: "name")}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="project.organisationProjects.label" default="Organisations" /></td>

                            <td valign="top" style="text-align: left;" class="value">
                              <table class="nested">
                                <tbody>
                                  <g:each in="${projectInstance.organisationProjects}" var="op">
                                    <tr>
                                      <td class="rowButton"><g:link class="show" controller="organisationProject" action="show" id="${op?.id}">.</g:link></td>
                                      <td>
                                        <g:link controller="organisation" action="show" id="${op?.organisation.id}">${op?.organisation?.encodeAsHTML()}</g:link>
                                      </td>
                                    </tr>

                                  </g:each>
                                </tbody>
                              </table>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                              <label for="projectRoles"><g:message code="project.projectRoles.people.label" default="People" /></label>
                            </td>

                            <td >

                              <table class="nested">
                                <thead>
                                  <tr>
                                    <th/>
                                    <th>Name</th>
                                    <th>Project Role</th>
                                    <th>Access</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  <g:each in="${projectInstance?.projectRoles?}" var="p">
                                    <tr>
                                      <td class="rowButton"><g:link class="show" controller="projectRole" action="show" id="${p?.id}">.</g:link></td>
                                      <td>
                                        <g:link controller="person" action="show" id="${p?.person?.id}">${p?.person?.encodeAsHTML()}</g:link>
                                      </td>
                                      <td>${p?.roleType}</td>
                                      <td>${p?.access}</td>
                                    </tr>

                                  </g:each>
                                </tbody>
                              </table>

                            </td>
                         </tr>

                        <shiro:hasRole name="SysAdmin">
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="project.status.label" default="Status" /></td>

                              <td valign="top" class="value">${projectInstance?.status?.encodeAsHTML()}</td>

                          </tr>

                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="project.requestingUser.label" default="Requester" /></td>

                              <td valign="top" class="value"><g:link controller="person" action="show" id="${projectInstance?.requestingUser?.id}">${projectInstance?.requestingUser?.encodeAsHTML()}</g:link></td>

                          </tr>
                        </shiro:hasRole>

                        <g:if test="${projectInstance?.isProtected}">
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="project.isProtected.label" default="Protected" /></td>

                                <td valign="top" class="value">This Project is protected</td>
                            </tr>
                        </g:if>
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${projectInstance?.id}" />

                    <shiro:hasPermission permission="${'project:' + projectInstance?.id + ':edit'}">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    </shiro:hasPermission>

                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </g:form>
            </div>
        </div>
    </body>
</html>
