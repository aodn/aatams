<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
		xmlns:aatams="http://www.imos.org.au/aatams"
		xmlns:gml="http://www.opengis.net/gml"
		xmlns:wfs="http://www.opengis.net/wfs"
		xmlns:xlink="http://www.w3.org/1999/xlink"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
	<xsl:processing-instruction name="xml-stylesheet">
		href="xslt/xhtml.xsl" type="text/xsl"
	</xsl:processing-instruction>
		
	<xsl:copy-of select="."/>
	</xsl:template>
</xsl:stylesheet>
