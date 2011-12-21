package au.org.emii.aatams.report

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.ValidDetection
import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.Folder
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark

class KmlService 
{
    static transactional = true

    Kml toKml(List<ValidDetection> detections) 
	{
		final Kml kml = new Kml()
		Document doc = kml.createAndSetDocument()

		List<Project> projects = subsetModel(detections)
		
		projects.each
		{
			project ->
			
			Folder projectFolder = project.toKmlFolder()
			doc.getFeature().add(projectFolder)
		}
		
		return kml 
    }
	
	/**
	 * Build a subset of the GORM model based on the given list of detections.
	 * 
	 * @param detections
	 * @return
	 */
	private List<Project> subsetModel(List<ValidDetection> detections)
	{
		return Project.list().collect { it.toKmlClone(detections) }
	}
}
