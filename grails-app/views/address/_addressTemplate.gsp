<!--<g:form action="save" >-->
    <div class="dialog">
        <table>
            <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="streetAddress"><g:message code="address.streetAddress.label" default="Street Address" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: it, field: 'streetAddress', 'errors')}">
                        <g:textField name="${name}.streetAddress" value="${it?.streetAddress}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="suburbTown"><g:message code="address.suburbTown.label" default="Suburb Town" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: it, field: 'suburbTown', 'errors')}">
                        <g:textField name="${name}.suburbTown" value="${it?.suburbTown}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="state"><g:message code="address.state.label" default="State" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: it, field: 'state', 'errors')}">
                        <g:textField name="${name}.state" value="${it?.state}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="postcode"><g:message code="address.postcode.label" default="Postcode" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: it, field: 'postcode', 'errors')}">
                        <g:textField name="${name}.postcode" value="${it?.postcode}" />

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="country"><g:message code="address.country.label" default="Country" /></label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: it, field: 'country', 'errors')}">
                        <g:textField name="${name}.country" value="${it?.country}" />

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
