<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gml="http://www.opengis.net/gml" xmlns:iso19112="http://www.opengis.net/iso19112" xmlns:wfs="http://www.opengis.net/wfs" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan" xmlns="http://www.w3.org/1999/xhtml">
    <xsl:output method="xml" version="1.0" encoding="iso-8859-1" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"/>
    <xsl:template match="wfs:FeatureCollection">
        <html>
            <head>
                <style type="text/css">th{ border: 1px solid #6699CC;border-collapse: collapse;background-color: #BEC8D1;font-family: Verdana;font-weight: bold;font-size: 11px;color: /*#404040*/ black; padding: 5px; }td{ border: 1px solid #9CF;border-collapse: collapse;font-family: Verdana, sans-serif, Arial;font-weight: normal;font-size: 11px;color: /*#404040*/ black;white-space: nowrap;background-color: #fafafa; }table{ text-align: center;font-family: Verdana;font-weight: normal;font-size: 11px;color: /*#404040*/ black;background-color: #fafafa;border: 1px #6699CC solid;border-collapse: collapse;border-spacing: 0px; }</style>
            </head>
            <body>
                <table>
                    <thead>
                        <xsl:choose>
                            <xsl:when test="count(gml:featureMember) = 0">
                                <tr>
                                    <th>Member Count</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:Installation">
                                <tr>
                                    <th>Installation id</th>
                                    <th>Name</th>
                                    <th>Installation type</th>
                                    <th>Disabled</th>
                                    <th>Created</th>
                                    <th>Modified</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:Deployment">
                                <tr>
                                    <th colspan="1" rowspan="4">Deployment Id</th>
                                    <th colspan="6" rowspan="1">Installation</th>
                                    <th colspan="12" rowspan="1">Device</th>
                                    <th colspan="28" rowspan="1">Project/Role/Person</th>
                                    <th colspan="2" rowspan="1">Location</th>
                                    <th colspan="1" rowspan="4">Deployment UTC Date-Time</th>
                                    <th colspan="1" rowspan="4">Disabled</th>
                                    <th colspan="1" rowspan="4">Created</th>
                                    <th colspan="1" rowspan="4">Modified</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="3">Installation Id</th>
                                    <th colspan="1" rowspan="3">Name</th>
                                    <th colspan="1" rowspan="3">Type</th>
                                    <th colspan="1" rowspan="3">Disabled</th>
                                    <th colspan="1" rowspan="3">Created</th>
                                    <th colspan="1" rowspan="3">Modified</th>
                                    <th colspan="1" rowspan="3">Device Id</th>
                                    <th colspan="1" rowspan="3">Name</th>
                                    <th colspan="7" rowspan="1">Model</th>
                                    <th colspan="1" rowspan="3">Disabled</th>
                                    <th colspan="1" rowspan="3">Created</th>
                                    <th colspan="1" rowspan="3">Modified</th>
                                    <th colspan="11" rowspan="1">Project</th>
                                    <th colspan="5" rowspan="1">Role</th>
                                    <th colspan="9" rowspan="1">Person</th>
                                    <th colspan="1" rowspan="3">Disabled</th>
                                    <th colspan="1" rowspan="3">Created</th>
                                    <th colspan="1" rowspan="3">Modified</th>
                                    <th colspan="1" rowspan="3">Longitude</th>
                                    <th colspan="1" rowspan="3">Latitude</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="2">Model Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="1" rowspan="2">Manufacturer</th>
                                    <th colspan="1" rowspan="2">Device Type</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                    <th colspan="1" rowspan="2">Project Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="6" rowspan="1">Organization</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                    <th colspan="1" rowspan="2">Role Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                    <th colspan="1" rowspan="2">Person Id</th>
                                    <th colspan="1" rowspan="2">First Name</th>
                                    <th colspan="1" rowspan="2">Last Name</th>
                                    <th colspan="1" rowspan="2">Email</th>
                                    <th colspan="1" rowspan="2">Address</th>
                                    <th colspan="1" rowspan="2">Phone</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="1">Organization Id</th>
                                    <th colspan="1" rowspan="1">Name</th>
                                    <th colspan="1" rowspan="1">Initials</th>
                                    <th colspan="1" rowspan="1">Disabled</th>
                                    <th colspan="1" rowspan="1">Created</th>
                                    <th colspan="1" rowspan="1">Modified</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:Detection">
                                <tr>
                                    <th colspan="1" rowspan="3">Detection Id</th>
                                    <th colspan="3" rowspan="1">Deployment</th>
                                    <th colspan="12" rowspan="1">Tag</th>
                                    <th colspan="1" rowspan="3">Detection UTC Time Stamp</th>
                                    <th colspan="1" rowspan="3">Disabled</th>
                                    <th colspan="1" rowspan="3">Created</th>
                                    <th colspan="1" rowspan="3">Modified</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="2">Deployment Id</th>
                                    <th colspan="2" rowspan="1">Location</th>
                                    <th colspan="1" rowspan="2">Device Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="7" rowspan="1">Model</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="1">Longitude</th>
                                    <th colspan="1" rowspan="1">Latitude</th>
                                    <th colspan="1" rowspan="1">Model Id</th>
                                    <th colspan="1" rowspan="1">Name</th>
                                    <th colspan="1" rowspan="1">Manufacturer</th>
                                    <th colspan="1" rowspan="1">Device Type</th>
                                    <th colspan="1" rowspan="1">Disabled</th>
                                    <th colspan="1" rowspan="1">Created</th>
                                    <th colspan="1" rowspan="1">Modified</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:Surgery">
                                <tr>
                                    <th colspan="1" rowspan="3">Surgery Id</th>
                                    <th colspan="5" rowspan="1">Geographic Area</th>
                                    <th colspan="12" rowspan="1">Animal</th>
                                    <th colspan="9" rowspan="1">Person</th>
                                    <th colspan="12" rowspan="1">Device</th>
                                    <th colspan="1" rowspan="3">Device Implant Type</th>
                                    <th colspan="2" rowspan="1">Location</th>
                                    <th colspan="1" rowspan="3">Surgery UTC Time Stamp</th>
                                    <th colspan="1" rowspan="3">Comments</th>
                                    <th colspan="1" rowspan="3">DNA Sample Taken</th>
                                    <th colspan="1" rowspan="3">Disabled</th>
                                    <th colspan="1" rowspan="3">Created</th>
                                    <th colspan="1" rowspan="3">Modified</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="2">Geographic Area Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                    <th colspan="1" rowspan="2">Animal Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="6" rowspan="1">Species</th>
                                    <th colspan="1" rowspan="2">Sex</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                    <th colspan="1" rowspan="2">Person Id</th>
                                    <th colspan="1" rowspan="2">First Name</th>
                                    <th colspan="1" rowspan="2">Last Name</th>
                                    <th colspan="1" rowspan="2">Email</th>
                                    <th colspan="1" rowspan="2">Address</th>
                                    <th colspan="1" rowspan="2">Phone</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                    <th colspan="1" rowspan="2">Device Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="7" rowspan="1">Model</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
				    <th colspan="1" rowspan="2">Modified</th>
                                    <th colspan="1" rowspan="2">Longitude</th>
                                    <th colspan="1" rowspan="2">Latitude</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="1">Species Id</th>
                                    <th colspan="1" rowspan="1">Name</th>
                                    <th colspan="1" rowspan="1">Genus</th>
                                    <th colspan="1" rowspan="1">Disabled</th>
                                    <th colspan="1" rowspan="1">Created</th>
                                    <th colspan="1" rowspan="1">Modified</th>
                                    <th colspan="1" rowspan="1">Model Id</th>
                                    <th colspan="1" rowspan="1">Name</th>
                                    <th colspan="1" rowspan="1">Manufacturer</th>
                                    <th colspan="1" rowspan="1">Device Type</th>
                                    <th colspan="1" rowspan="1">Disabled</th>
                                    <th colspan="1" rowspan="1">Created</th>
                                    <th colspan="1" rowspan="1">Modified</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:Device">
                                <tr>
                                    <th colspan="1" rowspan="2">Device Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="7" rowspan="1">Model</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="1">Model Id</th>
                                    <th colspan="1" rowspan="1">Name</th>
                                    <th colspan="1" rowspan="1">Manufacturer</th>
                                    <th colspan="1" rowspan="1">Device Type</th>
                                    <th colspan="1" rowspan="1">Disabled</th>
                                    <th colspan="1" rowspan="1">Created</th>
                                    <th colspan="1" rowspan="1">Modified</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:Animal">
                                <tr>
                                    <th colspan="1" rowspan="2">Animal Id</th>
                                    <th colspan="1" rowspan="2">Name</th>
                                    <th colspan="6" rowspan="1">Species</th>
                                    <th colspan="1" rowspan="2">Sex</th>
                                    <th colspan="1" rowspan="2">Disabled</th>
                                    <th colspan="1" rowspan="2">Created</th>
                                    <th colspan="1" rowspan="2">Modified</th>
                                </tr>
                                <tr>
                                    <th colspan="1" rowspan="1">Species Id</th>
                                    <th colspan="1" rowspan="1">Name</th>
                                    <th colspan="1" rowspan="1">Genus</th>
                                    <th colspan="1" rowspan="1">Disabled</th>
                                    <th colspan="1" rowspan="1">Created</th>
                                    <th colspan="1" rowspan="1">Modified</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:Species">
                                <tr>
                                    <th colspan="1" rowspan="1">Species Id</th>
                                    <th colspan="1" rowspan="1">Name</th>
                                    <th colspan="1" rowspan="1">Genus</th>
                                    <th colspan="1" rowspan="1">Disabled</th>
                                    <th colspan="1" rowspan="1">Created</th>
                                    <th colspan="1" rowspan="1">Modified</th>
                                </tr>
                            </xsl:when>
                            <xsl:when test="gml:featureMember/aatams:DetectionDeploymentAnimalSpecies">
                                <tr>
                                    <th colspan="1" rowspan="1">Detection Id</th>
                                    <th colspan="1" rowspan="1">Detection UTC Time Stamp</th>
                                    <th colspan="1" rowspan="1">Tag Device Id</th>
                                    <th colspan="1" rowspan="1">Tag Name</th>
                                    <th colspan="1" rowspan="1">Tag Model Id</th>
                                    <th colspan="1" rowspan="1">Installation Id</th>
                                    <th colspan="1" rowspan="1">Deployment Id</th>
                                    <th colspan="1" rowspan="1">Receiver Device Id</th>
                                    <th colspan="1" rowspan="1">Receiver Name</th>
                                    <th colspan="1" rowspan="1">Longitude</th>
                                    <th colspan="1" rowspan="1">Latitude</th>
                                    <th colspan="1" rowspan="1">Surgery Id</th>
                                    <th colspan="1" rowspan="1">Animal Id</th>
                                    <th colspan="1" rowspan="1">Species Id</th>
                                    <th colspan="1" rowspan="1">Species Name</th>
                                    <th colspan="1" rowspan="1">Sex</th>
                                </tr>
                            </xsl:when>
                        </xsl:choose>
                    </thead>
                    <tbody>
                        <xsl:choose>
                            <xsl:when test="count(gml:featureMember) = 0">
                                <tr>
                                    <td>
                                        <xsl:value-of select="@numberOfFeatures"/>
                                    </td>
                                </tr>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:for-each select="gml:featureMember">
                                    <tr>
                                        <xsl:apply-templates/>
                                    </tr>
                                </xsl:for-each>
                            </xsl:otherwise>
                        </xsl:choose>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="aatams:Deployment">
        <td>
            <xsl:value-of select="aatams:deploymentId"/>
        </td>
        <xsl:choose>
            <xsl:when test="aatams:within/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:within/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Installation[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:within/aatams:Installation"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:deployed/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:deployed/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Device[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:deployed/aatams:Device"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:responsibility/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:responsibility/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:ProjectRolePerson[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:responsibility/aatams:ProjectRolePerson"/>
            </xsl:otherwise>
        </xsl:choose>
	<xsl:apply-templates select="aatams:location/gml:Point"/>
        <td>
            <xsl:value-of select="aatams:deploymentUtcTimeStamp"/>
        </td>	
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="gml:Point">
        <td>
            <xsl:value-of select="substring-before(gml:coordinates,',')"/>
        </td>
        <td>
            <xsl:value-of select="substring-after(gml:coordinates,',')"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Surgery">
        <td>
            <xsl:value-of select="aatams:surgeryId"/>
        </td>
        <xsl:choose>
            <xsl:when test="aatams:site/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:site/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:GeographicArea[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:site/aatams:GeographicArea"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:surgeryOn/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:surgeryOn/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Animal[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:surgeryOn/aatams:Animal"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:surgeryBy/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:surgeryBy/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Person[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:surgeryBy/aatams:Person"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:implanted/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:implanted/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Device[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:implanted/aatams:Device"/>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:value-of select="aatams:deviceImplantType"/>
        </td>
        <td>
	    <xsl:value-of select="substring-before(aatams:location/gml:Point/gml:coordinates,',')"/>
        </td>
        <td>
            <xsl:value-of select="substring-after(aatams:location/gml:Point/gml:coordinates,',')"/>
        </td> 
        <td>
            <xsl:value-of select="aatams:surgeryUtcTimeStamp"/>
        </td>
        <td style="text-align:left;">
            <xsl:value-of select="aatams:comments"/>
        </td>
        <td>
            <xsl:value-of select="aatams:dnaSampleTaken"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:GeographicArea">
        <td>
            <xsl:value-of select="aatams:geographicAreaId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Installation">
        <td>
            <xsl:value-of select="aatams:installationId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <td>
            <xsl:value-of select="aatams:type"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Device">
        <td>
            <xsl:value-of select="aatams:deviceId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <xsl:choose>
            <xsl:when test="aatams:specification/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:specification/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Model[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:specification/aatams:Model"/>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Model">
        <td>
            <xsl:value-of select="aatams:modelId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <td>
            <xsl:value-of select="aatams:manufacturer"/>
        </td>
        <td>
            <xsl:value-of select="aatams:deviceType"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Detection">
        <td>
            <xsl:value-of select="aatams:detectionId"/>
        </td>
        <xsl:choose>
            <xsl:when test="aatams:detector/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:detector/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:call-template name="detection-deployment">
                    <xsl:with-param name="deployment" select="//aatams:Deployment[@gml:id=$id]"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="detection-deployment">
                    <xsl:with-param name="deployment" select="aatams:detector/aatams:Deployment"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:detected/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:detected/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Device[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:detected/aatams:Device"/>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:value-of select="aatams:detectionUtcTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template name="detection-deployment">
        <xsl:param name="deployment"/>
        <td>
            <xsl:value-of select="$deployment/aatams:deploymentId"/>
        </td>
        <xsl:apply-templates select="$deployment/aatams:location/gml:Point"/>
    </xsl:template>
    <xsl:template match="aatams:ProjectRolePerson">
        <xsl:choose>
            <xsl:when test="aatams:project/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:project/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Project[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:project/aatams:Project"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:role/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:role/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Role[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:role/aatams:Role"/>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="aatams:person/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:person/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Person[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:person/aatams:Person"/>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Project">
        <td>
            <xsl:value-of select="aatams:projectId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <xsl:choose>
            <xsl:when test="aatams:owner/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:owner/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Organization[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:owner/aatams:Organization"/>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Organization">
        <td>
            <xsl:value-of select="aatams:organizationId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <td>
            <xsl:value-of select="aatams:initials"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Role">
        <td>
            <xsl:value-of select="aatams:roleId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Person">
        <td>
            <xsl:value-of select="aatams:personId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:firstName"/>
        </td>
        <td>
            <xsl:value-of select="aatams:lastName"/>
        </td>
        <td>
            <xsl:value-of select="aatams:email"/>
        </td>
        <td>
            <xsl:value-of select="aatams:address"/>
        </td>
        <td>
            <xsl:value-of select="aatams:phone"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Animal">
        <td>
            <xsl:value-of select="aatams:animalId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <xsl:choose>
            <xsl:when test="aatams:classification/@xlink:href != ''">
                <xsl:variable name="id">
                    <xsl:value-of select="substring-after(aatams:classification/@xlink:href,'#')"/>
                </xsl:variable>
                <xsl:apply-templates select="//aatams:Species[@gml:id=$id]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="aatams:classification/aatams:Species"/>
            </xsl:otherwise>
        </xsl:choose>
        <td>
            <xsl:value-of select="aatams:sex"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:Species">
        <td>
            <xsl:value-of select="aatams:speciesId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:name"/>
        </td>
        <td>
            <xsl:value-of select="aatams:genus"/>
        </td>
        <td>
            <xsl:value-of select="aatams:disabled"/>
        </td>
        <td>
            <xsl:value-of select="aatams:createdTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:modifiedTimeStamp"/>
        </td>
    </xsl:template>
    <xsl:template match="aatams:DetectionDeploymentAnimalSpecies">
        <td>
            <xsl:value-of select="aatams:detectionId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:detectionUtcTimeStamp"/>
        </td>
        <td>
            <xsl:value-of select="aatams:tagId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:tagName"/>
        </td>
        <td>
            <xsl:value-of select="aatams:tagModelId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:installationId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:deploymentId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:receiverId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:receiverName"/>
        </td>
        <xsl:apply-templates select="aatams:location/gml:Point"/>
        <td>
            <xsl:value-of select="aatams:surgeryId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:animalId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:speciesId"/>
        </td>
        <td>
            <xsl:value-of select="aatams:speciesName"/>
        </td>
        <td>
            <xsl:value-of select="aatams:sex"/>
        </td>
    </xsl:template>
</xsl:stylesheet>
