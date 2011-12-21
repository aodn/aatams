package au.org.emii.aatams

/**
 * Determines access priviliges that a Person has on a particular Project (via
 * ProjectRole).
 */
enum ProjectAccess 
{
    READ_ONLY('Read Only'),
    READ_WRITE('Read/Write')
    
    String displayStatus
    
    ProjectAccess(String displayStatus)
    {
        this.displayStatus = displayStatus
    }
    
    String toString()
    {
        return displayStatus
    }
    
    static list()
    {
        [READ_ONLY, READ_WRITE]
    }
}
