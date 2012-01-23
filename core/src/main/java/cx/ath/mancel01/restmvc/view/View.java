/*
 *  Copyright 2010 Mathieu ANCELIN.
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

package cx.ath.mancel01.restmvc.view;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author Mathieu ANCELIN
 */
public class View extends Renderable {

    private static final TemplateRenderer renderer = new TemplateRenderer();
    private static final String TYPE = MediaType.TEXT_HTML;
    private final String viewName;
    private final Map<String, Object> context;

    public View(String viewName) {
        this.contentType = TYPE;
        this.viewName = viewName;
        this.context = new HashMap<String, Object>();
    }

    public View(String viewName, Map<String, Object> context) {
        this.contentType = TYPE;
        this.viewName = viewName;
        this.context = context;
    }

    public View param(String name, Object value) {
        this.context.put(name, value);
        return this;
    }

    @Override
    public Response render() {
        try {
            String name = viewName;
            Throwable t = new Throwable();
            String className = t.getStackTrace()[1].getClassName();
            String methodName = t.getStackTrace()[1].getMethodName();
            if (this.viewName == null) {
                name = methodName + ".html";
            }
            className = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
            name = className + "/" + name;
            String renderText = renderer.render(name, context);
            ResponseBuilder builder = Response.ok(renderText, TYPE);
            return builder.build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
