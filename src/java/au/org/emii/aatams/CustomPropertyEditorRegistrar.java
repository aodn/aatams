package au.org.emii.aatams;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar 
{
    public void registerCustomEditors(PropertyEditorRegistry registry)
    {
        registry.registerCustomEditor(com.vividsolutions.jts.geom.Point.class, 
                                      new PointEditor());
        
        registry.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }
}
