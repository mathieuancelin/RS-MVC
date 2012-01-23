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

import cx.ath.mancel01.restmvc.FrameworkFilter;
import cx.ath.mancel01.restmvc.utils.FileUtils;
import groovy.text.SimpleTemplateEngine;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mathieu ANCELIN
 */
public class TemplateRenderer {

    private final SimpleTemplateEngine engine;
    private final ConcurrentHashMap<String, groovy.text.Template> templates =
            new ConcurrentHashMap<String, groovy.text.Template>();

    public TemplateRenderer() {
        this.engine = new SimpleTemplateEngine();
    }

    public String render(String source, Map<String, Object> context) throws Exception {
        context.put("request", FrameworkFilter.currentRequest.get());
        context.put("root", FrameworkFilter.getContextRoot());
        return renderWithGroovy(source, context);
    }

    private String renderWithGroovy(String fileName, Map<String, Object> context) throws Exception {
        // TODO : if file not exists, return 404
        StringWriter osw = new StringWriter();
        context.put("out", osw);
        File file = FrameworkFilter.getFile("views/" + fileName);
        if (!templates.containsKey(file.getAbsolutePath())) {
            String code = FileUtils.readFileAsString(file);
            code = code.replace("$.", "\\$.").replace("$(", "\\$(");
            templates.putIfAbsent(file.getAbsolutePath()
                , engine.createTemplate(code));
        }
        return templates.get(file.getAbsolutePath()).make(context).writeTo(osw).toString();
    }
}