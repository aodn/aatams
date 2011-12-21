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
        def lon = null
        def lat = null
        def srid = null
        
        if ((attrs.value == null) && !attrs.editable)
        {
            // Do nothing - we just display an empty string.
        }
        else
        {
            // Set defaults (because we must be editing to make it into this clause).
            if (attrs.value == null)
            {
            }
            else
            {
                lon = attrs.value?.getCoordinate()?.x
                lat = attrs.value?.getCoordinate()?.y
                srid = attrs.value?.getSRID()

                pointAsString += Math.abs(lat) + "\u00b0"
                if (lat >= 0)
                {
                    pointAsString += 'N'
                }
                else
                {
                    pointAsString += 'S'
                }

                pointAsString += ' ' + Math.abs(lon) + "\u00b0"
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
            out << render(template:"/common/geometry/pointInputTemplate", 
                          model:[pointName:attrs.name, 
                                 value:pointAsString,
                                 lon:lon,
                                 lonEastOrWest:(lon >= 0 ? 'E' : 'W'),
                                 lat:lat,
                                 latNorthOrSouth:(lat > 0 ? 'N' : 'S'),
                                 srid:srid,
                                 datums:datumService.datums,
								 clazz:attrs.class]) 
        }
        else
        {
            out << render(template:"/common/geometry/pointOutputTemplate", 
                          model:[pointName:attrs.name, 
                                 value:pointAsString])  
        }
    }
}
