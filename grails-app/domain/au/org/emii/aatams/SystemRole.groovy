package au.org.emii.aatams

/**
 * The system role of a person.  This is different from ProjectRole, in that
 * a person's security role is quite independent from their role (if any) within
 * a project.  e.g a person may have a SystemRole of "System Administrator", but
 * only be a Administrator within a project.
 *
 * It is a combination of SystemRole and ProjectRole which determines what a 
 * person may or may not do.
 *
 * e.g. Project Investigators have create/edit privileges on their own project
 * but not on other projects.
 */
class SystemRole 
{
    SystemRoleType roleType
    
    String toString()
    {
        return String.valueOf(roleType)
    }
}
