<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add Measurement to Animal Release</title>
  </head>
  <body>
        <div id="dialog-form-add-measurement" title="Add Measurement to Animal Release">
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="animalReleaseId"><g:message code="animalRelease.label" default="Animal Release" /></label>
                                    <g:hiddenField name="animalReleaseId" value="${animalReleaseInstance?.id}" />

                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'id', 'errors')}">
                                  <label id="project">${animalReleaseInstance}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="type"><g:message code="animalMeasurement.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'type', 'errors')}">
                                    <g:select name="measurementTypeId" from="${au.org.emii.aatams.AnimalMeasurementType.list()}" optionKey="id" value="${animalMeasurementInstance?.type?.id}"  />

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
                                    <g:select name="unitId" from="${au.org.emii.aatams.MeasurementUnit.list()}" optionKey="id" value="${animalMeasurementInstance?.unit?.id}"  />

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
                                    <label for="measurementComments"><g:message code="animalMeasurement.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'comments', 'errors')}">
                                    <g:textField name="measurementComments" value="${animalMeasurementInstance?.comments}" />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
            </g:form>
        </div>
  </body>
</html>
