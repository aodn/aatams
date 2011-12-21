package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils
import shiro.*
import org.apache.shiro.SecurityUtils
import org.apache.shiro.UnavailableSecurityManagerException

import org.joda.time.DateTimeZone

class Person extends SecUser
{
    static hasMany = [projectRoles:ProjectRole, requests:Request]
    static belongsTo = [organisation:Organisation]
    
    static transients = ['projects']
    
    String name;
    String emailAddress;
    String phoneNumber;
    
    // Allows for PENDING users when self-registering.
    EntityStatus status
    String registrationComment
    
    DateTimeZone defaultTimeZone
    
    static constraints = 
    {
        name(blank:false)
        organisation()
        phoneNumber(blank:true)
        emailAddress(email:true)
        status()
        registrationComment(nullable:true, blank:true)
        defaultTimeZone()
    }

    static mapping = 
    {
        // Speed up candidate entities service/permission utils service.
        cache true
		
		sort name:"asc"
    }
    
    static searchable = 
    {
        organisation(component:true)
        projectRoles(component:true)
        except = ['passwordHash', 'permissions', 'roles']
    }

    String toString()
    {
        return name
    }
    
    String getProjects()
    {
        return ListUtils.fold(projectRoles, "project")
    }

    static defaultTimeZone()
    {
        try
        {
            if (!SecurityUtils.subject?.isAuthenticated())
            {
                return DateTimeZone.getDefault()
            }

            Person principal = Person.findByUsername(SecurityUtils.subject?.principal, [cache:true])
            return principal?.defaultTimeZone
        }
        // This is being thrown for findByUsername in cobertura coverage tests.
        catch (MissingMethodException e)
        {
            return DateTimeZone.getDefault()
        }
        catch (UnavailableSecurityManagerException e)
        {
            return DateTimeZone.getDefault()
        }
    }
}
