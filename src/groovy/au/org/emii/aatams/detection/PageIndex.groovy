package au.org.emii.aatams.detection

import org.joda.time.DateTime

class PageIndex {

    DateTime timestamp
    def receiverName
    def transmitterId

    def toSqlString() {
        return "(timestamp, receiver_name, valid_detection.transmitter_id) > " +
                 "('${timestamp}', '${receiverName}', '${transmitterId}')"
    }

    static PageIndex fromRequestParams(params) {

        def sanitisedParams = [
            timestamp: params.timestamp,
            receiverName: params.receiverName,
            transmitterId: params.transmitterId
        ]

        if (sanitisedParams.timestamp && sanitisedParams.receiverName && sanitisedParams.transmitterId) {
            sanitisedParams.timestamp = new DateTime(sanitisedParams.timestamp)
            return new PageIndex(sanitisedParams)
        }

        return null
    }

    def toRequestParams() {
        [
            timestamp: this.timestamp,
            receiverName: this.receiverName,
            transmitterId: this.transmitterId
        ]
    }

    String toString() {
        return toRequestParams()
    }
}
