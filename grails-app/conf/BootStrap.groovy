import au.org.emii.aatams.*
import au.org.emii.aatams.data.*
import au.org.emii.aatams.detection.*

import grails.converters.JSON
import grails.converters.XML

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

import com.vividsolutions.jts.geom.Point

class BootStrap
{
    def grailsApplication
    def permissionUtilsService
    def searchableService

    def init =
    {
        servletContext ->

        // // Eager initialize GORM Domain Mixin Methods.
        // // See: http://grails.1312388.n4.nabble.com/GORM-dynamic-save-method-intermittently-not-found-td3859120.html
        // // Without this, can get a groovy.lang.MissingMethodException when load testing concurrent detection uploads.
        // grailsApplication.domainClasses.each
        // {
        //     dc ->

        //     dc.clazz.count()
        // }

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

        Tag.registerObjectMarshaller()

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

        XML.registerObjectMarshaller ReceiverDownloadFile.class, {
            rxrDownloadFile, xml ->

            xml.attribute 'id', String.valueOf(rxrDownloadFile.id)
            xml.attribute 'type', String.valueOf(rxrDownloadFile.type)
            xml.attribute 'name', String.valueOf(rxrDownloadFile.name)
            xml.attribute 'importDate', String.valueOf(rxrDownloadFile.importDate)
            xml.attribute 'status', String.valueOf(rxrDownloadFile.status)
            xml.attribute 'errMsg', String.valueOf(rxrDownloadFile.errMsg)
            xml.attribute 'requestingUser', String.valueOf(rxrDownloadFile.requestingUser)
            xml.attribute 'percentComplete', String.valueOf(rxrDownloadFile.progress?.percentComplete)
            xml.attribute 'link', String.valueOf(
                new ApplicationTagLib().createLink(action: 'show', id: rxrDownloadFile.id, absolute:true))
        }

        environments
        {
            test
            {
                new TestDataInitialiser(permissionUtilsService).execute()
                new ProtectedSpeciesTestDataInitialiser(permissionUtilsService).execute()
            }

            development
            {
                if (Boolean.getBoolean('initialiseWithData'))
                {
                    new DevelopmentDataInitialiser(permissionUtilsService).execute()
                }
            }

            performance
            {
                new PerformanceDataInitialiser(permissionUtilsService).execute()
            }
        }

        // Required for following metaclass override to "stick".
        ValidDetection.count()

        // Performance optimisation (select count(*) is slow on large tables).
        ValidDetection.metaClass.static.count =
        {
            return Statistics.getStatistic('numValidDetections')
        }
    }
}
