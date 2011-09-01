<!--<g:form action="save" >-->
    <div class="dialog">
        <table>
            <tbody>

                <g:if test="${asAboveElement}">
                  <g:hiddenField name="${addressName}.asAboveElement" value="${asAboveElement}"/>
                  
                  <tr class="prop">
                    <td valign="top" class="name">
                        <label for="asAbove"><g:message code="asAbove.label" default="As above" /></label>
                    </td>
                    <td>
                      <g:checkBox name="${addressName}.asAbove" value="${false}"/>
                    </td>  
                  </tr>
                </g:if>
                            
                <tr class="prop">
                    <td valign="top" class="name">
                        <label class="compulsory" for="streetAddress"><g:message code="address.streetAddress.label" default="Address" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: address, field: 'streetAddress', 'errors')}">
                        <g:textField name="${addressName}.streetAddress" value="${address?.streetAddress}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label class="compulsory" for="suburbTown"><g:message code="address.suburbTown.label" default="Suburb Town" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: address, field: 'suburbTown', 'errors')}">
                        <g:textField name="${addressName}.suburbTown" value="${address?.suburbTown}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label class="compulsory" for="state"><g:message code="address.state.label" default="State" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: address, field: 'state', 'errors')}">
                        <g:textField name="${addressName}.state" value="${address?.state}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label class="compulsory" for="postcode"><g:message code="address.postcode.label" default="Postcode" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: address, field: 'postcode', 'errors')}">
                        <g:textField name="${addressName}.postcode" value="${address?.postcode}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label class="compulsory" for="country"><g:message code="address.country.label" default="Country" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: address, field: 'country', 'errors')}">
                        <g:textField name="${addressName}.country" value="${address?.country}" />

                    </td>
                </tr>

            </tbody>
        </table>
    </div>
<!--  Handled by the owning Organisation view.

    <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
-->
<!--</g:form>-->
