

<%@ page import="au.org.emii.aatams.InstallationStation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'installationStation.label', default: 'InstallationStation')}" />
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
            <g:hasErrors bean="${installationStationInstance}">
            <div class="errors">
                <g:renderErrors bean="${installationStationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="installationStation.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationStationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${installationStationInstance?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="curtainPosition"><g:message code="installationStation.curtainPosition.label" default="Curtain Position" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationStationInstance, field: 'curtainPosition', 'errors')}">
                                    <g:textField name="curtainPosition" value="${fieldValue(bean: installationStationInstance, field: 'curtainPosition')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="location"><g:message code="installationStation.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationStationInstance, field: 'location', 'errors')}">
                                    <g:point name="location"
                                             value="${installationStationInstance?.location}"
                                             editable="${true}"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="installation"><g:message code="installationStation.installation.label" default="Installation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationStationInstance, field: 'installation', 'errors')}">
                                    <g:select name="installation.id" from="${candidateInstallations}" optionKey="id" value="${installationStationInstance?.installation?.id}"  />

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
