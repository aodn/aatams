import au.org.emii.aatams.*
import au.org.emii.aatams.util.GeometryUtils
import com.vividsolutions.jts.geom.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

def newPoint() {
    return new GeometryFactory().createPoint(new Coordinate(1, 2))
}

testDataConfig {
    sampleData {
        'au.org.emii.aatams.InstallationStation' {
            location = newPoint()
        }
        'au.org.emii.aatams.Person' {
            defaultTimeZone = DateTimeZone.UTC
        }
        'au.org.emii.aatams.Project' {
            def i = 1
            name = { ->
                "name${i++}"
            }
        }
        'au.org.emii.aatams.ReceiverDeployment' {
            location = newPoint()
        }
        'au.org.emii.aatams.ReceiverRecovery' {
            location = newPoint()
        }
    }
}
