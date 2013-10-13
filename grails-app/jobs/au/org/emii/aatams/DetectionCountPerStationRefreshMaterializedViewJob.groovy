package au.org.emii.aatams

class DetectionCountPerStationRefreshMaterializedViewJob extends RefreshMaterializedViewJob
{
    static triggers =
    {
        // Execute on Sundays at 7am.
        // Note: first field is seconds (non-standard for cron).
        cron name: 'refreshDetectionCountPerStationMaterializedDailyTrigger', cronExpression: "0 0 7 * * 1"
    }

    String getViewName()
    {
        return 'detection_count_per_station_mv'
    }
}
