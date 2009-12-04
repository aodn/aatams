package au.org.emii.aatams.DownloadFilters;

/*
* Copyright 2004 The Apache Software Foundation
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


import java.io.IOException;
//import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//import java.util.Locale;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.ServletResponse;
//import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Implementation of <b>HttpServletResponseWrapper</b> that works with
 * the CompressionServletResponseStream implementation..
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version $Revision: 1.3 $, $Date: 2004/03/18 16:40:33 $
 * 
 * @author modified Stephen Cameron, removed threshold concept, everything is compressed
 * add setWrappedContentType
 */

public class CompressionServletResponseWrapper extends HttpServletResponseWrapper {

    // ----------------------------------------------------- Constructor

	private String wrappedContentType = "";
	
    /**
     * Calls the parent constructor which creates a ServletResponse adaptor
     * wrapping the given response object.
     */

    public CompressionServletResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
        if (debug > 1) {
            System.out.println("CompressionServletResponseWrapper constructor gets called");
        }
    }


    // ----------------------------------------------------- Instance Variables

    /**
     * Original response
     */

    protected HttpServletResponse origResponse = null;

    /**
     * Descriptive information about this Response implementation.
     */

    protected static final String info = "CompressionServletResponseWrapper";

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */

    protected ServletOutputStream stream = null;


    /**
     * The PrintWriter that has been returned by
     * <code>getWriter()</code>, if any.
     */

    protected PrintWriter writer = null;

    /**
     * The bufferSize number to compress, can be reset.
     */
    protected int bufferSize = 8129;

    /**
     * Debug level
     */
    private int debug = 0;

    /**
     * Content type
     */
    protected String contentType = null;
    
    
    /**
     * entry name
     */
    protected String entryName = null;  

    // --------------------------------------------------------- Public Methods


    /**
     * Set content type
     */
    public void setContentType(String contentType) {
        if (this.debug > 1) {
            System.out.println("setContentType to "+contentType);
        }
        //this.contentType = contentType;
        //this.origResponse.setContentType("application/zip");
    }
    
    /**
     * Set content type
     */
    public void setWrappedContentType(String contentType) {
        if (this.debug > 1) {
            System.out.println("setContentType to "+contentType);
        }
        this.wrappedContentType = contentType;
        this.origResponse.setContentType(this.wrappedContentType);
    }


    /**
     * Set bufferSize number
     */
    public void setBufferSize(int bufferSize) {
        if (this.debug > 1) {
            System.out.println("setBufferSize to " + bufferSize);
        }
        this.bufferSize = bufferSize;
    }


    /**
     * Set debug level
     */
    public void setDebugLevel(int debug) {
        this.debug = this.debug;
    }

    /**
     * Set entry name
     */
    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }  
    

    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {
        if (this.debug > 1) {
            System.out.println("createOutputStream gets called");
        }

        CompressionResponseStream stream = new CompressionResponseStream(this.origResponse);
        stream.setDebugLevel(this.debug);
        stream.setBufferSize(this.bufferSize);
        stream.setEntryName(this.entryName);
        return stream;

    }


    /**
     * Finish a response.
     */
    public void finishResponse() {
        try {
            if (this.writer != null) {
                this.writer.close();
            } else {
                if (this.stream != null)
                    this.stream.close();
            }
        } catch (IOException e) {
        }
    }


    // ------------------------------------------------ ServletResponse Methods


    /**
     * Flush the buffer and commit this response.
     *
     * @exception IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {
        if (this.debug > 1) {
            System.out.println("flush buffer @ CompressionServletResponseWrapper");
        }
        ((CompressionResponseStream)this.stream).flush();

    }

    /**
     * Return the servlet output this.stream associated with this Response.
     *
     * @exception IllegalStateException if <code>getWriter</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException {

        if (this.writer != null)
            throw new IllegalStateException("getWriter() has already been called for this response");

        if (this.stream == null)
            this.stream = createOutputStream();
        	((CompressionResponseStream)this.stream).setEntryName(this.entryName);
        if (this.debug > 1) {
            System.out.println("this.stream is set to "+this.stream+" in getOutputStream");
        }

        return (this.stream);

    }

    /**
     * Return the this.writer associated with this Response.
     *
     * @exception IllegalStateException if <code>getOutputStream</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException {

        if (this.writer != null)
            return (this.writer);

        if (this.stream != null)
            throw new IllegalStateException("getOutputStream() has already been called for this response");

        this.stream = createOutputStream();
        ((CompressionResponseStream)this.stream).setEntryName(this.entryName);
        if (this.debug > 1) {
            System.out.println("this.stream is set to "+this.stream+" in getWriter");
        }
        //String charset = getCharsetFromContentType(contentType);
        String charEnc = this.origResponse.getCharacterEncoding();
        if (this.debug > 1) {
            System.out.println("character encoding is " + charEnc);
        }
        // HttpServletResponse.getCharacterEncoding() shouldn't return null
        // according the spec, so feel free to remove that "if"
        if (charEnc != null) {
            this.writer = new PrintWriter(new OutputStreamWriter(this.stream, charEnc));
        } else {
            this.writer = new PrintWriter(this.stream);
        }
        return (this.writer);
    }


    public void setContentLength(int length) {
    }
}

