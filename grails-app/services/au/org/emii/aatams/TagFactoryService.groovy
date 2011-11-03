package au.org.emii.aatams

class TagFactoryService {

    static transactional = true

    def lookupOrCreate(params) throws IllegalArgumentException
    {
		CodeMap codeMap = CodeMap.get(params.codeMap.id)
		def tag = Tag.findByCodeMapAndPingCode(codeMap, params.pingCode)
		
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
		def tag
		
		initDefaults(params)

		def codeMap = CodeMap.get(params.codeMap.id)
		if (!codeMap)
		{
			throw new IllegalArgumentException("Unknown code map ID: " + params.codeMap.id)
		}

		def pingCode

		try
		{
			pingCode = Integer.valueOf(params.pingCode)
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Invalid ping code ID: " + params.pingCode, e)
		}

		tag = new Tag(codeName:Tag.constructCodeName(params),
				codeMap:codeMap,
				serialNumber:params.serialNumber,
				pingCode:pingCode,
				model:TagDeviceModel.get(params.model.id),
				status:params.status,
				transmitterType:params.transmitterType)
		codeMap.addToTags(tag)
		return tag
	}

	private initDefaults(params) 
	{
		if (!params.status)
		{
			params.status = DeviceStatus.findByStatus('NEW')
		}

		if (!params.transmitterType)
		{
			params.transmitterType = TransmitterType.findByTransmitterTypeName('PINGER')
		}
	}
}
