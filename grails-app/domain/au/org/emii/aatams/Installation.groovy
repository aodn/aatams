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
    
    /**
     * Maximum longitudinal offset used to scramble data for visualisations.
     */
    Float lonOffset
    
    /**
     * Maximum latitudinal offset used to scramble data for visualisations.
     */
    Float latOffset
    
    static constraints =
    {
        name(blank:false)
        configuration()
        project()
        lonOffset(min:0F)
        latOffset(min:0F)
    }
     
    String toString()
    {
        return name
    }
}
