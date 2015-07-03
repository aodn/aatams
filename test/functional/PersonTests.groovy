import org.junit.Test;

import pages.*

class PersonTests extends GrailsCrudTest  {
    def listPage = PersonListPage
    def showPage = PersonShowPage
    def createPage = PersonCreatePage
    def editPage = PersonEditPage
    
    @Test
    void testList() {
        doTestList(3,
                   [name:"Joe Bloggs"],
                   [organisationLink:"CSIRO (CMAR)"],
                   [projects: ["Seal Count", "Tuna"]])
    }
    
    @Test
    void testShow() {
        doTestShow("Joe Bloggs",
                    [name:"Joe Bloggs",
                     organisation:"CSIRO (CMAR)",
                     projects:["Seal Count", "Tuna"],
                     defaultTimeZone: "Australia/Perth"])
    }

    @Test
    void testCreate() {
        def personName = "Ricky Ponting"
        def theusername = "rponting"
        def thephoneNumber = "1234 4321"
        def theemailAddress = "rponting@acb.org.au"
        def thetimezone = "Australia/Hobart"

        doTestCreate(
            [nameTextField:personName,
              usernameTextField:theusername,
             passwordTextField:"batsman",
             passwordConfirmTextField:"batsman",
             organisationSelect:"5",    // CSIRO
             phoneNumberTextField:thephoneNumber,
             emailAddressTextField:theemailAddress,
             defaultTimeZoneSelect:thetimezone],
             [name: personName, organisation: "CSIRO (CMAR)", defaultTimeZone: "Australia/Hobart", username: theusername])
    }

    @Test
    void testEdit() {
        doTestEdit("Joe Bloggs")

        navigateToEditPageFromShowPage()
        assertPasswordChange()
    }
    
    private void assertPasswordChange() {
        changePassword("new password")
        changePassword("password")
    }
    
    private void changePassword(password) {
        assert at(PersonEditPage)
        changePasswordLink.click()
        
        assert changePasswordDialog.personLabel.text() == "Joe Bloggs"
        changePasswordDialog.passwordTextField.value(password)
        changePasswordDialog.passwordConfirmTextField.value(password)
        
        changePasswordDialog.updateButton.click()
    }
}
