
<%@ page import="au.org.emii.aatams.ProjectRole" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'projectRole.label', default: 'ProjectRole')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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
                            <td valign="top" class="name"><g:message code="projectRole.access.label" default="Access" /></td>
                            
                            <td valign="top" class="value">${projectRoleInstance?.access?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="projectRole.person.label" default="Person" /></td>
                            
                            <td valign="top" class="value"><g:link controller="person" action="show" id="${projectRoleInstance?.person?.id}">${projectRoleInstance?.person?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="projectRole.project.label" default="Project" /></td>
                            
                            <td valign="top" class="value"><g:link controller="project" action="show" id="${projectRoleInstance?.project?.id}">${projectRoleInstance?.project?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="projectRole.roleType.label" default="Role Type" /></td>
                            
                            <td valign="top" class="value"><g:link controller="projectRoleType" action="show" id="${projectRoleInstance?.roleType?.id}">${projectRoleInstance?.roleType?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${projectRoleInstance?.id}" />
                    <g:hiddenField name="projectId" value="${projectRoleInstance?.project?.id}" />
                    <shiro:hasPermission permission="project:${projectRoleInstance?.project?.id}:write">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasPermission>
                </g:form>
            </div>
        </div>
    </body>
</html>
