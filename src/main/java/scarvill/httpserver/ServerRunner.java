package scarvill.httpserver;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        ServerArguments arguments = new ServerArguments(args);
        HTTPService service = new HTTPService(new Router(new String[]{"/", "/form"}));
        Server server = new Server(arguments.port, service);

        server.start();
    }
}
