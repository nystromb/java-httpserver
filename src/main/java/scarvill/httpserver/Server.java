package scarvill.httpserver;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.BiConsumer;

public class Server {
    private ServerSocket serverSocket;
    private BiConsumer<InputStream, OutputStream> service;

    public Server(int port, BiConsumer<InputStream, OutputStream> service) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.service = service;
    }

    public boolean isRunning() {
        return !serverSocket.isClosed();
    }

    public InetAddress getInetAddress() {
        return serverSocket.getInetAddress();
    }

    public int getLocalPort() {
        return serverSocket.getLocalPort();
    }

    public void start() {
        while(!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                service.accept(clientSocket.getInputStream(), clientSocket.getOutputStream());
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    public void stop() {
        try { serverSocket.close(); }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}
