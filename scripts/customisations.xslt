<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:aatams="http://www.imos.org.au/aatams" 
xmlns:xf="http://www.w3.org/2002/xforms"
xmlns:ev="http://www.w3.org/2001/xml-events"
>

<!--
		CUSTOMISATIONS 
	-->
	<xsl:template
		match="column[@name='STATUS_ID' or @primaryKey = 'true']" mode="wfs-t"
		priority="5">
		<!--excluded-->
	</xsl:template>

	<!-- special case -->
	<!--xsl:template match="column[@name='PROJECT_ROLE_PERSON_ID']"
		mode="wfs-t" priority="4">
		<xsl:element name="aatams:project_person_ref">
		<xsl:element name="aatams:project_person" />
		</xsl:element>
		</xsl:template-->
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
		<xsl:element
			name="aatams:{concat($namespace,lower-case(@name))}">
			<gml:Point>
				<gml:pos />
			</gml:Point>
		</xsl:element>
	</xsl:template>

	<xsl:template match="foreign-key[@foreignTable = 'PROJECT_PERSON']"
		mode="wfs-t">
		<xf:instance id="project_person"
			src="{concat($get_feature_url,'aatams:project_person')}" />
		<xf:instance id="project"
			src="{concat($get_feature_url,'aatams:project')}" />
	</xsl:template>

	<xsl:template
		match="foreign-key[@foreignTable = 'INSTALLATION_DEPLOYMENT']"
		mode="wfs-t">
		<xf:instance id="installation"
			src="{concat($get_feature_url,'aatams:installation')}" />
		<xf:instance id="installation_deployment"
			src="{concat($get_feature_url,'aatams:installation_deployment')}" />
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
				<xsl:text>instance('wfs-t')//aatams:</xsl:text>
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
				<xsl:text>instance('wfs-t')//aatams:</xsl:text>
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
	</xsl:template>

	<xsl:template match="column[@name = 'PROJECT_ROLE_PERSON_ID']"
		mode="form" priority="4">
		<!-- convert to project and person_role -->
		<div class="dependant-selects">
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
						ref="instance('subf')/project_person_id"
						value="instance('inst_project_person')/gml:featureMember/aatams:project_person[aatams:project_fid=instance('inst_subfeatures')/project_id][1]/@gml:id" />
				</xf:action>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
			</xf:select1>
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
	</xsl:template>
	<xsl:template match="column[@name = 'DEPLOYMENT_ID']" mode="form"
		priority="4">
		<!-- convert to project and person_role -->
		<div class="dependant-selects">
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
	</xsl:template>
	<xsl:template
		match="column[@name = 'INSTALLATION_ID' and following-sibling::column[1]/@name = 'STATION_ID']"
		mode="form" priority="4">
		<!-- convert to installation and station -->
		<div class="dependant-selects">
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
						ref="instance('wfs-t')//aatams:longitude"
						value="instance('station')/gml:featureMember/aatams:station[@gml:id=instance('subf')/station_id]/aatams:longitude" />
					<xf:setvalue
						ref="instance('wfs-t')//aatams:latitude"
						value="instance('station')/gml:featureMember/aatams:station[@gml:id=instance('subf')/station_id]/aatams:latitude" />
				</xf:action>
			</xf:select1>
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
	
	</xsl:stylesheet>
