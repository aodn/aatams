package org.joda.time.base

import au.org.emii.aatams.JodaOverrides
import au.org.emii.aatams.OpenInterval

import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.base.AbstractInterval

import grails.test.*

class AbstractIntervalTests extends GrailsUnitTestCase {

    def now = new DateTime()

    void setUp() {
        super.setUp()
        JodaOverrides.apply()
    }

    void tearDown() {
        JodaOverrides.unmock()
        super.tearDown()
    }

    void testContainsList() {
        def nextThreeDays = new Interval(
            now,
            now.plusDays(3)
        )

        def yesterday = now.minusDays(1)
        def tomorrow = now.plusDays(1)
        def nextWeek = now.plusWeeks(1)

        assertTrue(nextThreeDays.containsAny([ yesterday, tomorrow ]))
        assertTrue(nextThreeDays.containsAny([ tomorrow ]))
        assertTrue(nextThreeDays.containsAny([ now ]))
        assertFalse(nextThreeDays.containsAny([ yesterday, nextWeek ]))
        assertTrue(nextThreeDays.containsAny([ yesterday, tomorrow, nextWeek ]))

        assertFalse(nextThreeDays.containsAny([ ]))
        assertFalse(nextThreeDays.containsAny([ null ]))
        assertFalse(nextThreeDays.containsAny([ null, yesterday ]))
        assertTrue(nextThreeDays.containsAny([ null, tomorrow ]))
    }

    void testOverlapsOpenInterval() {
        def openInterval = new OpenInterval(now)
        def closedInterval = new Interval(now, now.plusDays(1))

        assertTrue(openInterval.overlaps(closedInterval))
        assertTrue(openInterval.overlaps(openInterval))
        assertTrue(closedInterval.overlaps(openInterval))
    }
}
