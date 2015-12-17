package scarvill.httpserver.cobspec;

import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routing.*;
import scarvill.httpserver.routing.resource.FileResource;
import scarvill.httpserver.routing.resource.Resource;
import scarvill.httpserver.routing.resource.StringResource;
import scarvill.httpserver.server.HttpService;
import scarvill.httpserver.server.Logger;
import scarvill.httpserver.server.Serveable;
import scarvill.httpserver.server.ServerConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.function.Function;

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

    @Override
    public void serverTearDown() {
        new File(arguments.getPublicDirectory() + "/logs").delete();
    }

    private Logger fileLogger(String publicDirectory) {
        try {
            File logFile = new File(publicDirectory + "/logs");
            logFile.createNewFile();

            return new Logger(new PrintStream(logFile));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Function<Request, Response> configuredRouter(String publicDirectory) {
        RouteRequest router = new RouteRequest();
        router.routeToResourcesInDirectory(Paths.get(publicDirectory));

        router.addRoute("/parameters", Method.GET, new EchoRequestParameters());

        router.addRoute("/redirect", Method.GET, giveRedirectResponse("http://localhost:5000/"));

        router.addRoute("/method_options", Method.GET, giveStatusOKResponse());
        router.addRoute("/method_options", Method.PUT, giveStatusOKResponse());
        router.addRoute("/method_options", Method.POST, giveStatusOKResponse());
        router.addRoute("/method_options", Method.HEAD, giveStatusOKResponse());

        Resource formResource = new StringResource("");
        router.addRoute("/form", Method.GET, new GetRouteResource(formResource));
        router.addRoute("/form", Method.POST, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.PUT, new ModifyRouteResource(formResource));
        router.addRoute("/form", Method.DELETE, new ModifyRouteResource(formResource));

        Resource logsResource = new FileResource(Paths.get(publicDirectory + "/logs"));
        router.addRoute("/logs", Method.GET,
            new VerifyRequestAuthorization("admin", "hunter2", "Logging",
                new GetRouteResource(logsResource)));

        Resource patchContent = new FileResource(Paths.get(publicDirectory + "/patch-content.txt"));
        router.addRoute("/patch-content.txt", Method.GET,
            new GetRouteResource(patchContent));
        router.addRoute("/patch-content.txt", Method.PATCH,
            new ModifyRouteResource(patchContent, Status.NO_CONTENT));

        return router;
    }

    private Function<Request, Response> giveRedirectResponse(String redirectLocation) {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.FOUND)
                .setHeader("Location", redirectLocation)
                .build());
    }

    private Function<Request, Response> giveStatusOKResponse() {
        return new GiveStaticResponse(
            new ResponseBuilder()
                .setStatus(Status.OK)
                .build());
    }
}