

<%@ page import="au.org.emii.aatams.Project" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${projectInstance}">
            <div class="errors">
                <g:renderErrors bean="${projectInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${projectInstance?.id}" />
                <g:hiddenField name="version" value="${projectInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="project.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${projectInstance?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="project.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${projectInstance?.description}" />

                                </td>
                            </tr>
                        
                           <!--
                                Organisations.
                           -->
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="organisationProjects"><g:message code="project.organisationProjects.label" default="Organisations" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'organisationProjects', 'errors')}">
                                    
                                  <ul id="organisation_list">
                                    <g:each in="${projectInstance?.organisationProjects?.organisation.sort{it?.name}}" var="o">
                                        <li><g:link controller="organisation" action="show" id="${o.id}">${o?.encodeAsHTML()}</g:link></li>
                                    </g:each>
                                    <li><br/></li>
                                    <li>
                                      <a href="#" 
                                         id='add_organisation_to_project'>${message(code: 'default.add.label', args: [message(code: 'organisationProject.label', default: 'Organisation...')])}</a>
                                    </li>
                                  </ul>
                                </td>
                            </tr>

                            <!--
                                 People.
                            -->
                            <tr>
                                <td valign="top" class="name">
                                  <label for="projectRoles"><g:message code="project.projectRoles.people.label" default="People" /></label>
                                </td>

                                <td valign="top" class="value">
                                  
                                  <table class="nested">
                                    <tbody id="people_table_body">
                                      <g:each in="${projectInstance?.projectRoles?}" var="p">
                                        <tr>
                                          <td valign="top" class="value">
                                            <g:link controller="person" action="show" id="${p?.person?.id}">${p?.person?.encodeAsHTML()}</g:link>
                                          </td>
                                          <td valign="top" class="value">${p?.roleType}</td>
                                          <td valign="top" class="value">${p?.access}</td>
                                        </tr>
                                        
                                      </g:each>
                                      <tr><td><br/></td></tr>
                                      <tr>
                                        <td>
                                          <a href="#" 
                                             id='add_person_to_project'>${message(code: 'default.add.label', args: [message(code: 'organisationProject.label', default: 'Person...')])}</a>
                                        </td>
                                      </tr>

                                    </tbody>
                                  </table>

                                </td>  
                            </tr>
<!--
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="devices"><g:message code="project.devices.label" default="Devices" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'devices', 'errors')}">

                                  <ul>
                                  <g:each in="${projectInstance?.devices?}" var="d">
                                      <li><g:link controller="device" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></li>
                                  </g:each>
                                  </ul>
                                  <g:link controller="device" action="create" params="['project.id': projectInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'device.label', default: 'Device')])}</g:link>
-->

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
      
      <!--
            Dialog presented when adding organisation to project.
            TODO: get this on demand (i.e. with AJAX)
      -->
      <div id="dialog-form-add-organisation" title="Add Organisation to Project">
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="projectId"><g:message code="organisationProject.project.label" default="Project" /></label>
                                    <g:hiddenField name="projectId" value="${projectInstance?.id}" />

                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'project', 'errors')}">
                                  <label id="project">${projectInstance}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="organisationId"><g:message code="organisationProject.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'organisation', 'errors')}">
                                  <g:select name="organisationId" from="${unrelatedOrganisations}" optionKey="id"/>
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
            </g:form>
      </div>
      
      <!--
            Dialog presented when adding person (with role) to project.
            TODO: get this on demand (i.e. with AJAX)
      -->
      <div id="dialog-form-add-person" title="Add Person to Project">
        <g:form action="save" >
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="projectId"><g:message code="organisationProject.project.label" default="Project" /></label>
                                <g:hiddenField name="projectId" value="${projectInstance?.id}" />

                            </td>
                            <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'project', 'errors')}">
                              <label id="project">${projectInstance}</label>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="person"><g:message code="projectRole.person.label" default="Person" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: projectRoleInstance, field: 'person', 'errors')}">
                                <g:select name="personId" from="${au.org.emii.aatams.Person.list()}" optionKey="id" value="${projectRoleInstance?.person?.id}"  />

                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="roleType"><g:message code="projectRole.roleType.label" default="Role Type" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: projectRoleInstance, field: 'roleType', 'errors')}">
                                <g:select name="roleTypeId" from="${au.org.emii.aatams.ProjectRoleType.list()}" optionKey="id" value="${projectRoleInstance?.roleType?.id}"  />

                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="access"><g:message code="projectRole.access.label" default="Access" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: projectRoleInstance, field: 'access', 'errors')}">
                                <g:select name="access" from="${au.org.emii.aatams.ProjectAccess?.values()}" keys="${au.org.emii.aatams.ProjectAccess?.values()*.name()}" value="${projectRoleInstance?.access?.name()}"  />

                            </td>
                        </tr>

                    </tbody>
                </table>
            </div>
        </g:form>
      </div>
    </body>
</html>
