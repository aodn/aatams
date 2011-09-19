

<%@ page import="au.org.emii.aatams.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${createPersonCmd}">
            <div class="errors">
                <g:renderErrors bean="${createPersonCmd}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="name"><g:message code="person.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${createPersonCmd?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="username"><g:message code="secUser.username.label" default="Username" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'username', 'errors')}">
                                    <g:textField name="username" value="${createPersonCmd?.username}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="password"><g:message code="secUser.password.label" default="Password" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'password', 'errors')}">
                                    <input type="password" name="password" value="" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="passwordConfirm"><g:message code="secUser.password.label" default="Password (confirm)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'passwordConfirm', 'errors')}">
                                    <input type="password" name="passwordConfirm" value="" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="organisation"><g:message code="person.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'organisation', 'errors')}">
                                    <g:select name="organisation.id" from="${au.org.emii.aatams.Organisation.listActive()}" optionKey="id" value="${createPersonCmd?.organisation?.id}"  />

                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="phoneNumber"><g:message code="person.phoneNumber.label" default="Phone Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'phoneNumber', 'errors')}">
                                    <g:textField name="phoneNumber" value="${createPersonCmd?.phoneNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="emailAddress"><g:message code="person.emailAddress.label" default="Email Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'emailAddress', 'errors')}">
                                    <g:textField name="emailAddress" value="${createPersonCmd?.emailAddress}" />

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
