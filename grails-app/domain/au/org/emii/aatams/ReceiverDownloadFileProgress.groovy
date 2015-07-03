package au.org.emii.aatams

class ReceiverDownloadFileProgress  {
    Integer percentComplete
    
    static belongsTo = [receiverDownloadFile: ReceiverDownloadFile]
    
    static constraints =  {
        percentComplete(nullable: true, min: 0, max: 100)
    }
}
