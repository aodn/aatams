<?xml version="1.0" encoding="utf-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:template match="/database/table">
		<xsl:value-of select="@name" />
		<xsl:text>&#13;</xsl:text>
		<xsl:for-each select="column">
			<xsl:value-of select="lower-case(@name)"/>
			<xsl:if test="position() != last()">
				<xsl:text>, </xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>&#13;</xsl:text>
		<xsl:for-each select="column">
			<xsl:text>?</xsl:text>
			<xsl:if test="position() != last()">
				<xsl:text>,</xsl:text>
			</xsl:if>
		</xsl:for-each>
		<xsl:text>&#13;</xsl:text>
		<xsl:for-each select="column">
			<xsl:variable name="type">
				<xsl:choose>
					<xsl:when test="@type='INTEGER'">Int</xsl:when>
					<xsl:when test="@type='VARCHAR'">String</xsl:when>
					<xsl:when test="@type='TIMESTAMP'">Timestamp</xsl:when>
				</xsl:choose>
			</xsl:variable>
			<xsl:text>psIn.set</xsl:text>
			<xsl:value-of select="$type"/>
			<xsl:text>(</xsl:text>
			<xsl:value-of select="position()"/>
			<xsl:text>,rsOut.get</xsl:text>
			<xsl:value-of select="$type"/>
			<xsl:text>(</xsl:text>
			<xsl:value-of select="position()"/>
			<xsl:text>));&#13;</xsl:text>
		</xsl:for-each>
		<xsl:text>&#13;&#13;</xsl:text>
	</xsl:template>
</xsl:stylesheet>