package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Resource;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.StatusTwo;

import java.util.function.Function;

public class GetResourceHandler implements Function<Request, Response> {
    private Resource resource;

    public GetResourceHandler(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Response apply(Request request) {
        String contentLength = "Content-Length: " + resource.getData().length() + "\r\n";
        return new Response(StatusTwo.OK, new String[]{contentLength}, resource.getData());
    }
}
