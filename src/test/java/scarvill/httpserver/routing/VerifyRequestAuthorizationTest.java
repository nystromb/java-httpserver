package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.Base64;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static scarvill.httpserver.request.Method.GET;

public class VerifyRequestAuthorizationTest {
    @Test
    public void testDelegatesToGivenStrategyWhenRequestProvidesCorrectUsernameAndPassword() {
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        String token = Base64.getEncoder().encodeToString("username:password".getBytes());

        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(
                new RequestBuilder()
                    .setMethod(GET)
                    .setURI("/")
                    .setHeader("Authorization", "Basic " + token)
                    .build());

        assertEquals(Status.OK, response.getStatus());

    }

    @Test
    public void testDeniesRequestWithoutAuthorizationHeader() {
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());

        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(
                new RequestBuilder()
                    .setMethod(GET)
                    .setURI("/")
                    .build());

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testIncludesWWWAuthenticateHeaderIn401Response() {
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());

        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(
                new RequestBuilder()
                    .setMethod(GET)
                    .setURI("/")
                    .build());

        assertEquals("Basic realm=FortKnox", response.getHeaders().get("WWW-Authenticate"));
    }

    @Test
    public void testDeniesRequestWithCorrectUsernameAndIncorrectPassword() {
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        String token = Base64.getEncoder().encodeToString("username:incorrect-password".getBytes());

        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(
                new RequestBuilder()
                    .setMethod(GET)
                    .setURI("/")
                    .setHeader("Authorization", "Basic " + token)
                    .build());

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testDeniesRequestWithIncorrectUsernameAndCorrectPassword() {
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());
        String token = Base64.getEncoder().encodeToString("incorrect-username:password".getBytes());

        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(
                new RequestBuilder()
                    .setMethod(GET)
                    .setURI("/")
                    .setHeader("Authorization", "Basic " + token)
                    .build());

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testDeniesRequestWithIllFormedAuthorizationHeader() {
        Function<Request, Response> routeStrategy =
            new GiveStaticResponse(new ResponseBuilder().setStatus(Status.OK).build());

        Response response =
            new VerifyRequestAuthorization("username", "password", "FortKnox", routeStrategy).apply(
                new RequestBuilder()
                    .setMethod(Method.GET)
                    .setURI("/")
                    .setHeader("Authorization", "foo")
                    .build());

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }
}