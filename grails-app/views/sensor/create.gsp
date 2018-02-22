<%@ page import="au.org.emii.aatams.Sensor" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:javascript src="sensorCreate.js"/>
        <g:set var="entityName" value="${message(code: 'tag.label', default: 'Tag')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> (or Sensor)</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${sensorInstance}">
                <div class="errors">
                    <g:renderErrors bean="${sensorInstance}" as="list" />
                </div>
            </g:hasErrors>
            <g:hasErrors bean="${sensorInstance?.tag}">
                <div class="errors">
                    <g:renderErrors bean="${sensorInstance?.tag}" as="list" />
                </div>
            </g:hasErrors>
            <g:form action="createNew">
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <!--  Parent tag properties. -->
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag.serialNumber"><g:message code="tag.serialNumber.label" default="Serial Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance.tag, field: 'serialNumber', 'errors')}">
                                    <g:textField name="tag.serialNumber" value="${sensorInstance.tag?.serialNumber}" placeholder="autocomplete - start typing"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag.project"><g:message code="tag.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance.tag, field: 'project', 'errors')}">
                                    <g:select name="tag.project.id" 
                                              class="remember"
                                              from="${candidateProjects}" 
                                              optionKey="id" 
                                              value="${sensorInstance.tag?.project?.id}" 
                                              noSelection="['':'unassigned']" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag.model"><g:message code="tag.model.label" default="Model" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance.tag, field: 'model', 'errors')}">
                                    <g:select name="tag.model.id" class="remember" from="${au.org.emii.aatams.TagDeviceModel.list()}" optionKey="id" value="${sensorInstance.tag?.model?.id}"  />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag.codeMap"><g:message code="tag.codeMap.label" default="Code Map" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance.tag, field: 'codeMap', 'errors')}">
                                  <g:select name="tag.codeMap.id" class="remember" from="${au.org.emii.aatams.CodeMap.list()}" optionKey="id" value="${sensorInstance.tag?.codeMap?.id}"  />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag.expectedLifeTimeDays"><g:message code="tag.expectedLifeTimeDays.label" default="Expected Life Time (days)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance.tag, field: 'expectedLifeTimeDays', 'errors')}">
                                    <g:textField name="tag.expectedLifeTimeDays" class="remember" value="${sensorInstance.tag?.expectedLifeTimeDays}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="tag.status"><g:message code="tag.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance.tag, field: 'status', 'errors')}">
                                    <g:select name="tag.status" from="${au.org.emii.aatams.DeviceStatus.values()}" optionKey="key" value="${sensorInstance.tag?.status?.key}"  />

                                </td>
                            </tr>


                            <!--  Sensor specific properties. -->
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="transmitterType"><g:message code="sensor.transmitterType.label" default="Transmitter Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance, field: 'transmitterType', 'errors')}">
                                    <g:select name="transmitterType.id" from="${au.org.emii.aatams.TransmitterType.list()}"  optionKey="id" value="${sensorInstance?.transmitterType?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="pingCode"><g:message code="sensor.pingCode.label" default="Ping Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance, field: 'pingCode', 'errors')}">
                                    <g:textField name="pingCode" value="${sensorInstance?.pingCode}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="slope"><g:message code="sensor.slope.label" default="Slope" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance, field: 'slope', 'errors')}">
                                    <g:textField name="slope" value="${fieldValue(bean: sensorInstance, field: 'slope')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="intercept"><g:message code="sensor.intercept.label" default="Intercept" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance, field: 'intercept', 'errors')}">
                                    <g:textField name="intercept" value="${fieldValue(bean: sensorInstance, field: 'intercept')}" />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="unit"><g:message code="sensor.unit.label" default="Unit" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: sensorInstance, field: 'unit', 'errors')}">
                                    <g:textField name="unit" readonly="readonly"  value="${sensorInstance?.unit}" from="${au.org.emii.aatams.Sensor.constraints.unit.inList}" />

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
