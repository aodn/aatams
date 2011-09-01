

<%@ page import="au.org.emii.aatams.SensorDetection" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'sensorDetection.label', default: 'SensorDetection')}" />
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
            <g:hasErrors bean="${sensorDetectionInstance}">
            <div class="errors">
                <g:renderErrors bean="${sensorDetectionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${sensorDetectionInstance?.id}" />
                <g:hiddenField name="version" value="${sensorDetectionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="timestamp"><g:message code="sensorDetection.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'timestamp', 'errors')}">
                                    <g:datePicker name="timestamp" precision="day" value="${sensorDetectionInstance?.timestamp}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="receiver"><g:message code="sensorDetection.receiver.label" default="Receiver" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'receiver', 'errors')}">
                                    <g:select name="receiver.id" from="${au.org.emii.aatams.Receiver.list()}" optionKey="id" value="${sensorDetectionInstance?.receiver?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="transmitterName"><g:message code="sensorDetection.transmitterName.label" default="Transmitter Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'transmitterName', 'errors')}">
                                    <g:textField name="transmitterName" value="${sensorDetectionInstance?.transmitterName}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="transmitterSerialNumber"><g:message code="sensorDetection.transmitterSerialNumber.label" default="Transmitter Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'transmitterSerialNumber', 'errors')}">
                                    <g:textField name="transmitterSerialNumber" value="${sensorDetectionInstance?.transmitterSerialNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="stationName"><g:message code="sensorDetection.stationName.label" default="Station Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'stationName', 'errors')}">
                                    <g:textField name="stationName" value="${sensorDetectionInstance?.stationName}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="location"><g:message code="sensorDetection.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'location', 'errors')}">
                                    <g:textField name="location" value="${sensorDetectionInstance?.location}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="uncalibratedValue"><g:message code="sensorDetection.uncalibratedValue.label" default="Uncalibrated Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'uncalibratedValue', 'errors')}">
                                    <g:textField name="uncalibratedValue" value="${fieldValue(bean: sensorDetectionInstance, field: 'uncalibratedValue')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="sensorUnit"><g:message code="sensorDetection.sensorUnit.label" default="Sensor Unit" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'sensorUnit', 'errors')}">
                                    <g:textField name="sensorUnit" value="${sensorDetectionInstance?.sensorUnit}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="sensors"><g:message code="sensorDetection.sensors.label" default="Sensors" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'sensors', 'errors')}">
                                    <g:select name="sensors" from="${au.org.emii.aatams.Sensor.list()}" multiple="yes" optionKey="id" size="5" value="${sensorDetectionInstance?.sensors*.id}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="tags"><g:message code="sensorDetection.tags.label" default="Tags" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorDetectionInstance, field: 'tags', 'errors')}">
                                    <g:select name="tags" from="${au.org.emii.aatams.Tag.list()}" multiple="yes" optionKey="id" size="5" value="${sensorDetectionInstance?.tags*.id}" />

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
