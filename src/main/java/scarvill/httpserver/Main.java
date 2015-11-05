package scarvill.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("HTTPServer started...\nport: 5000\n");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            HTTPRequest request = receiveRequest(in.readLine());
            String response = generateResponse(request);
            sendResponse(response, out);
            out.close();
        }
    }

    private static HTTPRequest receiveRequest(String rawRequest) {
        System.out.println("Received request: " + rawRequest);
        return new HTTPRequest(rawRequest);
    }

    private static String generateResponse(HTTPRequest request) {
        if (request.getURI().equals("/foobar")) {
            return HTTPStatus.NOT_FOUND;
        } else {
            return HTTPStatus.OK;
        }
    }

    private static void sendResponse(String response, OutputStream out) throws IOException {
        System.out.println("Sending response: " + response);
        out.write(response.getBytes());
    }
}

