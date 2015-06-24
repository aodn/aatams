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
        AbstractInterval.metaClass = null
        super.tearDown()
    }

    void testContainsList() {
        def interval = new Interval(
            now,
            now.plusDays(3)
        )

        def yesterday = now.minusDays(1)
        def tomorrow = now.plusDays(1)
        def nextWeek = now.plusWeeks(1)

        assertTrue(interval.contains([ yesterday, tomorrow ]))
        assertTrue(interval.contains([ tomorrow ]))
        assertTrue(interval.contains([ now ]))
        assertFalse(interval.contains([ yesterday, nextWeek ]))
        assertTrue(interval.contains([ yesterday, tomorrow, nextWeek ]))

        assertFalse(interval.contains([ ]))
        assertFalse(interval.contains([ null ]))
        assertFalse(interval.contains([ null, yesterday ]))
        assertTrue(interval.contains([ null, tomorrow ]))
    }

    void testOverlapsOpenInterval() {
        def openInterval = new OpenInterval(now)
        def closedInterval = new Interval(now, now.plusDays(1))

        assertTrue(openInterval.overlaps(closedInterval))
        assertTrue(openInterval.overlaps(openInterval))
        assertTrue(closedInterval.overlaps(openInterval))
    }
}
