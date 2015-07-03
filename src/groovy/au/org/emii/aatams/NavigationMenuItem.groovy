package au.org.emii.aatams

/**
 *
 * @author jburgess
 */
class NavigationMenuItem  {
    String controllerName
    String label
    Boolean canCreateNew
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NavigationMenuItem other = (NavigationMenuItem) obj;
        if ((this.controllerName == null) ? (other.controllerName != null) : !this.controllerName.equals(other.controllerName)) {
            return false;
        }
        if ((this.label == null) ? (other.label != null) : !this.label.equals(other.label)) {
            return false;
        }
        if (this.canCreateNew != other.canCreateNew && (this.canCreateNew == null || !this.canCreateNew.equals(other.canCreateNew))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.controllerName != null ? this.controllerName.hashCode() : 0);
        hash = 47 * hash + (this.label != null ? this.label.hashCode() : 0);
        hash = 47 * hash + (this.canCreateNew != null ? this.canCreateNew.hashCode() : 0);
        return hash;
    }
}

