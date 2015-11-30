package scarvill.httpserver.mocks;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.ResponseBuilder;
import scarvill.httpserver.constants.Status;

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
