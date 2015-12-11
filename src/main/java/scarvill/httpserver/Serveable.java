package scarvill.httpserver;

import java.net.Socket;

public interface Serveable {
    Runnable serve(Socket clientSocket);
}
