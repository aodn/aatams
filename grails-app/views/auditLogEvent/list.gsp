<%@ page import="org.apache.commons.lang.ClassUtils" %>
<%@ page import="grails.util.GrailsNameUtils" %>
<%@ page import="org.apache.commons.lang.WordUtils" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'auditLogEvent.label', default: 'Activity')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <td>

                               <g:sortableColumn property="dateCreated" title="Date" />
                        
                            <g:sortableColumn property="className" title="Type" />

                            <g:sortableColumn property="eventName" title="Action" />

                            <th>Description</th>
                            
                            <shiro:hasRole name="SysAdmin">
                                <g:sortableColumn property="actor" title="User" />
                            </shiro:hasRole>
                        
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${auditLogEventInstanceList}" status="i" var="auditLogEventInstance">
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            
                                <td class="rowButton">
                                    <g:link class="show" 
                                            controller="${WordUtils.uncapitalize(GrailsNameUtils.getShortName(auditLogEventInstance.className))}" 
                                            action="show" 
                                            id="${auditLogEventInstance.persistedObjectId}">.
                                    </g:link>
                                </td>
                                
                                <td><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${auditLogEventInstance.dateCreated}" /></td>
                            
                                <td>${GrailsNameUtils.getShortName(auditLogEventInstance.className)}</td>
    
                                <td>${fieldValue(bean:auditLogEventInstance, field:'eventName')}</td>
                            
                                <td>${String.valueOf(ClassUtils.getClass(auditLogEventInstance.className).get(auditLogEventInstance.persistedObjectId))}</td>
                            
                                <shiro:hasRole name="SysAdmin">
                                    <td>${fieldValue(bean:auditLogEventInstance, field:'actor')}</td>
                                </shiro:hasRole>
                            
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${auditLogEventInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
