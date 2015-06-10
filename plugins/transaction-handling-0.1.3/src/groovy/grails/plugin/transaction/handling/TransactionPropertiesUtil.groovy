package grails.plugin.transaction.handling

import grails.util.GrailsNameUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;

class TransactionPropertiesUtil {
    
    private final Log log = LogFactory.getLog(getClass())
    
    public LinkedHashMap removeImmutableDefaults(Map properties, String ... k) {
        List keys = ['propagation']
        if (k) {
            keys.addAll(k as List)
        }
        
        Map newProperties = new LinkedHashMap(properties)     
        for (key in keys) {
            for (entry in properties.entrySet()) {
                if (entry.key.startsWith(key)) {
                    newProperties.remove(entry.key)
                }
            }
        }
        return newProperties               
    }
    
    public void applyTo(Map properties, Object target) {
        Map rollbackMapping = [rollbackFor: RollbackRuleAttribute, noRollbackFor: NoRollbackRuleAttribute] 
        
        int constantModifier = Modifier.FINAL | Modifier.STATIC | Modifier.PUBLIC
        /* [transactionTemplatePropertyAlias: [transactionDefinitionConstantAlias: [name: constantName, value: constantValue]]] */
        Map constantMappings = [propagation: [:], isolation: [:], timeout: [:]]
        Set constantPrefixes = new LinkedHashSet(constantMappings.keySet().collect {it.toUpperCase()})
                
        for (field in TransactionDefinition.class.fields) {
            if ((field.modifiers & constantModifier) == constantModifier) {
                for (prefix in constantPrefixes) {
                    if (field.name.startsWith(prefix)) {
                        String key = field.name.replace(prefix, '').replace('_', '-').toLowerCase()
                        key = GrailsNameUtils.getPropertyNameForLowerCaseHyphenSeparatedName(key)
                        constantMappings[prefix.toLowerCase()][key] = [name: field.name, value: field.get(null)]
                    }
                }
            }
        }
        
        if (log.isDebugEnabled()) {
            log.debug("apply(): constantMappings ${constantMappings}")
        }
        
        
        /* [transactionTemplatePropertyAlias: [name: transactionTemplatePropertyName, value: transactionDefinitionConstantNameOrValue]] */
        Map propertyMappings = [propagation: [name: 'propagationBehaviorName', value: 'name'],
                                isolation: [name: 'isolationLevelName', value: 'name'],
                                timeout: [name: 'timeout', value: 'value']]
        
        if (log.isDebugEnabled()) {
            log.debug("apply(): transaction properties ${properties}")
        }        
               
        for (prop in properties) {
            if (!rollbackMapping.containsKey(prop.key)) {
                String name = prop.key
                Object value = prop.value

                Map propMapping = propertyMappings[name]

                String newName = propMapping?.name
                Object newValue = constantMappings[name]

                if (value != null && !(value instanceof CharSequence)) {
                    // shortcut for non-text values
                    newValue = null
                }

                if (newValue != null && value != null) {
                    newValue = newValue[value.toString()]
                    if (newValue != null) {
                        // Invalid property names produce null values
                        newValue = newValue[propMapping?.value]
                    }
                }

                name = (newName != null)? newName : name
                value = (newValue != null)? newValue: value

                if (log.isDebugEnabled()) {
                    log.debug("apply(): ${name}=${value}")
                }
                target[name] = value
            } 
        }
        
        List rollbackRules = null
        for (rollback in rollbackMapping.entrySet()) {
            if (properties.containsKey(rollback.key)) {
                Constructor stringConstructor = (rollback.value as Class).getConstructor(String) 
                Constructor classConstructor = (rollback.value as Class).getConstructor(Class)
                
                if (rollbackRules == null) {
                    rollbackRules = []
                }
                for (exception in properties[rollback.key]) {
                    if (exception instanceof CharSequence) {
                        exception = exception.toString()
                        exception = stringConstructor.newInstance(exception)
                    } else {
                        exception = classConstructor.newInstance(exception)
                    }
                    
                    rollbackRules << exception
                }
                     
            }
        }
        
        if (rollbackRules) {
            target.rollbackRules = rollbackRules
        }
    }
    
    public LinkedHashMap expand(Map properties) {
        LinkedHashMap expandedProperties = new LinkedHashMap()
        
        applyTo(properties, expandedProperties);
        
        return expandedProperties
    }

}
