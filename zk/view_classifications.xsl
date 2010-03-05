<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:gml="http://www.opengis.net/gml"  xmlns:wfs="http://www.opengis.net/wfs" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xalan" xmlns="http://www.w3.org/1999/xhtml">


	<xsl:template match="wfs:FeatureCollection">
		<table>
			<thead>
                <tr>
                    <th colspan="1" rowspan="1">ID</th>
					<th colspan="1" rowspan="1">Classification</th>
					<th colspan="1" rowspan="1">Common Name</th>
					<th colspan="1" rowspan="1"><a href="http://www.marine.csiro.au/caab/">CAAB code</a></th>
				</tr>
			</thead>
            <tbody>
                <xsl:for-each select="gml:featureMember/aatams:classification[aatams:classification_level_ref/aatams:classification_level/aatams:name='FAMILY']">
                    <xsl:sort select="aatams:name"/>
                    <tr>
                        <td>
                            <xsl:value-of select="substring-after(@gml:id,'aatams.classification.')"/>
                        </td>
                        <td class="text">
                            <xsl:text>FAMILY:</xsl:text><xsl:value-of select="aatams:name"/>
                        </td>
                        <td class="text">
                            <xsl:value-of select="aatams:common_name"/>
                        </td>
                        <td style="text-align:center;">
                             <a target="_blank">
                                 <xsl:attribute name="href">
                                     <xsl:text>http://www.marine.csiro.au/caabsearch/caab_search.caab_report?spcode=</xsl:text>
                                     <xsl:value-of select="aatams:caab_code" />
                                 </xsl:attribute>
                                 <xsl:value-of select="aatams:caab_code" />
                             </a>
                         </td>
                    </tr>
                    <xsl:for-each select="../../gml:featureMember/aatams:classification[aatams:parent_classification_ref/aatams:parent_classification/aatams:name=current()/aatams:name]">
                    <xsl:sort select="aatams:name"/>    
                    <tr>
                        <td>
                            <xsl:value-of select="substring-after(@gml:id,'aatams.classification.')"/>
                        </td>
                        <td class="text">
                            <xsl:text>&#160;&#160;&#160;GENUS:</xsl:text><xsl:value-of select="aatams:name"/>
                        </td>
                        <td class="text">
                            <xsl:value-of select="aatams:common_name"/>
                        </td>
                        <td style="text-align:center;">
                             <a target="_blank">
                                 <xsl:attribute name="href">
                                     <xsl:text>http://www.marine.csiro.au/caabsearch/caab_search.caab_report?spcode=</xsl:text>
                                     <xsl:value-of select="aatams:caab_code" />
                                 </xsl:attribute>
                                 <xsl:value-of select="aatams:caab_code" />
                             </a>
                         </td>
                    </tr>
                    <xsl:for-each select="../../gml:featureMember/aatams:classification[aatams:parent_classification_ref/aatams:parent_classification/aatams:name=current()/aatams:name]">
                    <xsl:sort select="aatams:name"/>
                    <tr>
                        <td>
                            <xsl:value-of select="substring-after(@gml:id,'aatams.classification.')"/>
                        </td>
                        <td class="text">
                            <xsl:text>&#160;&#160;&#160;&#160;&#160;&#160;SPECIES:</xsl:text><xsl:value-of select="aatams:name"/>
                        </td>
                        <td class="text">
                            <xsl:value-of select="aatams:common_name"/>
                        </td>
                        <td style="text-align:center;">
                             <a target="_blank">
                                 <xsl:attribute name="href">
                                     <xsl:text>http://www.marine.csiro.au/caabsearch/caab_search.caab_report?spcode=</xsl:text>
                                     <xsl:value-of select="aatams:caab_code" />
                                 </xsl:attribute>
                                 <xsl:value-of select="aatams:caab_code" />
                             </a>
                         </td>
                    </tr>
                    </xsl:for-each>
                    </xsl:for-each>
                </xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
</xsl:stylesheet>

