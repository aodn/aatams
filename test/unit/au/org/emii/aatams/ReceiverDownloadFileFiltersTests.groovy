package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractFiltersUnitTestCase

import grails.test.*
import org.apache.shiro.SecurityUtils
import org.apache.shiro.subject.Subject
import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig


class ReceiverDownloadFileFiltersTests extends AbstractFiltersUnitTestCase {

    void testShowAsNormalUser() {
        FilterConfig filter = initFilter("receiverDownloadFileList")

        Person alice = new Person(username: 'alice', id : 123)
        Person bob = new Person(username: 'bob', id : 124)

        mockDomain(Person, [alice, bob])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:alice)
        ReceiverDownloadFile file2 = new ReceiverDownloadFile(requestingUser:bob)

        mockDomain(ReceiverDownloadFile, [file1, file2])

        def model =  [receiverDownloadFileInstanceList: ReceiverDownloadFile.findAll()]

        user = alice
        hasRole = false

        filter.after(model)

        assertEquals 1, model.receiverDownloadFileInstanceList.size()
        assertEquals alice.id, model.receiverDownloadFileInstanceList[0].requestingUser.id
    }

    void testShowAsSysAdmin() {
        FilterConfig filter = initFilter("receiverDownloadFileList")

        Person alice = new Person(username: 'alice', id : 123)
        Person bob = new Person(username: 'bob', id : 124)

        mockDomain(Person, [alice, bob])

        ReceiverDownloadFile file1 = new ReceiverDownloadFile(requestingUser:alice)
        ReceiverDownloadFile file2 = new ReceiverDownloadFile(requestingUser:bob)

        mockDomain(ReceiverDownloadFile, [file1, file2])

        def model =  [receiverDownloadFileInstanceList: ReceiverDownloadFile.findAll()]

        user = alice
        hasRole = true

        filter.after(model)

        assertEquals 2, model.receiverDownloadFileInstanceList.size()
        assertEquals alice.id, model.receiverDownloadFileInstanceList[0].requestingUser.id
        assertEquals bob.id, model.receiverDownloadFileInstanceList[1].requestingUser.id
    }

}
