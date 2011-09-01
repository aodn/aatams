package au.org.emii.aatams

import grails.test.*

import org.apache.log4j.*

class EmbargoExpirationJobTests extends GrailsUnitTestCase 
{
    protected def job
    
    protected void setUp() 
    {
        super.setUp()
        
        mockConfig("animalRelease { embargoExpiration {warningPeriodMonths = 1}}")

        mockLogging(EmbargoExpirationJob, true)
        job = new EmbargoExpirationJob()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testFindEmbargoedReleasesToday()
    {
        Calendar yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        Calendar tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)

        Tag tag = new Tag(codeMap:'A69-1303', pingCode:1234)
        mockDomain(Tag, [tag])
        tag.save()
        
        AnimalRelease nullRelease = new AnimalRelease()
        AnimalRelease yesterdayRelease = new AnimalRelease(embargoDate: yesterday.getTime())
        AnimalRelease todayRelease = new AnimalRelease(embargoDate: new Date())
        AnimalRelease tomorrowRelease = new AnimalRelease(embargoDate: tomorrow.getTime())
        
        def releases = [nullRelease, yesterdayRelease, todayRelease, tomorrowRelease]
        mockDomain(AnimalRelease, releases)
        
        nullRelease.addToSurgeries(new Surgery(release:nullRelease, tag:tag))
        yesterdayRelease.addToSurgeries(new Surgery(release:yesterdayRelease, tag:tag))
        todayRelease.addToSurgeries(new Surgery(release:todayRelease, tag:tag))
        tomorrowRelease.addToSurgeries(new Surgery(release:tomorrowRelease, tag:tag))
        
        releases.each{ it.save() }
        
        def releaseCriteria = [list: {Closure cls -> [yesterdayRelease, todayRelease, tomorrowRelease]}]
        AnimalRelease.metaClass.static.createCriteria = {releaseCriteria}
        
        def embargoedReleases = job.findEmbargoedReleases(0)
        assertEquals([todayRelease], embargoedReleases)
    }
}
