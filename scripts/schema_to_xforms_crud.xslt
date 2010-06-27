<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" xmlns="http://www.w3.org/1999/xhtml"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
xmlns:emii="http://www.imos.org.au/emii"
xmlns:xf="http://www.w3.org/2002/xforms"
xmlns:ev="http://www.w3.org/2001/xml-events" 
xmlns:wfs="http://www.opengis.net/wfs" 
xmlns:ogc="http://www.opengis.net/ogc"
xmlns:ows="http://www.opengis.net/ows" 
xmlns:gml="http://www.opengis.net/gml"
xmlns:fn="http://www.imos.org.au/functions">
	<xsl:output name="xml" method="xml" encoding="UTF-8" indent="yes" />
	<xsl:include href="help.xslt" />
	<xsl:include href="globals.xslt" />
	<xsl:template match="/">
		<xsl:for-each select="//table">
			<xsl:variable name="feature-name">
				<xsl:value-of select="lower-case(@name)" />
			</xsl:variable>
			<xsl:variable name="feature-title">
				<xsl:call-template name="proper-case">
					<xsl:with-param name="toconvert" select="translate(@name,'_',' ')" />
				</xsl:call-template>
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
				<html>
					<head>
						<title>
							<xsl:value-of select="$title" />
						</title>
						<xsl:call-template name="models" />
					</head>
					<body>
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
										<xsl:call-template name="view_all_table" />
									</div>
								</xf:case>
								<xf:case id="create">
									<div class="tab-content">
										<xsl:call-template name="form" />
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
		template to build the paged table view
	-->
	<xsl:template name="view_all_table">
		<xsl:variable name="feature-instance" select="concat(&quot;instance('&quot;,lower-case(@name),&quot;')&quot;)" />
		<table>
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
					<xsl:choose>
						<xsl:when test="../foreign-key[reference/@local=current()/@name]">
							<xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
							<xsl:call-template name="find_view_all_table_column_headings">
								<xsl:with-param name="group_name" select="replace(lower-case(@name),'_id$','')" />
								<xsl:with-param name="table" select="//table[@name=$foreign_table_name]" />
								<xsl:with-param name="depth" select="$depth + 1" />
							</xsl:call-template>
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
	<xsl:template name="find_depths">
		<xsl:param name="headings" />
		<xsl:param name="depth" />
		<xsl:choose>
			<xsl:when test="count($headings/child::*)=0">
				<xsl:sequence select="$depth" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="$headings/child::*">
					<xsl:call-template name="find_depths">
						<xsl:with-param name="headings" select="." />
						<xsl:with-param name="depth" select="$depth + 1" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
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
						<xsl:call-template name="proper-case">
							<xsl:with-param name="toconvert" select="translate(@name,'_',' ')" />
						</xsl:call-template>
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
				<xsl:choose>
					<xsl:when test="../foreign-key[reference/@local=current()/@name]">
						<xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
						<xsl:call-template name="view_all_table_column_data_refs">
							<xsl:with-param name="table" select="//table[@name=$foreign_table_name]" />
							<xsl:with-param name="path" select="concat($path,$namespace,replace(lower-case(@name),'_id$',''),'_ref/',$namespace,lower-case($foreign_table_name),'/')" />
							<xsl:with-param name="depth" select="$depth + 1" />
						</xsl:call-template>
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
													<xsl:when test="@type = 'VARCHAR'">
														text
                                                    </xsl:when>
													<xsl:otherwise>
														numeric
                                                    </xsl:otherwise>
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
		template to build the models 
	-->
	<xsl:template name="models">
		<!-- add the primary model -->
		<xsl:call-template name="model">
			<xsl:with-param name="model-number" select="number(1)" />
		</xsl:call-template>
		<!-- add the subfeature prototypes as separate models -->
		<xsl:for-each select="/database/table[@codeTable = 'true' and @name=current()/foreign-key/@foreignTable]">
			<xsl:call-template name="model">
				<xsl:with-param name="model-number" select="position()+1" />
			</xsl:call-template>
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
			<!-- action to select first subfeatures in lists -->
			<xsl:call-template name="initialise">
				<xsl:with-param name="model-number" select="$model-number" />
			</xsl:call-template>
		</xf:model>
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
							<xsl:call-template name="model-feature">
								<xsl:with-param name="table" select="." />
								<xsl:with-param name="depth" select="number(1)" />
							</xsl:call-template>
						</gml:featureMember>
					</wfs:FeatureCollection>
				</wfs:Insert>
			</wfs:Transaction>
		</xf:instance>
	</xsl:template>
	<!-- 
    	template to build an feature editing model> 
    -->
	<xsl:template name="edit_model">
		<xsl:variable name="feature_name" select="concat($namespace,lower-case(@name))" />
		<xf:model id="edit_model">
			<xf:instance id="id1">
				<dummy xmlns="">
					<id />
				</dummy>
			</xf:instance>
			<xf:instance id="find1">
				<wfs:GetFeature>
					<wfs:Query typeName="{$feature_name}">
						<ogc:Filter>
							<ogc:GmlObjectId gml:id="" />
						</ogc:Filter>
					</wfs:Query>
				</wfs:GetFeature>
			</xf:instance>
			<xf:instance id="view1">
				<wfs:FeatureCollection>
					<gml:featureMember>
						<xsl:element name="{$feature_name}" />
					</gml:featureMember>
				</wfs:FeatureCollection>
			</xf:instance>
			<xf:instance id="edit1">
				<wfs:Transaction version="1.1.0" service="WFS">
					<wfs:Update typeName="{$feature_name}">
						<wfs:Property>
							<wfs:Name/>
							<wfs:Value fid=""/>
						</wfs:Property>
						<ogc:Filter>
							<ogc:GmlObjectId gml:id="" />
						</ogc:Filter>
					</wfs:Update>
				</wfs:Transaction>
			</xf:instance>
			<xf:instance id="edit_save">
				<dummy xmlns="" >
					<wfs:Update/>
				</dummy>
			</xf:instance>
			<xf:instance id="edit_resp">
				<dummy xmlns="" />
			</xf:instance>
			<xf:bind id="edit_error" nodeset="instance('edit_resp')//ows:ExceptionText" calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)" type="xsd:string" />
			<xf:bind id="edit_success" nodeset="instance('edit_resp')//ogc:FeatureId/@fid" type="xsd:string" />
			<xf:action ev:event="clear-response">
				<xf:delete nodeset="instance('edit_resp')//ogc:FeatureId/@fid" />
			</xf:action>
			<xf:action ev:event="process-response" if="instance('edit_resp')//ogc:FeatureId/@fid">
			</xf:action>
			<xf:submission id="edit_submit1" ref="instance('find1')" method="post" action="../services" replace="instance" instance="view1">
				<xf:setvalue ev:event="xforms-submit" ref="instance('find1')//ogc:GmlObjectId/@gml:id" value="{concat(&quot;concat('&quot;,lower-case(@name),&quot;.',instance('id1')//id)&quot;)}" />
				<xf:setvalue ev:event="xforms-submit" ref="instance('edit1')//ogc:GmlObjectId/@gml:id" value="{concat(&quot;concat('&quot;,lower-case(@name),&quot;.',instance('id1')//id)&quot;)}" /> 
				<xf:action ev:event="xforms-submit-done" if="instance('view1')/gml:featureMember">
					<xf:toggle case="do_edit" />
				</xf:action>
			</xf:submission>
			<xf:submission id="edit_submit2" ref="instance('edit1')" method="post" action="../services" replace="instance" instance="edit_resp">
				<xf:action ev:event="xforms-submit">
	                 <xf:dispatch name="clear-response" target="edit_model"/>
	                 <xf:insert nodeset="instance('edit_save')/wfs:Update" origin="instance('edit1')/wfs:Update"/>
	                 <xf:delete nodeset="instance('edit_save')/wfs:Update" at="1"/>
	                 <xf:delete nodeset="instance('edit1')//@fid"/>
	                 <xf:delete nodeset="instance('edit1')//wfs:Property[wfs:Name = '']"/>
	             </xf:action>    
	            <xf:message level="modeless" ev:event="xforms-submit-error">
					Submit error.
	            </xf:message>
	            <xf:action ev:event="xforms-submit-done">
	                <xf:insert nodeset="instance('edit1')/wfs:Update" origin="instance('edit_save')/wfs:Update" />
	                <xf:delete nodeset="instance('edit1')/wfs:Update" at="1" />
	                <xf:dispatch name="process-response" target="edit_model"/>
	             </xf:action>
			</xf:submission>
		</xf:model>
	</xsl:template>
	<!--
    	template to build a model for editing feature 
    -->
	<xsl:template name="delete_model">
		<xf:model id="delete_model">
			<xf:instance id="delete1">
				<wfs:Transaction version="1.1.0" service="WFS">
					<wfs:Delete typeName="{concat($namespace,lower-case(@name))}">
						<ogc:Filter>
							<ogc:GmlObjectId gml:id="" />
						</ogc:Filter>
					</wfs:Delete>
				</wfs:Transaction>
			</xf:instance>
			<xf:instance id="delete_resp">
				<dummy xmlns="" />
			</xf:instance>
			<xf:bind id="delete_error" nodeset="instance('delete_resp')//ows:ExceptionText" calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)" type="xsd:string" />
			<xf:bind id="delete_success" nodeset="instance('delete_resp')//ogc:FeatureId/@fid" type="xsd:string" />
			<xf:action ev:event="clear-response">
				<xf:delete nodeset="instance('delete_resp')//ogc:FeatureId/@fid" />
			</xf:action>
			<xf:action ev:event="process-response" if="instance('delete_resp')//ogc:FeatureId/@fid">
			</xf:action>
			<xf:submission id="delete_submit" method="post" action="../services" replace="instance" instance="delete_resp">
				<xf:message level="modeless" ev:event="xforms-submit-error">
					Submit error.
            	</xf:message>
				<xf:dispatch ev:event="xforms-submit" name="clear-response" target="delete_model" />
				<xf:dispatch ev:event="xforms-submit-done" name="process-response" target="delete_model" />
			</xf:submission>
		</xf:model>
	</xsl:template>
	<!-- 
		recursive routine to add features (and subfeatures) to model
	-->
	<xsl:template name="model-feature">
		<xsl:param name="parent_table_name" />
		<xsl:param name="table" />
		<xsl:param name="depth" />
		<!--xsl:comment>
			<xsl:value-of select="concat('feature:',$depth)"/>
			</xsl:comment-->
		<xsl:choose>
			<xsl:when test="$depth &gt; $max_depth">
				<xsl:element name="{concat($namespace,lower-case($table/@name))}">
					<xsl:if test="$depth &gt; 1">
						<xsl:attribute name="gml:id" />
					</xsl:if>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="{concat($namespace,lower-case($table/@name))}">
					<xsl:attribute name="gml:id" />
					<!-- handle each column -->
					<xsl:for-each select="$table/column">
						<xsl:choose>
							<!-- is it a foreign-key -->
							<xsl:when test="../foreign-key[reference/@local=current()/@name]">
								<xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
								<xsl:choose>
									<xsl:when test="$foreign_table_name = $parent_table_name">
										<!-- do nothing as we've just come from there  -->
									</xsl:when>
									<xsl:otherwise>
										<xsl:element name="{concat($namespace, replace(lower-case(@name),'_id$',''), '_ref')}">
											<xsl:call-template name="model-subfeature">
												<xsl:with-param name="parent_table_name" select="$table/@name" />
												<xsl:with-param name="table_name" select="$foreign_table_name" />
												<xsl:with-param name="depth" select="$depth + 1" />
											</xsl:call-template>
										</xsl:element>
									</xsl:otherwise>
								</xsl:choose>
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
					<xsl:if test="$table/@codeTable = 'false' and $depth &lt; $max_depth">
						<xsl:for-each select="/database/table[not(@name=$parent_table_name) and foreign-key/@foreignTable=$table/@name and not(@codeTable='true')]">
							<xsl:element name="{concat($namespace, replace(lower-case(./@name),'_id$',''), '_ref')}">
								<xsl:call-template name="model-feature">
									<xsl:with-param name="parent_table_name" select="$table/@name" />
									<xsl:with-param name="table" select="." />
									<xsl:with-param name="depth" select="$max_depth" />
								</xsl:call-template>
							</xsl:element>
						</xsl:for-each>
					</xsl:if>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 
		template for foreign-key columns/properties.
		we need to differentiate between two types of foreign keys:
		(1) one referencing a code table (or view) - add a subfeature
		(2) one referencing a non-code table (or view) - add the subfeature
		and it can be manually removed if not needed) 
	-->
	<xsl:template name="model-subfeature">
		<xsl:param name="parent_table_name" />
		<xsl:param name="table_name" />
		<xsl:param name="depth" />
		<xsl:choose>
			<xsl:when test="/database/table[@name=$table_name and @codeTable='true']">
				<xsl:call-template name="model-code-subfeature">
					<xsl:with-param name="table" select="/database/table[@name=$table_name and @codeTable='true'][1]" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="/database/view[@name=$table_name and @codeTable='true']">
				<xsl:call-template name="model-code-subfeature">
					<xsl:with-param name="table" select="/database/view[@name=$table_name and @codeTable='true'][1]" />
				</xsl:call-template>
			</xsl:when>
			<!-- not a code table so handle as for normal feature -->
			<xsl:when test="/database/table[@name=$table_name]">
				<xsl:call-template name="model-feature">
					<xsl:with-param name="parent_table_name" select="$parent_table_name" />
					<xsl:with-param name="table" select="/database/table[@name=$table_name][1]" />
					<xsl:with-param name="depth" select="$depth" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="model-feature">
					<xsl:with-param name="parent_table_name" select="$parent_table_name" />
					<xsl:with-param name="table" select="/database/view[@name=$table_name][1]" />
					<xsl:with-param name="depth" select="$depth" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
		recursive routine to add 'code' sub-features 
		where are only interested in distinguishing columns or subfeatures
	-->
	<xsl:template name="model-code-subfeature">
		<xsl:param name="table" />
		<xsl:element name="{concat($namespace,lower-case($table/@name))}">
			<xsl:attribute name="gml:id" />
			<!-- handle each column -->
			<xsl:for-each select="$table/column">
				<!-- is it a distinguishing column -->
				<xsl:if test="$table/unique[@name='UNIQUE_INDEX']/column[@name=current()/@name]">
					<xsl:choose>
						<!-- if a foreign-key we will only allow one level of distinguishing subfeatures
							so depth is $max_depth -->
						<xsl:when test="../foreign-key[reference/@local=current()/@name]">
							<xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
							<xsl:element name="{concat($namespace, replace(lower-case(@name),'_id$',''), '_ref')}">
								<xsl:call-template name="model-subfeature">
									<xsl:with-param name="table_name" select="$foreign_table_name" />
									<xsl:with-param name="depth" select="$max_depth" />
								</xsl:call-template>
							</xsl:element>
						</xsl:when>
						<!-- exclude simple numeric primary keys a present in @gml:id -->
						<xsl:when test="@primaryKey='true' and count(../column[@primaryKey='true'])=1 and @type='INTEGER'">
						</xsl:when>
						<!-- simple non-foreign-key -->
						<xsl:otherwise>
							<xsl:element name="{concat($namespace,lower-case(@name))}" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- 
		template to create instances containing lists of subfeatures obtained from WFS 	
	-->
	<xsl:template name="subfeature-lists">
		<xf:instance>
			<xsl:attribute name="id">
				<xsl:value-of select="lower-case(@name)" />
			</xsl:attribute>
			<xsl:attribute name="src">
				<xsl:value-of select="$get_feature_url" />
				<xsl:value-of select="concat($namespace,lower-case(@name))" />
			</xsl:attribute>
		</xf:instance>
		<!-- find a list of foreign-keys recursively -->
		<xsl:variable name="foreign-keys">
			<xsl:call-template name="find_foreign_keys">
				<xsl:with-param name="table" select="." />
				<xsl:with-param name="depth" select="number(1)" />
			</xsl:call-template>
		</xsl:variable>
		<!-- add in the subfeature instances -->
		<xsl:for-each select="$foreign-keys/*">
			<xsl:sort select="@foreignTable" />
			<xsl:if test="not(./preceding-sibling::*/@foreignTable = ./@foreignTable)">
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
		<xsl:if test="$depth &lt;= $max_depth">
			<xsl:value-of select="$table/foreign-key" />
			<xsl:for-each select="//table[@name=$table/foreign-key/@foreignTable]">
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
		<xsl:call-template name="binding-feature">
			<xsl:with-param name="path" select="concat('wfs:Insert/wfs:FeatureCollection/gml:featureMember/',$namespace,lower-case(@name))" />
			<xsl:with-param name="table" select="." />
			<xsl:with-param name="parent_id" select="''" />
			<xsl:with-param name="depth" select="number(1)" />
		</xsl:call-template>
	</xsl:template>
	<!--
		
	-->
	<xsl:template name="binding-feature">
		<xsl:param name="parent_table_name" />
		<xsl:param name="path" />
		<xsl:param name="table" />
		<xsl:param name="parent_id" />
		<xsl:param name="depth" />
		<xsl:if test="$depth &lt;= $max_depth">
			<xsl:variable name="id_root">
				<xsl:choose>
					<xsl:when test="string-length($parent_id) = 0">
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat($parent_id,'_')" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:for-each select="$table/column">
				<xsl:choose>
					<!--  subfeature as indicated by foreign-key -->
					<xsl:when test="../foreign-key[reference/@local=current()/@name]">
						<xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
						<xsl:choose>
							<xsl:when test="$foreign_table_name = $parent_table_name">
								<!-- do nothing as we've just come from there!  -->
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name="binding-subfeature">
									<xsl:with-param name="parent_table_name" select="$table/@name" />
									<xsl:with-param name="path" select="concat($path,'/',$namespace,replace(lower-case(@name),'_id$',''),'_ref')" />
									<xsl:with-param name="table_name" select="$foreign_table_name" />
									<xsl:with-param name="parent_id" select="concat($id_root,lower-case($foreign_table_name))" />
									<xsl:with-param name="depth" select="$depth+1" />
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<!-- primary-key -->
					<xsl:when test="@primaryKey='true' and count(../column[@primaryKey='true'])=1 and @type='INTEGER'">
						<!-- exclude simple numeric primary keys as present in @gml:id -->
					</xsl:when>
					<!-- simple property -->
					<xsl:otherwise>
						<xsl:element name="xf:bind">
							<xsl:attribute name="id">
								<xsl:value-of select="concat($id_root,lower-case(@name))" />
							</xsl:attribute>
							<xsl:attribute name="nodeset">
								<xsl:value-of select="concat($path,'/',$namespace,lower-case(@name))" />
							</xsl:attribute>
							<xsl:attribute name="required">
								<xsl:apply-templates select="@required" />
							</xsl:attribute>
							<xsl:attribute name="type">
								<xsl:apply-templates select="@type" />
							</xsl:attribute>
							<xsl:if test="@type = 'VARCHAR'">
								<xsl:attribute name="constraint">
									<xsl:text>string-length(.) &lt;= </xsl:text>
									<xsl:value-of select="@size" />
								</xsl:attribute>
							</xsl:if>
							<xsl:attribute name="type">
								<xsl:apply-templates select="@type" />
							</xsl:attribute>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>
	<!-- 
    	differentiates between tables (code and non-code) and view
    -->
	<xsl:template name="binding-subfeature">
		<xsl:param name="parent_table_name" />
		<xsl:param name="path" />
		<xsl:param name="table_name" />
		<xsl:param name="parent_id" />
		<xsl:param name="depth" />
		<xsl:choose>
			<xsl:when test="/database/table[@name=$table_name and @codeTable='true']">
				<xsl:call-template name="binding-code-subfeature">
					<xsl:with-param name="path" select="concat($path,'/',$namespace,lower-case($table_name))" />
					<xsl:with-param name="table" select="/database/table[@name=$table_name and @codeTable='true'][1]" />
					<xsl:with-param name="parent_id" select="$parent_id" />
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="/database/view[@name=$table_name and @codeTable='true']">
				<xsl:call-template name="binding-code-subfeature">
					<xsl:with-param name="path" select="concat($path,'/',$namespace,lower-case($table_name))" />
					<xsl:with-param name="table" select="/database/view[@name=$table_name and @codeTable='true'][1]" />
					<xsl:with-param name="parent_id" select="$parent_id" />
				</xsl:call-template>
			</xsl:when>
			<!-- not a code table so handle as for normal feature -->
			<xsl:when test="/database/table[@name=$table_name]">
				<xsl:call-template name="binding-feature">
					<xsl:with-param name="path" select="concat($path,'/',$namespace,lower-case($table_name))" />
					<xsl:with-param name="table" select="/database/table[@name=$table_name]" />
					<xsl:with-param name="parent_id" select="$parent_id" />
					<xsl:with-param name="depth" select="$depth+1" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="binding-feature">
					<xsl:with-param name="path" select="concat($path,'/',$namespace,lower-case($table_name))" />
					<xsl:with-param name="table" select="/database/view[@name=$table_name]" />
					<xsl:with-param name="parent_id" select="$parent_id" />
					<xsl:with-param name="depth" select="$depth+1" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
		recursive routine to add 'code' sub-features 
		we are only interested in distinguishing columns or subfeatures
	-->
	<xsl:template name="binding-code-subfeature">
		<xsl:param name="path" />
		<xsl:param name="table" />
		<xsl:param name="parent_id" />
		<xsl:variable name="id_root">
			<xsl:choose>
				<xsl:when test="string-length($parent_id) = 0">
					<xsl:text>
					</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="concat($parent_id,'_')" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- handle each column -->
		<xsl:for-each select="$table/column">
			<!-- is it a distinguishing column -->
			<xsl:if test="$table/unique[@name='UNIQUE_INDEX']/column[@name=current()/@name]">
				<xsl:choose>
					<!-- is it a foreign-key we will only allow one level of distinguishing subfeatures
						so depth is $max_depth -->
					<xsl:when test="../foreign-key[reference/@local=current()/@name]">
						<xsl:variable name="foreign_table_name" select="../foreign-key[reference/@local=current()/@name][1]/@foreignTable" />
						<xsl:call-template name="binding-subfeature">
							<xsl:with-param name="parent_table_name" select="$table/@name" />
							<xsl:with-param name="path" select="concat($path,'/',$namespace,replace(lower-case($table/@name),'_id$',''),'_ref')" />
							<xsl:with-param name="table_name" select="$foreign_table_name" />
							<xsl:with-param name="parent_id" select="concat($id_root,lower-case($table/@name))" />
							<xsl:with-param name="depth" select="$max_depth" />
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="@primaryKey='true' and count(../column[@primaryKey='true'])=1 and @type='INTEGER'">
						<!-- exclude simple numeric primary keys a present in @gml:id -->
					</xsl:when>
					<xsl:otherwise>
						<xsl:element name="xf:bind">
							<xsl:attribute name="id">
								<xsl:value-of select="concat($id_root,lower-case(@name))" />
							</xsl:attribute>
							<xsl:attribute name="nodeset">
								<xsl:value-of select="concat($path,'/',$namespace,lower-case(@name))" />
							</xsl:attribute>
							<xsl:attribute name="required">
								<xsl:apply-templates select="@required" />
							</xsl:attribute>
							<xsl:attribute name="type">
								<xsl:apply-templates select="@type" />
							</xsl:attribute>
						</xsl:element>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!--
		converts db type to xsd type 
	-->
	<xsl:template match="@type">
		<xsl:choose>
			<xsl:when test=". = 'INTEGER'">
				<xsl:text>xsd:integer</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'DECIMAL'">
				<xsl:text>xsd:decimal</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'VARCHAR'">
				<xsl:text>xsd:string</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'DATE'">
				<xsl:text>xsd:date</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'TIMESTAMP'">
				<xsl:text>xsd:dateTime</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'LONGVARCHAR'">
				<xsl:text>xsd:string</xsl:text>
			</xsl:when>
			<xsl:when test=". = 'BOOLEAN'">
				<xsl:text>xsd:boolean</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>xsd:string</xsl:text>
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
			<xsl:attribute name="type">xsd:string</xsl:attribute>
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
			<xf:message level="modeless" ev:event="xforms-submit-error">
				Submit error.
            </xf:message>
			<!-- a successful submission of a new subfeature must trigger an insert of the new
             subfeature into the relevant instance -->
			<xf:dispatch ev:event="xforms-submit" name="clear-response" target="{concat('model',$model-number)}" />
			<xf:dispatch ev:event="xforms-submit-done" name="process-response" target="{concat('model',$model-number)}" />
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
		<xf:bind id="error1" nodeset="instance('resp1')//ows:ExceptionText" calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)" type="xsd:string" />
		<xf:bind id="success1" nodeset="instance('resp1')//ogc:FeatureId/@fid" type="xsd:string" />
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
		<xf:bind id="{concat('error',$model-number)}" nodeset="{concat('instance(',&quot;'resp&quot;,$model-number,&quot;')//ows:ExceptionText&quot;)}" calculate="choose(contains(.,'Equal feature'),substring-after(.,'. '),.)" type="xsd:string" />
		<xf:bind id="{concat('success',$model-number)}" nodeset="{$fid}" type="xsd:string" />
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
    	template to set initial 'selected' subfeature to be the first in list
    	also sets boolean fields to false
	-->
	<xsl:template name="initialise">
		<xsl:param name="model-number" />
		<xsl:variable name="path" select="concat(&quot;instance('trans&quot;,$model-number,&quot;')/wfs:Insert/wfs:FeatureCollection/gml:featureMember/&quot;)" />
		<xf:dispatch ev:event="xforms-ready" name="set-selected" target="{concat('model',$model-number)}" />
		<xf:action ev:event="set-selected">
			<xsl:for-each select="foreign-key">
				<xsl:call-template name="initialise-subfeature">
					<xsl:with-param name="path" select="concat($path,$namespace,lower-case(../@name),'/',$namespace,replace(lower-case(reference/@local),'_id$',''),'_ref')" />
					<xsl:with-param name="foreign-key" select="." />
					<xsl:with-param name="depth" select="number(1)" />
				</xsl:call-template>
			</xsl:for-each>
			<xsl:for-each select="column[@type='BOOLEAN']">
				<xf:setvalue ref="{concat($path,$namespace,lower-case(../@name),'/',$namespace,lower-case(@name))}" value="false()" />
			</xsl:for-each>
			<xf:dispatch name="xforms-revalidate" target="{concat('model',$model-number)}" />
		</xf:action>
	</xsl:template>
	<!--
		set @gml:id of subfeatures recursively
	-->
	<xsl:template name="initialise-subfeature">
		<xsl:param name="path" />
		<xsl:param name="foreign-key" />
		<xsl:param name="depth" />
		<xsl:if test="$depth &lt;= $max_depth">
			<xsl:variable name="foreign_table_name">
				<xsl:value-of select="$foreign-key/@foreignTable" />
			</xsl:variable>
			<!--  
				copy selected subfeature into submission
			-->
			<xsl:element name="xf:insert">
				<!-- where to put it -->
				<xsl:attribute name="nodeset">
					<xsl:value-of select="concat($path,'/',$namespace,lower-case($foreign_table_name))" />
				</xsl:attribute>
				<!-- what to put there -->
				<xsl:attribute name="origin">
					<xsl:value-of select="concat(&quot;instance('&quot;, lower-case($foreign_table_name), &quot;')/gml:featureMember[1]/&quot;, $namespace, lower-case($foreign_table_name))" />
				</xsl:attribute>
			</xsl:element>
			<!-- delete the dummy one -->
			<xsl:element name="xf:delete">
				<xsl:attribute name="nodeset">
					<xsl:value-of select="concat($path,'/',$namespace,lower-case($foreign_table_name))" />
				</xsl:attribute>
				<xsl:attribute name="at">1</xsl:attribute>
			</xsl:element>
			<!-- go to foreign table/view and look for more foreign-keys -->
			<!--xsl:for-each
				select="/database/table[@name=$foreign_table_name and @codeTable='true']/foreign-key">
				<xsl:call-template name="initialise-code-subfeature">
					<xsl:with-param name="path"
						select="concat($path,'/',$namespace,lower-case(./@foreignTable),'_ref')" />
					<xsl:with-param name="foreign-key" select="." />
				</xsl:call-template>
			</xsl:for-each>
			<xsl:for-each
				select="/database/view[@name=$foreign_table_name and @codeTable='true']/foreign-key">
				<xsl:call-template name="initialise-code-subfeature">
					<xsl:with-param name="path"
						select="concat($path,'/',$namespace,lower-case(./@foreignTable),'_ref')" />
					<xsl:with-param name="foreign-key" select="." />
				</xsl:call-template>
			</xsl:for-each>
			<xsl:for-each
				select="/database/table[@name=$foreign_table_name and @codeTable='false']/foreign-key">
				<xsl:call-template name="initialise-subfeature">
					<xsl:with-param name="path"
						select="concat($path,'/',$namespace,lower-case(./@foreignTable),'_ref')" />
					<xsl:with-param name="foreign-key" select="." />
					<xsl:with-param name="depth" select="$depth+1" />
				</xsl:call-template>
			</xsl:for-each>
			<xsl:for-each
				select="/database/view[@name=$foreign_table_name and @codeTable='false']/foreign-key">
				<xsl:call-template name="initialise-subfeature">
					<xsl:with-param name="path"
						select="concat($path,'/',$namespace,lower-case(./@foreignTable),'_ref')" />
					<xsl:with-param name="foreign-key" select="." />
					<xsl:with-param name="depth" select="$depth+1" />
				</xsl:call-template>
			</xsl:for-each-->
		</xsl:if>
	</xsl:template>
	<!--
		set @gml:id of subfeatures recursively
	-->
	<xsl:template name="initialise-code-subfeature">
		<xsl:param name="path" />
		<xsl:param name="foreign-key" />
		<xsl:variable name="foreign_table_name">
			<xsl:value-of select="$foreign-key/@foreignTable" />
		</xsl:variable>
		<xsl:element name="xf:insert">
			<!-- where to put it -->
			<xsl:attribute name="nodeset">
				<xsl:value-of select="concat($path,'/',$namespace,lower-case($foreign_table_name))" />
			</xsl:attribute>
			<!-- what to put there -->
			<xsl:attribute name="origin">
				<xsl:value-of select="concat(&quot;instance('&quot;, lower-case($foreign_table_name), &quot;')/gml:featureMember/&quot;, $namespace, lower-case($foreign_table_name), '[1]')" />
			</xsl:attribute>
		</xsl:element>
		<!-- delete the dummy one -->
		<xsl:element name="xf:delete">
			<xsl:attribute name="nodeset">
				<xsl:value-of select="concat($path,'/',$namespace,lower-case($foreign_table_name))" />
			</xsl:attribute>
			<xsl:attribute name="at">1</xsl:attribute>
		</xsl:element>
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
		template to build the form
	-->
	<xsl:template name="form">
		<div class="form">
			<xsl:variable name="home">
				<xsl:value-of select="'home'" />
			</xsl:variable>
			<xf:switch>
				<xf:case id="{$home}" selected="true">
					<label>
						<xsl:text>CREATE </xsl:text>
						<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
					</label>
					<div class="form-contents">
						<xsl:call-template name="form-feature">
							<xsl:with-param name="table" select="." />
							<xsl:with-param name="depth" select="number(1)" />
						</xsl:call-template>
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
				<xsl:for-each select="/database/table[@codeTable = 'true' and @name=current()/foreign-key/@foreignTable]">
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
								<xsl:apply-templates select="column" mode="form" />
							</xsl:element>
							<xf:submit submission="{concat('s',position()+1)}">
								<xf:label>Save</xf:label>
							</xf:submit>
							<xf:trigger>
								<xf:label>Back</xf:label>
								<xf:toggle ev:event="DOMActivate" case="{$home}" />
							</xf:trigger>
							<div class="error">
								<xf:output bind="{concat('error',position()+1)}">
									<xf:label>Error:</xf:label>
								</xf:output>
							</div>
						</div>
					</xf:case>
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
					<xf:input ref="instance('id1')/id">
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
					<xf:group ref="{concat(&quot;instance('view1')/gml:featureMember/&quot;,$namespace,lower-case(@name))}">
						<label>
							<xsl:text>EDIT </xsl:text>
							<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
						</label>
						<table class="edit">
							<tbody>
								<xsl:for-each select="column[not(@primaryKey = 'true')]">
									<tr>
										<td>
											<xsl:apply-templates select="." mode="display" />
										</td>
										<td>
											<xf:trigger>
												<xf:label>Edit</xf:label>
												<xf:action ev:event="DOMActivate">
													<xf:insert nodeset="instance('edit1')/wfs:Update/wfs:Property" if="{concat(&quot;count(instance('edit1')/wfs:Update/wfs:Property[wfs:Name = '&quot;,fn:property-name(@name),&quot;']) = 0&quot;)}" at="1" position="before" />
													<xf:setvalue ref="instance('edit1')/wfs:Update/wfs:Property[position()=1]/wfs:Name" value="{concat(&quot;'&quot;,fn:property-name(@name),&quot;'&quot;)}"/>
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
							<xf:label>Reset</xf:label>
							<xf:action ev:event="DOMActivate">
								<xf:reset model="edit_model"/>
								<xf:delete nodeset="instance('edit_resp')/ows:Exception" />
								<xf:delete nodeset="instance('edit_resp')//ogc:FeatureId/@fid" />
							</xf:action>
						</xf:trigger>
						<div class="error">
							<xf:output bind="edit_error">
								<xf:label>Error:</xf:label>
							</xf:output>
						</div>
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
				<xf:output ref="{concat($namespace,lower-case(@name))}">
					<xf:label>
						<xsl:call-template name="proper-case">
							<xsl:with-param name="toconvert" select="replace(replace(lower-case(@name),'_id$',''),'_',' ')" />
						</xsl:call-template>
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
					<xsl:with-param name="ref" select="concat(&quot;instance('edit1')/wfs:Update/wfs:Property[wfs:Name='&quot;,fn:property-name(@name),&quot;']/wfs:Value/@fid&quot;)" />
					<xsl:with-param name="include_label" select="'false'" />
					<xsl:with-param name="include_add" select="'false'" />
					<xsl:with-param name="include_action" select="'false'" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xf:input ref="{concat(&quot;instance('edit1')/wfs:Update/wfs:Property[wfs:Name='&quot;,fn:property-name(@name),&quot;']/wfs:Value&quot;)}">
				</xf:input>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 
		template to build the edit form
	-->
	<xsl:template name="delete">
		<xf:group ref="{concat(&quot;instance('delete1')/gml:featureMember/&quot;,$namespace,lower-case(@name))}">
			<div class="form">
				<label>
					<xsl:text>EDIT </xsl:text>
					<xsl:value-of select="translate(upper-case(@name),'_',' ')" />
				</label>
				<table class="edit">
					<tbody>
						<xsl:for-each select="column">
							<tr>
								<td>
									<xsl:apply-templates select="." mode="display" />
								</td>
								<td>
									<xf:trigger>
										<xf:label>Edit</xf:label>
										<xf:action ev:event="DOMActivate">
											<xf:insert nodeset="" origin="" if="" />
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
				<xf:submit submission="edit_submit">
					<xf:label>Save</xf:label>
				</xf:submit>
				<xf:trigger>
					<xf:label>Reset</xf:label>
					<xf:action ev:event="DOMActivate">
						<xf:reset model="delete_model"/>
						<xf:delete nodeset="instance('edit_submit')/ows:Exception" />
						<xf:delete nodeset="instance('edit_submit')//ogc:FeatureId/@fid" />
					</xf:action>
				</xf:trigger>
				<div class="error">
					<xf:output bind="edit_error">
						<xf:label>Error:</xf:label>
					</xf:output>
				</div>
				<div class="success">
					<xf:output bind="edit_success">
						<xf:label>
						</xf:label>
					</xf:output>
				</div>
			</div>
		</xf:group>
	</xsl:template>
	<!--
		Creates an xform:group for the current table (feature) and processes the columns
	-->
	<xsl:template name="form-feature">
		<xsl:param name="table" />
		<xsl:param name="depth" />
		<xsl:if test="$depth &lt;= $max_depth">
			<xsl:element name="xf:group">
				<xsl:attribute name="ref">
					<xsl:choose>
						<xsl:when test="$depth = 1">
							<xsl:text>wfs:Insert/wfs:FeatureCollection/gml:featureMember/</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="concat($namespace,lower-case($table/@name),'_ref')" />
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="concat($namespace,lower-case($table/@name))" />
				</xsl:attribute>
				<xsl:if test="$depth=1">
					<xsl:attribute name="model">model1</xsl:attribute>
				</xsl:if>
				<!-- handle each column -->
				<xsl:apply-templates select="column" mode="form" />
			</xsl:element>
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
			<xsl:element name="{if(not(@type = 'VARCHAR') or @size &lt;= 20) then 'xf:input' else 'xf:textarea'}">
				<xsl:attribute name="ref">
					<xsl:value-of select="concat($namespace,lower-case(@name))" />
				</xsl:attribute>
				<xsl:attribute name="incremental">
					<xsl:text>true()</xsl:text>
				</xsl:attribute>
				<xf:label>
					<xsl:call-template name="proper-case">
						<xsl:with-param name="toconvert" select="replace(replace(lower-case(@name),'_id$',''),'_',' ')" />
					</xsl:call-template>
				</xf:label>
				<xsl:call-template name="help">
					<xsl:with-param name="key">
						<xsl:value-of select="@name" />
					</xsl:with-param>
				</xsl:call-template>
				<xsl:if test="@type = 'VARCHAR'">
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
		<xsl:param name="include_action">true</xsl:param>
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
				<xsl:if test="$include_action = 'true'">
					<xsl:element name="xf:action">
						<xsl:attribute name="ev:event">
							<xsl:text>xforms-value-changed</xsl:text>
						</xsl:attribute>
						<!--  
						copy selected subfeature into submission
					-->
						<xsl:element name="xf:insert">
							<!-- where to put it -->
							<xsl:attribute name="nodeset">
								<xsl:value-of select="'context()/../../*'" />
							</xsl:attribute>
							<!-- what to put there -->
							<xsl:attribute name="origin">
								<xsl:value-of select="concat(&quot;instance('&quot;,$feature_name,&quot;')/gml:featureMember/&quot;,$namespace,$feature_name,'[@gml:id=current()]')" />
							</xsl:attribute>
						</xsl:element>
						<!-- delete the dummy one -->
						<xsl:element name="xf:delete">
							<xsl:attribute name="nodeset">
								<xsl:value-of select="'context()/../../*'" />
							</xsl:attribute>
							<xsl:attribute name="at">1</xsl:attribute>
						</xsl:element>
					</xsl:element>
				</xsl:if>
			</xsl:element>
			<!-- add an 'Add' button to create a new feature in this select1 list -->
			<xsl:if test="$include_add = 'true'">
				<xf:trigger>
					<xf:label>+</xf:label>
					<xf:action ev:event="DOMActivate">
						<xf:toggle case="{concat('new_',$feature_name)}" />
					</xf:action>
				</xf:trigger>
			</xsl:if>
		</div>
		</xf:group>	
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
					<xsl:text>
					</xsl:text>
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
  		<xsl:param name="name" as="xsd:string"/>
  		<xsl:sequence  select="concat($namespace,replace(lower-case($name),'_id$','_ref'))" />    
	</xsl:function>

	
</xsl:stylesheet>