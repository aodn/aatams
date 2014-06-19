import org.apache.shiro.authc.LogoutAware;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor

import pages.*

abstract class GrailsCrudTest extends TestBase 
{
    abstract protected def getListPage()
    abstract protected def getShowPage()
    abstract protected def getCreatePage()
    abstract protected def getEditPage()
    
    @Test
    abstract void testList()
    
    protected void doTestList(numRows, stringValues, linkValues, listValues)
    {
        to getListPage()
        
        assert detailRows.size() == numRows
        
        def testRow =  findRowByName(detailRows, stringValues.name)
        
        stringValues.each
        {
            k, v ->
            assert testRow[k] == v
        }
        
        linkValues.each
        {
            k, v ->
            assert testRow[k].text() == v
        }
        
        listValues.each
        {
            k,v ->
            assert testRow[k].size() == v.size()
            assert testRow[k].containsAll(v)
        }
        
//        assert !newButton
//        
//        loginAsSysAdmin()
//        to toPage
//        assert newButton
    }

    @Test
    abstract void testShow()

    protected void doTestShow(detailRowName, details)
    {
        to getListPage()

        def detailRow =  findRowByName(detailRows, detailRowName)
        detailRow.showLink.click()
        
        assertShowPageDetails(details)
    }

    protected void assertShowPageDetails(details)
    {
        assert at(getShowPage())
        assertDetails(details)
    }

    protected void assertEditPageDetails(details)
    {
        assert at(getEditPage())
        assertDetails(details)
    }

    private assertDetails(details) 
    {
        details.each
        {
            k, v ->

            if (v instanceof List)
            {
                assert this[k].size() == v.size()
                assert this[k].containsAll(v)
            }
            else
            {
                assert this[k] == v
            }
        }
    }
    
    @Test
    abstract void testCreate()

    protected void doTestCreate(values, showValues)
    {
        loginAsSysAdmin()
        to getListPage()
        newButton.click()
        
        assert at(getCreatePage())
        
        values.each
        {
            k, v ->
            
            this[k].value(v)
        }

        doCustomCreateEntry()

        // Workaround for: http://code.google.com/p/selenium/issues/detail?id=2700
        JavascriptExecutor js = (JavascriptExecutor) driver
        js.executeScript( "document.getElementById('create').click();" );
//        createButton.click()
        
        try
        {
            assertShowPageDetails(showValues)
        }
        finally
        {
            // Clean up.
            withConfirm { deleteButton.click() }
        }
    }
    
    protected void doCustomCreateEntry()
    {
        
    }
    
    @Test
    abstract void testEdit()
    
    protected void doTestEdit(detailRowName, value, fieldToEdit)
    {
        loginAsSysAdmin()
        to getListPage()
        def detailRow = findRowByName(detailRows, detailRowName)
        detailRow.showLink.click()

        String currValue = this[value]
        String newValue = "different name"
        
        try
        {
            assertFieldUpdate(newValue, value, fieldToEdit)
        }
        finally
        {
            // Cleanup.
            assertFieldUpdate(currValue, value, fieldToEdit)
        }
    }
    
    protected void doTestEdit(detailRowName)
    {
        doTestEdit(detailRowName, "name", "nameTextField")
    }
    
    @Test
    void testDelete()
    {
        // Implementations will generally test deletion as part of "testCreate()".
    }

    protected def findRowByName(rows, name) 
    {
        rows.find
        {
            it.name == name
        }
    }

    protected void assertFieldUpdate(newValue, value, fieldToEdit) 
    {
        assert at(getShowPage())
        editButton.click()
        assert at(getEditPage())
        
        this[fieldToEdit].value(newValue)

        // Workaround for: http://code.google.com/p/selenium/issues/detail?id=2700
        JavascriptExecutor js = (JavascriptExecutor) driver
        js.executeScript( "document.getElementsByName('_action_update')[0].click();" );
//        updateButton.click()

        assert at(getShowPage())
//        assert name == newValue
        assert this[value] == newValue
    }
    
    protected void navigateToEditPageFromShowPage() 
    {
        assert at(getShowPage())
        editButton.click()
        assert at(getEditPage())
    }
}
