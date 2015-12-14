package scarvill.httpserver.cobspec;

import scarvill.httpserver.HTTPService;
import scarvill.httpserver.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerArguments arguments = new ServerArguments(args);
        HTTPService service = new HTTPService(
            Cobspec.fileLogger(arguments.publicDirectory),
            Cobspec.configuredRouter(arguments.publicDirectory));
        Server server = new Server(arguments.port, service);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Cobspec.serverTeardown(arguments.publicDirectory);
            }
        });

        server.start();
    }
}
