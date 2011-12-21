class UrlMappings 
{
    
    static mappings = 
    {
        "/$controller/$action?/$id?"
        {
            constraints 
            {
                // apply constraints here
            }
        }
        
        "/"(controller:"about", action:"home")
        
        "500"(view:'/error')
    }
}
