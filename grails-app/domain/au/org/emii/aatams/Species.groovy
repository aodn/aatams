package au.org.emii.aatams

/**
 * Represents a particular species of animal.
 */
class Species
{
    String name

    static constraints =
    {
        name(nullable:true)
    }

    String toString()
    {
        return String.valueOf(name)
    }
}
