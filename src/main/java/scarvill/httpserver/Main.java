package scarvill.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        int portNumber = parsePortNumber(args);
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("HTTPServer started...\nport: " + portNumber + "\n");

        serve(serverSocket);
    }

    private static int parsePortNumber(String[] args) {
        return (args.length != 1) ? 5000 : Integer.parseInt(args[0]);
    }

    private static void serve(ServerSocket serverSocket) throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            Request request = receiveRequest(in.readLine());
            String response = Dispatch.handle(request);
            sendResponse(response, out);
            out.close();
        }
    }

    private static Request receiveRequest(String rawRequest) {
        System.out.println("Received request: " + rawRequest);
        return new Request(rawRequest);
    }

    private static void sendResponse(String response, OutputStream out) throws IOException {
        System.out.println("Sending response: " + response);
        out.write(response.getBytes());
    }
}

