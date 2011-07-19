package au.org.emii.aatams

/**
 * An installation is a configuration of multiple receivers generally identified
 * by a geographic location.  An installation can contrain multiple Installation
 * Stations.
 */
class Installation 
{
    static hasMany = [stations:InstallationStation]
    
    String name
    InstallationConfiguration configuration
    Project project
    
    static constraints =
    {
        name(blank:false)
        configuration()
        project()
    }
     
    String toString()
    {
        return name
    }
}
