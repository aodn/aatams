package au.org.emii.aatams

enum ReceiverDownloadFileType 
{
    CSV('csv'),
    VRL('vrl'),
    RLD('rld')
    
    String type
    
    ReceiverDownloadFileType(type)
    {
        this.type = type
    }
}
