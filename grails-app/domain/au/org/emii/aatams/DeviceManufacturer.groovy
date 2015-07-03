package au.org.emii.aatams

class DeviceManufacturer  {
    String manufacturerName

    static constraints = {
        manufacturerName(blank:false, unique:true)
    }

    String toString() {
        return manufacturerName
    }
}
