

<%@ page import="au.org.emii.aatams.AnimalMeasurement" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalMeasurement.label', default: 'AnimalMeasurement')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${animalMeasurementInstance}">
            <div class="errors">
                <g:renderErrors bean="${animalMeasurementInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${animalMeasurementInstance?.id}" />
                <g:hiddenField name="version" value="${animalMeasurementInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="type"><g:message code="animalMeasurement.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'type', 'errors')}">
                                    <g:select name="type.id" from="${au.org.emii.aatams.AnimalMeasurementType.list()}" optionKey="id" value="${animalMeasurementInstance?.type?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="value"><g:message code="animalMeasurement.value.label" default="Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'value', 'errors')}">
                                    <g:textField name="value" value="${fieldValue(bean: animalMeasurementInstance, field: 'value')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="unit"><g:message code="animalMeasurement.unit.label" default="Unit" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'unit', 'errors')}">
                                    <g:select name="unit.id" from="${au.org.emii.aatams.MeasurementUnit.list()}" optionKey="id" value="${animalMeasurementInstance?.unit?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="estimate"><g:message code="animalMeasurement.estimate.label" default="Estimate" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'estimate', 'errors')}">
                                    <g:checkBox name="estimate" value="${animalMeasurementInstance?.estimate}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="comments"><g:message code="animalMeasurement.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'comments', 'errors')}">
                                    <g:textField name="comments" value="${animalMeasurementInstance?.comments}" />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
