

<%@ page import="au.org.emii.aatams.ReceiverDownload" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownload.label', default: 'ReceiverDownload')}" />
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
            <g:hasErrors bean="${receiverDownloadInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverDownloadInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${receiverDownloadInstance?.id}" />
                <g:hiddenField name="version" value="${receiverDownloadInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="receiverRecovery"><g:message code="receiverDownload.receiverRecovery.label" default="Receiver Recovery" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'receiverRecovery', 'errors')}">
                                    <g:select name="receiverRecovery.id" from="${au.org.emii.aatams.ReceiverRecovery.list()}" optionKey="id" value="${receiverDownloadInstance?.receiverRecovery?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="downloadDateTime"><g:message code="receiverDownload.downloadDateTime.label" default="Download Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'downloadDateTime', 'errors')}">
                                    <joda:dateTimePicker name="downloadDateTime" 
                                                         value="${receiverDownloadInstance?.downloadDateTime}"
                                                         useZone="true"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="clockDrift"><g:message code="receiverDownload.clockDrift.label" default="Clock Drift" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'clockDrift', 'errors')}">
                                    <g:textField name="clockDrift" value="${fieldValue(bean: receiverDownloadInstance, field: 'clockDrift')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="pingCount"><g:message code="receiverDownload.pingCount.label" default="Ping Count" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'pingCount', 'errors')}">
                                    <g:textField name="pingCount" value="${fieldValue(bean: receiverDownloadInstance, field: 'pingCount')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="detectionCount"><g:message code="receiverDownload.detectionCount.label" default="Detection Count" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'detectionCount', 'errors')}">
                                    <g:textField name="detectionCount" value="${fieldValue(bean: receiverDownloadInstance, field: 'detectionCount')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="comments"><g:message code="receiverDownload.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'comments', 'errors')}">
                                    <g:textField name="comments" value="${receiverDownloadInstance?.comments}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="downloader"><g:message code="receiverDownload.downloader.label" default="Downloader" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'downloader', 'errors')}">
                                    <g:select name="downloader.id" from="${au.org.emii.aatams.Person.list()}" optionKey="id" value="${receiverDownloadInstance?.downloader?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="batteryVoltage"><g:message code="receiverDownload.batteryVoltage.label" default="Battery Voltage" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'batteryVoltage', 'errors')}">
                                    <g:textField name="batteryVoltage" value="${fieldValue(bean: receiverDownloadInstance, field: 'batteryVoltage')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="batteryDays"><g:message code="receiverDownload.batteryDays.label" default="Battery Days" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'batteryDays', 'errors')}">
                                    <g:textField name="batteryDays" value="${fieldValue(bean: receiverDownloadInstance, field: 'batteryDays')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="downloadFiles"><g:message code="receiverDownload.downloadFiles.label" default="Download Files" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'downloadFiles', 'errors')}">
                                    <g:select name="downloadFiles" from="${au.org.emii.aatams.ReceiverDownloadFile.list()}" multiple="yes" optionKey="id" size="5" value="${receiverDownloadInstance?.downloadFiles*.id}" />

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
