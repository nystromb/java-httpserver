package scarvill.httpserver;

import org.junit.Test;
import scarvill.httpserver.constants.StatusTwo;

import static org.junit.Assert.*;

public class HTTPResponseTest {
    @Test
    public void testGeneratesAWellFormedHTTPResponseFromResponseObject() {
        String[] headers = {"Foo: a random header\r\n", "Bar: another header\r\n"};
        Response response = new Response.Builder()
            .setStatus(StatusTwo.OK)
            .setHeaders(headers)
            .setBody("this is the response body")
            .build();
        String expectedRawResponse =
            "HTTP/1.1 200 OK\r\n" +
            "Foo: a random header\r\n" +
            "Bar: another header\r\n" +
            "\r\n" +
            "this is the response body";

        assertEquals(expectedRawResponse, new HTTPResponse().generate(response));
    }
}