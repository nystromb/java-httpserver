package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class DispatchTest {
    @Test
    public void testGetSuccess() {
        Request request = new Request("GET / HTTP/1.1\r\n");

        assertEquals(Status.OK, Dispatch.handle(request));
    }

    @Test
    public void testGetFailure() {
        Request request = new Request("GET /foobar HTTP/1.1\r\n");

        assertEquals(Status.NOT_FOUND, Dispatch.handle(request));
    }

    @Test
    public void testPut() {
        Request request = new Request("PUT / HTTP/1.1\r\n");

        assertEquals(Status.OK, Dispatch.handle(request));
    }

    @Test
    public void testPost() {
        Request request = new Request("POST / HTTP/1.1\r\n");

        assertEquals(Status.OK, Dispatch.handle(request));
    }

    @Test
    public void testOptions() {
        Request request = new Request("OPTIONS / HTTP/1.1\r\n");

        assertEquals(Status.OK + "Allow: GET,HEAD,POST,OPTIONS,PUT\r\n", Dispatch.handle(request));
    }

    @Test
    public void testUnsupportedAction() {
        Request request = new Request("FOO / HTTP/1.1\r\n");

        assertEquals(Status.NOT_FOUND, Dispatch.handle(request));
    }
}