<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:aatams="http://www.imos.org.au/aatams" 
xmlns:xf="http://www.w3.org/2002/xforms"
xmlns:ev="http://www.w3.org/2001/xml-events"
>
	<xsl:output name="xml" method="xml" encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
		<xsl:for-each select="//table">
			<xsl:result-document href="{concat('file:///C:/tomcat-5.5.25/apache-tomcat-5.5.25/webapps/aatams/create_',lower-case(@name))}" format="xml">
				<xsl:processing-instruction name="xml-stylesheet">
					<xsl:text>href="xsltforms-beta/xsltforms/xsltforms.xsl" type="text/xsl"</xsl:text>
				</xsl:processing-instruction>
				<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:aatams="http://www.imos.org.au/aatams">
					<head>
						<title>AATAMS Web Interface</title>
						<link href="aatams.css" rel="stylesheet" type="text/css"/>
						<xf:model>
							<xf:instance>
								<wfs:Transaction version="1.1.0" service="WFS" xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc">
									<wfs:Insert>
										<wfs:FeatureCollection>
											<gml:featureMember>
												<xsl:call-template name="model" />
											</gml:featureMember>
                                        </wfs:FeatureCollection>
									</wfs:Insert>
								</wfs:Transaction>
							</xf:instance>
							<xf:submission id="s01" method="post" action="../deegree-wfs/services">
								<xf:message level="modeless" ev:event="xforms-submit-error">Submit error.</xf:message>
							</xf:submission>
						</xf:model>
						<!-- add an external model file for each code list -->
						<xsl:for-each select="foreign-key">
							<xf:model>
								<xsl:attribute name="id">
									<xsl:value-of select="lower-case(@foreignTable)"/>
								</xsl:attribute>
								<xf:instance>
									<xsl:attribute name="src">
										<xsl:text>../deegree-wfs/services?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:</xsl:text><xsl:value-of select="lower-case(@foreignTable)"/>
									</xsl:attribute>
								</xf:instance>
                            </xf:model>
                        </xsl:for-each>
					</head>
					<body>
						<div id="xformControl">
							<span>
								<input type="checkbox" onclick="$('console').style.display = this.checked? 'block' : 'none';" checked="checked" /><xsl:text>Debug</xsl:text>
							</span>
						</div>
						<xsl:call-template name="form"/>
						<br/>
						<div id="console"/>
					</body>
				</html>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="model">
		<xsl:element name="aatams:{lower-case(@name)}">
			<xsl:for-each select="column">
				<xsl:element name="aatams:{lower-case(@name)}" />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<xsl:template name="form">
		<!--xsl:variable name="table-name">
				<xsl:call-template name="proper-case">
					<xsl:with-param name="toconvert" select="@name" />
				</xsl:call-template>	
        </xsl:variable-->
		<xf:group>
			<xsl:attribute name="ref">
				<xsl:text>//aatams:</xsl:text><xsl:value-of select="lower-case(@name)"/>
            </xsl:attribute>
			<fieldset>
            <legend><xsl:value-of select="translate(upper-case(@name),'_',' ')"/></legend>
			<xsl:for-each select="column">
				<xsl:variable name="column-name"></xsl:variable>
				<xsl:choose>
					<xsl:when test="@primaryKey = 'true'">
						<!-- export status from Access not needed -->
					</xsl:when>
					<xsl:when test="@name = 'STATUS_ID'">
						<!-- export status from Access not needed -->
					</xsl:when>
					<xsl:when test="../foreign-key[reference/@local=current()/@name]">
						<xsl:call-template name="select1-from-foreign-key">
							<xsl:with-param name="fk_node" select="../foreign-key[reference/@local=current()/@name]"/>
                        </xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="xf:input">
							<xsl:attribute name="ref">
								<xsl:text>aatams:</xsl:text><xsl:value-of select="lower-case(@name)"/>
							</xsl:attribute>
							<xf:label>
								<xsl:call-template name="proper-case">
									<xsl:with-param name="toconvert" select="translate(@name,'_',' ')" />
								</xsl:call-template>
							</xf:label>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			</fieldset>
		</xf:group>
		<xf:submit submission="s01">
			<xf:label>Save</xf:label>
			</xf:submit>
		<xf:trigger>
			<xf:label>Reset</xf:label>
			<xf:reset ev:event="DOMActivate" />
		</xf:trigger>
	</xsl:template>
	<xsl:template match="column">
		<xsl:call-template name="proper-case">
			<xsl:with-param name="toconvert" select="translate(@name,&quot;_&quot;,&quot; &quot;)" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="proper-case">
		<xsl:param name="toconvert" />
		<xsl:if test="string-length($toconvert) &gt; 0">
			<xsl:variable name="f" select="substring($toconvert, 1, 1)" />
			<xsl:variable name="s" select="substring($toconvert, 2)" />
			<xsl:value-of select="upper-case($f)"/>
			<xsl:choose>
				<xsl:when test="contains($s,' ')">
					<xsl:value-of select="lower-case(substring-before($s,&quot; &quot;))" />
					<xsl:text> </xsl:text>
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
	<xsl:template name="select1-from-foreign-key">
		<xsl:param name="fk_node" />
		<xsl:element name="xf:select1">
			<xsl:attribute name="ref">
				<xsl:text>aatams:</xsl:text><xsl:value-of select="lower-case($fk_node[1]/reference/@local)"/>
			</xsl:attribute>
			<xsl:attribute name="appearance">minimal</xsl:attribute>
			<xsl:attribute name="incremental">true</xsl:attribute>
			<xf:label>
				<xsl:call-template name="proper-case">
					<xsl:with-param name="toconvert" select="translate(substring-before($fk_node[1]/reference/@local,'_ID'),'_',' ')"/>
				</xsl:call-template>
			</xf:label>
			<xsl:element name="xf:itemset">
				<xsl:attribute name="model">
					<xsl:value-of select="lower-case($fk_node[1]/@foreignTable)"/>
				</xsl:attribute>
				<xsl:attribute name="nodeset">
					<xsl:text>//aatams:</xsl:text><xsl:value-of select="lower-case($fk_node[1]/@foreignTable)"/>
				</xsl:attribute>
				<xsl:element name="xf:label">
					<xsl:attribute name="ref">
						<xsl:text>aatams:name</xsl:text>
					</xsl:attribute>
				</xsl:element>
				<xsl:element name="xf:value">
					<xsl:attribute name="ref">
						<xsl:text>aatams:</xsl:text><xsl:value-of select="lower-case($fk_node[1]/reference/@foreign)"/>
					</xsl:attribute>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	
</xsl:stylesheet>