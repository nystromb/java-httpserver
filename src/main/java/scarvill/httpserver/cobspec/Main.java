package scarvill.httpserver.cobspec;

import scarvill.httpserver.Server;
import scarvill.httpserver.ServerConfiguration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerConfiguration config = new CobspecConfiguration(new CommandLineArguments(args));
        Server server = new Server(config);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                config.serverTearDown();
            }
        });

        server.start();
    }
}
