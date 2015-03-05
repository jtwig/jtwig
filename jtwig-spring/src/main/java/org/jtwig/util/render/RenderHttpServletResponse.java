/**
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

package org.jtwig.util.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class RenderHttpServletResponse implements HttpServletResponse {

    private static Logger LOG = LoggerFactory.getLogger(RenderHttpServletResponse.class);

    private int contentLength = 0;
    private long contentLengthLong = 0;
    private OutputStream output = new ByteArrayOutputStream();
    private PrintWriter writer = new PrintWriter(output);

    private ServletOutputStream outputStream = new ServletOutputStream() {
        private WriteListener writeListener;

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.writeListener = writeListener;
        }

        @Override
        public void write(int b) throws IOException {
            output.write(b);
        }
    };


    @Override
    public String toString() {
        return output.toString();
    }

    @Override
    public void addCookie(Cookie cookie) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public boolean containsHeader(String name) {
        LOG.debug("Operation not supported on embed content");
        return false;
    }

    @Override
    public String encodeURL(String url) {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        LOG.debug("You can't send an error when including. Only the main request can do that!");
    }

    @Override
    public void sendError(int sc) throws IOException {
        LOG.debug("You can't send an error when including. Only the main request can do that!");
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        LOG.debug("You can't redirect when including. Only the main request can do that!");
    }

    @Override
    public void setDateHeader(String name, long date) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public void addDateHeader(String name, long date) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public void setHeader(String name, String value) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public void addHeader(String name, String value) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public void setIntHeader(String name, int value) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public void addIntHeader(String name, int value) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public void setStatus(int sc) {
        LOG.debug("You can't set the status when including. Only the main request can do that!");
    }

    @Override
    public void setStatus(int sc, String sm) {
        LOG.debug("You can't set the status when including. Only the main request can do that!");
    }

    @Override
    public int getStatus() {
        LOG.debug("Operation not supported on embed content");
        return 0;
    }

    @Override
    public String getHeader(String name) {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public String getContentType() {
        LOG.debug("Operation not supported on embed content");
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        LOG.debug("You can't set the character encoding when including. Only the main request can do that!");
    }

    @Override
    public void setContentLength(int len) {
        this.contentLength = len;
    }

    @Override
    public void setContentLengthLong(long len) {
        this.contentLengthLong = len;
    }

    @Override
    public void setContentType(String type) {
        LOG.debug("You can't set the content type when including. Only the main request can do that!");
    }

    @Override
    public void setBufferSize(int size) {
        LOG.debug("I'm not doing anything, sorry");
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return true;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {
        LOG.debug("Operation not supported on embed content");
    }

    @Override
    public Locale getLocale() {
        LOG.debug("Operation not supported on embed content");
        return null;
    }
}
