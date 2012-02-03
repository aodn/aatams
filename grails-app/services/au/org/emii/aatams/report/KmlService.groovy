package au.org.emii.aatams.report

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.ValidDetection
import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.Folder
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark

class KmlService 
{
    static transactional = false

    Kml toKml(List<InstallationStation> stations) 
	{
		final Kml kml = new Kml()
		Document doc = kml.createAndSetDocument()

		def projects = stations*.installation*.project.unique().sort()
		{
			a, b ->
			
			a.name <=> b.name
		}
		
		projects.each
		{
			project ->
			
			Folder projectFolder = project.toKmlFolder()
			doc.getFeature().add(projectFolder)
		}
		
		return kml 
    }
}
