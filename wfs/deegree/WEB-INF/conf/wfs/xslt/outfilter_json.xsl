<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:gml="http://www.opengis.net/gml" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" xmlns:wfs="http://www.opengis.net/wfs">
    <xsl:output method="xml" version="1.0" encoding="iso-8859-1" omit-xml-declaration="yes" />
    <xsl:strip-space elements="*"/>
    <xsl:template match="wfs:FeatureCollection">
            <xsl:text>var </xsl:text><xsl:value-of select="local-name(gml:featureMember[1]/*)"/><xsl:text>_list = [</xsl:text>
            <xsl:apply-templates select="gml:featureMember">
                    <xsl:sort select="*/aatams:name"/>
            </xsl:apply-templates>
        <xsl:text>];</xsl:text>
    </xsl:template>
    <xsl:template match="gml:featureMember">
        <xsl:choose>
            <xsl:when test="aatams:installation">
		    <xsl:text>["</xsl:text><xsl:value-of select="*/@gml:id"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:name"/><xsl:text>",[</xsl:text><xsl:apply-templates select="*/aatams:installation_station_ref"/><xsl:text>]]</xsl:text>
            </xsl:when>
            <xsl:when test="aatams:genus">
                  <xsl:text>["</xsl:text><xsl:value-of select="*/@gml:id"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:name"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:family_ref/aatams:family/@gml:id"/><xsl:text>"]</xsl:text>
            </xsl:when>
            <xsl:when test="aatams:species">
                  <xsl:text>["</xsl:text><xsl:value-of select="*/@gml:id"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:name"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:genus_ref/aatams:genus/@gml:id"/><xsl:text>"]</xsl:text>
            </xsl:when>
            <xsl:when test="aatams:project_person">
                  <xsl:text>["</xsl:text><xsl:value-of select="*/@gml:id"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:person_role"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:project_fid"/><xsl:text>"]</xsl:text>
            </xsl:when>
            <xsl:otherwise>
               <xsl:text>["</xsl:text><xsl:value-of select="*/@gml:id"/><xsl:text>","</xsl:text><xsl:value-of select="*/aatams:name"/><xsl:text>"]</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="position()!=last()">
            <xsl:text>,&#013;</xsl:text>
        </xsl:if>
    </xsl:template>
    <!-- installation_stations within installations -->
    <xsl:template match="aatams:installation_station_ref">
	 <xsl:if test="position()=1">
               <xsl:text>&#013;</xsl:text>
         </xsl:if>   
         <xsl:text>["</xsl:text><xsl:value-of select="aatams:installation_station/@gml:id"/><xsl:text>","</xsl:text><xsl:value-of select="aatams:installation_station/aatams:name"/><xsl:text>"]</xsl:text>
         <xsl:if test="position()!=last()">
               <xsl:text>,&#013;</xsl:text>
         </xsl:if>
         <xsl:if test="position()=last()">
               <xsl:text>&#013;</xsl:text>
         </xsl:if>
    </xsl:template>
</xsl:stylesheet>
