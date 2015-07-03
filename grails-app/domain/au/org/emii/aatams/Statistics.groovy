package au.org.emii.aatams

class Statistics {
    String key
    Long value

    static transients = ['getStatistic']

    static mapping = {
        key(unique: true) 
    }
    
    static Long getStatistic(key) {
        return Statistics.findByKey(key)?.value
    }
}
