package scarvill.httpserver.cobspec.route_strategies;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;

import java.util.HashMap;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class EchoRequestParametersTest {
    @Test
    public void testReturnsRequestParametersInResponseBody() {
        Function<Request, Response> handler = new EchoRequestParameters();
        HashMap<String , String> parameters = new HashMap<>();
        parameters.put("foo", "bar");
        parameters.put("bar", "baz");
        Request request = new RequestBuilder().setParameters(parameters).build();

        Response response = handler.apply(request);

        assertTrue(response.getBody().contains("foo = bar"));
        assertTrue(response.getBody().contains("bar = baz"));
    }
}