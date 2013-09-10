package au.org.emii.aatams

class DetectionCountPerStationRefreshMaterializedViewJob extends RefreshMaterializedViewJob
{
    static triggers =
    {
        // Execute daily at 8pm.
        // Note: first field is seconds (non-standard for cron).
        cron name: 'refreshDetectionCountPerStationMaterializedDailyTrigger', cronExpression: "0 0 20 * * *"
    }

    String getViewName()
    {
        return 'detection_count_per_station_mv'
    }
}
