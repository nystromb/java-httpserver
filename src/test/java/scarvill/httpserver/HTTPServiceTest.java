package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

import java.io.*;

import static org.junit.Assert.*;

public class HTTPServiceTest {

    @Test
    public void testRespondsToARequest() throws Exception {
        String rawRequest = "GET / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        Response expectedResponse = new Response(StatusTwo.OK, new String[]{"Header: a header\r\n"});
        Logger logger = new Logger(new NullPrintStream());
        HTTPService service = new HTTPService(new MockRouter(expectedResponse), logger);

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

    private class NullPrintStream extends PrintStream {
        public NullPrintStream() {
            super(new ByteArrayOutputStream());
        }
    }
}