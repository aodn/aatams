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
                station:
                [
                    installation:
                    [
                        project:
                        [
                            "eq":["name", "Seal Count"],
                            "order": ["name", "asc"]
                        ],
                    ], 
                ], 
            ]
        ]
        
        assertEquals(expectedTransformedParams, queryService.transformParams(params))
    }
    
    void testRemoveDottedKeyNames()
    {
        def params = [filter: ["station.eq": ["name", "Seal Count"], station: [eq: ["name", "Seal Count"]]]]
        def expectedParams = [filter: [station: [eq: ["name", "Seal Count"]]]]
        
        assertEquals(expectedParams, queryService.transformParams(params))
    }

    void testRemoveBlankValues()
    {
        def params = [filter: [station: [eq: ["name", "Bondi SW1"], installation: [eq: ["name", ""] as Object[]]]]]
        def expectedParams = [filter: [station: [eq: ["name", "Bondi SW1"]]]]
        
        assertEquals(expectedParams, queryService.transformParams(params))
    }

    void testRemoveNullValues()
    {
        def params = [filter: [station: [installation: [eq: ["name", null]]]], action:"list", controller:"installation", max:20]
        def expectedParams = [action:"list", controller:"installation", max:20, filter: [:]]
        
        assertEquals(expectedParams, queryService.transformParams(params))
    }

    void testIntegerValues()
    {
        def params = [filter: [station: [eq: ["curtainPosition", 1]]], max: 1]
        def expectedParams = [filter: [station: [eq: ["curtainPosition", 1]]], max: 1]
        
        assertEquals(expectedParams, queryService.transformParams(params))
    }
}
