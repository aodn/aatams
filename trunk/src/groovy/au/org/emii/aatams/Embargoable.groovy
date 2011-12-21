package au.org.emii.aatams

/**
 *
 * @author jburgess
 */
interface Embargoable 
{
    /**
     * Applies embargoing rules, e.g. remove embargoed child entities.
     * May return null if the object itself is embargoed.
     */
    def applyEmbargo()
}

