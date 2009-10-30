<?xml version="1.0" encoding="utf-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />
	<xsl:template match="/database">
		<xsl:for-each select="table">
			<xsl:text>&#13;</xsl:text>
			<xsl:text>CREATE SEQUENCE AATAMS.</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>_SERIAL;&#13;</xsl:text>
			<xsl:text>CREATE TABLE AATAMS.</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>(&#13;</xsl:text>
			<xsl:apply-templates select="column" />
			<xsl:call-template name="default-fields" />
			<xsl:text>);&#13;</xsl:text>
		</xsl:for-each>
		<xsl:apply-templates select="table/foreign-key" />
	</xsl:template>
	<xsl:template match="column">
		<xsl:variable name="name">
			<xsl:value-of select="@name" />
		</xsl:variable>
		<xsl:value-of select="$name" />
		<xsl:text> </xsl:text>
		<xsl:choose>
			<xsl:when
				test="@type = 'VARCHAR' or @type = 'LONGVARCHAR'">
				<xsl:text>VARCHAR2(</xsl:text>
				<xsl:value-of select="@size" />
				<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:when test="@type = 'DECIMAL'">
				<xsl:text>NUMBER(</xsl:text>
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
		<xsl:text>,
</xsl:text>
	</xsl:template>
	<xsl:template match="foreign-key">
		<xsl:text></xsl:text>
		<xsl:text>&#13;ALTER TABLE AATAMS.</xsl:text>
		<xsl:value-of select="../@name" />
		<xsl:text>&#13;</xsl:text>
		<xsl:text>ADD CONSTRAINT </xsl:text>
		<xsl:value-of select="@name" />
		<xsl:text>&#13;</xsl:text>
		<xsl:text>FOREIGN KEY(</xsl:text>
		<xsl:value-of select="reference/@local" />
		<xsl:text>)&#13;</xsl:text>
		<xsl:text>REFERENCES AATAMS.</xsl:text>
		<xsl:value-of select="@foreignTable" />
		<xsl:text>(</xsl:text>
		<xsl:value-of select="reference/@foreign" />
		<xsl:text>);&#13;</xsl:text>
	</xsl:template>
	<xsl:template name="default-fields">
		<xsl:text>DISABLED CHAR(1) DEFAULT 'N' NOT NULL CHECK(DISABLED IN('Y','N')),&#13;</xsl:text>
		<xsl:text>CREATED TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL,&#13;</xsl:text>
		<xsl:text>MODIFIED TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL&#13;</xsl:text>
	</xsl:template>
</xsl:stylesheet>