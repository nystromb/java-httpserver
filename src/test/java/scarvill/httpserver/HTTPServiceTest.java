package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HTTPResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.Router;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HTTPServiceTest {
    @Test
    public void testLogsHTTPTransactions() {
        byte[] rawRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        MockSocket clientSocket =
            new MockSocket(new ByteArrayInputStream(rawRequest), new ByteArrayOutputStream());
        Response expectedResponse = new ResponseBuilder().setStatus(Status.OK).build();
        ByteArrayOutputStream logStream = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(logStream));
        HTTPService service = new HTTPService(logger, new MockRouter(expectedResponse));

        service.serve(clientSocket).run();

        assertTrue(new String(logStream.toByteArray()).contains("Received Request"));
        assertTrue(new String(logStream.toByteArray()).contains("Sent Response"));
    }

    @Test
    public void testSendsResponseToClientRequest() throws Exception {
        byte[] rawRequest = "GET / HTTP/1.1\r\n\r\n".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(rawRequest);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MockSocket clientSocket = new MockSocket(inputStream, outputStream);
        Response expectedResponse = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Header", "a header")
            .setBody("body".getBytes())
            .build();
        Logger logger = new Logger(new NullPrintStream());
        HTTPService service = new HTTPService(logger, new MockRouter(expectedResponse));
        String expectedResponseString = new String(new HTTPResponse().generate(expectedResponse));

        service.serve(clientSocket).run();

        assertEquals(expectedResponseString, new String(outputStream.toByteArray()));
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