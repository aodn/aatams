package au.org.emii.aatams

import grails.converters.JSON

/**
 * Represents a physical tag (which may be attached at any one time to an animal via a surgery).
 *
 * @author jburgess
 *
 */
class Tag extends Device implements Embargoable {
    DeviceStatus status

    static hasMany = [sensors:Sensor,
                      surgeries:Surgery]

    Project project
    static belongsTo = [codeMap: CodeMap]
    static auditable = true

    /**
     * The expected lifetime (in days) of a tag once is it deployed.  This
     * value is used to derive the "window of operation" of a Surgery when
     * searching for surgeries to match against detections.
     *
     * If not specified, then assume infinity.
     */
    Integer expectedLifeTimeDays

    static constraints = {
        project(nullable:true)
        expectedLifeTimeDays(nullable:false, min:0)
        sensors (validator: { sensors ->
            def error = true
            sensors.every {
                if (!it.validate()) {
                    error = 'tag.invalidCodeMapTransmitterType'
                }
            }
            return  error
        })
    }

    static transients = ['expectedLifeTimeDaysAsString', 'deviceID', 'pinger', 'pingCode', 'pingCodes', 'transmitterTypeNames', 'nonPingerSensors', 'owningPIs']

    static searchable = [only: ['serialNumber']]

    static mapping = {
        cache true
        surgeries cache:true
    }

    // For reports...
    String getExpectedLifeTimeDaysAsString() {
        return String.valueOf(expectedLifeTimeDays)
    }

    String toString() {
        return getDeviceID()
    }

    String getDeviceID() {
        return sensors?.join(", ")
    }

    Sensor getPinger() {
        def searchPinger = sensors.find {
            it.transmitterType == TransmitterType.findByTransmitterTypeName('PINGER', [cache:true])
        }
        return searchPinger
    }

    void setPingCode(Integer newPingCode) {
        if (!pinger) {
            Sensor newPinger = new Sensor(tag: this, pingCode: newPingCode, transmitterType: TransmitterType.findByTransmitterTypeName('PINGER', [cache:true]))
            addToSensors(newPinger)
        }
        else {
            assert(pinger)
            pinger.pingCode = newPingCode
        }
    }

    Integer getPingCode() {
        return pinger?.pingCode
    }

    void setCodeMap(CodeMap codeMap) {
        this.codeMap = codeMap
        sensors.each {
            it?.refreshTransmitterId()
        }
    }

    String getPingCodes() {
        return sensors*.pingCode?.sort()?.join(", ")
    }

    String getTransmitterTypeNames() {
        return sensors*.transmitterType?.transmitterTypeName?.join(", ")
    }

    List<Sensor> getNonPingerSensors() {
        return Sensor.findAllByTagAndTransmitterTypeNotEqual(this, TransmitterType.findByTransmitterTypeName('PINGER', [cache:true]), [sort:"transmitterId"])
    }

    def applyEmbargo() {
        boolean embargoed = false

        surgeries.each {
            embargoed |= it.release.isEmbargoed()
        }

        if (this instanceof Sensor) {
            tag.surgeries.each {
                embargoed |= it.release.isEmbargoed()
            }
        }

        if (!embargoed) {
            return this
        }

        log.debug("Tag is embargoed, id: " + id)
        return null
    }

    List<Person> getOwningPIs() {
        if (project) {
            return project.getPrincipalInvestigators()
        }

        return []
    }

    List<TransmitterType> getUnusedTransmitterTypes() {

        def typesOnThis = sensors*.transmitterType
        def unusedTypes = TransmitterType.list() - typesOnThis
        unusedTypes.retainAll(codeMap.listValidTransmitterTypes())

        return unusedTypes
    }

    static void registerObjectMarshaller() {
        JSON.registerObjectMarshaller(Tag.class) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['label'] = it.serialNumber
            returnArray['serialNumber'] = it.serialNumber
            returnArray['model'] = it.model
            returnArray['codeMap'] = it.codeMap
            returnArray['deviceID'] = it.deviceID
            returnArray['project'] = it.project
            returnArray['expectedLifeTimeDays'] = it.expectedLifeTimeDays
            returnArray['status'] = it.status
            returnArray['pingCode'] = it.pingCodes

            return returnArray
        }
    }
}
