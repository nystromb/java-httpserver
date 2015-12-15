package scarvill.httpserver;

import scarvill.httpserver.request.HTTPRequest;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HTTPResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.routing.Router;

import java.io.*;
import java.net.Socket;

public class HTTPService implements Serveable {
    private final Router router;
    private final Logger logger;
    private final ServerIO io;

    public HTTPService(Logger logger, Router router) {
        this.router = router;
        this.logger = logger;
        this.io = new ServerIO();
    }

    public Runnable serve(Socket clientSocket) {
        return () -> {
            try (BufferedReader in =
                     new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedOutputStream out =
                     new BufferedOutputStream(clientSocket.getOutputStream())
            ) {
                serveRequest(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void serveRequest(BufferedReader in, BufferedOutputStream out) throws IOException {
        Request request = io.readRequest(in);
        Response response = router.routeRequest(request);
        io.writeResponse(out, response);
        logTransaction(request, response);
    }

    private void logTransaction(Request request, Response response) {
        logger.logRequest(request);
        logger.logResponse(response);
    }
}
