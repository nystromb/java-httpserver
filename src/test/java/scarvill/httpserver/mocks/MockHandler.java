package scarvill.httpserver.mocks;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;
import scarvill.httpserver.constants.StatusTwo;

import java.util.function.Function;

public class MockHandler implements Function<Request, Response> {
    private StatusTwo expectedResponseStatus;

    public MockHandler(StatusTwo expectedResponseStatus) {
        this.expectedResponseStatus = expectedResponseStatus;
    }

    @Override
    public Response apply(Request request) {
        return new Response(expectedResponseStatus);
    }
}
