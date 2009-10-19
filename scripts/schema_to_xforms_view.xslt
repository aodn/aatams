<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:ev="http://www.w3.org/2001/xml-events">
	<xsl:output name="xml" method="xml" encoding="UTF-8" indent="yes" />
	<xsl:variable name="wfs-url">../../degree-wfs/services</xsl:variable>
	<xsl:template match="/">
		<xsl:for-each select="//table">
			<xsl:variable name="typename"><xsl:value-of select="lower-case(@name)"/></xsl:variable>
			<xsl:result-document href="{concat('file:///C:/eclipse_workspace/aatams/forms/view_',$typename,'.xml')}" format="xml">
				<xsl:processing-instruction name="xml-stylesheet">
					<xsl:text>href="xsltforms/xsltforms.xsl" type="text/xsl"</xsl:text>
				</xsl:processing-instruction>
				<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:aatams="http://www.imos.org.au/aatams">
					<head>
						<title>AATAMS Web Interface</title>
						<link href="aatams.css" rel="stylesheet" type="text/css" />
						<xf:model>
							<xf:instance id="inst_request">
								<wfs:GetFeature xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:aatams="http://www.imos.org.au/aatams" outputFormat="text/xml; subtype=gml/3.1.1">
									<wfs:Query>
										<xsl:attribute name="typeName">
											<!-- namespaces get renamed by XSLTForms,
											to get the http://www.imos.org.au/aatams ns to stay present we
											add a dummy element and the ns name is changed to 'pre3' -->
											<xsl:text>pre3:</xsl:text>
											<xsl:value-of select="$typename" />
										</xsl:attribute>
										<ogc:Filter>
											<ogc:GmlObjectId gml:id="" />
										</ogc:Filter>
									</wfs:Query>
									<aatams:dummy/>
								</wfs:GetFeature>
							</xf:instance>
							<xf:instance id="inst_feature_id">
								<id xmlns="">
									<xsl:text>aatams.</xsl:text>
									<xsl:value-of select="$typename" />
									<xsl:text>.NNNN</xsl:text>
								</id>
							</xf:instance>
							<!-- id binding -->
							<xf:bind id="feature_id" nodeset="instance('inst_feature_id')" type="xsd:string" />
							<!-- the response instance that gets replaced with the server response on submission,
								need to seed the namespaces --> 
							<xf:instance id="inst_response">
								<wfs:FeatureCollection xmlns:wfs="http://www.opengis.net/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:aatams="http://www.imos.org.au/aatams">
									<gml:featureMember>
										<xsl:element name="aatams:{$typename}"/>
									</gml:featureMember>
								</wfs:FeatureCollection>
							</xf:instance>
							<!-- bindings -->
							<xsl:for-each select="column">
								<xsl:choose>
									<xsl:when test="@name = 'STATUS_ID' or @primaryKey = 'true'">
										<!-- not of interest -->
                                    </xsl:when>
									<xsl:when test="@name = 'PROJECT_ROLE_PERSON_ID'">
										<!-- remapped to PROJECT_PERSON feature -->
										<xf:bind id="project" nodeset="instance('inst_response')//aatams:project_person/aatams:project_name" type="xsd:string"/>
										<xf:bind id="person_role" nodeset="instance('inst_response')//aatams:project_person/aatams:person_name" type="xsd:string"/>
                                    </xsl:when>
									<xsl:when test="../foreign-key[reference/@local=current()/@name]">
										<xsl:variable name="foreignTable">
											<xsl:value-of select="lower-case(../foreign-key[reference/@local=current()/@name][1]/@foreignTable)" />
										</xsl:variable>
										<xsl:element name="xf:bind">
											<xsl:attribute name="id">
												<xsl:value-of select="$foreignTable"/>
											</xsl:attribute>
											<xsl:attribute name="nodeset">
												<xsl:text>instance('inst_response')//aatams:</xsl:text>
												<xsl:value-of select="$foreignTable"/>
												<xsl:text>/aatams:name</xsl:text>
											</xsl:attribute>
											<xsl:attribute name="type">xsd:string</xsl:attribute>
										</xsl:element>
                                    </xsl:when>
									<xsl:otherwise>
										<xsl:element name="xf:bind">
											<xsl:attribute name="id">
												<xsl:value-of select="lower-case(replace(@name,'_ID$',''))" />
											</xsl:attribute>
											<xsl:attribute name="nodeset">
												<xsl:text>instance('inst_response')//aatams:</xsl:text>
												<xsl:value-of select="$typename" />
												<xsl:text>/aatams:</xsl:text>
												<xsl:value-of select="lower-case(replace(@name,'_ID$',''))" />
											</xsl:attribute>
											<xsl:attribute name="type">xsd:string</xsl:attribute>
										</xsl:element>
                                    </xsl:otherwise>
                                </xsl:choose>
							</xsl:for-each>
							<!--submission-->
							<xf:submission id="s01" method="post" action="{$wfs-url}" ref="instance('inst_request')" replace="instance" instance="inst_response">
								<xf:message level="modeless" ev:event="xforms-submit-error">Submit error.</xf:message>
							</xf:submission>
						</xf:model>
					</head>
					<body>
						<!--<div id="xformControl">
							<span>
								<input type="checkbox" onclick="$('console').style.display = this.checked? 'block' : 'none';" checked="checked" />
								<xsl:text>Debug</xsl:text>
							</span>
						</div>-->
						<xsl:call-template name="form" />
						<br />
						<div id="console" style="display:none;"/>
					</body>
				</html>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="form">
		<fieldset>
			<legend>
				<xsl:text>VIEW </xsl:text><xsl:value-of select="translate(upper-case(@name),'_',' ')" />
			</legend>
			<table xmlns="http://www.w3.org/1999/xhtml">
				<tbody>
					<tr>
						<td>
							<xf:input bind="feature_id">
								<xf:label>
									<xsl:call-template name="proper-case">
										<xsl:with-param name="toconvert" select="concat('ENTER ' ,replace(@name,'_',' ') ,' ID')" />
									</xsl:call-template>
								</xf:label>
							</xf:input>
						</td>
						<td>
							<!-- request trigger -->
							<xf:trigger>
								<xf:label>Search</xf:label>
								<xf:action ev:event="DOMActivate">
									<xf:setvalue ref="instance('inst_request')//@gml:id" value="instance('inst_feature_id')" />
									<xf:send submission="s01" />
								</xf:action>
							</xf:trigger>
						</td>
					</tr>
				</tbody>
			</table>
			<!-- output for each column -->
			<xsl:for-each select="column">
				<xsl:choose>
					<xsl:when test="@primaryKey = 'true'">
						<!-- using gml:id as identifier -->
					</xsl:when>
					<xsl:when test="@name = 'STATUS_ID'">
						<!-- export status from Access not needed -->
					</xsl:when>
					<xsl:when test="@name = 'PROJECT_ROLE_PERSON_ID'">
						<!-- remapped to PROJECT_PERSON view -->
						<xf:output bind="project">
							<xf:label>Project:</xf:label>
							<xf:help>This is a presently unhelpful help message</xf:help>
						</xf:output>
						<xf:output bind="person_role">
							<xf:label>Person(Role):</xf:label>
						<xf:help>This is a presently unhelpful help message</xf:help>
						</xf:output>
					</xsl:when>
					<xsl:when test="@name = 'COMMENTS'">
						<xsl:element name="xf:output">
							<xsl:attribute name="bind">
								<xsl:value-of select="lower-case(@name)" />
							</xsl:attribute>
							<xf:label>
								<xsl:call-template name="proper-case">
									<xsl:with-param name="toconvert" select="translate(@name,'_',' ')" />
								</xsl:call-template>
								<xsl:text>:</xsl:text>
							</xf:label>
							<xf:help>This is a presently unhelpful help message</xf:help>
						</xsl:element>
					</xsl:when>
					<xsl:when test="../foreign-key[reference/@local=current()/@name]">
						<xsl:variable name="foreignTable">
							<xsl:value-of select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
						</xsl:variable>
						<xsl:element name="xf:output">
							<xsl:attribute name="bind">
								<xsl:value-of select="lower-case($foreignTable)" />
							</xsl:attribute>
							<xf:label>
								<xsl:call-template name="proper-case">
									<xsl:with-param name="toconvert" select="translate($foreignTable,'_',' ')" />
								</xsl:call-template>
								<xsl:text>:</xsl:text>
							</xf:label>
							<xf:help>This is a presently unhelpful help message</xf:help>
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="xf:output">
							<xsl:attribute name="bind">
								<xsl:value-of select="lower-case(replace(@name,'_ID',''))" />
							</xsl:attribute>
							<xf:label>
								<xsl:call-template name="proper-case">
									<xsl:with-param name="toconvert" select="translate(replace(@name,'_ID',''),'_',' ')" />
								</xsl:call-template>
								<xsl:text>:</xsl:text>
							</xf:label>
							<xf:help>This is a presently unhelpful help message</xf:help>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</fieldset>
	</xsl:template>
	<xsl:template name="proper-case">
		<xsl:param name="toconvert" />
		<xsl:if test="string-length($toconvert) &gt; 0">
			<xsl:variable name="f" select="substring($toconvert, 1, 1)" />
			<xsl:variable name="s" select="substring($toconvert, 2)" />
			<xsl:value-of select="upper-case($f)" />
			<xsl:choose>
				<xsl:when test="contains($s,' ')">
					<xsl:value-of select="lower-case(substring-before($s,&quot; &quot;))" />
					<xsl:text>&#x20;</xsl:text>
					<xsl:call-template name="proper-case">
						<xsl:with-param name="toconvert" select="substring-after($s,&quot; &quot;)" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="lower-case($s)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
