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
    
    static searchable =
    {
        project(component:true)
    }
    
    String toString()
    {
        return name
    }
}
