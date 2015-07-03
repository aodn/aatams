package au.org.emii.aatams

import grails.test.*

class FilterTests extends GroovyTestCase  {
    def filterInterceptor
    def grailsApplication
    def grailsWebRequest
     
    def request(Map params, controllerName, actionName)  {
        grailsWebRequest = GrailsWebUtil.bindMockWebRequest(grailsApplication.mainContext)
        grailsWebRequest.params.putAll(params)
        grailsWebRequest.controllerName = controllerName
        grailsWebRequest.actionName = actionName
        filterInterceptor.preHandle(grailsWebRequest.request, grailsWebRequest.response, null)
    }
     
    def getResponse()  {
        grailsWebRequest.currentResponse
    }
     
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    def testEmpty() {
        
    }
//    def testFilterRedirects() 
//    {
//        def result = request("home", "index", someParameter: "2")
//        assertFalse result
//        assertTrue response.redirectedUrl.endsWith(/* something */)
//    }
}
