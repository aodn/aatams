<?xml version="1.0" encoding="utf-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:template match="/database">
		<xsl:apply-templates select="table" />
		<xsl:apply-templates select="table/foreign-key" />
	</xsl:template>
	<xsl:template match="table">
		<xsl:text>&#13;</xsl:text>
		<xsl:text>CREATE SEQUENCE aatams.</xsl:text>
		<xsl:value-of select="lower-case(@name)" />
		<xsl:text>_serial;&#13;</xsl:text>
		<xsl:text>CREATE TABLE aatams.</xsl:text>
		<xsl:value-of select="lower-case(@name)" />
		<xsl:text>(&#13;</xsl:text>
		<xsl:apply-templates select="column" />
		<xsl:call-template name="default-fields" />
		<xsl:text>);&#13;</xsl:text>
		<xsl:for-each select="column[@type='GEOMETRY']">
			<xsl:text>select addgeometrycolumn('aatams', '</xsl:text>
			<xsl:value-of select="lower-case(../@name)"/>
			<xsl:text>', '</xsl:text>
			<xsl:value-of select="lower-case(@name)"/>
			<xsl:text>', 4326, 'POINT', 2);&#13;</xsl:text>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="column">
		<xsl:variable name="name">
			<xsl:value-of select="lower-case(@name)" />
		</xsl:variable>
		<xsl:value-of select="$name" />
		<xsl:text> </xsl:text>
		<xsl:choose>
			<xsl:when
				test="@type = 'VARCHAR' or @type = 'LONGVARCHAR'">
				<xsl:text>VARCHAR(</xsl:text>
				<xsl:value-of select="@size" />
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:when test="@type = 'DECIMAL'">
				<xsl:text>NUMERIC(</xsl:text>
				<xsl:value-of select="@size" />
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@type" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="@primaryKey = 'true'">
			<xsl:text> PRIMARY KEY</xsl:text>
		</xsl:if>
		<xsl:if test="@required = 'true'">
			<xsl:text> NOT NULL</xsl:text>
		</xsl:if>
		<xsl:text>,&#013;</xsl:text>
	</xsl:template>
	<xsl:template match="column[@type='GEOMETRY']">
		<!-- SEE NAMED GEOMETRY TEMPLATE - DON'T NEED COLUMN IN CREATE TABLE -->
	</xsl:template>
	<xsl:template match="foreign-key">
		<xsl:text></xsl:text>
		<xsl:text>&#13;ALTER TABLE aatams.</xsl:text>
		<xsl:value-of select="lower-case(../@name)" />
		<xsl:text>&#13;</xsl:text>
		<xsl:text>ADD CONSTRAINT </xsl:text>
		<xsl:value-of select="lower-case(@name)" />
		<xsl:text>&#13;</xsl:text>
		<xsl:text>FOREIGN KEY(</xsl:text>
		<xsl:value-of select="lower-case(reference/@local)" />
		<xsl:text>)&#13;</xsl:text>
		<xsl:text>REFERENCES aatams.</xsl:text>
		<xsl:value-of select="lower-case(@foreignTable)" />
		<xsl:text>(</xsl:text>
		<xsl:value-of select="lower-case(reference/@foreign)" />
		<xsl:text>);&#13;</xsl:text>
	</xsl:template>
	<xsl:template name="default-fields">
		<xsl:text>disabled CHAR(1) DEFAULT 'N' NOT NULL CHECK(disabled IN('Y','N')),&#13;</xsl:text>
		<xsl:text>created TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL,&#13;</xsl:text>
		<xsl:text>modified TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL&#13;</xsl:text>
	</xsl:template>
</xsl:stylesheet>