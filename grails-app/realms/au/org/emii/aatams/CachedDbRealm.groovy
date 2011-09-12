package au.org.emii.aatams

import shiro.SecDbRealm

/**
 *
 * @author jburgess
 */
class CachedDbRealm extends SecDbRealm
{
    // This is here for unit testing
    boolean cacheHit = false
    
    /**
     * Keyed by principal, with each principal's map in turn being a map
     * of permission name to bool value.
     */
    private Map<Object, Map<String, Boolean>> cache = [:]
    
    /**
     * Becomes true when something happens that means the cache is out of date
     */
    static boolean cacheIsDirty = false

    static markCacheDirty()
    {
        cacheIsDirty = true
    }
    
    def checkCache()
    {
        if (cacheIsDirty)
        {
            cache = [:]
            cacheIsDirty = false
        }
    }
    
    def isPermitted(principal, requiredPermission) 
    {
        checkCache()
        cacheHit = false
       
        // Is the principal in the cache?
        def principalsCache = cache.get(principal)
        
        // Never checked for this user before...
        if (!principalsCache)
        {
            principalsCache = [:]
            cache[principal] = principalsCache
        }
        
        def permitted = principalsCache.get(requiredPermission)
        if (permitted == null)
        {
            principalsCache[requiredPermission] = super.isPermitted(principal, requiredPermission)
        }
        else
        {
            cacheHit = true
        }
        
        def retVal = cache[principal][requiredPermission]
        assert(retVal != null): "retVal cannot be null"
        
        return retVal
    }
    
}

