package au.org.emii.aatams

enum ReceiverDownloadFileType 
{
    DETECTIONS_CSV('detections (CSV)'),
    EVENTS_CSV('events (CSV)'),
    VRL('VRL'),
    RLD('RLD')
    
    String type
    
    ReceiverDownloadFileType(type)
    {
        this.type = type
    }
    
    String toString()
    {
        return type
    }

    static list()
    {
        [DETECTIONS_CSV ,EVENTS_CSV, VRL('VRL'), RLD('RLD')]
    }
}
