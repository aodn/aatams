package au.org.emii.aatams

/**
 * A receiver is a device deployed in the ocean that is able to receive 'ping'
 * transmissions from tags attached or surgically implanted into fish.
 */
class Receiver extends Device
{
    static belongsTo = [station:InstallationStation]
    
    /**
     * TODO: units?
     */
    Float batteryLife
    
    Float batteryVoltage
    

}
