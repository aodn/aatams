

<%@ page import="au.org.emii.aatams.ReceiverEvent" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverEvent.label', default: 'ReceiverEvent')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.upload.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${receiverEventInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverEventInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${receiverEventInstance?.id}" />
                <g:hiddenField name="version" value="${receiverEventInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="timestamp"><g:message code="receiverEvent.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverEventInstance, field: 'timestamp', 'errors')}">
                                    <g:datePicker name="timestamp" precision="day" value="${receiverEventInstance?.timestamp}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="description"><g:message code="receiverEvent.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverEventInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${receiverEventInstance?.description}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="data"><g:message code="receiverEvent.data.label" default="Data" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverEventInstance, field: 'data', 'errors')}">
                                    <g:textField name="data" value="${receiverEventInstance?.data}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="units"><g:message code="receiverEvent.units.label" default="Units" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverEventInstance, field: 'units', 'errors')}">
                                    <g:textField name="units" value="${receiverEventInstance?.units}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="receiverDeployment"><g:message code="receiverEvent.receiverDeployment.label" default="Receiver Deployment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverEventInstance, field: 'receiverDeployment', 'errors')}">
                                    <g:select name="receiverDeployment.id" from="${au.org.emii.aatams.ReceiverDeployment.list()}" optionKey="id" value="${receiverEventInstance?.receiverDeployment?.id}"  />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </div>
            </g:form>
        </div>
    </body>
</html>
