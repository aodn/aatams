grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    inherits("global") {
    }

    repositories {
        inherits true

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()

        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repo.grails.org/grails/core"

        mavenRepo "http://download.osgeo.org/webdav/geotools"
        mavenRepo "http://www.hibernatespatial.org/repository"
    }

    dependencies {
        build 'com.lowagie:itext:2.1.7'
        compile 'org.jadira.usertype:usertype.jodatime:1.9'
        compile 'com.vividsolutions:jts:1.13'
        compile 'org.apache.commons:commons-compress:1.9'
        compile('org.hibernatespatial:hibernate-spatial-postgis:1.1.1') {
            excludes 'hibernate-core', 'javassist'
        }
        compile 'org.jooq:jooq:3.6.1'
        compile('org.postgis:postgis-jdbc:1.3.3') {
            excludes 'postgis-stubs'
        }
        runtime 'org.grails:grails-web-databinding-spring:2.4.4'
        runtime 'org.postgresql:postgresql:9.4-1201-jdbc41'
        test 'xmlunit:xmlunit:1.6'
    }

    plugins {

        build ':tomcat:7.0.55'

        compile ':audit-logging:1.0.4'
        compile ':csv:0.3.1'
        compile ":executor:0.3"
        compile ':hibernate-spatial:0.0.4'
        compile ':hibernate-spatial-postgresql:0.0.4'
        compile(':jasper:1.11.0') {
            excludes 'lucene-core', 'lucene-analyzers-common', 'lucene-queryparser'
        }
        compile ':joda-time:1.5'
        compile ":mail:1.0.7"
        compile ':plugin-config:0.2.0'
        compile ":quartz:1.0.2"
        compile ':scaffolding:2.1.2'
        compile ':searchable:0.6.9'
        compile ':shiro:1.2.1'

        runtime ':database-migration:1.4.0'
        runtime ':hibernate:3.6.10.19'

// plugins.build-test-data=1.1.1
// plugins.quartz=0.4.2


// plugins.audit-logging=0.5.4
// plugins.code-coverage=1.2.4
// plugins.csv=0.3
// plugins.database-migration=1.0
// plugins.executor=0.2
// plugins.file-uploader=1.1
// plugins.functional-test-development=0.2
// plugins.grails-template-engine=0.2
// plugins.hibernate=1.3.7
// plugins.hibernate-spatial=0.0.4
// plugins.hibernate-spatial-postgresql=0.0.4
// plugins.jasper=1.5.3
// plugins.joda-time=1.2
// plugins.mail=1.0-SNAPSHOT
// plugins.perf4j=0.1.1
// plugins.plugin-config=0.1.5
// plugins.searchable=0.6.3
// plugins.shiro=1.1.3
// plugins.tomcat=1.3.7
// plugins.transaction-handling=0.1.3


    }
}
