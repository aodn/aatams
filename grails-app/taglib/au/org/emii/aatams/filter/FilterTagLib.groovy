package au.org.emii.aatams.filter

class FilterTagLib  {
    def listFilterIncludes = {
        out << g.javascript(src:"listFilter.js?v=${grailsApplication.metadata.'app.version'}")
    }

    def listFilter = {
        attrs, body ->

        out << render(template:"/filter/listFilterTemplate", model:[name:attrs.name, body:body])
    }
}
