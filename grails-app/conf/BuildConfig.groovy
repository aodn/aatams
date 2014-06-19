grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    inherits("global") {
    }

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://download.java.net/maven/2/"
    }

    dependencies {
        build 'commons-io:commons-io:2.1'
        runtime 'commons-io:commons-io:2.1'
        runtime 'postgresql:postgresql:9.0-801.jdbc4'
        compile 'org.apache.commons:commons-compress:1.4'
        test "xmlunit:xmlunit:1.3"
    }

    plugins {
    }
}
