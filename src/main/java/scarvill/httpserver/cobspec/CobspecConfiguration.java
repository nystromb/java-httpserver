package scarvill.httpserver.cobspec;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.resource.FileResource;
import scarvill.httpserver.resource.Resource;
import scarvill.httpserver.resource.StringResource;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.*;
import scarvill.httpserver.server.HttpService;
import scarvill.httpserver.server.Logger;
import scarvill.httpserver.server.Serveable;
import scarvill.httpserver.server.ServerConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.function.Function;

import static scarvill.httpserver.response.Status.*;
import static scarvill.httpserver.request.Method.*;

public class CobspecConfiguration implements ServerConfiguration {
    private CommandLineArguments arguments;

    public CobspecConfiguration(CommandLineArguments arguments) {
        this.arguments = arguments;
    }

    @Override
    public int getPort() {
        return arguments.getPort();
    }

    @Override
    public String getPublicDirectory() {
        return arguments.getPublicDirectory();
    }

    @Override
    public Serveable getService() {
        return new HttpService(
            fileLogger(getPublicDirectory()),
            configuredRouter(getPublicDirectory()));
    }

    private Logger fileLogger(String publicDirectory) {
        try {
            File logFile = new File(publicDirectory + "/logs");
            logFile.createNewFile();

            return new Logger(new PrintStream(logFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Function<Request, Response> configuredRouter(String publicDirectory) {
        RouteRequest router = new RouteRequest();
        router.routeToResourcesInDirectory(Paths.get(publicDirectory));

        router.addRoute("/parameters", GET, new EchoRequestParameters());

        router.addRoute("/redirect", GET, giveRedirectResponse("http://localhost:5000/"));

        router.addRoute("/method_options", GET, giveStatusOKResponse());
        router.addRoute("/method_options", PUT, giveStatusOKResponse());
        router.addRoute("/method_options", POST, giveStatusOKResponse());
        router.addRoute("/method_options", HEAD, giveStatusOKResponse());

        Resource formResource = new StringResource("");
        router.addRoute("/form", GET, new GetRouteResource(formResource));
        router.addRoute("/form", POST, new ModifyRouteResource(formResource));
        router.addRoute("/form", PUT, new ModifyRouteResource(formResource));
        router.addRoute("/form", DELETE, new ModifyRouteResource(formResource));

        Resource logsResource = new FileResource(Paths.get(publicDirectory + "/logs"));
        router.addRoute("/logs", GET,
            new VerifyRequestAuthorization("admin", "hunter2", "Logging",
                new GetRouteResource(logsResource)));

        Resource patchContent = new FileResource(Paths.get(publicDirectory + "/patch-content.txt"));
        router.addRoute("/patch-content.txt", GET,
            new GetRouteResource(patchContent));
        router.addRoute("/patch-content.txt", PATCH,
            new ModifyRouteResource(patchContent, Status.NO_CONTENT));

        return router;
    }

    private Function<Request, Response> giveRedirectResponse(String redirectLocation) {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(FOUND)
                .setHeader("Location", redirectLocation)
                .build());
    }

    private Function<Request, Response> giveStatusOKResponse() {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(OK)
                .build());
    }
}

// To me, it seems a little out of place for the the "giveRedirectResponse" and "giveStatusOKResponse" functions to be defined here
// Shouldn't the server always be configured to give a 200 OK response as long as the route is defined?
// This, I believe, is not something that should be user configurable

// creating the routing chain might be more user friendly if you create the chain like how you create the logs route for each
// like first RouteRequest checks if route is defined
//      if not then send 404
//      if defined -> keep going down request chain of handlers and eventually to the one handler/controller you define for that route

// This seems redundant. Why not have 1 class to handle each request method?
//         router.addRoute("/form", POST, new ModifyRouteResource(formResource));
//         router.addRoute("/form", PUT, new ModifyRouteResource(formResource));
//         router.addRoute("/form", DELETE, new ModifyRouteResource(formResource));


// I like the abstractions using the routing classes this way, but thinking from a "framework" standpoint
// I think each route should have its own class to handling each method