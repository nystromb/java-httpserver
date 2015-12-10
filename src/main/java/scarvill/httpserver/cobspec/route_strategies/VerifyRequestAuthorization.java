package scarvill.httpserver.cobspec.route_strategies;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.util.Base64;
import java.util.function.Function;

public class VerifyRequestAuthorization implements Function<Request, Response> {
    private String encodedId;
    private Function<Request, Response> verifiedRequestRouteStrategy;

    public VerifyRequestAuthorization(
        String username, String password, Function<Request, Response> routeStrategy) {
        encodedId = idEncode(username, password);
        verifiedRequestRouteStrategy = routeStrategy;
    }

    @Override
    public Response apply(Request request) {

        if (request.getHeaders().containsKey("Authorization") &&
            authorizationToken(request).equals(encodedId)) {
            return verifiedRequestRouteStrategy.apply(request);
        } else {
            return new ResponseBuilder().setStatus(Status.UNAUTHORIZED).build();
        }
    }

    private String idEncode(String username, String password) {
        String usernamePassword = username + ":" + password;

        return Base64.getEncoder().encodeToString(usernamePassword.getBytes());
    }

    private String authorizationToken(Request request) {
        return request.getHeaders().get("Authorization").split(" ")[1];
    }
}
