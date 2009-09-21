<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:redirect="http://xml.apache.org/xalan/redirect" xmlns:xalan="http://xml.apache.org/xslt" xmlns:aatams="http://www.imos.org.au/aatams" extension-element-prefixes="redirect">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="4" />
	<xsl:template match="/">
		<xsl:for-each select="//table">
			<xsl:variable name="table-name" select="@name" />
			<redirect:write select="concat('xforms/',$table-name,'.xml')">
				<xsl:processing-instruction name="xml-stylesheet">
					<xsl:text>href="xsltforms-beta/xsltforms/xsltforms.xsl" type="text/xsl"</xsl:text>
				</xsl:processing-instruction>
				<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:aatams="http://www.imos.org.au/aatams">
					<head>
						<title>AATAMS Web Interface</title>
						<xf:model>
							<xf:instance>
								<wfs:Transaction version="1.1.0" service="WFS" xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc">
									<wfs:Insert>
										<xsl:call-template name="model" />
									</wfs:Insert>
								</wfs:Transaction>
							</xf:instance>
						</xf:model>
					</head>
					<body>
						<div id="xformControl">
							<span>
								<input type="checkbox" onclick="$('console').style.display = this.checked? 'block' : 'none';" checked="checked" /> Debug
							</span>
						</div>
						<xsl:call-template name="form" />
						<br />
						<div id="console">
						</div>
					</body>
				</html>
			</redirect:write>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="model">
		<xsl:variable name="feature-name">
			<xsl:call-template name="convertpropercase">
				<xsl:with-param name="toconvert" select="@name" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="aatams:{$feature-name}">
			<xsl:for-each select="column">
				<xsl:variable name="property-name">
					<xsl:call-template name="convertcase">
						<xsl:with-param name="conversion">lower</xsl:with-param>
						<xsl:with-param name="toconvert" select="@name" />
					</xsl:call-template>
				</xsl:variable>
				<xsl:element name="aatams:{$property-name}" />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<xsl:template name="form">
		<xsl:for-each select="column">
			<xsl:choose>
				<xsl:when test="fn:ends-with(@name,'ID')">
					<xsl:element name="xf:input">
						<xsl:attribute name="ref">
							<xsl:text>aatams:</xsl:text>
							<xsl:call-template name="convertcase">
								<xsl:with-param name="conversion">lower</xsl:with-param>
								<xsl:with-param name="toconvert" select="@name" />
							</xsl:call-template>
                        </xsl:attribute>
						<label>
							<xsl:call-template name="convertcase">
								<xsl:with-param name="conversion">proper</xsl:with-param>
								<xsl:with-param name="toconvert" select="@name" />
							</xsl:call-template>
                        </label>
                    </xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<input ref="sample:name">
						<label>Name</label>
					</input>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="column">
		<xsl:call-template name="convertpropercase">
			<xsl:with-param name="toconvert" select="translate(@name,&quot;_&quot;,&quot; &quot;)" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="child">
		<xsl:param name="child-table" />
		<li style="display:inline;margin:10px;">
			<xsl:value-of select="$child-table/@name" />
		</li>
	</xsl:template>
	<xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
	<xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
	<xsl:template name="convertcase">
		<xsl:param name="toconvert" />
		<xsl:param name="conversion" />
		<xsl:choose>
			<xsl:when test="$conversion=&quot;lower&quot;">
				<xsl:value-of select="translate($toconvert,$ucletters,$lcletters)" />
			</xsl:when>
			<xsl:when test="$conversion=&quot;upper&quot;">
				<xsl:value-of select="translate($toconvert,$lcletters,$ucletters)" />
			</xsl:when>
			<xsl:when test="$conversion=&quot;proper&quot;">
				<xsl:call-template name="convertpropercase">
					<xsl:with-param name="toconvert" select="translate($toconvert,$ucletters,$lcletters)" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$toconvert" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="convertpropercase">
		<xsl:param name="toconvert" />
		<xsl:if test="string-length($toconvert) &gt; 0">
			<xsl:variable name="f" select="substring($toconvert, 1, 1)" />
			<xsl:variable name="s" select="substring($toconvert, 2)" />
			<xsl:call-template name="convertcase">
				<xsl:with-param name="toconvert" select="$f" />
				<xsl:with-param name="conversion">upper</xsl:with-param>
			</xsl:call-template>
			<xsl:choose>
				<xsl:when test="contains($s,' ')">
					<xsl:call-template name="convertcase">
						<xsl:with-param name="toconvert" select="substring-before($s,&quot; &quot;)" />
						<xsl:with-param name="conversion">lower</xsl:with-param>
					</xsl:call-template>
					<xsl:text>
					</xsl:text>
					<xsl:call-template name="convertpropercase">
						<xsl:with-param name="toconvert" select="substring-after($s,&quot; &quot;)" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="convertcase">
						<xsl:with-param name="toconvert" select="$s" />
						<xsl:with-param name="conversion">lower</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>