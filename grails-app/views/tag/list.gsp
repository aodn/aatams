
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
            
            <g:listFilter name="tag" />
            
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <td/>
                            
                            <%--<g:sortableColumn property="deviceID" title="${message(code: 'tag.codeName.label', default: 'IDs')}" params="${executedFilter}"/> --%>
                            <th><g:message code="deviceID.label" default="IDs" /></th>
                        
                            <g:sortableColumn property="model" title="${message(code: 'tag.model.label', default: 'Model')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="serialNumber" title="${message(code: 'tag.serialNumber.label', default: 'Serial Number')}" params="${executedFilter}"/>
                        
                            <%--<g:sortableColumn property="project.name" title="${message(code: 'device.project.label', default: 'Project')}" params="${executedFilter}"/> --%>
                            <th><g:message code="project.name.label" default="Project" /></th>

                            <g:sortableColumn property="codeMap" title="${message(code: 'tag.codeMap.label', default: 'Code Map')}" params="${executedFilter}"/>
                        
                            <%--<g:sortableColumn property="pingCodes" title="${message(code: 'tag.pingCodes.label', default: 'Ping ID Codes')}" params="${executedFilter}"/> --%>
                            <th><g:message code="pingCodes.label" default="Ping Codes" /></th>
                        
                            <th>Sensors</th>
                            
                            <g:sortableColumn property="status" title="${message(code: 'tag.status.label', default: 'Status')}" params="${executedFilter}"/>
                            
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${entityList}" status="i" var="tagInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton">
                              <g:link class="show" action="show" id="${tagInstance.id}"></g:link>
                            </td>
                    
                          
                            <td>${fieldValue(bean: tagInstance, field: "deviceID")}</td>
                            
                            <td>${fieldValue(bean: tagInstance, field: "model")}</td>

                            <td>${fieldValue(bean: tagInstance, field: "serialNumber")}</td>
                        
                            <td><g:link controller="project" action="show" id="${tagInstance?.project?.id}">${fieldValue(bean: tagInstance, field: "project")}</g:link></td>

                            <td>${fieldValue(bean: tagInstance, field: "codeMap")}</td>
                        
                            <td>${tagInstance?.pingCodes}</td>
                        
                            <td>${tagInstance?.sensors*.transmitterType.transmitterTypeName}</td>
                            
                            <td>${fieldValue(bean: tagInstance, field: "status")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${total}" params="${executedFilter}"/>
            </div>
        </div>
    </body>
</html>
