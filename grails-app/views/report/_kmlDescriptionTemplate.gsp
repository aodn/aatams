<%@ page import="au.org.emii.aatams.InstallationStation" %>
<div>
	<link rel="stylesheet" type="text/css" href="${createLinkTo(dir:'css', file:'main.css', absolute:true) }" />
	<div class="description">
	
	    <!--  "Header" data. -->
	    <div class="dialog">
	        <table>
	            <tbody>
	            
	                <tr class="prop">
	                    <td valign="top" class="name">Project</td>
	                    <td valign="top" class="value">${installationStationInstance?.installation?.project?.name}</td>
	                </tr>
	    
	                <tr class="prop">
	                    <td valign="top" class="name">Installation</td>
	                    <td valign="top" class="value">${installationStationInstance?.installation?.name}</td>
	                </tr>
	    
	                <tr class="prop">
	                    <td valign="top" class="name">Active</td>
	                    <td valign="top" class="value">${installationStationInstance?.isActive()}</td>
	                </tr>
	    
	            </tbody>
	        </table>
	    </div>
	    
        <g:set var="stationSummary" value="${installationStationInstance?.summary()}" />
	    <g:if test="${stationSummary?.hasDetections()}">
            <!--  Detections (by species). -->
             
            <br/>            
            <h4>Detections (by species)</h4>
            <div class="dialog">
                <table>
                    <tbody>
                        <g:each in="${stationSummary?.detectionCountsBySpecies()}" var="e">
                            <tr class="prop">
                                <td valign="top" class="name">${e.value}</td>
                                <td valign="top" class="value">${e.key}</td>
                            </tr>
                        </g:each> 
                    </tbody>
                </table>
            </div>
            
            <!--  Detections (by tag). -->
            <br/>            
            <h4>Detections (by tag)</h4>
            <div class="dialog">
                <table>
                    <tbody>
                        <g:each in="${stationSummary?.detectionCountsByTag()}" var="e">
                            <tr class="prop">
                                <td valign="top" class="name">${e.value}</td>
                                <td valign="top" class="value">${e.key}</td>
                            </tr>
                        </g:each> 
                    </tbody>
                </table>
            </div>
	    </g:if>
	    <g:else>
           <br/>            
           <h4>No detections at this station</h4>
           <br/>            
        </g:else>
	    
	    <br/>
	</div>
</div>