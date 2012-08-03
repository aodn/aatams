<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                >
	<xsl:template match="/">
	<!--  INSERT INTO VS_detection VALUES (20120613123356000,1,1,1);
	2012-06-13T12:33:56.000
	 -->
	   <xsl:for-each select="//detection">
	       <xsl:text>INSERT INTO VS_detection VALUES (</xsl:text>
           <xsl:value-of select="substring(@dateTime, 1, 4)"/>
           <xsl:value-of select="substring(@dateTime, 6, 2)"/>
           <xsl:value-of select="substring(@dateTime, 9, 2)"/>
           <xsl:value-of select="substring(@dateTime, 12, 2)"/>
           <xsl:value-of select="substring(@dateTime, 15, 2)"/>
           <xsl:value-of select="substring(@dateTime, 18, 6)"/>
	       <xsl:text>,</xsl:text>
           <xsl:value-of select="@txr"/>
           <xsl:text>,</xsl:text>
           <xsl:value-of select="@rxr"/>
           <xsl:text>,</xsl:text>
           <xsl:value-of select="@eventContext"/>
           <xsl:text>);
</xsl:text>
	   </xsl:for-each>
	</xsl:template>
</xsl:stylesheet>