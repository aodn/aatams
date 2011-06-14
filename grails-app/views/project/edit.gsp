

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
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="organisationProjects"><g:message code="project.organisationProjects.label" default="Organisations" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'organisationProjects', 'errors')}">
                                    
                                  <ul>
                                    <g:each in="${projectInstance?.organisationProjects?.organisation}" var="o">
                                        <li><g:link controller="organisation" action="show" id="${o.id}">${o?.encodeAsHTML()}</g:link></li>
                                    </g:each>
                                    <br/>
<!--                                    <g:link controller="organisationProject" 
                                            action="createOrganisationToProject" 
                                            params="['project.id': projectInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'organisationProject.label', default: 'Organisation...')])}</g:link>-->
                                    <a href="#" id='add_organisation_to_project'>${message(code: 'default.add.label', args: [message(code: 'organisationProject.label', default: 'Organisation...')])}</a>
                                  </ul>
                                </td>
                            </tr>
                        

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="principalInvestigator"><g:message code="project.principalInvestigator.label" default="Principal Investigator" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'principalInvestigator', 'errors')}">
                                    <g:select name="principalInvestigator.id" from="${au.org.emii.aatams.ProjectRole.list()}" optionKey="id" value="${projectInstance?.principalInvestigator?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="projectRoles"><g:message code="project.projectRoles.label" default="Project Roles" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'projectRoles', 'errors')}">
                                    
<ul>
<g:each in="${projectInstance?.projectRoles?}" var="p">
    <li><g:link controller="projectRole" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="projectRole" action="create" params="['project.id': projectInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'projectRole.label', default: 'ProjectRole')])}</g:link>


                                </td>
                            </tr>
                        
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
      
      <div id="dialog-form" title="Add Organisation to Project">
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="organisationProject.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'project', 'errors')}">
                                  <g:textField name="duration" value="${fieldValue(bean: organisationProjectInstance, field: 'project')}"/>
                                </td>
                                <g:hiddenField name="project.id" value="${organisationProjectInstance?.project?.id}" />
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="organisation"><g:message code="organisationProject.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'organisation', 'errors')}">
                                    <g:select name="organisation.id" from="${au.org.emii.aatams.Organisation.list()}" optionKey="id" value="${organisationProjectInstance?.organisation?.id}"  />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="saveOrganisationToProject" class="saveOrganisationToProject" value="${message(code: 'default.button.create.label', default: 'Add')}" /></span>
                </div>
            </g:form>
      </div>
      
    </body>
</html>
