

<%@ page import="au.org.emii.aatams.ReceiverRecovery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverRecovery.label', default: 'ReceiverRecovery')}" />
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
            <g:hasErrors bean="${receiverRecoveryInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverRecoveryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${receiverRecoveryInstance?.id}" />
                <g:hiddenField name="version" value="${receiverRecoveryInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="deployment"><g:message code="receiverRecovery.deployment.label" default="Deployment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'deployment', 'errors')}">
                                    <g:select name="deployment.id" from="${au.org.emii.aatams.ReceiverDeployment.list()}" optionKey="id" value="${receiverRecoveryInstance?.deployment?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="download"><g:message code="receiverRecovery.download.label" default="Download" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'download', 'errors')}">
                                    <g:select name="download.id" from="${au.org.emii.aatams.ReceiverDownload.list()}" optionKey="id" value="${receiverRecoveryInstance?.download?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="location"><g:message code="receiverRecovery.location.label" default="Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'location', 'errors')}">
                                    <g:textField name="location" value="${receiverRecoveryInstance?.location}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="recoveryDate"><g:message code="receiverRecovery.recoveryDate.label" default="Recovery Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'recoveryDate', 'errors')}">
                                    <g:datePicker name="recoveryDate" precision="day" value="${receiverRecoveryInstance?.recoveryDate}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="status"><g:message code="receiverRecovery.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverRecoveryInstance, field: 'status', 'errors')}">
                                    <g:select name="status.id" from="${au.org.emii.aatams.DeviceStatus.list()}" optionKey="id" value="${receiverRecoveryInstance?.status?.id}"  />

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
