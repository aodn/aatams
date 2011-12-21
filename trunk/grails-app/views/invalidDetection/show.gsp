
<%@ page import="au.org.emii.aatams.detection.InvalidDetection" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'invalidDetection.label', default: 'InvalidDetection')}" />
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
                            <td valign="top" class="name"><g:message code="invalidDetection.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.transmitterName.label" default="Transmitter Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "transmitterName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.transmitterSerialNumber.label" default="Transmitter Serial Number" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "transmitterSerialNumber")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.sensorValue.label" default="Sensor Value" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "sensorValue")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.sensorUnit.label" default="Sensor Unit" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "sensorUnit")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.stationName.label" default="Station Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "stationName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.location.label" default="Location" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "location")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.reason.label" default="Reason" /></td>
                            
                            <td valign="top" class="value">${invalidDetectionInstance?.reason?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.message.label" default="Message" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "message")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.receiverName.label" default="Receiver Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "receiverName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.timestamp.label" default="Timestamp" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${invalidDetectionInstance?.timestamp}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="invalidDetection.transmitterId.label" default="Transmitter Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: invalidDetectionInstance, field: "transmitterId")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${invalidDetectionInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
