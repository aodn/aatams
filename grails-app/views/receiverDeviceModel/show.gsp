<%@ page import="au.org.emii.aatams.ReceiverDeviceModel" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>

            <shiro:hasRole name="SysAdmin">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasRole>
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
                            <td valign="top" class="name"><g:message code="receiverDeviceModel.modelName.label" default="Model Name" /></td>

                            <td valign="top" class="value">${fieldValue(bean: receiverDeviceModelInstance, field: "modelName")}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="receiverDeviceModel.manufacturer.label" default="Manufacturer" /></td>

                            <td valign="top" class="value"><g:link controller="deviceManufacturer" action="show" id="${receiverDeviceModelInstance?.manufacturer?.id}">${receiverDeviceModelInstance?.manufacturer?.encodeAsHTML()}</g:link></td>

                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${receiverDeviceModelInstance?.id}" />

                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    </shiro:hasRole>
                </g:form>
            </div>
        </div>
    </body>
</html>
