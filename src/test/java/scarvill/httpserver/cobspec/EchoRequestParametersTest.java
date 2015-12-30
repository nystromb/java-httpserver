package scarvill.httpserver.cobspec;

import org.junit.Test;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;

import java.util.function.Function;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class EchoRequestParametersTest {
    @Test
    public void testReturnsRequestParametersInResponseBody() {
        Function<Request, Response> handler = new EchoRequestParameters();
        Request request = new RequestBuilder()
            .setParameter("foo", "bar")
            .setParameter("bar", "baz")
            .build();

        Response response = handler.apply(request);

        assertThat(new String(response.getBody()), containsString("foo = bar"));
        assertThat(new String(response.getBody()), containsString("bar = baz"));
    }
}