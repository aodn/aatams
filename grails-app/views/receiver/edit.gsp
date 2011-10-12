

<%@ page import="au.org.emii.aatams.Receiver" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiver.label', default: 'Receiver')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasPermission permission="receiverCreate">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${receiverInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${receiverInstance?.id}" />
                <g:hiddenField name="version" value="${receiverInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="codeName"><g:message code="receiver.codeName.label" default="ID" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'codeName', 'errors')}">${receiverInstance?.codeName}</td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="project"><g:message code="receiver.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'organisation', 'errors')}">
                                    <g:select name="organisation.id" from="${au.org.emii.aatams.Organisation.listActive()}" optionKey="id" value="${receiverInstance?.organisation?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="model"><g:message code="receiver.model.label" default="Model" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'model', 'errors')}">
                                    <g:select name="model.id" from="${au.org.emii.aatams.ReceiverDeviceModel.list()}" optionKey="id" value="${receiverInstance?.model?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="serialNumber"><g:message code="receiver.serialNumber.label" default="Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'serialNumber', 'errors')}">
                                    <g:textField name="serialNumber" value="${receiverInstance?.serialNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="comment"><g:message code="receiver.comment.label" default="Comment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverInstance, field: 'comment', 'errors')}">
                                    <g:textArea name="comment" value="${receiverInstance?.comment}" />

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
