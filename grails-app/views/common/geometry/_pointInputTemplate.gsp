<g:javascript src="pointEdit.js" />

<div class="pointEdit" id="${parentName}">
  <%-- <g:textField name="${parentName}" value="${value}" size="40"/> --%>
  <%-- <g:textField name="pointInputTextField" value="${value}" size="40"/> --%>
  <g:textField name="${parentName}" size="40"/>

  <!-- Point attributes are stored in the following hidden fields.  These
       are updated when the edit dialog is closed.
  -->
  <g:hiddenField name="lon" value="${lon}"/>
  <g:hiddenField name="lat" value="${lat}"/>
  <g:hiddenField name="srid" value="${srid}"/>

  <div id="dialog-form-edit-point" title="Edit Point">
      <g:form action="save" >


          <div class="dialog">
              <table>
                  <tbody>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label for="value"><g:message code="point.longitude.label" default="Longitude" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="editLon" value="${lon}" />

                          </td>
                      </tr>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label for="value"><g:message code="point.latitude.label" default="Latitude" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="editLat" value="${lat}" />

                          </td>
                      </tr>

                      <tr class="prop">
                          <td valign="top" class="name">
                              <label for="value"><g:message code="point.srid.label" default="SRID" /></label>
                          </td>
                          <td valign="top" class="value">
                              <g:textField name="editSrid" value="${srid}" />

                          </td>
                      </tr>

                  </tbody>
              </table>
          </div>
      </g:form>
  </div>
</div>

