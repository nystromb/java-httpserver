package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Status;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class HTTPServiceTest {

    @Test
    public void testRespondsToARequest() throws Exception {
        String rawRequest = "GET / HTTP/1.1";
        InputStream inputStream = new ByteArrayInputStream(rawRequest.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        MockSocket clientSocket = new MockSocket(inputStream, outputStream);
        Response expectedResponse = new Response.Builder()
            .setStatus(Status.OK)
            .setHeaders(new String[]{"Header: a header\r\n"})
            .build();
        Logger logger = new Logger(new NullPrintStream());
        HTTPService service = new HTTPService(new MockRouter(expectedResponse), logger);

        service.serve(clientSocket).run();

        assertEquals(new HTTPResponse().generate(expectedResponse), outputStream.toString());
    }

    private class MockSocket extends Socket {
        private final OutputStream outputStream;
        private final InputStream inputStream;

        public MockSocket(InputStream inputStream, OutputStream outputStream) {
            this.outputStream = outputStream;
            this.inputStream = inputStream;
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }
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