
<%@ page import="au.org.emii.aatams.ReceiverDownload" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownload.label', default: 'ReceiverDownload')}" />
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
                            <td valign="top" class="name"><g:message code="receiverDownload.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.receiverRecovery.label" default="Receiver Recovery" /></td>
                            
                            <td valign="top" class="value"><g:link controller="receiverRecovery" action="show" id="${receiverDownloadInstance?.receiverRecovery?.id}">${receiverDownloadInstance?.receiverRecovery?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.downloadDate.label" default="Download Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${receiverDownloadInstance?.downloadDate}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.clockDrift.label" default="Clock Drift" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadInstance, field: "clockDrift")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.pingCount.label" default="Ping Count" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadInstance, field: "pingCount")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.detectionCount.label" default="Detection Count" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadInstance, field: "detectionCount")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.comments.label" default="Comments" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadInstance, field: "comments")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.downloader.label" default="Downloader" /></td>
                            
                            <td valign="top" class="value"><g:link controller="person" action="show" id="${receiverDownloadInstance?.downloader?.id}">${receiverDownloadInstance?.downloader?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.batteryVoltage.label" default="Battery Voltage" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadInstance, field: "batteryVoltage")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.batteryDays.label" default="Battery Days" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: receiverDownloadInstance, field: "batteryDays")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDownload.downloadFiles.label" default="Download Files" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${receiverDownloadInstance.downloadFiles}" var="d">
                                    <li><g:link controller="receiverDownloadFile" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${receiverDownloadInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
