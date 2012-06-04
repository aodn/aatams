package au.org.emii.aatams.bulk

import grails.test.*

class ReceiverLoaderTests extends GrailsUnitTestCase 
{
	ReceiverLoader processor
	
    protected void setUp() 
	{
        super.setUp()
		
		processor = new ReceiverLoader()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testEmpty()
	{
		try
		{
			processor.load(null)
			fail()
		}
		catch (BulkImportException e)
		{
			assertEquals("Invalid receivers data: data is empty.", e.message)
		}	
	}
	
	void testInvalidFormat()
	{
//		def receiversText = '''"RCV_ID","RCV_SERIAL_NO","RCV_MODEL_CODE","RCV_OWNER","RCV_COMMENTS","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
		def receiversText = '''"RCV_SERIAL_NO","RCV_MODEL_CODE","RCV_OWNER","RCV_COMMENTS","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
		1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"
		1663,"VR2",,,15/5/2008 16:01:56,"TAG",15/5/2008 16:01:56,"TAG"
'''

		try
		{
			processor.load(new ByteArrayInputStream(receiversText.bytes))
			fail()
		}
		catch (BulkImportException e)
		{
			assertEquals("Invalid receivers data format.", e.message)
		}
	}
}
