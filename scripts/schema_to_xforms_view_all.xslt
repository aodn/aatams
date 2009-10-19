<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:ev="http://www.w3.org/2001/xml-events">
	<xsl:output name="xml" method="xml" encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
		<xsl:for-each select="//table">
			<xsl:variable name="typename"><xsl:value-of select="lower-case(@name)"/></xsl:variable>
			<xsl:result-document href="{concat('file:///C:/eclipse_workspace/aatams/forms/view_all_',$typename,'.xml')}" format="xml">
				<xsl:processing-instruction name="xml-stylesheet">
					<xsl:text>href="xsltforms/xsltforms.xsl" type="text/xsl"</xsl:text>
				</xsl:processing-instruction>
				<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:aatams="http://www.imos.org.au/aatams">
					<head>
						<title>AATAMS Web Interface</title>
						   	<style type="text/css">
	      						<xsl:text>ul.xforms-repeat {display:table; border-collapse:collapse;}</xsl:text>
	      						<xsl:text>li.xforms-repeat-item {display:table-row;}</xsl:text>
	      						<xsl:text>span.xforms-value {display:table-cell; padding:3px; border:solid 1px gray;}</xsl:text>
      						</style>
						<xf:model>
							<xf:instance id="inst_features">
								<xsl:attribute name="src">
									<xsl:text>../deegree-wfs/services?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:</xsl:text>
									<xsl:value-of select="lower-case(@name)" />
								</xsl:attribute>
								<wfs:FeatureCollection xmlns:gml="http://www.opengis.net/gml" xmlns:wfs="http://www.opengis.net/wfs"  xmlns:aatams="http://www.imos.org.au/aatams" >
									<gml:featureMember>
										<xsl:element name="aatams:{lower-case(@name)}"/>
									</gml:featureMember>
								</wfs:FeatureCollection>
							</xf:instance>
						</xf:model>
					</head>
					<body>
						<!--<div id="xformControl">
							<span>
								<input type="checkbox" onclick="$('console').style.display = this.checked? 'block' : 'none';" checked="checked" />
								<xsl:text>Debug</xsl:text>
							</span>
						</div>-->
						<xsl:call-template name="form" />
						<br />
						<div id="console" style="display:none;"/>
					</body>
				</html>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="form">
		<fieldset>
			<legend>
				<xsl:text>VIEW ALL </xsl:text><xsl:value-of select="translate(upper-case(@name),'_',' ')" />
			</legend>
			<xf:repeat >
				<xsl:attribute name="nodeset">
					<xsl:text>instance('inst_features')//aatams:</xsl:text>
					<xsl:value-of select="lower-case(@name)" />
				</xsl:attribute>
				<xf:output ref="@gml:id"/>
			<xsl:for-each select="column">
				<xsl:choose>
					<xsl:when test="@primaryKey = 'true'">
						<!-- using gml:id as identifier -->
					</xsl:when>
					<xsl:when test="@name = 'STATUS_ID'">
						<!-- export status from Access not needed -->
					</xsl:when>
					<xsl:when test="@name = 'PROJECT_ROLE_PERSON_ID'">
						<!-- remapped to PROJECT_PERSON view -->
						<xf:output ref="//aatams:project_person/aatams:project_name">
						</xf:output>
						<xf:output ref="//aatams:project_person/aatams:person_role">
						</xf:output>
					</xsl:when>
					<xsl:when test="../foreign-key[reference/@local=current()/@name]">
						<xsl:variable name="foreignTable">
							<xsl:value-of select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
						</xsl:variable>
						<xsl:element name="xf:output">
							<xsl:attribute name="ref">
								<xsl:value-of select="concat('//aatams:',lower-case($foreignTable),'/aatams:name')" />
							</xsl:attribute>
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="xf:output">
							<xsl:attribute name="ref">
								<xsl:value-of select="concat('aatams:',lower-case(replace(@name,'_ID','')))" />
							</xsl:attribute>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		    </xf:repeat>
		</fieldset>
	</xsl:template>
	<xsl:template name="proper-case">
		<xsl:param name="toconvert" />
		<xsl:if test="string-length($toconvert) &gt; 0">
			<xsl:variable name="f" select="substring($toconvert, 1, 1)" />
			<xsl:variable name="s" select="substring($toconvert, 2)" />
			<xsl:value-of select="upper-case($f)" />
			<xsl:choose>
				<xsl:when test="contains($s,' ')">
					<xsl:value-of select="lower-case(substring-before($s,&quot; &quot;))" />
					<xsl:text>&#x20;</xsl:text>
					<xsl:call-template name="proper-case">
						<xsl:with-param name="toconvert" select="substring-after($s,&quot; &quot;)" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="lower-case($s)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
