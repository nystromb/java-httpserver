package scarvill.httpserver;

import scarvill.httpserver.routes.Router;
import scarvill.httpserver.request.HTTPRequest;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HTTPResponse;
import scarvill.httpserver.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HTTPService implements Serveable {
    private Router router;
    private Logger logger;
    private Socket clientSocket;

    public HTTPService(Router router, Logger logger) {
        this.router = router;
        this.logger = logger;
    }

    public HTTPService serve(Socket clientSocket) {
        this.clientSocket = clientSocket;
        return this;
    }

    @Override
    public void run() {
        try {
            serveRequest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void serveRequest() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        Request request = readRequest(in);
        Response response = router.routeRequest(request);
        sendResponse(out, response);
        out.close();
    }

    private Request readRequest(BufferedReader in) throws IOException {
        String rawRequest = in.readLine() + "\r\n";
        while (in.ready()) {
            rawRequest = rawRequest.concat(Character.toString((char) in.read()));
        }
        logger.logRequest(rawRequest);
        return new HTTPRequest(rawRequest).parse();
    }

    private void sendResponse(PrintWriter out, Response response) {
        String rawResponse = new HTTPResponse().generate(response);
        logger.logResponse(rawResponse);
        out.print(rawResponse);
    }
}
