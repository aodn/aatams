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
    static hasMany = [receivers:Receiver]
    static transients = ['scrambledLocation']
    
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
}
