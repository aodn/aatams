
<%@ page import="au.org.emii.aatams.ReceiverEvent" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverEvent.label', default: 'ReceiverEvent')}" />
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
                        
                            <th></th>

                            <g:sortableColumn property="timestamp" title="${message(code: 'receiverEvent.timestamp.label', default: 'Timestamp')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'receiverEvent.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="data" title="${message(code: 'receiverEvent.data.label', default: 'Data')}" />
                        
                            <g:sortableColumn property="units" title="${message(code: 'receiverEvent.units.label', default: 'Units')}" />
                        
                            <th><g:message code="receiverEvent.receiverDeployment.label" default="Receiver Deployment" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverEventInstanceList}" status="i" var="receiverEventInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${receiverEventInstance.id}">.</g:link></td>
                      
                            <td><g:formatDate date="${receiverEventInstance.timestamp}"
                                              format="yyyy-MM-dd'T'HH:mm:ssZ"
                                              timeZone='${TimeZone.getTimeZone("UTC")}'/></td>
                        
                            <td>${fieldValue(bean: receiverEventInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: receiverEventInstance, field: "data")}</td>
                        
                            <td>${fieldValue(bean: receiverEventInstance, field: "units")}</td>
                        
                            <td><g:link controller="receiverDeployment" action="show" id="${receiverEventInstance?.receiverDeployment?.id}">${fieldValue(bean: receiverEventInstance, field: "receiverDeployment")}</g:link></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${receiverEventInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
