package scarvill.httpserver.response;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpResponseTest {
    @Test
    public void testGeneratesAWellFormedHTTPResponseFromResponseObject() {
        Response response = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Foo", "a header")
            .setHeader("Bar", "other header")
            .setBody("this is the response body".getBytes())
            .build();
        String expectedRawResponse =
            "HTTP/1.1 200 OK\r\n" +
            "Bar: other header\r\n" +
            "Foo: a header\r\n" +
            "\r\n" +
            "this is the response body";

        assertEquals(expectedRawResponse, new String(new HttpResponse().generate(response)));
    }
}