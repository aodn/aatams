
<%@ page import="au.org.emii.aatams.ReceiverRecovery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverRecovery.label', default: 'ReceiverRecovery')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
              
                <table>
                    <thead>
                      <th colspan="6">Deployment Details</th>
                      <th></th>
                      <th colspan="4">Recovery Details</th>
                    </thead>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="deploymentDate" title="${message(code: 'receiverDeployment.deploymentDate.label', default: 'Deployment Date')}" />

                            <g:sortableColumn property="installation" title="${message(code: 'receiverDeployment.installation.label', default: 'Installation')}" />
                        
                            <g:sortableColumn property="station" title="${message(code: 'receiverDeployment.station.label', default: 'Station')}" />
                        
                            <th><g:message code="receiverDeployment.station.location.label" default="Location" /></th>
                        
                            <g:sortableColumn property="receiver" title="${message(code: 'receiverDeployment.receiver.label', default: 'Receiver')}" />
                        
                            <th><g:message code="receiverDeployment.station.depth.label" default="Depth" /></th>
                        
                            <!-- New/edit column -->
                            <th></th>
                            
                            <g:sortableColumn property="recoverer" title="${message(code: 'receiverRecovery.recoverer.label', default: 'Recovered By')}" />

                            <th><g:message code="receiverRecovery.location" default="Location" /></th>
                        
                            <g:sortableColumn property="recoveryDate" title="${message(code: 'receiverRecovery.recoveryDate.label', default: 'Recovery Date')}" />
                        
                            <g:sortableColumn property="status" title="${message(code: 'receiverRecovery.status.label', default: 'Status')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDeploymentInstanceList}" status="i" var="receiverDeployment">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:formatDate date="${receiverDeployment.deploymentDate}" /></td>
                        
                            <td><g:link controller="installation" action="show" id="${receiverDeployment?.station?.installation?.id}">${receiverDeployment?.station?.installation}</g:link></td>

                            <td><g:link controller="installationStation" action="show" id="${receiverDeployment?.station?.id}">${receiverDeployment?.station}</g:link></td>
                        
                            <td>${fieldValue(bean: receiverDeployment?.station, field: "location")}</td>
                            
                            <td><g:link action="receiver" controller="receiver" action="show" id="${receiverDeployment?.receiver?.id}">${receiverDeployment?.receiver}</g:link></td>

                            <td>${fieldValue(bean: receiverDeployment, field: "depthBelowSurfaceM")}</td>

                            <td>
                              <g:if test="${receiverDeployment?.recovery == null}">
                                <g:link class="create" action="create" params="[deploymentId:receiverDeployment.id]"><g:message code="default.new.label" args="[entityName]" /></g:link>
                              </g:if>
                              <g:else>
                                <g:link action="show" id="${receiverDeployment?.recovery?.id}">Details</g:link>
                              </g:else>
                            </td>
                            
                            <td>${fieldValue(bean: receiverDeployment?.recovery?.recoverer, field: "person")}</td>

                            <td>${fieldValue(bean: receiverDeployment?.recovery, field: "location")}</td>

                            <td><g:formatDate date="${receiverDeployment?.recovery?.recoveryDate}" /></td>

                            <td>${fieldValue(bean: receiverDeployment?.recovery, field: "status")}</td>

                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${receiverDeploymentInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
