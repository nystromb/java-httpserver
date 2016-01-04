package scarvill.httpserver.cobspec;

import scarvill.httpserver.server.Server;
import scarvill.httpserver.server.ServerConfiguration;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerConfiguration config = new CobspecConfiguration(new CommandLineArguments(args));
        Server server = new Server(config);

        server.start();
    }
}

// I really like the simplicity here of setting up and starting your server