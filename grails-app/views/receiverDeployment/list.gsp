
<%@ page import="au.org.emii.aatams.ReceiverDeployment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDeployment.label', default: 'ReceiverDeployment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
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
                            
                            <g:sortableColumn property="installation" title="${message(code: 'receiverDeployment.installation.label', default: 'Installation')}" />
                        
                            <g:sortableColumn property="station" title="${message(code: 'receiverDeployment.station.label', default: 'Station')}" />
                        
                            <g:sortableColumn property="receiver" title="${message(code: 'receiverDeployment.receiver.label', default: 'Receiver')}" />
                        
                            <g:sortableColumn property="project" title="${message(code: 'receiverDeployment.project.label', default: 'Project')}" />
                        
                            <g:sortableColumn property="deploymentDateTime" title="${message(code: 'receiverDeployment.deploymentDateTime.label', default: 'Deployment Date')}" />
                        
                            <g:sortableColumn property="recoveryDate" title="${message(code: 'receiverDeployment.recoveryDate.label', default: 'Recovery Date')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDeploymentInstanceList}" status="i" var="receiverDeploymentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${receiverDeploymentInstance.id}">.</g:link></td>
                    
                            <td><g:link controller="installation" action="show" id="${receiverDeploymentInstance?.station?.installation?.id}">${receiverDeploymentInstance?.station?.installation}</g:link></td>

                            <td><g:link controller="installationStation" action="show" id="${receiverDeploymentInstance?.station?.id}">${receiverDeploymentInstance?.station}</g:link></td>
                        
                            <td><g:link action="receiver" controller="receiver" action="show" id="${receiverDeploymentInstance?.receiver?.id}">${receiverDeploymentInstance?.receiver}</g:link></td>
                        
                            <td><g:link controller="project" action="show" id="${receiverDeploymentInstance?.station?.installation?.project?.id}">${receiverDeploymentInstance?.station?.installation?.project}</g:link></td>
                        
                            <td><joda:format value="${receiverDeploymentInstance.deploymentDateTime}" /></td>
                        
                            <td><g:formatDate date="${receiverDeploymentInstance.recoveryDate}" /></td>
                        
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
