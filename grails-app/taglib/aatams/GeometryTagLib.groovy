package aatams

class GeometryTagLib 
{
    def pointInput = { attrs, body ->
        
        out << render(template:"/common/geometry/pointInputTemplate", 
                      model:[parentName:attrs.name, lon:attrs.lon, lat:attrs.lat, srid:attrs.srid])
    }
    
    def pointOutput = { attrs, body ->
        
        out << render(template:"/common/geometry/pointOutputTemplate", 
                      model:[parentName:attrs.name, lon:attrs.lon, lat:attrs.lat, srid:attrs.srid])
    }
    
    /**
     * Generic tag for Point types.
     *
     * The template used depends on whether the "editable" attribute is set.
     */
    def point = { attrs, body ->
        
        def pointAsString = ""
        def lon
        def lat
        def srid
        
        if (attrs.value == null)
        {
            lon = 0.0
            lat = 0.0
            srid = 4326
        }
        else
        {
            lon = attrs.value?.getCoordinate()?.x
            lat = attrs.value?.getCoordinate()?.y
            srid = attrs.value?.getSRID()
        }
        
        pointAsString += Math.abs(lon) + "°"
        if (lon >= 0)
        {
            pointAsString += 'N'
        }
        else
        {
            pointAsString += 'S'
        }
        
        pointAsString += ' ' + Math.abs(lat) + "° "
        if (lat >= 0)
        {
            pointAsString += 'E'
        }
        else
        {
            pointAsString += 'W'
        }
        
        pointAsString += " (EPSG:" + srid + ")"

        if (attrs.editable)
        {
            out << render(template:"/common/geometry/pointInputTemplate", 
                          model:[parentName:attrs.name, 
                                 value:pointAsString,
                                 lon:lon,
                                 lat:lat,
                                 srid:srid]) 
        }
        else
        {
            out << render(template:"/common/geometry/pointOutputTemplate", 
                          model:[parentName:attrs.name, 
                                 value:pointAsString])  
        }
    }
}
