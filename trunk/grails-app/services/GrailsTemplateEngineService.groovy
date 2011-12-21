import javax.servlet.http.HttpServletRequest
import org.codehaus.groovy.grails.web.servlet.DefaultGrailsApplicationAttributes
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.springframework.web.context.support.WebApplicationContextUtils
import org.codehaus.groovy.grails.plugins.PluginManagerHolder
import org.codehaus.groovy.grails.commons.GrailsResourceUtils

class GrailsTemplateEngineService {

    static transactional = false

    GroovyPagesTemplateEngine groovyPagesTemplateEngine

    static PATH_TO_VIEWS = "/WEB-INF/grails-app/views"


    protected String renderView(templateName, model, pluginName = null) {
        if(!groovyPagesTemplateEngine) throw new IllegalStateException("Property [groovyPagesTemplateEngine] must be set!")
        assert templateName

        def engine = groovyPagesTemplateEngine
        def requestAttributes = RequestContextHolder.getRequestAttributes()
		boolean unbindRequest = false

		// outside of an executing request, establish a mock version
		if(!requestAttributes) {
			def servletContext  = ServletContextHolder.getServletContext()
			def applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext)
			requestAttributes = grails.util.GrailsWebUtil.bindMockWebRequest(applicationContext)
			unbindRequest = true
		}

		def servletContext = requestAttributes.request.servletContext
		def request = requestAttributes.request

        def grailsAttributes = new DefaultGrailsApplicationAttributes(servletContext);
        // See if the application has the view for it
        def uri = resolveViewUri(templateName, request)

        def r = engine.getResourceForUri(uri)

        // Try plugin view if not found in application
        if ((!r || !r.exists()) && pluginName) {
            // Caution, this uses views/ always, whereas our app view resolution uses the PATH_TO_MAILVIEWS which may in future be orthogonal!
            def plugin = PluginManagerHolder.pluginManager.getGrailsPlugin(pluginName)
            String pathToView
            if (plugin) {
                pathToView = '/plugins/'+plugin.name+'-'+plugin.version+'/'+GrailsResourceUtils.GRAILS_APP_DIR+'/views'+templateName
            }

            if (pathToView != null) {
                uri = GrailsResourceUtils.WEB_INF +pathToView +templateName+".gsp";
                r = engine.getResourceForUri(uri)
            }
        }
		def t = engine.createTemplate( r )
		
        def out = new StringWriter();
        def originalOut = requestAttributes.getOut()
        requestAttributes.setOut(out)
        try {
            if(model instanceof Map) {
                t.make( model ).writeTo(out)
            }
    		else {
    			t.make().writeTo(out)
    		}
	    }
	    finally {
	        requestAttributes.setOut(originalOut)
			if(unbindRequest) {
				RequestContextHolder.setRequestAttributes(null)
			}
	    }

        return out.toString();
    }

    protected String resolveViewUri(String viewName, HttpServletRequest request) {

        StringBuffer buf = new StringBuffer(PATH_TO_VIEWS);

        if(viewName.startsWith("/")) {
           String tmp = viewName.substring(1,viewName.length());
           if(tmp.indexOf('/') > -1) {
        	   buf.append('/');
        	   buf.append(tmp.substring(0,tmp.lastIndexOf('/')));
        	   buf.append("/");
        	   buf.append(tmp.substring(tmp.lastIndexOf('/') + 1,tmp.length()));
           }
           else {
        	   buf.append("/");
        	   buf.append(viewName.substring(1,viewName.length()));
           }
        }
        else {
           if (!request) throw new IllegalArgumentException(
               "View cannot be loaded from relative view paths where there is no current HTTP request")
           def grailsAttributes = new DefaultGrailsApplicationAttributes(request.servletContext);
           buf.append(grailsAttributes.getControllerUri(request))
                .append("/")
                .append(viewName);

        }
        return buf.append(".gsp").toString();
	}

}

