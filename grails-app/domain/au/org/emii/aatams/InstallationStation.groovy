package au.org.emii.aatams

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
     */
    Integer curtainPosition
    
    /**
     * Geographic position of this station.
     */
    Point location
    
    String toString()
    {
        return name
    }
}
