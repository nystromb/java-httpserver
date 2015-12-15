package scarvill.httpserver.server;

import java.net.Socket;

public interface Serveable {
    Runnable serve(Socket clientSocket);
}
