

<%@ page import="au.org.emii.aatams.AnimalRelease" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalRelease.label', default: 'AnimalRelease')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript src="speciesLookup.js" />
        <g:javascript src="animalLookup.js" />
        <g:javascript src="addsurgerytoanimalrelease.js"/>
        <g:javascript src="addmeasurementtoanimalrelease.js"/>
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
                                  <g:hiddenField name="speciesId" value="${animalReleaseInstance?.animal?.species?.id}"/>
                                  <g:textField name="speciesName" value="${animalReleaseInstance?.animal?.species?.name}" />
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
                                              noSelection="['':'Not specified']" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="animal"><g:message code="animalRelease.animal.label" default="Animal" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: animalReleaseInstance, field: 'animal', 'errors')}">
                                    <g:select name="animal.id" 
                                              from="${animalReleaseInstance?.animal}"
                                              optionKey="id" 
                                              noSelection="['':'New animal']"
                                              value="${animalReleaseInstance?.animal?.id}" />
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

                                          <td class="rowButton"><g:link class="show" controller="surgery" action="show" id="${s?.id}"></g:link></td>
                                          <td valign="top" class="value"><joda:format value="${s?.timestamp}" /></td>
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

                                          <td class="rowButton"><g:link class="show" controller="animalMeasurement" action="show" id="${m?.id}"></g:link></td>
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
        <div id='dialog-form-add-surgery'></div>
        
        <!--
             Dialog presented when adding measurement to animal release.
        -->
        <div id='dialog-form-add-measurement'></div>

    </body>
</html>
