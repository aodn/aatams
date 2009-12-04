package au.org.emii.aatams.DownloadFilters;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ZipDownloadFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;			
			// set the zip filename
			String filename = "data";
			String feature = request.getParameter("typename");
			if(feature != null){
				filename = feature.replace("aatams:","AATAMS-");
				response.setHeader("Content-Disposition:", "attachment; filename=" + filename + ".zip;");
			}
			else{
				response.setHeader("Content-Disposition:", "attachment; filename=AATAMS-Data-Download.zip;");
			}
			response.setHeader("Cache-Control:", "no-cache"); 
			//create response wrapper to do compression
			CompressionServletResponseWrapper wrappedResponse = new CompressionServletResponseWrapper(response);
			wrappedResponse.setWrappedContentType("application/zip");
			// set the single zip archive entry name
			String format = request.getParameter("outputformat");
			if(format != null){
				if(format.toLowerCase().endsWith("gml/3.1.1")){
					filename += ".xml";
				}
				else if (format.toLowerCase().endsWith("html")){
					filename += ".html";
				}
				else if (format.toLowerCase().endsWith("csv")){
					filename += ".csv";
				}
			}
			wrappedResponse.setEntryName(filename);
			//go to WFS for data
			chain.doFilter(req, wrappedResponse);
			wrappedResponse.finishResponse();
			return;
		} else
			chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
