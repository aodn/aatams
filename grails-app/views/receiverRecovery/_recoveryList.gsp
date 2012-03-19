<div class="recoveryTable">                
	<div class="list">
	    <table>
	        <thead>
	          <tr>
	              <th colspan="7">Deployment Details</th>
	              <th colspan="5">Recovery Details</th>
	          </tr>
	        </thead>
	        <thead>
	            <tr>
	            
	                <td/>
	                
	                <g:sortableColumn property="deploymentDateTime" title="${message(code: 'receiverDeployment.deploymentDateTime.label', default: 'Deployment Date')}"
	                                  params="${params}"/>
	
	                <g:sortableColumn property="station.installation" title="${message(code: 'receiverDeployment.installation.label', default: 'Installation')}"
	                                  params="${params}"/>
	                                  
	            
	                <g:sortableColumn property="station" title="${message(code: 'receiverDeployment.station.label', default: 'Station')}"
	                                  params="${params}"/>
	            
	                <th><g:message code="receiverDeployment.station.location.label" default="Location" /></th>
	            
	                <g:if test="${!hideReceiverColumn}">
		                <g:sortableColumn property="receiver" title="${message(code: 'receiverDeployment.receiver.label', default: 'Receiver')}"
		                                  params="${params}"/>
	                </g:if>
	            
	                <th><g:message code="receiverDeployment.station.depth.label" default="Depth" /></th>
	            
	                <!-- New/edit column -->
	                <g:sortableColumn property="recovery" title="${message(code: 'receiverRecovery.label', default: 'Recovery')}"
	                                  params="${params}"/>
	                
	                <g:sortableColumn property="recovery.recoverer.person.name" title="${message(code: 'receiverRecovery.recoverer.label', default: 'Recovered By')}"
	                                  params="${params}"/>
	
	                <th><g:message code="receiverRecovery.location" default="Location" /></th>
	            
	                <g:sortableColumn property="recovery.recoveryDateTime" title="${message(code: 'receiverRecovery.recoveryDateTime.label', default: 'Recovery Date')}"
	                                  params="${params}"/>
	            
	                <g:sortableColumn property="recovery.status" title="${message(code: 'receiverRecovery.status.label', default: 'Status')}"
	                                  params="${params}"/>
	            
	            </tr>
	        </thead>
	        <tbody>
	        <g:each in="${entityList}" status="i" var="receiverDeployment">
	            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	
	                <td class="rowButton"><g:link class="show" controller="receiverDeployment" action="show" id="${receiverDeployment.id}">.</g:link></td>
	        
	                <td><joda:format value="${receiverDeployment.deploymentDateTime}" /></td>
	            
	                <td><g:link controller="installation" action="show" id="${receiverDeployment?.station?.installation?.id}">${receiverDeployment?.station?.installation}</g:link></td>
	
	                <td><g:link controller="installationStation" action="show" id="${receiverDeployment?.station?.id}">${receiverDeployment?.station}</g:link></td>
	            
	                <td>
	                  <g:point name="scrambledLocation"
	                           value="${receiverDeployment?.station?.scrambledLocation}" />
	                </td>
	                
                    <g:if test="${!hideReceiverColumn}">
                        <td><g:link action="receiver" controller="receiver" action="show" id="${receiverDeployment?.receiver?.id}">${receiverDeployment?.receiver}</g:link></td>
                    </g:if>
	
	                <td>${fieldValue(bean: receiverDeployment, field: "depthBelowSurfaceM")}</td>
	
	                <td class="rowButton">
	                  <g:if test="${receiverDeployment?.recovery == null}">
	                    <shiro:hasPermission permission="project:${projectId:receiverDeployment?.station?.installation?.project?.id}:write">
	                      <g:link class="create" action="create" 
	                              params="[deploymentId:receiverDeployment.id, projectId:receiverDeployment?.station?.installation?.project?.id]"></g:link>
	                    </shiro:hasPermission>  
	                  </g:if>
	                  <g:else>
	                    <g:link class="show" action="show" id="${receiverDeployment?.recovery?.id}"></g:link>
	                  </g:else>
	                </td>
	                
	                <td>${fieldValue(bean: receiverDeployment?.recovery?.recoverer, field: "person")}</td>
	
	                <td>
	                  <g:point name="scrambledLocation"
	                           value="${receiverDeployment?.recovery?.scrambledLocation}" />
	                </td>
	
	                <td><joda:format value="${receiverDeployment?.recovery?.recoveryDateTime}" /></td>
	
	                <td>${fieldValue(bean: receiverDeployment?.recovery, field: "status")}</td>
	
	            
	            </tr>
	        </g:each>
	        </tbody>
	    </table>
	</div>
</div>