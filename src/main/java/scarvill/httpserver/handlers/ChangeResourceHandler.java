package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Resource;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.Status;

import java.util.function.Function;

public class ChangeResourceHandler implements Function<Request, Response> {
    private Resource resource;

    public ChangeResourceHandler(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Response apply(Request request) {
        resource.setData(request.getBody());

        return new Response.Builder().setStatus(Status.OK).build();
    }
}
