package scarvill.httpserver.server;

import scarvill.httpserver.request.HttpRequest;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HttpResponse;
import scarvill.httpserver.response.Response;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServerIO {
    public Request readRequest(BufferedReader in) throws IOException, HttpRequest.IllFormedRequest {
        String requestLineAndHeaders = readRequestLineAndHeaders(in);
        byte[] body = readBody(in);

        return new HttpRequest(requestLineAndHeaders, body).parse();
    }

    public void writeResponse(OutputStream out, Response response) throws IOException {
        byte[] rawResponse = new HttpResponse().generate(response);

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
