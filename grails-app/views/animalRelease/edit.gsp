

<%@ page import="au.org.emii.aatams.AnimalRelease" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalRelease.label', default: 'AnimalRelease')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript src="speciesLookup.js" />
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
            <g:hasErrors bean="${animalReleaseInstance}">
            <div class="errors">
                <g:renderErrors bean="${animalReleaseInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${animalReleaseInstance?.id}" />
                <g:hiddenField name="version" value="${animalReleaseInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="project"><g:message code="animalRelease.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${candidateProjects}" optionKey="id" value="${animalReleaseInstance?.project?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="animal"><g:message code="animalRelease.animal.species.label" default="Species" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance.animal, field: 'species', 'errors')}">
<!--                                    <g:select name="species.id" from="${au.org.emii.aatams.Species.list()}" optionKey="id" value="${animalReleaseInstance?.animal?.species?.id}"  />-->
                                  <g:textField name="speciesName"  value="${animalReleaseInstance?.animal?.species?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="captureLocality"><g:message code="animalRelease.captureLocality.label" default="Capture Locality" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureLocality', 'errors')}">
                                    <g:textField name="captureLocality" value="${animalReleaseInstance?.captureLocality}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="captureLocation"><g:message code="animalRelease.captureLocation.label" default="Capture Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureLocation', 'errors')}">
                                    <g:textField name="captureLocation" value="${animalReleaseInstance?.captureLocation}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="captureDateTime"><g:message code="animalRelease.captureDateTime.label" default="Capture Date Time" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureDateTime', 'errors')}">
                                    <g:datePicker name="captureDateTime" precision="day" value="${animalReleaseInstance?.captureDateTime}"  />

                                </td>
                            </tr>
                       
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="captureMethod"><g:message code="animalRelease.captureMethod.label" default="Capture Method" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureMethod', 'errors')}">
                                    <g:select name="captureMethod.id" from="${au.org.emii.aatams.CaptureMethod.list()}" optionKey="id" value="${animalReleaseInstance?.captureMethod?.id}"  />
                                </td>
                            </tr>
                        
                            <tr>
                                <td valign="top" class="name">
                                  <label for="tagging"><g:message code="animalRelease.surgeries.label" default="Tagging" /></label>
                                </td>

                                <td valign="top" class="value">
                                  
                                  <table class="nested">
                                    <thead>
                                      <tr>
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

                                          <td valign="top" class="value">${s?.timestamp?.encodeAsHTML()}</td>
                                          <td valign="top" class="value">
                                            <g:link controller="tag" action="show" id="${s?.tag?.id}">${s?.tag}</g:link>
                                          </td>
                                          <td valign="top" class="value">${s?.type}</td>
                                          <td valign="top" class="value">${s?.treatmentType}</td>
                                          <td valign="top" class="value">${s?.comments}</td>

                                        </tr>
                                        
                                      </g:each>
                                      <tr><td><br/></td></tr>
                                      <tr>
                                        <td>
                                          <a href="#" 
                                             id='add_surgery_to_animal_release'>${message(code: 'default.add.label', args: [message(code: 'surgery.label', default: 'Tagging...')])}</a>
                                        </td>
                                      </tr>

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

                                          <td valign="top" class="value">${m?.type?.type}</td>
                                          <td valign="top" class="value">${m?.value}</td>
                                          <td valign="top" class="value">${m?.unit?.unit}</td>
                                          <td valign="top" class="value">${m?.estimate}</td>
                                          <td valign="top" class="value">${m?.comments}</td>

                                        </tr>
                                        
                                      </g:each>
                                      <tr><td><br/></td></tr>
                                      <tr>
                                        <td>
                                          <a href="#" 
                                             id='add_measurement_to_animal_release'>${message(code: 'default.add.label', args: [message(code: 'measurement.label', default: 'Measurement...')])}</a>
                                        </td>
                                      </tr>

                                    </tbody>
                                  </table>

                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="animal"><g:message code="animalRelease.animal.sex.label" default="Sex" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance.animal, field: 'sex', 'errors')}">
                                    <g:select name="sex.id" from="${au.org.emii.aatams.Sex.list()}" optionKey="id" value="${animalReleaseInstance?.animal?.sex?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="releaseLocality"><g:message code="animalRelease.releaseLocality.label" default="Release Locality" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'releaseLocality', 'errors')}">
                                    <g:textField name="releaseLocality" value="${animalReleaseInstance?.releaseLocality}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="releaseLocation"><g:message code="animalRelease.releaseLocation.label" default="Release Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'releaseLocation', 'errors')}">
                                    <g:textField name="releaseLocation" value="${animalReleaseInstance?.releaseLocation}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="releaseDateTime"><g:message code="animalRelease.releaseDateTime.label" default="Release Date Time" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'releaseDateTime', 'errors')}">
                                    <g:datePicker name="releaseDateTime" precision="day" value="${animalReleaseInstance?.releaseDateTime}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="comments"><g:message code="animalRelease.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'comments', 'errors')}">
                                    <g:textArea name="comments" value="${animalReleaseInstance?.comments}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="embargoDate"><g:message code="animalRelease.embargoDate.label" default="Embargo Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'embargoDate', 'errors')}">
                                    <g:datePicker name="embargoDate" precision="day" value="${animalReleaseInstance?.embargoDate}" default="none" noSelection="['': '']" />

                                </td>
                            </tr>
                        
                        
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </div>
            </g:form>
        </div>
      
        <!--
             Dialog presented when adding surgery to animal release.
        -->
        <div id="dialog-form-add-surgery" title="Add Tagging to Animal Release">
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="animalReleaseId"><g:message code="animalRelease.label" default="Animal Release" /></label>
                                    <g:hiddenField name="animalReleaseId" value="${animalReleaseInstance?.id}" />

                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'id', 'errors')}">
                                  <label id="project">${animalReleaseInstance}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="surgeryTimestamp"><g:message code="surgery.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'timestamp', 'errors')}">
                                    <g:datePicker name="surgeryTimestamp" precision="day" value="${surgeryInstance?.timestamp}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="tag"><g:message code="surgery.tag.label" default="Tag" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'tag', 'errors')}">
                                    <g:select name="tagId" from="${candidateTags}" optionKey="id" value="${surgeryInstance?.tag?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="surgery.type.label" default="Placement" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'type', 'errors')}">
                                    <g:select name="surgeryTypeId" from="${au.org.emii.aatams.SurgeryType.list()}" optionKey="id" value="${surgeryInstance?.type?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="treatmentType"><g:message code="surgery.treatmentType.label" default="Treatment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'treatmentType', 'errors')}">
                                    <g:select name="treatmentTypeId" from="${au.org.emii.aatams.SurgeryTreatmentType.list()}" optionKey="id" value="${surgeryInstance?.treatmentType?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="comments"><g:message code="surgery.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'comments', 'errors')}">
                                    <g:textField name="surgeryComments" value="${surgeryInstance?.comments}" />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
            </g:form>
        </div>

        <!--
             Dialog presented when adding surgery to animal release.
        -->
        <div id="dialog-form-add-measurement" title="Add Measurement to Animal Release">
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="animalReleaseId"><g:message code="animalRelease.label" default="Animal Release" /></label>
                                    <g:hiddenField name="animalReleaseId" value="${animalReleaseInstance?.id}" />

                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'id', 'errors')}">
                                  <label id="project">${animalReleaseInstance}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="animalMeasurement.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'type', 'errors')}">
                                    <g:select name="measurementTypeId" from="${au.org.emii.aatams.AnimalMeasurementType.list()}" optionKey="id" value="${animalMeasurementInstance?.type?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="value"><g:message code="animalMeasurement.value.label" default="Value" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'value', 'errors')}">
                                    <g:textField name="value" value="${fieldValue(bean: animalMeasurementInstance, field: 'value')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="unit"><g:message code="animalMeasurement.unit.label" default="Unit" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalMeasurementInstance, field: 'unit', 'errors')}">
                                    <g:select name="unitId" from="${au.org.emii.aatams.MeasurementUnit.list()}" optionKey="id" value="${animalMeasurementInstance?.unit?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="estimate"><g:message code="animalMeasurement.estimate.label" default="Estimate" /></label>
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
