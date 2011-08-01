
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
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
              
                <table>
                    <thead>
                      <th colspan="7">Deployment Details</th>
                      <th colspan="5">Recovery Details</th>
                    </thead>
                    <thead>
                        <tr>
                        
                            <td/>
                            
                            <g:sortableColumn property="deploymentDateTime" title="${message(code: 'receiverDeployment.deploymentDateTime.label', default: 'Deployment Date')}" />

                            <g:sortableColumn property="station.installation" title="${message(code: 'receiverDeployment.installation.label', default: 'Installation')}" />
                        
                            <g:sortableColumn property="station" title="${message(code: 'receiverDeployment.station.label', default: 'Station')}" />
                        
                            <th><g:message code="receiverDeployment.station.location.label" default="Location" /></th>
                        
                            <g:sortableColumn property="receiver" title="${message(code: 'receiverDeployment.receiver.label', default: 'Receiver')}" />
                        
                            <th><g:message code="receiverDeployment.station.depth.label" default="Depth" /></th>
                        
                            <!-- New/edit column -->
                            <g:sortableColumn property="recovery" title="${message(code: 'receiverRecovery.label', default: 'Recovery')}" />
                            
                            <g:sortableColumn property="recovery.recoverer" title="${message(code: 'receiverRecovery.recoverer.label', default: 'Recovered By')}" />

                            <th><g:message code="receiverRecovery.location" default="Location" /></th>
                        
                            <g:sortableColumn property="recovery.recoveryDateTime" title="${message(code: 'receiverRecovery.recoveryDateTime.label', default: 'Recovery Date')}" />
                        
                            <g:sortableColumn property="recovery.status" title="${message(code: 'receiverRecovery.status.label', default: 'Status')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDeploymentInstanceList}" status="i" var="receiverDeployment">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td class="rowButton"><g:link class="show" controller="receiverDeployment" action="show" id="${receiverDeployment.id}"></g:link></td>
                    
                            <td><joda:format value="${receiverDeployment.deploymentDateTime}" /></td>
                        
                            <td><g:link controller="installation" action="show" id="${receiverDeployment?.station?.installation?.id}">${receiverDeployment?.station?.installation}</g:link></td>

                            <td><g:link controller="installationStation" action="show" id="${receiverDeployment?.station?.id}">${receiverDeployment?.station}</g:link></td>
                        
                            <td>
                              <g:point name="scrambledLocation"
                                       value="${receiverDeployment?.station?.scrambledLocation}" />
                            </td>
                            
                            <td><g:link action="receiver" controller="receiver" action="show" id="${receiverDeployment?.receiver?.id}">${receiverDeployment?.receiver}</g:link></td>

                            <td>${fieldValue(bean: receiverDeployment, field: "depthBelowSurfaceM")}</td>

                            <td class="rowButton">
                              <g:if test="${receiverDeployment?.recovery == null}">
                                <shiro:hasPermission permission="project:${projectId:receiverDeployment?.station?.installation?.project?.id}:write">
                                  <g:link class="create" action="create" 
                                          params="[deploymentId:receiverDeployment.id, projectId:receiverDeployment?.station?.installation?.project?.id]"></g:link>
                                </shiro:hasPermission>  
                              </g:if>
                              <g:else>
                                <g:link class="show" action="show" id="${receiverDeployment?.recovery?.id}"></g:link>
                              </g:else>
                            </td>
                            
                            <td>${fieldValue(bean: receiverDeployment?.recovery?.recoverer, field: "person")}</td>

                            <td>
                              <g:point name="scrambledLocation"
                                       value="${receiverDeployment?.recovery?.scrambledLocation}" />
                            </td>

                            <td><joda:format value="${receiverDeployment?.recovery?.recoveryDateTime}" /></td>

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
