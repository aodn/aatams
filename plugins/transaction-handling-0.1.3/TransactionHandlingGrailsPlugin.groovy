/*
* Copyright 2010-2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
import grails.plugin.transaction.handling.GroovyDynamicMethods
import grails.plugin.transaction.handling.TransactionHandlingPostProcessor

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
* @author Daniel Henrique Alves Lima
*/
class TransactionHandlingGrailsPlugin {

    private final Log log = LogFactory.getLog(getClass())
    private final GroovyDynamicMethods dynamicMethods = new GroovyDynamicMethods()

    // the plugin version
    def version = "0.1.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.5 > *"
    // the other plugins this plugin depends on
    def dependsOn = ['pluginConfig': '0.1.3 > *']
    def loadAfter = ['hibernate']
    def observe = ['hibernate']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            'grails-app/controllers/**/',
            'grails-app/domain/**/*',
            'grails-app/i18n/**/*',
            'grails-app/services/**/*',
            'grails-app/taglib/**/*',
            'grails-app/views/**/*',
            'test/**/*',
            'web-app/**/*',
            'scripts/**/Eclipse.groovy'
    ]

    // TODO Fill in these fields
    def author = "Daniel Henrique Alves Lima"
    def authorEmail = "email_daniel_h@yahoo.com.br"
    def title = 'Grails Transaction Handling Plugin'
    def description = '''\\
Plugin for advanced management of transactions in Grails.
Possibly a backport of http://jira.grails.org/browse/GRAILS-7093.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/transaction-handling"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        "${TransactionHandlingPostProcessor.class.name}"(TransactionHandlingPostProcessor) {
            grailsApplication = ref('grailsApplication', true)
        }
    }

    def doWithDynamicMethods = { ctx ->
        doWithDynamicMethodsImpl(ctx, application)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
        log.debug("onChange ${event.source}")
        if (application.isDomainClass(event.source)) {
            doWithDynamicMethodsImpl(ctx, application, event.source)
        }
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }


    def doWithDynamicMethodsImpl(ctx, application, targetDomainClass = null) {
        dynamicMethods.doWith(ctx, application, targetDomainClass = null)
    }
}
