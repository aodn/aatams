package au.org.emii.aatams

import grails.test.*

class DeviceStatusTests extends GrailsUnitTestCase {
    void testRecoveryStatuses() {
        assertTrue(DeviceStatus.listRecoveryStatuses().containsAll(
                       [DeviceStatus.RECOVERED, DeviceStatus.LOST, DeviceStatus.STOLEN])
                  )
        assertFalse(DeviceStatus.listRecoveryStatuses().contains(DeviceStatus.NEW))
        assertFalse(DeviceStatus.listRecoveryStatuses().contains(DeviceStatus.DEPLOYED))
    }
}
