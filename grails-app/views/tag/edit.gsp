<%@ page import="au.org.emii.aatams.Tag" %>
<%@ page import="au.org.emii.aatams.TransmitterType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tag.label', default: 'Tag')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <script type="text/javascript" src="${resource(dir:'js',file:'addsensortotag.js')}"></script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link controller="sensor" class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" controller="sensor" action="create"><g:message code="default.new.label" args="[entityName]" /> (or Sensor)</g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${tagInstance}">
            <div class="errors">
                <g:renderErrors bean="${tagInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${tagInstance?.id}" />
                <g:hiddenField name="version" value="${tagInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="deviceID"><g:message code="tag.deviceID.label" default="IDs" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'deviceID', 'errors')}">${tagInstance?.deviceID}</td>
                            </tr>

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
                                  <label class="compulsory" for="pingCode"><g:message code="tag.pingIDCode.label" default="Ping ID Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'pingCode', 'errors')}">
                                    <g:textField name="pingCode" value="${tagInstance?.pingCode}" placeholder="e.g. '46601'" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="expectedLifeTimeDays"><g:message code="tag.expectedLifeTimeDays.label" default="Expected Life Time (days)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'pingCode', 'errors')}">
                                    <g:textField name="expectedLifeTimeDays" value="${tagInstance?.expectedLifeTimeDays}" />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="status"><g:message code="tag.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tagInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${au.org.emii.aatams.DeviceStatus.values()}" optionKey="key" value="${tagInstance?.status.key}"  />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="tag.sensors.label" default="Sensors" /></td>

                                <td>
                                  <table class="nested">
                                    <thead>
                                      <tr>
                                        <th></th>
                                        <shiro:hasPermission permission="project:${tagInstance?.project?.id}:edit_children">
                                          <th/>
                                        </shiro:hasPermission>
                                        <th>Tag Type</th>
                                        <th>Code Map</th>
                                        <th>Ping Code</th>
                                        <th>Slope</th>
                                        <th>Intercept</th>
                                        <th>Unit</th>
                                      </tr>
                                    </thead>
                                    <tbody id="sensor_table_body">
                                      <g:each in="${tagInstance.sensors}" var="s">
                                        <tr>
                                          <td class="rowButton">
                                            <g:link class="show" controller="sensor" action="show" id="${s?.id}"></g:link>
                                          </td>
                                          <g:if test="${tagInstance.sensors.size() > 1}">
                                              <shiro:hasPermission permission="project:${tagInstance?.project?.id}:edit_children">
                                                  <td class="rowButton">
                                                      <g:link controller="sensor"
                                                              action="delete"
                                                              class="delete"
                                                              params="[projectId:tagInstance?.project?.id]"
                                                              id="${s?.id}"
                                                              onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">.</g:link>
                                                  </td>
                                              </shiro:hasPermission>
                                          </g:if>
                                          <td>${s?.transmitterType}</td>
                                          <td>${s?.codeMap}</td>
                                          <td>${s?.pingCode}</td>
                                          <td>${s?.slope}</td>
                                          <td>${s?.intercept}</td>
                                          <td>${s?.unit}</td>
                                        </tr>

                                      </g:each>
                                      <tr><td><br/></td></tr>
                                      <tr>
                                        <td colspan="5">
                                          <g:if test="${!tagInstance.unusedSensorTypes.isEmpty()}">
                                            <a href="#" id='add_sensor_to_tag'>${message(code: 'default.add.label', args: [message(code: 'sensor.label', default: 'Sensor...')])}</a>
                                          </g:if>
                                        </td>
                                      </tr>
                                    </tbody>
                                  </table>
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
            Dialog presented when adding sensor to tag.
            TODO: get this on demand (i.e. with AJAX)
      -->
      <div id="dialog-form-add-sensor" title="Add Sensor to Tag">
        <g:form action="save" >
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label class="compulsory" for="transmitterType"><g:message code="sensor.transmitterType.label" default="Transmitter Type" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: sensorInstance, field: 'transmitterType', 'errors')}">
                                <g:select name="transmitterTypeId"
                                          from="${tagInstance.unusedSensorTypes}"
                                          optionKey="id"
                                          value="${sensorInstance?.transmitterType?.id}"  />

                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label class="compulsory" for="sensorPingCode"><g:message code="sensor.pingCode.label" default="Ping ID Code" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: sensorInstance, field: 'pingCode', 'errors')}">
                                <g:textField name="sensorPingCode" value="${fieldValue(bean: sensorInstance, field: 'pingCode')}" />

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
                                <g:textField name="unit" value="${sensorInstance?.unit}" />

                            </td>
                        </tr>

                    </tbody>
                </table>
            </div>
        </g:form>
      </div>

    </body>
</html>
