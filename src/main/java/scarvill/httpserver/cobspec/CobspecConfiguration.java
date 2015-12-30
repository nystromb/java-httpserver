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