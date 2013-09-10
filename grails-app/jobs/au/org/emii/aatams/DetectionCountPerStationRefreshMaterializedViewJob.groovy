package au.org.emii.aatams

class DetectionCountPerStationRefreshMaterializedViewJob extends RefreshMaterializedViewJob
{
    static triggers =
    {
        // Execute daily at 8pm.
        cron name: 'refreshDetectionCountPerStationMaterializedDailyTrigger', cronExpression: "0 20 0 * * ?"
    }

    String getViewName()
    {
        return 'detection_count_per_station_mv'
    }
}
