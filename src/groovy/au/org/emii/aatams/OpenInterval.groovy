package au.org.emii.aatams

import org.joda.time.*

class OpenInterval { // implements ReadableInterval - except in a groovy way!
    final Interval wrappedInterval

    OpenInterval(ReadableInstant start) {
        wrappedInterval = new Interval(start, new DateTime(new Date(Long.MAX_VALUE)))
    }

    boolean equals(Interval other) {
        return false
    }

    boolean equals(OpenInterval other) {
        return wrappedInterval == other.wrappedInterval
    }

    def methodMissing(String name, args) {
        wrappedInterval.invokeMethod(name, args)
    }

    String toString() {
        "${wrappedInterval.start}/-"
    }
}
