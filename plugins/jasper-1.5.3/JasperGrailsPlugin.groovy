class JasperGrailsPlugin {
    def version = "1.5.3"
    def grailsVersion = "1.3.0 > *"

    def developers = [
            [name: "Sebastian Hohns", email: "sebastian.hohns@googlemail.com"],
            [name: "Marcos Pereira", email: "mfpereira@gmail.com"]]

    def license = "Apache 2.0"
    def issueManagement = [system: "JIRA", url: "http://jira.grails.org/browse/GPJASPER"]
    def scm = [url: "http://svn.grails-plugins.codehaus.org/browse/grails-plugins/grails-jasper"]

    def author = "Sebastian Hohns"
    def authorEmail = "sebastian.hohns@googlemail.com"

    List pluginExcludes = [
            'grails-app/views/**',
            'grails-app/controllers/org/codehaus/groovy/grails/plugins/jasper/JasperDemoController.groovy',
            'src/groovy/org/codehaus/groovy/grails/plugins/jasper/demo/**',
            'docs/**',
            'src/docs/**'
    ]

    def title = "jasper plugin"
    def description = '''
	This plugin adds easy support for launching jasper reports from GSP pages.
	After installing this plugin, run your application and request (app-url)/jasper/demo for a demonstration and instructions.
    '''
    def documentation = "http://www.grails.org/plugin/jasper"
    def dependsOn = [:]

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def doWithWebDescriptor = { xml ->
        def servlets = xml.servlet
        def lastServlet = servlets[servlets.size() - 1]
        lastServlet + {
            servlet {
                'servlet-name'('image')
                'servlet-class'('net.sf.jasperreports.j2ee.servlets.ImageServlet')
            }
        }

        def mappings = xml.'servlet-mapping'
        def lastMapping = mappings[mappings.size() - 1]
        lastMapping + {
            'servlet-mapping' {
                'servlet-name'('image')
                'url-pattern'('/reports/image')
            }
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when this class plugin class is changed
        // the event contains: event.application and event.applicationContext objects
    }

    def onApplicationChange = { event ->
        // TODO Implement code that is executed when any class in a GrailsApplication changes
        // the event contain: event.source, event.application and event.applicationContext objects
    }
}
