

<%@ page import="au.org.emii.aatams.ReceiverDownload" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownload.label', default: 'ReceiverDownload')}" />
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
            <g:hasErrors bean="${receiverDownloadInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverDownloadInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
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
                                    <label for="downloadDate"><g:message code="receiverDownload.downloadDate.label" default="Download Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadInstance, field: 'downloadDate', 'errors')}">
                                    <g:datePicker name="downloadDate" precision="day" value="${receiverDownloadInstance?.downloadDate}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="clockDrift"><g:message code="receiverDownload.clockDrift.label" default="Clock Drift (s)" /></label>
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
