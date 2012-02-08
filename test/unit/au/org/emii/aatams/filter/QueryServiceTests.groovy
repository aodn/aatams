package au.org.emii.aatams.filter

import grails.test.*

class QueryServiceTests extends GrailsUnitTestCase 
{
	def queryService
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(QueryService, true)
		queryService = new QueryService()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testTransformParamsWithoutCriteria()
	{
		def params = [filter: [eq: ["curtainPosition", 1]], sort: "name", order: "asc"]
		def expectedParams = [filter: [eq: ["curtainPosition", 1], order: ["name", "asc"]]]
		
		assertEquals(expectedParams, queryService.transformParams(params))
	}

	void testTransformParamsAssociationWithoutCriteria()
	{
		def params = [filter: [eq: ["curtainPosition", 1]], sort: "installation.name", order: "asc"]
		def expectedParams = 
		[
			filter: 
			[
				eq: ["curtainPosition", 1], 
				installation: 
				[
					order: ["name", "asc"]
				]
			]
		]
		
		assertEquals(expectedParams, queryService.transformParams(params))
	}

    void testTransformParamsWithCriteria() 
	{
		def params =
		[
			sort: "station.installation.project.name", 
			order: "asc",
			filter:
			[
				"station.installation.project.eq": ["name", "Seal Count"],
				station:
				[
					"installation.project.eq": ["name", "Seal Count"],
					installation:
					[
						"project.eq": ["name", "Seal Count"],
						project:
						[
							"eq":["name", "Seal Count"]
						],
					 	"eq": ""
					], 
				    "installation.name.eq": ""
				], 
				"station.installation.name.eq": ""
			]
		]

	def expectedTransformedParams = 
		[
			filter:
			[
				"station.installation.project.eq": ["name", "Seal Count"],
				station:
				[
					"installation.project.eq": ["name", "Seal Count"],
					installation:
					[
						"project.eq": ["name", "Seal Count"],
						project:
						[
							"eq":["name", "Seal Count"],
							"order": ["name", "asc"]
						],
					 	"eq": ""
					], 
				    "installation.name.eq": ""
				], 
				"station.installation.name.eq": ""
			]
		]
		
		assertEquals(expectedTransformedParams, queryService.transformParams(params))
	}
}
