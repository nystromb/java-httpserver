package scarvill.httpserver.routing;

public interface Resource {
    byte[] getData();

    void setData(byte[] data);
}
