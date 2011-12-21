
<%@ page import="au.org.emii.aatams.Tag" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tag.label', default: 'Tag')}" />
        <g:set var="projectId" value="${tagInstance?.project?.id}" />

        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
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
                            <td valign="top" class="name"><g:message code="tag.codeName.label" default="ID" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: tagInstance, field: "codeName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.project.label" default="Project" /></td>
                            
                            <td valign="top" class="value"><g:link controller="project" action="show" id="${tagInstance?.project?.id}">${tagInstance?.project?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.model.label" default="Model" /></td>
                            
                            <td valign="top" class="value">${tagInstance?.model?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.serialNumber.label" default="Serial Number" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: tagInstance, field: "serialNumber")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.codeMap.label" default="Code Map" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: tagInstance, field: "codeMap")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.pingCode.label" default="Ping ID Code" /></td>
                            
                            <td valign="top" class="value">${tagInstance?.pingCode}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.expectedLifeTimeDays.label" default="Expected Life Time (days)" /></td>
        
                            <td valign="top" class="value">${tagInstance?.expectedLifeTimeDays}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.status.label" default="Status" /></td>
                            
                            <td valign="top" class="value">${tagInstance?.status?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="tag.sensors.label" default="Sensors" /></td>
                            
                            <td>
                              <table class="nested">
                                <thead>
                                  <tr>
                                    <th></th>  <!-- Link to sensor/show -->
                                    <th>Tag Type</th>
                                    <th>Code Map</th>
                                    <th>Ping Code</th>
                                    <th>Slope</th>
                                    <th>Intercept</th>
                                    <th>Unit</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  <g:each in="${tagInstance.sensors}" var="s">
                                    <tr>
                                      <td class="rowButton">
                                        <g:link class="show" controller="sensor" action="show" id="${s?.id}"></g:link>
                                      </td>
                                      <td>${s?.transmitterType}</td>
                                      <td>${s?.codeMap}</td>
                                      <td>${s?.pingCode}</td>
                                      <td>${s?.slope}</td>
                                      <td>${s?.intercept}</td>
                                      <td>${s?.unit}</td>
                                    </tr>

                                  </g:each>
                                </tbody>
                              </table>
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${tagInstance?.id}" />
                    <g:hiddenField name="project.id" value="${projectId}" />

                    <shiro:hasPermission permission="project:${projectId}:write">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    </shiro:hasPermission>
                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </g:form>
            </div>
        </div>
    </body>
</html>
