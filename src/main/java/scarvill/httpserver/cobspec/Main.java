package scarvill.httpserver.cobspec;

import scarvill.httpserver.HTTPService;
import scarvill.httpserver.Server;
import scarvill.httpserver.ServerConfiguration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerConfiguration config = new CommandLineArguments(args);
        HTTPService service = new HTTPService(
            Cobspec.fileLogger(config.getPublicDirectory()),
            Cobspec.configuredRouter(config.getPublicDirectory()));
        Server server = new Server(config.getPort(), service);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Cobspec.serverTeardown(config.getPublicDirectory());
            }
        });

        server.start();
    }
}
