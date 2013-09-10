package au.org.emii.aatams

class DetectionExtractRefreshMaterializedViewJob extends RefreshMaterializedViewJob
{
    static triggers =
    {
        // Execute daily at 8pm.
        cron name: 'refreshDetectionExtractMaterializedDailyTrigger', cronExpression: "0 20 0 * * ?"
    }

    String getViewName()
    {
        return 'detection_extract_view_mv'
    }
}
