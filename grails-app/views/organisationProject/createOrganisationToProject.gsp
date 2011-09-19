

<%@ page import="au.org.emii.aatams.OrganisationProject" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisationProject.label', default: 'OrganisationProject')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1>Add Organisation to Project</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${organisationProjectInstance}">
            <div class="errors">
                <g:renderErrors bean="${organisationProjectInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="organisationProject.project.label" default="Project" /></label>
                                </td>
                                <td>${organisationProjectInstance.project}</td>
                                <g:hiddenField name="project.id" value="${organisationProjectInstance?.project?.id}" />
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="organisation"><g:message code="organisationProject.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationProjectInstance, field: 'organisation', 'errors')}">
                                    <g:select name="organisation.id" from="${au.org.emii.aatams.Organisation.listActive()}" optionKey="id" value="${organisationProjectInstance?.organisation?.id}"  />

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
