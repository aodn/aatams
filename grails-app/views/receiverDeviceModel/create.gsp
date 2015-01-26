

<%@ page import="au.org.emii.aatams.ReceiverDeviceModel" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel')}" />
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
            <g:hasErrors bean="${receiverDeviceModelInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverDeviceModelInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="modelName"><g:message code="receiverDeviceModel.modelName.label" default="Model Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeviceModelInstance, field: 'modelName', 'errors')}">
                                    <input type="text" id="modelName" name="modelName" value="${fieldValue(bean:receiverDeviceModelInstance,field:'modelName')}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="manufacturer"><g:message code="receiverDeviceModel.manufacturer.label" default="Manufacturer" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDeviceModelInstance, field: 'manufacturer', 'errors')}">
                                    <g:select optionKey="id" from="${au.org.emii.aatams.DeviceManufacturer.list()}" name="manufacturer.id" value="${receiverDeviceModelInstance?.manufacturer?.id}" ></g:select>
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
