package scarvill.httpserver.server;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Function;

public class HttpService implements Serveable {
    private final Function<Request, Response> router;
    private final Logger logger;
    private final ServerIO io;

    public HttpService(Logger logger, Function<Request, Response> router) {
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
                handleClientTransaction(in, out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void handleClientTransaction(BufferedReader in, BufferedOutputStream out) throws IOException {
        Request request = io.readRequest(in);
        Response response = router.apply(request);
        io.writeResponse(out, response);
        logTransaction(request, response);
    }

    private void logTransaction(Request request, Response response) {
        logger.logRequest(request);
        logger.logResponse(response);
    }
}
