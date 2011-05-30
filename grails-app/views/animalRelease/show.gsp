
<%@ page import="au.org.emii.aatams.AnimalRelease" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalRelease.label', default: 'AnimalRelease')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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
                            <td valign="top" class="name"><g:message code="animalRelease.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.animal.label" default="Animal" /></td>
                            
                            <td valign="top" class="value"><g:link controller="animal" action="show" id="${animalReleaseInstance?.animal?.id}">${animalReleaseInstance?.animal?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.captureDateTime.label" default="Capture Date Time" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${animalReleaseInstance?.captureDateTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.captureLocality.label" default="Capture Locality" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "captureLocality")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.captureLocation.label" default="Capture Location" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "captureLocation")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.comments.label" default="Comments" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "comments")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.measurements.label" default="Measurements" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${animalReleaseInstance.measurements}" var="m">
                                    <li><g:link controller="animalMeasurement" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.project.label" default="Project" /></td>
                            
                            <td valign="top" class="value"><g:link controller="project" action="show" id="${animalReleaseInstance?.project?.id}">${animalReleaseInstance?.project?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.releaseDateTime.label" default="Release Date Time" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${animalReleaseInstance?.releaseDateTime}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.releaseLocality.label" default="Release Locality" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "releaseLocality")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.releaseLocation.label" default="Release Location" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: animalReleaseInstance, field: "releaseLocation")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="animalRelease.surgeries.label" default="Surgeries" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${animalReleaseInstance.surgeries}" var="s">
                                    <li><g:link controller="surgery" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${animalReleaseInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
