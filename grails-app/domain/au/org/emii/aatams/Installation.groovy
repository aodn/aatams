package au.org.emii.aatams

import de.micromata.opengis.kml.v_2_2_0.Folder
import de.micromata.opengis.kml.v_2_2_0.Placemark

/**
 * An installation is a configuration of multiple receivers generally identified
 * by a geographic location.  An installation can contrain multiple Installation
 * Stations.
 */
class Installation  {
    static hasMany = [stations:InstallationStation]
    static belongsTo = [project:Project]
    static auditable = true
    
    String name
    InstallationConfiguration configuration
    
    static constraints = {
        name(blank:false)
        configuration()
        project()
    }
     
    static mapping = {
        // Speed up candidateEntitiesService.
        cache true
        project cache:true
    }
    
    static searchable = [only: ['name']]
    
    String toString() {
        return name
    }

    Folder toKmlFolder() {
        Folder installationFolder = new Folder().withName(name)
        
        stations.sort() {
            a, b ->
            
            a.name <=> b.name
        }.each {
            station ->

            final Placemark stationPlacemark = station.toPlacemark()
            installationFolder.getFeature().add(stationPlacemark)
        }
        
        return installationFolder
    }
}
