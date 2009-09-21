<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml"
>
	<xsl:output name="xml" method="xml" encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
<html>
   <body style="font-weight:bold; font-family:Arial, Helvetica, sans-serif;">
		<table style="padding:5px;">
			<tbody>
			<xsl:for-each select="//table">
				<tr>
					<td><xsl:value-of select="upper-case(replace(@name,'_',' '))" /></td>
					<td>
						<a>
							<xsl:attribute name="href">
								<xsl:text>create_</xsl:text><xsl:value-of select="lower-case(@name)"/><xsl:text>.xml</xsl:text>
							</xsl:attribute>
							<xsl:text>Create</xsl:text>
						</a>
					</td>
					<td>
						<a>
							<xsl:attribute name="href">
								<xsl:text>view_</xsl:text><xsl:value-of select="lower-case(@name)"/><xsl:text>.xml</xsl:text>
							</xsl:attribute>
							<xsl:text>View</xsl:text>

						</a>
					</td>
					<td>
						<a>
							<xsl:attribute name="href">
								<xsl:text>view_all_</xsl:text><xsl:value-of select="lower-case(@name)"/><xsl:text>.xml</xsl:text>
							</xsl:attribute>
							<xsl:text>View All</xsl:text>

						</a>
					</td>
				</tr>
			</xsl:for-each>
	</tbody>
	</table>
	</body>
</html>
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
</xsl:stylesheet>
