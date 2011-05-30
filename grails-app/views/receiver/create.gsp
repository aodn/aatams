

<%@ page import="au.org.emii.aatams.Receiver" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiver.label', default: 'Receiver')}" />
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
            <g:hasErrors bean="${receiverInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="batteryLife"><g:message code="receiver.batteryLife.label" default="Battery Life" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'batteryLife', 'errors')}">
                                    <g:textField name="batteryLife" value="${fieldValue(bean: receiverInstance, field: 'batteryLife')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="batteryVoltage"><g:message code="receiver.batteryVoltage.label" default="Battery Voltage" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'batteryVoltage', 'errors')}">
                                    <g:textField name="batteryVoltage" value="${fieldValue(bean: receiverInstance, field: 'batteryVoltage')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="codeName"><g:message code="receiver.codeName.label" default="Code Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'codeName', 'errors')}">
                                    <g:textField name="codeName" value="${receiverInstance?.codeName}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="embargoDate"><g:message code="receiver.embargoDate.label" default="Embargo Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'embargoDate', 'errors')}">
                                    <g:datePicker name="embargoDate" precision="day" value="${receiverInstance?.embargoDate}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="model"><g:message code="receiver.model.label" default="Model" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'model', 'errors')}">
                                    <g:select name="model.id" from="${au.org.emii.aatams.DeviceModel.list()}" optionKey="id" value="${receiverInstance?.model?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="receiver.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${au.org.emii.aatams.Project.list()}" optionKey="id" value="${receiverInstance?.project?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="serialNumber"><g:message code="receiver.serialNumber.label" default="Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'serialNumber', 'errors')}">
                                    <g:textField name="serialNumber" value="${receiverInstance?.serialNumber}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="station"><g:message code="receiver.station.label" default="Station" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'station', 'errors')}">
                                    <g:select name="station.id" from="${au.org.emii.aatams.InstallationStation.list()}" optionKey="id" value="${receiverInstance?.station?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="status"><g:message code="receiver.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'status', 'errors')}">
                                    <g:select name="status.id" from="${au.org.emii.aatams.DeviceStatus.list()}" optionKey="id" value="${receiverInstance?.status?.id}"  />
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
