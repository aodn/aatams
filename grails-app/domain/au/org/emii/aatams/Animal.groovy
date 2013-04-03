package au.org.emii.aatams

class Animal implements Embargoable
{
    static hasMany = [releases: AnimalRelease]
    Species species
    Sex sex
    
    static constraints =
    {
        sex(nullable:true)
    }

    static transients = ['embargoed', 'project']
    
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
    
    boolean isEmbargoed()
    {
        def embargoed = false

        releases.each {
            embargoed |= it.embargoed
        }
        
        return embargoed
    }

    def applyEmbargo() {
        return embargoed ? null : this
    }

    def getProject() {
        return (releases as List).first()?.project
    }
}
