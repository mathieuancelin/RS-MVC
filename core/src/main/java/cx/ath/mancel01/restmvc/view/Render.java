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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * Main class to render thing from controllers.
 * 
 * @author Mathieu ANCELIN
 */
public class Render {

    public static Response redirect(String url) {
        ResponseBuilder builder;
        try {
            builder = Response.seeOther(new URI(url));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        return builder.build();
    }

    public static Response text(final String text) {
        return Response.ok(text, MediaType.TEXT_PLAIN).build();
    }

    public static Response binary(String file) {
        return binary(file, MediaType.APPLICATION_OCTET_STREAM);
    }

    public static Response binary(File file) {
        return binary(file, MediaType.APPLICATION_OCTET_STREAM);
    }
    
    public static Response binary(String file, String type) {
        return Response.ok(new File(file), type).build();
    }

    public static Response binary(File file, String type) {
        return Response.ok(file, type).build();
    }

    public static Response json(Object json) {
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    public static Response xml(Object xml) {
        return Response.ok(xml, MediaType.APPLICATION_XML).build();
    }

    public static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND)
            .type(MediaType.TEXT_HTML)
            .entity(new PageDuo("Page not found").toString()).build();
    }

    public static Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST)
            .type(MediaType.TEXT_HTML)
            .entity(new PageDuo("Bad request").toString()).build();
    }

    public static Response ok() {
        return Response.ok().build();
    }

    public static Response error() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.TEXT_HTML)
            .entity(new PageDuo("Error").toString()).build();
    }

    public static Response unavailable() {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
            .type(MediaType.TEXT_HTML)
            .entity(new Page("Error", "<h1>Service unavailable</h1>").toString()).build();
    }

    public static Response accesDenied() {
        return Response.status(Response.Status.FORBIDDEN)
            .type(MediaType.TEXT_HTML)
            .entity(new PageDuo("Acces denied").toString()).build();
    }

    public static Response todo() {
        return Response.status(501)
            .type(MediaType.TEXT_HTML)
            .entity(new Page("TODO", "<h1>Page not yet implemented</h1>").toString()).build();
    }

    public static View view(String name) {
        return new View(name);
    }
    
    static class PageDuo extends Page {

        public PageDuo(String title) {
            super(title, "<h1>" + title + "</h1>");
        }
    }
    
    static class Page {
        private final String title;
        private final String boody;

        public Page(String title, String boody) {
            this.title = title;
            this.boody = boody;
        }

        @Override
        public String toString() {
            return "<html>"
                    + "<head>"
                        + "<title>" + title + "</title>"
                    + "</head>"
                    + "<body>"
                        + boody
                    + "</body>"
                + "</html>";
        }
    }
}
