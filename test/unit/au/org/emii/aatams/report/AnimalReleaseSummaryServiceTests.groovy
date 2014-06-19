package au.org.emii.aatams.report

import au.org.emii.aatams.*
import grails.test.*
import org.joda.time.*

import java.util.Calendar
import java.util.Date

class AnimalReleaseSummaryServiceTests extends GrailsUnitTestCase 
{
    def service
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(AnimalReleaseSummaryService, true)
        service = new AnimalReleaseSummaryService()

        Species whiteShark = new Species(name:"white shark")
        Species flathead = new Species(name:"flathead")
        Species stingray = new Species(name:"stingray")
        Species pinkLing = new Species(name:"pink ling")
        def speciesList = [whiteShark, flathead, stingray, pinkLing]
        mockDomain(Species, speciesList)
        speciesList.each { it.save() }
        
        Animal whiteShark1 = new Animal(species:whiteShark)
        Animal flathead1 = new Animal(species:flathead)
        Animal stingray1 = new Animal(species:stingray)
        Animal pinkLing1 = new Animal(species:pinkLing)
        def animalList = [whiteShark1, flathead1, stingray1, pinkLing1]
        mockDomain(Animal, animalList)
        animalList.each { it.save() }
        
        AnimalRelease whiteSharkCurrent1 = new AnimalRelease(animal:whiteShark1, status:AnimalReleaseStatus.CURRENT, releaseDateTime:now())
        AnimalRelease whiteSharkCurrent2 = new AnimalRelease(animal:whiteShark1, status:AnimalReleaseStatus.CURRENT, embargoDate:tomorrow(), releaseDateTime:now())
        AnimalRelease whiteSharkCurrent3 = new AnimalRelease(animal:whiteShark1, status:AnimalReleaseStatus.CURRENT, embargoDate:tomorrow(), releaseDateTime:now())
        AnimalRelease whiteSharkFinished1 = new AnimalRelease(animal:whiteShark1, status:AnimalReleaseStatus.FINISHED, releaseDateTime:fiftyDaysAgo())
        AnimalRelease whiteSharkFinished2 = new AnimalRelease(animal:whiteShark1, status:AnimalReleaseStatus.FINISHED, releaseDateTime:fiftyDaysAgo())
        AnimalRelease flatheadCurrent1 = new AnimalRelease(animal:flathead1, status:AnimalReleaseStatus.CURRENT, embargoDate:tomorrow(), releaseDateTime:lastYear())
        AnimalRelease flatheadCurrent2 = new AnimalRelease(animal:flathead1, status:AnimalReleaseStatus.CURRENT, embargoDate:tomorrow(), releaseDateTime:lastYear())
        AnimalRelease stingrayFinished1 = new AnimalRelease(animal:stingray1, status:AnimalReleaseStatus.FINISHED, releaseDateTime:lastYear())
        
        def releaseList = 
            [whiteSharkCurrent1, whiteSharkCurrent2, whiteSharkCurrent3,
             whiteSharkFinished1, whiteSharkFinished2, flatheadCurrent1,
             flatheadCurrent2, stingrayFinished1]
        mockDomain(AnimalRelease, releaseList)
        releaseList.each { it.save() }
    }

    private Date tomorrow()
    {
        Calendar cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        return cal.getTime()
    }
    
    private DateTime now()
    {
        return new DateTime()
    }

    private DateTime fiftyDaysAgo()
    {
        return now().minusDays(50)
    }
    
    private DateTime lastYear()
    {
        return now().minusYears(1)
    }
    
    protected void tearDown() 
    {
        super.tearDown()
    }

    void testCountBySpecies()
    {
        def result = service.countBySpecies()

        assertEquals(3, result.size())
        
        AnimalReleaseCount count0 = result[0]
        assertEquals("white shark", count0.species.name)
        assertEquals(3, count0.currentReleases)
        assertEquals(5, count0.totalReleases)
        println(count0.percentEmbargoed)
        assertTrue(count0.percentEmbargoed - 40.0f < 0.01)
        
        AnimalReleaseCount count1 = result[1]
        assertEquals("flathead", count1.species.name)
        assertEquals(2, count1.currentReleases)
        assertEquals(2, count1.totalReleases)
        assertTrue(count1.percentEmbargoed - 100.0f < 0.01)
        
        AnimalReleaseCount count2 = result[2]
        assertEquals("stingray", count2.species.name)
        assertEquals(0, count2.currentReleases)
        assertEquals(1, count2.totalReleases)
        assertTrue(count2.percentEmbargoed - 0 < 0.01)
    }
    
    void testSummary()
    {
        def result = service.summary()
        
        assertEquals(50, result["% embargoed"])
        assertEquals(3, result["last 30 days"])
        // Temp commented out - need to fix dates etc.
//        assertEquals(5, result["this year"])    // This will be broken between 1st Jan and 19th Feb :-)
        assertEquals(8, result["total"])
    }
}
