package scarvill.httpserver.mocks;

import scarvill.httpserver.Request;
import scarvill.httpserver.Response;

import java.util.function.Function;

public class MockHandler implements Function<Request, Response> {
    private String expectedResponseStatus;

    public MockHandler(String expectedResponseStatus) {
        this.expectedResponseStatus = expectedResponseStatus;
    }

    @Override
    public Response apply(Request request) {
        return new Response(expectedResponseStatus);
    }
}
