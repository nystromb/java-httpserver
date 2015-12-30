package scarvill.httpserver.server;

import org.junit.Test;
import scarvill.httpserver.response.HttpResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.GiveStaticResponse;
import scarvill.httpserver.routing.RouteRequest;

import java.io.*;
import java.net.Socket;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class HttpServiceTest {
    @Test
    public void testLogsHTTPTransactions() {
        ByteArrayInputStream inputStream =
            new ByteArrayInputStream("GET / HTTP/1.1\r\n\r\n".getBytes());
        ByteArrayOutputStream logStream = new ByteArrayOutputStream();
        HttpService service = new HttpService(
            new Logger(new PrintStream(logStream)),
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build()));

        service.serve(new MockSocket(inputStream, new ByteArrayOutputStream())).run();

        assertThat(new String(logStream.toByteArray()), containsString("Received Request"));
        assertThat(new String(logStream.toByteArray()), containsString("Sent Response"));
    }

    @Test
    public void testSendsResponseToClientRequest() throws Exception {
        ByteArrayInputStream inputStream =
            new ByteArrayInputStream("GET / HTTP/1.1\r\n\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Response expectedResponse = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Header", "a header")
            .setBody("body".getBytes())
            .build();
        HttpService service = new HttpService(
            new Logger(new NullPrintStream()),
            new GiveStaticResponse(expectedResponse));

        service.serve(new MockSocket(inputStream, outputStream)).run();

        assertThat(new HttpResponse().generate(expectedResponse),
            equalTo(outputStream.toByteArray()));
    }

    @Test
    public void testSends500ServerErrorResponseWhenAnIOExceptionIsRaised() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpService service = new HttpService(
            new Logger(new NullPrintStream()),
            new RouteRequest());

        service.serve(new MockSocket(new FailingInputStream(), outputStream)).run();

        assertThat(new String(outputStream.toByteArray()),
            containsString(Status.SERVER_ERROR.toString()));
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

    private class FailingInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException();
        }
    }

    private class NullPrintStream extends PrintStream {
        public NullPrintStream() {
            super(new ByteArrayOutputStream());
        }
    }
}