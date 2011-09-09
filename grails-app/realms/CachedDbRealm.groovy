/**
 *
 * @author jburgess
 */
class CachedDbRealm extends SecDbRealm
{
    // This is here for unit testing
    boolean cacheHit = false
    
    private Map<Object, Map<String, Boolean>> cache = [:]
    
    def isPermitted(principal, requiredPermission) 
    {
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

