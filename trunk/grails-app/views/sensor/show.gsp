
<%@ page import="au.org.emii.aatams.Sensor" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'sensor.label', default: 'Sensor')}" />
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
                            <td valign="top" class="name"><g:message code="sensor.transmitterType.label" default="Transmitter Type" /></td>
                            
                            <td valign="top" class="value"><g:link controller="transmitterType" action="show" id="${sensorInstance?.transmitterType?.id}">${sensorInstance?.transmitterType?.encodeAsHTML()}</g:link></td>
                            
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="sensor.pingCode.label" default="Ping Code" /></td>
                            
                            <td valign="top" class="value">${sensorInstance?.pingCode}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="sensor.slope.label" default="Slope" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: sensorInstance, field: "slope")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="sensor.intercept.label" default="Intercept" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: sensorInstance, field: "intercept")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="sensor.unit.label" default="Unit" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: sensorInstance, field: "unit")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="sensor.status.label" default="Status" /></td>
                            
                            <td valign="top" class="value"><g:link controller="deviceStatus" action="show" id="${sensorInstance?.status?.id}">${sensorInstance?.status?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${sensorInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
