package au.org.emii.aatams.report.filter

import au.org.emii.aatams.detection.*
import java.util.Map;

import grails.test.*

class ReportFilterFactoryServiceTests extends GrailsUnitTestCase 
{
	def reportFilterFactoryService
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(ReportFilterFactoryServiceTests, true)
		reportFilterFactoryService = new ReportFilterFactoryService()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testNewFilterEquals()
	{
		def params = [detectionSurgeries:[surgery:[release:[animal:[species:[SPCODE:"37010003"]]]]]]
		assertNewFilter(
			ValidDetection, 
			new EqualsReportFilterCriterion(params), 
			[eq:params])
    }

    void testNewFilterIn()
	{
		def params = [detectionSurgeries:[surgery:[release:[animal:[species:[SPCODE:["37010003", "37010004"]]]]]]]
		assertNewFilter(
			ValidDetection, 
			new InReportFilterCriterion(params),
			[in:params])
    }
	
	void testNewFilterBetween()
	{
		def params = [detectionSurgeries:[surgery:[release:[animal:[species:[SPCODE:["37010003", "37010004"]]]]]]]
		assertNewFilter(
			ValidDetection, 
			new BetweenReportFilterCriterion(params),
			[between:params])
	}
	
	private void assertNewFilter(domain, criterion, params)
	{
		ReportFilter expectedFilter = new ReportFilter(domain)
		expectedFilter.addCriterion(criterion)
		ReportFilter factoryFilter = reportFilterFactoryService.newFilter(domain, params)
		
		assertEquals(expectedFilter, factoryFilter)
	}
	
	void testRemoveEmptyFilterParameters()
	{
		def params = [receiverDeployment:[station:[installation:[project:[name:'Seal Count']]]], 
				      detectionSurgeries:['surgery.release.animal.species.SPCODE':'']]
		reportFilterFactoryService.removeEmptyFilterParameters(params)
		
		assertEquals([receiverDeployment:[station:[installation:[project:[name:'Seal Count']]]]], 
					 params)
	}
}
