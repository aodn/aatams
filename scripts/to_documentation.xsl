<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
    version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
                
<xsl:output method="html" 
            encoding="UTF-8"
            indent="yes" />

    <xsl:template match="/">
	    <html>
		<head>
			<style type="text/css">th{ border: 1px solid #6699CC;border-collapse: collapse;background-color: #BEC8D1;font-family: Verdana;font-weight: bold;font-size: 11px;color: /*#404040*/ black; padding: 5px; }td{ border: 1px solid #9CF;border-collapse: collapse;font-family: Verdana, sans-serif, Arial;font-weight: normal;font-size: 11px;color: /*#404040*/ black;white-space: nowrap;background-color: #fafafa; }table{ text-align: center;font-family: Verdana;font-weight: normal;font-size: 11px;color: /*#404040*/ black;background-color: #fafafa;border: 1px #6699CC solid;border-collapse: collapse;border-spacing: 0px; }.tablename{background-color: #FFEE88;}</style>
		</head>
		<body>
			<table border="1">
				<thead>
				</thead>
				<tbody>
					<xsl:for-each select="//table">
						<tr><th colspan="6" class="tablename">TABLE: <xsl:value-of select="@name"/></th></tr>
						<tr><th>COLUMN</th><th>KEY</th><th>DATA-TYPE</th><th>SIZE</th><th>REQUIRED</th><th>DEFAULT</th></tr>
						<xsl:apply-templates select="column"/>
					</xsl:for-each>
				</tbody>
			</table>
		</body>
	</html>	
</xsl:template>

<xsl:template match="column">
	<tr>
		<td><xsl:value-of select="@name"/></td>
		<td><xsl:choose><xsl:when test="@primaryKey='true'">PK</xsl:when><xsl:when test="@name=../foreign-key/reference/@local">FK</xsl:when><xsl:otherwise>&#160;</xsl:otherwise></xsl:choose></td>
		<td><xsl:value-of select="@type"/></td>
		<td><xsl:value-of select="@size"/></td>
		<td><xsl:value-of select="@required"/>&#160;</td>
		<td><xsl:value-of select="@default"/>&#160;</td>
	</tr>
</xsl:template>

<xsl:template name="child">
	<xsl:param name='child-table'/>
	<li style="display:inline;margin:10px;">
		<xsl:value-of select="$child-table/@name"/>
	</li>
</xsl:template>

<xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>
<xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>	

<xsl:template name='convertcase'>
	<xsl:param name='toconvert' />
	<xsl:param name='conversion' />
	<xsl:choose>
		<xsl:when test='$conversion="lower"'>
			<xsl:value-of select="translate($toconvert,$ucletters,$lcletters)"/>
		</xsl:when>
		<xsl:when test='$conversion="upper"'>
			<xsl:value-of select="translate($toconvert,$lcletters,$ucletters)"/>
		</xsl:when>
		<xsl:when test='$conversion="proper"'>
			<xsl:call-template name='convertpropercase'>
				<xsl:with-param name='toconvert' select="translate($toconvert,$ucletters,$lcletters)"/>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select='$toconvert' />
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name='convertpropercase'>
	<xsl:param name='toconvert' />
	<xsl:if test="string-length($toconvert) > 0">
		<xsl:variable name='f' select='substring($toconvert, 1, 1)' />
		<xsl:variable name='s' select='substring($toconvert, 2)' />
		<xsl:call-template name='convertcase'>
			<xsl:with-param name='toconvert' select='$f' />
			<xsl:with-param name='conversion'>upper</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="contains($s,' ')">
				<xsl:call-template name='convertcase'>
					<xsl:with-param name='toconvert'  select='substring-before($s," ")' />
					<xsl:with-param name='conversion'>lower</xsl:with-param>
				</xsl:call-template>
				<xsl:text> </xsl:text>
				<xsl:call-template name='convertpropercase'>
					<xsl:with-param name='toconvert' select='substring-after($s," ")' />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name='convertcase'>
					<xsl:with-param name='toconvert' select='$s' />
					<xsl:with-param name='conversion'>lower</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>

