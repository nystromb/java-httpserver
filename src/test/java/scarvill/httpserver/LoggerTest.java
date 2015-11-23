package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Method;
import scarvill.httpserver.constants.Status;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class LoggerTest {
    @Test
    public void testLogsRequestToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        String requestToBeLogged = RequestUtility.rawRequest(Method.GET, "/");
        logger.logRequest(requestToBeLogged);

        assertTrue(out.toString().contains("Received request:"));
        assertTrue(out.toString().contains(requestToBeLogged));
    }

    @Test
    public void testLogsResponseToGivenOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Logger logger = new Logger(new PrintStream(out));
        String responseToBeLogged = new Response(Status.OK).generate();
        logger.logResponse(responseToBeLogged);

        assertTrue(out.toString().contains("Sent response:"));
        assertTrue(out.toString().contains(responseToBeLogged));
    }
}