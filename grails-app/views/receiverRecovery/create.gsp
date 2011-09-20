

<%@ page import="au.org.emii.aatams.ReceiverRecovery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverRecovery.label', default: 'ReceiverRecovery')}" />
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
            <g:hasErrors bean="${receiverRecoveryInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverRecoveryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:set var="receiverDeploymentInstance" value="${receiverRecoveryInstance?.deployment}"/>
            <g:form action="save" >
              <g:hiddenField name="deploymentId" value="${receiverDeploymentInstance?.id}"/>
              <g:hiddenField name="projectId" value="${receiverDeploymentInstance?.station?.installation?.project?.id}"/>
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <!-- Deployment details -->
                            <tr class="prop">
                                <td valign="top" class="name">Deployment Details</td>
                                <td valign="top" class="name"><g:message code="receiverDeployment.deploymentDateTime.label" default="Deployment Date" /></td>
                                <td valign="top" class="value"><joda:format value="${receiverDeploymentInstance?.deploymentDateTime}" /></td>

                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.station.installation.label" default="Installation" /></td>
                                <td valign="top" class="value"><g:link controller="installation" action="show" id="${receiverDeploymentInstance?.station?.installation?.id}">${receiverDeploymentInstance?.station?.installation?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.station.label" default="Station" /></td>
                                <td valign="top" class="value"><g:link controller="installationStation" action="show" id="${receiverDeploymentInstance?.station?.id}">${receiverDeploymentInstance?.station?.encodeAsHTML()}</g:link></td>

                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name"><g:message code="receiverDeployment.location.label" default="Location" /></td>
                                <td valign="top" class="value">
                                  <g:point name="location"
                                           value="${receiverDeploymentInstance?.location}"/>
                                </td>

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
                                <td valign="top" class="name">
                                    <label class="compulsory" for="recoverer"><g:message code="receiverRecovery.recoverer.label" default="Recovered By" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'recoverer', 'errors')}">
                                    <g:select name="recoverer.id" 
                                              from="${receiverDeploymentInstance?.station?.installation?.project?.projectRoles}" 
                                              optionKey="id" 
                                              optionValue="person"
                                              value="${receiverRecoveryInstance?.recoverer?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label class="compulsory" for="location"><g:message code="receiverRecovery.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'location', 'errors')}">
                                  <g:point name="location"
                                           value="${receiverRecoveryInstance?.location}"
                                           editable="${true}"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label class="compulsory" for="recoveryDateTime"><g:message code="receiverRecovery.recoveryDate.label" default="Recovery Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'recoveryDateTime', 'errors')}">
                                    <joda:dateTimePicker name="recoveryDateTime" 
                                                         value="${receiverRecoveryInstance?.recoveryDateTime}"
                                                         useZone="true"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label class="compulsory" for="status"><g:message code="receiverRecovery.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'status', 'errors')}">
                                    <g:select name="status.id" 
                                              from="${au.org.emii.aatams.DeviceStatus.list()}" 
                                              optionKey="id" 
                                              value="${receiverRecoveryInstance?.status?.id ? receiverRecoveryInstance?.status?.id : au.org.emii.aatams.DeviceStatus.findByStatus('RECOVERED').id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label for="comments"><g:message code="receiverRecovery.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'comments', 'errors')}">
                                    <g:textArea name="comments" value="${fieldValue(bean: receiverRecoveryInstance, field: 'comments')}" />

                                </td>
                            </tr>
                        
                            <!-- Import data file(s) -->
<%-- Import is now done in pop-up on separate screen                             
                            <tr><td/></tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">Import Data Files</td>
                                <td valign="top" class="name">
                                    <label class="compulsory" for="import">Import VRL</label>
                                </td>
                                <td>
                                    <input type="file" name="vrlFile" accept="vrl" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label class="compulsory" for="import">Import RLD</label>
                                </td>
                                <td>
                                    <input type="file" name="rldFile" accept="rld" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td/>
                                <td valign="top" class="name">
                                    <label class="compulsory" for="import">Import CSV</label>
                                </td>
                                <td>
                                    <input type="file" name="csvFile" accept="csv" />
                                </td>
                            </tr>
--%>                            
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
