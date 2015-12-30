package scarvill.httpserver.routing;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.resource.Resource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.function.Function;

public class GetRouteResource implements Function<Request, Response> {
    private Resource resource;

    public GetRouteResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Response apply(Request request) {
        if (hasPartialContentRangeHeader(request)) {
            return new GetPartialContent(resource).apply(request);
        } else {
            return new ResponseBuilder()
                .setStatus(Status.OK)
                .setHeader("Content-Length", String.valueOf(resource.getData().length))
                .setBody(resource.getData())
                .build();
        }
    }

    private boolean hasPartialContentRangeHeader(Request request) {
        return request.getHeaderNames().contains("Range");
    }
}
