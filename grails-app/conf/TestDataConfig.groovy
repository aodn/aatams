import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

import org.joda.time.DateTimeZone

import au.org.emii.aatams.*

def defaultLocation = (Point)new WKTReader().read("POINT(30.1234 30.1234)")
testDataConfig {
    sampleData {
        'au.org.emii.aatams.DeviceModel' {
            def i = 1
            modelName = { "model${i++}" }
        }
        'au.org.emii.aatams.DeviceStatus' {
            def i = 1
            status = { "status${i++}" }
        }
        'au.org.emii.aatams.DeviceManufacturer' {
            def i = 1
            manufacturerName = { "manufacturer${i++}" }
        }
        'au.org.emii.aatams.InstallationConfiguration' {
            def i = 1
            type = { "type${i++}" }
        }
        'au.org.emii.aatams.InstallationStation' {
            location = defaultLocation
        }
        'au.org.emii.aatams.MooringType' {
            def i = 1
            type = { "type${i++}" }
        }
        'au.org.emii.aatams.Organisation' {
            status = EntityStatus.ACTIVE
        }
        'au.org.emii.aatams.Person' {
            defaultTimeZone = DateTimeZone.getDefault()

            def i = 1
            username = { "username${i++}" }
        }
        'au.org.emii.aatams.Project' {
            def i = 1
            name = { "project${i++}" }
        }
        'au.org.emii.aatams.ProjectRoleType' {
            def i = 1
            displayName = { "displayName${i++}" }
        }
        'au.org.emii.aatams.ReceiverRecovery' {
            location = defaultLocation
        }
    }
}

environments {
    production {
        testDataConfig {
            enabled = false
        }
    }
}
