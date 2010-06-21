<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
>
    <xsl:variable name="is_xsltforms">true</xsl:variable>
    <xsl:variable name="include_pkeys_in_form">false</xsl:variable>
    <xsl:variable name="xsltforms_path">../xsltforms/xsltforms.xsl</xsl:variable>
    <xsl:variable name="namespace_name">emii</xsl:variable>
    <xsl:variable name="namespace_uri">http://www.imos.org.au/emii</xsl:variable>
    <xsl:variable name="title">IMOS Publications Database</xsl:variable>
	<xsl:variable name="wfs_url">
		<xsl:text>../services</xsl:text>
	</xsl:variable>
    <xsl:variable name="get_feature_url">
        <xsl:value-of select="$wfs_url"/>
		<xsl:text>?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(emii=http://www.imos.org.au/emii)&amp;typename=</xsl:text>
	</xsl:variable>
	<!-- add a variable to limit number of recursions when adding subfeatures -->
	<xsl:variable name="max_depth">5</xsl:variable>
    <xsl:variable name="namespace"><xsl:value-of select="$namespace_name"/>:</xsl:variable>
    <xsl:variable name="output_path">C:/tomcat-5.5.25/apache-tomcat-5.5.25/webapps/deegree-wfs/forms</xsl:variable>
    <xsl:variable name="feature_prefix">imos.pub.</xsl:variable>
    <xsl:variable name="db_schema">imos_publications</xsl:variable> 
</xsl:stylesheet>


