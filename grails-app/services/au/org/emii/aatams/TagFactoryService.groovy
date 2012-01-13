package au.org.emii.aatams

class TagFactoryService {

    static transactional = true

    def lookupOrCreate(params) throws IllegalArgumentException
    {
		def tag = Tag.findBySerialNumber(params.serialNumber)
		
        if (tag == null)
        {
            tag = createNewTag(params)
        }
        
        if (!tag.project)
        {
            tag.project = params.project
        }
        else if (tag.project != params.project)
        {
            log.warn("Tag released in different project, tag's project: " + tag.project + ", release project: " + params.project)
        }
        
        if (tag.save(flush:true))
        {
            log.info("Created tag: " + String.valueOf(tag))
        }
        else
        {
            log.error(tag.errors)
        }
        
        return tag
    }

	private Tag createNewTag(params) 
	{
		initDefaults(params)
		
		return new Tag(params)
	}

	private initDefaults(params) 
	{
		if (!params.status)
		{
			params.status = DeviceStatus.findByStatus('NEW')
		}
	}
}
