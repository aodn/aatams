package au.org.emii.aatams

import org.springframework.jdbc.core.JdbcTemplate

class RefreshMaterializedViewsJob
{
	def dataSource

	static triggers =
	{
		// Execute daily at 8pm.
		cron name: 'refreshMaterializedDailyTrigger', cronExpression: "0 20 0 * * ?"
	}

	def execute()
	{
		refreshDetectionCountPerStationView()
	}

	private void refreshDetectionCountPerStationView()
	{
		long startTime = System.currentTimeMillis()
		log.info("Refreshing 'detection counts' materialized view...")
		JdbcTemplate refreshStatement = new JdbcTemplate(dataSource)
		refreshStatement.execute('''SELECT refresh_matview('detection_extract_view_mv');''')
		refreshStatement.execute('''SELECT refresh_matview('detection_count_per_station_mv');''')
		long endTime = System.currentTimeMillis()
		log.info("'detection counts' materialized view refreshed, time taken (ms): " + (endTime - startTime))
	}
}
