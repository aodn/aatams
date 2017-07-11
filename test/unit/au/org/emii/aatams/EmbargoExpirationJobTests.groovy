package au.org.emii.aatams

import grails.test.*
import org.apache.log4j.*

class EmbargoExpirationJobTests extends GrailsUnitTestCase  {
    protected def job

    AnimalRelease nullRelease
    AnimalRelease yesterdayRelease
    AnimalRelease todayRelease
    AnimalRelease tomorrowRelease

    protected void setUp()  {
        super.setUp()

        mockDomain(Person)

        mockLogging(EmbargoExpirationJob, true)
        job = new EmbargoExpirationJob()

        Calendar yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        Calendar tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_YEAR, 1)

        TransmitterType transmitterType = new TransmitterType(transmitterTypeName: "PINGER")
        mockDomain(TransmitterType, [transmitterType])

        CodeMap codeMap = new CodeMap(codeMap:'A69-1303')
        mockDomain(CodeMap, [codeMap])

        ValidCodeMapTransmitterType validCodeMapTransmitterType = new ValidCodeMapTransmitterType(codeMap: codeMap, transmitterType: transmitterType)
        mockDomain(ValidCodeMapTransmitterType, [validCodeMapTransmitterType])

        Tag tag = new Tag(codeMap:codeMap)
        Sensor sensor = new Sensor(pingCode: 1234, tag: tag)
        mockDomain(Sensor, [sensor])
        mockDomain(Tag, [tag])
        tag.addToSensors(sensor)

        tag.save()

        nullRelease = new AnimalRelease()
        yesterdayRelease = new AnimalRelease(embargoDate: yesterday.getTime())
        todayRelease = new AnimalRelease(embargoDate: new Date())
        tomorrowRelease = new AnimalRelease(embargoDate: tomorrow.getTime())

        def releases = [nullRelease, yesterdayRelease, todayRelease, tomorrowRelease]
        mockDomain(AnimalRelease, releases)

        nullRelease.addToSurgeries(new Surgery(release:nullRelease, tag:tag))
        yesterdayRelease.addToSurgeries(new Surgery(release:yesterdayRelease, tag:tag))
        todayRelease.addToSurgeries(new Surgery(release:todayRelease, tag:tag))
        tomorrowRelease.addToSurgeries(new Surgery(release:tomorrowRelease, tag:tag))

        releases.each{ it.save() }

        def releaseCriteria = [list: {Closure cls -> [yesterdayRelease, todayRelease, tomorrowRelease]}]
        AnimalRelease.metaClass.static.createCriteria = {releaseCriteria}

        mockConfig('''animalRelease  {
                            embargoExpiration  {
                                warningPeriodMonths = 1
                            }
                        }

                        grails  {
                            serverURL = "http://localhost:8090/aatams"
                        }''')
//        job.grailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
        job.metaClass.getServerUrl = { -> "http://localhost:8090/aatams" }
    }

    protected void tearDown()  {
        super.tearDown()
    }

    void testFindEmbargoedReleasesToday() {
        def embargoedReleases = job.findEmbargoedReleases(0)
        assertEquals([todayRelease], embargoedReleases)
    }

    void testExpiringSubject() {
        assertEquals("AATAMS tag embargoes expiring today: [A69-1303-1234]", job.createExpiringSubject(todayRelease))
    }

    void testExpiringBody() {
        String expectedBody = '''The embargoes on the following tags will expire today.

A69-1303-1234

http://localhost:8090/aatams/animalRelease/show/''' + todayRelease.id

        assertEquals(expectedBody, job.createExpiringBody(todayRelease))
    }
}
