<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="1.0" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gml="http://www.opengis.net/gml" xmlns:iso19112="http://www.opengis.net/iso19112" xmlns:wfs="http://www.opengis.net/wfs" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan" xmlns="http://www.w3.org/1999/xhtml">
	<xsl:output method="xml" version="1.0" encoding="iso-8859-1" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />
	<xsl:template match="wfs:FeatureCollection">
		<html>
			<head>
				<style type="text/css">
                	th{ border: 1px solid #6699CC;
					border-collapse: collapse;
					background-color: #BEC8D1;
					font-family: Verdana;
					font-weight: bold;
					font-size: 11px;
					color: /*#404040*/ black; padding: 5px; }
               	 	tr{ vertical-align:top; }
					td{ border: 1px solid #9CF;
                    border-collapse: collapse;
                    font-family: Verdana, sans-serif, Arial;
                    padding:0px 10px;
                    text-align:right;
					font-weight: normal;
					font-size: 11px;
					color: /*#404040*/ black;
					white-space: nowrap;
					background-color: #fafafa; }
					table{ text-align: center;
					font-family: Verdana;
					font-weight: normal;
					font-size: 11px;
					color: /*#404040*/ black;
					background-color: #fafafa;
					border: 1px #6699CC solid;
					border-collapse: collapse;
                    border-spacing: 0px; }
                    .text {text-align:left;}
				</style>
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
							<xsl:when test="gml:featureMember[1]/aatams:receiver_deployment">
								<tr>
									<th colspan="1" rowspan="2">ID</th>
									<th colspan="3" rowspan="1">Installation</th>
									<th colspan="2" rowspan="1">Station</th>
									<th colspan="1" rowspan="2">Project ID</th>
									<th colspan="2" rowspan="1">Receiver</th>
									<th colspan="2" rowspan="1">Location</th>
									<th colspan="1" rowspan="2">Deployment UTC Date-Time</th>
									<th colspan="1" rowspan="2">Mooring Type</th>
									<th colspan="1" rowspan="2">Bottom Depth</th>
									<th colspan="1" rowspan="2">Comments</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Type</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:detection">
								<tr>
									<th colspan="1" rowspan="2">Detection ID</th>
									<th colspan="2" rowspan="1">Installation</th>
									<th colspan="2" rowspan="1">Tag</th>
									<th colspan="3" rowspan="1">Animal Classification</th>
									<th colspan="3" rowspan="1">Receiver</th>
									<th colspan="3" rowspan="1">Detection</th>
									<th colspan="3" rowspan="1">Release</th>
								</tr>
								<tr>
									<!--Installation-->
									<th colspan="1" rowspan="1">Installation ID</th>
									<th colspan="1" rowspan="1">Station ID</th>
									<!--Tag-->
									<th colspan="1" rowspan="1">Tag ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<!--Classification-->
									<th colspan="1" rowspan="1">Family ID</th>
									<th colspan="1" rowspan="1">Genus ID</th>
									<th colspan="1" rowspan="1">Species ID</th>
									<!--Receiver-->
									<th colspan="1" rowspan="1">Deployment ID</th>
									<th colspan="1" rowspan="1">Receiver ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<!--Detection -->
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
									<th colspan="1" rowspan="1">UTC Date-time</th>
									<!--Release -->
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
									<th colspan="1" rowspan="1">UTC Date-time</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:surgery">
								<tr>
									<th colspan="1" rowspan="3">Surgery FID</th>
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
									<th colspan="1" rowspan="2">Geographic Area FID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="1" rowspan="2">Disabled</th>
									<th colspan="1" rowspan="2">Created</th>
									<th colspan="1" rowspan="2">Modified</th>
									<th colspan="1" rowspan="2">Animal FID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="6" rowspan="1">Species</th>
									<th colspan="1" rowspan="2">Sex</th>
									<th colspan="1" rowspan="2">Disabled</th>
									<th colspan="1" rowspan="2">Created</th>
									<th colspan="1" rowspan="2">Modified</th>
									<th colspan="1" rowspan="2">Person FID</th>
									<th colspan="1" rowspan="2">First Name</th>
									<th colspan="1" rowspan="2">Last Name</th>
									<th colspan="1" rowspan="2">Email</th>
									<th colspan="1" rowspan="2">Address</th>
									<th colspan="1" rowspan="2">Phone</th>
									<th colspan="1" rowspan="2">Disabled</th>
									<th colspan="1" rowspan="2">Created</th>
									<th colspan="1" rowspan="2">Modified</th>
									<th colspan="1" rowspan="2">Device FID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="7" rowspan="1">Model</th>
									<th colspan="1" rowspan="2">Disabled</th>
									<th colspan="1" rowspan="2">Created</th>
									<th colspan="1" rowspan="2">Modified</th>
									<th colspan="1" rowspan="2">Longitude</th>
									<th colspan="1" rowspan="2">Latitude</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Species FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Genus</th>
									<th colspan="1" rowspan="1">Disabled</th>
									<th colspan="1" rowspan="1">Created</th>
									<th colspan="1" rowspan="1">Modified</th>
									<th colspan="1" rowspan="1">Model FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Manufacturer</th>
									<th colspan="1" rowspan="1">Device Type</th>
									<th colspan="1" rowspan="1">Disabled</th>
									<th colspan="1" rowspan="1">Created</th>
									<th colspan="1" rowspan="1">Modified</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:device">
								<tr>
									<th colspan="1" rowspan="2">Device ID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="4" rowspan="1">Model</th>
									<th colspan="3" rowspan="1">Project</th>
								</tr>
								<tr>
									<!-- Model -->
									<th colspan="1" rowspan="1">Model ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Manufacturer</th>
									<th colspan="1" rowspan="1">Device Type</th>
									<!-- Project -->
									<th colspan="1" rowspan="1">Project ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Person(Role)</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:tag_device or gml:featureMember[1]/aatams:receiver_device">
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Model Name</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:device_model">
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Type</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Manufacturer</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:project_person">
								<tr>
									<th colspan="2" rowspan="1">Project</th>
									<th colspan="2" rowspan="1">Person</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Person ID</th>
									<th colspan="1" rowspan="1">Name(Role)</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:animal">
								<tr>
									<th colspan="1" rowspan="2">Animal FID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="6" rowspan="1">Species</th>
									<th colspan="1" rowspan="2">Sex</th>
									<th colspan="1" rowspan="2">Disabled</th>
									<th colspan="1" rowspan="2">Created</th>
									<th colspan="1" rowspan="2">Modified</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Species FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Genus</th>
									<th colspan="1" rowspan="1">Disabled</th>
									<th colspan="1" rowspan="1">Created</th>
									<th colspan="1" rowspan="1">Modified</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:species">
								<tr>
									<th colspan="1" rowspan="1">Species FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Genus</th>
									<th colspan="1" rowspan="1">Disabled</th>
									<th colspan="1" rowspan="1">Created</th>
									<th colspan="1" rowspan="1">Modified</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:classification">
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Level</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Common Name</th>
									<th colspan="1" rowspan="1">
										<a href="http://www.marine.csiro.au/caab/">CAAB code</a>
									</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:tag_release">
								<tr>
									<th colspan="1" rowspan="2">Tag Release ID</th>
									<th colspan="1" rowspan="2">Project</th>
									<th colspan="2" rowspan="1">Tag Device</th>
									<th colspan="1" rowspan="2">Animal Classification</th>
									<th colspan="2" rowspan="1">Capture Location</th>
									<th colspan="1" rowspan="2">Capture Datetime</th>
									<th colspan="2" rowspan="1">Release Location</th>
									<th colspan="1" rowspan="2">Release Datetime</th>
									<th colspan="1" rowspan="2">Implant Type</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Device ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:detections_by_installation">
								<tr>
									<th colspan="3" rowspan="1">Installation</th>
									<th colspan="3" rowspan="1">Tag</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Detection Count</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Detection Count</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:detections_by_installation_station">
								<tr>
									<th colspan="2" rowspan="1">Installation</th>
									<th colspan="5" rowspan="1">Station</th>
									<th colspan="3" rowspan="1">Tag</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
									<th colspan="1" rowspan="1">Detection Count</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Detection Count</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:detections_by_classification_tag">
								<tr>
									<th colspan="3" rowspan="1">Classification</th>
									<th colspan="3" rowspan="1">Tag</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Family</th>
									<th colspan="1" rowspan="1">Genus</th>
									<th colspan="1" rowspan="1">Species</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Detection Count</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:detections_by_project_tag">
								<tr>
									<th colspan="2" rowspan="1">Project</th>
									<th colspan="3" rowspan="1">Tag</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Detection Count</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:installation">
								<tr>
									<th colspan="3" rowspan="1">Installation</th>
									<th colspan="4" rowspan="1">Station</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Configuration Type</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:project">
								<tr>
									<th colspan="2" rowspan="1">Project</th>
									<th colspan="2" rowspan="1">Organisation</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:person">
								<tr>
									<th colspan="1" rowspan="2">ID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="1" rowspan="2">Personal Mobile Number</th>
									<th colspan="1" rowspan="2">Phone Number</th>
									<th colspan="1" rowspan="2">Email</th>
									<th colspan="5" rowspan="1">Home Physical Address</th>
									<th colspan="5" rowspan="1">Home Postal Address</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Address 1</th>
									<th colspan="1" rowspan="1">Address 2</th>
									<th colspan="1" rowspan="1">State</th>
									<th colspan="1" rowspan="1">Country</th>
									<th colspan="1" rowspan="1">Postcode</th>
									<th colspan="1" rowspan="1">Address 1</th>
									<th colspan="1" rowspan="1">Address 2</th>
									<th colspan="1" rowspan="1">State</th>
									<th colspan="1" rowspan="1">Country</th>
									<th colspan="1" rowspan="1">Postcode</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:organisation">
								<tr>
									<th colspan="1" rowspan="2">ID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="1" rowspan="2">Phone Number</th>
									<th colspan="1" rowspan="2">Fax Number</th>
									<th colspan="5" rowspan="1">Physical Address</th>
									<th colspan="5" rowspan="1">Postal Address</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Address 1</th>
									<th colspan="1" rowspan="1">Address 2</th>
									<th colspan="1" rowspan="1">State</th>
									<th colspan="1" rowspan="1">Country</th>
									<th colspan="1" rowspan="1">Postcode</th>
									<th colspan="1" rowspan="1">Address 1</th>
									<th colspan="1" rowspan="1">Address 2</th>
									<th colspan="1" rowspan="1">State</th>
									<th colspan="1" rowspan="1">Country</th>
									<th colspan="1" rowspan="1">Postcode</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:tag_release_min">
								<tr>
									<th colspan="1" rowspan="2">Release ID</th>
									<th colspan="3" rowspan="1">Classification</th>
									<th colspan="2" rowspan="1">Project</th>
									<th colspan="2" rowspan="1">Tag</th>
									<th colspan="3" rowspan="1">Release</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Family</th>
									<th colspan="1" rowspan="1">Genus</th>
									<th colspan="1" rowspan="1">Species</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
									<th colspan="1" rowspan="1">Date-time</th>
								</tr>
							</xsl:when>
						</xsl:choose>
					</thead>
					<tbody>
						<xsl:choose>
							<xsl:when test="count(gml:featureMember) = 0">
								<tr>
									<td>
										<xsl:value-of select="@numberOfFeatures" />
									</td>
								</tr>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="gml:featureMember[1]/aatams:receiver_deployment">
										<xsl:for-each select="gml:featureMember/aatams:receiver_deployment">
											<xsl:sort select="aatams:installation_ref/aatams:installation/aatams:name" />
											<xsl:sort select="aatams:station_ref/aatams:station/aatams:curtain_position" data-type="number"/>
											<!--xsl:sort select="aatams:installation_station_ref/aatams:installation_station/aatams:name" />
											<xsl:sort select="aatams:deployment_timestamp" /-->
											<xsl:apply-templates select="."/>
										</xsl:for-each>
									</xsl:when>
									<xsl:when test="gml:featureMember[1]/aatams:device_model">
										<xsl:for-each select="gml:featureMember/aatams:device_model">
											<xsl:sort select="aatams:device_type_ref/aatams:device_type/aatams:name" />
											<xsl:sort select="aatams:name" />
											<xsl:apply-templates select="."/>
										</xsl:for-each>
									</xsl:when>
									<xsl:when test="gml:featureMember[1]/aatams:installation">
										<xsl:for-each select="gml:featureMember/aatams:installation">
											<xsl:sort select="aatams:name" />
											<xsl:choose>
												<xsl:when test="aatams:installation_conf_ref/aatams:installation_conf/aatams:name = 'CURTAIN' and count(aatams:installation_station_ref)&gt;0">
													<xsl:call-template name="installation-stations-by-curtain-position" />
												</xsl:when>
												<xsl:otherwise>
													<xsl:call-template name="installation-stations-by-name" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:for-each>
									</xsl:when>
									<xsl:when test="gml:featureMember[1]/*/aatams:name">
										<xsl:for-each select="gml:featureMember">
											<xsl:sort select="*/aatams:name" />
											<xsl:apply-templates select="."/>
										</xsl:for-each>
									</xsl:when>
									<xsl:otherwise>
										<xsl:for-each select="gml:featureMember">
											<xsl:apply-templates />
										</xsl:for-each>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="aatams:receiver_deployment">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.receiver_deployment.')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:installation_ref/aatams:installation/@gml:id,'aatams.installation.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:installation_ref/aatams:installation/aatams:name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:installation_ref/aatams:installation/aatams:installation_conf_ref/aatams:installation_conf/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:station_ref/aatams:station/@gml:id,'aatams.station.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:station_ref/aatams:station/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:project_person_ref/aatams:project_person/aatams:project_fid,'aatams.project.')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:receiver_device_ref/aatams:receiver_device/@gml:id,'aatams.receiver_device.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:receiver_device_ref/aatams:receiver_device/aatams:code_name" />
			</td>
			<td>
				<xsl:value-of select="substring-before(aatams:location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="translate(aatams:deployment_timestamp,'T',' ')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:mooring_type_ref/aatams:mooring_type/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:bottom_depth" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:comments" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:tag_release">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.tag_release.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_name" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:tag_device_ref/aatams:tag_device/@gml:id,'aatams.tag_device.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:tag_device_ref/aatams:tag_device/aatams:code_name" />
			</td>
			<td class="text">
				<xsl:choose>
					<xsl:when test="aatams:species_ref">
						<xsl:value-of select="aatams:species_ref/aatams:species/aatams:name" />
					</xsl:when>
					<xsl:when test="aatams:genus_ref">
						<xsl:value-of select="aatams:genus_ref/aatams:genus/aatams:name" />
					</xsl:when>
					<xsl:when test="aatams:family_ref">
						<xsl:value-of select="aatams:family_ref/aatams:family/aatams:name" />
					</xsl:when>
				</xsl:choose>
			</td>
			<td>
				<xsl:value-of select="substring-before(aatams:capture_location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:capture_location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="translate(aatams:capture_timestamp,'T',' ')" />
			</td>
			<td>
				<xsl:value-of select="substring-before(aatams:release_location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:release_location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="translate(aatams:release_timestamp,'T',' ')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:implant_type_ref/aatams:implant_type/aatams:name" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:device">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.device.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:code_name" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:device_model_ref/aatams:device_model/@gml:id,'aatams.device_model.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:device_manufacturer_ref/aatams:device_manufacturer/aatams:name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:device_type_ref/aatams:device_type/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:project_person_ref/aatams:project_person/aatams:project_fid,'aatams.project.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:person_role" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:tag_device">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.tag_device.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:code_name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:model_name" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:receiver_device">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.receiver_device.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:code_name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:model_name" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:device_model">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.device_model.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:device_type_ref/aatams:device_type/aatams:name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:device_manufacturer_ref/aatams:device_manufacturer/aatams:name" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:detection">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.detection.')" />
			</td>
			<td>
				<xsl:value-of select="aatams:installation_id" />
			</td>
			<td>
				<xsl:value-of select="aatams:station_id" />
			</td>
			<td>
				<xsl:value-of select="aatams:tag_id" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:tag_code_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:family_id" />
			</td>
			<td>
				<xsl:value-of select="aatams:genus_id" />
			</td>
			<td>
				<xsl:value-of select="aatams:species_id" />
			</td>
			<td>
				<xsl:value-of select="aatams:deployment_id" />
			</td>
			<td>
				<xsl:value-of select="aatams:receiver_id" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:receiver_code_name" />
			</td>
			<td>
				<xsl:value-of select="substring-before(aatams:location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="translate(aatams:detection_timestamp,'T',' ')" />
			</td>
			<td>
				<xsl:value-of select="substring-before(aatams:release_location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:release_location/gml:Point/gml:pos,' ')" />
			</td>
			<td>
				<xsl:value-of select="translate(aatams:release_timestamp,'T',' ')" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:detections_by_installation">
		<xsl:variable name="tag_row_count">
			<xsl:value-of select="count(aatams:detections_by_installation_tag_ref)" />
		</xsl:variable>
		<tr>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="substring-after(@gml:id,'aatams.detections_by_installation.')" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1" class="text">
				<xsl:value-of select="aatams:installation_name" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:count" />
			</td>
			<td rowspan="1" colspan="1">
				<xsl:value-of select="aatams:detections_by_installation_tag_ref[1]/aatams:detections_by_installation_tag/aatams:tag" />
			</td>
			<td rowspan="1" colspan="1" class="text">
				<xsl:value-of select="aatams:detections_by_installation_tag_ref[1]/aatams:detections_by_installation_tag/aatams:tag_name" />
			</td>
			<td rowspan="1" colspan="1">
				<xsl:value-of select="aatams:detections_by_installation_tag_ref[1]/aatams:detections_by_installation_tag/aatams:count" />
			</td>
		</tr>
		<xsl:for-each select="aatams:detections_by_installation_tag_ref[position()&gt;1]">
			<tr>
				<td rowspan="1" colspan="1">
					<xsl:value-of select="aatams:detections_by_installation_tag/aatams:tag" />
				</td>
				<td rowspan="1" colspan="1" class="text">
					<xsl:value-of select="aatams:detections_by_installation_tag/aatams:tag_name" />
				</td>
				<td rowspan="1" colspan="1">
					<xsl:value-of select="aatams:detections_by_installation_tag/aatams:count" />
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="aatams:detections_by_installation_station">
		<xsl:variable name="tag_row_count">
			<xsl:value-of select="count(aatams:detections_by_station_tag_ref)" />
		</xsl:variable>
		<tr>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:installation" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1" class="text">
				<xsl:value-of select="aatams:installation_name" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:station" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1" class="text">
				<xsl:value-of select="aatams:station_name" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="substring-before(aatams:location/gml:Point/gml:pos,' ')" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="substring-after(aatams:location/gml:Point/gml:pos,' ')" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:count" />
			</td>
			<td rowspan="1" colspan="1">
				<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:tag_id" />
			</td>
			<td rowspan="1" colspan="1" class="text">
				<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:tag_code_name" />
			</td>
			<td rowspan="1" colspan="1">
				<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:count" />
			</td>
		</tr>
		<xsl:for-each select="aatams:detections_by_station_tag_ref[position()&gt;1]">
			<tr>
				<td rowspan="1" colspan="1">
					<xsl:value-of select="aatams:detections_by_station_tag/aatams:tag_id" />
				</td>
				<td rowspan="1" colspan="1" class="text">
					<xsl:value-of select="aatams:detections_by_station_tag/aatams:tag_code_name" />
				</td>
				<td rowspan="1" colspan="1">
					<xsl:value-of select="aatams:detections_by_station_tag/aatams:count" />
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="aatams:detections_by_project_tag">
		<tr>
			<td>
				<xsl:value-of select="aatams:project" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:project_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:tag" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:tag_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:count" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:detections_by_classification_tag">
		<tr>
			<td>
				<xsl:value-of select="aatams:family" />
			</td>
			<td>
				<xsl:value-of select="aatams:genus" />
			</td>
			<td>
				<xsl:value-of select="aatams:species" />
			</td>
			<td>
				<xsl:value-of select="aatams:tag" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:tag_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:count" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template name="installation-stations-by-curtain-position">
		<xsl:variable name="station_row_count">
			<xsl:choose>
				<xsl:when test="count(aatams:installation_station_ref) &gt; 0">
					<xsl:value-of select="count(aatams:installation_station_ref)" />
				</xsl:when>
				<xsl:otherwise>1</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td rowspan="{$station_row_count}" colspan="1">
				<xsl:value-of select="substring-after(@gml:id,'aatams.installation.')" />
			</td>
			<td rowspan="{$station_row_count}" colspan="1" class="text">
				<xsl:value-of select="aatams:name" />
			</td>
			<td rowspan="{$station_row_count}" colspan="1" class="text">
				<xsl:value-of select="aatams:installation_conf_ref/aatams:installation_conf/aatams:name" />
			</td>
			<xsl:choose>
				<xsl:when test="aatams:installation_station_ref/aatams:installation_station[aatams:curtain_position='1']">
					<xsl:apply-templates select="aatams:installation_station_ref/aatams:installation_station[aatams:curtain_position='1']" />
				</xsl:when>
				<xsl:otherwise>
					<td />
					<td />
					<td />
					<td />
				</xsl:otherwise>
			</xsl:choose>
		</tr>
		<xsl:for-each select="aatams:installation_station_ref/aatams:installation_station[aatams:curtain_position!='1']">
			<xsl:sort select="aatams:curtain_position" data-type="number"/>
			<tr>
				<xsl:apply-templates select="." />
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="installation-stations-by-name">
		<xsl:variable name="station_row_count">
			<xsl:choose>
				<xsl:when test="count(aatams:installation_station_ref) &gt; 0">
					<xsl:value-of select="count(aatams:installation_station_ref)" />
				</xsl:when>
				<xsl:otherwise>1</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<td rowspan="{$station_row_count}" colspan="1">
				<xsl:value-of select="substring-after(@gml:id,'aatams.installation.')" />
			</td>
			<td rowspan="{$station_row_count}" colspan="1" class="text">
				<xsl:value-of select="aatams:name" />
			</td>
			<td rowspan="{$station_row_count}" colspan="1" class="text">
				<xsl:value-of select="aatams:installation_conf_ref/aatams:installation_conf/aatams:name" />
			</td>
			<xsl:choose>
				<xsl:when test="aatams:installation_station_ref">
					<xsl:for-each select="aatams:installation_station_ref/aatams:installation_station">
						<xsl:sort select="aatams:name" />
						<xsl:if test="position()=1">
							<xsl:apply-templates select="." />
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<td />
					<td />
					<td />
					<td />
				</xsl:otherwise>
			</xsl:choose>
		</tr>
		<xsl:for-each select="aatams:installation_station_ref/aatams:installation_station">
			<xsl:sort select="aatams:name" />
			<xsl:if test="position()>1">
				<tr>
					<xsl:apply-templates select="." />
				</tr>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="aatams:installation_station">
		<td rowspan="1" colspan="1">
			<xsl:value-of select="substring-after(@gml:id,'aatams.installation_station.')" />
		</td>
		<td rowspan="1" colspan="1" class="text">
			<xsl:value-of select="aatams:name" />
		</td>
		<td rowspan="1" colspan="1">
			<xsl:value-of select="substring-before(aatams:location/gml:Point/gml:pos,' ')" />
		</td>
		<td rowspan="1" colspan="1">
			<xsl:value-of select="substring-after(aatams:location/gml:Point/gml:pos,' ')" />
		</td>
	</xsl:template>
	<xsl:template match="aatams:project">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.project.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:name" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:organisation_ref/aatams:organisation/@gml:id,'aatams.organisation.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:organisation_ref/aatams:organisation/aatams:name" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:person">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.person.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:personal_mobile_number" />
			</td>
			<td>
				<xsl:value-of select="aatams:home_phone_number" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:email" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_physical_address_1" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_physical_address_2" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_physical_address_state" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_physical_address_country" />
			</td>
			<td>
				<xsl:value-of select="aatams:home_physical_address_postcode" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_postal_address_1" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_postal_address_2" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_postal_address_state" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:home_postal_address_country" />
			</td>
			<td>
				<xsl:value-of select="aatams:home_postal_address_postcode" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:organisation">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.organisation.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:phone_number" />
			</td>
			<td>
				<xsl:value-of select="aatams:fax_number" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:physical_address_1" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:physical_address_2" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:physical_address_state" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:physical_address_country" />
			</td>
			<td>
				<xsl:value-of select="aatams:physical_address_postcode" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:postal_address_1" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:postal_address_2" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:postal_address_state" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:postal_address_country" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:postal_address_postcode" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:tag_release_min">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.tag_release_min.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:family" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:genus" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:species" />
			</td>
			<td>
				<xsl:value-of select="aatams:project" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:project_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:tag" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:code_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_longitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_latitude" />
			</td>
			<td>
				<xsl:value-of select="translate(aatams:release_timestamp,'T',' ')" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:classification">
		<tr>
			<td>
				<xsl:value-of select="substring-after(@gml:id,'aatams.classification.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:classification_level_ref/aatams:classification_level/aatams:name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:name" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:common_name" />
			</td>
			<td class="text">
				<a>
					<xsl:attribute name="href">
						<xsl:text>http://www.marine.csiro.au/caabsearch/caab_search.caab_report?spcode=</xsl:text>
						<xsl:value-of select="aatams:caab_code" />
					</xsl:attribute>
					<xsl:value-of select="aatams:caab_code" />
				</a>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:project_person">
		<tr>
			<td>
				<xsl:value-of select="substring-after(aatams:project_fid,'aatams.project.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:project_name" />
			</td>
			<td>
				<xsl:value-of select="substring-after(aatams:person_fid,'aatams.person.')" />
			</td>
			<td class="text">
				<xsl:value-of select="aatams:person_role" />
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>