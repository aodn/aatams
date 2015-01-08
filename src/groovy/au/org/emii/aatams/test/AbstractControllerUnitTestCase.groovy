package au.org.emii.aatams.test

import grails.test.ControllerUnitTestCase

import net.sf.jasperreports.engine.export.JRCsvExporter
import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import au.org.emii.aatams.EmbargoService
import au.org.emii.aatams.PermissionUtilsService;
import au.org.emii.aatams.Person;
import au.org.emii.aatams.export.ExportService
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.report.ReportInfoService

import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder

abstract class AbstractControllerUnitTestCase extends ControllerUnitTestCase
{
    protected hasRole = true
    protected user
    protected authenticated = true
    protected permitted = false

    private MetaClass originalSecurityUtilsMetaClass

    protected Map flashMsgParams

    protected void setUp()
    {
        super.setUp()

        hasRole = true
        authenticated = true
        permitted = false

        def subject = [ getPrincipal: { getPrincipal() },
                        isAuthenticated: { isAuthenticated() },
                        hasRole: { hasRole() },
                        isPermitted: { isPermitted(it) },
                      ] as Subject

        // Save SecurityUtils' current meta class and clear it from
        // the registry.
        def registry = GroovySystem.metaClassRegistry
        this.originalSecurityUtilsMetaClass = registry.getMetaClass(SecurityUtils)
        registry.removeMetaClass(SecurityUtils)

        SecurityUtils.metaClass.static.getSubject = { subject }

        controller.metaClass.message = { Map params -> flashMsgParams = params }

        try
        {
            mockLogging(QueryService, true)
            mockLogging(ReportInfoService, true)
            controller.queryService = new QueryService()

            mockLogging(EmbargoService, true)
            controller.queryService.embargoService = new EmbargoService()

            mockLogging(PermissionUtilsService, true)
            controller.queryService.embargoService.permissionUtilsService = new PermissionUtilsService()

            controller.reportInfoService = new ReportInfoService()
            controller.reportInfoService.permissionUtilsService = new PermissionUtilsService()
        }
        catch (MissingPropertyException e)
        {
            // Some controllers don't have these properties, just ignore.
        }

        controller.params.format = "PDF"

        ExportService.metaClass.getExporter =
        {
            params ->

            return new JRCsvExporter()
        }

        controller.metaClass.getMimeType =
        {
            params ->

            return "text/csv"
        }
    }

    protected void tearDown()
    {
        // Restore the old meta class on SecurityUtils.
        GroovySystem.metaClassRegistry.setMetaClass(SecurityUtils, this.originalSecurityUtilsMetaClass)

        super.tearDown()
    }

    protected Person getUser()
    {
        Person person = Person.findByUsername('jkburges')

        if (!person)
        {
            person = new Person(username:"jkburges", name: "Joe Bloggs")
        }

        return person
    }

    protected def getPrincipal()
    {
        return getUser()?.id
    }

    protected boolean isAuthenticated()
    {
        return authenticated
    }

    protected boolean hasRole()
    {
        return hasRole
    }

    protected boolean isPermitted(permission)
    {
        return permitted
    }

    protected void assertExport(filter, name) {
        controller.params.filter = filter

        controller.export()

        File actualReport = File.createTempFile(name + ".", ".csv")
        OutputStream out = new FileOutputStream(actualReport)
        out.write(controller.response.contentAsByteArray)
        out.close()

        println("Test output for test '" + name + "' written to: " + actualReport.getAbsolutePath())

        File expectedReport = new File(constructFilePath(name))

        assertContainsAllLines(removePageFooter(controller.response.contentAsString.trim()), removePageFooter(expectedReport.getText()))
    }

    protected String constructFilePath(expectedFileName)
    {
        String expectedFilePath = \
            System.getProperty("user.dir") + \
            "/test/integration/au/org/emii/aatams/report/resources/" + \
            expectedFileName + ".expected.csv"
        return expectedFilePath
    }

    protected void assertContainsAllLines(actual, expected)
    {
        if (!expected.readLines().containsAll(actual.readLines()) || !actual.readLines().containsAll(expected.readLines()) || !(expected.readLines().size() == actual.readLines().size()))
        {
            println "expected:\n" + expected
            println "\n\nactual:\n" + actual
        }
        assertTrue(expected.readLines().containsAll(actual.readLines()))
        assertTrue(actual.readLines().containsAll(expected.readLines()))
        assertEquals(expected.readLines().size(), actual.readLines().size())
    }

    protected String removePageFooter(String s)
    {
        def lineCount = 0
        s.eachLine { lineCount ++}

        def retString = ""
        int index = 0

        s.eachLine
        {
            if (it.contains("Page"))
            {
                // remove page footer
            }
            else
            {
                retString += it + '\n'
            }

            index++
        }

        return retString
    }

    static void createDetectionViews(dataSource) {
        println("createDetectionViews")

        def sql = new Sql(dataSource)

        [ 'detection_view', 'detection_by_species_view' ].each {
            viewName ->

            sql.execute(String.valueOf("create or replace view ${viewName} as ${ConfigurationHolder.config.detection.extract[viewName].definition}"))
        }
    }
}
