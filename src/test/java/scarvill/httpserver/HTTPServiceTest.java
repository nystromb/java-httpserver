package scarvill.httpserver;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class HTTPServiceTest {

    @Test
    public void testRespondsToARequest() throws Exception {
        HTTPService service = new HTTPService(new MockRouter());
        InputStream inputStream = new ByteArrayInputStream("GET / HTTP/1.1\r\n".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        service.accept(inputStream, outputStream);

        assertEquals(Status.OK, outputStream.toString());
    }

    private class MockRouter extends Router {
        public MockRouter() {
            super(new String[]{});
        }

        @Override
        public Response routeRequest(Request request) {
            return new Response(Status.OK);
        }
    }
}