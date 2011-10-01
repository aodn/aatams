

<%@ page import="au.org.emii.aatams.detection.InvalidDetection" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'invalidDetection.label', default: 'InvalidDetection')}" />
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
            <g:hasErrors bean="${invalidDetectionInstance}">
            <div class="errors">
                <g:renderErrors bean="${invalidDetectionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="transmitterName"><g:message code="invalidDetection.transmitterName.label" default="Transmitter Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'transmitterName', 'errors')}">
                                    <input type="text" id="transmitterName" name="transmitterName" value="${fieldValue(bean:invalidDetectionInstance,field:'transmitterName')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="transmitterSerialNumber"><g:message code="invalidDetection.transmitterSerialNumber.label" default="Transmitter Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'transmitterSerialNumber', 'errors')}">
                                    <input type="text" id="transmitterSerialNumber" name="transmitterSerialNumber" value="${fieldValue(bean:invalidDetectionInstance,field:'transmitterSerialNumber')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sensorValue"><g:message code="invalidDetection.sensorValue.label" default="Sensor Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'sensorValue', 'errors')}">
                                    <input type="text" id="sensorValue" name="sensorValue" value="${fieldValue(bean:invalidDetectionInstance,field:'sensorValue')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sensorUnit"><g:message code="invalidDetection.sensorUnit.label" default="Sensor Unit" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'sensorUnit', 'errors')}">
                                    <input type="text" id="sensorUnit" name="sensorUnit" value="${fieldValue(bean:invalidDetectionInstance,field:'sensorUnit')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="stationName"><g:message code="invalidDetection.stationName.label" default="Station Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'stationName', 'errors')}">
                                    <input type="text" id="stationName" name="stationName" value="${fieldValue(bean:invalidDetectionInstance,field:'stationName')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="location"><g:message code="invalidDetection.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'location', 'errors')}">
                                    
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="reason"><g:message code="invalidDetection.reason.label" default="Reason" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'reason', 'errors')}">
                                    <g:select  from="${au.org.emii.aatams.detection.InvalidDetectionReason?.values()}" value="${invalidDetectionInstance?.reason}" name="reason" ></g:select>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="message"><g:message code="invalidDetection.message.label" default="Message" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'message', 'errors')}">
                                    <input type="text" id="message" name="message" value="${fieldValue(bean:invalidDetectionInstance,field:'message')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="receiverName"><g:message code="invalidDetection.receiverName.label" default="Receiver Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'receiverName', 'errors')}">
                                    <input type="text" id="receiverName" name="receiverName" value="${fieldValue(bean:invalidDetectionInstance,field:'receiverName')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="timestamp"><g:message code="invalidDetection.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'timestamp', 'errors')}">
                                    <g:datePicker name="timestamp" value="${invalidDetectionInstance?.timestamp}" ></g:datePicker>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="transmitterId"><g:message code="invalidDetection.transmitterId.label" default="Transmitter Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: invalidDetectionInstance, field: 'transmitterId', 'errors')}">
                                    <input type="text" id="transmitterId" name="transmitterId" value="${fieldValue(bean:invalidDetectionInstance,field:'transmitterId')}"/>
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
