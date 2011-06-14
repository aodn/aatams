

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
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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
                                  <label for="name"><g:message code="organisation.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${organisationInstance?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="phoneNumber"><g:message code="organisation.phoneNumber.label" default="Phone Number" /></label>
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
                                  <label for="streetAddress"><g:message code="organisation.streetAddress.label" default="Street Address" /></label>
                              </td>
                              <td>
                                <g:addressDetail addressName='streetAddress' address='${organisationInstance?.streetAddress}'/>
                              </td>  
                            </tr>
                            
                            <!-- Postal address. -->
                            <tr class="prop">
                              <td valign="top" class="name">
                                  <label for="postalAddress"><g:message code="organisation.streetAddress.label" default="Postal Address" /></label>
                              </td>
                              <td>
                                <g:addressDetail addressName='postalAddress' address='${organisationInstance?.postalAddress}'/>
                              </td>  
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="status"><g:message code="organisation.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${au.org.emii.aatams.EntityStatus?.values()}" keys="${au.org.emii.aatams.EntityStatus?.values()*.name()}" value="${organisationInstance?.status?.name()}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                              <td valign="top" class="name">
                                <label for="organisationProjects"><g:message code="organisation.organisationProjects.label" default="Organisation Projects" /></label>
                              </td>
                              <td valign="top" class="value ${hasErrors(bean: organisationInstance, field: 'organisationProjects', 'errors')}">

                                <ul>
                                  <g:each in="${organisationInstance?.organisationProjects?}" var="o">
                                    <li><g:link controller="organisationProject" action="show" id="${o.id}">${o?.encodeAsHTML()}</g:link></li>
                                  </g:each>
                                </ul>
                                <g:link controller="organisationProject" action="create" params="['organisation.id': organisationInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'organisationProject.label', default: 'OrganisationProject')])}</g:link>
                              </td>
                            </tr>
                            
                            <tr>
                              <td valign="top" class="name">
                                <label for="organisationProjects"><g:message code="organisation.organisationProjects.label" default="Projects" /></label>
                              </td>
                              <td>

                                <select id="languages" class="multiselect" multiple="multiple" name="languages[]" title="- Please select -"> 
                                  <g:each in="${Project.list()}" var="o">
                                    <option value="${o.id}">${o?.encodeAsHTML()}</option> 
                                    
<!--                                    <li><g:link controller="organisationProject" action="show" id="${o.id}">${o?.encodeAsHTML()}</g:link></li>-->
                                  </g:each>
                                  
<!--                                  <option value="Czech">Czech</option> 
                                  <option value="Dutch">Dutch</option> 
                                  <option value="German">German</option> 
                                  <option value="English">English</option> 
                                  <option value="Klingon">Klingon</option> 
                                  <option value="Polish">Polish</option> 
                                  <option value="Portuguese">Portuguese</option> 
                                  <option value="Russian">Russian</option> 
                                  <option value="Swedish">Swedish</option> -->
                                </select>

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
