
<%@ page import="au.org.emii.aatams.AnimalRelease" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalRelease.label', default: 'AnimalRelease')}" />
        <g:set var="projectId" value="${animalReleaseInstance?.project?.id}" />
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
                            <td valign="top" class="name"><g:message code="animalRelease.project.label" default="Project" /></td>
                            
                            <td valign="top" class="value"><g:link controller="project" action="show" id="${animalReleaseInstance?.project?.id}">${animalReleaseInstance?.project?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.animal.species.label" default="Species" /></td>
                            
                            <td valign="top" class="value">${animalReleaseInstance?.animal?.species}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.animal.sex.label" default="Sex" /></td>
                            
                            <td valign="top" class="value">${animalReleaseInstance?.animal?.sex}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.captureLocality.label" default="Capture Locality" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "captureLocality")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.captureLocation.label" default="Capture Location" /></td>
                            
                            <td valign="top" class="value">
                              <g:point name="captureLocation" 
                                       value="${animalReleaseInstance?.captureLocation}"/>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.captureDateTime.label" default="Capture Date Time" /></td>
                            
                            <td valign="top" class="value"><joda:format value="${animalReleaseInstance?.captureDateTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.captureMethod.label" default="Capture Method" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "captureMethod")}</td>
                            
                        </tr>
                        <tr>
                            <td valign="top" class="name">
                              <label for="tagging"><g:message code="animalRelease.surgeries.label" default="Tagging" /></label>
                            </td>

                            <td valign="top" class="value">

                              <table class="nested">
                                <thead>
                                  <tr>
                                    <th/>
                                    <th>Date/Time</th>
                                    <th>Tag</th>
                                    <th>Placement</th>
                                    <th>Treatment</th>
                                    <th>Comments</th>
                                  </tr>
                                </thead>
                                <tbody id="surgeries_table_body">
                                  <g:each in="${animalReleaseInstance?.surgeries?}" var="s">
                                    <tr>

                                      <td class="rowButton"><g:link class="show" controller="surgery" action="show" id="${s?.id}">.</g:link></td>
                                      <td valign="top" class="value"><joda:format value="${s?.timestamp}" /></td>
                                      <td valign="top" class="value">
                                        <g:link controller="tag" action="show" id="${s?.tag?.id}">${s?.tag}</g:link>
                                      </td>
                                      <td valign="top" class="value">${s?.type}</td>
                                      <td valign="top" class="value">${s?.treatmentType}</td>
                                      <td valign="top" class="value">${s?.comments}</td>

                                    </tr>

                                  </g:each>

                                </tbody>
                              </table>

                            </td>  
                        </tr>

                        <tr>
                            <td valign="top" class="name">
                              <label for="measurements"><g:message code="animalRelease.measurements.label" default="Measurements" /></label>
                            </td>

                            <td valign="top" class="value">

                              <table class="nested">
                                <thead>
                                  <tr>
                                    <th/>
                                    <th>Type</th>
                                    <th>Value</th>
                                    <th>Units</th>
                                    <th>Estimated</th>
                                    <th>Comments</th>
                                  </tr>
                                </thead>
                                <tbody id="measurements_table_body">
                                  <g:each in="${animalReleaseInstance?.measurements?}" var="m">
                                    <tr>

                                      <td class="rowButton">
                                        <g:link class="show" 
                                                controller="animalMeasurement" 
                                                action="show"
                                                id="${m?.id}">.</g:link>
                                      </td>
                                      <td valign="top" class="value">${m?.type?.type}</td>
                                      <td valign="top" class="value">${m?.value}</td>
                                      <td valign="top" class="value">${m?.unit?.unit}</td>
                                      <td valign="top" class="value"><g:formatBoolean boolean="${m?.estimate}" true="yes" false="no"/></td>
                                      <td valign="top" class="value">${m?.comments}</td>

                                    </tr>

                                  </g:each>

                                </tbody>
                              </table>

                            </td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.releaseLocality.label" default="Release Locality" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "releaseLocality")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.releaseLocation.label" default="Release Location" /></td>
                            
                            <td valign="top" class="value">
                              <g:point name="scrambledReleaseLocation" 
                                       value="${animalReleaseInstance?.scrambledReleaseLocation}"/>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.releaseDateTime.label" default="Release Date Time" /></td>
                            
                            <td valign="top" class="value"><joda:format date="${animalReleaseInstance?.releaseDateTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.comments.label" default="Comments" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "comments")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.embargoDate.label" default="Embargo Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${animalReleaseInstance?.embargoDate}" /></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${animalReleaseInstance?.id}" />
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
