package scarvill.httpserver;

import scarvill.httpserver.request.HTTPRequest;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HTTPResponse;
import scarvill.httpserver.response.Response;

import java.io.*;

public class ServerIO {
    public Request readRequest(BufferedReader in) throws IOException {
        String requestLineAndHeaders = readRequestLineAndHeaders(in);
        byte[] body = readBody(in);

        return new HTTPRequest(requestLineAndHeaders, body).parse();
    }

    public void writeResponse(OutputStream out, Response response) throws IOException {
        byte[] rawResponse = new HTTPResponse().generate(response);

        out.write(rawResponse, 0, rawResponse.length);
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
}
