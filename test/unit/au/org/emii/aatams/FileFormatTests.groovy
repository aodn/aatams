package au.org.emii.aatams

import grails.test.*

import java.text.ParseException

import au.org.emii.aatams.FileFormatException
import au.org.emii.aatams.detection.VueDetectionFormat
import au.org.emii.aatams.event.EventFormat

class FileFormatTests extends GrailsUnitTestCase {
    FileFormat format

    protected void setUp() {
        super.setUp()

        // Instantiate abstract FileFormat by mocking abstract parseRow method
        format = [ parseRow: { -> return [:] } ] as FileFormat
    }

    void testDetectionsCSV() {
        assertTrue(FileFormat.newFormat(ReceiverDownloadFileType.DETECTIONS_CSV) instanceof VueDetectionFormat)
    }

    void testEventsCSV() {
        assertTrue(FileFormat.newFormat(ReceiverDownloadFileType.EVENTS_CSV) instanceof EventFormat)
    }

    void testUnknownDetectionFormat() {
        try {
            FileFormat.newFormat("XYZ")
            fail()
        }
        catch (IllegalArgumentException e) {
            assertEquals("Unknown detection format: XYZ", e.message)
        }
    }

    void testNullDateTime() {
        try {
            format.getUtcDate([:], "TIME_STAMP_COLUMN", "dd/MM/yyyy HH:mm:ss Z")
            fail("Timestamp parsed when expecting an Exception")
        }
        catch (NullPointerException e) {
            // Null should be caught and rethrown as a FileFormatException
            fail("Caught NullPointerException expected FileFormatException")
        }
        catch (FileFormatException e) {
            // Not fussed about the content of the exception just that it handles the right cases
            assert true
        }
    }

    void testIncorrectFormatForDateTime() {
        try {
            format.getUtcDate(['DET_DATETIME': '31-01-2013 12:59:59'], 'DET_DATETIME', 'dd/MM/yyyy HH:mm:ss Z')
            fail("getUtcDate parsed when expecting an Exception")
        }
        catch (ParseException e) {
            // ParseException should be caught and rethrown as a FileFormatException
            fail("Caught ParseException, expected FileFormatException")
        }
        catch (FileFormatException e) {
            // Not fussed about the content of the exception just that it handles the right cases
            assert true
        }
    }

    void testValidFormatForDateTime() {
        try {
            format.getUtcDate(['DET_DATETIME': '31/01/2013 12:59:59'], 'DET_DATETIME', 'dd/MM/yyyy HH:mm:ss Z')
            assert true
        }
        catch (FileFormatException e) {
            fail("getUtcDate threw an Exception when expected to parse")
        }
    }
}
