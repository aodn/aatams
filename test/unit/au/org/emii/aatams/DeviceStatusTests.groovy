package au.org.emii.aatams

import grails.test.*

class DeviceStatusTests extends GrailsUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
        
        DeviceStatus newStatus = new DeviceStatus(status:'NEW')
        DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED')
        DeviceStatus recoveredStatus = new DeviceStatus(status:'RECOVERED')
        DeviceStatus lostStatus = new DeviceStatus(status:'LOST')
        DeviceStatus stolenStatus = new DeviceStatus(status:'STOLEN')
        
        def statusList = [newStatus, deployedStatus, recoveredStatus, lostStatus, stolenStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testRecoveryStatuses() 
    {
        assertTrue(DeviceStatus.listRecoveryStatuses()*.status.containsAll(['RECOVERED', 'LOST', 'STOLEN']))
        assertFalse(DeviceStatus.listRecoveryStatuses()*.status.contains('NEW'))
        assertFalse(DeviceStatus.listRecoveryStatuses()*.status.contains('DEPLOYED'))
    }
}
