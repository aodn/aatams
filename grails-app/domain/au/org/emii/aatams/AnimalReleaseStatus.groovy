package au.org.emii.aatams

/**
 * Status is used to model the case where an animal (with associated tag
 * and surgery) is recaptured at which point the pre-existing surgeries are no longer 
 * current.
 * 
 * @author jburgess
 */
enum AnimalReleaseStatus  {
    /**
     * The default status set when a release is created.
     */
    CURRENT,
    
    /**
     * A release goes to finished when a new release is created for the same 
     * animal (i.e. an animal is re-captured and then re-released).
     */
    FINISHED
}

