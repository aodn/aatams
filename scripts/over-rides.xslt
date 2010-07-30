<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:aatams="http://www.imos.org.au/aatams" 
>

	<!--  
		template to insert an xform select1 control 
	-->
	<xsl:template match="column[@name='created' or @name='modified' or @name='disabled']" mode="form" priority="5">
    </xsl:template>

    <xsl:template match="column[@name='created' or @name='modified' or @name='disabled']" mode="edit" priority="5">
	</xsl:template>

    <xsl:template match="column[@name='created' or @name='modified' or @name='disabled']" mode="delete" priority="5">
    </xsl:template>

    <xsl:template match="column[@name='disabled']" mode="view" priority="5">
	</xsl:template>

</xsl:stylesheet>


