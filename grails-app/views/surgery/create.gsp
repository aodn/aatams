

<%@ page import="au.org.emii.aatams.Surgery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'surgery.label', default: 'Surgery')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
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
            <g:hasErrors bean="${surgeryInstance}">
            <div class="errors">
                <g:renderErrors bean="${surgeryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="timestamp"><g:message code="surgery.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'timestamp', 'errors')}">
                                    <joda:dateTimePicker name="timestamp" 
                                                         value="${surgeryInstance?.timestamp}"
                                                         useZone="true"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="release"><g:message code="surgery.release.label" default="Release" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'release', 'errors')}">
                                    <g:select name="release.id" from="${au.org.emii.aatams.AnimalRelease.list()}" optionKey="id" value="${surgeryInstance?.release?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag"><g:message code="surgery.tag.label" default="Tag" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'tag', 'errors')}">
                                    <g:select name="tag.id" from="${au.org.emii.aatams.Tag.list()}" optionKey="id" value="${surgeryInstance?.tag?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="treatmentType"><g:message code="surgery.treatmentType.label" default="Treatment Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'treatmentType', 'errors')}">
                                    <g:select name="treatmentType.id" from="${au.org.emii.aatams.SurgeryTreatmentType.list()}" optionKey="id" value="${surgeryInstance?.treatmentType?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="surgeon"><g:message code="surgery.surgeon.label" default="Surgeon" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'surgeon', 'errors')}">
                                    <g:select name="surgeon.id" from="${au.org.emii.aatams.Person.list()}" optionKey="id" value="${surgeryInstance?.surgeon?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="comments"><g:message code="surgery.comments.label" default="Comments" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'comments', 'errors')}">
                                    <g:textField name="comments" value="${surgeryInstance?.comments}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="type"><g:message code="surgery.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: surgeryInstance, field: 'type', 'errors')}">
                                    <g:select name="type.id" from="${au.org.emii.aatams.SurgeryType.list()}" optionKey="id" value="${surgeryInstance?.type?.id}"  />

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
    </body>
</html>
