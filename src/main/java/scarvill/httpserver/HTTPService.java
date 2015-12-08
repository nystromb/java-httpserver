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
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        Request request = readRequest(in);
        Response response = router.routeRequest(request);
        sendResponse(out, response);
        out.close();
    }

    private Request readRequest(BufferedReader in) throws IOException {
        String statusAndHeaders = readStatusAndHeaders(in);
        byte[] body = readBody(in);
        logger.logRequest(statusAndHeaders);
        return new HTTPRequest(statusAndHeaders + new String(body)).parse();
    }

    private String readStatusAndHeaders(BufferedReader in) throws IOException {
        String statusAndHeaders = "";
        while (!statusAndHeaders.contains("\r\n\r\n")) {
            statusAndHeaders += in.readLine() + "\r\n";
        }
        return statusAndHeaders;
    }

    private byte[] readBody(BufferedReader in) throws IOException {
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        while (in.ready()) {
            body.write(in.read());
        }
        return body.toByteArray();
    }

    private void sendResponse(PrintWriter out, Response response) {
        byte[] rawResponse = new HTTPResponse().generate(response);
        logger.logResponse(new String(rawResponse));
        out.print(new String(rawResponse));
    }
}
