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

import org.parboiled.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ReadListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import static org.jtwig.util.FilePath.path;
import static org.jtwig.util.ObjectSnapshot.snapshot;

public class RenderHttpServletRequest implements HttpServletRequest {
    private static Logger LOG = LoggerFactory.getLogger(RenderHttpServletRequest.class);

    private static final String URL_GET_ATTR = "=";
    private static final String URL_GET_SEPARATOR = "&amp;";

    private HttpMethod method = HttpMethod.GET;
    private String relativeUrl;
    private Map<String, List<String>> getParameters = new LinkedHashMap<>();
    private InputStream content = new ByteArrayInputStream("".getBytes());
    private MediaType mediaType = MediaType.APPLICATION_JSON;
    private final Map<String, Object> attributes = new HashMap<>();
    private Map<String, List<String>> postParameters = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();
    private final HttpServletRequest initialValues;
    private RequestDispatcher requestDispatcher;

    private String realPath;

    public RenderHttpServletRequest(HttpServletRequest initialRequest) {
        initialValues = snapshot(initialRequest, HttpServletRequest.class);
        Enumeration attributeNames = initialRequest.getAttributeNames();
        if (attributeNames != null) {
            while (attributeNames.hasMoreElements()) {
                String name = (String) attributeNames.nextElement();
                attributes.put(name, initialRequest.getAttribute(name));
            }
        }
        realPath = initialRequest.getRealPath("");
        requestDispatcher = initialRequest.getRequestDispatcher(initialRequest.getServletPath());
    }

    public RenderHttpServletRequest withMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public RenderHttpServletRequest to(String relativeUrl) {
        this.relativeUrl = relativeUrl;
        return this;
    }

    public RenderHttpServletRequest withGetParameter(String name, String value) {
        if (!getParameters.containsKey(name))
            getParameters.put(name, new ArrayList<String>());
        getParameters.get(name).add(value);
        return this;
    }

    public RenderHttpServletRequest withPostParameter(String name, String value) {
        if (!postParameters.containsKey(name))
            postParameters.put(name, new ArrayList<String>());
        postParameters.get(name).add(value);
        return this;
    }

    public RenderHttpServletRequest withContent(InputStream inputStream) {
        content = inputStream;
        return this;
    }

    public RenderHttpServletRequest withContent(String content) {
        this.content = new ByteArrayInputStream(content.getBytes());
        return this;
    }

    public RenderHttpServletRequest withContentType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public String getMethod() {
        return method.name();
    }

    @Override
    public String getPathInfo() {
        return relativeUrl;
    }

    @Override
    public String getQueryString() {
        List<String> parameters = new ArrayList<>();
        for (String getParameterKey : getParameters.keySet()) {
            for (String value : getParameters.get(getParameterKey))
                parameters.add(
                        encode(getParameterKey) + URL_GET_ATTR + encode(value)
                );
        }
        return StringUtils.join(parameters, URL_GET_SEPARATOR);
    }

    @Override
    public String[] getParameterValues(String name) {
        List<String> values = new ArrayList<>();
        if (getParameters.containsKey(name))
            values.addAll(getParameters.get(name));
        if (postParameters.containsKey(name))
            values.addAll(postParameters.get(name));

        return values.toArray(new String[values.size()]);
    }

    @Override
    public Map getParameterMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.putAll(getParameters);
        map.putAll(postParameters);
        return map;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(content));
    }

    @Override
    public String getRequestURI() {
        return getContextPath() + getServletPath() +
                relativeUrl + ";jsessionid=" + getRequestedSessionId();
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        String scheme = getScheme();
        int port = getServerPort();

        url.append(scheme);
        url.append("://");
        url.append(getServerName());
        if ((scheme.equals("http") && (port != 80))
                || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(getRequestURI());
        return url;
    }

    @Override
    public int getContentLength() {
        try {
            return (content == null) ? 0 : content.available();
        } catch (IOException e) {
            LOG.error("Can't get the content size of the content body", e);
            return 0;
        }
    }

    @Override
    public long getContentLengthLong() {
        try {
            return (content == null) ? 0 : content.available();
        } catch (IOException e) {
            LOG.error("Can't get the content size of the content body", e);
            return 0;
        }
    }

    @Override
    public String getContentType() {
        return mediaType.getType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            ReadListener readListener;

            @Override
            public boolean isFinished() {
                try {
                    return content.available() != 0;
                } catch (IOException e) {
                    return true;
                }
            }

            @Override
            public boolean isReady() {
                return !this.isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
            }

            @Override
            public int read() throws IOException {
                return content.read();
            }
        };
    }

    @Override
    public String getParameter(String name) {
        List<String> values = null;
        if (!postParameters.containsKey(name))
            values = getParameters.get(name);
        else
            values = postParameters.get(name);

        return (values == null || values.isEmpty()) ? null : values.get(0);
    }

    @Override
    public Enumeration getParameterNames() {
        Vector<String> vector = new Vector<>();
        vector.addAll(postParameters.keySet());
        vector.addAll(getParameters.keySet());
        return vector.elements();
    }

    @Override
    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        return new Vector<>(attributes.keySet()).elements();
    }

    @Override
    public String getPathTranslated() {
        return getRealPath(this.relativeUrl);
    }

    @Override
    public String getAuthType() {
        return initialValues.getAuthType();
    }

    @Override
    public Cookie[] getCookies() {
        return initialValues.getCookies();
    }

    @Override
    public long getDateHeader(String name) {
        return (long) headers.get(name);
    }

    @Override
    public String getHeader(String name) {
        return (String) headers.get(name);
    }

    @Override
    public Enumeration getHeaders(String name) {
        return (Enumeration) headers.get(name);
    }

    @Override
    public Enumeration getHeaderNames() {
        return new Vector<>(headers.keySet()).elements();
    }

    @Override
    public int getIntHeader(String name) {
        return (int) headers.get(name);
    }

    @Override
    public String getContextPath() {
        return initialValues.getContextPath();
    }

    @Override
    public String getRemoteUser() {
        return initialValues.getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String role) {
        LOG.debug("Cannot check if user is in role");
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return initialValues.getUserPrincipal();
    }

    @Override
    public String getRequestedSessionId() {
        return initialValues.getRequestedSessionId();

    }

    @Override
    public String getServletPath() {
        return initialValues.getServletPath();
    }

    @Override
    public HttpSession getSession(boolean create) {
        LOG.error("Cannot create session in embedded rendering");
        return getSession();
    }

    @Override
    public HttpSession getSession() {
        return initialValues.getSession();
    }

    @Override
    public String changeSessionId() {
        return this.initialValues.changeSessionId();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return initialValues.isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return initialValues.isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return initialValues.isRequestedSessionIdFromURL();
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

	@Override public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return this.initialValues.authenticate(response);
	}

	@Override public void login(String username, String password) throws ServletException {
		this.initialValues.login(username, password);
	}

	@Override public void logout() throws ServletException {
		this.initialValues.logout();
	}

	@Override public Collection<Part> getParts() throws IOException, ServletException {
		return this.initialValues.getParts();
	}

	@Override public Part getPart(String name) throws IOException, ServletException {
		return this.initialValues.getPart(name);
	}

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return this.initialValues.upgrade(handlerClass);
    }

    @Override
    public String getCharacterEncoding() {
        return initialValues.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        LOG.warn("Unable to set the character encoding for embed requests");
    }

    @Override
    public String getProtocol() {
        return initialValues.getProtocol();
    }

    @Override
    public String getScheme() {
        return initialValues.getScheme();
    }

    @Override
    public String getServerName() {
        return initialValues.getServerName();
    }

    @Override
    public int getServerPort() {
        return initialValues.getServerPort();
    }

    @Override
    public String getRemoteAddr() {
        return initialValues.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return initialValues.getRemoteHost();
    }

    @Override
    public Locale getLocale() {
        return initialValues.getLocale();
    }

    @Override
    public Enumeration getLocales() {
        return initialValues.getLocales();
    }

    @Override
    public boolean isSecure() {
        return initialValues.isSecure();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        LOG.debug("Returning the same request dispatcher everytime");
        return requestDispatcher;
    }

    @Override
    public String getRealPath(String path) {
        return path(realPath).append(path).toString();
    }

    @Override
    public int getRemotePort() {
        return initialValues.getRemotePort();
    }

    @Override
    public String getLocalName() {
        return initialValues.getLocalName();
    }

    @Override
    public String getLocalAddr() {
        return initialValues.getLocalAddr();
    }

    @Override
    public int getLocalPort() {
        return initialValues.getLocalPort();
    }

	@Override public ServletContext getServletContext() {
		return this.initialValues.getServletContext();
	}

	@Override public AsyncContext startAsync() throws IllegalStateException {
		return this.initialValues.startAsync();
	}

	@Override public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
		return null;
	}

	@Override public boolean isAsyncStarted() {
		return this.initialValues.isAsyncStarted();
	}

	@Override public boolean isAsyncSupported() {
		return this.initialValues.isAsyncSupported();
	}

	@Override public AsyncContext getAsyncContext() {
		return this.initialValues.getAsyncContext();
	}

	@Override public DispatcherType getDispatcherType() {
		return this.initialValues.getDispatcherType();
	}

	private String encode(String value) {
        String encoding = Charset.defaultCharset().displayName();
        try {
            return URLEncoder.encode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Unable to find encoding " + encoding, e);
            return value;
        }
    }

}
