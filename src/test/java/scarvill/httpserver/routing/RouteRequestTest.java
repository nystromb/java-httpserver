package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static scarvill.httpserver.request.Method.GET;
import static scarvill.httpserver.request.Method.OPTIONS;

public class RouteRequestTest {

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() {
        Response response = new RouteRequest().apply(
            new RequestBuilder().setURI("/unconfigured").build());

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenNoStrategyExistsForRequestMethod() {
        RouteRequest routeRequest = new RouteRequest();
        Response responseToGive = new ResponseBuilder().setStatus(Status.OK).build();
        routeRequest.addRoute("/", Method.POST, new GiveStaticResponse(responseToGive));

        Response routerResponse = routeRequest.apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/")
                .build());

        assertEquals(Status.METHOD_NOT_ALLOWED, routerResponse.getStatus());
    }

    @Test
    public void testReturnsResultOfApplyingConfiguredRouteStrategy() {
        RouteRequest router = new RouteRequest();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/")
                .build());

        assertEquals(Status.OK, routerResponse.getStatus());
    }

    @Test
    public void testDynamicallyHandlesOptionsRequests() {
        Request optionsRequest = new RequestBuilder().setMethod(OPTIONS).setURI("/").build();
        RouteRequest routeRequest = new RouteRequest();
        routeRequest.addRoute("/", Method.GET, new GiveStaticResponse(
            new ResponseBuilder().build()));

        Response withoutConfiguredPutRouteResponse = routeRequest.apply(optionsRequest);

        routeRequest.addRoute("/", Method.PUT, new GiveStaticResponse(
            new ResponseBuilder().build()));

        Response withConfiguredPutRouteResponse = routeRequest.apply(optionsRequest);

        assertEquals(Status.OK, withConfiguredPutRouteResponse.getStatus());
        assertEquals(Status.OK, withoutConfiguredPutRouteResponse.getStatus());

        assertTrue(withConfiguredPutRouteResponse.getHeaders().get("Allow").contains("PUT"));
        assertFalse(withoutConfiguredPutRouteResponse.getHeaders().get("Allow").contains("PUT"));
    }

    @Test
    public void testCanRouteToResourcesInADirectory() throws IOException {
        String fileContents = "contents";
        Path directory = createTempDirectory();
        Path file = createTempFileWithContent(directory, fileContents.getBytes());

        RouteRequest routeRequest = new RouteRequest();
        routeRequest.routeToResourcesInDirectory(directory);

        Response response = routeRequest.apply(
            new RequestBuilder()
                .setMethod(GET)
                .setURI("/" + file.getFileName())
                .build());

        assertEquals(Status.OK, response.getStatus());
        assertEquals(fileContents, new String(response.getBody()));
    }

    private Path createTempDirectory() throws IOException {
        Path directory = Files.createTempDirectory("");
        directory.toFile().deleteOnExit();

        return directory;
    }

    private Path createTempFileWithContent(Path dir, byte[] content) throws IOException {
        File file = File.createTempFile("temp", "", dir.toFile());
        Files.write(file.toPath(), content);
        file.deleteOnExit();

        return file.toPath();
    }
}