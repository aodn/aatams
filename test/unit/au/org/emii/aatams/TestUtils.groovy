package au.org.emii.aatams

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

/**
 * Utility methods common to all tests.
 * 
 * @author jburgess
 */
class TestUtils 
{
    static loginSysAdmin(caller)
    {
        Person jkburges = new Person(username: 'jkburges')
        def subject = [ getPrincipal: { jkburges.username },
                        isAuthenticated: { true },
                        hasRole: { true } 
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
        
        // Need this for "findByUsername()" etc.
        caller.mockDomain(Person, [jkburges])
    }
    
    static loginJoeBloggs(caller)
    {
        Person jbloggs = new Person(username: 'jbloggs')
        def subject = [ getPrincipal: { jbloggs.username },
                        isAuthenticated: { true },
                        hasRole:
                        {
                            if (it == "SysAdmin")
                            {
                                false
                            }
                            else
                            {
                                true
                            }
                        }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }

        // Need this for "findByUsername()" etc.
        caller.mockDomain(Person, [jbloggs])
    }
    
    static logout()
    {
        SecurityUtils.metaClass.static.getSubject = null
    }
    
    static setupMessage(controller)
    {
        controller.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
    }
}

