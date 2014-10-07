package au.org.emii.aatams.detection

import org.joda.time.DateTime

import grails.test.*

class PageIndexTests extends GrailsUnitTestCase {

    void testToSqlString() {
        def params = [
            timestamp: new DateTime('2014-10-10T11:23:45+11:00'),
            receiverName: 'VR2W-111222',
            transmitterId: 'A69-1303-12345'
        ]

        assertEquals("(timestamp, receiver_name, valid_detection.transmitter_id) > " +
                         "('2014-10-10T11:23:45.000+11:00', 'VR2W-111222', 'A69-1303-12345')",
                     new PageIndex(params).toSqlString())
    }

    void testFromRequestParams() {
        def params = [:]
        assertNull(PageIndex.fromRequestParams(params))

        params.timestamp = '2014-10-10T11:23:45+11:00'
        assertNull(PageIndex.fromRequestParams(params))

        params.receiverName = 'VR2W-111222'
        assertNull(PageIndex.fromRequestParams(params))

        params.transmitterId = 'A69-1303-12345'
        assertNotNull(PageIndex.fromRequestParams(params))

        params.foo = 'bar'
        assertNotNull(PageIndex.fromRequestParams(params))
    }

    void testToRequestParams() {
        def params = [
            timestamp: new DateTime('2014-10-10T11:23:45+11:00'),
            receiverName: 'VR2W-111222',
            transmitterId: 'A69-1303-12345'
        ]

        assertEquals(params, new PageIndex(params).toRequestParams())
    }
}
