package aatams

import au.org.emii.aatams.DatumService

class GeometryTagLib 
{
    def datumService
    
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
        
        if ((attrs.value == null) && !attrs.editable)
        {
            // Do nothing - we just display an empty string.
        }
        else
        {
            // Set defaults (because we must be editing to make it into this clause).
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

                pointAsString += Math.abs(lat) + "°"
                if (lat >= 0)
                {
                    pointAsString += 'N'
                }
                else
                {
                    pointAsString += 'S'
                }

                pointAsString += ' ' + Math.abs(lon) + "° "
                if (lon >= 0)
                {
                    pointAsString += 'E'
                }
                else
                {
                    pointAsString += 'W'
                }

                pointAsString += " (datum:" + datumService.getName(srid) + ")"
            }
        }
        
        if (attrs.editable)
        {
            log.debug("Datums: " + datumService.datums)

            out << render(template:"/common/geometry/pointInputTemplate", 
                          model:[parentName:attrs.name, 
                                 value:pointAsString,
                                 lon:lon,
                                 lonEastOrWest:(lon >= 0 ? 'E' : 'W'),
                                 lat:lat,
                                 latNorthOrSouth:(lat > 0 ? 'N' : 'S'),
                                 srid:srid,
                                 datums:datumService.datums]) 
        }
        else
        {
            out << render(template:"/common/geometry/pointOutputTemplate", 
                          model:[parentName:attrs.name, 
                                 value:pointAsString])  
        }
    }
}
