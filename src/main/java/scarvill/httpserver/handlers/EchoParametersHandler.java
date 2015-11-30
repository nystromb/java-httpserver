package scarvill.httpserver.handlers;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.ResponseBuilder;
import scarvill.httpserver.constants.Status;

import java.util.HashMap;
import java.util.function.Function;

public class EchoParametersHandler implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        return new ResponseBuilder()
            .setStatus(Status.OK)
            .setBody(assembleResponseBody(request.getParameters()))
            .build();
    }

    private String assembleResponseBody(HashMap<String, String> parameters) {
        String body = "";

        for (String key : parameters.keySet()) {
            body += key + " = " + parameters.get(key) + "\r\n";
        }

        return body;
    }
}
