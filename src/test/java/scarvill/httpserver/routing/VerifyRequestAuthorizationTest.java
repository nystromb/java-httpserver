package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.GiveStaticResponse;
import scarvill.httpserver.routing.VerifyRequestAuthorization;

import java.util.Base64;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class VerifyRequestAuthorizationTest {
    @Test
    public void testDelegatesToGivenStrategyWhenRequestProvidesCorrectUsernameAndPassword() {
        String token = Base64.getEncoder().encodeToString("username:password".getBytes());
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/")
            .setHeader("Authorization", "Basic " + token)
            .build();
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(request);

        assertEquals(Status.OK, response.getStatus());

    }

    @Test
    public void testDeniesRequestWithoutUsernameAndPassword() {
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/")
            .build();
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(request);

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testIncludesWWWAuthenticateHeaderIn401Response() {
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/")
            .build();
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(request);

        assertEquals("Basic realm=FortKnox", response.getHeaders().get("WWW-Authenticate"));
    }

    @Test
    public void testDeniesRequestWithCorrectUsernameAndIncorrectPassword() {
        String token = Base64.getEncoder().encodeToString("username:incorrect-password".getBytes());
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/")
            .setHeader("Authorization", "Basic " + token)
            .build();
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(request);

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testDeniesRequestWithIncorrectUsernameAndCorrectPassword() {
        String token = Base64.getEncoder().encodeToString("incorrect-username:password".getBytes());
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/")
            .setHeader("Authorization", "Basic " + token)
            .build();
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(request);

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }
}