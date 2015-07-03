package au.org.emii.aatams

/**
 * Ping or Sensor type (e.g. PINGER, DEPTH, TEMPERATURE, ACCELEROMETER).
 */
class TransmitterType {
    String transmitterTypeName

    static constraints = {
        transmitterTypeName(blank:false, unique:true)
    }

    String toString() {
        return transmitterTypeName
    }

    static def sensorTypes() {
        return (list() - findByTransmitterTypeName('PINGER'))
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result \
                + ((transmitterTypeName == null) ? 0 : transmitterTypeName
                        .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.is(obj))
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransmitterType other = (TransmitterType) obj;
        if (transmitterTypeName == null) {
            if (other.transmitterTypeName != null)
                return false;
        } else if (!transmitterTypeName.equals(other.transmitterTypeName))
            return false;
        return true;
    }


}
