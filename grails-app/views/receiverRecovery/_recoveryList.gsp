<div class="recoveryTable">                
	<div class="list">
	    <table class="${clazz}">
	        <thead>
	          <tr>
	              <th colspan="7">Deployment Details</th>
	              <th colspan="5">Recovery Details</th>
	          </tr>
	        </thead>
	        <thead>
	            <tr>
	            
	                <td/>
                    <g:column property="deploymentDateTime" title="${message(code: 'receiverDeployment.deploymentDateTime.label', default: 'Deployment Date')}"
                              params="${params}" sortable="${sortable}" />
	
	                <g:column property="station.installation" title="${message(code: 'receiverDeployment.installation.label', default: 'Installation')}"
	                                  params="${params}" sortable="${sortable}" />
	                                  
	            
	                <g:column property="station" title="${message(code: 'receiverDeployment.station.label', default: 'Station')}"
	                                  params="${params}" sortable="${sortable}" />
	            
                    <g:if test="${!hideColumns?.contains('deploymentLocation')}">
    	                <th><g:message code="receiverDeployment.station.location.label" default="Location" /></th>
                    </g:if>
	            
	                <g:if test="${!hideColumns?.contains('receiver')}">
		                <g:column property="receiver" title="${message(code: 'receiverDeployment.receiver.label', default: 'Receiver')}"
		                                  params="${params}" sortable="${sortable}" />
	                </g:if>
	            
	                <th><g:message code="receiverDeployment.station.depth.label" default="Depth" /></th>
	            
	                <!-- New/edit column -->
	                <g:column property="recovery" title="${message(code: 'receiverRecovery.label', default: 'Recovery')}"
	                                  params="${params}" sortable="${sortable}" />
	                
	                <g:column property="recovery.recoverer.person.name" title="${message(code: 'receiverRecovery.recoverer.label', default: 'Recovered By')}"
	                                  params="${params}" sortable="${sortable}" />
	
                    <g:if test="${!hideColumns?.contains('recoveryLocation')}">
                        <th><g:message code="receiverRecovery.location" default="Location" /></th>
                    </g:if>
	            
	                <g:column property="recovery.recoveryDateTime" title="${message(code: 'receiverRecovery.recoveryDateTime.label', default: 'Recovery Date')}"
	                                  params="${params}" sortable="${sortable}" />
	            
	                <g:column property="recovery.status" title="${message(code: 'receiverRecovery.status.label', default: 'Status')}"
	                                  params="${params}" sortable="${sortable}" />
	            
	            </tr>
	        </thead>
	        <tbody>
	        <g:each in="${entityList}" status="i" var="receiverDeployment">
	            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	
	                <td class="rowButton"><g:link class="show" controller="receiverDeployment" action="show" id="${receiverDeployment.id}">.</g:link></td>
	        
	                <td><joda:format value="${receiverDeployment.deploymentDateTime}" /></td>
	            
	                <td><g:link controller="installation" action="show" id="${receiverDeployment?.station?.installation?.id}">${receiverDeployment?.station?.installation}</g:link></td>
	
	                <td><g:link controller="installationStation" action="show" id="${receiverDeployment?.station?.id}">${receiverDeployment?.station}</g:link></td>
	            
                    <g:if test="${!hideColumns?.contains('deploymentLocation')}">
	                    <td>
	                      <g:point name="scrambledLocation"
	                               value="${receiverDeployment?.station?.scrambledLocation}" />
	                    </td>
                    </g:if>
	                
                    <g:if test="${!hideColumns?.contains('receiver')}">
                        <td><g:link action="receiver" controller="receiver" action="show" id="${receiverDeployment?.receiver?.id}">${receiverDeployment?.receiver}</g:link></td>
                    </g:if>
	
	                <td>${fieldValue(bean: receiverDeployment, field: "depthBelowSurfaceM")}</td>
	
	                <td class="rowButton">
	                  <g:if test="${receiverDeployment?.recovery == null}">
	                    <shiro:hasPermission permission="project:${projectId:receiverDeployment?.station?.installation?.project?.id}:write">
	                      <g:link class="create" controller="receiverRecovery" action="create" 
	                              params="[deploymentId:receiverDeployment.id, projectId:receiverDeployment?.station?.installation?.project?.id]"></g:link>
	                    </shiro:hasPermission>  
	                  </g:if>
	                  <g:else>
	                    <g:link class="show" controller="receiverRecovery" action="show" id="${receiverDeployment?.recovery?.id}"></g:link>
	                  </g:else>
	                </td>
	                
	                <td>${fieldValue(bean: receiverDeployment?.recovery?.recoverer, field: "person")}</td>
	
                    <g:if test="${!hideColumns?.contains('recoveryLocation')}">
	                    <td>
	                      <g:point name="scrambledLocation"
	                               value="${receiverDeployment?.recovery?.scrambledLocation}" />
	                    </td>
                    </g:if>
	
	                <td><joda:format value="${receiverDeployment?.recovery?.recoveryDateTime}" /></td>
	
	                <td>${fieldValue(bean: receiverDeployment?.recovery, field: "status")}</td>
	
	            
	            </tr>
	        </g:each>
	        </tbody>
	    </table>
	</div>
</div>