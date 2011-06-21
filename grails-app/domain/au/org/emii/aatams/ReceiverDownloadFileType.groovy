package au.org.emii.aatams

enum ReceiverDownloadFileType 
{
    CSV('csv'),
    VRL('vrl'),
    RLD('rld'),
    INVALID('invalid')
    
    String type
    
    ReceiverDownloadFileType(type)
    {
        this.type = type
    }
    
    static ReceiverDownloadFile fromPath(String path)
    {
        if (path.endsWith(CSV.type))
        {
            return CSV
        }
        else if (path.endsWith(VRL.type))
        {
            return VRL
        }
        else if (path.endsWith(RLD.type))
        {
            return RLD
        }
        
        return INVALID
    }
}
