package scarvill.httpserver.routing;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetRouteOptions implements Function<Request, Response> {
    private Collection<Method> allowedMethods;

    public GetRouteOptions(Collection<Method> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    @Override
    public Response apply(Request request) {
        return new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Allow", allowedMethodsString(allowedMethods))
            .build();
    }

    private String allowedMethodsString(Collection<Method> allowedMethods) {
        String methodsString =
            allowedMethods.stream().map(Method::toString).collect(Collectors.joining(","));

        if (!methodsString.contains(Method.OPTIONS.toString())) {
            methodsString += "," + Method.OPTIONS.toString();
        }

        return methodsString;
    }
}

// I like the way you handle your options request
