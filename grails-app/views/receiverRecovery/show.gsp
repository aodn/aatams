
<%@ page import="au.org.emii.aatams.ReceiverRecovery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverRecovery.label', default: 'ReceiverRecovery')}" />
        <g:set var="receiverDeploymentInstance" value="${receiverRecoveryInstance?.deployment}"/>
        <g:set var="projectId" value="${receiverRecoveryInstance?.deployment?.receiver?.project?.id}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tbody>
                        
                            <!-- Deployment details -->
                            <tr class="prop">
                                <td valign="top" class="name">Deployment Details</td>
                                <td valign="top" class="name"><g:message code="receiverDeployment.deploymentDate.label" default="Deployment Date" /></td>
                                <td valign="top" class="value"><g:formatDate date="${receiverDeploymentInstance?.deploymentDate}" /></td>

                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.station.installation.label" default="Installation" /></td>
                                <td valign="top" class="value">
                                  <g:link controller="installation" action="show" id="${receiverDeploymentInstance?.station?.installation?.id}">${receiverDeploymentInstance?.station?.installation?.encodeAsHTML()}</g:link>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.station.label" default="Station" /></td>
                                <td valign="top" class="value"><g:link controller="installationStation" action="show" id="${receiverDeploymentInstance?.station?.id}">${receiverDeploymentInstance?.station?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.location.label" default="Location" /></td>
                                <td valign="top" class="value">${fieldValue(bean: receiverDeploymentInstance, field: "scrambledLocation")}</td>

                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.receiver.label" default="Receiver" /></td>
                                <td valign="top" class="value"><g:link controller="receiver" action="show" id="${receiverDeploymentInstance?.receiver?.id}">${receiverDeploymentInstance?.receiver?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.acousticReleaseID.label" default="Acoustic Release ID" /></td>
                                <td valign="top" class="value">${fieldValue(bean: receiverDeploymentInstance, field: "acousticReleaseID")}</td>
                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.mooringType.label" default="Mooring Type" /></td>
                                <td valign="top" class="value">${receiverDeploymentInstance?.mooringType?.encodeAsHTML()}</td>
                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.depthBelowSurfaceM.label" default="Depth Below Surface (m)" /></td>
                                <td valign="top" class="value">${fieldValue(bean: receiverDeploymentInstance, field: "depthBelowSurfaceM")}</td>
                            </tr>

                          
                          
                            <!-- Recovery -->
                            <tr><td/></tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">Recovery Details</td>
                                <td valign="top" class="name"><g:message code="receiverRecovery.recoverer.label" default="Recovered By" /></td>
                                <td valign="top" class="value"><g:link controller="person" action="show" id="${receiverRecoveryInstance?.recoverer?.person?.id}">${receiverRecoveryInstance?.recoverer?.person?.encodeAsHTML()}</g:link></td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverRecovery.location.label" default="Location" /></td>
                                <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "scrambledLocation")}</td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverRecovery.recoveryDate.label" default="Recovery Date" /></td>
                                <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "recoveryDate")}</td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverRecovery.status.label" default="Status" /></td>
                                <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "status")}</td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverRecovery.comments.label" default="Comments" /></td>
                                <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "comments")}</td>
                            </tr>
                        
                            <!-- Import data file(s) -->
                            <!-- TODO -->
<%--                            
                            <tr><td/></tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">Import Data Files</td>
                                <td valign="top" class="name">
                                    <label for="import">Import VRL</label>
                                </td>
                                <td>
                                    <input type="file" name="vrlFile" accept="vrl" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label for="import">Import RLD</label>
                                </td>
                                <td>
                                    <input type="file" name="rldFile" accept="rld" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label for="import">Import CSV</label>
                                </td>
                                <td>
                                    <input type="file" name="csvFile" accept="csv" />
                                </td>
                            </tr>
--%>                            
                        </tbody>
                      
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${receiverRecoveryInstance?.id}" />
                    <g:hiddenField name="project.id" value="${projectId}" />

                    <shiro:hasPermission permission="project:${projectId}:write">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    </shiro:hasPermission>
                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </g:form>
            </div>
        </div>
    </body>
</html>
