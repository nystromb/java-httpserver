package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;

import java.io.*;

import static org.junit.Assert.*;

public class HTTPServiceTest {

    @Test
    public void testRespondsToARequest() throws Exception {
        HTTPService service = new HTTPService(new MockRouter());
        String rawRequest = RequestBuilder.build(Method.GET, "/");
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        service.accept(inputStream, outputStream);

        assertEquals(Status.OK, outputStream.toString());
    }

    private class MockRouter extends Router {
        public MockRouter() {
            super();
        }

        @Override
        public Response routeRequest(Request request) {
            return new Response(Status.OK);
        }
    }
}