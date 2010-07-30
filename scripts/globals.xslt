<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:aatams="http://www.imos.org.au/aatams" 
>
    <xsl:variable name="is_xsltforms">true</xsl:variable>
    <xsl:variable name="include_pkeys_in_form">false</xsl:variable>
    <xsl:variable name="xsltforms_path">xsltforms/xsltforms.xsl</xsl:variable>
    <xsl:variable name="namespace_name">aatams</xsl:variable>
    <xsl:variable name="namespace_uri">http://www.imos.org.au/aatams</xsl:variable>
    <xsl:variable name="title">AATAMS Database</xsl:variable>
	<xsl:variable name="wfs_url">
        <xsl:text>../../deegree-wfs/services</xsl:text>
	</xsl:variable>
    <xsl:variable name="get_feature_url">
        <xsl:value-of select="concat($wfs_url,'?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(',$namespace_name,'=',$namespace_uri,')&amp;typename=')"/>
	</xsl:variable>
	<!-- add a variable to limit number of recursions when adding subfeatures -->
	<xsl:variable name="max_depth">5</xsl:variable>
    <xsl:variable name="namespace"><xsl:value-of select="$namespace_name"/>:</xsl:variable>
    <xsl:variable name="output_path">C:/tomcat-5.5.25/apache-tomcat-5.5.25/webapps/aatams/forms</xsl:variable>
    <xsl:variable name="feature_prefix">aatams_</xsl:variable>
    <xsl:variable name="db_schema">aatams</xsl:variable> 
</xsl:stylesheet>


