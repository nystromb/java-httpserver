package scarvill.httpserver;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class QueryStringTest {

    @Test
    public void testParsesQueryString() {
        assertEquals("bar", new QueryString().parse("foo=bar").get("foo"));
    }

    @Test
    public void testSeparatesQueryStringArguments() {
        HashMap<String, String> parameters = new QueryString().parse("foo=bar&bar=baz");

        assertEquals("bar", parameters.get("foo"));
        assertEquals("baz", parameters.get("bar"));
    }

    @Test
    public void testTranslatesSpecialCharacterCodes() {
        String query = "message=%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26" +
            "%2C%20%40%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";

        String translatedMessage =
            "<, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?";

        assertEquals(translatedMessage, new QueryString().parse(query).get("message"));
    }
}