package grails.plugin.transaction.handling;

import groovy.util.Eval;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.DefaultGrailsServiceClass;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsClass;
import org.codehaus.groovy.grails.commons.GrailsServiceClass;
import org.codehaus.groovy.grails.commons.spring.TypeSpecifyableTransactionProxyFactoryBean;
import org.codehaus.groovy.grails.orm.support.GroovyAwareNamedTransactionAttributeSource;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.util.Assert;

public class TransactionHandlingPostProcessor implements BeanPostProcessor,
        BeanFactoryPostProcessor, GrailsApplicationAware, InitializingBean {

    private Log log = LogFactory.getLog(getClass());
    private GrailsApplication grailsApplication;
    private Map<Object, Object> implicitConfig;
    private Map<Object, Object> declarativeConfig;
    private int timeout;

    @Override
    public Object postProcessAfterInitialization(Object bean, String name)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name)
            throws BeansException {
        if (log.isDebugEnabled()) {
            log.debug("postProcessBeforeInitialization(): name " + name);
        }

        if (bean != null && bean instanceof PlatformTransactionManager) {
            if (bean instanceof AbstractPlatformTransactionManager) {
                /* Configure global transaction management. */
                AbstractPlatformTransactionManager tm = (AbstractPlatformTransactionManager) bean;

                if (log.isDebugEnabled()) {
                    log.debug("postProcessBeforeInitialization(): transactionManager "
                            + tm);
                    log.debug("postProcessBeforeInitialization(): transactionManager.defaultTimeout "
                            + tm.getDefaultTimeout());
                    log.debug("postProcessBeforeInitialization(): timeout "
                            + this.timeout);
                }

                if (tm.getDefaultTimeout() != this.timeout
                        && this.timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
                    tm.setDefaultTimeout(this.timeout);
                }

                if (log.isDebugEnabled()) {
                    log.debug("postProcessBeforeInitialization(): transactionManager.defaultTimeout "
                            + tm.getDefaultTimeout());
                }
            } else {
                if (this.timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
                    log.error("postProcessBeforeInitialization(): Default timeout cannot be set for "
                            + bean);
                }
            }
        }

        return bean;
    }

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {

        /* Configure declarative transaction management. */
        if (!this.declarativeConfig.isEmpty()) {
            String[] names = beanFactory.getBeanNamesForType(
                    TransactionInterceptor.class, true, false);
            log.debug("postProcessBeanFactory(): "
                    + TransactionInterceptor.class.getName() + " "
                    + Arrays.asList(names));

            for (String name : names) {
                BeanDefinition defin = beanFactory.getBeanDefinition(name);
                log.debug("postProcessBeanFactory(): old bean class "
                        + defin.getBeanClassName());
                defin.setBeanClassName(ConfigurableTransactionInterceptor.class
                        .getName());
            }
        }

        /* Configure implicit transaction management. */
        if (!this.implicitConfig.isEmpty()) {
            GrailsClass[] serviceClasses = grailsApplication
                    .getArtefacts(DefaultGrailsServiceClass.SERVICE);

            log.debug("postProcessBeanFactory(): serviceClasses "
                    + Arrays.asList(serviceClasses));
            Properties props = new Properties();
            props.setProperty("*", "PROPAGATION_REQUIRED");
            GroovyAwareNamedTransactionAttributeSource source = new GroovyAwareNamedTransactionAttributeSource();
            source.setProperties(props);

            ConfigurableTransactionAttributeSource newSource = new ConfigurableTransactionAttributeSource(
                    source, this.implicitConfig, false);

            for (GrailsClass grailsClass : serviceClasses) {
                GrailsServiceClass serviceClass = (GrailsServiceClass) grailsClass;
                BeanDefinition defin = beanFactory.getBeanDefinition(serviceClass
                        .getPropertyName());
                if (log.isDebugEnabled()) {
                    log.debug("postProcessBeanFactory(): def.getBeanClassName() "
                            + defin.getBeanClassName());
                }

                if (TypeSpecifyableTransactionProxyFactoryBean.class.getName()
                        .equals(defin.getBeanClassName())) {
                    MutablePropertyValues propValues = defin.getPropertyValues();

                    if (log.isDebugEnabled()) {
                        log.debug("postProcessBeanFactory(): old transaction attribute source "
                                + propValues
                                        .getPropertyValue("transactionAttributeSource").getValue());
                    }

                    propValues.removePropertyValue("transactionAttributeSource");
                    propValues.addPropertyValue("transactionAttributeSource",
                            newSource);
                }
            }
        }

    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    @Override
    //@SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.grailsApplication);

        Map<Object, Object> config = (Map<Object, Object>) Eval.x(
                this.grailsApplication,
                "x.mergedConfig.asMap(true).grails.plugin.transactionHandling");
        Map<Object, Object> globalConfig = (Map<Object, Object>) config
                .get("global");
        this.implicitConfig = (Map<Object, Object>) config.get("implicit");
        this.declarativeConfig = (Map<Object, Object>) config
                .get("declarative");
        this.timeout = globalConfig.containsKey("timeout") ? (Integer) globalConfig
                .get("timeout") : TransactionDefinition.TIMEOUT_DEFAULT;
    }

}
