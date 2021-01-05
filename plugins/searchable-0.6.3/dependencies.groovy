grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir    = "target/test-reports"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits "global"
    log "warn"

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        // mavenCentral()
        mavenRepo "https://repo1.maven.org/maven2/"
        mavenRepo "https://repo.maven.apache.org/maven2/"
    }

    dependencies {
    }

    plugins {
        build ":release:1.0.0.RC3", {
            export = false
        }

        compile ":hibernate:$grailsVersion", {
            export = false
        }
    }
}
