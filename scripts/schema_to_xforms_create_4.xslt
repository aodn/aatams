<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<!-- 
	New version including prototypes of subfeatures, 'Add' buttons.
	
-->

<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
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
			<xsl:variable name="feature-name">
				<xsl:value-of select="lower-case(@name)" />
			</xsl:variable>
			<xsl:result-document
				href="{concat('file:///C:/eclipse_workspace/aatams/forms4/create_',lower-case(@name),'.xml')}"
				format="xml">
				<xsl:processing-instruction name="xml-stylesheet">
					<xsl:text>href="xsltforms/xsltforms.xsl" type="text/xsl"</xsl:text>
				</xsl:processing-instruction>
				<html>
					<head>
						<title>AATAMS Web Interface</title>
						<link href="aatams.css" rel="stylesheet"
							type="text/css" />
						<xsl:call-template name="model" />
					</head>
					<body>
						<xsl:call-template name="form" />
					</body>
				</html>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>

	<!-- 
		template to build the model 
	-->
	<xsl:template name="model">
		<xf:model id="model1">
			<xsl:call-template name="wfs-transaction" />
			<xsl:call-template name="subfeature-lists" />
			<xsl:call-template name="selected-subfeatures" />
			<xsl:call-template name="prototypes" />
			<xsl:call-template name="bindings" />
			<xsl:call-template name="submission" />
			<xsl:call-template name="wfs-response" />
			<xsl:call-template name="messages" />
			<xsl:call-template name="initial-subfeatures" />
		</xf:model>
	</xsl:template>

	<!-- 
		template to build the form
	-->
	<xsl:template name="form">
		<div class="form">
			<label>
				<xsl:text>ADD </xsl:text>
				<xsl:value-of
					select="translate(upper-case(@name),'_',' ')" />
			</label>
			<div class="form-contents">
				<xf:switch>
					<xf:case id="{lower-case(@name)}" selected="true">
						<xsl:apply-templates select="column"
							mode="form" />
						<div class="buttons">
							<div class="xfsubmit">
								<xf:submit submission="s01">
									<xf:label>Save</xf:label>
								</xf:submit>
							</div>
							<div class="xftrigger"
								style="clear:none;">
								<xf:trigger>
									<xf:label>Reset</xf:label>
									<xf:reset ev:event="DOMActivate" />
									<xf:dispatch ev:event="DOMActivate"
										name="set-selected" target="model1" />
									<xf:delete ev:event="DOMActivate"
										nodeset="instance('resp')//ServiceException" />
									<xf:delete ev:event="DOMActivate"
										nodeset="instance('resp')//ogc:FeatureId/@fid" />
								</xf:trigger>
							</div>
						</div>
						<div id="messages">
							<div id="error-message">
								<xf:output bind="_error_message">
									<xf:label>Error:</xf:label>
								</xf:output>
							</div>
							<div id="success-message">
								<xf:output bind="_success_message">
									<xf:label>New Record Id:</xf:label>
								</xf:output>
							</div>
						</div>
					</xf:case>
					<!-- add cases for subfeature prototype manipulation -->
					<xsl:for-each
						select="../table[foreign-key/@foreignTable=current()/@name]">
						<xf:case id="{lower-case(@name)}">
							<xsl:apply-templates select="." mode="form-case" />
						</xf:case>
					</xsl:for-each>
				</xf:switch>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="table" mode="form-case">
		<xsl:choose>
			<xsl:when test="foreign-key">
				<!--xf:switch>
					<xf:case id="{lower-case(@name)}" selected="true">
					</xf:case-->
					<!-- add cases for subfeature prototype manipulation -->
					<!-- xsl:for-each
						select="../table[foreign-key/@foreignTable=current()/@name]">
						<xf:case id="{lower-case(@name)}">
							<xsl:apply-templates mode="form-case" />
						</xf:case>
					</xsl:for-each>
				</xf:switch-->
				<xsl:apply-templates select="column" mode="form" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="column" mode="form" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!--
		template to create xml base transaction for submission to WFS 
	-->
	<xsl:template name="wfs-transaction">
		<xf:instance id="wfst">
			<wfs:Transaction version="1.1.0" service="WFS">
				<wfs:Insert>
					<wfs:FeatureCollection>
						<gml:featureMember>
							<xsl:element
								name="aatams:{lower-case(@name)}">
								<xsl:apply-templates select="column"
									mode="wfs-t" />
							</xsl:element>
						</gml:featureMember>
					</wfs:FeatureCollection>
				</wfs:Insert>
			</wfs:Transaction>
		</xf:instance>
	</xsl:template>

	<!-- 
		template for simple feature properties
	-->
	<xsl:template match="column" mode="wfs-t">
		<xsl:element
			name="aatams:{lower-case(replace(@name,'_ID$',''))}" />
	</xsl:template>

	<!-- 
		template for subfeature properties
	-->
	<xsl:template
		match="column[../foreign-key[reference/@local=current()/@name]]"
		mode="wfs-t" priority="3">
		<xsl:variable name="foreignTable">
			<xsl:value-of
				select="lower-case(../foreign-key[reference/@local=current()/@name][1]/@foreignTable)" />
		</xsl:variable>
		<xsl:element name="aatams:{concat($foreignTable,'_ref')}">
			<xsl:element name="aatams:{$foreignTable}" />
		</xsl:element>
	</xsl:template>

	<!-- 
		template to create instances containing lists of subfeatures obtained from WFS 	
	-->
	<xsl:template name="subfeature-lists">
		<xsl:apply-templates select="foreign-key" mode="wfs-t" />
	</xsl:template>

	<!-- template to create subfeature list instance from foreign-key -->
	<xsl:template match="foreign-key" mode="wfs-t">
		<xf:instance>
			<xsl:attribute name="id">
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
			<xsl:attribute name="src">
				<xsl:value-of select="$wfs-url" /><xsl:text>?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
		</xf:instance>
	</xsl:template>

	<!-- 
		template to create an instance to hold subfeature fids, this is needed due to lack
		of support for xf:select/xf:itemset/xf:copy in xsltforms
	-->
	<xsl:template name="selected-subfeatures">
		<xf:instance id="subf">
			<data xmlns="">
				<xsl:apply-templates select="foreign-key"
					mode="selected-subfeature-ids" />
			</data>
		</xf:instance>
	</xsl:template>

	<xsl:template match="foreign-key" mode="selected-subfeature-ids">
		<xsl:element name="{lower-case(@foreignTable)}_id" xmlns="" />
	</xsl:template>
	
	
	<!-- 
		template for adding prototype subfeatures
	 -->

	<!--
		template to create data bindings
	-->
	<xsl:template name="bindings">
		<xsl:apply-templates select="column" mode="binding" />
	</xsl:template>

	<!--
		bindings for simple properties  
	-->
	<xsl:template match="column" mode="binding" priority="2">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">
				<!-- add underscore to differentiate from instances -->
				<xsl:text>_</xsl:text>
				<xsl:value-of
					select="lower-case(replace(@name,'_ID$',''))" />
			</xsl:attribute>
			<xsl:attribute name="nodeset">
				<xsl:text>instance('wfst')//aatams:</xsl:text>
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
					<xsl:when test="@type = 'DATE'">
						<xsl:text>xsd:date</xsl:text>
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
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
			<xsl:apply-templates select="." mode="constraint" />
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

	<!-- bindings for subfeatures -->
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
				<xsl:text>_</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
			<xsl:attribute name="nodeset">
				<xsl:text>instance('subf')/</xsl:text>
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

	<!--
		template to create form submission
	-->
	<xsl:template name="submission">
		<xf:submission id="s01" ref="instance('wfst')" method="post"
			action="{$wfs-url}" replace="instance" instance="resp">
			<xf:action ev:event="xforms-submit">
				<xsl:apply-templates select="column" mode="submission" />
				<xsl:apply-templates select="foreign-key"
					mode="submission" />
			</xf:action>
			<xf:message level="modeless"
				ev:event="xforms-submit-error">
				Submit error.
			</xf:message>
		</xf:submission>
	</xsl:template>

	<!--  
		template to copy selected subfeature into submission
	-->
	<xsl:template match="foreign-key" mode="submission">
		<!-- add the new 'selected' one after the dummy one present at start -->
		<xsl:element name="xf:insert">
			<!-- where to put it -->
			<xsl:variable name="feature">
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:variable>
			<xsl:attribute name="nodeset">
				<xsl:text>instance('wfst')//aatams:</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>_ref/aatams:</xsl:text>
				<xsl:value-of select="$feature" />
			</xsl:attribute>
			<!-- what to put there -->
			<xsl:attribute name="origin">
				<xsl:text>instance('</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>')/gml:featureMember/aatams:</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>[@gml:id=instance('subf')/</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>_id]</xsl:text>
			</xsl:attribute>
		</xsl:element>
		<!-- delete the dummy one -->
		<xsl:element name="xf:delete">
			<xsl:attribute name="nodeset">
				<xsl:text>instance('wfst')//aatams:</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
				<xsl:text>_ref/aatams:</xsl:text>
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:attribute>
			<xsl:attribute name="at">1</xsl:attribute>
		</xsl:element>
	</xsl:template>

	<!--
		template to create submission response instance
	-->
	<xsl:template name="wfs-response">
		<xf:instance id="resp">
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
	</xsl:template>

	<!--
		template to create post submission messages
	-->
	<xsl:template name="messages">
		<xf:bind id="_error_message"
			nodeset="instance('resp')//ServiceException"
			calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)"
			type="xsd:string" />
		<xf:bind id="_success_message"
			nodeset="instance('resp')//ogc:FeatureId/@fid" type="xsd:string" />
	</xsl:template>

	<!-- 
		template to set initial 'selected' subfeature to be the first in list
	-->
	<xsl:template name="initial-subfeatures">
		<xf:dispatch ev:event="xforms-ready" name="set-selected"
			target="model1" />
		<xf:action ev:event="set-selected">
			<xsl:apply-templates select="foreign-key"
				mode="default-value" />
			<xf:dispatch name="xforms-revalidate" target="model1" />
		</xf:action>
	</xsl:template>

	<!--
		sets the initial selected value for select1 controls 
	-->
	<xsl:template match="foreign-key" mode="default-value">
		<xsl:element name="xf:setvalue">
			<xsl:variable name="feature">
				<xsl:value-of select="lower-case(@foreignTable)" />
			</xsl:variable>
			<xsl:attribute name="ref">
				<xsl:text>instance('subf')/</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>_id</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:text>instance('</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>')/gml:featureMember/aatams:</xsl:text>
				<xsl:value-of select="$feature" />
				<xsl:text>[1]/@gml:id</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>



	<!--  
		template to insert an xform input control
	-->
	<xsl:template match="column" mode="form">
		<div class="xfinput">
			<xsl:element name="xf:input">
				<xsl:attribute name="bind">
					<xsl:text>_</xsl:text>
					<xsl:value-of
						select="lower-case(replace(@name,'_ID',''))" />
				</xsl:attribute>
				<xsl:attribute name="incremental">
					<xsl:text>true()</xsl:text>
				</xsl:attribute>
				<xf:label>
					<xsl:call-template name="proper-case">
						<xsl:with-param name="toconvert"
							select="replace(replace(@name,'_ID',''),'_',' ')" />
					</xsl:call-template>
				</xf:label>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:element>
		</div>
	</xsl:template>

	<!--  
		template to insert an xform select1 control 
	-->
	<xsl:template
		match="column[../foreign-key[reference/@local=current()/@name]]"
		mode="form" priority="3">
		<div class="xfselect1">
			<xsl:call-template name="select1-from-foreign-key">
				<xsl:with-param name="fk_node"
					select="../foreign-key[reference/@local=current()/@name]" />
			</xsl:call-template>
		</div>
	</xsl:template>

	<!-- 
		template to build an xforms select1 control
	-->
	<xsl:template name="select1-from-foreign-key">
		<xsl:param name="fk_node" />
		<xsl:element name="xf:select1">
			<xsl:attribute name="bind">
				<xsl:text>_</xsl:text>
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
			<xsl:element name="xf:itemset">
				<!--xsl:attribute name="ref">
					<xsl:text>instance(</xsl:text><xsl:value-of select="lower-case($fk_node[1]/@foreignTable)"/>
					</xsl:attribute-->
				<xsl:attribute name="nodeset">
					<xsl:text>instance('</xsl:text>
					<xsl:value-of
						select="lower-case($fk_node[1]/@foreignTable)" />
					<xsl:text>')/gml:featureMember/aatams:</xsl:text>
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

	<!-- 
		template to convert control labels to proper-case
	-->
	<xsl:template name="proper-case">
		<xsl:param name="toconvert" />
		<xsl:if test="string-length($toconvert) &gt; 0">
			<xsl:variable name="f" select="substring($toconvert, 1, 1)" />
			<xsl:variable name="s" select="substring($toconvert, 2)" />
			<xsl:value-of select="upper-case($f)" />
			<xsl:choose>
				<xsl:when test="contains($s,' ')">
					<xsl:value-of
						select="lower-case(substring-before($s,' '))" />
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="proper-case">
						<xsl:with-param name="toconvert"
							select="substring-after($s,' ')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="lower-case($s)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!--
		CUSTOMISATIONS 
	-->
	<xsl:template
		match="column[@name='STATUS_ID' or @primaryKey = 'true']" mode="wfs-t"
		priority="5">
		<!--excluded-->
	</xsl:template>

	<!-- special case -->
	<xsl:template match="column[@name='PROJECT_ROLE_PERSON_ID']"
		mode="wfs-t" priority="4">
		<xsl:element name="aatams:project_person_ref">
			<xsl:element name="aatams:project_person" />
		</xsl:element>
	</xsl:template>
	<!-- special case -->
	<xsl:template match="column[@name='DEPLOYMENT_ID']" mode="wfs-t"
		priority="4">
		<xsl:element name="aatams:installation_deployment_ref">
			<xsl:element name="aatams:installation_deployment" />
		</xsl:element>
	</xsl:template>
	<!-- special case , location is a gml:Point -->
	<xsl:template match="column[ends-with(@name,'LOCATION')]"
		mode="wfs-t" priority="4">
		<xsl:element name="aatams:{lower-case(@name)}">
			<gml:Point>
				<gml:pos />
			</gml:Point>
		</xsl:element>
	</xsl:template>

	<xsl:template match="foreign-key[@foreignTable = 'PROJECT_PERSON']"
		mode="wfs-t">
		<xf:instance id="project_person"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:project_person" />
		<xf:instance id="project"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:project" />
	</xsl:template>
	<xsl:template
		match="foreign-key[@foreignTable = 'INSTALLATION_DEPLOYMENT']"
		mode="wfs-t">
		<xf:instance id="installation"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:installation" />
		<xf:instance id="installation_deployment"
			src="{$wfs-url}?service=WFS&amp;version=1.1.0&amp;request=GetFeature&amp;namespace=xmlns(aatams=http://www.imos.org.au/aatams)&amp;typename=aatams:installation_deployment" />
	</xsl:template>

	<!--  location longitude and latitude bindings -->
	<xsl:template match="column[ends-with(@name,'LOCATION')]"
		mode="binding" priority="5">
		<xsl:variable name="name">
			<xsl:value-of select="lower-case(@name)" />
		</xsl:variable>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_<xsl:value-of select="$name" />_longitude</xsl:attribute>
			<xsl:attribute name="nodeset">instance('<xsl:value-of
					select="$name" />')/longitude</xsl:attribute>
			<xsl:attribute name="type">xsd:decimal</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
			<xsl:attribute name="constraint"><xsl:text>. &gt;= -180 and . &lt;= 180</xsl:text>
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_<xsl:value-of select="$name" />_latitude</xsl:attribute>
			<xsl:attribute name="nodeset">instance('<xsl:value-of
					select="$name" />')/latitude</xsl:attribute>
			<xsl:attribute name="type">xsd:decimal</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
			<xsl:attribute name="constraint"><xsl:text>. &gt;= -90 and . &lt;= 90</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<!--  not interested in status field -->
	<xsl:template
		match="column[@name = 'STATUS_ID' or @primaryKey = 'true']"
		mode="binding" priority="5">
		<!-- excluded -->
	</xsl:template>
	<xsl:template match="column[@name = 'PROJECT_ROLE_PERSON_ID']"
		mode="binding" priority="4">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_project</xsl:attribute>
			<xsl:attribute name="nodeset">instance('subf')/project_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_project_person</xsl:attribute>
			<xsl:attribute name="nodeset">instance('subf')/project_person_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="column[@name = 'INSTALLATION_ID']"
		mode="binding" priority="4">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_installation</xsl:attribute>
			<xsl:attribute name="nodeset">instance('subf')/installation_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_installation_station</xsl:attribute>
			<xsl:attribute name="nodeset">instance('subf')/installation_station_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<xsl:template match="column[@name = 'DEPLOYMENT_ID']" mode="binding"
		priority="4">
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_installation</xsl:attribute>
			<xsl:attribute name="nodeset">instance('subf')/installation_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
		</xsl:element>
		<xsl:element name="xf:bind">
			<xsl:attribute name="id">_installation_deployment</xsl:attribute>
			<xsl:attribute name="nodeset">instance('subf')/installation_deployment_id</xsl:attribute>
			<xsl:attribute name="type">xsd:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:apply-templates select="@required" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>

	<xsl:template match="foreign-key[@foreignTable = 'PROJECT_PERSON']"
		mode="selected-subfeature-ids">
		<project_id xmlns="" />
		<project_person_id xmlns="" />
	</xsl:template>

	<xsl:template
		match="foreign-key[@foreignTable = 'INSTALLATION_DEPLOYMENT']"
		mode="selected-subfeature-ids">
		<installation_id xmlns="" />
		<installation_deployment_id xmlns="" />
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

	<!-- special cases -->
	<xsl:template match="foreign-key[@foreignTable = 'PROJECT_PERSON']"
		mode="default-value">
		<!-- convert to project and person_role -->
		<xf:setvalue ref="instance('subf')/project_id"
			value="instance('project')/gml:featureMember/aatams:project[1]/@gml:id" />
		<xf:setvalue ref="instance('subf')/project_person_id"
			value="instance('project_person')/gml:featureMember/aatams:project_person[aatams:project_fid=instance('subf')/project_id][1]/@gml:id" />
	</xsl:template>
	<xsl:template
		match="foreign-key[@foreignTable = 'INSTALLATION_DEPLOYMENT']"
		mode="default-value">
		<xf:setvalue ref="instance('subf')/installation_id"
			value="instance('installation')/gml:featureMember/aatams:installation[1]/@gml:id" />
		<xf:setvalue ref="instance('subf')/installation_deployment_id"
			value="instance('installation_deployment')/gml:featureMember/aatams:installation_deployment[aatams:installation_fid=instance('subf')/installation_id][1]/@gml:id" />
	</xsl:template>
	<!-- remove any non-mandatory numeric fields if no value entered -->
	<xsl:template
		match="column[@required='false' and not(../foreign-key[reference/@local=current()/@name]) and matches(@type,'INTEGER|DECIMAL|TIMESTAMP|DATE')]"
		mode="submission">
		<xsl:element name="xf:delete">
			<xsl:attribute name="nodeset">
				<xsl:text>instance('wfst')//aatams:</xsl:text>
				<xsl:value-of select="lower-case(@name)" />
				<xsl:text>[.='']</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>

	<!-- set and longitude and latitudes -->
	<xsl:template match="column[ends-with(@name,'LOCATION')]"
		mode="submission">
		<xsl:variable name="name">
			<xsl:value-of select="lower-case(@name)" />
		</xsl:variable>
		<xsl:element name="xf:setvalue">
			<!-- where to put it -->
			<xsl:attribute name="ref">
				<xsl:text>instance('wfst')//aatams:</xsl:text>
				<xsl:value-of select="$name" />
				<xsl:text>/gml:Point/gml:pos</xsl:text>
			</xsl:attribute>
			<!-- what to put there, a concatenation of the entered longitude and latitude -->
			<xsl:attribute name="value">
				<xsl:text>concat(instance('</xsl:text>
				<xsl:value-of select="$name" />
				<xsl:text>')/longitude,' ',instance('</xsl:text>
				<xsl:value-of select="$name" />
				<xsl:text>')/latitude)</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>

	<xsl:template
		match="column[@name = 'STATUS_ID' or @primaryKey = 'true']"
		mode="form" priority="5">
		<!-- excluded -->
	</xsl:template>
	<xsl:template
		match="column[@name = 'COMMENTS' or @name = 'CC_EMAIL_ADDRESSES']"
		mode="form">
		<div class="xftextarea">
			<xsl:element name="xf:textarea">
				<xsl:attribute name="bind">
					<xsl:text>_</xsl:text>
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
		</div>
	</xsl:template>

	<xsl:template match="column[@name = 'PROJECT_ROLE_PERSON_ID']"
		mode="form" priority="4">
		<!-- convert to project and person_role -->
		<div class="dependant-selects">
			<div class="xfselect1">
				<xf:select1 bind="_project" appearance="minimal"
					incremental="true()">
					<xf:label>Project</xf:label>
					<xf:itemset
						nodeset="instance('project')/gml:featureMember/aatams:project">
						<xf:value ref="@gml:id" />
						<xf:label ref="aatams:name" />
					</xf:itemset>
					<xf:action ev:event="xforms-value-changed">
						<xf:setvalue
							ref="instance('subf')/project_person_id" value="" />
					</xf:action>
					<xsl:call-template name="help">
						<xsl:with-param name="key">
							<xsl:value-of select="@name" />
						</xsl:with-param>
					</xsl:call-template>
				</xf:select1>
			</div>
			<div class="xfselect1">
				<xf:select1 bind="_project_person" appearance="minimal"
					incremental="true()">
					<xf:label>Person(Role)</xf:label>
					<xf:itemset
						nodeset="instance('project_person')/gml:featureMember/aatams:project_person[aatams:project_fid=instance('subf')/project_id]">
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
		</div>
	</xsl:template>
	<xsl:template match="column[@name = 'DEPLOYMENT_ID']" mode="form"
		priority="4">
		<!-- convert to project and person_role -->
		<div class="dependant-selects">
			<div class="xfselect1">
				<xf:select1 bind="_installation" appearance="minimal"
					incremental="true()">
					<xf:label>Installation</xf:label>
					<xf:itemset
						nodeset="instance('installation')/gml:featureMember/aatams:installation">
						<xf:value ref="@gml:id" />
						<xf:label ref="aatams:name" />
					</xf:itemset>
					<xf:action ev:event="xforms-value-changed">
						<xf:setvalue
							ref="instance('subf')/installation_deployment_id" value="" />
					</xf:action>
					<xsl:call-template name="help">
						<xsl:with-param name="key">
							<xsl:value-of select="@name" />
						</xsl:with-param>
					</xsl:call-template>
				</xf:select1>
			</div>
			<div class="xfselect1">
				<xf:select1 bind="_installation_deployment"
					appearance="minimal" incremental="true()">
					<xf:label>Deployment</xf:label>
					<xf:itemset
						nodeset="instance('installation_deployment')/gml:featureMember/aatams:installation_deployment[aatams:installation_fid=instance('subf')/installation_id]">
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
		</div>
	</xsl:template>
	<xsl:template
		match="column[@name = 'INSTALLATION_ID' and following-sibling::column[1]/@name = 'STATION_ID']"
		mode="form" priority="4">
		<!-- convert to installation and station -->
		<div class="dependant-selects">
			<div class="xfselect1">
				<xf:select1 bind="_installation" appearance="minimal"
					incremental="true()">
					<xf:label>Installation</xf:label>
					<xf:itemset
						nodeset="instance('installation')/gml:featureMember/aatams:installation">
						<xf:value ref="@gml:id" />
						<xf:label ref="aatams:name" />
					</xf:itemset>
					<xf:action ev:event="xforms-value-changed">
						<xf:setvalue ref="instance('subf')/station_id"
							value="" />
					</xf:action>
				</xf:select1>
			</div>
			<div class="xfselect1">
				<xf:select1 bind="_station" appearance="minimal"
					incremental="true()">
					<xf:label>Station</xf:label>
					<xf:itemset
						nodeset="instance('station')/gml:featureMember/aatams:station[aatams:installation_fid=instance('subf')/installation_id]">
						<xf:value ref="@gml:id" />
						<xf:label ref="aatams:name" />
					</xf:itemset>
					<xf:action ev:event="xforms-value-changed">
						<xf:setvalue
							ref="instance('wfst')//aatams:longitude"
							value="instance('station')/gml:featureMember/aatams:station[@gml:id=instance('subf')/station_id]/aatams:longitude" />
						<xf:setvalue
							ref="instance('wfst')//aatams:latitude"
							value="instance('station')/gml:featureMember/aatams:station[@gml:id=instance('subf')/station_id]/aatams:latitude" />
					</xf:action>
				</xf:select1>
			</div>
		</div>
	</xsl:template>
	<xsl:template
		match="column[@name = 'STATION_ID' and preceding-sibling::column[1]/@name = 'INSTALLATION_ID']"
		mode="form" priority="4">
		<!-- included in previous installation -->
	</xsl:template>
	<!--  latitude and longitude -->
	<xsl:template match="column[ends-with(@name,'LOCATION')]"
		mode="form" priority="4">
		<div class="xfinput">
			<xsl:element name="xf:input">
				<xsl:attribute name="bind">
					<xsl:value-of select="lower-case(@name)" />
					<xsl:text>_longitude</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="incremental">
					<xsl:text>true()</xsl:text>
				</xsl:attribute>
				<xf:label>
					<xsl:if test="not(@name = 'LOCATION')">
						<xsl:call-template name="proper-case">
							<xsl:with-param name="toconvert"
								select="replace(@name,'_',' ')" />
						</xsl:call-template>
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:text>Longitude</xsl:text>
				</xf:label>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:element>
		</div>
		<div class="xfinput">
			<xsl:element name="xf:input">
				<xsl:attribute name="bind">
					<xsl:value-of select="lower-case(@name)" />
					<xsl:text>_latitude</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="incremental">
					<xsl:text>true()</xsl:text>
				</xsl:attribute>
				<xf:label>
					<xsl:if test="not(@name = 'LOCATION')">
						<xsl:call-template name="proper-case">
							<xsl:with-param name="toconvert"
								select="replace(@name,'_',' ')" />
						</xsl:call-template>
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:text>Latitude</xsl:text>
				</xf:label>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:element>
		</div>
	</xsl:template>

</xsl:stylesheet>
