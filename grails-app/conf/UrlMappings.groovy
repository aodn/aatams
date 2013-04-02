class UrlMappings 
{
    
    static mappings = 
    {
        "/$controller/$action?/$id?"
        {
            constraints 
            {
                // id must be numeric.
				id(matches:/\d*/)
            }
        }
        
        "/"(controller:"about", action:"home")
        
        "500"(view:'/error')
        
		"/robots.txt" (view: "/robots")
    }
}
