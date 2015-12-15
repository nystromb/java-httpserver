package scarvill.httpserver;

public interface ServerConfiguration {
    int getPort();
    String getPublicDirectory();
    Serveable getService();
    void serverTearDown();
}
