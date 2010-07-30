<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml"
>
    <xsl:output method="text" encoding="UTF-8" />
    <xsl:template match="/">
			<xsl:for-each select="//table">
                <xsl:value-of select="concat('saxon view_all_',lower-case(@name),'.xml ../xsltforms/xsltforms.xsl view_all_',lower-case(@name),'.html')"/>
                <xsl:text>
</xsl:text>
			</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
