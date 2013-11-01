package au.org.emii.aatams

import au.org.emii.aatams.detection.ValidDetection
import au.org.emii.aatams.test.AbstractFiltersUnitTestCase

import grails.test.*
import org.apache.shiro.SecurityUtils
import org.apache.shiro.subject.Subject
import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig


class ExportVisibilityFiltersTests extends FiltersUnitTestCase {

    private MetaClass originalSecurityUtilsMetaClass

    ReceiverDownloadFileController receiverDownloadFileController

    protected void setUp() {
        super.setUp()

        // Save SecurityUtils' current meta class and clear it
        this.originalSecurityUtilsMetaClass = GroovySystem.metaClassRegistry.getMetaClass(SecurityUtils)
        GroovySystem.metaClassRegistry.removeMetaClass(SecurityUtils)

        mockController(ReceiverDownloadFileController)

        receiverDownloadFileController = new ReceiverDownloadFileController ()
    }

    protected void tearDown() {

        // Restore SecurityUtils' current meta class
        GroovySystem.metaClassRegistry.setMetaClass(SecurityUtils, this.originalSecurityUtilsMetaClass)
        super.tearDown()
    }

    void test1() {

        //    Scenario: receiver export list view as unathenticated user
        //    Given I am not logged in
        //    And someone else has uploaded a receiver export
        //    Then I should not be able to see any receiver exports

        SecurityUtils.metaClass.static.getSubject = {[
            getPrincipal: { "alice" },
            isAuthenticated: { false },
            hasRole: { true },
            isPermitted: { true  },
        ] as Subject }

        Person user = new Person(username: 'bob', id : 123)
        mockDomain(Person, [user])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:user)
        ReceiverDownloadFile file2 = new ReceiverDownloadFile(requestingUser:user)
        mockDomain(ReceiverDownloadFile, [file1, file2])

        receiverDownloadFileController.params.max = "100"
        def model = receiverDownloadFileController.list()
        assertNotNull(model)
        assertEquals(2, model.receiverDownloadFileInstanceList.size())

        controllerName = 'receiverDownloadFile'
        actionName = 'list'
        FilterConfig filter = initFilter("receiverDownloadFileList")
        assertNotNull(filter)

        ExportVisibilityFilters.metaClass.getTargetUri = { it }

        filter.after(model)
        assertEquals("auth", redirectArgs.controller)
        assertEquals("login", redirectArgs.action)
        assertEquals("receiverDownloadFile", redirectArgs.params.targetUri.controllerName)
        assertEquals("list", redirectArgs.params.targetUri.actionName)
    }

    void test2() {

        //    Scenario: receiver export list view as normal user
        //    Given I am logged in as a normal user
        //    And I have uploaded a receiver export
        //    And someone else has uploaded a receiver export
        //    Then the receiver export list view should show only my receiver export

        SecurityUtils.metaClass.static.getSubject = {[
            getPrincipal: { "alice" },
            isAuthenticated: { true },
            hasRole: { it != "SysAdmin" },
            isPermitted: { true  },
        ] as Subject }

        Person alice = new Person(username: 'alice', id : 123)
        Person bob = new Person(username: 'bob', id : 124)
        mockDomain(Person, [alice, bob])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:alice)
        ReceiverDownloadFile file2 = new ReceiverDownloadFile(requestingUser:bob)
        mockDomain(ReceiverDownloadFile, [file1, file2])

        receiverDownloadFileController.params.max = "100"
        def model = receiverDownloadFileController.list()
        assertNotNull(model)
        assertEquals(2, model.receiverDownloadFileInstanceList.size())

        controllerName = 'receiverDownloadFile'
        actionName = 'list'
        FilterConfig filter = initFilter("receiverDownloadFileList")
        assertNotNull(filter)

        filter.after(model)
        assertEquals(1, model.receiverDownloadFileInstanceList.size())
        assertEquals(alice.id, model.receiverDownloadFileInstanceList[0].requestingUser.id)
    }

    void test3() {

        //    Scenario: receiver export list view as sys admin
        //    Given I am logged in as a sys admin
        //    And someone else has uploaded a receiver export
        //    Then the receiver export list view should show the other person's export

        SecurityUtils.metaClass.static.getSubject = {[
            getPrincipal: { "alice" },
            isAuthenticated: { true },
            hasRole: { it == "SysAdmin" },
            isPermitted: { true  },
        ] as Subject }

        Person user = new Person(username: 'bob', id : 123)
        mockDomain(Person, [user])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:user)
        mockDomain(ReceiverDownloadFile, [file1])

        receiverDownloadFileController.params.max = "100"
        def model = receiverDownloadFileController.list()
        assertNotNull(model)

        controllerName = 'receiverDownloadFile'
        actionName = 'list'
        FilterConfig filter = initFilter("receiverDownloadFileList")
        assertNotNull(filter)

        filter.after(model)
        assertEquals(1, model.receiverDownloadFileInstanceList.size())
    }

    void test4() {

        //    Scenario: receiver export show view of own export as normal user
        //    Given I am logged in as a normal user
        //    And I have uploaded a receiver export
        //    Then I should be able to navigate to the show view of that receiver export
        //    And I should be able to download the associated export file from the show view

        SecurityUtils.metaClass.static.getSubject = {[
            getPrincipal: { "alice" },
            isAuthenticated: { true },
            hasRole: { it != "SysAdmin" },
            isPermitted: { true  },
        ] as Subject }

        Person user = new Person(username: 'alice', id : 123)
        mockDomain(Person, [user])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:user)
        mockDomain(ReceiverDownloadFile, [file1])

        receiverDownloadFileController.params.id = file1.id
        def model = receiverDownloadFileController.show()
        assertNotNull(model)

        controllerName = 'receiverDownloadFile'
        actionName = 'show'
        FilterConfig filter = initFilter("receiverDownloadFileShow")
        assertNotNull(filter)

        filter.after(model)
        assertNotNull(model)
        assertNotNull(model.receiverDownloadFileInstance)
        assertEquals(file1.id, model.receiverDownloadFileInstance.id)
    }

    void test5() {

        //    Scenario: receiver export show view of someone else's export as sys admin
        //    Given I am logged in as a sys admin
        //    And someone else has uploaded a receiver export
        //    Then I should be able to navigate to the show view of that receiver export
        //    And I should be able to download the associated export file from the show view

        SecurityUtils.metaClass.static.getSubject = {[
            getPrincipal: { "alice" },
            isAuthenticated: { true },
            hasRole: { it == "SysAdmin" },
            isPermitted: { true  },
        ] as Subject }

        Person user = new Person(username: 'bob', id : 123)
        mockDomain(Person, [user])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:user)
        mockDomain(ReceiverDownloadFile, [file1])

        receiverDownloadFileController.params.id = file1.id
        def model = receiverDownloadFileController.show()
        assertNotNull(model)

        controllerName = 'receiverDownloadFile'
        actionName = 'show'
        FilterConfig filter = initFilter("receiverDownloadFileShow")
        assertNotNull(filter)

        filter.after(model)
        assertNotNull(model)
        assertNotNull(model.receiverDownloadFileInstance)
        assertEquals(file1.id, model.receiverDownloadFileInstance.id)
    }

    void test6() {

        //    Scenario: direct navigation to receiver export show view as unauthenticated user
        //    Given I am not logged in
        //    And I have uploaded a receiver export
        //    And I attempt to go directly to the show view of my receiver export
        //    Then I should be redirected to the sign in screen
        //    And subsequently be redirected to the show view

        SecurityUtils.metaClass.static.getSubject = {[
            getPrincipal: { "alice" },
            isAuthenticated: { false },
            hasRole: { it != "SysAdmin" },
            isPermitted: { true },
        ] as Subject }

        Person user = new Person(username: 'alice', id : 123)
        mockDomain(Person, [user])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:user)
        mockDomain(ReceiverDownloadFile, [file1])

        receiverDownloadFileController.params.id = file1.id
        def model = receiverDownloadFileController.show()
        assertNotNull(model)

        controllerName = 'receiverDownloadFile'
        actionName = 'show'
        FilterConfig filter = initFilter("receiverDownloadFileShow")
        assertNotNull(filter)

        ExportVisibilityFilters.metaClass.getTargetUri = { it }

        filter.after(model)
        assertEquals("auth", redirectArgs.controller)
        assertEquals("login", redirectArgs.action)
        assertEquals("receiverDownloadFile", redirectArgs.params.targetUri.controllerName)
        assertEquals("show", redirectArgs.params.targetUri.actionName)
    }

    void test7() {

        //    Scenario: direct navigation to receiver export show view of someone else's export
        //    Given I am logged in as a normal user
        //    And someone else has uploaded a receiver export
        //    And I attempt to go directly to the show view of that receiver export
        //    Then I should see 'not authorized' in the browser \
        //    # I'm not sure if these are the exact words - there's already a precedent though.

        SecurityUtils.metaClass.static.getSubject = {[
            getPrincipal: { "alice" },
            isAuthenticated: { true },
            hasRole: { it != "SysAdmin" },
            isPermitted: { true },
        ] as Subject }

        Person alice = new Person(username: 'alice', id : 123)
        Person bob = new Person(username: 'bob', id : 124)
        mockDomain(Person, [alice, bob])

        Person.metaClass.'static'.findByUsername = {
            name, args ->
                if(name == alice.username) return alice
                else if(name == bob.username) return bob
                return null
        }

        ReceiverDownloadFile bobsFile = new ReceiverDownloadFile(requestingUser:bob)
        mockDomain(ReceiverDownloadFile, [bobsFile])

        receiverDownloadFileController.params.id = bobsFile.id
        def model = receiverDownloadFileController.show()
        assertNotNull(model)

        controllerName = 'receiverDownloadFile'
        actionName = 'show'
        FilterConfig filter = initFilter("receiverDownloadFileShow")
        assertNotNull(filter)

        filter.after(model)
        assertEquals("auth", redirectArgs.controller)
        assertEquals("unauthorized", redirectArgs.action)
    }

    void test8() {

        //    Scenario: direct navigation to receiver export show view of someone else's export as sysadmin
        //    Given I am logged in as a sys admin
        //    And someone else has uploaded a receiver export
        //    And I go directly to the show view of that receiver export
        //    Then I should be able to see it

        SecurityUtils.metaClass.static.getSubject = {[
                getPrincipal: { "alice" },
                isAuthenticated: { true },
                hasRole: { it == "SysAdmin" },
                isPermitted: { true },
        ] as Subject }

        Person alice = new Person(username: 'alice', id : 123)
        Person bob = new Person(username: 'bob', id : 124)
        mockDomain(Person, [alice, bob])

        ReceiverDownloadFile bobsFile = new ReceiverDownloadFile(requestingUser:bob)
        mockDomain(ReceiverDownloadFile, [bobsFile])

        receiverDownloadFileController.params.id = bobsFile.id
        def model = receiverDownloadFileController.show()
        assertNotNull(model)

        controllerName = 'receiverDownloadFile'
        actionName = 'show'
        FilterConfig filter = initFilter("receiverDownloadFileShow")
        assertNotNull(filter)

        filter.after(model)

        assertNotNull(model)
        assertNotNull(model.receiverDownloadFileInstance)
        assertEquals(bobsFile.id, model.receiverDownloadFileInstance.id)
    }
}
