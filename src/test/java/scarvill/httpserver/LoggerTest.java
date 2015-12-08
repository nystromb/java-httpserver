package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.HTTPResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoggerTest {
    @Test
    public void testLogsRequestToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/a/route")
            .setParameter("name", "value")
            .setBody("body".getBytes())
            .build();
        logger.logRequest(request);

        assertTrue(out.toString().contains("Received request:"));
        assertTrue(out.toString().contains("Method: " + request.getMethod().toString()));
        assertTrue(out.toString().contains("Path: " + request.getURI()));
        assertTrue(out.toString().contains("Parameters: " + "name=value"));
        assertTrue(out.toString().contains("Body-length: " + request.getBody().length));
    }

    @Test
    public void testLogsResponseToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        Response response = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeaders(new String[]{"Header: a header\r\n", "Other: other header\r\n"})
            .setBody("body".getBytes())
            .build();
        logger.logResponse(response);

        assertTrue(out.toString().contains("Sent response:"));
        assertTrue(out.toString().contains("Status: " + response.getStatus().toString()));
        assertTrue(out.toString().contains("Headers:"));
        assertTrue(out.toString().contains("- " + "Header: a header\r\n"));
        assertTrue(out.toString().contains("- " + "Other: other header\r\n"));
        assertTrue(out.toString().contains("Body-length: " + response.getBody().length));
    }
}