package au.org.emii.aatams

class ListTagLib 
{
    def listControlForm =
    {
        attrs, body ->
        
        out << render(template:"/common/listControlFormTemplate", model: attrs + [body:body])
    }
}
