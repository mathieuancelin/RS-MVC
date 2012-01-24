/*
 *  Copyright 2012 Mathieu ANCELIN.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package cx.ath.mancel01.restmvc;

import cx.ath.mancel01.restmvc.utils.SimpleLogger;
import java.io.File;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebFilter used to access view in the current war.
 * Also, catch current request and response to use it later.
 * 
 * @author Mathieu ANCELIN
 */
@WebFilter(filterName = "FrameworkFilter", urlPatterns = {"/*"})
public class FrameworkFilter implements Filter {

    private static ServletContext context;

    private static ThreadLocal<HttpServletRequest> currentRequest =
            new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> currentResponse =
            new ThreadLocal<HttpServletResponse>();


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        currentRequest.set((HttpServletRequest) request);
        currentResponse.set((HttpServletResponse) response);
        Session.current.set(Session.restore());
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (Session.current.get() != null) {
            Session.current.get().save();
            Session.current.remove();
        }
        currentRequest.remove();
        currentResponse.remove();
    }

    @Override
    public void init(FilterConfig filterConfig) { 
        SimpleLogger.enableColors(true);
        SimpleLogger.enableTrace(true);
        context = filterConfig.getServletContext();
        if (filterConfig != null) {
            System.out.println("Initialization of REST-MVC framework");
        }
    }

    public static String getContextRoot() {
        return context.getContextPath();
    }

    public static String getFilePath(String name) {
        return context.getRealPath(name);
    }

    public static File getFile(String name) {
        return new File(context.getRealPath(name));
    }

    public static HttpServletResponse getResponse() {
        return currentResponse.get();
    }

    public static HttpServletRequest getRequest() {
        return currentRequest.get();
    }

    @Override
    public void destroy() {
        currentRequest.remove();
        currentResponse.remove();
    }
}
