

<%@ page import="au.org.emii.aatams.Organisation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisation.label', default: 'Organisation')}" />
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
            <g:hasErrors bean="${organisationInstance}">
            <div class="errors">
                <g:renderErrors bean="${organisationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="organisation.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${organisationInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="phoneNumber"><g:message code="organisation.phoneNumber.label" default="Phone Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'phoneNumber', 'errors')}">
                                    <g:textField name="phoneNumber" value="${organisationInstance?.phoneNumber}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="faxNumber"><g:message code="organisation.faxNumber.label" default="Fax Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'faxNumber', 'errors')}">
                                    <g:textField name="faxNumber" value="${organisationInstance?.faxNumber}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="postalAddress"><g:message code="organisation.postalAddress.label" default="Postal Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'postalAddress', 'errors')}">
                                    <g:textField name="postalAddress" value="${organisationInstance?.postalAddress}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="status"><g:message code="organisation.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${au.org.emii.aatams.EntityStatus?.values()}" keys="${au.org.emii.aatams.EntityStatus?.values()*.name()}" value="${organisationInstance?.status?.name()}"  />
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
