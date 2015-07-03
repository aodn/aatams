package grails.plugin.transaction.handling

import grails.util.GrailsNameUtils

import java.lang.reflect.Modifier

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClass
import org.springframework.context.ApplicationContext
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate


class GroovyDynamicMethods {
    
    private final Log log = LogFactory.getLog(getClass())
    private final TransactionPropertiesUtil txPropsUtil = new TransactionPropertiesUtil()
    
    public GroovyDynamicMethods() {
    }
    
    public void doWith(ApplicationContext ctx, GrailsApplication application, GrailsClass targetDomainClass = null) {               
        Map pluginConfig = application.mergedConfig.asMap(true).grails.plugin.transactionHandling
        
        log.debug("pluginConfig ${pluginConfig}")      

        Map withTrxDefaults = [propagation: 'required']
        Map withNewTrxDefaults = [propagation: 'requiresNew']
        
        withTrxDefaults.putAll(txPropsUtil.removeImmutableDefaults(pluginConfig.programmatic))
        withNewTrxDefaults.putAll(txPropsUtil.removeImmutableDefaults(pluginConfig.programmatic))

        log.debug("withTrxDefaults ${withTrxDefaults}")
        log.debug("withNewTrxDefaults ${withNewTrxDefaults}")

        Closure withTrxImpl = {Map defaults, Map properties, Closure callable ->
            if (properties != Collections.EMPTY_MAP) {
                Map props = new LinkedHashMap(defaults)
                props.putAll(properties)
                properties = props
            } else {
                properties = defaults
            }

            if (log.isDebugEnabled()) {
                log.debug("transaction properties ${properties}")
            }
            
            TransactionTemplate template = new TransactionTemplate(ctx.getBean('transactionManager'))            
            txPropsUtil.applyTo properties, template
            
            template.execute( {status ->
                    callable.call(status)
                } as TransactionCallback)
        }

        Closure withTrx = {Map properties = Collections.EMPTY_MAP, Closure callable ->
            withTrxImpl(withTrxDefaults, properties, callable)
        }
        Closure withNewTrx = {Map properties = Collections.EMPTY_MAP, Closure callable ->
            withTrxImpl(withNewTrxDefaults, properties, callable)
        }

        List domainClasses = targetDomainClass != null? [targetDomainClass] : application.domainClasses
        for (domainClass in domainClasses) {
            Class clazz = domainClass.clazz

            // Force the lazy init of GORM dynamic methods
            Closure c = clazz.&withTransaction

            clazz.metaClass.'static'.withTransaction = withTrx
            clazz.metaClass.'static'.withNewTransaction = withNewTrx
        }
    }

}
