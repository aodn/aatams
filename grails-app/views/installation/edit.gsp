

<%@ page import="au.org.emii.aatams.Installation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'installation.label', default: 'Installation')}" />
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
            <g:hasErrors bean="${installationInstance}">
            <div class="errors">
                <g:renderErrors bean="${installationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${installationInstance?.id}" />
                <g:hiddenField name="version" value="${installationInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="configuration"><g:message code="installation.configuration.label" default="Configuration" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'configuration', 'errors')}">
                                    <g:select name="configuration.id" from="${au.org.emii.aatams.InstallationConfiguration.list()}" optionKey="id" value="${installationInstance?.configuration?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="latOffset"><g:message code="installation.latOffset.label" default="Lat Offset" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'latOffset', 'errors')}">
                                    <g:textField name="latOffset" value="${fieldValue(bean: installationInstance, field: 'latOffset')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="lonOffset"><g:message code="installation.lonOffset.label" default="Lon Offset" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'lonOffset', 'errors')}">
                                    <g:textField name="lonOffset" value="${fieldValue(bean: installationInstance, field: 'lonOffset')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="installation.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${installationInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="project"><g:message code="installation.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${au.org.emii.aatams.Project.list()}" optionKey="id" value="${installationInstance?.project?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="stations"><g:message code="installation.stations.label" default="Stations" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'stations', 'errors')}">
                                    
<ul>
<g:each in="${installationInstance?.stations?}" var="s">
    <li><g:link controller="installationStation" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="installationStation" action="create" params="['installation.id': installationInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'installationStation.label', default: 'InstallationStation')])}</g:link>

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
    </body>
</html>
