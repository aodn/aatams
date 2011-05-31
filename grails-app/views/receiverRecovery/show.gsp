
<%@ page import="au.org.emii.aatams.ReceiverRecovery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverRecovery.label', default: 'ReceiverRecovery')}" />
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
                            <td valign="top" class="name"><g:message code="receiverRecovery.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverRecovery.recoveryDate.label" default="Recovery Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${receiverRecoveryInstance?.recoveryDate}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverRecovery.location.label" default="Location" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "location")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverRecovery.status.label" default="Status" /></td>
                            
                            <td valign="top" class="value"><g:link controller="deviceStatus" action="show" id="${receiverRecoveryInstance?.status?.id}">${receiverRecoveryInstance?.status?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverRecovery.download.label" default="Download" /></td>
                            
                            <td valign="top" class="value"><g:link controller="receiverDownload" action="show" id="${receiverRecoveryInstance?.download?.id}">${receiverRecoveryInstance?.download?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverRecovery.deployment.label" default="Deployment" /></td>
                            
                            <td valign="top" class="value"><g:link controller="receiverDeployment" action="show" id="${receiverRecoveryInstance?.deployment?.id}">${receiverRecoveryInstance?.deployment?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverRecovery.batteryLife.label" default="Battery Life" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "batteryLife")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverRecovery.batteryVoltage.label" default="Battery Voltage" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverRecoveryInstance, field: "batteryVoltage")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${receiverRecoveryInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
