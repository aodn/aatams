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
					border-collapse: collapse;font-family: Verdana, sans-serif, Arial;
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
									<th colspan="1" rowspan="2">Deployment FID</th>
									<th colspan="3" rowspan="1">Installation</th>
									<th colspan="2" rowspan="1">Station</th>
									<th colspan="2" rowspan="1">Receiver</th>
									<th colspan="2" rowspan="1">Location</th>
									<th colspan="1" rowspan="2">Deployment UTC Date-Time</th>
									<th colspan="1" rowspan="2">Mooring Type</th>
									<th colspan="1" rowspan="2">Bottom Depth</th>
									<th colspan="1" rowspan="2">Comments</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Installation FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Type</th>
									<th colspan="1" rowspan="1">Station FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Device FID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:detection">
								<tr>
									<th colspan="1" rowspan="2">Detection FID</th>
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
									<th colspan="1" rowspan="2">Device FID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="4" rowspan="1">Model</th>
									<th colspan="3" rowspan="1">Project</th>
								</tr>
								<tr>
									<!-- Model -->
									<th colspan="1" rowspan="1">Model FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Manufacturer</th>
									<th colspan="1" rowspan="1">Device Type</th>
									<!-- Project -->
									<th colspan="1" rowspan="1">Project FID</th>
									<th colspan="1" rowspan="1">Name</th>
									<th colspan="1" rowspan="1">Person(Role)</th>
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
							<xsl:when test="gml:featureMember[1]/aatams:display_tag_release">
								<tr>
									<th colspan="1" rowspan="2">Tag Release FID</th>
									<th colspan="1" rowspan="2">Project FID</th>
									<th colspan="2" rowspan="1">Tag Device</th>
									<th colspan="1" rowspan="2">Animal Classification</th>
									<th colspan="2" rowspan="1">Capture Location</th>
									<th colspan="1" rowspan="2">Capture Datetime</th>
									<th colspan="2" rowspan="1">Release Location</th>
									<th colspan="1" rowspan="2">Release Datetime</th>
									<th colspan="1" rowspan="2">Implant Type</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Device FID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
									<th colspan="1" rowspan="1">Longitude</th>
									<th colspan="1" rowspan="1">Latitude</th>
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
									<th colspan="1" rowspan="2">Project</th>
									<th colspan="3" rowspan="1">Tag</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">ID</th>
									<th colspan="1" rowspan="1">Code Name</th>
									<th colspan="1" rowspan="1">Detection Count</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:installation">
								<tr>
									<th>ID</th>
									<th>Name</th>
									<th>Configuration Type</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:project">
								<tr>
									<th>ID</th>
									<th>Name</th>
								</tr>
							</xsl:when>
							<xsl:when test="gml:featureMember[1]/aatams:person">
								<tr>
									<th colspan="1" rowspan="2">ID</th>
									<th colspan="1" rowspan="2">Name</th>
									<th colspan="1" rowspan="2">Personal Mobile Number</th>
									<th colspan="1" rowspan="2">Home Phone Number</th>
									<th colspan="1" rowspan="2">email</hr>
									<th colspan="5" rowspan="1">Home Physical Address</th>
									<th colspan="5" rowspan="1">Home Postal Address</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Address 1</th>
									<th colspan="1" rowspan="1">Address 2</th>
									<th colspan="1" rowspan="1">State</th>
									<th colspan="1" rowspan="1">Country</th>
									<th colspan="1" rowspan="1">Postcode</th>
								</tr>
								<tr>
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
									<th colspan="1" rowspan="2">Personal Mobile Number</th>
									<th colspan="1" rowspan="2">Home Phone Number</th>
									<th colspan="1" rowspan="2">email</hr>
									<th colspan="5" rowspan="1">Home Physical Address</th>
									<th colspan="5" rowspan="1">Home Postal Address</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Address 1</th>
									<th colspan="1" rowspan="1">Address 2</th>
									<th colspan="1" rowspan="1">State</th>
									<th colspan="1" rowspan="1">Country</th>
									<th colspan="1" rowspan="1">Postcode</th>
								</tr>
								<tr>
									<th colspan="1" rowspan="1">Address 1</th>
									<th colspan="1" rowspan="1">Address 2</th>
									<th colspan="1" rowspan="1">State</th>
									<th colspan="1" rowspan="1">Country</th>
									<th colspan="1" rowspan="1">Postcode</th>
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
								<xsl:for-each select="gml:featureMember">
									<xsl:apply-templates />
								</xsl:for-each>
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
				<xsl:value-of select="@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:installation_ref/aatams:installation/@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:installation_ref/aatams:installation/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:installation_ref/aatams:installation/aatams:installation_conf_ref/aatams:installation_conf/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:station_ref/aatams:station/@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:station_ref/aatams:station/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:receiver_device_ref/aatams:receiver_device/@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:receiver_device_ref/aatams:receiver_device/aatams:code_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:longitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:latitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:deployment_timestamp" />
			</td>
			<td>
				<xsl:value-of select="aatams:mooring_type_ref/aatams:mooring_type/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:bottom_depth" />
			</td>
			<td>
				<xsl:value-of select="aatams:comments" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="gml:Point">
		<tr>
			<td>
				<xsl:value-of select="substring-before(gml:coordinates,',')" />
			</td>
			<td>
				<xsl:value-of select="substring-after(gml:coordinates,',')" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:display_tag_release">
		<tr>
			<td>
				<xsl:value-of select="@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_fid" />
			</td>
			<td>
				<xsl:value-of select="aatams:transmitter_device_ref/aatams:transmitter_device/@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:transmitter_device_ref/aatams:transmitter_device/aatams:code_name" />
			</td>
			<td>
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
				<xsl:value-of select="aatams:capture_longitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:capture_latitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:capture_timestamp" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_longitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_latitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_timestamp" />
			</td>
			<td>
				<xsl:value-of select="aatams:implant_type_ref/aatams:implant_type/aatams:name" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:device">
		<tr>
			<td>
				<xsl:value-of select="@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:code_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:device_model_ref/aatams:device_model/@gml:id" />
			</td>
			<td>
				<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:device_manufacturer_ref/aatams:device_manufacturer/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:device_type_ref/aatams:device_type/aatams:name" />
			</td>
			<td>
				<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_fid" />
			</td>
			<td>
				<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:person_role" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:detection">
		<tr>
			<td>
				<xsl:value-of select="aatams:detection_id" />
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
			<td>
				<xsl:value-of select="aatams:tag_name" />
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
			<td>
				<xsl:value-of select="aatams:receiver_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:longitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:latitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:detection_timestamp" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_longitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_latitude" />
			</td>
			<td>
				<xsl:value-of select="aatams:release_timestamp" />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="aatams:detections_by_installation_station">
		<xsl:variable name="tag_row_count">
			<xsl:value-of select="count(aatams:detections_by_station_tag_ref)" />
		</xsl:variable>
		<tr>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:installation" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:installation_name" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:station" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:station_name" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:longitude" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:latitude" />
			</td>
			<td rowspan="{$tag_row_count}" colspan="1">
				<xsl:value-of select="aatams:count" />
			</td>
			<td rowspan="1" colspan="1">
				<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:tag" />
			</td>
			<td rowspan="1" colspan="1">
				<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:tag_name" />
			</td>
			<td rowspan="1" colspan="1">
				<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:count" />
			</td>
		</tr>
		<xsl:for-each select="aatams:detections_by_station_tag_ref[position()&gt;1]">
			<tr>
				<td rowspan="1" colspan="1">
					<xsl:value-of select="aatams:detections_by_station_tag/aatams:tag" />
				</td>
				<td rowspan="1" colspan="1">
					<xsl:value-of select="aatams:detections_by_station_tag/aatams:tag_name" />
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
			<td>
				<xsl:value-of select="aatams:tag" />
			</td>
			<td>
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
			<td>
				<xsl:value-of select="aatams:tag_name" />
			</td>
			<td>
				<xsl:value-of select="aatams:count" />
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
