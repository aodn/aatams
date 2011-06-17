package au.org.emii.aatams

/**
 * Ping or Sensor type (e.g. PINGER, DEPTH, TEMPERATURE, ACCELEROMETER).
 */
class TransmitterType 
{
    String transmitterTypeName
    
    static constraints =
    {
        transmitterTypeName(blank:false, unique:true)
    }
    
    String toString()
    {
        return transmitterTypeName
    }
}
