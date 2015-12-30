package scarvill.httpserver.cobspec;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.function.Function;

public class EchoRequestParameters implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        return new ResponseBuilder()
            .setStatus(Status.OK)
            .setBody(assembleResponseBody(request))
            .build();
    }

    private byte[] assembleResponseBody(Request request) {
        String body = "";

        for (String parameter : request.getParameterNames()) {
            body += parameter + " = " + request.getParameterValue(parameter) + "\r\n";
        }

        return body.getBytes();
    }
}
