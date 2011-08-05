<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add Tagging to Animal Release</title>
  </head>
  <body>
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
                                    <label class="compulsory" for="surgeryTimestamp"><g:message code="surgery.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'timestamp', 'errors')}">
                                    <joda:dateTimePicker name="surgeryTimestamp" 
                                                         value="${surgeryInstance?.timestamp}"
                                                         useZone="true"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag"><g:message code="surgery.tag.label" default="Tag" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'tag', 'errors')}">
                                    <g:select name="tagId" from="${candidateTags}" optionKey="id" value="${surgeryInstance?.tag?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="type"><g:message code="surgery.type.label" default="Placement" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'type', 'errors')}">
                                    <g:select name="surgeryTypeId" from="${au.org.emii.aatams.SurgeryType.list()}" optionKey="id" value="${surgeryInstance?.type?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="treatmentType"><g:message code="surgery.treatmentType.label" default="Treatment" /></label>
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
  </body>
</html>
