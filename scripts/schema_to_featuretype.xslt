<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:emii="http://www.imos.org.au/emii" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:deegreewfs="http://www.deegree.org/wfs">
    <xsl:output method="xml" encoding="UTF-8" indent="yes" />
    <xsl:include href="globals.xslt" />
    <xsl:template match="/">
        <xsl:call-template name="validate"/>
        <xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:deegreewfs="http://www.deegree.org/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:emii="http://www.imos.org.au/emii">
            <!--xsl:attribute name="{concat('xmlns:',$namespace_name)}"><xsl:value-of select="$namespace_uri"/></xsl:attribute-->
            <xsl:attribute name="targetNamespace"><xsl:value-of select="$namespace_uri"/></xsl:attribute>
			<xsd:import namespace="http://www.opengis.net/gml" schemaLocation="http://schemas.opengis.net/ gml/3.1.1/base/feature.xsd" />
			<xsd:import namespace="http://www.opengis.net/gml" schemaLocation="http://schemas.opengis.net/gml/3.1.1/base/geometryAggregates.xsd" />
			<xsd:annotation>
				<xsd:appinfo>
					<deegreewfs:Prefix><xsl:value-of select="$namespace_name"/></deegreewfs:Prefix>
					<deegreewfs:Backend>POSTGIS</deegreewfs:Backend>
					<deegreewfs:DefaultSRS>EPSG:4326</deegreewfs:DefaultSRS>
				    <deegreewfs:SuppressXLinkOutput>true</deegreewfs:SuppressXLinkOutput>
				    <JDBCConnection xmlns="http://www.deegree.org/jdbc">
						<Driver>org.postgresql.Driver</Driver>
						<Url>jdbc:postgresql://obsidian.bluenet.utas.edu.au:5432/steve</Url>
						<User>steve</User>
						<Password>66CoStHo</Password>
						<SecurityConstraints />
						<Encoding>iso-8859-1</Encoding>
					</JDBCConnection>
				</xsd:appinfo>
			</xsd:annotation>
			<xsl:for-each select="//table">
				<xsl:element name="xsd:element">
					<xsl:attribute name="name">
						<xsl:value-of select="lower-case(@name)" />
					</xsl:attribute>
					<xsl:attribute name="substitutionGroup">gml:_Feature</xsl:attribute>
					<xsl:attribute name="type">
						<xsl:value-of select="$namespace"/>
						<xsl:value-of select="lower-case(@name)" />
						<xsl:text>_type</xsl:text>
					</xsl:attribute>
					<xsd:annotation>
						<xsd:appinfo>
							<deegreewfs:table>
                                <xsl:value-of select="$db_schema"/>.<xsl:value-of select="lower-case(@name)"/>
							</deegreewfs:table>
							<xsl:element name="deegreewfs:gmlId">
								<xsl:attribute name="prefix">
									<xsl:value-of select="$feature_prefix" />
									<xsl:value-of select="lower-case(@name)" />
									<xsl:text>.</xsl:text>
                                </xsl:attribute>
                                <xsl:for-each select="column[@primaryKey='true']">
								    <xsl:element name="deegreewfs:MappingField">
									    <xsl:attribute name="field">
										    <xsl:value-of select="lower-case(@name)" />
								    	</xsl:attribute>
									    <xsl:attribute name="type">
										    <xsl:value-of select="@type" />
									    </xsl:attribute>
								    </xsl:element>
                                </xsl:for-each>
								<deegreewfs:IdGenerator type="DB_SEQ">
									<deegreewfs:param name="sequence">
										<xsl:value-of select="$db_schema"/>.<xsl:value-of select="lower-case(@name)"/>
										<xsl:text>_serial</xsl:text>
									</deegreewfs:param>
								</deegreewfs:IdGenerator>
                                <deegreewfs:IdentityPart>
                                    <xsl:choose>
							            <xsl:when test="unique[@name='UNIQUE_INDEX']">false</xsl:when>
							            <xsl:otherwise>true</xsl:otherwise>
                                    </xsl:choose>
                                </deegreewfs:IdentityPart>
							</xsl:element>
							<deegreewfs:visible>true</deegreewfs:visible>
							<deegreewfs:transaction update="true" delete="true" insert="true" />
						</xsd:appinfo>
					</xsd:annotation>
				</xsl:element>
				<xsl:element name="xsd:complexType">
					<xsl:attribute name="name">
						<xsl:value-of select="lower-case(@name)" />
						<xsl:text>_type</xsl:text>
					</xsl:attribute>
					<xsd:complexContent>
						<xsd:extension base="gml:AbstractFeatureType">
							<xsd:sequence>
								<xsl:apply-templates select="column" />
							</xsd:sequence>
						</xsd:extension>
					</xsd:complexContent>
				</xsl:element>
			</xsl:for-each>
			<!-- add the views -->
			<xsl:for-each select="//view">
				<xsl:element name="xsd:element">
					<xsl:attribute name="name">
						<xsl:value-of select="lower-case(@name)" />
					</xsl:attribute>
					<xsl:attribute name="substitutionGroup">gml:_Feature</xsl:attribute>
					<xsl:attribute name="type">
						<xsl:value-of select="$namespace"/>
						<xsl:value-of select="lower-case(@name)" />
						<xsl:text>_type</xsl:text>
					</xsl:attribute>
					<xsd:annotation>
						<xsd:appinfo>
							<deegreewfs:table>
								<xsl:value-of select="$db_schema"/>.<xsl:value-of select="lower-case(@name)"/>
							</deegreewfs:table>
							<xsl:element name="deegreewfs:gmlId">
								<xsl:attribute name="prefix">
									<xsl:value-of select="$feature_prefix" />
									<xsl:value-of select="lower-case(@name)" />
									<xsl:text>.</xsl:text>
								</xsl:attribute>
								<xsl:element name="deegreewfs:MappingField">
									<xsl:attribute name="field">
										<xsl:value-of select="lower-case(column[@primaryKey='true']/@name)" />
									</xsl:attribute>
									<xsl:attribute name="type">
										<xsl:value-of select="column[@primaryKey='true']/@type" />
									</xsl:attribute>
								</xsl:element>
								<deegreewfs:IdentityPart>false</deegreewfs:IdentityPart>
							</xsl:element>
							<deegreewfs:visible>true</deegreewfs:visible>
							<deegreewfs:transaction update="false" delete="false" insert="false" />
						</xsd:appinfo>
					</xsd:annotation>
				</xsl:element>
				<xsl:element name="xsd:complexType">
					<xsl:attribute name="name">
						<xsl:value-of select="lower-case(@name)" />
						<xsl:text>_type</xsl:text>
					</xsl:attribute>
					<xsd:complexContent>
						<xsd:extension base="gml:AbstractFeatureType">
							<xsd:sequence>
								<xsl:apply-templates select="column" />
							</xsd:sequence>
						</xsd:extension>
					</xsd:complexContent>
				</xsl:element>
			</xsl:for-each>
		</xsd:schema>
    </xsl:template>

    <xsl:template name="validate">
        <xsl:for-each select="table[count(field/@primaryKey='true')=0]">
            table <xsl:value-of select="@name"/> has no primary key
        </xsl:for-each>
    </xsl:template> 

	<!-- column to property -->
	<xsl:template match="column">
		<xsl:choose>
			<xsl:when test="@primaryKey='true'">
				<!-- don't want the primary key as that is in the gml:Id attribute -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="xsd:element">
					<!-- assume anything ending in _ID is refering to a subfeature or a code-list so drop the _ID and add _ref -->
					<xsl:attribute name="name">
						<xsl:choose>
							<xsl:when test="../foreign-key[reference/@local=current()/@name]">
								<xsl:value-of select="concat(lower-case(../foreign-key[reference/@local=current()/@name][1]/@foreignTable),'_ref')" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="lower-case(replace(@name,'_ID',''))" />
                            </xsl:otherwise>
                        </xsl:choose>
					</xsl:attribute>
					<xsl:attribute name="minOccurs">
						<xsl:choose>
							<xsl:when test="@required='true'">
								<xsl:text>1</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>0</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<xsl:attribute name="maxOccurs">1</xsl:attribute>
					<xsl:attribute name="type">
						<xsl:choose>
							<xsl:when test="../foreign-key[reference/@local=current()/@name]">
								<!-- foreign-key, so a subfeature -->
								<xsl:text>gml:FeaturePropertyType</xsl:text>
							</xsl:when>
							<xsl:when test="@type = 'GEOMETRY'">
								<!-- foreign-key, so a subfeature -->
								<xsl:text>gml:GeometryPropertyType</xsl:text>
							</xsl:when>
							<xsl:when test="@type = 'INTEGER'">
								<xsl:text>xsd:integer</xsl:text>
							</xsl:when>
							<xsl:when test="@type = 'VARCHAR'">
								<xsl:text>xsd:string</xsl:text>
							</xsl:when>
							<xsl:when test="@type = 'TIMESTAMP'">
								<xsl:text>xsd:dateTime</xsl:text>
							</xsl:when>
							<xsl:when test="@type = 'DATE'">
								<xsl:text>xsd:date</xsl:text>
							</xsl:when>
							<xsl:when test="@type = 'LONGVARCHAR'">
								<xsl:text>xsd:string</xsl:text>
							</xsl:when>
							<xsl:when test="@type = 'DECIMAL'">
								<xsl:text>xsd:decimal</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>xsd:string</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<xsd:annotation>
						<xsd:appinfo>
							<deegreewfs:IdentityPart>
								<xsl:choose>
									<xsl:when test="../unique[@name='UNIQUE_INDEX' and unique-column[@name=current()/@name]]">
										<xsl:text>true</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>false</xsl:text>
									</xsl:otherwise>
								</xsl:choose>
							</deegreewfs:IdentityPart>
							<xsl:choose>
								<xsl:when test="../foreign-key[reference/@local=current()/@name]">
									<xsl:element name="deegreewfs:Content">
										<xsl:attribute name="type">
											<xsl:value-of select="$namespace"/>
											<xsl:value-of select="lower-case(../foreign-key[reference/@local=current()/@name][1]/@foreignTable)" />
										</xsl:attribute>
										<deegreewfs:Relation>
											<deegreewfs:From fk="true">
												<xsl:element name="deegreewfs:MappingField">
													<xsl:attribute name="field">
														<xsl:value-of select="lower-case(@name)" />
													</xsl:attribute>
													<xsl:attribute name="type">INTEGER</xsl:attribute>
												</xsl:element>
											</deegreewfs:From>
											<deegreewfs:To>
												<xsl:element name="deegreewfs:MappingField">
													<xsl:attribute name="field">
														<xsl:value-of select="lower-case(../foreign-key[reference/@local=current()/@name][1]/reference/@foreign)" />
													</xsl:attribute>
													<xsl:attribute name="type">INTEGER</xsl:attribute>
												</xsl:element>
											</deegreewfs:To>
										</deegreewfs:Relation>
									</xsl:element>
								</xsl:when>
								<xsl:when test="@type='GEOMETRY'">
									<deegreewfs:Content>
                                        <deegreewfs:SRS>EPSG:4326</deegreewfs:SRS>
                                        <deegreewfs:MappingField field="{@name}" type="GEOMETRY" srs="4326"/>
									</deegreewfs:Content>
								</xsl:when>
								<xsl:otherwise>
									<deegreewfs:Content>
										<xsl:element name="deegreewfs:MappingField">
											<xsl:attribute name="field">
												<xsl:value-of select="lower-case(@name)" />
											</xsl:attribute>
											<xsl:attribute name="type">
												<xsl:value-of select="@type" />
											</xsl:attribute>
										</xsl:element>
									</deegreewfs:Content>
								</xsl:otherwise>
							</xsl:choose>
						</xsd:appinfo>
					</xsd:annotation>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
