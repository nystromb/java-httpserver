package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTest {
    @Test
    public void testSetStatusLine() throws Exception {
        Response response = new Response(Status.OK);

        assertEquals("HTTP/1.1 200 OK\r\n", response.getStatusLine());
    }
}