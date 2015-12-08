package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HTTPResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routes.Router;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertArrayEquals;

public class HTTPServiceTest {

    @Test
    public void testRespondsToARequest() throws Exception {
        byte[] rawRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MockSocket clientSocket = new MockSocket(inputStream, outputStream);
        Response expectedResponse = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeaders(new String[]{"Header: a header\r\n"})
            .build();
        Logger logger = new Logger(new NullPrintStream());
        HTTPService service = new HTTPService(new MockRouter(expectedResponse), logger);

        service.serve(clientSocket).run();

        assertArrayEquals(new HTTPResponse().generate(expectedResponse), outputStream.toByteArray());
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