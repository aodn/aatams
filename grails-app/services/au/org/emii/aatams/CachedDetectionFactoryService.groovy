package au.org.emii.aatams

/**
 * Provides own caching for tags, receivers.
 * 
 * @author jburgess
 */
class CachedDetectionFactoryService extends DetectionFactoryService
{
    Map<String, Collection<Tag>> tagCache = new HashMap<String, Collection<Tag>>()
    Map<String, Receiver> receiverCache = new HashMap<String, Receiver>()
    
    def findTags(codeMap, pingCode)
    {
        // Check in cache first...
        String key = codeMap + "-" + pingCode
        Collection<Tag> tags = tagCache.get(key)
        
        if (tags)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Cache hit for tag key: " + key)
            }
            return tags
        }
        
        // Call super.
        if (log.isDebugEnabled())
        {
            log.debug("Cache miss for tag key: " + key)
        }
        tags = super.findTags(codeMap, pingCode)
        
        // Add to cache before returning.
        if (tags)
        {
            tagCache.put(key, tags)
        }
        
        return tags
    }
    
    def findReceiver(codeName) throws FileProcessingException
    {
        // Check in cache first...
        Receiver receiver = receiverCache.get(codeName)
        
        if (receiver)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Cache hit for receiver key: " + codeName)
            }
            return receiver
        }
        
        // Call super.
        if (log.isDebugEnabled())
        {
            log.debug("Cache miss for receiver key: " + codeName)
        }
        receiver = super.findReceiver(codeName)
        
        // Add to cache before returning.
        if (receiver)
        {
            receiverCache.put(codeName, receiver)
        }
        
        return receiver
    }
    
    void clearCache()
    {
        log.debug("Clearing cache...")
        
        tagCache.clear()
        receiverCache.clear()
    }
}

