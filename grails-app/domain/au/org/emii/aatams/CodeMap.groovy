package au.org.emii.aatams

/**
 * Code map, specific to Vemco code maps currently.

 * @author jburgess
 */
class CodeMap  {
    String codeMap
    static hasMany = [tags:Tag, validCodeMapTransmitterTypes:ValidCodeMapTransmitterType]

    static constraints =  {
        codeMap(nullable:false, blank:false, unique:true)
    }

    static mapping = {
        sort "codeMap"
    }

    String toString() {
        return codeMap
    }

    List<String> listValidTransmitterTypeValues() {
        List<ValidCodeMapTransmitterType> codeMapTransmitterTypes = ValidCodeMapTransmitterType.findAllByCodeMap(this)
        List<String> transmitterTypes = new ArrayList<String>();
        if (codeMapTransmitterTypes.size() > 0) {
            for (ValidCodeMapTransmitterType type :  codeMapTransmitterTypes) {
                transmitterTypes.add(type.transmitterType.toString())
            }
        } else {
            for (TransmitterType transmitterType :  TransmitterType.findAll()) {
                transmitterTypes.add(transmitterType.toString())
            }
        }

        return transmitterTypes
    }

    List<TransmitterType> listValidTransmitterTypes() {
        List<ValidCodeMapTransmitterType> codeMapTransmitterTypes = ValidCodeMapTransmitterType.findAllByCodeMap(this)
        List<String> transmitterTypes = new ArrayList<String>();

        if (codeMapTransmitterTypes.size() > 0) {
            for (ValidCodeMapTransmitterType type :  codeMapTransmitterTypes) {
                transmitterTypes.add(type.transmitterType)
            }
            return transmitterTypes
        }
         else
            return TransmitterType.findAll()

    }
}
