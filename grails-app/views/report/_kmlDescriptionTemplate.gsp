<%@ page import="au.org.emii.aatams.InstallationStation" %>

<%--<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" /> --%>
<%--<link rel="stylesheet" type="text/css" href="http://localhost:8080/aatams/css/main.css" /> --%>
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
    
    <!--  Detections (by species). -->
    <g:set var="stationSummary" value="${installationStationInstance?.summary()}" />
     
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
    
    <br/>
</div>
