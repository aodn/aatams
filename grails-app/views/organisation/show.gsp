
<%@ page import="au.org.emii.aatams.Organisation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisation.label', default: 'Organisation')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
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
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisation.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: organisationInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisation.department.label" default="Department" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: organisationInstance, field: "department")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisation.phoneNumber.label" default="Phone Number" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: organisationInstance, field: "phoneNumber")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisation.faxNumber.label" default="Fax Number" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: organisationInstance, field: "faxNumber")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisation.streetAddress.label" default="Street Address" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: organisationInstance, field: "streetAddress")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisation.postalAddress.label" default="Postal Address" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: organisationInstance, field: "postalAddress")}</td>
                            
                        </tr>
                    
                        <shiro:hasRole name="SysAdmin">
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="organisation.status.label" default="Status" /></td>

                              <td valign="top" class="value">${organisationInstance?.status?.encodeAsHTML()}</td>

                          </tr>

                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="organisation.requestingUser.label" default="Requester" /></td>

                              <td valign="top" class="value"><g:link controller="person" action="show" id="${organisationInstance?.request?.requester?.id}">${organisationInstance?.request?.encodeAsHTML()}</g:link></td>

                          </tr>
                        </shiro:hasRole>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisation.organisationPeople.label" default="People" /></td>
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${organisationInstance.people}" var="person">
                                    <li><g:link controller="person" action="show" id="${person.id}">${person?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${organisationInstance?.id}" />
                    <shiro:hasRole name="SysAdmin">

                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>

                </g:form>
            </div>
        </div>
    </body>
</html>
