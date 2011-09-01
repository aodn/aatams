

<%@ page import="au.org.emii.aatams.DetectionSurgery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'detectionSurgery.label', default: 'DetectionSurgery')}" />
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
            <g:hasErrors bean="${detectionSurgeryInstance}">
            <div class="errors">
                <g:renderErrors bean="${detectionSurgeryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="detection"><g:message code="detectionSurgery.detection.label" default="Detection" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionSurgeryInstance, field: 'detection', 'errors')}">
                                    <g:select optionKey="id" from="${au.org.emii.aatams.Detection.list()}" name="detection.id" value="${detectionSurgeryInstance?.detection?.id}" ></g:select>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="surgery"><g:message code="detectionSurgery.surgery.label" default="Surgery" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionSurgeryInstance, field: 'surgery', 'errors')}">
                                    <g:select optionKey="id" from="${au.org.emii.aatams.Surgery.list()}" name="surgery.id" value="${detectionSurgeryInstance?.surgery?.id}" ></g:select>
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
