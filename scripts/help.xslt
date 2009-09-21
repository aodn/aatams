<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:aatams="http://www.imos.org.au/aatams" 
xmlns:xf="http://www.w3.org/2002/xforms"
xmlns:ev="http://www.w3.org/2001/xml-events"
>

<xsl:template name="help">
	<xsl:param name="key"/>
	<xsl:variable name="message">
		<xsl:choose>
			<xsl:when test="$key='help-message'">help message</xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:if test="$message != ''">
		<xf:help><xf:value-of select="$message"/></xf:help>
	</xsl:if>
</xsl:template>
</xsl:stylesheet>
