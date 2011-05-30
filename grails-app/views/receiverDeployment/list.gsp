
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
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'receiverDeployment.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="acousticReleaseID" title="${message(code: 'receiverDeployment.acousticReleaseID.label', default: 'Acoustic Release ID')}" />
                        
                            <g:sortableColumn property="bottomDepthM" title="${message(code: 'receiverDeployment.bottomDepthM.label', default: 'Bottom Depth M')}" />
                        
                            <g:sortableColumn property="comments" title="${message(code: 'receiverDeployment.comments.label', default: 'Comments')}" />
                        
                            <g:sortableColumn property="deploymentDate" title="${message(code: 'receiverDeployment.deploymentDate.label', default: 'Deployment Date')}" />
                        
                            <g:sortableColumn property="deploymentNumber" title="${message(code: 'receiverDeployment.deploymentNumber.label', default: 'Deployment Number')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${receiverDeploymentInstanceList}" status="i" var="receiverDeploymentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${receiverDeploymentInstance.id}">${fieldValue(bean: receiverDeploymentInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: receiverDeploymentInstance, field: "acousticReleaseID")}</td>
                        
                            <td>${fieldValue(bean: receiverDeploymentInstance, field: "bottomDepthM")}</td>
                        
                            <td>${fieldValue(bean: receiverDeploymentInstance, field: "comments")}</td>
                        
                            <td><g:formatDate date="${receiverDeploymentInstance.deploymentDate}" /></td>
                        
                            <td>${fieldValue(bean: receiverDeploymentInstance, field: "deploymentNumber")}</td>
                        
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
