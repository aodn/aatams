package aatams

class GeometryTagLib 
{
    def pointInput = { attrs, body ->
        
        out << render(template:"/common/geometry/pointInputTemplate", 
                      model:[parentName:attrs.name, lon:attrs.lon, lat:attrs.lat, datum:attrs.datum])
    }
    
    def pointOutput = { attrs, body ->
        
        out << render(template:"/common/geometry/pointOutputTemplate", 
                      model:[parentName:attrs.name, lon:attrs.lon, lat:attrs.lat, datum:attrs.datum])
    }
    
}
