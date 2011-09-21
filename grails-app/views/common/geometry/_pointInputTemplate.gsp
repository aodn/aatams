<g:javascript src="pointEdit.js" />

<div id="${parentName}" class="pointEdit">
  
  <g:textField name="pointInputTextField" size="60"/>

  <!-- This is the value which is sent back to the server to be parsed. -->
  <g:hiddenField name="${parentName}"/>

  <!-- Point attributes are stored in the following hidden fields.  These
       are updated when the edit dialog is closed.
  -->
  <g:hiddenField name="${parentName}_lon" value="${lon}"/>
  <g:hiddenField name="${parentName}_lat" value="${lat}"/>
  <g:hiddenField name="${parentName}_srid" value="${srid}"/>
  
  <div class="pointEditDialog" id="dialog-form-edit-point" parent="${parentName}" title="Edit Point">

          <div class="dialog">
              <table>
                  <tbody>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label class="compulsory" for="value"><g:message code="point.latitude.label" default="Latitude" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="editLat" value="${lat}" />

                          </td>
                          <td valign="top" class="value">
                              <g:select name="editNorthSouth" 
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
                              <g:textField name="editLon" value="${lon}" />

                          </td>
                          <td valign="top" class="value">
                              <g:select name="editEastWest" 
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
                              <g:select name="editSrid" 
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

