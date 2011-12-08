package au.org.emii.aatams.filter

import au.org.emii.aatams.*
import au.org.emii.aatams.report.*

class FilterTagLib 
{
	def listFilterIncludes =
	{
		out << g.javascript(src:"listFilter.js")	
	}
	
	def listFilter =
	{
		attrs, body ->

		out << render(template:"/filter/listFilterTemplate", model:[name:attrs.name, body:body])		
	}
}
