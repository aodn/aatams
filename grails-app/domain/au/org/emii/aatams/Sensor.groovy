package au.org.emii.aatams

import org.apache.commons.lang.StringUtils

/**
 * Represents a single sensor belonging to a tag (there can be more than one
 * sensor on each tag).
 */
class Sensor implements Embargoable {
    static belongsTo = [tag:Tag]

    static transients = ['project', 'codeName', 'codeMap', 'status', 'owningPIs']

    Integer pingCode
    TransmitterType transmitterType

    /**
     * Sensor units.
     */
    String unit

    /**
     * Calibration slope.
     */
    Float slope

    /**
     * Calibration intercept.
     */
    Float intercept

    /**
     * Concatenation of code map and ping code, e.g. "A69-9002-1234".
     */
    String transmitterId

    static constraints = {
        tag(validator: codeMapValidator)
        pingCode()
        transmitterType(unique: 'tag')
        unit(nullable:true, validator: sensorFieldValidator, inList: ["m", "°C", "m/s²"])
        slope(nullable:true, validator: sensorFieldValidator)
        intercept(nullable:true, validator: sensorFieldValidator)
        transmitterId(nullable:true)
    }

    static mapping = {
        cache true
    }

    static searchable = {
        tag(component:true)
    }

    void setPingCode(Integer pingCode) {
        this.pingCode = pingCode
        refreshTransmitterId()
    }

    void setTag(Tag tag) {
        this.tag = tag
        refreshTransmitterId()
    }

    private void refreshTransmitterId() {
        transmitterId = codeMap?.codeMap + '-' + pingCode
    }

    String getCodeName() {
        return transmitterId
    }

    DeviceStatus getStatus() {
        return tag?.getStatus()
    }

    CodeMap getCodeMap() {
        return tag?.getCodeMap()
    }

    String toString() {
        return transmitterId
    }

    Project getProject() {
        return tag.project
    }

    def applyEmbargo() {
        if (tag.applyEmbargo()) {
            return this
        }

        return null
    }

    static def codeMapValidator = { tag, deployment ->
        if (!deployment.tag.codeMap.listValidTransmitterTypeValues().contains(deployment.transmitterType.toString())) {
            CodeMap invalidCodeMap = CodeMap.get(tag.codeMap.id)
            return ['sensor.invalidCodeMapTransmitterType', invalidCodeMap.codeMap, StringUtils.join(invalidCodeMap.listValidTransmitterTypeValues(), ", ")]
        }
    }

    static def sensorFieldValidator = { field, obj ->
       return ['PINGER','RANGE_TEST'].contains(obj.transmitterType.toString()) ||
               field != null ? true : ['sensor.missingSensorFields', obj.transmitterType.toString()
       ]
    }

    List<Person> getOwningPIs() {
        return tag.getOwningPIs()
    }

    static Map<Person, SortedSet<Sensor>> groupByOwningPI(List<Sensor> sensors) {
        def groupedSensors = new HashMap<Person, SortedSet<Sensor>>()

        sensors.each {
            sensor ->

            def pis = sensor.getOwningPIs()

            pis.each {
                pi ->

                def sensorsForPI = groupedSensors[pi]
                if (!sensorsForPI) {
                    sensorsForPI = new TreeSet<Sensor>([compare: {a, b -> a.transmitterId <=> b.transmitterId} ] as Comparator)
                    groupedSensors[pi] = sensorsForPI
                }

                sensorsForPI.add(sensor)
            }
        }

        return groupedSensors
    }

    List<TransmitterType> getTypesCanChangeTo() {

        return tag.unusedTransmitterTypes + transmitterType
    }
}
