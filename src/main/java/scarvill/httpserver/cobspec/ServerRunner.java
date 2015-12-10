package scarvill.httpserver.cobspec;

import scarvill.httpserver.HTTPService;
import scarvill.httpserver.Logger;
import scarvill.httpserver.Server;
import scarvill.httpserver.routes.Router;
import sun.rmi.runtime.Log;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        ServerArguments arguments = new ServerArguments(args);
        Logger logger = Cobspec.fileLogger(arguments.publicDirectory);
        Router router = Cobspec.configuredRouter(arguments.publicDirectory);
        Server server = new Server(arguments.port, new HTTPService(router, logger));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Cobspec.serverTeardown(arguments.publicDirectory);
            }
        });

        server.start();
    }
}
