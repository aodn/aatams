grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.plugins.dir = "./plugins"

grails.project.dependency.resolution = {

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    inherits("global") {
    }

    repositories {
        grailsRepo("http://grails.org/plugins", "grailsCentral")
        grailsHome()
        mavenLocal()

        // mavenCentral()
        mavenRepo "https://repo1.maven.org/maven2/"
        mavenRepo "https://repo.maven.apache.org/maven2/"

        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "https://repo.grails.org/grails"
        mavenRepo "https://repo.grails.org/grails/plugins"
    }

    dependencies {
        build 'commons-io:commons-io:2.1'
        runtime 'commons-io:commons-io:2.1'
        runtime 'postgresql:postgresql:9.0-801.jdbc4'
        compile 'org.apache.commons:commons-compress:1.4'
        compile 'org.jooq:jooq:3.5.1'
        test "xmlunit:xmlunit:1.3"
    }

    plugins {
    }
}
