
<%@ page import="au.org.emii.aatams.AnimalRelease" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'animalRelease.label', default: 'AnimalRelease')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <shiro:hasPermission permission="projectWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            
            <g:listFilter name="animalRelease" />
            
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <td/>
                            
                            <g:sortableColumn property="id" title="${message(code: 'animalRelease.id.label', default: 'Tag(s)')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="animalRelease.animal.species" title="${message(code: 'animalRelease.animal.species.label', default: 'Species')}" params="${executedFilter}"/>

                            <g:sortableColumn property="releaseDateTime" title="${message(code: 'animalRelease.releaseDateTime.label', default: 'Release Date Time')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="releaseLocality" title="${message(code: 'animalRelease.releaseLocality.label', default: 'Release Locality')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="releaseLocation" title="${message(code: 'animalRelease.releaseLocation.label', default: 'Release Location')}" params="${executedFilter}"/>
                        
                            <g:sortableColumn property="project.name" title="${message(code: 'animalRelease.project.label', default: 'Project')}" params="${executedFilter}"/>

                            <g:sortableColumn property="embargoDate" title="${message(code: 'animalRelease.embargoDate.label', default: 'Embargo Date')}" params="${executedFilter}"/>

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${entityList}" status="i" var="animalReleaseInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td class="rowButton"><g:link class="show" action="show" id="${animalReleaseInstance.id}">.</g:link></td>
                    
                            <td>
                              <g:each var="surgery" in="${animalReleaseInstance.surgeries}">
                                <g:link controller="tag" action="show" id="${surgery?.tag?.id}">${surgery.tag}<p/></g:link>
                              </g:each>
                            </td>
                        
                            <td>${fieldValue(bean: animalReleaseInstance, field: "animal.species")}</td>

                            <td><joda:format value="${animalReleaseInstance.releaseDateTime}" /></td>
                        
                            <td>${fieldValue(bean: animalReleaseInstance, field: "releaseLocality")}</td>
                        
                            <td>
                              <g:point name="scrambledReleaseLocation" 
                                       value="${animalReleaseInstance?.scrambledReleaseLocation}"/>
                            </td>
                            <td><g:link controller="project" action="show" id="${animalReleaseInstance.project.id}">${animalReleaseInstance.project}</g:link></td>
                        
                            <td><g:formatDate date="${animalReleaseInstance.embargoDate}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${total}" params="${executedFilter}"/>
            </div>
        </div>
    </body>
</html>
