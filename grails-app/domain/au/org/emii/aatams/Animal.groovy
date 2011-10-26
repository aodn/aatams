package au.org.emii.aatams

class Animal 
{
    static hasMany = [releases: AnimalRelease]
    Species species
    Sex sex
    
    static constraints =
    {
        sex(nullable:true)
    }
    
    String toString()
    {
        // Cast is necessary to avoid NPE.
        StringBuffer buf = new StringBuffer(String.valueOf(species))
        buf.append("-")
        buf.append(String.valueOf(sex))
        buf.append(", ")

        buf.append("tags (")
        buf.append(releases*.surgeries*.tag)
        buf.append(")")
        
        return buf.toString()
    }
}
