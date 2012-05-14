package au.org.emii.aatams

import java.util.List;
import java.util.Map;

import au.org.emii.aatams.detection.ValidDetection
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark

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
        cache true
        project cache:true
    }
    
    static searchable = [only: ['name']]
    
    String toString()
    {
        return name
    }

	Folder toKmlFolder()
	{
		Folder installationFolder = new Folder().withName(name)
		
		stations.sort()
		{
			a, b ->
			
			a.name <=> b.name
		}.each
		{
			station ->

			final Placemark stationPlacemark = station.toPlacemark()
			installationFolder.getFeature().add(stationPlacemark)
		}
		
		return installationFolder
	}
	
	static Kml toKml(Map<Installation, List<Feature>> installationsWithKmlChildren)
	{
		def projects = new HashMap<Project, TreeSet<Feature>>()
		installationsWithKmlChildren.each
		{
			installation, kmlChildren ->
			
			def installationSiblings = projects[installation.project]
			
			if (!installationSiblings)
			{
				installationSiblings = new TreeSet<Feature>([compare: {a, b -> a.name <=> b.name} ] as Comparator)
				projects[installation.project] = installationSiblings
			}
			
			Folder installationFolder = new Folder().withName(String.valueOf(installation))

			kmlChildren.each
			{
				installationFolder.getFeature().add(it)
			}

			installationSiblings.add(installationFolder)
		}
		
		return Project.toKml(projects)
	}
}
