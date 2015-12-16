package scarvill.httpserver.routing;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.request.RequestBuilder;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.route_strategies.GiveStaticResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VirtualResourceRouterTest {

    @Test
    public void testReturnsResponseWithStatusNotFoundForUnconfiguredRoute() {
        Request request = new RequestBuilder().setURI("/unconfigured").build();
        VirtualResourceRouter router = new VirtualResourceRouter();

        Response response = router.routeRequest(request);

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    @Test
    public void testReturnsMethodNotAllowedWhenNoStrategyExistsForRequestMethod() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        VirtualResourceRouter router = new VirtualResourceRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.POST, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(Status.METHOD_NOT_ALLOWED, routerResponse.getStatus());
    }

    @Test
    public void testReturnsResultOfApplyingConfiguredRouteStrategy() {
        Request request = new RequestBuilder().setMethod(Method.GET).setURI("/").build();
        Status expectedResponseStatus = Status.OK;
        VirtualResourceRouter router = new VirtualResourceRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(expectedResponseStatus, routerResponse.getStatus());
    }

    @Test
    public void testDynamicallyHandlesOptionsRequests() {
        Request request = new RequestBuilder().setMethod(Method.OPTIONS).setURI("/").build();
        VirtualResourceRouter router = new VirtualResourceRouter();
        Response response = new ResponseBuilder().setStatus(Status.OK).build();
        router.addRoute("/", Method.GET, new GiveStaticResponse(response));

        Response routerResponse = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertTrue(routerResponse.getHeaders().get("Allow").contains("GET"));
        assertTrue(routerResponse.getHeaders().get("Allow").contains("OPTIONS"));

        router.addRoute("/", Method.POST, new GiveStaticResponse(response));
        Response newRouterResponse = router.routeRequest(request);

        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("GET"));
        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("POST"));
        assertTrue(newRouterResponse.getHeaders().get("Allow").contains("OPTIONS"));
    }

    @Test
    public void testCanDelegateHandlingUnconfiguredRoutesToFileSystemRouter() throws IOException {
        String fileContents = "contents";
        Path directory = Files.createTempDirectory("dir");
        Path file = createTempFileWithContent(directory, fileContents.getBytes());

        VirtualResourceRouter router = new VirtualResourceRouter();
        router.addFilesystemRouter(new FileSystemRouter(directory));
        Request request = new RequestBuilder()
            .setMethod(Method.GET)
            .setURI("/" + file.getFileName())
            .build();

        Response response = router.routeRequest(request);

        assertEquals(Status.OK, response.getStatus());
        assertEquals(fileContents, new String(response.getBody()));

        deleteFiles(Arrays.asList(file, directory));
    }

    private Path createTempFileWithContent(Path dir, byte[] content) throws IOException {
        Path file = Files.createTempFile(dir, "temp", "");
        Files.write(file, content);

        return file;
    }

    private void deleteFiles(List<Path> paths) throws IOException {
        for (Path path : paths) {
            Files.delete(path);
        }
    }
}