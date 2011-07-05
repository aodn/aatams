

<%@ page import="au.org.emii.aatams.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <script type="text/javascript" src="${resource(dir:'js',file:'changepassword.js')}"></script>
        
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
            <g:hasErrors bean="${personInstance}">
            <div class="errors">
                <g:renderErrors bean="${personInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${personInstance?.id}" />
                <g:hiddenField name="version" value="${personInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="person.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${personInstance?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="username"><g:message code="secUser.username.label" default="Username" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'username', 'errors')}">
                                    <g:textField name="username" value="${personInstance?.username}" />

                                </td>
                            </tr>
<%--                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="password"><g:message code="secUser.password.label" default="Password" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'password', 'errors')}">
                                    <g:textField name="password" value="${personInstance?.passwordHash}" />
                                    
                                </td>
                            </tr>
--%>
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="phoneNumber"><g:message code="person.phoneNumber.label" default="Phone Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'phoneNumber', 'errors')}">
                                    <g:textField name="phoneNumber" value="${personInstance?.phoneNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="emailAddress"><g:message code="person.emailAddress.label" default="Email Address" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'emailAddress', 'errors')}">
                                    <g:textField name="emailAddress" value="${personInstance?.emailAddress}" />

                                </td>
                            </tr>
                        <%--
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="systemRoles"><g:message code="person.systemRoles.label" default="System Roles" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'systemRoles', 'errors')}">
                                    <g:select name="systemRoles" from="${au.org.emii.aatams.SystemRole.list()}" multiple="yes" optionKey="id" size="5" value="${personInstance?.systemRoles*.id}" />

                                </td>
                            </tr>
                        --%>
                        
                            <tr>
                              <td>
                                <a href="#" 
                                   id='change_password'>${message(code: 'person.password.change.label', default: 'Change password...')}</a>
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
      
      <!--
            Dialog presented when changing password.
            TODO: get this on demand (i.e. with AJAX)
      -->
      <div id="dialog-form-change-password" title="Change Password">
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="personId"><g:message code="person.label" default="Person" /></label>
                                    <g:hiddenField name="personId" value="${personInstance?.id}" />

                                </td>
                                <td valign="top" class="value ${hasErrors(bean: personInstance, field: 'name', 'errors')}">
                                  <label id="project">${personInstance?.name}</label>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="password"><g:message code="secUser.password.label" default="Password" /></label>
                                </td>
                                <td valign="top" class="value ">
                                    <g:textField name="password" />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="passwordConfirm"><g:message code="secUser.password.label" default="Password (confirm)" /></label>
                                </td>
                                <td valign="top" class="value ">
                                    <g:textField name="passwordConfirm" />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
            </g:form>
      </div>
      
    </body>
</html>
