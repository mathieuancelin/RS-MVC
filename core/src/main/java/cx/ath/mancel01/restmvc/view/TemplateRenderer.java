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

package cx.ath.mancel01.restmvc.view;

import cx.ath.mancel01.restmvc.FrameworkFilter;
import cx.ath.mancel01.restmvc.utils.FileUtils;
import cx.ath.mancel01.restmvc.utils.SimpleLogger;
import cx.ath.mancel01.restmvc.view.Render.Page;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to render groovy templates.
 * 
 * @author Mathieu ANCELIN
 */
class TemplateRenderer {

    private final SimpleTemplateEngine engine;
    private final ConcurrentHashMap<String, Template> templates =
            new ConcurrentHashMap<String, Template>();
    private final ViewLogger logger = new ViewLogger();

    public TemplateRenderer() {
        this.engine = new SimpleTemplateEngine();
        try {
            templates.put("404", 
                    engine.createTemplate(
                        new Page("Not found", "File not found").toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String render(String source, Map<String, Object> context) throws Exception {
        context.put("request", FrameworkFilter.getRequest());
        context.put("response", FrameworkFilter.getResponse());
        context.put("logger", logger);
        context.put("root", FrameworkFilter.getContextRoot());
        return renderWithGroovy(source, context);
    }

    private String renderWithGroovy(String fileName, Map<String, Object> context) throws Exception {
        StringWriter osw = new StringWriter();
        context.put("out", osw);
        File file = FrameworkFilter.getFile("views/" + fileName);
        String absolutePath = "404";
        if (file.exists()) {
            absolutePath = file.getAbsolutePath();
            if (!templates.containsKey(absolutePath)) {
                String code = FileUtils.readFileAsString(file);
                code = code.replace("$.", "\\$.").replace("$(", "\\$(");
                templates.putIfAbsent(absolutePath
                    , engine.createTemplate(code));
            }
        }
        return templates.get(absolutePath).make(context).writeTo(osw).toString();
    }
    
    static class ViewLogger {
        public void error(String message, Object... printable) {
            SimpleLogger.error(message, printable);
        }

        public void info(String message, Object... printable) {
            SimpleLogger.info(message, printable);
        }

        public void trace(String message, Object... printable) {
            SimpleLogger.trace(message, printable);
        }
    }
}