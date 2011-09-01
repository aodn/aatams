

<%@ page import="au.org.emii.aatams.Detection" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'detection.label', default: 'Detection')}" />
        <g:set var="projectId" value="${detectionInstance?.receiverDeployment?.station?.installation?.project?.id}" />
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
            <g:hasErrors bean="${detectionInstance}">
            <div class="errors">
                <g:renderErrors bean="${detectionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${detectionInstance?.id}" />
                <g:hiddenField name="version" value="${detectionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="timestamp"><g:message code="detection.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'timestamp', 'errors')}">
                                    <g:datePicker name="timestamp" precision="minute" value="${detectionInstance?.timestamp}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="receiver"><g:message code="detection.receiverDeployment.label" default="Receiver Deployment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'receiverDeployment', 'errors')}">
                                    <g:select name="receiverDeployment.id" from="${candidateDeployments}" optionKey="id" value="${detectionInstance?.receiverDeployment?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="transmitterName"><g:message code="detection.transmitterName.label" default="Transmitter Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'transmitterName', 'errors')}">
                                    <g:textField name="transmitterName" value="${detectionInstance?.transmitterName}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="transmitterSerialNumber"><g:message code="detection.transmitterSerialNumber.label" default="Transmitter Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'transmitterSerialNumber', 'errors')}">
                                    <g:textField name="transmitterSerialNumber" value="${detectionInstance?.transmitterSerialNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="stationName"><g:message code="detection.stationName.label" default="Station Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'stationName', 'errors')}">
                                    <g:textField name="stationName" value="${detectionInstance?.stationName}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="location"><g:message code="detection.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'location', 'errors')}">
                                  <g:point name="location" 
                                           value="${detectionInstance?.location}" 
                                           editable="${true}" />
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
