package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;

import java.io.*;

import static org.junit.Assert.*;

public class HTTPServiceTest {

    @Test
    public void testRespondsToARequest() throws Exception {
        String rawRequest = RequestBuilder.build(Method.GET, "/");
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Response expectedResponse = new Response(Status.OK, new String[]{"Header: a header\r\n"});
        HTTPService service = new HTTPService(new MockRouter(expectedResponse));

        service.accept(inputStream, outputStream);

        assertEquals(expectedResponse.generate(), outputStream.toString());
    }

    private class MockRouter extends Router {
        private Response response;

        public MockRouter(Response expectedResponse) {
            this.response = expectedResponse;
        }

        @Override
        public Response routeRequest(Request request) {
            return response;
        }
    }
}