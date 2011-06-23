

<%@ page import="au.org.emii.aatams.ProjectRole" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'projectRole.label', default: 'ProjectRole')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${projectRoleInstance}">
            <div class="errors">
                <g:renderErrors bean="${projectRoleInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="access"><g:message code="projectRole.access.label" default="Access" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectRoleInstance, field: 'access', 'errors')}">
                                    <g:select name="access" from="${au.org.emii.aatams.ProjectAccess?.values()}" keys="${au.org.emii.aatams.ProjectAccess?.values()*.name()}" value="${projectRoleInstance?.access?.name()}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="person"><g:message code="projectRole.person.label" default="Person" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectRoleInstance, field: 'person', 'errors')}">
                                    <g:select name="person.id" from="${au.org.emii.aatams.Person.list()}" optionKey="id" value="${projectRoleInstance?.person?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="projectRole.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectRoleInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${au.org.emii.aatams.Project.list()}" optionKey="id" value="${projectRoleInstance?.project?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="roleType"><g:message code="projectRole.roleType.label" default="Role Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: projectRoleInstance, field: 'roleType', 'errors')}">
                                    <g:select name="roleType.id" from="${au.org.emii.aatams.ProjectRoleType.list()}" optionKey="id" value="${projectRoleInstance?.roleType?.id}"  />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
