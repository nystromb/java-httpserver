package scarvill.httpserver;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class QueryStringTest {

    @Test
    public void testParsesQueryString() {
        assertEquals("foo=bar", new QueryString().parse("foo=bar").get(0));
    }

    @Test
    public void testSeparatesQueryStringArguments() {
        List<String> queryStringArgs = new QueryString().parse("foo=bar&foobar=foobaz");

        assertEquals("foo=bar", queryStringArgs.get(0));
        assertEquals("foobar=foobaz", queryStringArgs.get(1));
    }

    @Test
    public void testTranslatesSpecialCharacterCodes() {
        String query = "%3C%2C%20%3E%2C%20%3D%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40" +
            "%2C%20%23%2C%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F";

        String translatedQuery = "<, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?";

        assertEquals(translatedQuery, new QueryString().parse(query).get(0));
    }
}