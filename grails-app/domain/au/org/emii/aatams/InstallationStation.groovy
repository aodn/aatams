package au.org.emii.aatams

import groovy.sql.Sql

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.hibernatespatial.GeometryUserType

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point

import de.micromata.opengis.kml.v_2_2_0.Placemark

/**
 * An Installation Station is a location within an Installation where a 
 * receiver is deployed.  A single Installation Station will only have one 
 * receiver deployed at any one time, but may have multiple receivers deployed
 * over time.
 */
class InstallationStation 
{
	def dataSource
	def grailsTemplateEngineService
	
    static belongsTo = [installation:Installation]
    static hasMany = [receivers:Receiver, deployments:ReceiverDeployment]
    static transients = ['curtainPositionAsString', 'scrambledLocation', 'latitude', 'longitude', 'active', 'detectionCount']
    
    static mapping =
    {
        // Speed up candidateEntitiesService.
        cache: true
        installation cache:true
		location type: GeometryUserType
    }
    
    static searchable = [only: ['name']]
    
	static Map<Long, Long> detectionCounts = [:]
	
    String name
    
    /**
     * Numeric sequence relating to Station position in the owning Installation.
     * Not applicable to some installation configurations (e.g. array).
     */
    Integer curtainPosition
    
    /**
     * Number of deployments at this particular station.
     */
    Integer numDeployments = 0
	
    /**
     * Geographic position of this station.
     */
    Point location
    
    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
    
    /**
     * Convenience method to get the (possible scrambled) latitude.
     * It's mainly here because Jasper reports expects bean properties, and
     * Point.coordinate.y doesn't conform to that (i.e. there is no getY() 
     * method).
     */
    double getLatitude()
    {
        return getScrambledLocation().coordinate.y
    }
    
    double getLongitude()
    {
        return getScrambledLocation().coordinate.x
    }
    
    static constraints =
    {
        name(blank:false)
        curtainPosition(nullable:true, min:0)
        location()
    }
    
    String toString()
    {
        return name
    }
    
    String getCurtainPositionAsString()
    {
        if (!curtainPosition)
        {
            return ""
        }
        
        return String.valueOf(curtainPosition)
    }
    
    /**
     * Station is active if there is one or more active deployments at this
     * station.
     */
    boolean isActive()
    {
        def activeDeployments = deployments.grep 
        {
            it.isActive()
        }
        
        return activeDeployments.size() >= 1
    }

	Placemark toPlacemark()
	{
		final Placemark placemark = new Placemark()
		placemark.setName(name)
		placemark.setOpen(Boolean.TRUE)
		placemark.createAndSetPoint().addToCoordinates(getLongitude(), getLatitude())
		placemark.setDescription(toKmlDescription())
		placemark.setStyleUrl("#defaultStationStyle")
		
		return placemark
	}
	
	String toKmlDescription()
	{
		return grailsTemplateEngineService.renderView("/report/_kmlDescriptionTemplate", [installationStationInstance:this])
	}
	
	boolean hasDetections()
	{
		return (detectionCount() != 0)
	}
	
	long getDetectionCount()
	{
		return detectionCounts.get(id) ?: 0
	}
	
	static void refreshDetectionCounts()
	{
		detectionCounts = [:]
		
		def sql = new Sql(AH.application.mainContext.dataSource)
		
		sql.eachRow('''select station_id, count(*) from detection_extract_view group by station_id''')
		{
			row ->
			
			detectionCounts[row.station_id] = row.count
		}
	}
}
