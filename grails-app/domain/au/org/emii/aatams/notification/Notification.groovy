package au.org.emii.aatams.notification

import au.org.emii.aatams.Person

class Notification 
{
    String key
    String htmlFragment
    String anchorSelector
    
    /**
     * Display for unauthenticated users.
     */
    Boolean unauthenticated = false
    
    Set<Person> acknowledgers = new HashSet<Person>()
    static hasMany = [acknowledgers:Person]
     
    static constraints = 
    {
        key(unique:true)
    }
    
    static transients = ['activeForPerson']
    
    boolean isActiveForPerson(Person person)
    {
        if (!person)
        {
            return unauthenticated
        }
        
        return (!unauthenticated && !acknowledgers*.username.contains(person.username))
    }
    
    String toString()
    {
        return key
    }
}
