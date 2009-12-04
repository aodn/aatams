package au.org.emii.aatams.DownloadFilters;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class TextDownloadFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			// setting headers and contentType after chain.doFilter() is
			// ineffective!! (surprisingly)
			// set the filename
			String filename = "data";
			String feature = request.getParameter("typename");
			if(feature != null){
				filename = feature.replace("aatams:","AATAMS-");
			}
			else{
				filename = "AATAMS-Data-Download";
			}
			// set the filename based on output format name
			String format = request.getParameter("outputformat");
			if(format != null){
				if(format.toLowerCase().endsWith("gml/3.1.1")){
					response.setContentType("text/xml");
					response.setHeader("Content-Disposition:", "attachment; filename=" + filename + ".xml;");
				}
				else if (format.toLowerCase().endsWith("html")){
					response.setContentType("text/html");
					response.setHeader("Content-Disposition:", "attachment; filename=" + filename + ".html;");
				}
				else if (format.toLowerCase().endsWith("csv")){
					response.setContentType("text/csv");
					response.setHeader("Content-Disposition:", "attachment; filename=" + filename + ".csv;");
				}
			}
			response.setHeader("Cache-Control:", "no-cache"); 
			//go to WFS for data
			chain.doFilter(req, response);
			return;
		} else
			chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
