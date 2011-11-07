<g:javascript src="pointEdit.js" />

<div id="${pointName}_div" pointName="${pointName}" class="pointEdit">
  
  <g:textField name="${pointName}_pointInputTextField" class="${clazz}" size="60" readonly="readonly" />

  <!-- This is the value which is sent back to the server to be parsed. -->
  <g:hiddenField name="${pointName}"/>

  <!-- Point attributes are stored in the following hidden fields.  These
       are updated when the edit dialog is closed.
  -->
  <g:hiddenField name="${pointName}_lon" value="${lon}"/>
  <g:hiddenField name="${pointName}_lat" value="${lat}"/>
  <g:hiddenField name="${pointName}_srid" value="${srid}"/>
  
  <div class="pointEditDialog" id="${pointName}_dialog-form-edit-point" pointName="${pointName}" title="Edit Point">

          <div class="dialog">
              <table>
                  <tbody>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label class="compulsory" for="value"><g:message code="point.latitude.label" default="Latitude" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="${pointName}_editLat" 
                                           value="${lat}" 
                                           placeholder="decimal degrees, e.g. 34.1234"/>

                          </td>
                          <td valign="top" class="value">
                              <g:select name="${pointName}_editNorthSouth" 
                                        optionKey="key"
                                        optionValue="value"
                                        from="['S':'S', 'N':'N']"
                                        value="${latNorthOrSouth}"/>

                          </td>
                      </tr>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label class="compulsory" for="value"><g:message code="point.longitude.label" default="Longitude" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="${pointName}_editLon" 
                                           value="${lon}" 
                                           placeholder="decimal degrees, e.g. 34.1234"/>

                          </td>
                          <td valign="top" class="value">
                              <g:select name="${pointName}_editEastWest" 
                                        optionKey="key"
                                        optionValue="value"
                                        from="['W':'W', 'E':'E']"
                                        value="${lonEastOrWest}"/>

                          </td>
                      </tr>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label class="compulsory" for="value"><g:message code="point.srid.label" default="Datum" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:select name="${pointName}_editSrid" 
                                        optionKey="key"
                                        optionValue="value"
                                        from="${datums}"
                                        value="${srid}" />

                          </td>
                          <td/>
                      </tr>

                  </tbody>
              </table>
          </div>
  </div>
</div>

