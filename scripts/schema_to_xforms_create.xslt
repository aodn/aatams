<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:aatams="http://www.imos.org.au/aatams"
	xmlns:xf="http://www.w3.org/2002/xforms"
	xmlns:ev="http://www.w3.org/2001/xml-events"
	xmlns:wfs="http://www.opengis.net/wfs"
	xmlns:ogc="http://www.opengis.net/ogc"
	xmlns:ows="http://www.opengis.net/ows"
	xmlns:gml="http://www.opengis.net/gml">
	<xsl:output name="xml" method="xml" encoding="UTF-8" indent="yes" />
	<xsl:include href="help.xslt" />
	<xsl:variable name="wfs-url">
		<xsl:text>../../deegree-wfs/services</xsl:text>
	</xsl:variable>
	<xsl:template match="/">
		<xsl:for-each select="//table">
			<xsl:result-document
				href="{concat('file:///C:/eclipse_workspace/aatams/forms/create_',lower-case(@name),'.xml')}"
				format="xml">
				<!--xsl:result-document href="{concat('file:///C:/eclipse_workspace/zktest/WebContent/create_',lower-case(@name),'.xml')}" format="xml"-->
				<xsl:processing-instruction name="xml-stylesheet">
					<xsl:text>href="xsltforms/xsltforms.xsl" type="text/xsl"</xsl:text>
				</xsl:processing-instruction>
				<html>
					<head>
						<title>AATAMS Web Interface</title>
						<link href="aatams.css" rel="stylesheet"
							type="text/css" />
						<script type="text/javascript"
							src="aatams_xforms.js" charset="UTF-8" />
						<xf:model id="model1">
							<!-- instance for submission -->
							<xf:instance id="inst_data">
								<wfs:Transaction version="1.1.0"
									service="WFS">
									<wfs:Insert>
										<wfs:FeatureCollection>
											<gml:featureMember>
												<xsl:element
													name="aatams:{lower-case(@name)}">
													<xsl:apply-templates
														select="column" mode="instance" />
												</xsl:element>
											</gml:featureMember>
										</wfs:FeatureCollection>
									</wfs:Insert>
								</wfs:Transaction>
							</xf:instance>
							<!-- instance to hold subfeature ids -->
							<xf:instance id="inst_subfeatures">
								<data xmlns="">
									<xsl:for-each
										select="foreign-key">
										<xsl:choose>
											<xsl:when
												test="@foreignTable = 'PROJECT_PERSON'">
												<project_id />
												<project_person_id />
											</xsl:when>
											<xsl:when
												test="@foreignTable = 'INSTALLATION_DEPLOYMENT'">
												<installation_id />
												<installation_deployment_id />
											</xsl:when>
											<xsl:otherwise>
												<xsl:element
													name="{lower-case(@foreignTable)}_id">
													<!--xsl:value-of
														select="concat('aatams.',lower-case(@foreignTable),'.1')" /-->
												</xsl:element>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</data>
							</xf:instance>
							<!-- instances for subfeature lists -->
							<xsl:apply-templates select="foreign-key"
								mode="instance" />
							<!-- instance to receive response -->
							<xf:instance id="inst_response">
								<dummy xmlns="">
									<ows:Exception></ows:Exception>
									<wfs:TransactionResponse>
										<wfs:InsertResults>
											<wfs:Feature>
												<ogc:FeatureId />
											</wfs:Feature>
										</wfs:InsertResults>
									</wfs:TransactionResponse>
								</dummy>
							</xf:instance>
							<!-- bindings -->
							<xsl:apply-templates select="column"
								mode="binding" />
							<xf:bind id="error_message"
								nodeset="instance('inst_response')//ServiceException"
								type="xsd:string" />
							<xf:bind id="success_message"
								nodeset="instance('inst_response')//ogc:FeatureId/@fid"
								type="xsd:string" />
							<!--submission-->
							<xf:submission id="s01"
								ref="instance('inst_data')" method="post" action="{$wfs-url}"
								replace="instance" instance="inst_response">
								<xf:action ev:event="xforms-submit">
									<xsl:apply-templates select="column"
										mode="submission" />
									<xsl:apply-templates
										select="foreign-key" mode="submission" />
								</xf:action>
								<xf:message level="modeless"
									ev:event="xforms-submit-error">
									Submit error.
								</xf:message>
							</xf:submission>
							<!-- subfeature defaults, need to set initial value 'selected' to first in list -->
							<xf:dispatch ev:event="xforms-ready"
								name="set-selected" target="model1" />
							<xf:action ev:event="set-selected">
								<xsl:apply-templates
									select="foreign-key" mode="default-value" />
								<xf:dispatch name="xforms-revalidate" target="model1"/>
							</xf:action>
						</xf:model>
					</head>
					<body>
						<xsl:comment>
							<!-- debugging checkbox for events display -->
							<div id="xformControl">
								<span>
									<input type="checkbox"
										onclick="$('console').style.display = this.checked? 'block' : 'none';"
										checked="checked" />
									<xsl:text>Debug</xsl:text>
								</span>
							</div>
						</xsl:comment>
						<xsl:call-template name="form" />
						<br />
						<div id="console" />
					</body>
				</html>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="column" mode="instance">
		<xsl:element
			name="aatams:{lower-case(replace(@name,'_ID$',''))}" />
	</xsl:template>
	<xsl:template
		match="column[@name='STATUS_ID' or @primaryKey = 'true']"
		mode="instance" priority="5">
		<!--excluded-->
	</xsl:template>
	<!-- special case -->
	<xsl:template match="column[@name='PROJECT_ROLE_PERSON_ID']"
		mode="instance" priority="4">
		<xsl:element name="aatams:project_person_ref">
			<xsl:element name="aatams:project_person" />
		</xsl:element>
	</xsl:template>
	<!-- special case -->
	<xsl:template match="column[@name='DEPLOYMENT_ID']" mode="instance"
		priority="4">
		<xsl:element name="aatams:installation_deployment_ref">
			<xsl:element name="aatams:installation_deployment" />
		</xsl:element>
	</xsl:template>
	<!-- columns that are foreign keys are subfeatures -->
	<xsl:template
		match="column[../foreign-key[reference/@local=current()/@name]]"
		mode="instance" priority="3">
		<xsl:variable name="foreignTable">
			<xsl:value-of
				select="lower-case(../foreign-key[reference/@local=current()/@name][1]/@foreignTable)" />
		</xsl:variable>
		<xsl:element name="aatams:{concat($foreignTable,'_ref')}">
			<xsl:element name="aatams:{$foreignTable}" />
		</xsl:element>
	</xsl:template>
	<!-- subfeature list instance from foreign-key -->
	<xsl:template match="foreign-key" mode="instance">
		<xf:instance>
			<xsl:attribute name="id">
				<xsl:text>inst_</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
			<xsl:attribute name="src">
				<xsl:value-of select="$wfs-url" /><xsl:text>?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
		</xf:instance>
	</xsl:template>
	<xsl:template match="foreign-key[@foreignTable = 'PROJECT_PERSON']"
		mode="instance">
		<xf:instance id="inst_project_person"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:project_person" />
		<xf:instance id="inst_project"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:project" />
	</xsl:template>
	<xsl:template
		match="foreign-key[@foreignTable = 'INSTALLATION_DEPLOYMENT']"
		mode="instance">
		<xf:instance id="inst_installation"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:installation" />
		<xf:instance id="inst_installation_deployment"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:installation_deployment" />
	</xsl:template>
	<xsl:template match="column" mode="binding" priority="2">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">
				<xsl:value-of
					select="lower-case(replace(@name,'_ID$',''))" />
			</xsl:attribute>
			<xsl:attribute name="nodeset">
				<xsl:text>instance('inst_data')//aatams:</xsl:text>
				<xsl:value-of
					select="lower-case(replace(@name,'_ID$',''))" />
			</xsl:attribute>
			<xsl:attribute name="type">
				<xsl:choose>
					<!-- assume anything ending in _ID is refering to a subfeature so make it a string -->
					<xsl:when test="ends-with(@name,'_ID')">
						<xsl:text>xsd:string</xsl:text>
					</xsl:when>
					<xsl:when test="@type = 'INTEGER'">
						<xsl:text>xsd:integer</xsl:text>
					</xsl:when>
					<xsl:when test="@type = 'DECIMAL'">
						<xsl:text>xsd:decimal</xsl:text>
					</xsl:when>
					<xsl:when test="@type = 'VARCHAR'">
						<xsl:text>xsd:string</xsl:text>
					</xsl:when>
					<xsl:when test="@type = 'TIMESTAMP'">
						<xsl:text>xsd:dateTime</xsl:text>
					</xsl:when>
					<xsl:when test="@type = 'LONGVARCHAR'">
						<xsl:text>xsd:string</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>xsd:string</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required"/>
			</xsl:attribute>
			<xsl:apply-templates select="." mode="constraint" />
		</xsl:element>
	</xsl:template>
	<xsl:template
		match="column[@name = 'STATUS_ID' or @primaryKey = 'true']"
		mode="binding" priority="5">
		<!-- excluded -->
	</xsl:template>
	<xsl:template match="column[@name = 'PROJECT_ROLE_PERSON_ID']"
		mode="binding" priority="4">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">project</xsl:attribute>
			<xsl:attribute name="nodeset">instance('inst_subfeatures')//project_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required"/>
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">project_person</xsl:attribute>
			<xsl:attribute name="nodeset">instance('inst_subfeatures')//project_person_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="column[@name = 'INSTALLATION_ID']"
		mode="binding" priority="4">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">installation</xsl:attribute>
			<xsl:attribute name="nodeset">instance('inst_subfeatures')//installation_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required"/>
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">installation_station</xsl:attribute>
			<xsl:attribute name="nodeset">instance('inst_subfeatures')//installation_station_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="column[@name = 'DEPLOYMENT_ID']"
		mode="binding" priority="4">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">installation</xsl:attribute>
			<xsl:attribute name="nodeset">instance('inst_subfeatures')//installation_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required"/>
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">installation_deployment</xsl:attribute>
			<xsl:attribute name="nodeset">instance('inst_subfeatures')//installation_deployment_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="@required">
		<xsl:choose>
			<xsl:when test="current()='true'">
				<xsl:text>true()</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>false()</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template
		match="column[../foreign-key[reference/@local=current()/@name]]"
		mode="binding" priority="3">
		<xsl:apply-templates
			select="../foreign-key[reference/@local=current()/@name]"
			mode="binding" />
	</xsl:template>
	<!-- bindings for subfeature ids -->
	<xsl:template match="foreign-key" mode="binding">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
			<xsl:attribute name="nodeset">
				<xsl:text>instance('inst_subfeatures')//</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
				<xsl:text>_id</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:choose>
					<!-- see if the fk field is required="true"-->
					<xsl:when
						test="../column[@name=current()/reference/@local]/@required='true'">
						<xsl:text>true()</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>false()</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<!-- add any contraints to bindings -->
	<!-- this should probably be done via schema restrictions but not sure if supported by xsltforms -->
	<xsl:template match="column[ends-with(./@name,'LATITUDE')]"
		mode="constraint">
		<xsl:attribute name="constraint">. &gt;= -90 and . &lt;= 90</xsl:attribute>
	</xsl:template>
	<xsl:template match="column[ends-with(./@name,'LONGITUDE')]"
		mode="constraint">
		<xsl:attribute name="constraint">. &gt;= -180 and . &lt;= 180</xsl:attribute>
	</xsl:template>
	<!--set the initial selected value for select1 controls,
		this is like having a default value but actually choosing the correct 
		default value is problematic as it may differ for each person depending on
		what features they have access to. So better to take the first feature in each
		itemset.  
	-->
	<xsl:template match="foreign-key" mode="default-value">
		<xsl:element name="xf:setvalue">
			<xsl:variable name="feature">
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:variable>
			<xsl:attribute name="ref">
				<xsl:text>instance('inst_subfeatures')//</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>_id</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:text>instance('inst_</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>')//aatams:</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>[1]/@gml:id</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<!-- special cases -->
	<xsl:template match="foreign-key[@foreignTable = 'PROJECT_PERSON']"
		mode="default-value">
		<!-- convert to project and person_role -->
		<xf:setvalue ref="instance('inst_subfeatures')//project_id"
			value="instance('inst_project')//aatams:project[1]/@gml:id" />
		<xf:setvalue
			ref="instance('inst_subfeatures')//project_person_id"
			value="instance('inst_project_person')//aatams:project_person[aatams:project_fid=instance('inst_subfeatures')/project_id][1]/@gml:id" />
	</xsl:template>
	<xsl:template
		match="foreign-key[@foreignTable = 'INSTALLATION_DEPLOYMENT']"
		mode="default-value">
		<xf:setvalue
			ref="instance('inst_subfeatures')//installation_id"
			value="instance('inst_installation')//aatams:installation[1]/@gml:id" />
		<xf:setvalue
			ref="instance('inst_subfeatures')//installation_deployment_id"
			value="instance('inst_installation_deployment')//aatams:installation_deployment[aatams:installation_fid=instance('inst_subfeatures')/installation_id][1]/@gml:id" />
	</xsl:template>
	<!-- remove any non-mandatory numeric fields if no value -->
	<xsl:template
		match="column[@required='false' and not(../foreign-key[reference/@local=current()/@name]) and matches(@type,'INTEGER|DECIMAL|TIMESTAMP')]"
		mode="submission">
		<xsl:element name="xf:delete">
			<xsl:attribute name="nodeset">
				<xsl:text>instance('inst_data')//aatams:</xsl:text>
				<xsl:value-of select="lower-case(@name)" />
				<xsl:text>[.='']</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="foreign-key" mode="submission">
		<xsl:element name="xf:insert">
			<!-- where to put it -->
			<xsl:variable name="feature">
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:variable>
			<xsl:attribute name="nodeset">
				<xsl:text>instance('inst_data')//aatams:</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>_ref/aatams:</xsl:text>
				<xsl:value-of select="$feature" />
			</xsl:attribute>
			<!-- what to put there -->
			<xsl:attribute name="origin">
				<xsl:text>instance('inst_</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>')//aatams:</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>[@gml:id=instance('inst_subfeatures')/</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>_id]</xsl:text>
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:delete">
			<!-- where to put it -->
			<xsl:attribute name="nodeset">
				<xsl:text>instance('inst_data')//aatams:</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
				<xsl:text>_ref/aatams:</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
			<xsl:attribute name="at">1</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template name="form">
		<div class="form">
			<legend>
				<xsl:text>ADD </xsl:text>
				<xsl:value-of
					select="translate(upper-case(@name),'_',' ')" />
			</legend>
			<xsl:apply-templates select="column" mode="form" />
			<xf:submit submission="s01">
				<xf:label>Save</xf:label>
			</xf:submit>
			<xf:trigger>
				<xf:label>Reset</xf:label>
				<xf:reset ev:event="DOMActivate" />
				<xf:dispatch ev:event="DOMActivate" name="set-selected"
					target="model1" />
			</xf:trigger>
			<xf:group>
				<xf:output bind="error_message" class="error">
					<xf:label>Error:</xf:label>
				</xf:output>
				<xf:output bind="success_message">
					<xf:label>New Record Id:</xf:label>
				</xf:output>
			</xf:group>
		</div>
	</xsl:template>
	<xsl:template match="column" mode="form">
		<xsl:element name="xf:input">
			<xsl:attribute name="bind">
				<xsl:value-of
					select="lower-case(replace(@name,'_ID',''))" />
			</xsl:attribute>
			<xsl:attribute name="incremental">
				<xsl:text>true()</xsl:text>
			</xsl:attribute>
			<xf:label>
				<xsl:call-template name="proper-case">
					<xsl:with-param name="toconvert"
						select="translate(replace(@name,'_ID',''),'_',' ')" />
				</xsl:call-template>
			</xf:label>
			<xsl:call-template name="help">
				<xsl:with-param name="key">
					<xsl:value-of select="@name" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>
	<xsl:template
		match="column[@name = 'STATUS_ID' or @primaryKey = 'true']"
		mode="form" priority="5">
		<!-- excluded -->
	</xsl:template>
	<xsl:template match="column[@name = 'COMMENTS']" mode="form">
		<xsl:element name="xf:textarea">
			<xsl:attribute name="bind">
				<xsl:value-of select="lower-case(@name)" />
			</xsl:attribute>
			<xf:label>
				<xsl:call-template name="proper-case">
					<xsl:with-param name="toconvert"
						select="translate(@name,'_',' ')" />
				</xsl:call-template>
			</xf:label>
			<xsl:call-template name="help">
				<xsl:with-param name="key">
					<xsl:value-of select="@name" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>
	<xsl:template
		match="column[../foreign-key[reference/@local=current()/@name]]"
		mode="form" priority="3">
		<xsl:call-template name="select1-from-foreign-key">
			<xsl:with-param name="fk_node"
				select="../foreign-key[reference/@local=current()/@name]" />
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="column[@name = 'PROJECT_ROLE_PERSON_ID']"
		mode="form" priority="4">
		<!-- convert to project and person_role -->
		<div class="dependant-selects">
			<xf:select1 bind="project" appearance="minimal"
				incremental="true()">
				<xf:label>Project</xf:label>
				<xf:itemset
					nodeset="instance('inst_project')//aatams:project">
					<xf:value ref="@gml:id" />
					<xf:label ref="aatams:name" />
				</xf:itemset>
				<xf:action ev:event="xforms-value-changed">
					<xf:setvalue
						ref="instance('inst_subfeatures')//project_person_id" value="" />
				</xf:action>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xf:select1>
			<xf:select1 bind="project_person" appearance="minimal"
				incremental="true()">
				<xf:label>Person(Role)</xf:label>
				<xf:itemset
					nodeset="instance('inst_project_person')//aatams:project_person[aatams:project_fid=instance('inst_subfeatures')/project_id]">
					<xf:value ref="@gml:id" />
					<xf:label ref="aatams:person_role" />
				</xf:itemset>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xf:select1>
		</div>
	</xsl:template>
	<xsl:template match="column[@name = 'DEPLOYMENT_ID']" mode="form"
		priority="4">
		<!-- convert to project and person_role -->
		<div class="dependant-selects">
			<xf:select1 bind="installation" appearance="minimal"
				incremental="true()">
				<xf:label>Installation</xf:label>
				<xf:itemset
					nodeset="instance('inst_installation')//aatams:installation">
					<xf:value ref="@gml:id" />
					<xf:label ref="aatams:name" />
				</xf:itemset>
				<xf:action ev:event="xforms-value-changed">
					<xf:setvalue
						ref="instance('inst_subfeatures')//installation_deployment_id"
						value="" />
				</xf:action>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xf:select1>
			<xf:select1 bind="installation_deployment"
				appearance="minimal" incremental="true()">
				<xf:label>Deployment</xf:label>
				<xf:itemset
					nodeset="instance('inst_installation_deployment')//aatams:installation_deployment[aatams:installation_fid=instance('inst_subfeatures')/installation_id]">
					<xf:value ref="@gml:id" />
					<xf:label ref="aatams:name" />
				</xf:itemset>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xf:select1>
		</div>
	</xsl:template>
	<xsl:template
		match="column[@name = 'INSTALLATION_ID' and following-sibling::column[1]/@name = 'STATION_ID']"
		mode="form" priority="4">
		<!-- convert to installation and station -->
		<div class="dependant-selects">
			<xf:select1 bind="installation" appearance="minimal"
				incremental="true()">
				<xf:label>Installation</xf:label>
				<xf:itemset
					nodeset="instance('inst_installation')//aatams:installation">
					<xf:value ref="@gml:id" />
					<xf:label ref="aatams:name" />
				</xf:itemset>
				<xf:action ev:event="xforms-value-changed">
					<xf:setvalue
						ref="instance('inst_subfeatures')/station_id" value="" />
				</xf:action>
			</xf:select1>
			<xf:select1 bind="station" appearance="minimal"
				incremental="true()">
				<xf:label>Station</xf:label>
				<xf:itemset
					nodeset="instance('inst_station')//aatams:station[aatams:installation_fid=instance('inst_subfeatures')/installation_id]">
					<xf:value ref="@gml:id" />
					<xf:label ref="aatams:name" />
				</xf:itemset>
				<xf:action ev:event="xforms-value-changed">
					<xf:setvalue
						ref="instance('inst_data')//aatams:longitude"
						value="instance('inst_station')//aatams:station[@gml:id=instance('inst_subfeatures')/station_id]/aatams:longitude" />
					<xf:setvalue
						ref="instance('inst_data')//aatams:latitude"
						value="instance('inst_station')//aatams:station[@gml:id=instance('inst_subfeatures')/station_id]/aatams:latitude" />
				</xf:action>
			</xf:select1>
		</div>
	</xsl:template>
	<xsl:template
		match="column[@name = 'STATION_ID' and preceding-sibling::column[1]/@name = 'INSTALLATION_ID']"
		mode="form" priority="4">
		<!-- included in previous installation -->
	</xsl:template>
	<xsl:template name="proper-case">
		<xsl:param name="toconvert" />
		<xsl:if test="string-length($toconvert) &gt; 0">
			<xsl:variable name="f" select="substring($toconvert, 1, 1)" />
			<xsl:variable name="s" select="substring($toconvert, 2)" />
			<xsl:value-of select="upper-case($f)" />
			<xsl:choose>
				<xsl:when test="contains($s,' ')">
					<xsl:value-of
						select="lower-case(substring-before($s,&quot; &quot;))" />
					<xsl:text> </xsl:text>
					<xsl:call-template name="proper-case">
						<xsl:with-param name="toconvert"
							select="substring-after($s,&quot; &quot;)" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="lower-case($s)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<xsl:template name="select1-from-foreign-key">
		<xsl:param name="fk_node" />
		<xsl:element name="xf:select1">
			<xsl:attribute name="bind">
				<xsl:value-of
					select="lower-case($fk_node[1]/@foreignTable)" />
			</xsl:attribute>
			<xsl:attribute name="appearance">minimal</xsl:attribute>
			<xsl:attribute name="incremental">true()</xsl:attribute>
			<xf:label>
				<xsl:call-template name="proper-case">
					<xsl:with-param name="toconvert"
						select="translate(substring-before($fk_node[1]/reference/@local,'_ID'),'_',' ')" />
				</xsl:call-template>
			</xf:label>
			<!-- blank entry -->
			<!--xf:item>
				<xf:label>
				</xf:label>
				<xf:value>
				</xf:value>
				</xf:item-->
			<xsl:element name="xf:itemset">
				<!--xsl:attribute name="ref">
					<xsl:text>instance(</xsl:text><xsl:value-of select="lower-case($fk_node[1]/@foreignTable)"/>
					</xsl:attribute-->
				<xsl:attribute name="nodeset">
					<xsl:text>instance('inst_</xsl:text>
					<xsl:value-of
						select="lower-case($fk_node[1]/@foreignTable)" />
					<xsl:text>')//aatams:</xsl:text>
					<xsl:value-of
						select="lower-case($fk_node[1]/@foreignTable)" />
				</xsl:attribute>
				<xsl:element name="xf:value">
					<xsl:attribute name="ref">
						<xsl:text>@gml:id</xsl:text>
					</xsl:attribute>
				</xsl:element>
				<xsl:element name="xf:label">
					<xsl:attribute name="ref">
						<xsl:text>aatams:name</xsl:text>
					</xsl:attribute>
				</xsl:element>
			</xsl:element>
			<xsl:call-template name="help">
				<xsl:with-param name="key">
					<xsl:value-of select="@name" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
