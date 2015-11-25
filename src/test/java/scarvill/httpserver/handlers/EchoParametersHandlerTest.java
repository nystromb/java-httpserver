package scarvill.httpserver.handlers;

import org.junit.Test;
import scarvill.httpserver.Request;
import scarvill.httpserver.Response;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class EchoParametersHandlerTest {
    @Test
    public void testReturnsRequestParametersInResponseBody() {
        Function<Request, Response> handler = new EchoParametersHandler();
        HashMap<String , String> parameters = new HashMap<>();
        parameters.put("foo", "bar");
        parameters.put("bar", "baz");
        Request request = new Request.Builder().setParameters(parameters).build();

        Response response = handler.apply(request);

        assertTrue(response.getBody().contains("foo = bar"));
        assertTrue(response.getBody().contains("bar = baz"));
    }
}