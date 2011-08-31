package au.org.emii.aatams

class TagFactoryService {

    static transactional = true

    def lookupOrCreate(params) 
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
                log.error("Invalid tag code name: " + codeName)
            }
            else
            {
                DeviceModel model = DeviceModel.get(params.model.id)
                
                String codeMap = codeNameTokens[0] + "-" + codeNameTokens[1]
                String pingCode = codeNameTokens[2]
                tag = new Tag(codeName:codeName,
                              serialNumber:params.serialNumber,
                              codeMap:codeMap,
                              pingCode:pingCode,
                              model:model,
                              project:params.project,
                              status:params.status,
                              transmitterType:params.transmitterType)
                
                if (tag.save(flush:true))
                {
                    log.info("Created tag: " + String.valueOf(tag))
                }
                else
                {
                    log.error(tag.errors)
                }
            }
        }
        
        return tag
    }
}
