

<%@ page import="au.org.emii.aatams.AnimalRelease" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalRelease.label', default: 'AnimalRelease')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
        
        <g:javascript src="speciesLookup.js" />
        <g:javascript src="animalLookup.js" />
        <g:javascript src="addsurgerytoanimalrelease.js"/>
        <g:javascript src="addmeasurementtoanimalrelease.js"/>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${animalReleaseInstance}">
            <div class="errors">
                <g:renderErrors bean="${animalReleaseInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:hasErrors bean="${animalInstance}">
            <div class="errors">
                <g:renderErrors bean="${animalInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="project"><g:message code="animalRelease.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" from="${candidateProjects}" optionKey="id" value="${animalReleaseInstance?.project?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="species"><g:message code="animalRelease.animal.species.label" default="Species" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance.animal, field: 'species', 'errors')}">
                                  <g:hiddenField name="speciesId" />
                                  <g:textField name="speciesName" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="sex"><g:message code="animalRelease.animal.sex.label" default="Sex" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance.animal, field: 'sex', 'errors')}">
                                    <g:select name="sex.id" 
                                              from="${au.org.emii.aatams.Sex.list()}" 
                                              optionKey="id" value="${animalReleaseInstance?.animal?.sex?.id}" 
                                              noSelection="['':'Not specified']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="animal"><g:message code="animalRelease.animal.label" default="Animal" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'animal', 'errors')}">
                                    <g:select name="animal.id" 
                                              optionKey="id" 
                                              noSelection="['':'New animal']"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="captureLocality"><g:message code="animalRelease.captureLocality.label" default="Capture Locality" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureLocality', 'errors')}">
                                    <g:textField name="captureLocality" value="${animalReleaseInstance?.captureLocality}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="captureLocation"><g:message code="animalRelease.captureLocation.label" default="Capture Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureLocation', 'errors')}">
                                  <g:point name="captureLocation" 
                                           value="${animalReleaseInstance?.captureLocation}"
                                           editable="${true}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="captureDateTime"><g:message code="animalRelease.captureDateTime.label" default="Capture Date Time" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureDateTime', 'errors')}">
                                    <joda:dateTimePicker name="captureDateTime" 
                                                         value="${animalReleaseInstance?.captureDateTime}"
                                                         useZone="true"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="captureMethod"><g:message code="animalRelease.captureMethod.label" default="Capture Method" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'captureMethod', 'errors')}">
                                    <g:select name="captureMethod.id" from="${au.org.emii.aatams.CaptureMethod.list()}" optionKey="id" value="${animalReleaseInstance?.captureMethod?.id}"  />
                                </td>
                            </tr>
                        
                            <!-- Surgeries -->
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

                                      <tr><td><br/></td></tr>
                                      <tr>
                                        <td colspan="5">
                                          <a href="#" 
                                             id='add_surgery_to_animal_release'>${message(code: 'default.add.label', args: [message(code: 'surgery.label', default: 'Tagging...')])}</a>
                                        </td>
                                      </tr>

                                    </tbody>
                                  </table>

                                </td>  
                            </tr>
                            
                            <!-- Measurements -->
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

                                      <tr><td><br/></td></tr>
                                      <tr>
                                        <td colspan="5">
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
                                    <label class="compulsory" for="releaseLocality"><g:message code="animalRelease.releaseLocality.label" default="Release Locality" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'releaseLocality', 'errors')}">
                                    <g:textField name="releaseLocality" value="${animalReleaseInstance?.releaseLocality}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="releaseLocation"><g:message code="animalRelease.releaseLocation.label" default="Release Location" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'releaseLocation', 'errors')}">
                                  <g:point name="releaseLocation" 
                                           value="${animalReleaseInstance?.releaseLocation}"
                                           editable="${true}"/>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="releaseDateTime"><g:message code="animalRelease.releaseDateTime.label" default="Release Date Time" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'releaseDateTime', 'errors')}">
                                    <joda:dateTimePicker name="releaseDateTime" 
                                                         value="${animalReleaseInstance?.releaseDateTime}"
                                                         useZone="true"/>

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
                                    <label for="embargoPeriod"><g:message code="animalRelease.embargoDate.label" default="Embargo Period" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'embargoDate', 'errors')}">
                                    <g:select from="${embargoPeriods}"
                                              name="embargoPeriod" 
                                              optionKey="key"
                                              optionValue="value"
                                              noSelection="['':'No embargo']"/>
                                </td>
                            </tr>
                        
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
      
        <div id='dialog-form-add-surgery'></div>
        <div id='dialog-form-add-measurement'></div>
    </body>
</html>
