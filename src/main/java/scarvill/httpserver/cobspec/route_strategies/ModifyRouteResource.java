package scarvill.httpserver.cobspec.route_strategies;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routes.Resource;

import java.util.function.Function;

public class ModifyRouteResource implements Function<Request, Response> {
    private Resource resource;
    private Status responseStatus = Status.OK;

    public ModifyRouteResource(Resource resource) {
        this.resource = resource;
    }

    public ModifyRouteResource(Resource resource, Status responseStatus) {
        this.resource = resource;
        this.responseStatus = responseStatus;
    }

    @Override
    public Response apply(Request request) {
        resource.setData(request.getBody());

        return new ResponseBuilder().setStatus(responseStatus).build();
    }
}
