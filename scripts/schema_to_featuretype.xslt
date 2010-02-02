<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Liquid XML Studio - FREE Community Edition 7.0.4.795 (http://www.liquid-technologies.com) -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:xf="http://www.w3.org/2002/xforms" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:deegreewfs="http://www.deegree.org/wfs">
	<xsl:output method="xml" encoding="UTF-8" indent="yes" />
	<xsl:template match="/">
		<xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:aatams="http://www.imos.org.au/aatams" xmlns:deegreewfs="http://www.deegree.org/wfs" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" attributeFormDefault="unqualified" elementFormDefault="qualified" targetNamespace="http://www.imos.org.au/aatams">
			<xsd:import namespace="http://www.opengis.net/gml" schemaLocation="http://schemas.opengis.net/ gml/3.1.1/base/feature.xsd" />
			<xsd:import namespace="http://www.opengis.net/gml" schemaLocation="http://schemas.opengis.net/gml/3.1.1/base/geometryAggregates.xsd" />
			<xsd:annotation>
				<xsd:appinfo>
					<deegreewfs:Prefix>aatams</deegreewfs:Prefix>
					<deegreewfs:Backend>POSTGIS</deegreewfs:Backend>
					<deegreewfs:DefaultSRS>EPSG:4326</deegreewfs:DefaultSRS>
				    <deegreewfs:SuppressXLinkOutput>true</deegreewfs:SuppressXLinkOutput>
				    <JDBCConnection xmlns="http://www.deegree.org/jdbc">
						<Driver>org.postgresql.Driver</Driver>
						<Url>jdbc:postgresql://obsidian.bluenet.utas.edu.au:5432/aatams</Url>
						<User>steve</User>
						<Password>66CoStHo</Password>
						<SecurityConstraints />
						<Encoding>iso-8859-1</Encoding>
					</JDBCConnection>
					<!-- JDBCConnection xmlns="http://www.deegree.org/jdbc">
						<Driver>oracle.jdbc.driver.OracleDriver</Driver>
						<Url>jdbc:oracle:thin:@obsidian.bluenet.utas.edu.au:1521:orcl</Url>
						<User>AATAMS</User>
						<Password>boomerSIMS</Password>
						<SecurityConstraints />
						<Encoding>iso-8859-1</Encoding>
					</JDBCConnection-->
				</xsd:appinfo>
			</xsd:annotation>
			<xsl:for-each select="//table">
				<xsl:element name="xsd:element">
					<xsl:attribute name="name">
						<xsl:value-of select="lower-case(@name)" />
					</xsl:attribute>
					<xsl:attribute name="substitutionGroup">gml:_Feature</xsl:attribute>
					<xsl:attribute name="type">
						<xsl:text>aatams:</xsl:text>
						<xsl:value-of select="lower-case(@name)" />
						<xsl:text>_type</xsl:text>
					</xsl:attribute>
					<xsd:annotation>
						<xsd:appinfo>
							<deegreewfs:table>
								<xsl:text>aatams.</xsl:text><xsl:value-of select="lower-case(@name)"/>
							</deegreewfs:table>
							<xsl:element name="deegreewfs:gmlId">
								<xsl:attribute name="prefix">
									<xsl:text>aatams.</xsl:text>
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
								<deegreewfs:IdGenerator type="DB_SEQ">
									<deegreewfs:param name="sequence">
										<xsl:value-of select="lower-case(@name)" />
										<xsl:text>_serial</xsl:text>
									</deegreewfs:param>
								</deegreewfs:IdGenerator>
								<deegreewfs:IdentityPart>false</deegreewfs:IdentityPart>
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
						<xsl:text>aatams:</xsl:text>
						<xsl:value-of select="lower-case(@name)" />
						<xsl:text>_type</xsl:text>
					</xsl:attribute>
					<xsd:annotation>
						<xsd:appinfo>
							<deegreewfs:table>
								<xsl:text>aatams.</xsl:text><xsl:value-of select="lower-case(@name)" />
							</deegreewfs:table>
							<xsl:element name="deegreewfs:gmlId">
								<xsl:attribute name="prefix">
									<xsl:text>aatams.</xsl:text>
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
	<!-- column to property -->
	<xsl:template match="column">
		<xsl:choose>
			<xsl:when test="@primaryKey='true'">
				<!-- don't want the primary key as that is in the gml:Id attribute -->
			</xsl:when>
			<xsl:when test="@name='PROJECT_ROLE_PERSON_ID'">
				<!-- substiture aatams:project_person based on project_person view -->
				<xsl:element name="xsd:element">
					<!-- assume anything ending in _ID is refering to a subfeature or a code-list so drop the _ID and add _ref -->
					<xsl:attribute name="name">project_person_ref</xsl:attribute>
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
						<xsl:text>gml:FeaturePropertyType</xsl:text>
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
							<xsl:element name="deegreewfs:Content">
								<xsl:attribute name="type"><xsl:text>aatams:project_person</xsl:text></xsl:attribute>
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
											<xsl:attribute name="field">project_role_person_id</xsl:attribute>
											<xsl:attribute name="type">INTEGER</xsl:attribute>
										</xsl:element>
									</deegreewfs:To>
								</deegreewfs:Relation>
							</xsl:element>
						</xsd:appinfo>
					</xsd:annotation>
				</xsl:element>
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
											<xsl:text>aatams:</xsl:text>
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
	<!-- extra views needed for user interface -->
	<xsl:template name="view_features">
		<xsd:element name="project_person" substitutionGroup="gml:_Feature" type="aatams:project_person_type">
			<xsd:annotation>
				<xsd:appinfo>
					<deegreewfs:table>PROJECT_PERSON</deegreewfs:table>
					<deegreewfs:gmlId prefix="aatams.project_person.">
						<deegreewfs:MappingField field="project_role_person_id" type="INTEGER" />
						<deegreewfs:IdentityPart>false</deegreewfs:IdentityPart>
					</deegreewfs:gmlId>
					<deegreewfs:visible>true</deegreewfs:visible>
					<deegreewfs:transaction update="false" delete="false" insert="false" />
				</xsd:appinfo>
			</xsd:annotation>
		</xsd:element>
		<xsd:complexType name="project_person_type">
			<xsd:complexContent>
				<xsd:extension base="gml:AbstractFeatureType">
					<xsd:sequence>
						<xsd:element name="project_fid" minOccurs="1" maxOccurs="1" type="xsd:string">
							<xsd:annotation>
								<xsd:appinfo>
									<deegreewfs:IdentityPart>false</deegreewfs:IdentityPart>
									<deegreewfs:Content>
										<deegreewfs:MappingField field="project_fid" type="VARCHAR" />
									</deegreewfs:Content>
								</xsd:appinfo>
							</xsd:annotation>
						</xsd:element>
						<xsd:element name="project_name" minOccurs="1" maxOccurs="1" type="xsd:string">
							<xsd:annotation>
								<xsd:appinfo>
									<deegreewfs:IdentityPart>false</deegreewfs:IdentityPart>
									<deegreewfs:Content>
										<deegreewfs:MappingField field="project_name" type="VARCHAR" />
									</deegreewfs:Content>
								</xsd:appinfo>
							</xsd:annotation>
						</xsd:element>
						<xsd:element name="person_fid" minOccurs="1" maxOccurs="1" type="xsd:string">
							<xsd:annotation>
								<xsd:appinfo>
									<deegreewfs:IdentityPart>false</deegreewfs:IdentityPart>
									<deegreewfs:Content>
										<deegreewfs:MappingField field="project_fid" type="VARCHAR" />
									</deegreewfs:Content>
								</xsd:appinfo>
							</xsd:annotation>
						</xsd:element>
						<xsd:element name="person_role" minOccurs="1" maxOccurs="1" type="xsd:string">
							<xsd:annotation>
								<xsd:appinfo>
									<deegreewfs:IdentityPart>false</deegreewfs:IdentityPart>
									<deegreewfs:Content>
										<deegreewfs:MappingField field="person_role" type="VARCHAR" />
									</deegreewfs:Content>
								</xsd:appinfo>
							</xsd:annotation>
						</xsd:element>
					</xsd:sequence>
				</xsd:extension>
			</xsd:complexContent>
		</xsd:complexType>
	</xsl:template>
</xsl:stylesheet>
