package au.org.emii.aatams

/**
 * Common base class for receivers and tags.
 * TODO: this should be abstract but this is causing "unmapped" exceptions
 * with hibernate.
 */
class Device
{
    DeviceModel model
    String serialNumber

    String comment

    static constraints =
    {
        serialNumber(blank:false, unique: ['model'])
        comment(nullable:true, blank:true)
    }

    static mapping =
    {
        cache true
    }

    static transients = ['deviceID']

    String getComment()
    {
        if (!comment)
        {
            return ""
        }

        return comment
    }
}
