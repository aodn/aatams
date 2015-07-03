package au.org.emii.aatams

/**
 * TODO: get data from "spatial_ref_sys" table.
 */
class DatumService  {
    static transactional = true

    private Map<Integer, String> datums = 
               [4326: "WGS 84",
                28348: "GDA94/ MGA zone 48", 
                28349: "GDA94/ MGA zone 49", 
                28350: "GDA94/ MGA zone 50", 
                28351: "GDA94/ MGA zone 51", 
                28352: "GDA94/ MGA zone 52", 
                28353: "GDA94/ MGA zone 53", 
                28354: "GDA94/ MGA zone 54", 
                28355: "GDA94/ MGA zone 55", 
                28356: "GDA94/ MGA zone 56", 
                28357: "GDA94/ MGA zone 57", 
                28358: "GDA94/ MGA zone 58"]
    
    Map<Integer, String> getDatums() {
        return datums
    }
    
    Map<Integer, String> getDefaultDatum() {
        return [4326, "WGS 84"]
    }
    
    String getName(srid) {
        return datums[srid]
    }
}
