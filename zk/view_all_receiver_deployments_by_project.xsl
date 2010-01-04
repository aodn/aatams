<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:gml="http://www.opengis.net/gml"  xmlns:wfs="http://www.opengis.net/wfs" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan" xmlns="http://www.w3.org/1999/xhtml">

	<xsl:key name="projects" match="/wfs:FeatureCollection/gml:featureMember/aatams:receiver_deployment" 
		use="aatams:project_person_ref/aatams:project_person/aatams:project_name"/>

	<xsl:template match="wfs:FeatureCollection">
		<table>
			<thead>
				<tr>
					<th colspan="2" rowspan="1">Project</th>
					<th colspan="1" rowspan="2">Deployment ID</th>
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
					<th colspan="1" rowspan="1">ID</th>
					<th colspan="1" rowspan="1">Code Name</th>
					<th colspan="1" rowspan="1">Longitude</th>
					<th colspan="1" rowspan="1">Latitude</th>
				</tr>
			</thead>
			<tbody>
				<!-- group and sort by 'Muenchian Method' (see: http://www.jenitennison.com/xslt/grouping/muenchian.html) -->
				<xsl:for-each select="gml:featureMember/aatams:receiver_deployment[count(. | key('projects', aatams:project_person_ref/aatams:project_person/aatams:project_name)[1]) = 1]">
					<xsl:sort select="aatams:project_person_ref/aatams:project_person/aatams:project_name"/>
					
					<xsl:for-each select="key('projects', aatams:project_person_ref/aatams:project_person/aatams:project_name)">
						<xsl:sort select="aatams:deployment_timestamp" />
						<tr>
							<xsl:if test="position() = 1">
								<xsl:variable name="rowspan">
									<xsl:value-of select="count(key('projects', aatams:project_person_ref/aatams:project_person/aatams:project_name))"/>	
								</xsl:variable>
								<td>
									<xsl:attribute name="rowspan"><xsl:value-of select="$rowspan"/></xsl:attribute>
									<xsl:value-of select="substring-after(aatams:project_person_ref/aatams:project_person/aatams:project_fid,'aatams.project.')"/>
								</td>
								<td>
									<xsl:attribute name="rowspan"><xsl:value-of select="$rowspan"/></xsl:attribute>
									<xsl:value-of select="aatams:project_person_ref/aatams:project_person/aatams:project_name"/>
								</td>
							</xsl:if>
							<td>
								<xsl:value-of select="substring-after(@gml:id,'aatams.receiver_deployment.')" />
							</td>
							<td>
								<xsl:value-of select="substring-after(aatams:receiver_device_ref/aatams:receiver_device/@gml:id,'aatams.receiver_device.')" />
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
					</xsl:for-each>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
</xsl:stylesheet>

