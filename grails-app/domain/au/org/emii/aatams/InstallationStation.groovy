package au.org.emii.aatams

class InstallationStation 
{
    static belongsTo = [installation:Installation]
    static hasMany = [receivers:Receiver]
    
    String name
    
    /**
     * Numeric sequence relating to Station position in the owning Installation.
     */
    Integer curtainPosition
    
    /**
     * Geographic position of this station.
     */
    //Point location
    
    String toString()
    {
        return name
    }
}
