package scarvill.httpserver;

import scarvill.httpserver.routes.Router;
import scarvill.httpserver.request.HTTPRequest;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HTTPResponse;
import scarvill.httpserver.response.Response;

import java.io.*;
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
        BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());

        Request request = readRequest(in);
        Response response = router.routeRequest(request);
        sendResponse(out, response);

        out.close();
    }

    private Request readRequest(BufferedReader in) throws IOException {
        String requestLineAndHeaders = readRequestLineAndHeaders(in);
        byte[] body = readBody(in);

        logger.logRequest(requestLineAndHeaders + new String(body));

        return new HTTPRequest(requestLineAndHeaders, body).parse();
    }

    private String readRequestLineAndHeaders(BufferedReader in) throws IOException {
        String requestLineAndHeaders = "";

        while (!requestLineAndHeaders.contains("\r\n\r\n")) {
            requestLineAndHeaders += in.readLine() + "\r\n";
        }

        return requestLineAndHeaders;
    }

    private byte[] readBody(BufferedReader in) throws IOException {
        ByteArrayOutputStream body = new ByteArrayOutputStream();

        while (in.ready()) {
            body.write(in.read());
        }

        return body.toByteArray();
    }

    private void sendResponse(OutputStream out, Response response) throws IOException {
        byte[] rawResponse = new HTTPResponse().generate(response);

        logger.logResponse(new String(rawResponse));

        out.write(rawResponse, 0, rawResponse.length);
    }
}
