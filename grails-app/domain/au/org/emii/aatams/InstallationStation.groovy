package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point

/**
 * An Installation Station is a location within an Installation where a 
 * receiver is deployed.  A single Installation Station will only have one 
 * receiver deployed at any one time, but may have multiple receivers deployed
 * over time.
 */
class InstallationStation 
{
    static belongsTo = [installation:Installation]
    static hasMany = [receivers:Receiver, deployments:ReceiverDeployment]
    static transients = ['curtainPositionAsString', 'scrambledLocation', 'latitude', 'longitude']
    
    static mapping =
    {
        columns
        {
            location type:org.hibernatespatial.GeometryUserType
        }
    }
    
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
}
