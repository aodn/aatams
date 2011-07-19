
<%@ page import="au.org.emii.aatams.Tag" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tag.label', default: 'Tag')}" />
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
                            
                            <g:sortableColumn property="codeName" title="${message(code: 'tag.codeName.label', default: 'ID')}" />
                        
                            <g:sortableColumn property="model" title="${message(code: 'tag.model.label', default: 'Model')}" />
                        
                            <g:sortableColumn property="serialNumber" title="${message(code: 'tag.serialNumber.label', default: 'Serial Number')}" />
                        
                            <g:sortableColumn property="project" title="${message(code: 'device.project.label', default: 'Project')}" />

                            <g:sortableColumn property="transmitterType" title="${message(code: 'tag.transmitterType.label', default: 'Type')}" />

                            <g:sortableColumn property="codeMap" title="${message(code: 'tag.codeMap.label', default: 'Code Map')}" />
                        
                            <g:sortableColumn property="pingCode" title="${message(code: 'tag.pingCode.label', default: 'Ping ID Code')}" />
                        
                            <g:sortableColumn property="status" title="${message(code: 'tag.status.label', default: 'Status')}" />
                            
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${tagInstanceList}" status="i" var="tagInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton">
                              <g:if test="${tagInstance instanceof au.org.emii.aatams.Sensor}">
                                <g:link class="show" controller="sensor" action="show" id="${tagInstance.id}"></g:link>
                              </g:if>
                              <g:else>
                                <g:link class="show" action="show" id="${tagInstance.id}"></g:link>
                              </g:else>
                            </td>
                    
                          
                            <td>${fieldValue(bean: tagInstance, field: "codeName")}</td>
                            
                            <td>${fieldValue(bean: tagInstance, field: "model")}</td>

                            <td>${fieldValue(bean: tagInstance, field: "serialNumber")}</td>
                        
                            <td><g:link controller="project" action="show" id="${tagInstance?.project?.id}">${fieldValue(bean: tagInstance, field: "project")}</g:link></td>

                            <td>${tagInstance?.transmitterType?.transmitterTypeName}</td>

                            <td>${fieldValue(bean: tagInstance, field: "codeMap")}</td>
                        
                            <td>${tagInstance?.pingCode}</td>
                        
                            <td>${fieldValue(bean: tagInstance, field: "status")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${tagInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
