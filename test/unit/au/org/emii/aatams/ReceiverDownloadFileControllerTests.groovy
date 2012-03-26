package au.org.emii.aatams

import grails.test.*
import org.springframework.jdbc.core.JdbcTemplate

class ReceiverDownloadFileControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testRefreshDetectionCountPerStationView()
	{
		controller.metaClass.getRefreshStatement =
		{
			def ret = new JdbcTemplate()
			ret.metaClass.execute =
			{
				String sql ->
				
				assertEquals('''SELECT refresh_matview('detection_count_per_station_mv');''', sql)
			}

			return ret		
		}
		
		controller.refreshDetectionCountPerStationView()
	}
}
