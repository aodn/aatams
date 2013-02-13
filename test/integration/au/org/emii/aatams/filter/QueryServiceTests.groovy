package au.org.emii.aatams.filter

import grails.test.*
import org.joda.time.DateTime
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*
import au.org.emii.aatams.report.ReportInfoService
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

class QueryServiceTests extends AbstractGrailsUnitTestCase 
{
	def queryService
	
    protected void setUp() 
	{
        super.setUp()
		
		permitted = true
    }

    protected void tearDown()
	{
        super.tearDown()
    }

	// no restriction
	void testNoRestriction()
	{
		assertQuery(InstallationStation, InstallationStation.list(), null)
		assertQuery(InstallationStation, InstallationStation.list(), [:])
		
		assertQuery(
			Installation, 
			Installation.list(),
			["filter.project.eq":["name", ""] as Object[], filter:["project.eq":["name", ""] as Object[], project:[eq:["name", ""] as Object[]]], action:"list", controller:"installation"])
	}
	
	void testSortAndOrderWithoutFilter()
	{
		assertQuery(InstallationStation,
					InstallationStation.list(sort: "name", order: "asc"),
					[filter: [installation: [project: [eq: ["name", ""]]]],	sort: "name", order: "asc"])
	}

	void testOneEqualsRestriction()
	{
		assertQuery(InstallationStation, InstallationStation.findAllByName("Bondi SW1"), [filter:[eq: ["name", "Bondi SW1"]]])
		assertQuery(InstallationStation, InstallationStation.findAllByName("Bondi SW2"), [filter:[eq: ["name", "Bondi SW2"]]])
		
		assertQuery(InstallationStation,
			["Bondi SW1", "Ningaloo S1", "Heron S1", "Whale Station"].collect
			{
				InstallationStation.findByName(it)
			},
			[filter:[eq: ["curtainPosition", 1]]])
	}

// Not passing currently, but there is not filtering on two different properties from same association	 
//	void testTwoEqualsRestrictions()
//	{
//		assertQuery(InstallationStation, InstallationStation.findAllByName("Bondi SW1"), 
//					[filter:[eq: [["name", "Bondi SW1"], ["curtainPosition", 1]]]])
//	}
	
	void testOneInRestriction()
	{
		assertQuery(InstallationStation, 
			["Bondi SW1", "Ningaloo S1"].collect
			{
				InstallationStation.findByName(it)
			},
			[filter: [in: ["name", "Bondi SW1, Ningaloo S1"]]])
		
		assertQuery(
			InstallationStation,
			InstallationStation.createCriteria().list
			{
				installation
				{
					"in"("name", "Bondi Line", "Whale Curtain")
				}
			},
			[filter: [installation: ["in": ["name", "Bondi Line ,  Whale Curtain, "]]]])
	}

	void testBetweenRestriction()
	{
		assertQuery(ValidDetection,
					ValidDetection.findAllByTimestamp(new DateTime("2011-05-17T02:54:00+00:00").toDate()),
					[filter: [between: [aaa:"aaa", "0": "timestamp", "1": new DateTime("2011-05-17T02:53:00+00:00").toDate(), "2": new DateTime("2011-05-17T02:54:00+00:00").toDate()],
						      "between.0": "timestamp",
						      "between.1": new DateTime("2011-05-17T02:53:00+00:00").toDate(),
						      "between.2": new DateTime("2011-05-17T02:54:00+00:00").toDate()]])

		assertQuery(ValidDetection,
					ValidDetection.findAllByTimestamp(new DateTime("2011-05-17T02:54:00+00:00").toDate()),
					[
						filter: 
						[
							between: [aaa:"aaa", "0": "timestamp", "1": new DateTime("2011-05-17T02:53:00+00:00").toDate(), "2": new DateTime("2011-05-17T02:54:00+00:00").toDate()],
							"between.0": "timestamp",
							"between.1": new DateTime("2011-05-17T02:53:00+00:00").toDate(),
							"between.2": new DateTime("2011-05-17T02:54:00+00:00").toDate(),
							between_year: ["timestamp", 17, 17],
							between_month: ["timestamp", 17, 17],
							between_day: ["timestamp", 17, 17],
							between_hour: ["timestamp", 17, 17],
							between_minute: ["timestamp", 17, 17],
							between_second: ["timestamp", 17, 17]
						]
					])
		
		assertQuery(ValidDetection,
			ValidDetection.findAllByTimestamp(new DateTime("2011-05-17T02:54:00+00:00").toDate()),
			[
				"filter.between.1": new DateTime("2011-05-17T02:53:00+00:00").toDate(),
				"filter.between.2": new DateTime("2011-05-17T02:54:00+00:00").toDate(),
				filter: 
				[	
					between: [aaa:"aaa", "0": "timestamp", "1": new DateTime("2011-05-17T02:53:00+00:00").toDate(), "2": new DateTime("2011-05-17T02:54:00+00:00").toDate()],
					"between.0": "timestamp",
					"between.1": new DateTime("2011-05-17T02:53:00+00:00").toDate(),
					"between.2": new DateTime("2011-05-17T02:54:00+00:00").toDate()
				]
			])
		
		assertQuery(ValidDetection,
			ValidDetection.findAllByTimestamp(new DateTime("2011-05-17T02:54:00+00:00").toDate()),
			[
				//  Thu Jun 18 12:38:00 EST 2009
				"filter.between.1": "Tue May 17 12:53:00 EST 2011",
				"filter.between.2": "Tue May 17 12:54:00 EST 2011",
				filter:
				[
					between: [aaa:"aaa", "0": "timestamp", "1": "Tue May 17 12:53:00 EST 2011", "2": "Tue May 17 12:54:00 EST 2011"],
					"between.0": "timestamp",
					"between.1": "Tue May 17 12:53:00 EST 2011",
					"between.2": "Tue May 17 12:54:00 EST 2011"
				]
			])
	}

 	void testIsNullRestriction()
	{
		assertQuery(ReceiverDeployment,
					ReceiverDeployment.list().grep
					{
						it.recovery == null
					},
					[filter:[recovery: [isNull: true]]])
	}

	void testOneAssociation()
	{
		assertQuery(InstallationStation, 
			["Bondi SW1", "Bondi SW2", "Bondi SW3"].collect
			{
				InstallationStation.findByName(it)
			},
			[filter: [installation: [eq: ["name", "Bondi Line"]]]])
	}
	
	void testMultiLevelAssociation()
	{
		assertQuery(
			InstallationStation,
			InstallationStation.createCriteria().list
			{
				installation
				{
					project
					{
						eq("name", "Seal Count")
					}
				}
			},
			[filter: [installation: [project: [eq: ["name", "Seal Count"]]]]])
	}

	void testDottedKeyNamesIgnored()
	{
		def params = [filter: ["project.eq":["name", "Seal Count"], project:[eq:["name", "Seal Count"]]]]
			
		assertQuery(Installation,
					Installation.findAllByProject(Project.findByName("Seal Count")),
					params)
	}

	// Don't think this test is any longer valid.					
//	void testDottedRestrictionTypesIgnored()
//	{
//		assertQuery(
//			InstallationStation,
//			["Bondi SW1", "Bondi SW2", "Bondi SW3"].collect
//			{
//				InstallationStation.findByName(it)
//			},
//			[filter:[installation: [eq: ["name", "Bondi Line"], "name.eq": "Bondi Line"]]])
//	}

	void testSortWithEqualsRestriction()
	{
		assertQuery(
			InstallationStation,
			["Bondi SW1", "Heron S1", "Ningaloo S1", "Whale Station"].collect
			{
				InstallationStation.findByName(it)
			},
			[filter:[eq: ["curtainPosition", 1]], sort: "name", order: "asc"])
	}
	
 	void testSortWithRestrictionOnSameField()
	{
		assertQuery(
			InstallationStation,
			["Bondi SW1"].collect
			{
				InstallationStation.findByName(it)
			},
			[filter:[eq: ["name", "Bondi SW1"]], sort: "name", order: "asc"])

		assertQuery(
			InstallationStation,
			["Bondi SW1", "Bondi SW2", "Bondi SW3"].collect
			{
				InstallationStation.findByName(it)
			},
			[filter:[ilike: ["name", "Bondi SW%"]], sort: "name", order: "asc"])
		
		assertQuery(
			InstallationStation,
			["Bondi SW1", "Bondi SW2", "Bondi SW3"].reverse().collect
			{
				InstallationStation.findByName(it)
			},
			[filter:[ilike: ["name", "Bondi SW%"]], sort: "name", order: "desc"])
	}

	void testPagination()
	{
		int offset = 0
		["Bondi SW1", "Bondi SW2", "Bondi SW3"].each
		{
			stationName ->
			
			assertQuery(
				InstallationStation,
				InstallationStation.findAllByName(stationName),
				[filter:[ilike: ["name", "Bondi SW%"]], max: 1, offset: offset])
			
			offset++
		}
	}
	
// TODO: occasionally failing because of non-specified ordering of projects - need to fix.	
//	void testMemberProjectsRestriction()
//	{
//		def projects = Project.findAllByNameInList(["Seal Count", "Tuna"])
//		queryService.metaClass.getMemberProjects =
//		{
//			return projects
//		}
//		
//		assertQuery(Installation, 
//					Installation.findAllByProjectInList(projects), 
//					[filter: [project: [eq: ["name", ReportInfoService.MEMBER_PROJECTS]]]])
//		
//		queryService.metaClass.getMemberProjects =
//		{
//			return []
//		}
//		
//		assertQuery(Installation,
//					[],
//					[filter: [project: [eq: ["name", ReportInfoService.MEMBER_PROJECTS]]]])
//	}

	def realParams = 
	[
		codeName_textFieldId: "filter.in.codeName",
		"filter.project.eq": ["name", "Tuna"],
		filter:
		[
			"project.eq":["name", "Tuna"],
			project:
			[
				eq:["name", "Tuna"]
			],
		
		
			"surgeries.detectionSurgeries.sensor.in":["transmitterId", ""],
			 
			surgeries:
			[
				"detectionSurgeries.sensor.in":["transmitterId", ""],
				
				detectionSurgeries:
				[
					sensor:
					[
						in:["transmitterId", ""]
					]
				]
			],
		
			"animal.species.in":["spcode", ""],
			animal:
			[
				"species.in":["spcode", ""],
				species:
				[
					in:["spcode", ""]
				]
			]
		],
		spcode_textFieldId:"filter.in.spcode",
		"filter.surgeries.detectionSurgeries.sensor.in":["codeName", ""],
		spcode_lookupPath:"/species/lookupByNameAndReturnSpcode",
		codeName_lookupPath:"/sensor/lookupByTransmitterId",
		"filter.animal.species.in":["spcode", ""],
		action:"list",
		controller:"animalRelease"
	]
	
	void testRealQueryOneParameter()
	{
		def params = realParams
		
		assertQuery(AnimalRelease,
					AnimalRelease.createCriteria().list
					{
						project
						{
							eq("name", "Tuna")
						}
					},
					params)
	}
	
	void testRealQueryOneParameterWithSort()
	{
		def params = realParams + [sort: "project.name", order: "asc"]

		assertQuery(AnimalRelease,
					AnimalRelease.createCriteria().list
					{
						project
						{
							eq("name", "Tuna")
							order("name", "asc")
						}
					},
					params)
		
		params.order = "desc"
		assertQuery(AnimalRelease,
					AnimalRelease.createCriteria().list
					{
						project
						{
							eq("name", "Tuna")
							order("name", "desc")
						}
					},
					params)
		
	}

	void testRealQueryTwoParameters()
	{
		def params = realParams + [sort: "project.name", order: "asc"]
		params.filter += [animal:[sex:[eq: ["sex", "MALE"]]]]

		assertQuery(AnimalRelease,
					AnimalRelease.createCriteria().list
					{
						project
						{
							eq("name", "Tuna")
							order("name", "asc")
						}
						
						animal
						{
							sex
							{
								eq("sex", "MALE")
							}
						}
					},
					params)
	}

	void testDuplicateAssociationPath()
	{
		def params = 
		[
			sort:"recovery.recoverer.person.name", 
			order:"asc", 
			filter: [recovery: [isNull: true]]
		]
		
		assertQuery(ReceiverDeployment,
					ReceiverDeployment.list().grep
					{
						it.recovery == null
					},
					params)
	}

	private def assertQuery(clazz, expectedResults, queryParams)
	{
		def actualResults = new ArrayList(queryService.query(clazz, queryParams).results)
		println "expected: " + expectedResults
		println "actual: " + actualResults
		
		assertContainsAll(expectedResults, actualResults)
		
		return actualResults
	}

    private assertContainsAll(listA, listB)
    {
        assertEquals(listA.size(), listB.size())
        assertTrue(listA.containsAll(listB))
        assertTrue(listB.containsAll(listA))
    }
}
