package au.org.emii.aatams

/**
 * Treatment applied during a surgery.
 *
 * e.g. ANTIBIOTIC, ANISTHETIC.
 */
class SurgeryTreatmentType  {
    String type

    static constraints = {
        type(blank:false, unique:true)
    }

    String toString() {
        return type
    }
}
