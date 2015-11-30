package scarvill.httpserver;

import java.net.Socket;

public interface Serveable extends Runnable {
    Serveable serve(Socket clientSocket);
}
