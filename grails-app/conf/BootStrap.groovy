import au.org.emii.aatams.*
import au.org.emii.aatams.data.*

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
    def permissionUtilsService // = new PermissionUtilsService()
    
    def init = 
    { 
        servletContext ->

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
            returnArray['label'] = it.getCodeMapPingCode()
            returnArray['serialNumber'] = it.serialNumber
            returnArray['model'] = it.model
            returnArray['transmitterType'] = it.transmitterType
            returnArray['codeName'] = it.codeName

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
            returnArray['codeMap'] = it.codeMap
            returnArray['pingCode'] = it.pingCode
            returnArray['slope'] = it.slope
            returnArray['intercept'] = it.intercept
            returnArray['unit'] = it.unit
            returnArray['status'] = it.status

            returnArray['label'] = it.getCodeMapPingCode()
            returnArray['serialNumber'] = it.serialNumber
            returnArray['model'] = it.model
            
            return returnArray
        }
        
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
                initialiser = new DevelopmentDataInitialiser(permissionUtilsService)

                assert(initialiser): "Initialiser cannot be null"
                initialiser.execute()
            }
            
            production
            {
                initialiser = new ReferenceDataInitialiser(permissionUtilsService)
                assert(initialiser): "Initialiser cannot be null"
                initialiser.execute()
            }
            
            performance
            {
                initialiser = new PerformanceDataInitialiser(permissionUtilsService)
                assert(initialiser): "Initialiser cannot be null"
                initialiser.execute()
            }
        }
    }
    
    def destroy = 
    {
    }
 }
