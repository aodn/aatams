package au.org.emii.aatams

class TagFactoryService {

    static transactional = true

    def lookupOrCreate(params) throws IllegalArgumentException
    {
        log.debug(params)
        
        String codeName = params.codeName
        log.debug("Parsing codeName: " + codeName)
        def tag = Tag.findByCodeName(codeName)

        if (tag == null)
        {
            String[] codeNameTokens = codeName.split("-")
            if (codeNameTokens.length != 3)
            {
                String msg = "Invalid tag code name: " + codeName
                log.error(msg)
                throw new IllegalArgumentException(msg)
            }
            else
            {
                TagDeviceModel model = TagDeviceModel.get(params.model.id)
                
                String codeMap = codeNameTokens[0] + "-" + codeNameTokens[1]
                String pingCode = codeNameTokens[2]
                
                try
                {
                    Integer pingCodeAsInt = Integer.valueOf(pingCode)
                    tag = new Tag(codeName:codeName,
                                  serialNumber:params.serialNumber,
                                  codeMap:codeMap,
                                  pingCode:pingCode,
                                  model:model,
                                  status:params.status,
                                  transmitterType:params.transmitterType)
                }
                catch (NumberFormatException e)
                {
                    throw new IllegalArgumentException("Invalid tag code name: " + codeName, e)
                }
            }
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
}
