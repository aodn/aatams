

<%@ page import="au.org.emii.aatams.OrganisationProject" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisationProject.label', default: 'OrganisationProject')}" />
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
            <g:hasErrors bean="${organisationProjectInstance}">
            <div class="errors">
                <g:renderErrors bean="${organisationProjectInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${organisationProjectInstance?.id}" />
                <g:hiddenField name="version" value="${organisationProjectInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="organisation"><g:message code="organisationProject.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'organisation', 'errors')}">
                                    <g:select name="organisation.id" from="${au.org.emii.aatams.Organisation.listActive()}" optionKey="id" value="${organisationProjectInstance?.organisation?.id}"  />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="project"><g:message code="organisationProject.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${au.org.emii.aatams.Project.list()}" optionKey="id" value="${organisationProjectInstance?.project?.id}"  />

                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:hiddenField name="projectId" value="${organisationProjectInstance?.project?.id}" />
                    <shiro:hasPermission permission="project:${organisationProjectInstance?.project?.id}:edit">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasPermission>
                </div>
            </g:form>
        </div>
    </body>
</html>
