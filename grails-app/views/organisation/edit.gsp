

<%@ page import="au.org.emii.aatams.Organisation" %>
<%@ page import="au.org.emii.aatams.Project" %>
<html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
      <meta name="layout" content="main" />
      <g:set var="entityName" value="${message(code: 'organisation.label', default: 'Organisation')}" />
      <title><g:message code="default.edit.label" args="[entityName]" /></title>

    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:user>
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:user>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${organisationInstance}">
            <div class="errors">
                <g:renderErrors bean="${organisationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${organisationInstance?.id}" />
                <g:hiddenField name="version" value="${organisationInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="name"><g:message code="organisation.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${organisationInstance?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="name"><g:message code="organisation.department.label" default="Department" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'department', 'errors')}">
                                    <g:textField name="department" value="${organisationInstance?.department}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="phoneNumber"><g:message code="organisation.phoneNumber.label" default="Phone Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'phoneNumber', 'errors')}">
                                    <g:textField name="phoneNumber" value="${organisationInstance?.phoneNumber}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="faxNumber"><g:message code="organisation.faxNumber.label" default="Fax Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'faxNumber', 'errors')}">
                                    <g:textField name="faxNumber" value="${organisationInstance?.faxNumber}" />

                                </td>
                            </tr>
                        
                            <!-- Street address. -->
                            <tr class="prop">
                              <td valign="top" class="name">
                                  <label class="compulsory" for="streetAddress"><g:message code="organisation.streetAddress.label" default="Street Address" /></label>
                              </td>
                              <td>
                                <g:addressDetail addressName='streetAddress' address='${organisationInstance?.streetAddress}'/>
                              </td>  
                            </tr>
                            
                            <!-- Postal address. -->
                            <tr class="prop">
                              <td valign="top" class="name">
                                  <label class="compulsory" for="postalAddress"><g:message code="organisation.streetAddress.label" default="Postal Address" /></label>
                              </td>
                              <td>
                                <g:addressDetail addressName='postalAddress' address='${organisationInstance?.postalAddress}' asAboveElement="streetAddress"/>
                              </td>  
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label class="compulsory" for="status"><g:message code="organisation.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${au.org.emii.aatams.EntityStatus?.values()}" keys="${au.org.emii.aatams.EntityStatus?.values()*.name()}" value="${organisationInstance?.status?.name()}"  />

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
