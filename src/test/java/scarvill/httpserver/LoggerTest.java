package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Status;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class LoggerTest {
    @Test
    public void testLogsRequestToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        String rawRequest = "GET / HTTP/1.1";
        logger.logRequest(rawRequest);

        assertTrue(out.toString().contains("Received request:"));
        assertTrue(out.toString().contains(rawRequest));
    }

    @Test
    public void testLogsResponseToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        String rawResponse = new HTTPResponse().generate(
            new ResponseBuilder().setStatus(Status.OK).build());
        logger.logResponse(rawResponse);

        assertTrue(out.toString().contains("Sent response:"));
        assertTrue(out.toString().contains(rawResponse));
    }
}