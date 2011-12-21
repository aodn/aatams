package au.org.emii.aatams;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar 
{
    public void registerCustomEditors(PropertyEditorRegistry registry)
    {
        registry.registerCustomEditor(com.vividsolutions.jts.geom.Point.class, 
                                      new PointEditor());
        
//        registry.registerCustomEditor(org.joda.time.DateTime.class,
//                                      new DateTimeEditor());
    }
}
