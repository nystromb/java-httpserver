package scarvill.httpserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotFoundHandlerTest {

    @Test
    public void testReturnsResponseWith404NotFoundStatus() throws Exception {
        NotFoundHandler handler = new NotFoundHandler();
        Request anyRequest = new Request(RequestBuilder.build(Method.GET,"/"));

        assertEquals(Status.NOT_FOUND, handler.apply(anyRequest).getStatusLine());
    }
}