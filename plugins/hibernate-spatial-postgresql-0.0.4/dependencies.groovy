grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    boolean isNewerGrails = ("$grailsVersion" > "1.3.7") as boolean
    if (isNewerGrails) {checksums false}

    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        mavenRepo "https://repo1.maven.org/maven2/"
        mavenRepo "https://repo.maven.apache.org/maven2/"
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        if (!isNewerGrails) {ivySettings.setVariable('ivy.checksums', '')}
        String hsVersion = (isNewerGrails)? "1.1" : "1.0"
        compile ("org.hibernatespatial:hibernate-spatial-postgis:${hsVersion}") {
            excludes 'hibernate-core'
        }

        runtime ('org.postgis:postgis-jdbc:1.3.0') {
            exclude 'postgresql'
        }

        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
    }
}

