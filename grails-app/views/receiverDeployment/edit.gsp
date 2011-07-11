

<%@ page import="au.org.emii.aatams.ReceiverDeployment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDeployment.label', default: 'ReceiverDeployment')}" />
        <g:set var="projectId" value="${receiverDeploymentInstance?.receiver?.project?.id}" />
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
            <g:hasErrors bean="${receiverDeploymentInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverDeploymentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${receiverDeploymentInstance?.id}" />
                <g:hiddenField name="version" value="${receiverDeploymentInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="station"><g:message code="receiverDeployment.station.label" default="Station" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'station', 'errors')}">
                                    <g:select name="station.id" from="${au.org.emii.aatams.InstallationStation.list()}" optionKey="id" value="${receiverDeploymentInstance?.station?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="receiver"><g:message code="receiverDeployment.receiver.label" default="Receiver" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'receiver', 'errors')}">
                                    <g:select name="receiver.id" from="${au.org.emii.aatams.Receiver.list()}" optionKey="id" value="${receiverDeploymentInstance?.receiver?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="deploymentDate"><g:message code="receiverDeployment.deploymentDate.label" default="Deployment Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'deploymentDate', 'errors')}">
                                    <g:datePicker name="deploymentDate" precision="day" value="${receiverDeploymentInstance?.deploymentDate}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="location"><g:message code="receiverDeployment.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'location', 'errors')}">
                                    <g:textField name="location" value="${receiverDeploymentInstance?.location}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="recoveryDate"><g:message code="receiverDeployment.recoveryDate.label" default="Scheduled Recovery Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'recoveryDate', 'errors')}">
                                    <g:datePicker name="recoveryDate" precision="day" value="${receiverDeploymentInstance?.recoveryDate}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="acousticReleaseID"><g:message code="receiverDeployment.acousticReleaseID.label" default="Acoustic Release ID" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'acousticReleaseID', 'errors')}">
                                    <g:textField name="acousticReleaseID" value="${receiverDeploymentInstance?.acousticReleaseID}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="mooringType"><g:message code="receiverDeployment.mooringType.label" default="Mooring Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'mooringType', 'errors')}">
                                    <g:select name="mooringType.id" from="${au.org.emii.aatams.MooringType.list()}" optionKey="id" value="${receiverDeploymentInstance?.mooringType?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="bottomDepthM"><g:message code="receiverDeployment.bottomDepthM.label" default="Bottom Depth (m)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'bottomDepthM', 'errors')}">
                                    <g:textField name="bottomDepthM" value="${fieldValue(bean: receiverDeploymentInstance, field: 'bottomDepthM')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="depthBelowSurfaceM"><g:message code="receiverDeployment.depthBelowSurfaceM.label" default="Depth Below Surface (m)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeploymentInstance, field: 'depthBelowSurfaceM', 'errors')}">
                                    <g:textField name="depthBelowSurfaceM" value="${fieldValue(bean: receiverDeploymentInstance, field: 'depthBelowSurfaceM')}" />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:hiddenField name="project.id" value="${projectId}" />
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </div>
            </g:form>
        </div>
    </body>
</html>
