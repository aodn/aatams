<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:gml="http://www.opengis.net/gml" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:wfs="http://www.opengis.net/wfs">
    <xsl:output method="xml" version="1.0" encoding="iso-8859-1" omit-xml-declaration="yes" />
    <xsl:strip-space elements="*"/>
    <xsl:template match="wfs:FeatureCollection">
	    <!--<html><body><pre>-->
                    <xsl:choose>
                        <xsl:when test="count(gml:featureMember) = 0">
                            <xsl:text>Member Count&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:Installation">
                            <xsl:text>Installation Id,Name,Installation type,Disabled,Created,Modified&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:Deployment">
                            <xsl:text>Deployment Id,</xsl:text>
                            <xsl:text>Installation.Installation Id,Installation.Name,Installation.Type,Installation.Disabled,Installation.Created,Installation.Modified,</xsl:text>
                            <xsl:text>Device.Device Id,Device.Name,Device.Model.Model Id,Device.Model.Name,Device.Model.Manufacturer,Device.Model.Device Type,Device.Model.Disabled,Device.Model.Created,Device.Model.Modified,Device.Disabled,Device.Created,Device.Modified,</xsl:text>
                            <xsl:text>Project/Role/Person.Project.Project Id,Project/Role/Person.Project.Name,</xsl:text>
                            <xsl:text>Project/Role/Person.Project.Organization.Organization Id,Project/Role/Person.Project.Organization.Name,Project/Role/Person.Project.Organization.Initials,Project/Role/Person.Project.Organization.Disabled,Project/Role/Person.Project.Organization.Created,Project/Role/Person.Project.Organization.Modified,</xsl:text>
                            <xsl:text>Project/Role/Person.Project.Disabled,Project/Role/Person.Project.Created,Project/Role/Person.Project.Modified,</xsl:text>
                            <xsl:text>Project/Role/Person.Role.Role Id,Project/Role/Person.Role.Name,Project/Role/Person.Role.Disabled,Project/Role/Person.Role.Created,Project/Role/Person.Role.Modified,</xsl:text>
                            <xsl:text>Project/Role/Person.Person.Person Id,Project/Role/Person.Person.First Name,Project/Role/Person.Person.Last Name,Project/Role/Person.Person.Email,Project/Role/Person.Person.Address,Project/Role/Person.Person.Phone,Project/Role/Person.Person.Disabled,Project/Role/Person.Person.Created,Project/Role/Person.Person.Modified,</xsl:text>
                            <xsl:text>Project/Role/Person.Disabled,Project/Role/Person.Created,Project/Role/Person.Modified,</xsl:text>
                            <xsl:text>Location.Longitude,Location.Latitude,Deployment UTC Time Stamp,</xsl:text>
                            <xsl:text>Disabled,Created,Modified&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:Detection">
                            <xsl:text>Detection Id, Deployment.Deployment Id, Deployment.Location.Longitude,Deployment.Location.Latitude,</xsl:text>
                            <xsl:text>Tag.Device Id,Tag.Name,Tag.Model.Model Id,Tag.Model.Name,Tag.Model.Manufacturer,Tag.Model.Device Type,Tag.Model.Disabled,Tag.Model.Created,Tag.Model.Modified,Tag.Disabled,Tag.Created,Tag.Modified,</xsl:text>
                            <xsl:text>Detection UTC Time Stamp,Disabled,Created,Modified&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:Surgery">
                            <xsl:text>Surgery Id,</xsl:text>
                            <xsl:text>Geographic Area.Geographic Area Id,Geographic Area.Name,Geographic Area.Disabled,Geographic Area.Created,Geographic Area.Modified,</xsl:text>
                            <xsl:text>Animal.Animal Id,Animal.Name,Animal.Species.Species Id,Animal.Species.Name,Animal.Species.Genus,Animal.Species.Disabled,Animal.Species.Created,Animal.Species.Modified,Animal.Sex,Animal.Disabled,Animal.Created,Animal.Modified,</xsl:text>
                            <xsl:text>Person.Person Id,Person.First Name,Person.Last Name,Person.Email,Person.Address,Person.Phone,Person.Disabled,Person.Created,Person.Modified,</xsl:text>
                            <xsl:text>Device.Device Id,Device.Name,Device.Model.Model Id,Device.Model.Name,Device.Model.Manufacturer,Device.Model.Device Type,Device.Model.Disabled,Device.Model.Created,Device.Model.Modified,Device.Disabled,Device.Created,Device.Modified,</xsl:text>
                            <xsl:text>Device Implant Type,Location.Longitude,Location.Latitude,Surgery UTC Time Stamp,Comments,DNA Sample Taken,Disabled,Created,Modified&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:Device">
                            <xsl:text>Device Id,Name,Model.Model Id,Model.Name,Model.Manufacturer,Model.Device Type,Model.Disabled,Model.Created,Model.Modified,Disabled,Created,Modified&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:Animal">
                            <xsl:text>Animal Id,Name,ISpecies.Species Id,Species.Name,Species.Genus,Species.Disabled,Species.Created,Species.Modified,Sex,Disabled,Created,Modified&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:Species">
                            <xsl:text>Species Id,Name,Genus,Disabled,Created,Modified&#10;</xsl:text>
                        </xsl:when>
                        <xsl:when test="gml:featureMember/aatams:DetectionDeploymentAnimalSpecies">
                            <xsl:text>Detection Id,Detection UTC Time Stamp,Tag Device Id,Tag Name,Tag Model Id,Installation Id,Deployment Id,Receiver Device Id,Receiver Name,Longitude,Latitude,Surgery Id,Animal Id,Species Id,Species Name,Sex&#10;</xsl:text>
                        </xsl:when>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="count(gml:featureMember) = 0">
                            <xsl:value-of select="@numberOfFeatures"/>
                        </xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="gml:featureMember/*"/>
			</xsl:otherwise>
		</xsl:choose>
		<!--</pre></body></html>-->
    </xsl:template>
    <xsl:template match="aatams:Deployment">
        <xsl:value-of select="aatams:deploymentId"/>
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="aatams:location/gml:Point"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:deploymentUtcTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="gml:Point">
        <xsl:value-of select="substring-before(gml:coordinates,',')"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="substring-after(gml:coordinates,',')"/>
    </xsl:template>
    <xsl:template match="aatams:Surgery">
        <xsl:value-of select="aatams:surgeryId"/>
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:deviceImplantType"/>
    <xsl:text>,</xsl:text>
    <xsl:value-of select="substring-before(aatams:location/gml:Point/gml:coordinates,',')"/>
    <xsl:text>,</xsl:text>
        <xsl:value-of select="substring-after(aatams:location/gml:Point/gml:coordinates,',')"/>
    <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:surgeryUtcTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:comment"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:dnaSampleTaken"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:GeographicArea">
        <xsl:value-of select="aatams:geographicAreaId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Installation">
        <xsl:value-of select="aatams:installationId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
        <xsl:value-of select="aatams:type"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
	<xsl:if test="../../gml:featureMember">
            <xsl:text>&#10;</xsl:text>
	</xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Device">
        <xsl:value-of select="aatams:deviceId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
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
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Model">
        <xsl:value-of select="aatams:modelId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
        <xsl:value-of select="aatams:manufacturer"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:deviceType"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Detection">
        <xsl:value-of select="aatams:detectionId"/>
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:detectionUtcTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template name="detection-deployment">
        <xsl:param name="deployment"/>
        <xsl:value-of select="$deployment/aatams:deploymentId"/>
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
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
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Project">
        <xsl:value-of select="aatams:projectId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
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
	<xsl:text>,</xsl:text>
	<xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Organization">
        <xsl:value-of select="aatams:organizationId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
        <xsl:value-of select="aatams:initials"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Role">
        <xsl:value-of select="aatams:roleId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Person">
        <xsl:value-of select="aatams:personId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:firstName"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:lastName"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:email"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:address"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:phone"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Animal">
        <xsl:value-of select="aatams:animalId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
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
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:sex"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:Species">
        <xsl:value-of select="aatams:speciesId"/>
        <xsl:text>,"</xsl:text>
        <xsl:value-of select="aatams:name"/>
        <xsl:text>",</xsl:text>
        <xsl:value-of select="aatams:genus"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:disabled"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:createdTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:modifiedTimeStamp"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
    <xsl:template match="aatams:DetectionDeploymentAnimalSpecies">
        <xsl:value-of select="aatams:detectionId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:detectionUtcTimeStamp"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:tagId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:tagName"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:tagModelId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:installationId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:deploymentId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:receiverId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:receiverName"/>
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="aatams:location/gml:Point"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:surgeryId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:animalId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:speciesId"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:speciesName"/>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="aatams:sex"/>
        <xsl:if test="../../gml:featureMember">
        <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
