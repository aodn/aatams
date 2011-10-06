package au.org.emii.aatams

/**
 * An installation is a configuration of multiple receivers generally identified
 * by a geographic location.  An installation can contrain multiple Installation
 * Stations.
 */
class Installation 
{
    static hasMany = [stations:InstallationStation]
    static belongsTo = [project:Project]
    
    static searchable =
    {
        spellCheck "include"
    }
    
    String name
    InstallationConfiguration configuration
    
    static constraints =
    {
        name(blank:false)
        configuration()
        project()
    }
     
    static mapping =
    {
        // Speed up candidateEntitiesService.
        cache: true
        project cache:true
    }
    
    String toString()
    {
        return name
    }
}
