package au.org.emii.aatams

/**
 * The type of project role, e.g. Principal Investigator, Co-Investigator,
 * Research Assistant, Technical Assistant, Administrator, Student.
 */
class ProjectRoleType  {
    public static final String PRINCIPAL_INVESTIGATOR = "Principal Investigator"
    
    String displayName
    
    static constraints =  {
        displayName(blank:false, unique:true)
    }
    
    String toString() {
        return displayName
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectRoleType other = (ProjectRoleType) obj;
        if ((this.displayName == null) ? (other.displayName != null) : !this.displayName.equals(other.displayName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (this.displayName != null ? this.displayName.hashCode() : 0);
        return hash;
    }
}
