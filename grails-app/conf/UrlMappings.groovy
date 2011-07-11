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

		"/"(controller:"project")
		"500"(view:'/error')
	}
}
