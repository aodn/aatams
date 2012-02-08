package au.org.emii.aatams.filter

import grails.test.*
import org.joda.time.DateTime
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*

class QueryServiceTests extends GrailsUnitTestCase 
{
	def queryService
	
    protected void setUp() 
	{
        super.setUp()
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
			[filter: [in: ["name", ["Bondi SW1", "Ningaloo S1"]]]])
	}

	void testBetweenRestriction()
	{
		assertQuery(ValidDetection,
					ValidDetection.findAllByTimestamp(new DateTime("2011-05-17T02:54:00+00:00").toDate()),
					[filter: [between: ["timestamp", new DateTime("2011-05-17T02:53:00+00:00").toDate(), new DateTime("2011-05-17T02:54:00+00:00").toDate()]]])
	}
	
/*
 	void testIsNullRestriction()
	{
		assertQuery(ReceiverDeployment,
					ReceiverDeployment.list().grep
					{
						it.recovery == null
					},
					[filter:[recovery: [isNull: null]]])
	}
*/
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
	
	void testRealQueryOneParameter()
	{
		def params = 
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
			
			
				"surgeries.detectionSurgeries.sensor.in":["codeName", ""],
				 
				surgeries:
				[
					"detectionSurgeries.sensor.in":["codeName", ""],
					
					detectionSurgeries:
					[
						sensor:
						[
							in:["codeName", ""]
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
/**
	void testRealQueryOneParameterWithSort()
	{
		def params =
			[sort: "station.installation.project.name", order: "asc",
			 filter:
			["station.installation.project.name.eq":"Seal Count",
			station:["installation.project.name.eq":"Seal Count",
				installation:["project.name.eq":"Seal Count",
					project:["name.eq":"Seal Count",
						name:[eq:"Seal Count"]],
					"name.eq":"", name:[eq:""]], "installation.name.eq":""], "station.installation.name.eq":""]]
//		def params =
//		[filter:
//		["station.installation.project.name.eq":"Seal Count",
//		station:["installation.project.name.eq":"Seal Count",
//			installation:["project.name.eq":"Seal Count",
//				project:["name.eq":"Seal Count",
//					name:[eq:"Seal Count"]],
//				"name.eq":"", name:[eq:""]], "installation.name.eq":""], "station.installation.name.eq":""]]

		assertQuery(ReceiverDeployment,
					ReceiverDeployment.createCriteria().list
					{
						station
						{
							installation
							{
								project
								{
									eq("name", "Seal Count")
									order("name")
								}
							}
						}
					},
					params)
	}

	void testRealQueryTwoParameters()
	{
		def params = 
			[filter:["station.installation.project.name.eq":"Seal Count", 
			station:["installation.project.name.eq":"Seal Count", 
				installation:["project.name.eq":"Seal Count", 
					project:["name.eq":"Seal Count", 
						name:[eq:"Seal Count"]], 
					"name.eq":"Bondi Line", name:[eq:"Bondi Line"]], "installation.name.eq":"Bondi Line"], "station.installation.name.eq":"Bondi Line"]]
			
		assertQuery(ReceiverDeployment,
					ReceiverDeployment.createCriteria().list
					{
						station
						{
							installation
							{
								project
								{
									eq("name", "Seal Count")
								}
								
								eq("name", "Bondi Line")
							}
						}
					},
					params)
	}
*/	
	private def assertQuery(clazz, expectedResults, queryParams)
	{
		def actualResults = new ArrayList(queryService.query(clazz, queryParams).results)
//		println "expected: " + expectedResults
//		println "actual: " + actualResults
		
		assertEquals(expectedResults, actualResults)
		
		return actualResults
	}
}
