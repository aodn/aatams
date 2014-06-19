package au.org.emii.aatams.export

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils

import au.org.emii.aatams.filter.QueryService;

class PagedBeanDataSource extends JRAbstractBeanDataSource
{
    private int index = 0
    private Object currBean
    private static Map fieldNameMap = new HashMap()
    
    private QueryService queryService
    private Class clazz
    private Map filterParams
    
    private List resultPage;
    private int pageStart = Integer.MAX_VALUE;
    private int pageEnd = Integer.MIN_VALUE;
    private static final int PAGE_SIZE = 500
    
    public PagedBeanDataSource(QueryService queryService, Class clazz, Map filterParams)
    {
        super(true)
        
        this.queryService = queryService
        this.clazz = clazz
        this.filterParams = filterParams
    }
    
    @Override
    public void moveFirst() throws JRException 
    {
        index = 0
        currBean = getObject(index)
    }

    @Override
    public boolean next() throws JRException 
    {
        currBean = getObject(index++);
        return (currBean != null);
    }
    
    @Override
    public Object getFieldValue(JRField field) throws JRException 
    {
        String nameField = getFieldName(field.getName())
        try
        {
            return PropertyUtils.getProperty(currBean, nameField)
        }
        catch (NestedNullException e)
        {
            return null
        }
    }

    public List getObjects(int firstResult,    int maxResults) 
    {
        filterParams.offset = firstResult
        filterParams.max = maxResults
        
        List queryResults = query(clazz, filterParams)
        
        if (resultPage == null) 
        {
            resultPage = new ArrayList(queryResults.size());
        }
        resultPage.clear();
        for (int i = 0; i < queryResults.size(); i++) 
        {
            resultPage.add(queryResults.get(i));
        }
        pageStart = firstResult;
        pageEnd = firstResult + queryResults.size() - 1;
        
        return resultPage;
    }

    private List query(clazz, filterParams)
    {
        return queryService.query(clazz, filterParams).results
    }
    
    public final Object getObject(int index) 
    {
        if (   (resultPage == null)
            || (index < pageStart)
            || (index > pageEnd)) 
        {
            resultPage = getObjects(index, PAGE_SIZE);
        }
        
        Object result = null;
        int pos = index - pageStart;
        if (   (resultPage != null)
            && (resultPage.size() > pos)) 
        {
            result = resultPage.get(pos);
        }
        
        return result;
    }

   /**
    * Replace the character "_" by a ".".
    *
    * @param fieldName the name of the field
    * @return the value in the cache or make
    * the replacement and return this value
    */
    private String getFieldName(String fieldName) 
    {
        String filteredFieldName = (String) fieldNameMap.get(fieldName);
        
        if (filteredFieldName == null) 
        {
            filteredFieldName = fieldName.replace('_','.');
            fieldNameMap.put(fieldName,filteredFieldName);
        }
        return filteredFieldName;
    }
}
