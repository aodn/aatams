

<%@ page import="au.org.emii.aatams.Address" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'address.label', default: 'Address')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
<!--        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
-->
        <div class="body">
<!--            <h1><g:message code="default.create.label" args="[entityName]" /></h1>-->
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${addressInstance}">
            <div class="errors">
                <g:renderErrors bean="${addressInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="streetAddress"><g:message code="address.streetAddress.label" default="Street Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'streetAddress', 'errors')}">
                                    <g:textField name="streetAddress" value="${addressInstance?.streetAddress}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="suburbTown"><g:message code="address.suburbTown.label" default="Suburb Town" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'suburbTown', 'errors')}">
                                    <g:textField name="suburbTown" value="${addressInstance?.suburbTown}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state"><g:message code="address.state.label" default="State" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'state', 'errors')}">
                                    <g:textField name="state" value="${addressInstance?.state}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="postcode"><g:message code="address.postcode.label" default="Postcode" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'postcode', 'errors')}">
                                    <g:textField name="postcode" value="${fieldValue(bean: addressInstance, field: 'postcode')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country"><g:message code="address.country.label" default="Country" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'country', 'errors')}">
                                    <g:textField name="country" value="${addressInstance?.country}" />

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
            </g:form>
        </div>
    </body>
</html>
