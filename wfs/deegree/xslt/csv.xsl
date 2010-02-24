<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="1.0" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gml="http://www.opengis.net/gml" xmlns:iso19112="http://www.opengis.net/iso19112" xmlns:wfs="http://www.opengis.net/wfs" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan" xmlns="http://www.w3.org/1999/xhtml">
	<xsl:output method="xml" version="1.0" encoding="iso-8859-1" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />
	<xsl:template match="wfs:FeatureCollection">
		<html xmlns="http://www.w3.org/1999/xhtml"><body><pre>
		<xsl:choose>
			<xsl:when test="count(gml:featureMember) = 0">
				<xsl:text>Member Count</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:receiver_deployment">
				<xsl:text>Deployment ID,Installation ID,Installation Name,Installation Type,Station ID,Station Name,Project ID,Receiver ID,Receiver Code Name,Deployment Longitude,Deployment Latitude,Deployment UTC Date-Time,Mooring Type,Bottom Depth,Comments</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:detection">
				<xsl:text>Detection ID,Installation ID,Station ID,Tag ID,Tag Code Name,Classification Family ID,Classification Genus ID,Classification Species ID, Receiver Deployment ID, Receiver ID, Receiver Code Name,Detection Longitude,Detection Latitude,Detection UTC Date-time,Release Longitude,Release Latitude,Release UTC Date-time</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:surgery">
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:device">
				<xsl:text>Device ID,Device Code Name,Model ID,Model Name,Manufacturer,Device Type,Project ID,Project Name,Contact Name (Role)</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:tag_device or gml:featureMember[1]/aatams:receiver_device">
				<xsl:text>ID,Code Name,Model Name</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:device_model">
				<xsl:text>ID,Type,Model,Manufacturer</xsl:text>
            </xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:project_person">
				<xsl:text>Project ID,Project Name,Person ID,Person Name</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:animal">
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:classification">
                 <xsl:text>ID,Level,Name,Common Name</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:tag_release">
				<xsl:text>Tag Release ID,Project,Tag ID,Tag Code Name,Animal Classification,Capture Longitude,Capture Latitude,Capture Datetime,Release Longitude,Release Latitude,Release Datetime,Implant Type</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:detections_by_installation_station">	
				<xsl:text>Installation,Station,Tag,ID,Name,ID,Name,Longitude,Latitude,Detection Count,ID,Code Name,Detection Count,</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:detections_by_classification_tag">
				<xsl:text>Classification,Tag,Family,Genus,Species,ID,Code Name,Detection Count</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:detections_by_project_tag">
				<xsl:text>Project,Tag,ID,Name,ID,Code Name,Detection Count,</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:installation">
				<xsl:text>ID,Name,Configuration Type</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:project">
				<xsl:text>Project,Organisation,ID,Name,ID,Name</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:person">
				<xsl:text>ID,Name,Personal Mobile Number,Phone Number,Email,Home Physical Address,Home Postal Address,Address 1,Address 2,State,Country,Postcode,Address 1,Address 2,State,Country,Postcode</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:organisation">
				<xsl:text>ID,Name,Phone Number,Fax Number,Physical Address,Postal Address,,Address 1,Address 2,State,Country,Postcode,Address 1,Address 2,State,Country,Postcode</xsl:text>
			</xsl:when>
			<xsl:when test="gml:featureMember[1]/aatams:tag_release_min">
					<xsl:text>Classification,Project,Tag,Release,,Family,Genus,Species,ID,Name,ID,Code Name,Longitude,Latitude,Date-time</xsl:text>
			</xsl:when>
		</xsl:choose>
		<xsl:text>&#10;</xsl:text>
		<xsl:choose>
			<xsl:when test="count(gml:featureMember) = 0">
				<xsl:value-of select="@numberOfFeatures" />
				<xsl:text>&#10;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="gml:featureMember[1]/aatams:receiver_deployment">
						<xsl:for-each select="gml:featureMember">
							<xsl:sort select="aatams:receiver_deployment/aatams:installation_ref/aatams:installation/aatams:name" />
							<xsl:sort select="aatams:receiver_deployment/aatams:station_ref/aatams:station/aatams:name" />
							<xsl:sort select="aatams:receiver_deployment/aatams:deployment_timestamp" />
							<xsl:apply-templates />
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="gml:featureMember[1]/aatams:device_model">
						<xsl:for-each select="gml:featureMember">
							<xsl:sort select="aatams:device_model/aatams:device_type_ref/aatams:device_type/aatams:name" />
							<xsl:sort select="aatams:device_model/aatams:name" />
							<xsl:apply-templates />
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:when>
					<xsl:when test="gml:featureMember[1]/*/aatams:name">
						<xsl:for-each select="gml:featureMember">
							<xsl:sort select="*/aatams:name" />
							<xsl:apply-templates />
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<xsl:for-each select="gml:featureMember">
							<xsl:apply-templates />
							<xsl:text>&#10;</xsl:text>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		</pre></body></html>
	</xsl:template>
	<xsl:template match="aatams:receiver_deployment">
		<xsl:value-of select="substring-after(@gml:id,'aatams.receiver_deployment.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:installation_ref/aatams:installation/@gml:id,'aatams.installation.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:installation_ref/aatams:installation/aatams:name" />
		<xsl:text>",</xsl:text>
		<xsl:value-of select="aatams:installation_ref/aatams:installation/aatams:installation_conf_ref/aatams:installation_conf/aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:station_ref/aatams:station/@gml:id,'aatams.station.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:station_ref/aatams:station/aatams:name" />
		<xsl:text>",</xsl:text>
		<xsl:value-of select="substring-after(aatams:project_person_ref/aatams:project_person/aatams:project_fid,'aatams.project.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:receiver_device_ref/aatams:receiver_device/@gml:id,'aatams.receiver_device.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:receiver_device_ref/aatams:receiver_device/aatams:code_name" />
		<xsl:text>",</xsl:text>
		<xsl:value-of select="substring-before(aatams:location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:deployment_timestamp" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:mooring_type_ref/aatams:mooring_type/aatams:name" />
		<xsl:text>",</xsl:text>
		<xsl:value-of select="aatams:bottom_depth" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:comments" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	<xsl:template match="aatams:tag_release">
		<xsl:value-of select="substring-after(@gml:id,'aatams.tag_release.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_name" />
		<xsl:text>",</xsl:text>
		<xsl:value-of select="substring-after(aatams:tag_device_ref/aatams:tag_device/@gml:id,'aatams.tag_device.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:tag_device_ref/aatams:tag_device/aatams:code_name" />
		<xsl:text>","</xsl:text>
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
		<xsl:text>",</xsl:text>
		<xsl:value-of select="substring-before(aatams:capture_location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:capture_location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:capture_timestamp" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-before(aatams:release_location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:release_location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:release_timestamp" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:implant_type_ref/aatams:implant_type/aatams:name" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	<xsl:template match="aatams:device">
		<xsl:value-of select="substring-after(@gml:id,'aatams.device.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:code_name" />
		<xsl:text>",</xsl:text>
		<xsl:value-of select="substring-after(aatams:device_model_ref/aatams:device_model/@gml:id,'aatams.device_model.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:name" />
		<xsl:text>","</xsl:text>
		<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:device_manufacturer_ref/aatams:device_manufacturer/aatams:name" />
		<xsl:text>","</xsl:text>
		<xsl:value-of select="aatams:device_model_ref/aatams:device_model/aatams:device_type_ref/aatams:device_type/aatams:name" />
		<xsl:text>",</xsl:text>
		<xsl:value-of select="substring-after(aatams:project_person_ref/aatams:project_person/aatams:project_fid,'aatams.project.')" />
		<xsl:text>,"</xsl:text>
		<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_name" />
		<xsl:text>","</xsl:text>
		<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:person_role" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	<xsl:template match="aatams:tag_device">
		<xsl:value-of select="substring-after(@gml:id,'aatams.tag_device.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:code_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:model_name" />
	</xsl:template>
	<xsl:template match="aatams:receiver_device">
		<xsl:value-of select="substring-after(@gml:id,'aatams.receiver_device.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:code_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:model_name" />
		<xsl:text>,</xsl:text>
	</xsl:template>
	<xsl:template match="aatams:device_model">
		<xsl:value-of select="substring-after(@gml:id,'aatams.device_model.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:device_type_ref/aatams:device_type/aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:device_manufacturer_ref/aatams:device_manufacturer/aatams:name" />
	</xsl:template>
	<xsl:template match="aatams:detection">
		<xsl:value-of select="substring-after(@gml:id,'aatams.detection.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:installation_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:station_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:tag_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:tag_code_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:family_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:genus_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:species_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:deployment_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:receiver_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:receiver_code_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-before(aatams:location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:detection_timestamp" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-before(aatams:release_location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:release_location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:release_timestamp" />
	</xsl:template>
	<xsl:template match="aatams:detections_by_installation_station">
		<xsl:value-of select="aatams:installation" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:installation_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:station" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:station_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-before(aatams:location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:location/gml:Point/gml:pos,' ')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:count" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:tag_id" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:tag_code_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:detections_by_station_tag_ref[1]/aatams:detections_by_station_tag/aatams:count" />
		<xsl:text>,</xsl:text>
		<xsl:for-each select="aatams:detections_by_station_tag_ref[position()&gt;1]">
			<xsl:text>[</xsl:text>
			<xsl:value-of select="aatams:detections_by_station_tag/aatams:tag_id" />
			<xsl:text>|</xsl:text>
			<xsl:value-of select="aatams:detections_by_station_tag/aatams:tag_code_name" />
			<xsl:text>|</xsl:text>
			<xsl:value-of select="aatams:detections_by_station_tag/aatams:count" />
			<xsl:text>]</xsl:text>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="aatams:detections_by_project_tag">
		<xsl:value-of select="aatams:project" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:project_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:tag" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:tag_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:count" />
	</xsl:template>
	<xsl:template match="aatams:detections_by_classification_tag">
		<xsl:value-of select="aatams:family" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:genus" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:species" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:tag" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:tag_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:count" />
	</xsl:template>
	<xsl:template match="aatams:installation">
		<xsl:value-of select="substring-after(@gml:id,'aatams.installation.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:installation_conf_ref/aatams:installation_conf/aatams:name" />
	</xsl:template>
	<xsl:template match="aatams:project">
		<xsl:value-of select="substring-after(@gml:id,'aatams.project.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:organisation_ref/aatams:organisation/@gml:id,'aatams.organisation.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:organisation_ref/aatams:organisation/aatams:name" />
	</xsl:template>
	<xsl:template match="aatams:person">
		<xsl:value-of select="substring-after(@gml:id,'aatams.person.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:personal_mobile_number" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_phone_number" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:email" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_physical_address_1" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_physical_address_2" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_physical_address_state" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_physical_address_country" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_physical_address_postcode" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_postal_address_1" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_postal_address_2" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_postal_address_state" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_postal_address_country" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:home_postal_address_postcode" />
	</xsl:template>
	<xsl:template match="aatams:organisation">
		<xsl:value-of select="substring-after(@gml:id,'aatams.organisation.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:phone_number" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:fax_number" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:physical_address_1" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:physical_address_2" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:physical_address_state" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:physical_address_country" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:physical_address_postcode" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:postal_address_1" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:postal_address_2" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:postal_address_state" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:postal_address_country" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:postal_address_postcode" />
	</xsl:template>
	<xsl:template match="aatams:tag_release_min">
		<xsl:value-of select="aatams:family" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:genus" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:species" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:project" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:project_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:tag" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:code_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:release_longitude" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:release_latitude" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:release_timestamp" />
	</xsl:template>
	<xsl:template match="aatams:classification">
		<xsl:value-of select="substring-after(@gml:id,'aatams.classification.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:classification_level_ref/aatams:classification_level/aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:common_name" />
	</xsl:template>
	<xsl:template match="aatams:project_person">
		<xsl:value-of select="substring-after(aatams:project_fid,'aatams.project.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:project_name" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="substring-after(aatams:person_fid,'aatams.person.')" />
		<xsl:text>,</xsl:text>
		<xsl:value-of select="aatams:person_role" />
	</xsl:template>
	<xsl:template match="node()">
	</xsl:template>
</xsl:stylesheet>