import au.org.emii.aatams.*
import au.org.emii.aatams.data.*
import au.org.emii.aatams.detection.*

import grails.converters.JSON

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

import org.apache.shiro.crypto.hash.Sha256Hash
import org.joda.time.*
import org.joda.time.format.DateTimeFormat

import com.vividsolutions.jts.geom.Point

import shiro.*

class BootStrap 
{
    def permissionUtilsService
    def searchableService

    def init = 
    { 
        servletContext ->

		Map.metaClass.flatten = 
		{ 
			String prefix='' ->
			
			delegate.inject( [:] ) 
			{
				map, v ->
				def kstr = "$prefix${ prefix ? '.' : ''  }$v.key"
				
				if( v.value instanceof Map ) map += v.value.flatten( kstr )
				else                         map[ kstr ] = v.value
				map
		    }
		}
		
        JSON.registerObjectMarshaller(Animal.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.toString()

            return returnArray
        }
        
        // Add "label" property for the jquery autocomplete plugin.
        JSON.registerObjectMarshaller(Species.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.toString()
            returnArray['label'] = it.toString()

            return returnArray
        }

        JSON.registerObjectMarshaller(CaabSpecies.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['name'] = it.toString()
            returnArray['label'] = it.toString()

            return returnArray
        }
		
		JSON.registerObjectMarshaller(Installation.class)
		{
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['name'] = it.name
			returnArray['label'] = it.name
			return returnArray
		}

		JSON.registerObjectMarshaller(InstallationStation.class)
		{
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['name'] = it.name
			returnArray['label'] = it.name
			returnArray['location'] = it.location
			return returnArray
		}

		JSON.registerObjectMarshaller(Project.class)
		{
			def returnArray = [:]
			returnArray['id'] = it.id
			returnArray['name'] = it.name
			returnArray['label'] = it.name
			return returnArray
		}

        JSON.registerObjectMarshaller(ProjectAccess.class)
        {
            def returnArray = [:]
            returnArray['displayStatus'] = it.displayStatus
            return returnArray
        }

        // TODO: this is being ignored for some reason (so we register a
        // custom marshaller for Surgery, which includes a DateTime, 
        // instead).
        JSON.registerObjectMarshaller(DateTime.class, 0)
        {
            println("Formatting date")
            return DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss zz").print(it)
        }

        JSON.registerObjectMarshaller(Surgery.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['timestamp'] = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss zz").print(it.timestamp)
            returnArray['tag'] = it.tag
            returnArray['type'] = it.type
            returnArray['treatmentType'] = it.treatmentType
            returnArray['comments'] = it.comments

            return returnArray
        }
        
        JSON.registerObjectMarshaller(Tag.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['label'] = it.serialNumber
            returnArray['serialNumber'] = it.serialNumber
            returnArray['model'] = it.model
			returnArray['codeMap'] = it.codeMap
			returnArray['project'] = it.project
			returnArray['expectedLifeTimeDays'] = it.expectedLifeTimeDays
			returnArray['status'] = it.status
			
            return returnArray
        }
        

        JSON.registerObjectMarshaller(Point.class)
        {
            return [x:it.coordinate.x,
                    y:it.coordinate.y,
                    srid:it.SRID]
        }
           
        JSON.registerObjectMarshaller(AnimalMeasurement.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['type'] = it.type
            returnArray['value'] = it.value
            returnArray['unit'] = it.unit
            returnArray['estimate'] = it.estimate
            returnArray['comments'] = it.comments
            
            return returnArray
        }
        
        JSON.registerObjectMarshaller(ProjectRole.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['person'] = it.person
            returnArray['roleType'] = it.roleType
            returnArray['access'] = it.access
            
            return returnArray
        }
        
        JSON.registerObjectMarshaller(OrganisationProject.class)
        {
            def returnArray = [:]
            
            returnArray['id'] = it.id
            returnArray['organisation'] = it.organisation
            
            return returnArray
        }
        
        JSON.registerObjectMarshaller(Sensor.class)
        {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['transmitterType'] = it.transmitterType
            returnArray['codeMap'] = it.tag.codeMap
            returnArray['pingCode'] = it.pingCode
            returnArray['slope'] = it.slope
            returnArray['intercept'] = it.intercept
            returnArray['unit'] = it.unit
            returnArray['status'] = it.status

            returnArray['label'] = it.transmitterId
            returnArray['serialNumber'] = it.tag.serialNumber
            returnArray['model'] = it.tag.model
            
            return returnArray
        }
        
        searchableService.stopMirroring()
        
        assert(permissionUtilsService): "permissionUtilsService cannot be null"
        DataInitialiser initialiser  //= new DevelopmentDataInitialiser(permissionUtilsService)
            
        environments
        {
            test
            {
                initialiser = new TestDataInitialiser(permissionUtilsService)
                assert(initialiser): "Initialiser cannot be null"
                initialiser.execute()
            }
            
            development
            {
//                initialiser = new DevelopmentDataInitialiser(permissionUtilsService)
//
//                assert(initialiser): "Initialiser cannot be null"
//                initialiser.execute()
            }
            
            production
            {
//                initialiser = new ReferenceDataInitialiser(permissionUtilsService)
//                assert(initialiser): "Initialiser cannot be null"
//                initialiser.execute()
            }
            
            performance
            {
                initialiser = new PerformanceDataInitialiser(permissionUtilsService)
                assert(initialiser): "Initialiser cannot be null"
                initialiser.execute()
            }
        }
        
        searchableService.startMirroring()
    }
    
    def destroy = 
    {
    }
}
