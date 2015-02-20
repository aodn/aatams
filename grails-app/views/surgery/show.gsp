
<%@ page import="au.org.emii.aatams.Surgery" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'surgery.label', default: 'Surgery')}" />
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
                            <td valign="top" class="name"><g:message code="surgery.timestamp.label" default="Timestamp" /></td>

                            <td valign="top" class="value"><joda:format value="${surgeryInstance?.timestamp}" /></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="surgery.release.label" default="Release" /></td>

                            <td valign="top" class="value"><g:link controller="animalRelease" action="show" id="${surgeryInstance?.release?.id}">${surgeryInstance?.release?.encodeAsHTML()}</g:link></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="surgery.tag.label" default="Tag" /></td>

                            <td valign="top" class="value"><g:link controller="tag" action="show" id="${surgeryInstance?.tag?.id}">${surgeryInstance?.tag?.encodeAsHTML()}</g:link></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="surgery.treatmentType.label" default="Treatment Type" /></td>

                            <td valign="top" class="value"><g:link controller="surgeryTreatmentType" action="show" id="${surgeryInstance?.treatmentType?.id}">${surgeryInstance?.treatmentType?.encodeAsHTML()}</g:link></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="surgery.comments.label" default="Comments" /></td>

                            <td valign="top" class="value">${fieldValue(bean: surgeryInstance, field: "comments")}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="surgery.type.label" default="Type" /></td>

                            <td valign="top" class="value"><g:link controller="surgeryType" action="show" id="${surgeryInstance?.type?.id}">${surgeryInstance?.type?.encodeAsHTML()}</g:link></td>

                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${surgeryInstance?.id}" />
                    <g:hiddenField name="projectId" value="${surgeryInstance?.release?.project?.id}" />
                    <shiro:hasPermission permission="project:${surgeryInstance?.release?.project?.id}:write">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasPermission>
                </g:form>
            </div>
        </div>
    </body>
</html>
