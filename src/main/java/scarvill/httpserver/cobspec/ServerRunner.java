package scarvill.httpserver.cobspec;

import scarvill.httpserver.*;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        ServerArguments arguments = new ServerArguments(args);
        HTTPService service = new HTTPService(Cobspec.configuredRouter());
        Server server = new Server(arguments.port, service);

        server.start();
    }
}
