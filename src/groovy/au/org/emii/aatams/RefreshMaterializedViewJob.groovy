package au.org.emii.aatams

import org.springframework.jdbc.core.JdbcTemplate

abstract class RefreshMaterializedViewJob
{
    def dataSource

    def execute()
    {
        long startTime = System.currentTimeMillis()
        log.info("'${getViewName()}': refreshing materialized view...")
        JdbcTemplate refreshStatement = new JdbcTemplate(dataSource)
        refreshStatement.execute("SELECT refresh_matview('${getViewName()}');")
        long endTime = System.currentTimeMillis()
        log.info("'${getViewName()}': materialized view refreshed, time taken (ms): " + (endTime - startTime))
    }

    abstract String getViewName()
}
