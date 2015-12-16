package scarvill.httpserver.cobspec;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.HashMap;
import java.util.function.Function;

public class EchoRequestParameters implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        return new ResponseBuilder()
            .setStatus(Status.OK)
            .setBody(assembleResponseBody(request.getParameters()))
            .build();
    }

    private byte[] assembleResponseBody(HashMap<String, String> parameters) {
        String body = "";

        for (String key : parameters.keySet()) {
            body += key + " = " + parameters.get(key) + "\r\n";
        }

        return body.getBytes();
    }
}
