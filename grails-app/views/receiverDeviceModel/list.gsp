<%@ page import="au.org.emii.aatams.ReceiverDeviceModel" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDeviceModel.label', default: 'ReceiverDeviceModel')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasRole name="SysAdmin">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasRole>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>

            <div class="list">
                <table>
                    <thead>
                        <tr>

                            <td/>

                            <g:sortableColumn property="modelName" title="${message(code: 'receiverDeviceModel.modelName.label', default: 'Model Name')}" />

                            <th><g:message code="receiverDeviceModel.manufacturer.label" default="Manufacturer" /></th>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDeviceModelInstanceList}" status="i" var="receiverDeviceModelInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td class="rowButton"><g:link class="show" action="show" id="${receiverDeviceModelInstance.id}">.</g:link></td>

                            <td>${fieldValue(bean: receiverDeviceModelInstance, field: "modelName")}</td>

                            <td>${fieldValue(bean: receiverDeviceModelInstance, field: "manufacturer")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${receiverDeviceModelInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
