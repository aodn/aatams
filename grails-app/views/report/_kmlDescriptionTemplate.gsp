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
	    
                    <tr class="prop">
                        <td valign="top" class="name">Detection Count</td>
                        <td valign="top" class="value">${installationStationInstance?.detectionCount}</td>
                    </tr>
                    
	            </tbody>
	        </table>
	    </div>
       
	</div>
</div>