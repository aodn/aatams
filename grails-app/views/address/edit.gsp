

<%@ page import="au.org.emii.aatams.Address" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${addressInstance}">
            <div class="errors">
                <g:renderErrors bean="${addressInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${addressInstance?.id}" />
                <g:hiddenField name="version" value="${addressInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="streetAddress"><g:message code="address.streetAddress.label" default="Street Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'streetAddress', 'errors')}">
                                    <g:textField name="streetAddress" value="${addressInstance?.streetAddress}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="suburbTown"><g:message code="address.suburbTown.label" default="Suburb Town" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'suburbTown', 'errors')}">
                                    <g:textField name="suburbTown" value="${addressInstance?.suburbTown}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="state"><g:message code="address.state.label" default="State" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'state', 'errors')}">
                                    <g:textField name="state" value="${addressInstance?.state}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="postcode"><g:message code="address.postcode.label" default="Postcode" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'postcode', 'errors')}">
                                    <g:textField name="postcode" value="${fieldValue(bean: addressInstance, field: 'postcode')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="country"><g:message code="address.country.label" default="Country" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'country', 'errors')}">
                                    <g:textField name="country" value="${addressInstance?.country}" />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
