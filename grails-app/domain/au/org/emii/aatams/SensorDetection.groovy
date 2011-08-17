package au.org.emii.aatams

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * Some tags may include one or more sensors, for which a code map and ping
 * ID combination is transmitted by the tag.  These transmissions are modelled
 * as a specialisation of Detection, as they also include a sensor value. 
 */
class SensorDetection extends Detection
{
    Float uncalibratedValue
    String sensorUnit
    
    static constraints =
    {
        timestamp()
        receiverDeployment()
        transmitterName(nullable:true, blank:true)
        transmitterSerialNumber(nullable:true, blank:true)
        stationName(nullable:true, blank:true)
        location(nullable:true)
        uncalibratedValue()
    }
    
    String toString()
    {
        return super.toString() + ", " + String.valueOf(uncalibratedValue) 
    }
    
    public boolean equals(Object obj) 
    {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) 
        {
            return false;
        }
        
        Detection rhs = (Detection) obj;
        return new EqualsBuilder()
            .appendSuper(super.equals(obj))
            .append(uncalibratedValue, rhs.uncalibratedValue)
            .append(sensorUnit, rhs.sensorUnit)
        .isEquals();
    }

    public int hashCode() 
    {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.equals(obj))
            .append(uncalibratedValue, rhs.uncalibratedValue)
            .append(sensorUnit, rhs.sensorUnit)
        .toHashCode();
   }
}
    
