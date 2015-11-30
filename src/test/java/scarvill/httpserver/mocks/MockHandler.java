package scarvill.httpserver.mocks;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.function.Function;

public class MockHandler implements Function<Request, Response> {
    private Status expectedResponseStatus;

    public MockHandler(Status expectedResponseStatus) {
        this.expectedResponseStatus = expectedResponseStatus;
    }

    @Override
    public Response apply(Request request) {
        return new ResponseBuilder().setStatus(expectedResponseStatus).build();
    }
}
