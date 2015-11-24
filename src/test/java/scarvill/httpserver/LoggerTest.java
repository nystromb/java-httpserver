package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class LoggerTest {
    @Test
    public void testLogsRequestToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        String requestToBeLogged = "GET / HTTP/1.1";
        logger.logRequest(requestToBeLogged);

        assertTrue(out.toString().contains("Received request:"));
        assertTrue(out.toString().contains(requestToBeLogged));
    }

    @Test
    public void testLogsResponseToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        String responseToBeLogged = new HTTPResponse().generate(new Response(StatusTwo.OK));
        logger.logResponse(responseToBeLogged);

        assertTrue(out.toString().contains("Sent response:"));
        assertTrue(out.toString().contains(responseToBeLogged));
    }
}