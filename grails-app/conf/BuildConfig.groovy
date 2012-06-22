grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// The excluded jars are provided by the container (tomcat).
grails.war.resources = 
{ 
    stagingDir ->
    
	  // Container provided...
      delete(file:"${stagingDir}/WEB-INF/lib/mail-1.4.3.jar")
      delete(file:"${stagingDir}/WEB-INF/lib/postgis-jdbc-1.3.3.jar")
      delete(file:"${stagingDir}/WEB-INF/lib/postgresql-9.0-801.jdbc4.jar")

	  delete(file:"${stagingDir}/WEB-INF/lib/postgis-jdbc-1.3.0.jar")
	  delete(file:"${stagingDir}/WEB-INF/lib/postgis-stubs-1.3.0.jar")

      // The jars are being inserted by the hudson/tomcat build process, and
      // are causing errors on startup for the app on tomcat6.
      delete(file:"${stagingDir}/WEB-INF/lib/commons-collections-3.1.jar")
      delete(file:"${stagingDir}/WEB-INF/lib/slf4j-api-1.5.2.jar")

      delete(file:"${stagingDir}/WEB-INF/lib/servlet-api-2.3.jar")
}
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	
	def gebVersion = "0.6.1"
	def seleniumVersion = "2.12.0"

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
		mavenLocal()
		mavenCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
        runtime 'postgresql:postgresql:9.0-801.jdbc4'
		
		// KML
//		compile 'de.micromata.jak:JavaAPIforKml:2.2.0-SNAPSHOT'
		
		test("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion")
		{
			exclude "xml-apis"
		}
		test("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
		test("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")
		test("org.seleniumhq.selenium:selenium-support:$seleniumVersion")
		
		// You usually only need one of these, but this project uses both
//		test "org.codehaus.geb:geb-spock:$gebVersion"
		test "org.codehaus.geb:geb-junit4:$gebVersion"
		
		test "xmlunit:xmlunit:1.3"
    }
	plugins {
//		test ":tomcat:$grailsVersion"
//		test ":hibernate:$grailsVersion"
//
		test ":geb:$gebVersion"
	}
}
