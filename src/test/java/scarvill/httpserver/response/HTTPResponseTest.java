package scarvill.httpserver.response;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HTTPResponseTest {
    @Test
    public void testGeneratesAWellFormedHTTPResponseFromResponseObject() {
        String[] headers = {"Foo: a random header\r\n", "Bar: another header\r\n"};
        Response response = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeaders(headers)
            .setBody("this is the response body".getBytes())
            .build();
        String expectedRawResponse =
            "HTTP/1.1 200 OK\r\n" +
            "Foo: a random header\r\n" +
            "Bar: another header\r\n" +
            "\r\n" +
            "this is the response body";

        assertArrayEquals(expectedRawResponse.getBytes(), new HTTPResponse().generate(response));
    }
}