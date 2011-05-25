package au.org.emii.aatams

/**
 * Tag detections are received and stored on a receiver.  Each detection 
 * represents a valid 'ping' signal from a tag attached to an animal, which may
 * include information from one or two sensors on the tag.  Sensor information
 * can include temperature, depth, acceleration, pH and potentially other
 * readings in the future.
 */
class Detection 
{
    Date timestamp
    Receiver receiver
    Tag tag

    String toString()
    {
        return timestamp.toString() + " " + receiver.toString()
    }
}
