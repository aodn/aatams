package au.org.emii.aatams

import grails.test.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class CandidateEntitiesServiceTests extends GrailsUnitTestCase 
{
    def candidateEntitiesService
    def permService
    def person
    
    Receiver newReceiver
    Receiver deployedReceiver
    Receiver recoveredReceiver
    Receiver csiroReceiver
    
    protected void setUp()
    {
        super.setUp()
        
        mockLogging(PermissionUtilsService)
        permService = new PermissionUtilsService()
        
        mockLogging(CandidateEntitiesService)
        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.permissionUtilsService = permService

        // User belongs to IMOS, but not to CSIRO.
        def imos = new Organisation(name:"IMSO")
        def csiro = new Organisation(name:"CSIRO")
        def orgList = [imos, csiro]
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }
        
        person = new Person(username:"person",
                            organisation:imos)
                               
        mockDomain(Person, [person])
        person.save()
        
        def subject = [ getPrincipal: { person.username },
                        isAuthenticated: { true },
                        hasRole: { false },
                        isPermitted:
                        {
                            return false
                        }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
        
        DeviceStatus newStatus = new DeviceStatus(status:"NEW")
        DeviceStatus deployedStatus = new DeviceStatus(status:"DEPLOYED")
        DeviceStatus recoveredStatus = new DeviceStatus(status:"RECOVERED")
        
        def statusList = [newStatus, deployedStatus, recoveredStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }
        
        newReceiver = new Receiver(codeName:"VRW2-111", status:newStatus, organisation:imos)
        deployedReceiver = new Receiver(codeName:"VRW2-222", status:deployedStatus, organisation:imos)
        recoveredReceiver = new Receiver(codeName:"VRW2-333", status:recoveredStatus, organisation:imos)
        csiroReceiver = new Receiver(codeName:"VRW2-444", status:recoveredStatus, organisation:csiro)

        def receiverList = [newReceiver, deployedReceiver, recoveredReceiver]
        mockDomain(Receiver, receiverList)
        receiverList.each 
        {
            imos.addToReceivers(it)
            it.save() 
        }
        
        imos.save()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testReceivers() 
    {
        def receivers = candidateEntitiesService.receivers()
        
        println "receivers: " + receivers
        
        assertEquals(2, receivers.size())
        
        assertTrue(receivers.contains(newReceiver))
        assertTrue(receivers.contains(recoveredReceiver))
        assertFalse(receivers.contains(deployedReceiver))
        assertFalse(receivers.contains(csiroReceiver))
    }
}
