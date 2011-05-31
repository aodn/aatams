

<%@ page import="au.org.emii.aatams.Device" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'device.label', default: 'Device')}" />
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
            <g:hasErrors bean="${deviceInstance}">
            <div class="errors">
                <g:renderErrors bean="${deviceInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="codeName"><g:message code="device.codeName.label" default="Code Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: deviceInstance, field: 'codeName', 'errors')}">
                                    <g:textField name="codeName" value="${deviceInstance?.codeName}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="embargoDate"><g:message code="device.embargoDate.label" default="Embargo Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: deviceInstance, field: 'embargoDate', 'errors')}">
                                    <g:datePicker name="embargoDate" precision="day" value="${deviceInstance?.embargoDate}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="model"><g:message code="device.model.label" default="Model" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: deviceInstance, field: 'model', 'errors')}">
                                    <g:select name="model.id" from="${au.org.emii.aatams.DeviceModel.list()}" optionKey="id" value="${deviceInstance?.model?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="device.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: deviceInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${au.org.emii.aatams.Project.list()}" optionKey="id" value="${deviceInstance?.project?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="serialNumber"><g:message code="device.serialNumber.label" default="Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: deviceInstance, field: 'serialNumber', 'errors')}">
                                    <g:textField name="serialNumber" value="${deviceInstance?.serialNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="status"><g:message code="device.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: deviceInstance, field: 'status', 'errors')}">
                                    <g:select name="status.id" from="${au.org.emii.aatams.DeviceStatus.list()}" optionKey="id" value="${deviceInstance?.status?.id}"  />

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
