package au.org.emii.aatams.report

import grails.test.*

class ReportTagLibTests extends TagLibUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testReportFilterParameter() 
	{
		// Need grails 1.4 to test template rendering.
		
		def attrs = [template:"/report/filter/listTemplate", model:[:]]
		def body = {''}
		
		tagLib.reportFilterParameter(attrs, body)
    }
}
