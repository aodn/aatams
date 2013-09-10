package au.org.emii.aatams

class DetectionCountPerStationRefreshMaterializedViewJob extends RefreshMaterializedViewJob
{
    String getViewName()
    {
        return 'detection_count_per_station_mv'
    }
}
