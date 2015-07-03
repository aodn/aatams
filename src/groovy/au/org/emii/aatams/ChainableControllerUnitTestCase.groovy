package au.org.emii.aatams

import javax.servlet.ServletContext

import grails.test.*
import grails.util.GrailsNameUtils

/**
 * Test cases should subclass this if they want controller.chain() functionality
 * to be handled automatically.
 *
 * After chain is executed, "this.controller" will refer to the chained controller.
 *
 * @author jburgess
 */
class ChainableControllerUnitTestCase extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()

        // TODO: promote this to custom super class.
        controller.metaClass.servletContext =
            [ getRealPath: {System.getProperty("user.dir") + "/web-app" + it }] as ServletContext


        // This is where we provide our own implementation of chain that does
        // what we want it to do.
        controller.metaClass.chain = {
            it ->

            println("In chain impl")
//            controllerName, action, model, params ->
//
//            def logicalName = GrailsNameUtils.getLogicalName(controllerName, "Controller")
//            println("logicalName: " + logicalName)
//
//            mockController(logicalName)
//            def chainedController = logicalName.newInstance()
//            println("chainedController: " + chainedController)
//
////        jasperController.properties.put('chainModel', controller.chainArgs.model)
////        jasperController.metaClass.chainModel = controller.chainArgs.model
////
////        jasperController.params.putAll(controller.params)

        }

    }
}

