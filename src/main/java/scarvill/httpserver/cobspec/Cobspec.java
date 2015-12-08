package scarvill.httpserver.cobspec;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.cobspec.route_strategies.ModifyRouteResource;
import scarvill.httpserver.cobspec.route_strategies.EchoRequestParameters;
import scarvill.httpserver.cobspec.route_strategies.GetRouteResource;
import scarvill.httpserver.cobspec.route_strategies.GiveStaticResponse;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.routes.FileResource;
import scarvill.httpserver.routes.Resource;
import scarvill.httpserver.routes.StringResource;
import scarvill.httpserver.routes.Router;

import java.io.File;
import java.util.function.Function;

public class Cobspec {

    public static Router configuredRouter(String publicDirectory) {
        Router router = new Router();

        router.addRoute("/", Method.GET, new GetRouteResource(new StringResource("")));

        router.addRoute("/parameters", Method.GET, new EchoRequestParameters());

        router.addRoute("/redirect", Method.GET, giveRedirectResponse("http://localhost:5000/"));

        router.addRoute("/method_options", Method.GET, giveStatusResponse(Status.OK));
        router.addRoute("/method_options", Method.PUT, giveStatusResponse(Status.OK));
        router.addRoute("/method_options", Method.POST, giveStatusResponse(Status.OK));
        router.addRoute("/method_options", Method.HEAD, giveStatusResponse(Status.OK));

        Resource formResource = new StringResource("");
        router.addRoute("/form", Method.GET, new GetRouteResource(formResource));
        router.addRoute("/form", Method.POST, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.PUT, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.DELETE, new ModifyRouteResource(formResource));

        Resource file1 = new FileResource(new File(publicDirectory + "/file1").toPath());
        router.addRoute("/file1", Method.GET, new GetRouteResource(file1));

        Resource textfile = new FileResource(new File(publicDirectory + "/text-file.txt").toPath());
        router.addRoute("/text-file.txt", Method.GET, new GetRouteResource(textfile));

        Resource jpeg = new FileResource(new File(publicDirectory + "/image.jpeg").toPath());
        router.addRoute("/image.jpeg", Method.GET, new GetRouteResource(jpeg));

        Resource png = new FileResource(new File(publicDirectory + "/image.png").toPath());
        router.addRoute("/image.png", Method.GET, new GetRouteResource(png));

        Resource gif = new FileResource(new File(publicDirectory + "/image.gif").toPath());
        router.addRoute("/image.gif", Method.GET, new GetRouteResource(gif));

        return router;
    }

    private static Function<Request, Response> giveRedirectResponse(String redirectLocation) {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.FOUND)
                .setHeaders(new String[]{"Location: " + redirectLocation + "\r\n"})
                .build());
    }

    private static Function<Request, Response> giveStatusResponse(Status status) {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(status)
                .build());
    }
}