package scarvill.httpserver;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            Request request = readRequest(in);
            sendResponse(out, request);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request readRequest(BufferedReader in) throws IOException {
        String rawRequest = in.readLine() + "\r\n";
        while (in.ready()) {
            rawRequest = rawRequest.concat(Character.toString((char) in.read()));
        }
        logger.logRequest(rawRequest);
        return new HTTPRequest(rawRequest).parse();
    }

    private void sendResponse(PrintWriter out, Request request) {
        String rawResponse = new HTTPResponse().generate(router.routeRequest(request));
        logger.logResponse(rawResponse);
        out.print(rawResponse);
    }
}
