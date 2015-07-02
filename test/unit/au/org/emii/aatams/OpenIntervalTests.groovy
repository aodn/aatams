package au.org.emii.aatams

import org.joda.time.*
import grails.test.*

class OpenIntervalTests extends GrailsUnitTestCase {
    def now = new DateTime()

    void testOverlaps() {
        def openInterval = new OpenInterval(now)

        assertFalse(openInterval.overlaps(new Interval(now.minusDays(2), now.minusDays(1))))
        assertTrue(openInterval.overlaps(new Interval(now.plusDays(1), now.plusDays(2))))
        assertTrue(openInterval.overlaps(new Interval(now.minusDays(1), now.plusDays(2))))
    }

    void testContains() {
        def openInterval = new OpenInterval(now)

        assertFalse(openInterval.contains(now.minusDays(1)))
        assertTrue(openInterval.contains(now.plusDays(1)))
    }
}
