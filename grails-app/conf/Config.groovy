// 
// // locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

//
// JSON config.
//

// Require deep conversion to JSON.
//grails.converters.json.default.deep = true

// This is required to avoid org.codehaus.groovy.grails.web.json.JSONException: Misplaced key.
grails.converters.json.circular.reference.behaviour = "INSERT_NULL"

// Mail plugin config
grails.mail.adminEmailAddress = "aatams_admin@emii.org.au"
grails.mail.systemEmailAddress = "aatams_system@emii.org.au"
grails.mail.port = 25
grails.mail.username = "aatams_system@utas.edu.au"
grails.mail.props = ["mail.smtp.auth":"false"]

// Database migration.
grails.plugin.databasemigration.updateOnStart = true
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']

// set per-environment serverURL stem for creating absolute links
environments 
{
    production 
	{
        grails.serverURL = "http://preview.emii.org.au/${appName}"
        grails.serverHost = "http://preview.emii.org.au"
		fileimport.path = "/var/lib/tomcat/instance_8083_aatams3/uploads/prod"
		
		grails.mail.host = "localhost"
    }
    development 
	{
        grails.serverURL = "http://localhost:8080/${appName}"
        grails.serverHost = "http://localhost:8080"
        fileimport.path = "/Users/jburgess/Documents/aatams/test_uploads"
		
		grails.mail.host = "postoffice.utas.edu.au"
    }
    test 
	{
        grails.serverURL = "http://localhost:8090/${appName}"
        grails.serverHost = "http://localhost:8090"
		grails.plugin.databasemigration.updateOnStart = false
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {
//        console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
        console name:'stdout', layout:pattern(conversionPattern: '%d [%t] %-5p %c - %m%n')
        'null' name: "stacktrace"
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
//           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
    
//    debug  'org.springframework',
//           'org.hibernate',
//           'org.codehaus.groovy.grails.orm.hibernate'

//    info    "grails.app.service.au.org.emii"

    debug   "grails.app.controller.au.org.emii",
            "grails.app.service.au.org.emii",
//            "grails.app.service.aatams",
            "grails.app.domain.au.org.emii",
            "grails.app.tagLib.au.org.emii",
            "grails.app.task",
//           'org.hibernate',
//            "grails.buildtestdata",
//            "net.sf.jasperreports",
//            "org.codehaus.groovy.grails.plugins.jasper",
            "grails.app.filter"//,
            //"grails.app.tagLib.com.energizedwork.grails.plugins.jodatime"
            
    info    "grails.app.service.au.org.emii.aatams.detection.VueDetectionFileProcessorService"
    info    "grails.app.service.au.org.emii.aatams.VueEventFileProcessorService"
}


// Date formats.
//jodatime.format.org.joda.time.DateTime = "yyyy-MM-dd'T'HH:mm:ssZ"
jodatime.format.org.joda.time.DateTime = "dd/MM/yyyy HH:mm:ss zz"

tag.expectedLifeTime.gracePeriodDays = 182 // 6 months

// Warning period for release embargo expiration.
animalRelease.embargoExpiration.warningPeriodMonths = 1


grails.gorm.default.list.max = 20

// Added by the Joda-Time plugin:
grails.gorm.default.mapping = {
	"user-type" type: org.joda.time.contrib.hibernate.PersistentDateTime, class: org.joda.time.DateTime
	"user-type" type: org.joda.time.contrib.hibernate.PersistentDuration, class: org.joda.time.Duration
	"user-type" type: org.joda.time.contrib.hibernate.PersistentInstant, class: org.joda.time.Instant
	"user-type" type: org.joda.time.contrib.hibernate.PersistentInterval, class: org.joda.time.Interval
	"user-type" type: org.joda.time.contrib.hibernate.PersistentLocalDate, class: org.joda.time.LocalDate
	"user-type" type: org.joda.time.contrib.hibernate.PersistentLocalTimeAsString, class: org.joda.time.LocalTime
	"user-type" type: org.joda.time.contrib.hibernate.PersistentLocalDateTime, class: org.joda.time.LocalDateTime
	"user-type" type: org.joda.time.contrib.hibernate.PersistentPeriod, class: org.joda.time.Period
}
