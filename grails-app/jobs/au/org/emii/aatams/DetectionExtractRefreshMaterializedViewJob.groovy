package au.org.emii.aatams

class DetectionExtractRefreshMaterializedViewJob extends RefreshMaterializedViewJob
{
    String getViewName()
    {
        return 'detection_extract_view_mv'
    }
}
