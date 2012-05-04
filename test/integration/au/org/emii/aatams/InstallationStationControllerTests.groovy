package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*
import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class InstallationStationControllerTests extends AbstractControllerUnitTestCase 
{
	def dataSource
	def slurper = new XmlSlurper()
	
	void testExecuteInstallationStationNoFilter()
	{
		assertExport([:], "testExecuteInstallationStationNoFilter")
	}
	
	void testExecuteInstallationStationByProject()
	{
		assertExport([installation: [project: [eq: ["name", "Seal Count"]]]], "testExecuteInstallationStationByProject")
	}
	
	void testExecuteStationKmlExtract()
	{
		def sql = new Sql(dataSource)
		def viewName = ConfigurationHolder.config.rawDetection.extract.view.name
		def viewSelect = ConfigurationHolder.config.rawDetection.extract.view.select
		sql.execute ('create view ' + viewName + ' as ' + viewSelect)
		
		InstallationStation.metaClass.toKmlDescription = { "some description" }
		controller.params.format = "KML"
		
		assertExport([:], "testExecuteStationKmlExtract")
	}
}
