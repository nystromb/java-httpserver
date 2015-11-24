package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

import static org.junit.Assert.*;

public class ResponseTest {
    @Test
    public void testSetStatusLine() throws Exception {
        Response response = new Response(StatusTwo.OK);

        assertEquals("HTTP/1.1 200 OK\r\n", response.getStatusLine());
    }
}