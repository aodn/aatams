package au.org.emii.aatams

class ValidReceiverEvent extends ReceiverEvent 
{
    static belongsTo = [receiverDeployment: ReceiverDeployment]
    
    static boolean isDuplicate(other)
    {
        boolean duplicate = false
        ValidReceiverEvent.findByTimestamp(other.timestamp, [cache:true]).each
        {
            if (it.duplicate(other))
            {
                duplicate = true
                return
            }
        }
        
        return duplicate
    }
    
    private boolean duplicate(other)
    {
        return (
            this.timestamp == other.timestamp
         && this.receiverName == other.receiverName
         && this.description == other.description
         && this.data == other.data
         && this.units == other.units)
    }
}
