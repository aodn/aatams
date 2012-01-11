package au.org.emii.aatams.report

import java.util.Map;

import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.Species;
import au.org.emii.aatams.Tag

import com.vividsolutions.jts.geom.*

/*
 * Wrapper bean for InstallationStation for use as a data source element in 
 * Jasper Reports.
 * 
 * The main purpose is to expose indirect properties (e.g. properties of owning
 * installation, project etc) as bean methods.
 * 
 * TODO: can we add the methods dynamically?
 * TODO: can groovy classes act as data source beans?
 *
 * @author jburgess
 */
class InstallationStationReportWrapper 
{
    /**
     * The wrapped domain object.
     */
    private final station
    
    public static final String NULL_CURTAIN_POSITION = "-"
    
	private final TreeMap<Integer, String> speciesDetectionsByReverseCount = new TreeMap<Integer, String>(Collections.reverseOrder())
	private final TreeMap<Integer, String> tagsDetectionsByReverseCount = new TreeMap<Integer, String>(Collections.reverseOrder())
	
    public InstallationStationReportWrapper(InstallationStation station)
    {
        this.station = station
		
		initDetectionCountMaps()
    }
    
    public String getInstallationName()
    {
        return station.installation.name
    }
    
    public String getInstallationConfigurationType()
    {
        return station.installation.configuration.type
    }
    
    public String getProjectName()
    {
        return station.installation.project.name
    }

    public String getStationName()
    {
        return station.name
    }
    
    public String getStationCurtainPosition()
    {
        Integer curtainPos = station.curtainPosition
        
        if (curtainPos == null)
        {
            return NULL_CURTAIN_POSITION
        }
        
        return String.valueOf(curtainPos)
    }
    
    public double getStationLatitude()
    {
        return station.location.coordinate.y
    }
    
    public double getStationLongitude()
    {
        return station.location.coordinate.x
    }
    
    /**
     * This is here just to keep iReport happy.
     */
    public static Collection<InstallationStationReportWrapper> list()
    {
        return Collections.EMPTY_LIST
    }
	
	private void initDetectionCountMaps()
	{
		Map<Species, Integer> countBySpecies = [:]
		Map<Tag, Integer> countByTag = [:]
		
		station.deployments.each
		{
			deployment ->
			
			deployment.detections.each
			{
				detection ->
				
				detection.detectionSurgeries.each
				{
					detSurgery ->
					
					def species = detSurgery.surgery.release.animal.species
					incCountForSpecies(countBySpecies, species)
					
					def tag = detSurgery.sensor.tag
					incCountForTag(countByTag, tag)
				}
			}
		}

		countBySpecies.each
		{
			k, v ->
			speciesDetectionsByReverseCount.put(v, k)
		}

		countByTag.each
		{
			k, v ->
			tagsDetectionsByReverseCount.put(v, k)
		}
	}
	
	boolean hasDetections()
	{
		return !speciesDetectionsByReverseCount.isEmpty()
	}
	
	Map<Integer, Species> detectionCountsBySpecies()
	{
		return speciesDetectionsByReverseCount
	}
	
	Map<Integer, Species> detectionCountsByTag()
	{
		return tagsDetectionsByReverseCount
	}
	
	private void incCountForSpecies(Map countBySpecies, Species species)
	{
		if (!countBySpecies.get(species))
		{
			countBySpecies.put(species, 0)
		}
		
		countBySpecies[species] = countBySpecies[species] + 1
	}
	
	private void incCountForTag(Map countByTag, Tag tag)
	{
		if (!countByTag.get(tag))
		{
			countByTag.put(tag, 0)
		}
		
		countByTag[tag] = countByTag[tag] + 1
	}
}

