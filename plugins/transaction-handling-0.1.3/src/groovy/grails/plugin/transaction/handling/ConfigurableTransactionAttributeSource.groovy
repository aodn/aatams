package grails.plugin.transaction.handling;

import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.IdentityHashMap
import java.util.Map

import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
import org.springframework.transaction.interceptor.TransactionAttribute
import org.springframework.transaction.interceptor.TransactionAttributeSource
import org.springframework.util.Assert


public class ConfigurableTransactionAttributeSource implements TransactionAttributeSource {

    private final Log log = LogFactory.getLog(getClass());
    private final Map cache = new IdentityHashMap();
    protected final TransactionAttributeSource source;
    private final RuleBasedTransactionAttribute configuredDefaults;
    private final RuleBasedTransactionAttribute defaults;

    public ConfigurableTransactionAttributeSource(TransactionAttributeSource source, Map config, boolean ignoreReadOnly = true) {
        this.source = source;
        Assert.notNull(this.source);
        Assert.notNull(config);

        if (!config.isEmpty()) {
            TransactionPropertiesUtil txPropsUtil = new TransactionPropertiesUtil();

            this.configuredDefaults = new RuleBasedTransactionAttribute();
            config = txPropsUtil.expand(config)
            if (ignoreReadOnly) {
                config = txPropsUtil.removeImmutableDefaults(config, 'readOnly')
            } else {
                config = txPropsUtil.removeImmutableDefaults(config)
            }
            txPropsUtil.applyTo config, configuredDefaults

            this.defaults = new RuleBasedTransactionAttribute();
        } else {
            this.configuredDefaults = null;
            this.defaults = null;
        }
    }

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> clazz) {
        TransactionAttribute att = this.source.getTransactionAttribute(method, clazz)

        if (log.isDebugEnabled()) {
            log.debug("getTransactionAttribute(): att ${att}; template ${configuredDefaults}")
        }

        if (configuredDefaults != null) {
            TransactionAttribute newAtt = cache[att]
            if (newAtt == null) {
                Constructor c = null
                if (att != null) {
                    Class attClass = att.getClass()
                    c = attClass.getConstructor(attClass)
                }

                if (c != null) {
                    if (att.metaClass.hasProperty(att, 'rollbackRules')) {
                        /* Workaround: new RuleBasedTransactionAttribute(RuleBasedTransactionAttribute) is broken for rollbackRules == null */
                        att.rollbackRules
                    }
                    newAtt = c.newInstance(att)
                } else {
                    if (att != null) {
                        newAtt = BeanUtils.cloneBean(att)
                    } else {
                        newAtt = new RuleBasedTransactionAttribute()
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("getTransactionAttribute(): newAtt ${newAtt}")
                }


                for (key in BeanUtils.describe(configuredDefaults).keySet()) {
                    if (log.isDebugEnabled()) {
                        log.debug("getTransactionAttribute(): key ${key}")
                    }
                    Object value = configuredDefaults[key]

                    if (value != null && value != defaults[key] && newAtt[key] == defaults[key]) {
                        if (log.isDebugEnabled()) {
                            log.debug("getTransactionAttribute(): ${key} = ${value}")
                        }
                        newAtt[key] = value
                    }
                }
            }

            cache[att] = newAtt
            att = newAtt
        }

        if (log.isDebugEnabled()) {
            log.debug("getTransactionAttribute(): att = ${att}")
        }

        return att
    }
}