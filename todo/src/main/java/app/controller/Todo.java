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

package app.controller;

import app.model.Task;
import cx.ath.mancel01.restmvc.view.Render;
import cx.ath.mancel01.restmvc.view.View;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author Mathieu ANCELIN
 */
@Stateless
@Path("todo")
public class Todo {
    
    @Inject EntityManager em;

    @GET
    public Response index() {
        return new View("index.html")
            .param("tasks", Task.helper.all(em))
            .render();
    }

    @POST @Path("createTask")
    public Response createTask(@FormParam("title") String title) {
        Task task = new Task(title).save(em);
        return Render.json(task);
    }

    @POST @Path("change")
    public Response change(@FormParam("id") Long id, @FormParam("done") Boolean done) {
        Task task = Task.helper.findById(em, id);
        task.setDone(!done);
        return Render.json(task.save(em));
    }
}
