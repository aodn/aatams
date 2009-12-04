package au.org.emii.aatams.DownloadFilters;
/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
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
/** 
 * Modified by ASC for use in a Zip archive filter
 */
//import java.util.zip.GZipOutputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * Implementation of <b>ServletOutputStream</b> that works with
 * the CompressionServletResponseWrapper implementation.
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version $Revision$, $Date$
 */

public class CompressionResponseStream
        extends ServletOutputStream {

    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet output stream associated with the specified Response.
     *
     * @param response The associated response
     */
    public CompressionResponseStream(HttpServletResponse response) throws IOException {

        super();
        closed = false;
        this.response = response;
        this.output = response.getOutputStream();
    }

    // ----------------------------------------------------- Instance Variables


    /**
     * The threshold number which decides to compress or not.
     * Users can configure in web.xml to set it to fit their needs.
     */
    protected int bufferSize = 0;
    
    /**
     * Entry name
     */
    private String entryName = "data";

    /**
     * Debug level
     */
    private int debug = 0;

    /**
     * The buffer through which all of our output bytes are passed.
     */
    protected byte[] buffer = null;

    /**
     * The number of data bytes currently in the buffer.
     */
    protected int bufferCount = 0;

    /**
     * The underlying Zip output stream to which we should write data.
     */
    protected ZipOutputStream zipstream = null;

    /**
     * Has this stream been closed?
     */
    protected boolean closed = false;

    /**
     * The content length past which we will not write, or -1 if there is
     * no defined content length.
     */
    protected int length = -1;

    /**
     * The response with which this servlet output stream is associated.
     */
    protected HttpServletResponse response = null;

    /**
     * The underlying servket output stream to which we should write data.
     */
    protected ServletOutputStream output = null;

    // --------------------------------------------------------- Public Methods

    
    /**
     * Set entry name
     */
    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
    
    /**
     * Set debug level
     */
    public void setDebugLevel(int debug) {
        this.debug = debug;
    }


    /**
     * Set the bufferSize number and create buffer for this size
     */
    protected void setBufferSize(int size) {
        this.bufferSize = size;
        this.buffer = new byte[this.bufferSize];
        if (debug > 1) {
            System.out.println("this.buffer is set to " + this.bufferSize);
        }
    }

    /**
     * Close this output stream, causing any this.buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException {

        if (debug > 1) {
            System.out.println("close() @ CompressionResponseStream");
        }
        if (closed)
            throw new IOException("This output stream has already been closed");

        //ASC we only want a zip file
        //if (zipstream != null) {
            flushToZip();
            zipstream.closeEntry();
            zipstream.close();
            zipstream = null;
        /*} else {
            if (this.bufferCount > 0) {
                if (debug > 2) {
                    System.out.print("output.write(");
                    System.out.write(this.buffer, 0, this.bufferCount);
                    System.out.println(")");
                }
                output.write(this.buffer, 0, this.bufferCount);
                this.bufferCount = 0;
            }
        }*/

        output.close();
        closed = true;

    }


    /**
     * Flush any this.buffered data for this output stream, which also causes the
     * response to be committed.
     */
    public void flush() throws IOException {

        if (debug > 1) {
            System.out.println("flush() @ CompressionResponseStream");
        }
        if (closed) {
            throw new IOException("Cannot flush a closed output stream");
        }

        if (zipstream != null) {
            zipstream.flush();
        }

    }

    public void flushToZip() throws IOException {

        if (debug > 1) {
            System.out.println("flushToZip() @ CompressionResponseStream");
        }
        if (this.bufferCount > 0) {
            if (debug > 1) {
                System.out.println("flushing out to zipstream, this.bufferCount = " + this.bufferCount);
            }
            writeToZip(this.buffer, 0, this.bufferCount);
            this.bufferCount = 0;
        }

    }

    /**
     * Write the specified byte to our output stream.
     *
     * @param b The byte to be written
     * @throws IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {

        if (debug > 1) {
            System.out.println("write " + b + " in CompressionResponseStream ");
        }
        if (closed)
            throw new IOException("Cannot write to a closed output stream");

        if (this.bufferCount >= this.buffer.length) {
            flushToZip();
        }

        this.buffer[this.bufferCount++] = (byte) b;

    }


    /**
     * Write <code>b.length</code> bytes from the specified byte array
     * to our output stream.
     *
     * @param b The byte array to be written
     * @throws IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {

        write(b, 0, b.length);

    }


    /**
     * Write <code>len</code> bytes from the specified byte array, starting
     * at the specified offset, to our output stream.
     *
     * @param b   The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     * @throws IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {

        if (debug > 1) {
            System.out.println("write, this.bufferCount = " + this.bufferCount + " len = " + len + " off = " + off);
        }
        if (debug > 2) {
            System.out.print("write(");
            System.out.write(b, off, len);
            System.out.println(")");
        }

        if (closed)
            throw new IOException("Cannot write to a closed output stream");

        if (len == 0)
            return;

        // Can we write into this.buffer ?
        if (len <= (this.buffer.length - this.bufferCount)) {
            System.arraycopy(b, off, this.buffer, this.bufferCount, len);
            this.bufferCount += len;
            return;
        }

        // There is not enough space in this.buffer. Flush it ...
        flushToZip();

        // ... and try again. Note, that this.bufferCount = 0 here !
        if (len <= (this.buffer.length - this.bufferCount)) {
            System.arraycopy(b, off, this.buffer, this.bufferCount, len);
            this.bufferCount += len;
            return;
        }

        // write direct to Zip
        writeToZip(b, off, len);
    }

    public void writeToZip(byte b[], int off, int len) throws IOException {

        if (debug > 1) {
            System.out.println("writeToZip, len = " + len);
        }
        if (debug > 2) {
            System.out.print("writeToZip(");
            System.out.write(b, off, len);
            System.out.println(")");
        }
        if (zipstream == null) {
            if (debug > 1) {
                System.out.println("new ZipOutputStream");
            }
            //ASC this is not needed, we should set the "Content-Disposition"
            //response.addHeader("Content-Encoding", "Zip");
            zipstream = new ZipOutputStream(output);
            this.zipstream.putNextEntry(new ZipEntry(this.entryName));
        }
        zipstream.write(b, off, len);

    }

    // -------------------------------------------------------- Package Methods


    /**
     * Has this response stream been closed?
     */
    public boolean closed() {

        return (this.closed);

    }

}


