package au.org.emii.aatams

/**
 * Models the relationship between a detection and a surgery.
 *
 * From a user's point of view, they are really only interested in the 
 * relationship between a particular animal and a detection.
 *
 * However, the following points need to be considered:
 *
 * - a tag may be re-used on a different animal, therefore the association is
 *   with a surgery (or 'tagging' as it is known to users) and detection
 * - the transmitter ID uploaded as part of the detection may match multiple
 *   tag (and therefore surgeries), although it is expected that this will be a
 *   rare occurence.
 *
 * Therefore, the relationship is modelled as many-to-many between Detection and
 * Surgery.
 */
class DetectionSurgery 
{
    static belongsTo = [surgery:Surgery, detection:Detection]
}
