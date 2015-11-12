package scarvill.httpserver;

import java.io.IOException;

public class ServerRunner {
    public static void main(String[] args) throws IOException {
        ServerArguments arguments = new ServerArguments(args);
        HTTPService service = new HTTPService(cobspecRouter());
        Server server = new Server(arguments.port, service);

        server.start();
    }

    private static Router cobspecRouter() {
        Router router = new Router();
        router.addRoute("/", new String[]{Method.GET});
        router.addRoute("/form", new String[]{Method.GET, Method.PUT, Method.POST});
        router.addRoute("/file1", new String[]{Method.GET});
        router.addRoute("/text-file.txt", new String[]{Method.GET});
        return router;
    }
}
