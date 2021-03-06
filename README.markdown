About REST-MVC
===============

REST-MVC is a lightweight framework on top of Java EE 6 to write web applications using REST architecture.

For instance you can write a controller like this one :

```java
@Stateless
@Path("todo")
public class Todo {
    
    @Inject EntityManager em;

    @GET
    public Response index() {
        return Render.view("index.html")
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
```