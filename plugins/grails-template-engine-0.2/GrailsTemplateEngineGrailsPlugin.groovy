class GrailsTemplateEngineGrailsPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/*"
    ]

    // TODO Fill in these fields
    def author = "Peter Delahunty"
    def authorEmail = "peter.delahunty@solution51.com"
    def title = "Grails Template Engine"
    def description = '''\\
This plugin exposes the GSP engine as a service. It allows you use the gsp template engine to render gsp out side the standard response.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/GrailsTemplateEngine+Plugin"

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        exposeEngine(application, applicationContext)
    }

    def onChange = {event ->
        exposeEngine(event.application, event.ctx)
    }

    def exposeEngine(application, applicationContext) {
        application.controllerClasses*.metaClass*.renderWithTemplateEngine = {templateName, model, pluginName = null ->
            return applicationContext.grailsTemplateEngineService?.renderView(templateName, model, pluginName)
        }
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
