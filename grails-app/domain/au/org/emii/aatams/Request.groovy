package au.org.emii.aatams

class Request 
{
    static belongsTo = [requester:Person, organisation:Organisation]
    
    String toString()
    {
        return requester.toString()
    }
    
}
