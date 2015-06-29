package au.org.emii.aatams

enum ReceiverDownloadFileType
{
    DETECTIONS_CSV('detections (CSV)'),
    EVENTS_CSV('events (CSV)'),
    VRL('VRL'),
    RLD('RLD'),
    VUE_XML_ZIPPED('VUE XML zip file')

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
        [DETECTIONS_CSV, EVENTS_CSV, VRL, RLD, VUE_XML_ZIPPED]
    }

    static detectionTypes()
    {
        [DETECTIONS_CSV, VRL, RLD]
    }

    static eventTypes()
    {
        [EVENTS_CSV]
    }

    static String getExtension(ReceiverDownloadFileType type)
    {
        switch (type)
        {
            case EVENTS_CSV:
                return "csv"
            case DETECTIONS_CSV:
                return "csv"
            case VRL:
                return "vrl"
            case RLD:
                return "rld"
            case VUE_XML_ZIPPED:
                return "zip"
            default:
                return null
        }
    }

    static String getCategory(ReceiverDownloadFileType type)
    {
        switch (type)
        {
            case EVENTS_CSV:
                return "events"
            case DETECTIONS_CSV:
            case VRL:
            case RLD:
            case VUE_XML_ZIPPED:
                return "detections"
            default:
                return null
        }
    }
}
