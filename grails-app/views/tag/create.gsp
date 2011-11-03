

<%@ page import="au.org.emii.aatams.Tag" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tag.label', default: 'Tag')}" />
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
            <g:hasErrors bean="${tagInstance}">
            <div class="errors">
                <g:renderErrors bean="${tagInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="project"><g:message code="tag.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" 
                                              from="${candidateProjects}" 
                                              optionKey="id" 
                                              value="${tagInstance?.project?.id}" 
                                              noSelection="['':'unassigned']" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="model"><g:message code="tag.model.label" default="Model" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'model', 'errors')}">
                                    <g:select name="model.id" from="${au.org.emii.aatams.TagDeviceModel.list()}" optionKey="id" value="${tagInstance?.model?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="serialNumber"><g:message code="tag.serialNumber.label" default="Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'serialNumber', 'errors')}">
                                    <g:textField name="serialNumber" value="${tagInstance?.serialNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="codeMap"><g:message code="tag.codeMap.label" default="Code Map" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'codeMap', 'errors')}">
                                  <g:select name="codeMap.id" from="${au.org.emii.aatams.CodeMap.list()}" optionKey="id" value="${tagInstance?.codeMap?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="pingCode"><g:message code="tag.pingCode.label" default="Ping ID Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'pingCode', 'errors')}">
                                    <g:textField name="pingCode" value="${tagInstance?.pingCode}" 
                                                 placeholder="e.g. '46601'"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="expectedLifeTimeDays"><g:message code="tag.expectedLifeTimeDays.label" default="Expected Life Time (days)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'expectedLifeTimeDays', 'errors')}">
                                    <g:textField name="expectedLifeTimeDays" value="${fieldValue(bean: tagInstance, field: 'expectedLifeTimeDays')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="status"><g:message code="tag.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'status', 'errors')}">
                                    <g:select name="status.id" from="${au.org.emii.aatams.DeviceStatus.list()}" optionKey="id" value="${tagInstance?.status?.id}"  />

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
