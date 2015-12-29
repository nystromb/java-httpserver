package scarvill.httpserver.server;

public interface ServerConfiguration {
    int getPort();

    String getPublicDirectory();

    Serveable getService();
}
