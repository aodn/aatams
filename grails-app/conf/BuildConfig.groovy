grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

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

        runtime 'postgresql:postgresql:9.0-801.jdbc4'
        build 'commons-io:commons-io:2.1'
        runtime 'commons-io:commons-io:2.1'
	
		compile 'org.apache.commons:commons-compress:1.4'
        
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
		test ":geb:$gebVersion"
	}
}
