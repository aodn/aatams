package grails.plugin.transaction.handling;

import java.util.Map
import java.util.Properties

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.springframework.beans.factory.InitializingBean
import org.springframework.transaction.interceptor.TransactionAttributeSource
import org.springframework.transaction.interceptor.TransactionInterceptor
import org.springframework.util.Assert

class ConfigurableTransactionInterceptor extends TransactionInterceptor
        implements GrailsApplicationAware, InitializingBean {

    private final Log log = LogFactory.getLog(getClass());
    private GrailsApplication grailsApplication;
    private TransactionAttributeSource transactionAttributeSource;

    public ConfigurableTransactionInterceptor() {
        log.debug("constructor()");
    }


    @Override
    public void setTransactionAttributeSources(
            TransactionAttributeSource[] transactionAttributeSources) {
        log.debug("setTransactionAttributeSources(): transactionAttributeSources ${transactionAttributeSources}");
        super.setTransactionAttributeSources(transactionAttributeSources);
    }

    @Override
    public void setTransactionAttributes(Properties transactionAttributes) {
        log.debug("setTransactionAttributes(): transactionAttributes ${transactionAttributes}");
        super.setTransactionAttributes(transactionAttributes);
    }

    
    @Override
    public void setTransactionAttributeSource(
            TransactionAttributeSource transactionAttributeSource) {
        log.debug("setTransactionAttributeSource(): transactionAttributeSource ${transactionAttributeSource}");
        this.transactionAttributeSource = transactionAttributeSource;
    }
    
                    
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(grailsApplication)
        Assert.notNull(transactionAttributeSource)                     
        Map declarativeConfig = grailsApplication.mergedConfig.asMap(true).grails.plugin.transactionHandling.declarative
        super.setTransactionAttributeSource(new ConfigurableTransactionAttributeSource(this.transactionAttributeSource, declarativeConfig))              
        super.afterPropertiesSet();
    }


    @Override
    public TransactionAttributeSource getTransactionAttributeSource() {
        // log.debug('getTransactionAttributeSource(): Begin')
        TransactionAttributeSource source = super
                .getTransactionAttributeSource();
        Assert.notNull(source);

        Assert.isAssignable(ConfigurableTransactionAttributeSource.class,
                source.getClass());

        return source;
        // log.debug('getTransactionAttributeSource(): End')
    }

    @Override
    public void setGrailsApplication(GrailsApplication grailsApplication) {
        if (log.isDebugEnabled()) {
            log.debug("setGrailsApplication(): grailsApplication ${grailsApplication}");
        }
        this.grailsApplication = grailsApplication;
    }

}
