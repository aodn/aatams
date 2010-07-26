<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:emii="http://www.imos.org.au/emii" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:wfs="http://www.opengis.net/wfs" xmlns:ogc="http://www.opengis.net/ogc" xmlns:ows="http://www.opengis.net/ows" xmlns:gml="http://www.opengis.net/gml" xmlns:fn="http://www.imos.org.au/functions">
	<xsl:output name="xml" method="xml" encoding="UTF-8" indent="yes" />
	<xsl:include href="help.xslt" />
	<xsl:include href="globals_ownedBy.xslt" />
	<xsl:include href="over-rides.xslt" />
	<xsl:template match="/">
		<xsl:for-each select="//table">
			<xsl:variable name="feature-name">
				<xsl:value-of select="lower-case(@name)" />
			</xsl:variable>
            <xsl:variable name="feature-title">
                <xsl:apply-templates select="@name"/>
			</xsl:variable>
			<xsl:value-of select="$feature-name" />
			<xsl:result-document href="{concat('file:///',$output_path,'/view_all_',$feature-name,'.xml')}" format="xml">
				<xsl:if test="$is_xsltforms='true'">
					<xsl:processing-instruction name="xml-stylesheet">
						<xsl:text>href="</xsl:text>
						<xsl:value-of select="$xsltforms_path" />
						<xsl:text>" type="text/xsl"</xsl:text>
					</xsl:processing-instruction>
				</xsl:if>
				<html style="height:100%;">
					<head>
						<title>
							<xsl:value-of select="$title" />
						</title>
						<xsl:call-template name="models" />
					</head>
					<body style="height:100%;">
						<div class="horiz-tab-menu">
							<span id="tab-1" class="active-tab">
								<xf:trigger appearance="minimal">
									<xf:label>View</xf:label>
									<xf:action ev:event="DOMActivate">
										<xf:toggle case="view-all" />
										<xf:load resource="javascript:selectTab('tab-1')" />
									</xf:action>
								</xf:trigger>
							</span>
							<span id="tab-2">
								<xf:trigger appearance="minimal">
									<xf:label>Create</xf:label>
									<xf:action ev:event="DOMActivate">
										<xf:toggle case="create" />
										<xf:load resource="javascript:selectTab('tab-2')" />
									</xf:action>
								</xf:trigger>
							</span>
							<span id="tab-3">
								<xf:trigger appearance="minimal">
									<xf:label>Edit</xf:label>
									<xf:action ev:event="DOMActivate">
										<xf:toggle case="edit" />
										<xf:load resource="javascript:selectTab('tab-3')" />
									</xf:action>
								</xf:trigger>
							</span>
							<span id="tab-4">
								<xf:trigger appearance="minimal">
									<xf:label>Delete</xf:label>
									<xf:action ev:event="DOMActivate">
										<xf:toggle case="delete" />
										<xf:load resource="javascript:selectTab('tab-4')" />
									</xf:action>
								</xf:trigger>
							</span>
						</div>
						<div class="horiz-tab-contents">
							<xf:switch>
								<xf:case id="view-all" selected="true">
									<div class="tab-content">
										<xsl:call-template name="view" />
									</div>
								</xf:case>
								<xf:case id="create">
									<div class="tab-content">
										<xsl:call-template name="create" />
									</div>
								</xf:case>
								<xf:case id="edit">
									<div class="tab-content">
										<xsl:call-template name="edit" />
									</div>
								</xf:case>
								<xf:case id="delete">
									<div class="tab-content">
										<xsl:call-template name="delete" />
									</div>
								</xf:case>
							</xf:switch>
						</div>
					</body>
				</html>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
	<!-- 
		template to build the models 
	-->
	<xsl:template name="models">
		<!-- add the primary model -->
		<xsl:call-template name="model">
			<xsl:with-param name="model-number" select="number(1)" />
		</xsl:call-template>
		<!-- add foreign subfeature prototypes as separate models -->
		<xsl:variable name="lookups" select="/database/table[@name=current()/foreign-key[@parentOwnsChild='false']/@foreignTable]" />
		<xsl:for-each select="$lookups">
			<xsl:call-template name="model">
				<xsl:with-param name="model-number" select="position()+1" />
			</xsl:call-template>
		</xsl:for-each>
		<!-- if at top level look for maxOccurs='unbounded' subfeatures of this feature  -->
		<xsl:variable name="table" select="." />
		<xsl:variable name="model-number" select="count($lookups)+1" />
		<xsl:for-each select="//foreign-key[@foreignTable = current()/@name and @parentOwnsChild = 'true']/parent::table">
			<xsl:choose>
				<!-- all primary key columns are foreign-keys -->
				<xsl:when test="count(current()/column[@primaryKey='true' and not(current()/foreign-key/reference/@local = @name)]) = 0">
					<xsl:call-template name="n_child_model">
						<xsl:with-param name="model-number" select="$model-number + position()" />
						<xsl:with-param name="table" select="$table" />
						<!-- find the table referenced by the other foreign-key on the link table -->
						<xsl:with-param name="referenced_table" select="/database/table[@name=current()/foreign-key/@foreignTable and not(@name=$table/@name)][1]" />
						<xsl:with-param name="link_table" select="." />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="n_child_model">
						<xsl:with-param name="model-number" select="$model-number + position()" />
						<xsl:with-param name="table" select="$table" />
						<xsl:with-param name="referenced_table" select="." />
						<xsl:with-param name="link_table" select="." />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<!-- add the feature edit model -->
		<xsl:call-template name="edit_model" />
		<!-- add the feature deletion model -->
		<xsl:call-template name="delete_model" />
	</xsl:template>
	<!-- 
    	template to build a model from a <table> 
    -->
	<xsl:template name="model">
		<xsl:param name="model-number" />
		<xf:model id="{concat('model',$model-number)}">
			<!-- add the transaction instance (sent to wfs) -->
			<xsl:call-template name="wfs-transaction">
				<xsl:with-param name="model-number" select="$model-number" />
			</xsl:call-template>
			<!-- add an instance to receive server response -->
			<xsl:call-template name="wfs-response">
				<xsl:with-param name="model-number" select="$model-number" />
			</xsl:call-template>
			<!-- add the subfeature lists needed for select1 controls -->
			<xsl:if test="$model-number=1">
				<xsl:call-template name="subfeature-lists" />
			</xsl:if>
			<!-- add the model node bindings -->
			<xsl:call-template name="bindings" />
			<!-- add processing of submission success or failure -->
			<xsl:call-template name="process-response">
				<xsl:with-param name="model-number" select="$model-number" />
			</xsl:call-template>
			<!-- add the xform submission details -->
			<xsl:call-template name="submission">
				<xsl:with-param name="model-number" select="$model-number" />
			</xsl:call-template>
		</xf:model>
	</xsl:template>
	<!-- 
		template to build the paged table view
	-->
	<xsl:template name="view">
		<xsl:variable name="feature-instance" select="concat(&quot;instance('&quot;,lower-case(@name),&quot;')&quot;)" />
		<table class="datatable">
			<thead>
				<!-- build a headings nodeset -->
				<xsl:variable name="headings">
					<xsl:call-template name="find_view_all_table_column_headings">
						<xsl:with-param name="group_name" select="./@name" />
						<xsl:with-param name="table" select="." />
						<xsl:with-param name="depth" select="number(1)" />
					</xsl:call-template>
				</xsl:variable>
				<!-- find how many heading rows needed -->
				<xsl:variable name="max_depth" select="max($headings/descendant-or-self::*/count(ancestor-or-self::*))" />
				<!-- build the table from the nodeset -->
				<xsl:for-each select="2 to $max_depth">
					<tr>
						<xsl:if test=". = 2">
							<th colspan="1" rowspan="{$max_depth - 1}">No.</th>
						</xsl:if>
						<xsl:call-template name="display_view_all_table_column_headings">
							<xsl:with-param name="heading" select="$headings" />
							<xsl:with-param name="max_depth" select="$max_depth" />
							<xsl:with-param name="target_depth" select="." />
							<xsl:with-param name="depth" select="number(1)" />
						</xsl:call-template>
					</tr>
				</xsl:for-each>
			</thead>
			<tbody>
				<xf:repeat id="rows">
					<xsl:attribute name="nodeset">
						<xsl:value-of select="$feature-instance" />
						<xsl:text>/gml:featureMember[position()&gt;instance('grid1')/@pos and position()&lt;=instance('grid1')/@pos+instance('grid1')/@stride]/*</xsl:text>
					</xsl:attribute>
					<tr>
						<td>
							<xf:output ref="instance('grid1')/@pos+position()" />
						</td>
						<xsl:call-template name="view_all_table_column_data_refs">
							<xsl:with-param name="table" select="." />
							<xsl:with-param name="path" />
							<xsl:with-param name="depth" select="number(1)" />
						</xsl:call-template>
					</tr>
				</xf:repeat>
			</tbody>
		</table>
		<xf:repeat id="pages">
			<xsl:attribute name="nodeset">
				<xsl:value-of select="$feature-instance" />
				<xsl:text>/gml:featureMember[position() mod instance('grid1')/@stride = 0]</xsl:text>
			</xsl:attribute>
			<xf:trigger>
				<xf:label>
					<xf:output ref="position()" />
				</xf:label>
				<xf:action ev:event="DOMActivate">
					<xf:setvalue ev:event="DOMActivate" ref="instance('grid1')/@pos" value="(index('pages')-1)*instance('grid1')/@stride" />
				</xf:action>
			</xf:trigger>
		</xf:repeat>
	</xsl:template>
	<!--
		recursive template to find all column headings
	-->
	<xsl:template name="find_view_all_table_column_headings">
		<xsl:param name="group_name" />
		<xsl:param name="table" />
		<xsl:param name="depth" />
		<xsl:if test="$depth &lt;= $max_depth">
			<table name="{$group_name}">
				<!-- add the gml id if not a simple code table -->
				<xsl:if test="$depth = 1 or count($table/column) &gt; 2">
					<column name="id" />
				</xsl:if>
                <xsl:for-each select="$table/column">
                    <xsl:variable name="fk" select="../foreign-key[reference/@local=current()/@name]"/>
					<xsl:choose>
                        <xsl:when test="$fk">
                            <xsl:if test="$fk[@parentOwnsChild='false']">
							    <xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
							    <xsl:call-template name="find_view_all_table_column_headings">
								    <xsl:with-param name="group_name" select="replace(lower-case(@name),'_id$','')" />
								    <xsl:with-param name="table" select="//table[@name=$foreign_table_name]" />
								    <xsl:with-param name="depth" select="$depth + 1" />
                                </xsl:call-template>
                            </xsl:if>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="@primaryKey = 'false'">
								<xsl:choose>
									<!-- put all columns in if parent feature -->
									<xsl:when test="$depth = 1">
										<column name="{lower-case(@name)}" />
									</xsl:when>
									<xsl:otherwise>
										<!--xsl:if test="../unique[@name='UNIQUE_INDEX']/unique-column[@name=current()/@name]"-->
										<xsl:if test="@required = 'true'">
											<column name="{lower-case(@name)}" />
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>
	<!--
		recursive template to display all column headings
	-->
	<xsl:template name="display_view_all_table_column_headings">
		<xsl:param name="heading" />
		<xsl:param name="max_depth" />
		<xsl:param name="target_depth" />
		<xsl:param name="depth" />
		<xsl:choose>
			<xsl:when test="$depth &lt; $target_depth">
				<xsl:for-each select="$heading/*[count(child::*)&gt;0]">
					<xsl:call-template name="display_view_all_table_column_headings">
						<xsl:with-param name="heading" select="." />
						<xsl:with-param name="max_depth" select="$max_depth" />
						<xsl:with-param name="target_depth" select="$target_depth" />
						<xsl:with-param name="depth" select="$depth + 1" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="$depth = $target_depth">
				<xsl:for-each select="$heading/*">
					<th>
						<xsl:attribute name="colspan">
							<xsl:choose>
								<xsl:when test="count(child::*)=0">
									<xsl:text>1</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="count(descendant::*[count(child::*)=0])" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<xsl:attribute name="rowspan">
							<xsl:choose>
								<xsl:when test="name()='table'">
									<xsl:text>1</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$max_depth - $depth + 1" />
								</xsl:otherwise>
							</xsl:choose>
                        </xsl:attribute>
                        <xsl:apply-templates select="@name"/>
					</th>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise />
		</xsl:choose>
	</xsl:template>
	<!--
		recursive template to add column data refs
	-->
	<xsl:template name="view_all_table_column_data_refs">
		<xsl:param name="table" />
		<xsl:param name="path" />
		<xsl:param name="depth" />
		<xsl:if test="$depth &lt;= $max_depth">
			<!-- add the gml id if not a simple code table -->
			<xsl:if test="$depth = 1 or count($table/column)&gt;2">
				<td>
					<xf:output ref="{concat('substring-after(',$path,'@gml:id',&quot;,'.')&quot;)}" />
				</td>
			</xsl:if>
            <xsl:for-each select="$table/column">
                <xsl:variable name="fk" select="../foreign-key[reference/@local=current()/@name]"/>
				<xsl:choose>
                    <xsl:when test="$fk">
                        <xsl:if test="$fk[@parentOwnsChild = 'false']">
						    <xsl:variable name="foreign_table_name" select="$fk/@foreignTable" />
						    <xsl:call-template name="view_all_table_column_data_refs">
							    <xsl:with-param name="table" select="//table[@name=$foreign_table_name]" />
							    <xsl:with-param name="path" select="concat($path,$namespace,replace(lower-case(@name),'_id$',''),'_ref/',$namespace,lower-case($foreign_table_name),'/')" />
							    <xsl:with-param name="depth" select="$depth + 1" />
                            </xsl:call-template>
                        </xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="@primaryKey = 'false'">
							<xsl:choose>
								<!-- put all columns in if parent feature -->
								<xsl:when test="$depth = 1">
									<td>
										<xsl:attribute name="class">
											<xsl:choose>
												<xsl:when test="contains(upper-case(@type),'CHAR')">text</xsl:when>
												<xsl:otherwise>numeric</xsl:otherwise>
											</xsl:choose>
										</xsl:attribute>
										<xf:output ref="{concat($path,$namespace,lower-case(@name))}" />
									</td>
								</xsl:when>
								<xsl:otherwise>
									<!--xsl:if test="../unique[@name='UNIQUE_INDEX']/unique-column[@name=current()/@name]"-->
									<xsl:if test="@required = 'true'">
										<td>
											<xsl:attribute name="class">
												<xsl:choose>
													<xsl:when test="contains(upper-case(@type),'CHAR')">text</xsl:when>
													<xsl:otherwise>numeric</xsl:otherwise>
												</xsl:choose>
											</xsl:attribute>
											<xf:output ref="{concat($path,$namespace,lower-case(@name))}" />
										</td>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<!--
        
    -->
	<xsl:template name="n_child_model">
		<xsl:param name="model-number" />
		<xsl:param name="table" />
		<xsl:param name="referenced_table" />
		<xsl:param name="link_table" />
		<xsl:variable name="link_name" select="concat($namespace,lower-case($link_table/@name))" />
		<xsl:variable name="ref_name" select="concat($namespace,lower-case($referenced_table/@name))" />
		<xsl:for-each select="$referenced_table">
			<xf:model id="{concat('model',$model-number)}">
				<!-- add the transaction instance (sent to wfs) -->
				<xsl:call-template name="wfs-transaction">
					<xsl:with-param name="model-number" select="$model-number" />
				</xsl:call-template>
				<!-- add an instance to receive server response -->
				<xsl:call-template name="wfs-response">
					<xsl:with-param name="model-number" select="$model-number" />
				</xsl:call-template>
				<!-- add an instance to hold prototype reference to subfeature (direct or indirect) -->
                <xsl:variable name="child_instance_name" select="if(not($link_table = $referenced_table)) then 
                    lower-case($link_table/@name) else lower-case($referenced_table/@name)" />
				<xsl:choose>
					<xsl:when test="not($link_table = $referenced_table)">
						<xf:instance id="{concat($child_instance_name,$model-number)}">
							<dummy xmlns="" id="">
								<xsl:element name="{concat($link_name,'_ref')}">
									<xsl:element name="{$link_name}">
										<xsl:element name="{concat($ref_name,'_ref')}">
											<xsl:element name="{$ref_name}" />
										</xsl:element>
									</xsl:element>
								</xsl:element>
							</dummy>
						</xf:instance>
					</xsl:when>
					<xsl:otherwise>
						<xf:instance id="{concat($child_instance_name,$model-number)}">
							<dummy xmlns="" id="">
								<xsl:element name="{concat($ref_name,'_ref')}">
									<xsl:element name="{$ref_name}" />
								</xsl:element>
							</dummy>
						</xf:instance>
					</xsl:otherwise>
				</xsl:choose>
				<!-- add the model node bindings -->
				<xsl:call-template name="bindings" />
				<!-- add processing of submission success or failure -->
				<xsl:variable name="feature-name" select="concat($namespace,lower-case($referenced_table/@name))" />
				<xsl:variable name="fid" select="concat('instance(',&quot;'resp&quot;,$model-number,&quot;')//ogc:FeatureId/@fid&quot;)" />
				<xsl:variable name="nodeset" select="concat(&quot;instance('&quot;,lower-case($referenced_table/@name),&quot;')/gml:featureMember/&quot;,$feature-name)" />
				<xsl:variable name="origin" select="concat('instance(',&quot;'trans&quot;,$model-number,&quot;')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/&quot;,$feature-name)" />
				<xf:bind id="{concat('error',$model-number)}" nodeset="{concat('instance(',&quot;'resp&quot;,$model-number,&quot;')//ows:ExceptionText&quot;)}" calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)" type="xf:string" />
				<xf:bind id="{concat('success',$model-number)}" nodeset="{$fid}" type="xf:string" />
				<xf:action ev:event="clear-response">
					<xf:delete nodeset="{$fid}" />
				</xf:action>
				<!-- copies the current subfeature (just created) to the instance on which select1 itemset is built -->
				<xf:action ev:event="process-response" if="{$fid}">
					<xf:setvalue ref="{concat($origin,'/@gml:id')}" value="{$fid}" />
					<xf:insert nodeset="{$nodeset}" origin="{$origin}" />
					<xf:setvalue ref="{concat(&quot;instance('&quot;,$child_instance_name,&quot;')/@id&quot;)}" value="{$fid}" />
					<xf:dispatch name="xforms-revalidate" target="model1" />
					<xf:toggle case="home" />
				</xf:action>
				<!-- add the xform submission details -->
                <!--xsl:call-template name="submission">
					<xsl:with-param name="model-number" select="$model-number" />
				</xsl:call-template-->
			</xf:model>
		</xsl:for-each>
	</xsl:template>
	<!--
		template to create xml base transaction for submission to WFS 
	-->
	<xsl:template name="wfs-transaction">
		<xsl:param name="model-number" />
		<xf:instance id="{concat('trans',$model-number)}">
			<wfs:Transaction version="1.1.0" service="WFS">
				<wfs:Insert>
					<wfs:FeatureCollection>
						<gml:featureMember>
							<xsl:element name="{concat($namespace,lower-case(@name))}">
								<xsl:attribute name="gml:id">NULL</xsl:attribute>
								<!-- handle each column -->
								<xsl:for-each select="column">
									<!-- is it a foreign-key -->
									<xsl:variable name="fk" select="../foreign-key[reference/@local=current()/@name]" />
									<xsl:choose>
                                        <xsl:when test="$fk">
                                            <xsl:if test="$fk[@parentOwnsChild='false']">
											    <xsl:element name="{concat($namespace, replace(lower-case(@name),'_id$',''), '_ref')}">
												    <xsl:element name="{concat($namespace,lower-case($fk/@foreignTable))}">
													    <xsl:attribute name="gml:id">NULL</xsl:attribute>
												    </xsl:element>
                                                </xsl:element>
                                            </xsl:if>
										</xsl:when>
										<xsl:when test="@primaryKey='true' and $include_pkeys_in_form = 'false'">
										</xsl:when>
										<xsl:otherwise>
											<!-- simple non-foreign-key -->
											<xsl:element name="{concat($namespace,lower-case(@name))}" />
										</xsl:otherwise>
									</xsl:choose>
								</xsl:for-each>
							</xsl:element>
						</gml:featureMember>
					</wfs:FeatureCollection>
				</wfs:Insert>
			</wfs:Transaction>
		</xf:instance>
		<xsl:if test="$model-number = 1">
			<xf:instance id="temp1">
				<dummy xmlns="">
					<xsl:element name="{concat($namespace,lower-case(@name))}" />
				</dummy>
			</xf:instance>
		</xsl:if>
	</xsl:template>
	<!-- 
    	template to build an feature editing model> 
    -->
	<xsl:template name="edit_model">
		<xsl:variable name="feature_name" select="concat($namespace,lower-case(@name))" />
		<xf:model id="edit_model">
			<xf:instance id="edit_id1">
				<dummy xmlns="">
					<id />
				</dummy>
			</xf:instance>
			<xf:instance id="edit_find1">
				<wfs:GetFeature>
					<wfs:Query typeName="pre4:{lower-case(@name)}">
						<ogc:Filter>
							<ogc:GmlObjectId gml:id="" />
						</ogc:Filter>
						<xsl:element name="{$namespace}dummy" />
					</wfs:Query>
				</wfs:GetFeature>
			</xf:instance>
			<xf:instance id="edit_view1">
				<wfs:FeatureCollection>
					<gml:featureMember>
						<xsl:element name="{$feature_name}" />
					</gml:featureMember>
				</wfs:FeatureCollection>
			</xf:instance>
			<xf:instance id="edit_edit1">
				<wfs:Transaction version="1.1.0" service="WFS">
					<!--  leave of the feature namespace and let deegree guess, due to xsltforms issue -->
					<wfs:Update typeName="pre4:{lower-case(@name)}">
						<wfs:Property>
							<wfs:Name />
							<wfs:Value />
						</wfs:Property>
						<ogc:Filter>
							<ogc:GmlObjectId gml:id="" />
						</ogc:Filter>
						<xsl:element name="{$namespace}dummy" />
					</wfs:Update>
				</wfs:Transaction>
			</xf:instance>
			<xf:instance id="edit_save">
				<dummy xmlns="">
					<wfs:Update />
				</dummy>
			</xf:instance>
			<xf:instance id="edit_resp">
				<dummy xmlns="" />
			</xf:instance>
			<xf:instance id="edit_msg">
				<dummy xmlns="" message="" />
			</xf:instance>
			<xf:bind id="edit_success" nodeset="instance('edit_msg')/@message" calculate="choose(instance('edit_resp')/wfs:TransactionSummary/wfs:totalUpdated=0,'NO RECORD UPDATED','')" type="xf:string" />
			<xf:action ev:event="clear-response">
				<xf:delete nodeset="instance('edit_resp')/*" />
			</xf:action>
			<xf:action ev:event="process-response" if="instance('edit_resp')//ogc:FeatureId/@fid">
			</xf:action>
			<xf:submission id="edit_submit1" ref="instance('edit_find1')" method="post" action="../services" replace="instance" instance="edit_view1">
				<xf:setvalue ev:event="xforms-submit" ref="instance('edit_find1')//ogc:GmlObjectId/@gml:id" value="{concat(&quot;concat('&quot;,lower-case(@name),&quot;.',instance('edit_id1')//id)&quot;)}" />
				<xf:setvalue ev:event="xforms-submit" ref="instance('edit_edit1')//ogc:GmlObjectId/@gml:id" value="{concat(&quot;concat('&quot;,lower-case(@name),&quot;.',instance('edit_id1')//id)&quot;)}" />
				<xf:action ev:event="xforms-submit-done" if="instance('edit_view1')/gml:featureMember">
					<xf:toggle case="do_edit" />
				</xf:action>
			</xf:submission>
			<xf:submission id="edit_submit2" ref="instance('edit_edit1')" method="post" action="../services" replace="instance" instance="edit_resp">
				<xf:action ev:event="xforms-submit">
					<xf:delete nodeset="instance('edit_edit1')//wfs:Property[wfs:Name = '']" />
				</xf:action>
				<xf:action ev:event="xforms-submit-done" if="instance('edit_resp')/wfs:TransactionSummary/wfs:totalUpdated=1">
					<xf:reset model="edit_model" />
					<xf:revalidate model="edit_model" />
					<xf:toggle case="select_edit" />
				</xf:action>
			</xf:submission>
		</xf:model>
	</xsl:template>
	<!--
    	template to build a model for editing feature 
    -->
	<xsl:template name="delete_model">
		<xsl:variable name="feature_name" select="concat($namespace,lower-case(@name))" />
		<xf:model id="delete_model">
			<xf:instance id="delete_id1">
				<dummy xmlns="">
					<id />
				</dummy>
			</xf:instance>
			<xf:instance id="delete_find1">
				<wfs:GetFeature>
					<wfs:Query typeName="pre4:{lower-case(@name)}">
						<ogc:Filter>
							<ogc:GmlObjectId gml:id="" />
						</ogc:Filter>
						<xsl:element name="{$namespace}dummy" />
					</wfs:Query>
				</wfs:GetFeature>
			</xf:instance>
			<xf:instance id="delete_view1">
				<wfs:FeatureCollection>
					<gml:featureMember>
						<xsl:element name="{$feature_name}" />
					</gml:featureMember>
				</wfs:FeatureCollection>
			</xf:instance>
			<xf:instance id="delete1">
				<wfs:Transaction version="1.1.0" service="WFS">
					<wfs:Delete typeName="pre4:{lower-case(@name)}">
						<ogc:Filter>
							<ogc:GmlObjectId gml:id="" />
						</ogc:Filter>
						<xsl:element name="{$namespace}dummy" />
					</wfs:Delete>
				</wfs:Transaction>
			</xf:instance>
			<xf:instance id="delete_save">
				<dummy xmlns="">
					<wfs:Update />
				</dummy>
			</xf:instance>
			<xf:instance id="delete_resp">
				<dummy xmlns="" />
			</xf:instance>
			<xf:instance id="delete_msg">
				<dummy xmlns="" message="" />
			</xf:instance>
			<xf:bind id="delete_success" nodeset="instance('delete_msg')/@message" calculate="choose(instance('delete_resp')//wfs:TransactionSummary/wfs:totalDeleted=0,'NO RECORD DELETED','')" type="xf:string" />
			<xf:submission id="delete_submit1" ref="instance('delete_find1')" method="post" action="../services" replace="instance" instance="delete_view1">
				<xf:setvalue ev:event="xforms-submit" ref="instance('delete_find1')//ogc:GmlObjectId/@gml:id" value="{concat(&quot;concat('&quot;,lower-case(@name),&quot;.',instance('delete_id1')//id)&quot;)}" />
				<xf:setvalue ev:event="xforms-submit" ref="instance('delete1')//ogc:GmlObjectId/@gml:id" value="{concat(&quot;concat('&quot;,lower-case(@name),&quot;.',instance('delete_id1')//id)&quot;)}" />
				<xf:action ev:event="xforms-submit-done" if="instance('delete_view1')/gml:featureMember">
					<xf:toggle case="do_delete" />
				</xf:action>
			</xf:submission>
			<xf:submission id="delete_submit2" ref="instance('delete1')" method="post" action="../services" replace="instance" instance="delete_resp">
				<xf:action ev:event="xforms-submit-done" if="instance('delete_resp')//wfs:TransactionSummary/wfs:totalDeleted=1">
					<xf:delete nodeset="instance('delete_view1')/gml:featureMember/{$feature_name}/*" />
					<xf:delete nodeset="instance('{lower-case(@name)}')/gml:featureMember/{$feature_name}[@gml:id=instance('delete_find1')//ogc:GmlObjectId/@gml:id]" />
					<xf:delete nodeset="instance('delete_resp')/*" />
					<xf:setvalue ref="instance('delete_id1')/id" value="" />
					<xf:toggle case="select_delete" />
				</xf:action>
				<xf:message level="modeless" ev:event="xforms-submit-error">
					Submit error.
	            </xf:message>
			</xf:submission>
		</xf:model>
	</xsl:template>
	<!-- 
		routine to add features (and subfeatures) to model
	-->
	<xsl:template name="model-feature">
		<xsl:param name="table" />
		<xsl:element name="{concat($namespace,lower-case($table/@name))}">
			<xsl:attribute name="gml:id">NULL</xsl:attribute>
			<!-- handle each column -->
			<xsl:for-each select="$table/column">
				<!-- is it a foreign-key -->
				<xsl:variable name="fk" select="../foreign-key[reference/@local=current()/@name]" />
				<xsl:choose>
					<xsl:when test="$fk">
						<xsl:element name="{concat($namespace, replace(lower-case(@name),'_id$',''), '_ref')}">
							<xsl:element name="{concat($namespace,lower-case($fk/@foreignTable))}">
								<xsl:attribute name="gml:id">NULL</xsl:attribute>
							</xsl:element>
						</xsl:element>
					</xsl:when>
					<xsl:when test="@primaryKey='true' and $include_pkeys_in_form = 'false'">
					</xsl:when>
					<xsl:otherwise>
						<!-- simple non-foreign-key -->
						<xsl:element name="{concat($namespace,lower-case(@name))}" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<!-- add maxOccurs="unbounded" subfeatures by looking for foreign keys on other tables -->
			<!-- not interested if a code table -->
			<!--xsl:for-each select="/database/table[not(@name=$parent_table_name) and foreign-key/@foreignTable=$table/@name and not(@codeTable='true')]">
							<xsl:element name="{concat($namespace, replace(lower-case(./@name),'_id$',''), '_ref')}">
								<xsl:call-template name="model-feature">
									<xsl:with-param name="parent_table_name" select="$table/@name" />
									<xsl:with-param name="table" select="." />
									<xsl:with-param name="depth" select="$max_depth" />
								</xsl:call-template>
							</xsl:element>
					</xsl:for-each-->
		</xsl:element>
	</xsl:template>
    <xsl:template name="subfeature-lists">
        <xsl:variable name="current_table_name" select="./@name"/>
		<!-- find a list of foreign-key tables -->
		<xsl:variable name="foreign-keys">
			<!-- foreign keys on this table -->
			<xsl:call-template name="find_foreign_keys">
				<xsl:with-param name="table" select="." />
				<xsl:with-param name="depth" select="number(1)" />
			</xsl:call-template>
			<!-- foreign keys on 'owned' tables -->
			<xsl:variable name="table" select="." />
			<xsl:for-each select="//foreign-key[@foreignTable = current()/@name and @parentOwnsChild = 'true']/parent::table">
				<xsl:choose>
					<!-- all primary key columns are foreign-keys -->
					<xsl:when test="count(current()/column[@primaryKey='true' and not(current()/foreign-key/reference/@local = @name)]) = 0">
						<xsl:call-template name="find_foreign_keys">
							<xsl:with-param name="table" select="/database/table[@name=current()/foreign-key/@foreignTable and not(@name=$table/@name)][1]" />
							<xsl:with-param name="depth" select="number(1)" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="find_foreign_keys">
							<xsl:with-param name="table" select="." />
							<xsl:with-param name="depth" select="number(1)" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
        </xsl:variable>
        <!-- put in the current feature -->
		<xf:instance>
			<xsl:attribute name="id">
				<xsl:value-of select="lower-case($current_table_name)" />
			</xsl:attribute>
			<xsl:attribute name="src">
				<xsl:value-of select="$get_feature_url" />
				<xsl:value-of select="concat($namespace,lower-case($current_table_name))" />
			</xsl:attribute>
		</xf:instance>
		<!-- add in the subfeature instances -->
		<xsl:for-each select="$foreign-keys/*">
			<xsl:sort select="@foreignTable" />
            <xsl:if test="not(./@foreignTable = ./preceding-sibling::*/@foreignTable or ./@foreignTable = $current_table_name)">
				<xf:instance>
					<xsl:attribute name="id">
						<xsl:value-of select="lower-case(@foreignTable)" />
					</xsl:attribute>
					<xsl:attribute name="src">
						<xsl:value-of select="$get_feature_url" />
						<xsl:value-of select="concat($namespace,lower-case(@foreignTable))" />
					</xsl:attribute>
				</xf:instance>
			</xsl:if>
		</xsl:for-each>
		<!-- add grid instance -->
		<xf:instance id="grid1">
			<xf:data pos="0" stride="30" />
		</xf:instance>
	</xsl:template>
	<!-- 
    	search for all needed foreign-keys
	-->
	<xsl:template name="find_foreign_keys">
		<xsl:param name="table" />
		<xsl:param name="depth" />
		<xsl:copy-of select="$table/foreign-key" />
		<xsl:if test="$depth &lt;= 2">
			<xsl:value-of select="$table/foreign-key" />
			<xsl:for-each select="//table[@name=$table/foreign-key[@parentOwnsChild='false']/@foreignTable]">
				<xsl:call-template name="find_foreign_keys">
					<xsl:with-param name="table" select="." />
					<xsl:with-param name="depth" select="$depth + 1" />
				</xsl:call-template>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<!--
		template to create data bindings
	-->
	<xsl:template name="bindings">
		<xsl:variable name="table_name" select="lower-case(@name)" />
		<xsl:variable name="path" select="concat('wfs:Insert/wfs:FeatureCollection/gml:featureMember/',$namespace,$table_name,'/')" />
		<xsl:for-each select="column">
			<xsl:variable name="fk" select="../foreign-key[reference/@local=current()/@name]" />
			<xsl:choose>
				<!-- subfeature as indicated by foreign-key, add gml:id binding -->
				<xsl:when test="$fk">
					<xsl:element name="xf:bind">
						<xsl:attribute name="id">
							<xsl:value-of select="concat($table_name,'_',lower-case(@name))" />
						</xsl:attribute>
						<xsl:attribute name="nodeset">
							<xsl:value-of select="concat($path,$namespace,lower-case(replace(@name,'_id$','')),'_ref/',$namespace,lower-case($fk/@foreignTable),'/@gml:id')" />
						</xsl:attribute>
						<xsl:attribute name="required">
							<xsl:apply-templates select="@required" />
						</xsl:attribute>
						<xsl:attribute name="type">
							<xsl:text>xf:string</xsl:text>
						</xsl:attribute>
						<xsl:attribute name="constraint">
							<xsl:text>not(.='NULL')</xsl:text>
						</xsl:attribute>
					</xsl:element>
				</xsl:when>
				<!-- primary-key -->
				<xsl:when test="@primaryKey='true' and count(../column[@primaryKey='true'])=1 and @type='INTEGER'">
					<!-- exclude simple numeric primary keys as present in @gml:id -->
				</xsl:when>
				<!-- simple property -->
				<xsl:otherwise>
					<xsl:element name="xf:bind">
						<xsl:attribute name="id">
							<xsl:value-of select="concat($table_name,'_',lower-case(@name))" />
						</xsl:attribute>
						<xsl:attribute name="nodeset">
							<xsl:value-of select="concat($path,$namespace,lower-case(@name))" />
						</xsl:attribute>
						<xsl:attribute name="required">
							<xsl:apply-templates select="@required" />
						</xsl:attribute>
						<xsl:attribute name="type">
							<xsl:apply-templates select="@type" />
						</xsl:attribute>
						<xsl:if test="contains(upper-case(@type),'CHAR')">
							<xsl:attribute name="constraint">
								<xsl:text>string-length(.) &lt;= </xsl:text>
								<xsl:value-of select="@size" />
							</xsl:attribute>
						</xsl:if>
					</xsl:element>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<!--
		converts db type to xsd type 
	-->
	<xsl:template match="@type">
		<xsl:choose>
			<xsl:when test=". = 'INTEGER'">
				<xsl:text>xf:integer</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'DECIMAL'">
				<xsl:text>xf:decimal</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'VARCHAR'">
				<xsl:text>xf:string</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'DATE'">
				<xsl:text>xf:date</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'TIMESTAMP'">
				<xsl:text>xf:dateTime</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'LONGVARCHAR'">
				<xsl:text>xf:string</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'BOOLEAN'">
				<xsl:text>xf:boolean</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>xf:string</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
	
	-->
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
	<!--
        bindings for subfeatures 
    -->
	<xsl:template match="column[../foreign-key[reference/@local=current()/@name]]" mode="binding" priority="3">
		<xsl:apply-templates select="../foreign-key[reference/@local=current()/@name]" mode="binding" />
	</xsl:template>
	<!-- 
		bindings for subfeature ids
	-->
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
			<xsl:attribute name="type">xf:string</xsl:attribute>
			<xsl:attribute name="required">
				<xsl:choose>
					<!-- see if the fk field is required="true"-->
					<xsl:when test="../column[@name=current()/reference/@local]/@required='true'">
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
		<xsl:param name="model-number" />
		<xf:submission id="{concat('s',$model-number)}" method="post" action="{$wfs_url}" replace="instance" instance="{concat('resp',$model-number)}">
			<xsl:choose>
				<xsl:when test="$model-number = 1">
					<xsl:variable name="feature_name" select="concat($namespace,lower-case(@name))" />
					<xf:action ev:event="xforms-submit">
						<xf:insert nodeset="instance('temp1')/{$feature_name}" origin="instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/{$feature_name}" />
						<xf:delete nodeset="instance('temp1')/{$feature_name}" at="1" />
						<!-- delete any references to optional subfeatures that have not been selected -->
						<xf:delete nodeset="instance('trans1')//gml:featureMember/{$feature_name}/descendant::*[child::*[@gml:id='NULL']]" />
						<!-- remove all gml:id so the WFS server doesn't reject a transaction with two subfeatures having the same id
                        also does a comparison of new with existing to prevent duplications (using existing if present) -->
						<xf:delete nodeset="instance('trans1')//@gml:id" />
						<xf:dispatch name="clear-response" target="model1" />
					</xf:action>
					<xf:action ev:event="xforms-submit-done">
						<xf:insert nodeset="instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/{$feature_name}" origin="instance('temp1')/{$feature_name}" />
						<xf:delete nodeset="instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/{$feature_name}" at="1" />
						<xf:dispatch name="process-response" target="model1" />
					</xf:action>
					<xf:action ev:event="xforms-submit-error">
						<xf:insert nodeset="instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/{$feature_name}" origin="instance('temp1')/{$feature_name}" />
						<xf:delete nodeset="instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/{$feature_name}" at="1" />
						<xf:message level="modeless">Submit error</xf:message>
					</xf:action>
				</xsl:when>
				<xsl:otherwise>
					<xf:message level="modeless" ev:event="xforms-submit-error">
				        Submit error.
                    </xf:message>
					<!-- a successful submission of a new subfeature must trigger an insert of the new
                     subfeature into the relevant instance -->
					<xf:dispatch ev:event="xforms-submit" name="clear-response" target="{concat('model',$model-number)}" />
					<xf:dispatch ev:event="xforms-submit-done" name="process-response" target="{concat('model',$model-number)}" />
				</xsl:otherwise>
			</xsl:choose>
		</xf:submission>
	</xsl:template>
	<!--
		template to create submission response instance
	-->
	<xsl:template name="wfs-response">
		<xsl:param name="model-number" />
		<xf:instance id="{concat('resp',$model-number)}">
			<dummy xmlns="" />
		</xf:instance>
	</xsl:template>
	<!--
		template to create post submission messages
	-->
	<xsl:template name="messages">
		<xf:bind id="error1" nodeset="instance('resp1')//ows:ExceptionText" calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)" type="xf:string" />
		<xf:bind id="success1" nodeset="instance('resp1')//ogc:FeatureId/@fid" type="xf:string" />
	</xsl:template>
	<!--
		template to process submission result
	-->
	<xsl:template name="process-response">
		<xsl:param name="model-number" />
		<xsl:variable name="table-name" select="lower-case(@name)" />
		<xsl:variable name="fid" select="concat('instance(',&quot;'resp&quot;,$model-number,&quot;')//ogc:FeatureId/@fid&quot;)" />
		<xsl:variable name="nodeset" select="concat(&quot;instance('&quot;,$table-name,&quot;')/gml:featureMember/&quot;,$namespace,$table-name)" />
		<xsl:variable name="origin" select="concat('instance(',&quot;'trans&quot;,$model-number,&quot;')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/&quot;,$namespace,$table-name)" />
		<xf:bind id="{concat('error',$model-number)}" nodeset="{concat('instance(',&quot;'resp&quot;,$model-number,&quot;')//ows:ExceptionText&quot;)}" calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)" type="xf:string" />
		<xf:bind id="{concat('success',$model-number)}" nodeset="{$fid}" type="xf:string" />
		<xf:action ev:event="clear-response">
			<xf:delete nodeset="{$fid}" />
		</xf:action>
		<!-- copies the current subfeature (just created) to the instance on which select1 itemset is built -->
		<xf:action ev:event="process-response" if="{$fid}">
			<xf:setvalue ref="{concat($origin,'/@gml:id')}" value="{$fid}" />
			<xf:insert nodeset="{$nodeset}" origin="{$origin}" />
			<xsl:if test="$model-number &gt; 1">
				<xf:setvalue ref="{concat(&quot;instance('trans1')//&quot;,$namespace,$table-name,'/@gml:id')}" value="{$fid}" />
			</xsl:if>
			<xf:dispatch name="xforms-revalidate" target="model1" />
			<xf:toggle case="home" />
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
				<xsl:text>')/gml:featureMember/</xsl:text>
				<xsl:value-of select="concat($namespace,$feature)" />
				<xsl:text>[1]/@gml:id</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<!-- 
		template to build the create subfeature form
	-->
	<xsl:template name="create">
        <div class="form">
            <xsl:variable name="parent_table_name" select="@name"/>
			<xsl:variable name="lookups" select="/database/table[@name=current()/foreign-key[@parentOwnsChild='false']/@foreignTable]" />
			<xf:switch>
				<xf:case id="home" selected="true">
					<label>
						<xsl:text>CREATE </xsl:text>
						<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
					</label>
					<div class="form-contents">
						<xf:group ref="{concat('wfs:Insert/wfs:FeatureCollection/gml:featureMember/',$namespace,lower-case(@name))}" model="model1">
                            <xsl:for-each select="column[not(@name = ../foreign-key[@parentOwnsChild = 'true']/reference/@local)]">
                                <xsl:apply-templates select="." mode="form" />
                            </xsl:for-each>
							<!-- look for subfeatures 'owned' by this feature -->
							<xsl:variable name="table" select="current()" />
							<xsl:for-each select="//foreign-key[@foreignTable = current()/@name and @parentOwnsChild = 'true']">
								<xsl:variable name="reftable" select="parent::table" />
								<xsl:choose>
									<!-- all primary key columns are foreign-keys -->
									<xsl:when test="count($reftable/column[@primaryKey='true' and not($reftable/foreign-key/reference/@local = @name)]) = 0">
										<xsl:call-template name="create-children">
											<xsl:with-param name="model_number" select="count($lookups) + position() + 1" />
											<xsl:with-param name="table" select="$table" />
											<!-- find the table referenced by the other foreign-key on the link table -->
											<xsl:with-param name="referenced_table" select="/database/table[@name=$reftable/foreign-key/@foreignTable and not(@name=$table/@name)]" />
											<xsl:with-param name="link_table" select="$reftable" />
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="create-children">
											<xsl:with-param name="model_number" select="count($lookups) + position() + 1" />
											<xsl:with-param name="table" select="$table" />
											<xsl:with-param name="referenced_table" select="$reftable" />
											<xsl:with-param name="link_table" select="$reftable" />
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</xf:group>
						<xf:submit submission="s1">
							<xf:label>Save</xf:label>
						</xf:submit>
						<xf:trigger>
							<xf:label>Reset</xf:label>
							<xf:action ev:event="DOMActivate">
								<xf:reset />
								<xf:delete nodeset="instance('resp1')/ows:Exception" />
								<xf:delete nodeset="instance('resp1')//ogc:FeatureId/@fid" />
								<xf:dispatch name="set-selected" target="model1" />
							</xf:action>
						</xf:trigger>
						<div class="error">
							<xf:output bind="error1">
								<xf:label>Error:</xf:label>
							</xf:output>
						</div>
						<div class="success">
							<xf:output bind="success1">
								<xf:label>New Record Id:</xf:label>
							</xf:output>
						</div>
					</div>
				</xf:case>
				<!-- add cases for new subfeature generation -->
				<xsl:for-each select="$lookups">
					<xf:case id="{concat('new_',lower-case(@name))}">
						<label>
							<xsl:text>CREATE </xsl:text>
							<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
						</label>
						<div class="form-contents">
							<xsl:element name="xf:group">
								<xsl:attribute name="ref">
									<xsl:value-of select="concat('wfs:Insert/wfs:FeatureCollection/gml:featureMember/',$namespace,lower-case(@name))" />
								</xsl:attribute>
								<xsl:attribute name="model">
									<xsl:value-of select="concat('model',position()+1)" />
								</xsl:attribute>
								<!-- handle each column -->
								<xsl:for-each select="column[not(@name = ../foreign-key[@parentOwnsChild = 'true']/reference/@local)]">
                                    <xsl:apply-templates select="." mode="form" />
                                </xsl:for-each>
							</xsl:element>
							<xf:submit submission="{concat('s',position()+1)}">
								<xf:label>Save</xf:label>
							</xf:submit>
							<xf:trigger>
								<xf:label>Back</xf:label>
								<xf:toggle ev:event="DOMActivate" case="home" />
							</xf:trigger>
							<div class="error">
								<xf:output bind="{concat('error',position()+1)}">
									<xf:label>Error:</xf:label>
								</xf:output>
							</div>
						</div>
					</xf:case>
				</xsl:for-each>
				<!-- add cases for maxOccurs='unbounded' subfeatures of this feature  -->
				<xsl:variable name="model-number" select="count($lookups)+1" />
				<xsl:for-each select="//foreign-key[@foreignTable = current()/@name and @parentOwnsChild = 'true']/parent::table">
					<xsl:choose>
						<!-- many-to-many all primary key columns are foreign-keys -->
						<xsl:when test="count(current()/column[@primaryKey='true' and not(current()/foreign-key/reference/@local = @name)]) = 0">
							<xsl:for-each select="/database/table[@name=current()/foreign-key/@foreignTable and not(@name=current()/@name)][1]">
								<xf:case id="{concat('new_',lower-case(@name))}">
									<label>
										<xsl:text>CREATE </xsl:text>
										<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
									</label>
									<div class="form-contents">
										<xsl:element name="xf:group">
											<xsl:attribute name="ref">
												<xsl:value-of select="concat('wfs:Insert/wfs:FeatureCollection/gml:featureMember/',$namespace,lower-case(@name))" />
											</xsl:attribute>
											<xsl:attribute name="model">
												<xsl:value-of select="concat('model',position()+$model-number)" />
											</xsl:attribute>
                                            <!-- handle each column -->
                                            <xsl:for-each select="column[not(@name = ../foreign-key[@parentOwnsChild = 'true']/reference/@local)]">
                                                <xsl:apply-templates select="." mode="form" />
                                            </xsl:for-each>    
										</xsl:element>
									    <xf:trigger>
										<xf:label>Add</xf:label>
                                            <xf:action ev:event="DOMActivate">
                                                <xf:insert nodeset="instance('{lower-case(@name)}{$model-number + position()}')/{$namespace}{lower-case(@name)}_ref/{$namespace}{lower-case(@name)}" origin="instance('trans{$model-number + position()}')//{$namespace}{lower-case(@name)}" />
											    <xf:delete nodeset="instance('{lower-case(@name)}{$model-number + position()}')/{$namespace}{lower-case(@name)}_ref/{$namespace}{lower-case(@name)}" at="1" /> 
                                                <xf:insert nodeset="instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/{$namespace}{lower-case($parent_table_name)}/*" 
                                                origin="instance('{lower-case(@name)}{$model-number + position()}')/*" />


											    <xf:toggle case="home" />
										    </xf:action>
									    </xf:trigger>
										<xf:trigger>
											<xf:label>Back</xf:label>
											<xf:toggle ev:event="DOMActivate" case="home" />
										</xf:trigger>
										<div class="error">
											<xf:output bind="{concat('error',$model-number + position())}">
												<xf:label>Error:</xf:label>
											</xf:output>
										</div>
									</div>
								</xf:case>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<xf:case id="{concat('new_',lower-case(@name))}">
								<label>
									<xsl:text>CREATE </xsl:text>
									<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
								</label>
								<div class="form-contents">
									<xsl:element name="xf:group">
										<xsl:attribute name="ref">
											<xsl:value-of select="concat('wfs:Insert/wfs:FeatureCollection/gml:featureMember/',$namespace,lower-case(@name))" />
										</xsl:attribute>
										<xsl:attribute name="model">
											<xsl:value-of select="concat('model',$model-number+1)" />
										</xsl:attribute>
                                        <!-- handle each column -->
								        <xsl:for-each select="column[not(@name = ../foreign-key[@parentOwnsChild = 'true']/reference/@local)]">
                                            <xsl:apply-templates select="." mode="form" />
                                        </xsl:for-each>
                                        <!--xsl:apply-templates select="column" mode="form" /-->
									</xsl:element>
									<xf:trigger>
										<xf:label>Add</xf:label>
                                            <xf:action ev:event="DOMActivate">
                                                <xf:insert nodeset="instance('{lower-case(@name)}{$model-number + position()}')/{$namespace}{lower-case(@name)}_ref/{$namespace}{lower-case(@name)}" origin="instance('trans{$model-number + position()}')//{$namespace}{lower-case(@name)}" />
											    <xf:delete nodeset="instance('{lower-case(@name)}{$model-number + position()}')/{$namespace}{lower-case(@name)}_ref/{$namespace}{lower-case(@name)}" at="1" /> 
                                                <xf:insert nodeset="instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/{$namespace}{lower-case($parent_table_name)}/*" 
                                                origin="instance('{lower-case(@name)}{$model-number + position()}')/*" />


											    <xf:toggle case="home" />
										    </xf:action>
									</xf:trigger>
									<xf:trigger>
										<xf:label>Back</xf:label>
										<xf:toggle ev:event="DOMActivate" case="home" />
									</xf:trigger>
									<div class="error">
										<xf:output bind="{concat('error',$model-number+1)}">
											<xf:label>Error:</xf:label>
										</xf:output>
									</div>
								</div>
							</xf:case>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xf:switch>
		</div>
	</xsl:template>
	<!-- 
		template to build the edit form
	-->
	<xsl:template name="edit">
		<div class="form">
			<xf:switch>
				<xf:case id="select_edit">
					<xf:input ref="instance('edit_id1')/id">
                        <xf:label>
							<xsl:call-template name="proper-case">
								<xsl:with-param name="toconvert" select="concat('ENTER ' ,replace(@name,'_',' ') ,' ID')" />
							</xsl:call-template>
						</xf:label>
					</xf:input>
					<xf:submit submission="edit_submit1">
						<xf:label>Find</xf:label>
					</xf:submit>
				</xf:case>
				<xf:case id="do_edit">
					<xf:group ref="{concat(&quot;instance('edit_view1')/gml:featureMember/&quot;,$namespace,lower-case(@name))}">
						<label>
							<xsl:text>EDIT </xsl:text>
							<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
						</label>
						<table class="edit">
							<tbody>
								<!-- TODO need to handle foreign keys -->
								<xsl:for-each select="column[@primaryKey = 'false' and not(../foreign-key/reference/@local = @name)]">
									<tr>
										<td>
											<xsl:apply-templates select="." mode="display" />
										</td>
										<td>
											<xf:trigger>
												<xf:label>Edit</xf:label>
												<xf:action ev:event="DOMActivate">
													<xf:insert nodeset="instance('edit_edit1')/wfs:Update/wfs:Property" if="{concat(&quot;count(instance('edit_edit1')/wfs:Update/wfs:Property[wfs:Name = '&quot;,fn:property-name(@name),&quot;']) = 0&quot;)}" at="1" position="before" />
													<xf:setvalue ref="instance('edit_edit1')/wfs:Update/wfs:Property[position()=1]/wfs:Name" value="{concat(&quot;'&quot;,fn:property-name(@name),&quot;'&quot;)}" />
												</xf:action>
											</xf:trigger>
										</td>
										<td>
											<xsl:apply-templates select="." mode="edit" />
										</td>
									</tr>
								</xsl:for-each>
							</tbody>
						</table>
						<xf:submit submission="edit_submit2">
							<xf:label>Save</xf:label>
						</xf:submit>
						<xf:trigger>
							<xf:label>Cancel</xf:label>
							<xf:action ev:event="DOMActivate">
								<xf:reset model="edit_model" />
								<xf:toggle case="select_edit" />
							</xf:action>
						</xf:trigger>
						<!--div class="error">
							<xf:output bind="edit_error">
								<xf:label>Error:</xf:label>
							</xf:output>
						</div-->
						<div class="success">
							<xf:output bind="edit_success">
								<xf:label>
								</xf:label>
							</xf:output>
						</div>
					</xf:group>
				</xf:case>
			</xf:switch>
		</div>
	</xsl:template>
	<!-- 
		template to display property value as xf:output
	-->
	<xsl:template match="column" mode="display">
		<xsl:choose>
			<xsl:when test="../foreign-key[reference/@local=current()/@name]">
				<xsl:variable name="foreign-table" select="//database/table[@name=current()/../foreign-key[reference/@local=current()/@name]/@foreignTable]" />
				<xsl:variable name="foreign-table-field" select="$foreign-table/unique[@name='UNIQUE_INDEX']/unique-column[1]/@name" />
				<xf:output ref="{concat($namespace,replace(lower-case(@name),'_id$',''),'_ref/',$namespace,lower-case($foreign-table/@name),'/',$namespace,lower-case($foreign-table-field))}" />
			</xsl:when>
			<xsl:otherwise>
				<xf:output ref="{concat('concat(',$namespace,lower-case(@name),&quot;,' ')&quot;)}">
                    <xf:label>
                        <xsl:apply-templates select="@name"/>
						<xsl:text>:</xsl:text>
					</xf:label>
				</xf:output>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 
		template to create xf:input for changing property value
	-->
	<xsl:template match="column" mode="edit">
		<xsl:choose>
			<xsl:when test="../foreign-key[reference/@local=current()/@name]">
				<xsl:variable name="fk_node" select="../foreign-key[reference/@local=current()/@name]" />
				<xsl:call-template name="select1-from-foreign-key">
					<xsl:with-param name="fk_node" select="$fk_node" />
					<xsl:with-param name="ref" select="concat(&quot;instance('edit_edit1')/wfs:Update/wfs:Property[wfs:Name='&quot;,fn:property-name(@name),&quot;']/wfs:Value/@fid&quot;)" />
					<xsl:with-param name="include_label" select="'false'" />
					<xsl:with-param name="include_add" select="'false'" />
					<xsl:with-param name="action_nodeset">context()/../*</xsl:with-param>
					<xsl:with-param name="action_origin" select="concat(&quot;instance('&quot;,lower-case($fk_node/@foreignTable),&quot;')/gml:featureMember/&quot;,$namespace,lower-case($fk_node/@foreignTable),'[@gml:id=current()]')" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xf:input ref="{concat(&quot;instance('edit_edit1')/wfs:Update/wfs:Property[wfs:Name='&quot;,fn:property-name(@name),&quot;']/wfs:Value&quot;)}">
				</xf:input>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 
		template to build the delete form
	-->
	<xsl:template name="delete">
		<div class="form">
			<xf:switch>
				<xf:case id="select_delete">
					<xf:input ref="instance('delete_id1')/id">
						<xf:label>
							<xsl:call-template name="proper-case">
								<xsl:with-param name="toconvert" select="concat('ENTER ' ,replace(@name,'_',' ') ,' ID')" />
							</xsl:call-template>
						</xf:label>
					</xf:input>
					<xf:submit submission="delete_submit1">
						<xf:label>Find</xf:label>
					</xf:submit>
				</xf:case>
				<xf:case id="do_delete">
					<xf:group ref="{concat(&quot;instance('delete_view1')/gml:featureMember/&quot;,$namespace,lower-case(@name))}">
						<label>
							<xsl:text>DELETE </xsl:text>
							<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
						</label>
						<div class="form-contents">
							<!-- TODO need to handle foreign keys -->
							<xsl:for-each select="column[not(@primaryKey = 'true') and not(../foreign-key/reference/@local = @name)]">
								<xsl:apply-templates select="." mode="display" />
							</xsl:for-each>
						</div>
						<xf:switch>
							<xf:case id="delete_step1" selected="true">
								<xf:trigger>
									<xf:label>Delete</xf:label>
									<xf:toggle ev:event="DOMActivate" case="delete_step2" />
								</xf:trigger>
								<xf:trigger>
									<xf:label>Back</xf:label>
									<xf:toggle ev:event="DOMActivate" case="select_delete" />
								</xf:trigger>
							</xf:case>
							<xf:case id="delete_step2" selected="true">
								<xf:submit submission="delete_submit2">
									<xf:label>Confirm Delete</xf:label>
								</xf:submit>
								<xf:trigger>
									<xf:label>Cancel</xf:label>
									<xf:toggle ev:event="DOMActivate" case="delete_step1" />
								</xf:trigger>
								<div class="success">
									<xf:output bind="delete_success">
										<xf:label>
										</xf:label>
									</xf:output>
								</div>
							</xf:case>
						</xf:switch>
					</xf:group>
				</xf:case>
			</xf:switch>
		</div>
	</xsl:template>
	<!-- 
    template to create a maxOccurs='unbounded' select & table for new subfeatures
    based on a selected foreign-key in a child table
	-->
	<xsl:template name="create-children">
		<xsl:param name="model_number" />
		<xsl:param name="table" />
		<xsl:param name="referenced_table" />
		<xsl:param name="link_table" />
		<xsl:variable name="link_name" select="concat($namespace,lower-case($link_table/@name))" />
		<xsl:variable name="ref_name" select="concat($namespace,lower-case($referenced_table/@name))" />
		<xsl:choose>
			<xsl:when test="not($link_table = $referenced_table)">
                <label>
                    <xsl:apply-templates select="$link_table/@name"/>s
                </label>
				<!--select or create a referenced table entity -->
				<xsl:call-template name="create-children-table">
					<xsl:with-param name="nodeset" select="concat('context()/',$link_name,'_ref')" />
					<xsl:with-param name="path" select="concat($link_name,'/',$ref_name,'_ref/',$ref_name,'/')" />
					<xsl:with-param name="referenced_table" select="$referenced_table" />
					<xsl:with-param name="new_row">
						<div class="select1-group">
							<xf:select1 ref="{concat(&quot;instance('&quot;,lower-case($link_table/@name),$model_number,&quot;')/@id&quot;)}" appearance="minimal" incremental="true()">
                                <xf:label>
                                    <xsl:text>Select </xsl:text><apply-templates select="$link_table/@name"/>
								</xf:label>
								<xf:itemset nodeset="{concat(&quot;instance('&quot;,lower-case($referenced_table/@name),&quot;')/gml:featureMember/&quot;,$ref_name)}">
									<xf:value ref="@gml:id" />
									<xf:label ref="{$namespace}name" />
								</xf:itemset>
								<xf:action ev:event="xforms-value-changed">
									<xf:insert nodeset="{concat('context()/../',$link_name,'_ref/',$link_name,'/',$ref_name,'_ref/',$ref_name)}" origin="{concat(&quot;instance('&quot;,lower-case($referenced_table/@name),&quot;')/gml:featureMember/&quot;,$ref_name,'[@gml:id=current()]')}" />
									<xf:delete nodeset="{concat('context()/../',$link_name,'_ref/',$link_name,'/',$ref_name,'_ref/',$ref_name)}" at="1" />
									<xf:insert nodeset="{concat(&quot;instance('trans1')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/&quot;,$namespace,lower-case($table/@name),'/*')}" origin="{concat('context()/../',$link_name,'_ref')}" />
								</xf:action>
							</xf:select1>
							<xf:trigger>
								<xf:label>
									<img src="plus.jpg" />
								</xf:label>
								<xf:action ev:event="DOMActivate">
									<xf:toggle case="{concat('new_',lower-case($referenced_table/@name))}" />
								</xf:action>
							</xf:trigger>
						</div>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
                <label>
                    <xsl:apply-templates select="$referenced_table/@name"/>s
                </label>
				<xsl:call-template name="create-children-table">
					<xsl:with-param name="nodeset" select="concat('context()/',$ref_name,'_ref')" />
					<xsl:with-param name="path" select="concat($ref_name,'/')" />
					<xsl:with-param name="referenced_table" select="$referenced_table" />
					<xsl:with-param name="new_row">
						<!--just create a referenced table entity -->
						<xf:trigger>
                            <xf:label>
                                <xsl:text>Create </xsl:text><xsl:apply-templates select="$referenced_table/@name"/>
							</xf:label>
							<xf:action ev:event="DOMActivate">
								<xf:toggle case="{concat('new_',lower-case($referenced_table/@name))}" />
							</xf:action>
						</xf:trigger>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>


	<xsl:template name="create-children-table">
		<xsl:param name="nodeset" />
		<xsl:param name="path" />
		<xsl:param name="referenced_table" />
        <xsl:param name="new_row" />
        <!-- build a headings nodeset -->
		<xsl:variable name="headings">
            <xsl:call-template name="find_view_all_table_column_headings">
			    <xsl:with-param name="group_name" select="$referenced_table/@name" />
				<xsl:with-param name="table" select="$referenced_table" />
				<xsl:with-param name="depth" select="number(1)" />
			</xsl:call-template>
		</xsl:variable>
		<table class="datatable">
			<thead>
				<!-- find how many heading rows needed -->
				<xsl:variable name="max_depth" select="max($headings/descendant-or-self::*/count(ancestor-or-self::*))" />
				<!-- build the table from the nodeset -->
				<xsl:for-each select="2 to $max_depth">
					<tr>
						<xsl:call-template name="display_view_all_table_column_headings">
							<xsl:with-param name="heading" select="$headings" />
							<xsl:with-param name="max_depth" select="$max_depth" />
							<xsl:with-param name="target_depth" select="." />
							<xsl:with-param name="depth" select="number(1)" />
						</xsl:call-template>
						<xsl:if test=". = 2">
							<th colspan="1" rowspan="{$max_depth - 1}">Remove</th>
						</xsl:if>
					</tr>
				</xsl:for-each>
			</thead>
			<tbody>
				<xf:repeat nodeset="{$nodeset}">
					<tr>
						<xsl:call-template name="create_children_table_column_data_refs">
							<xsl:with-param name="table" select="$referenced_table" />
							<xsl:with-param name="path" select="$path" />
							<xsl:with-param name="depth" select="number(1)" />
						</xsl:call-template>
					</tr>
				</xf:repeat>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="{count($headings/descendant::*[count(child::*)=0])+1}">
                        <xsl:copy-of select="$new_row"/>
                    </td>    
                </tr>    
            </tfoot>    
		</table>
		<br />
	</xsl:template>
	<!--
		recursive template to add column data refs
	-->
	<xsl:template name="create_children_table_column_data_refs">
		<xsl:param name="table" />
		<xsl:param name="path" />
		<xsl:param name="depth" />
		<xsl:if test="$depth &lt;= $max_depth">
			<!-- add the gml id if not a simple code table -->
			<xsl:if test="$depth = 1 or count($table/column)&gt;2">
				<td>
					<xf:output ref="{concat('substring-after(',$path,'@gml:id',&quot;,'.')&quot;)}" />
				</td>
			</xsl:if>
            <xsl:for-each select="$table/column">
                <xsl:variable name="fk" select="../foreign-key[reference/@local=current()/@name]"/>
				<xsl:choose>
                    <xsl:when test="$fk">
                        <xsl:if test="$fk[@parentOwnsChild = 'false']">
						    <xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
						    <xsl:call-template name="create_children_table_column_data_refs">
							    <xsl:with-param name="table" select="//table[@name=$foreign_table_name]" />
							    <xsl:with-param name="path" select="concat($path,$namespace,replace(lower-case(@name),'_id$',''),'_ref/',$namespace,lower-case($foreign_table_name),'/')" />
							    <xsl:with-param name="depth" select="$depth + 1" />
                            </xsl:call-template>
                        </xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="@primaryKey = 'false'">
							<xsl:choose>
								<!-- put all columns in if parent feature -->
								<xsl:when test="$depth = 1">
									<td>
										<xf:output ref="{concat($path,$namespace,lower-case(@name))}" />
									</td>
								</xsl:when>
								<xsl:otherwise>
									<!--xsl:if test="../unique[@name='UNIQUE_INDEX']/unique-column[@name=current()/@name]"-->
									<xsl:if test="@required = 'true'">
										<td>
											<xsl:attribute name="class">
												<xsl:choose>
													<xsl:when test="contains(upper-case(@type),'CHAR')">text</xsl:when>
													<xsl:otherwise>numeric</xsl:otherwise>
												</xsl:choose>
											</xsl:attribute>
											<xf:output ref="{concat($path,$namespace,lower-case(@name))}" />
										</td>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:if test="$depth = 1">
				<td style="text-align:center;">
					<xf:trigger>
						<xf:label>X</xf:label>
						<xf:delete nodeset="." at="1" ev:event="DOMActivate" />
					</xf:trigger>
				</td>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<!--  
		NOTE: The following xform control generating templates are 
		overidable with more specific content templates by selecting
		on the column name.
	-->
	<!--  
		template to insert an xform input control
	-->
	<xsl:template match="column" mode="form">
		<xsl:if test="@primaryKey = 'false' or $include_pkeys_in_form = 'true'">
			<xsl:element name="{if(not(contains(upper-case(@type),'CHAR')) or @size &lt;= 20) then 'xf:input' else 'xf:textarea'}">
				<xsl:attribute name="ref">
					<xsl:value-of select="concat($namespace,lower-case(@name))" />
				</xsl:attribute>
				<xsl:attribute name="incremental">
					<xsl:text>true()</xsl:text>
				</xsl:attribute>
                <xf:label>
                    <xsl:apply-templates select="@name"/>
				</xf:label>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
				<xsl:if test="contains(upper-case(@type),'CHAR')">
					<xf:message ev:event="xforms-invalid" level="modal">maximum length is <xsl:value-of select="@size" /> characters</xf:message>
				</xsl:if>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<!--  
		template to insert an xform select1 control 
	-->
	<xsl:template match="column[../foreign-key[reference/@local=current()/@name]]" mode="form" priority="3">
		<xsl:variable name="fk_node" select="../foreign-key[reference/@local=current()/@name]" />
		<xsl:call-template name="select1-from-foreign-key">
			<xsl:with-param name="fk_node" select="$fk_node" />
			<xsl:with-param name="ref" select="concat($namespace,replace(lower-case($fk_node/reference/@local),'_id$',''),'_ref/',$namespace,lower-case($fk_node/@foreignTable),'/@gml:id')" />
			<xsl:with-param name="action_nodeset">context()/../../*</xsl:with-param>
			<xsl:with-param name="action_origin" select="concat(&quot;instance('&quot;,lower-case($fk_node/@foreignTable),&quot;')/gml:featureMember/&quot;,$namespace,lower-case($fk_node/@foreignTable),'[@gml:id=current()]')" />
		</xsl:call-template>
	</xsl:template>
	<!-- 
		template to build an xforms select1 control
	-->
	<xsl:template name="select1-from-foreign-key">
		<xsl:param name="fk_node" />
		<xsl:param name="ref" />
		<xsl:param name="include_label">true</xsl:param>
		<xsl:param name="include_add">true</xsl:param>
		<xsl:param name="action_nodeset" />
		<xsl:param name="action_origin" />
		<!-- the feature name is the name of the foreign table -->
		<xsl:variable name="feature_name">
			<xsl:value-of select="lower-case($fk_node/@foreignTable)" />
		</xsl:variable>
		<xf:group ref="{$ref}">
			<div class="select1-group">
				<xsl:element name="xf:select1">
					<xsl:attribute name="ref">
						<xsl:value-of select="'.'" />
					</xsl:attribute>
					<xsl:attribute name="appearance">minimal</xsl:attribute>
					<xsl:attribute name="incremental">true()</xsl:attribute>
					<xsl:if test="$include_label = 'true'">
						<xf:label>
							<xsl:call-template name="proper-case">
								<xsl:with-param name="toconvert" select="replace(lower-case(translate($fk_node[1]/reference/@local,'_',' ')),' id','')" />
							</xsl:call-template>
						</xf:label>
					</xsl:if>
					<xf:item>
						<xf:value>NULL</xf:value>
					</xf:item>
					<xsl:element name="xf:itemset">
						<xsl:attribute name="nodeset">
							<xsl:value-of select="concat(&quot;instance('&quot;, $feature_name, &quot;')/gml:featureMember/&quot;,$namespace,$feature_name)" />
						</xsl:attribute>
						<xsl:element name="xf:value">
							<xsl:attribute name="ref">
								<xsl:text>@gml:id</xsl:text>
							</xsl:attribute>
						</xsl:element>
						<xsl:element name="xf:label">
							<xsl:attribute name="ref">
								<xsl:value-of select="$namespace" />
								<!-- find the displayed property from the unique index of the foreign table-->
								<xsl:choose>
									<xsl:when test="/database/table[@name=$fk_node/@foreignTable]/unique[@name='UNIQUE_INDEX']">
										<xsl:value-of select="/database/table[@name=$fk_node/@foreignTable]/unique[@name='UNIQUE_INDEX']/unique-column[1]/@name" />
									</xsl:when>
									<xsl:otherwise>name</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</xsl:element>
					</xsl:element>
					<xsl:call-template name="help">
						<xsl:with-param name="key">
							<xsl:value-of select="@name" />
						</xsl:with-param>
					</xsl:call-template>
					<!-- when the selected value changes replace the relevant
				subfeature in the transaction by inserting a new one
				and then deleting the current one -->
					<xsl:if test="not($action_nodeset = '')">
						<xsl:element name="xf:action">
							<xsl:attribute name="ev:event">
								<xsl:text>xforms-value-changed</xsl:text>
							</xsl:attribute>
							<!-- only if action will select something -->
							<xsl:attribute name="if">
								<xsl:text>not(.='NULL')</xsl:text>
							</xsl:attribute>
							<!--  
						copy selected subfeature into submission
					-->
							<xsl:element name="xf:insert">
								<!-- where to put it -->
								<xsl:attribute name="nodeset">
									<xsl:value-of select="$action_nodeset" />
								</xsl:attribute>
								<!-- what to put there -->
								<xsl:attribute name="origin">
									<xsl:value-of select="$action_origin" />
								</xsl:attribute>
							</xsl:element>
							<!-- delete the dummy one -->
							<xsl:element name="xf:delete">
								<xsl:attribute name="nodeset">
									<xsl:value-of select="$action_nodeset" />
								</xsl:attribute>
								<xsl:attribute name="at">1</xsl:attribute>
							</xsl:element>
						</xsl:element>
					</xsl:if>
				</xsl:element>
				<!-- add an 'Add' button to create a new feature in this select1 list -->
				<xsl:if test="$include_add = 'true'">
					<xf:trigger>
						<xf:label>
							<img src="plus.jpg" />
						</xf:label>
						<xf:action ev:event="DOMActivate">
							<xf:toggle case="{concat('new_',$feature_name)}" />
						</xf:action>
					</xf:trigger>
				</xsl:if>
			</div>
		</xf:group>
    </xsl:template>

    <xsl:template match="@name">
		<xsl:call-template name="proper-case">
			<xsl:with-param name="toconvert" select="replace(replace(lower-case(.),'_id$',''),'_',' ')" />
        </xsl:call-template>
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
					<xsl:value-of select="lower-case(substring-before($s,' '))" />
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="proper-case">
						<xsl:with-param name="toconvert" select="substring-after($s,' ')" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="lower-case($s)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
	<xsl:function name="fn:property-name" as="xsd:string">
		<xsl:param name="name" as="xsd:string" />
		<xsl:sequence select="concat('pre4:',replace(lower-case($name),'_id$','_ref'))" />
	</xsl:function>
</xsl:stylesheet>
