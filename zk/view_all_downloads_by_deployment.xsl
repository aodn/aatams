<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:gml="http://www.opengis.net/gml"  xmlns:wfs="http://www.opengis.net/wfs" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan" xmlns="http://www.w3.org/1999/xhtml">

	<xsl:key name="downloads_by_receiver" match="/wfs:FeatureCollection/gml:featureMember/aatams:deployment_download" 
		use="aatams:installation_deployment_ref/aatams:installation_deployment/aatams:device_name"/>

	<xsl:template match="wfs:FeatureCollection">
		<table>
			<thead>
				<tr>
					<th colspan="2" rowspan="1">Receiver</th>
					<th colspan="5" rowspan="1">Deployment</th>
					<th colspan="2" rowspan="1">Data Download</th>
					<th colspan="1" rowspan="1">Detections</th>
				</tr>
				<tr>
					<th colspan="1" rowspan="1">ID</th>
					<th colspan="1" rowspan="1">Code Name</th>
					<th colspan="1" rowspan="1">ID</th>
					<th colspan="1" rowspan="1">Inst. ID</th>
					<th colspan="1" rowspan="1">UTC Date-Time</th>
					<th colspan="1" rowspan="1">Longitude</th>
					<th colspan="1" rowspan="1">Latitude</th>
					<th colspan="1" rowspan="1">ID</th>
					<th colspan="1" rowspan="1">UTC Date Time</th>
					<th colspan="1" rowspan="1">(click to show tags)</th>
				</tr>
			</thead>
			<tbody>
				<!-- group by 'Muenchian Method' (see: http://www.jenitennison.com/xslt/grouping/muenchian.html) -->
				<xsl:for-each select="gml:featureMember/aatams:deployment_download[count(. | key('downloads_by_receiver',aatams:installation_deployment_ref/aatams:installation_deployment/aatams:device_name)[1]) = 1]">
					<xsl:sort select="aatams:installation_deployment_ref/aatams:installation_deployment/aatams:device_name"/>
					<xsl:for-each select="key('downloads_by_receiver',aatams:installation_deployment_ref/aatams:installation_deployment/aatams:device_name)">
						<xsl:sort select="aatams:installation_deployment_ref/aatams:installation_deployment/aatams:deployment_timestamp"/>
						<xsl:sort select="aatams:download_timestamp"/>
						<tr>
							<xsl:if test="position() = 1">
								<xsl:variable name="rowspan">
									<xsl:value-of select="count(key('downloads_by_receiver',aatams:installation_deployment_ref/aatams:installation_deployment/aatams:device_name))"/>
								</xsl:variable>
								<td>
									<xsl:attribute name="rowspan"><xsl:value-of select="$rowspan"/></xsl:attribute>
									<xsl:value-of select="substring-after(aatams:installation_deployment_ref/aatams:installation_deployment/aatams:device_fid,'aatams.device.')"/>
								</td>
								<td>
									<xsl:attribute name="rowspan"><xsl:value-of select="$rowspan"/></xsl:attribute>
									<xsl:value-of select="aatams:installation_deployment_ref/aatams:installation_deployment/aatams:device_name"/>
								</td>
							</xsl:if>
							<td>
								<xsl:value-of select="substring-after(aatams:installation_deployment_ref/aatams:installation_deployment/aatams:deployment_fid,'aatams.receiver_deployment.')" />
							</td>
							<td>
								<xsl:value-of select="substring-after(aatams:installation_deployment_ref/aatams:installation_deployment/aatams:installation_fid,'aatams.installation.')" />
							</td>
							<td>
								<xsl:value-of select="aatams:installation_deployment_ref/aatams:installation_deployment/aatams:deployment_timestamp" />
							</td>
							<td>
                                <xsl:value-of select="substring-before(aatams:installation_deployment_ref/aatams:installation_deployment/aatams:location/gml:Point/gml:pos,' ')" />
							</td>
							<td>
                                <xsl:value-of select="substring-after(aatams:installation_deployment_ref/aatams:installation_deployment/aatams:location/gml:Point/gml:pos,' ')" />
							</td>
							<td>
								<xsl:value-of select="substring-after(@gml:id,'aatams.deployment_download.')" />
							</td>
							<td>
								<xsl:value-of select="aatams:download_timestamp" />
							</td>
							<td>	
								<xsl:if test="count(aatams:deployment_download_tag_ref)>0">
									<div style="width:160px;text-align:left;">
										<input type="button" onclick="toggle_tags(this.id);" style="display:block;position:relative;left:40px;width:80px;border:solid 2px outset;background-color:#CCCCCC;cursor:pointer;font-family: Verdana, sans-serif, Arial;font-weight: normal;font-size: 11px;">
											<xsl:attribute name="id"><xsl:value-of select="@gml:id"/></xsl:attribute>
											<xsl:attribute name="value"><xsl:value-of select="aatams:detections"/></xsl:attribute>
										</input>
									</div>
									<div style="display:none;text-align:left;line-height:100%;margin:5px 0px;">
										<xsl:attribute name="id"><xsl:value-of select="@gml:id"/>_tags</xsl:attribute>
										<xsl:for-each select="aatams:deployment_download_tag_ref/aatams:deployment_download_tag">
											<xsl:sort select="aatams:detection_count" order="descending" data-type="number"/>
											<xsl:value-of select="aatams:code_name"/> (<xsl:value-of select="aatams:detection_count"/>)<br/>
										</xsl:for-each>
									</div>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
</xsl:stylesheet>

