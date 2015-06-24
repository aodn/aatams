package au.org.emii.aatams

import org.joda.time.Interval
import org.joda.time.base.AbstractInterval

class JodaOverrides {

    // Would be nicer to do this with an "Extension Module", but alas, that's
    // only available in groovy 2.0.
    //
    // An extension module would avoid the need to mock/unmock in every test
    // which executes the overlapsAny code.
    static void apply() {
        AbstractInterval.metaClass.contains = { List instantList ->
            def owningInterval = delegate

            null != instantList.find {
                it && owningInterval.contains(it)
            }
        }

        AbstractInterval.metaClass.overlaps = { OpenInterval openInterval ->
            openInterval.overlaps(delegate)
        }
    }

    static void mock() {
        AbstractInterval.metaClass.contains = { List instantList ->
            false
        }
        AbstractInterval.metaClass.overlaps = { OpenInterval openInterval ->
            false
        }
    }

    static void unmock() {
        [ AbstractInterval, Interval ].each {
            GroovySystem.metaClassRegistry.removeMetaClass(it.class)
            it.metaClass = null
        }
    }
}
