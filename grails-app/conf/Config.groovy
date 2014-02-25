import org.apache.log4j.net.SMTPAppender
import javax.naming.InitialContext

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
grails.mail.supportEmailAddress = "info@emii.org.au"
grails.mail.port = 25
grails.mail.username = "aatams_system@utas.edu.au"
grails.mail.props = ["mail.smtp.auth":"false"]

// Database migration.
grails.plugin.databasemigration.updateOnStart = true
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
grails.plugin.databasemigration.updateOnStartDefaultSchema = "aatams"

auditLog {
    actorClosure = { request, session ->
        org.apache.shiro.SecurityUtils.getSubject()?.getPrincipal()
    }
}

compassConnection = new File(
    "/tmp/search/${grailsEnv}"
).absolutePath

// set per-environment serverURL stem for creating absolute links
environments
{
    production
    {
        grails.serverURL = "http://aatams.emii.org.au/${grails.util.Metadata.current.getApplicationName()}"
        grails.serverHost = "http://aatams.emii.org.au"
        fileimport.path = new File(System.getProperty('java.io.tmpdir'), "aatams/test_uploads").toString()
        bulkimport.path = new File(fileimport.path, "bulkimports").toString()
        grails.mail.host = "localhost"
    }
    development
    {
        grails.serverURL = "http://localhost:8080/${grails.util.Metadata.current.getApplicationName()}"
        grails.serverHost = "http://localhost:8080"
        fileimport.path = new File(System.getProperty('java.io.tmpdir'), "aatams/test_uploads").toString()
        bulkimport.path = new File(fileimport.path, "bulkimports").toString()

        grails.mail.adminEmailAddress = "jkburges@utas.edu.au"
        grails.mail.host = "postoffice.utas.edu.au"
        grails.mail.disabled = true
    }
    test
    {
        grails.serverURL = "http://localhost:8090/${grails.util.Metadata.current.getApplicationName()}"
        grails.serverHost = "http://localhost:8090"
        grails.plugin.databasemigration.updateOnStart = false
        grails.mail.disabled = true
        fileimport.path = "/tmp"
    }
}


/**
 * Instance specific customisation, clearly stolen from:
 * http://phatness.com/2010/03/how-to-externalize-your-grails-configuration/
 *
 * To use set for a specific instance, either set the environment variable "INSTANCE_NAME", or add this in the grails
 * commandline like so:
 *
 * grails -DINSTANCE_NAME=WA run-app
 *
 * Instance specific config files are located in $project_home/instances/
 *
 * Any configuration found in these instance specific file will OVERRIDE values set in Config.groovy and
 * application.properties.
 *
 * NOTE: app.name and version is ignored in external application.properties
 */
if(!grails.config.locations || !(grails.config.locations instanceof List)) {
    grails.config.locations = []
}

try {
    configurationPath = new InitialContext().lookup('java:comp/env/aodn.configuration')
    grails.config.locations << "file:${configurationPath}"

    println "Loading external config from '$configurationPath'..."
}
catch (e) {
    println "Not loading external config"
}


def log4jConversionPattern = '%d [%t] [%X{username}] %-5p %c{1} - %m%n'

log4j =
{
	appenders
	{
		console name: 'stdout', layout: pattern(conversionPattern: log4jConversionPattern)
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
		   'net.sf.ehcache.hibernate'

	warn   'org.mortbay.log'

	info   "grails.app"

	environments
	{
		production
		{
            appenders
            {
                appender new de.viaboxx.nagios.NagiosAppender(
                    name: 'nagiosAppender',
                    threshold: Level.ERROR,
                    nagiosHost: 'status.emii.org.au',
                    nagiosPort: 5667,
                    nagiosEncryption: "TRIPLE_DES",
                    nagiosPassword: "broken cat batteries",
                    monitoredServiceName: "AATAMS-Log4J-Appender",
                    monitoredHostName: "vm-115-41.ersa.edu.au"
                )
            }
		}

		development
		{
            appenders
            {
                file name: 'file', file: 'aatams.log', layout: pattern(conversionPattern: log4jConversionPattern)
            }

			debug  "grails.app.controller.au.org.emii.aatams.ReceiverRecoveryController",
				   "grails.app.service.au.org.emii.aatams.detection.DetectionExtractService",
				   "grails.app.service.au.org.emii.aatams.AnimalReleaseService",
				   "grails.app.domain.au.org.emii.aatams.Receiver",
				   "grails.app.domain.au.org.emii.aatams.bulk",
				   "grails.app.domain.au.org.emii.aatams.ReceiverDownloadFile",
				   "grails.app.service.au.org.emii.aatams.filter.QueryService",
				   "grails.app.service.au.org.emii.aatams.detection.VueDetectionFileProcessorService",
				   "grails.app.service.au.org.emii.aatams.detection.DetectionNotificationService",
				   "grails.app.service.au.org.emii.aatams.detection.JdbcTemplateDetectionFactoryService",
				   "grails.app.service.au.org.emii.aatams.report.KmlService"

		}

		test
		{
			debug  "grails.app.service.au.org.emii.aatams.AnimalReleaseService",
				   "grails.app.domain.au.org.emii.aatams.bulk"
			       "grails.app.service.au.org.emii.aatams.detection"
		}
	}

	root
	{
        info 'stdout', 'null', 'nagiosAppender', 'file'
    }
}

// Date formats.
//jodatime.format.org.joda.time.DateTime = "yyyy-MM-dd'T'HH:mm:ssZ"
jodatime.format.org.joda.time.DateTime = "dd/MM/yyyy HH:mm:ss zz"

tag.expectedLifeTime.gracePeriodDays = 182 // 6 months

// Warning period for release embargo expiration.
animalRelease.embargoExpiration.warningPeriodMonths = 1

// The count figure to cut off at to prevent long running tag detection queries
detection.filter.count.max = 300000


grails.gorm.default.list.max = 20

// Added by the Joda-Time plugin:
grails.gorm.default.mapping = {
   /* Added by the Hibernate Spatial Plugin. */
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Geometry)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.GeometryCollection)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.LineString)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Point)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Polygon)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.MultiLineString)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.MultiPoint)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.MultiPolygon)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.LinearRing)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Puntal)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Lineal)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Polygonal)
    "user-type" type: org.joda.time.contrib.hibernate.PersistentDateTime, class: org.joda.time.DateTime
    "user-type" type: org.joda.time.contrib.hibernate.PersistentDuration, class: org.joda.time.Duration
    "user-type" type: org.joda.time.contrib.hibernate.PersistentInstant, class: org.joda.time.Instant
    "user-type" type: org.joda.time.contrib.hibernate.PersistentInterval, class: org.joda.time.Interval
    "user-type" type: org.joda.time.contrib.hibernate.PersistentLocalDate, class: org.joda.time.LocalDate
    "user-type" type: org.joda.time.contrib.hibernate.PersistentLocalTimeAsString, class: org.joda.time.LocalTime
    "user-type" type: org.joda.time.contrib.hibernate.PersistentLocalDateTime, class: org.joda.time.LocalDateTime
    "user-type" type: org.joda.time.contrib.hibernate.PersistentPeriod, class: org.joda.time.Period
}

rawDetection.extract.limit = 50000
rawDetection.extract.view.name = 'detection_extract_view'
rawDetection.extract.view.select = '''select timestamp, to_char((timestamp::timestamp with time zone) at time zone '00:00', 'YYYY-MM-DD HH24:MI:SS') as formatted_timestamp,
            installation_station.name as station,
            installation_station.id as station_id,
            installation_station.location as location,
            st_y(installation_station.location) as latitude, st_x(installation_station.location) as longitude,
            (device_model.model_name || '-' || device.serial_number) as receiver_name,
            COALESCE(sensor.transmitter_id, '') as sensor_id,
            COALESCE((species.spcode || ' - ' || species.scientific_name || ' (' || species.common_name || ')'), '') as species_name,

            sec_user.name as uploader,
            valid_detection.transmitter_id as "transmitter_id",
            organisation.name as organisation,
            project.name as project,
            installation.name as installation,
            COALESCE(species.spcode, '') as spcode,
            animal_release.id as animal_release_id,
            animal_release.embargo_date as embargo_date,
            project.id as project_id,
            valid_detection.id as detection_id,

            animal_release.project_id as release_project_id,
            valid_detection.sensor_value, valid_detection.sensor_unit,
            valid_detection.provisional

            from valid_detection

            left join receiver_deployment on receiver_deployment_id = receiver_deployment.id
            left join installation_station on receiver_deployment.station_id = installation_station.id
            left join installation on installation_station.installation_id = installation.id
            left join project on installation.project_id = project.id
            left join device on receiver_deployment.receiver_id = device.id
            left join device_model on device.model_id = device_model.id
            left join receiver_download_file on receiver_download_id = receiver_download_file.id
            left join sec_user on receiver_download_file.requesting_user_id = sec_user.id
            left join organisation on device.organisation_id = organisation.id
            left join detection_surgery on valid_detection.id = detection_surgery.detection_id
            left join sensor on detection_surgery.sensor_id = sensor.id

            left join surgery on detection_surgery.surgery_id = surgery.id
            left join animal_release on surgery.release_id = animal_release.id
            left join animal on animal_release.animal_id = animal.id
            left join species on animal.species_id = species.id'''
