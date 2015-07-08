package au.org.emii.aatams.event

import grails.test.*
import java.text.SimpleDateFormat
import au.org.emii.aatams.FileFormatException
import au.org.emii.aatams.event.EventFormat

class EventFormatTests extends GrailsUnitTestCase {
    def format

    protected void setUp() {
        super.setUp()

        format = new EventFormat();
    }

    void testValidFormat() {
        def result = format.parseRow([
            'Date/Time': '2007-09-13 12:38:00',
            'Receiver': 'VR2W-101735',
            'Description': 'Blanking',
            'Data': '240',
            'Units': 'ms',
            'Units detail 1': '1',
            'Units detail 2': '2',
            'Units detail 3': '3'
        ])

        def expectedResult = [
            timestamp: new SimpleDateFormat('yyyy-MM-dd HH:mm:ss Z').parse('2007-09-13 12:38:00 UTC'),
            receiverName: 'VR2W-101735',
            description: 'Blanking',
            data: '240',
            units: 'ms,1,2,3'
        ]

        assertEquals(expectedResult, result)
    }

    void testInvalidFormat() {
        try {
            format.parseRow([
                'Date/Time': '2007/09/13 12:38:00'
            ])

            fail()
        }
        catch (FileFormatException e) {
            assertEquals("Incorrect format for Date/Time expected yyyy-MM-dd HH:mm:ss Z", e.message)
        }
    }
}
