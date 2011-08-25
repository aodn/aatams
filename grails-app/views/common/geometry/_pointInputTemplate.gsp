<g:javascript src="pointEdit.js" />

<div id="${parentName}" class="pointEdit">
  
  <g:textField name="pointInputTextField" size="40"/>

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
                      </tr>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label class="compulsory" for="value"><g:message code="point.longitude.label" default="Longitude" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="editLon" value="${lon}" />

                          </td>
                      </tr>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label class="compulsory" for="value"><g:message code="point.srid.label" default="SRID" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="editSrid" value="${srid}" />

                          </td>
                      </tr>

                  </tbody>
              </table>
          </div>
  </div>
</div>

