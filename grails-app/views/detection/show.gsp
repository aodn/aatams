
<%@ page import="au.org.emii.aatams.detection.ValidDetection" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'detection.label', default: 'Detection')}" />
        <g:set var="projectId" value="${detectionInstance?.receiverDeployment?.station?.installation?.project?.id}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasRole name="SysAdmin">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasRole>
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
                            <td valign="top" class="name"><g:message code="detection.timestamp.label" default="Timestamp" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${detectionInstance?.timestamp}"
                                                                         format="yyyy-MM-dd'T'HH:mm:ssZ"
                                                                         timeZone='${TimeZone.getTimeZone("UTC")}'/></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.receiverName.label" default="Receiver Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: detectionInstance, field: "receiverName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.receiverDeployment.label" default="Receiver Deployment" /></td>
                            
                            <td valign="top" class="value"><g:link controller="receiverDeployment" action="show" id="${detectionInstance?.receiverDeployment?.id}">${detectionInstance?.receiverDeployment?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.transmitterId.label" default="Transmitter ID" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: detectionInstance, field: "transmitterId")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.transmitterName.label" default="Transmitter Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: detectionInstance, field: "transmitterName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.transmitterSerialNumber.label" default="Transmitter Serial Number" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: detectionInstance, field: "transmitterSerialNumber")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.stationName.label" default="Station Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: detectionInstance, field: "stationName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.location.label" default="Location" /></td>
                            
                            <td valign="top" class="value">
                                  <g:point name="scrambledLocation" 
                                           value="${detectionInstance?.scrambledLocation}" />
                            </td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="detection.tags.label" default="Tags" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                              
                              <table class="nested">
                                <tbody>
                                  <g:each in="${detectionInstance?.detectionSurgeries}" var="s">
                                    <tr>
                                      <td class="rowButton"><g:link class="show" controller="detectionSurgery" action="show" id="${s?.id}">.</g:link></td>
                                      <td>
                                        <g:link controller="tag" action="show" id="${s?.surgery?.tag?.id}">${s?.surgery?.tag?.encodeAsHTML()}</g:link>
                                      </td>
                                    </tr>

                                  </g:each>
                                </tbody>
                              </table>

                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${detectionInstance?.id}" />
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
