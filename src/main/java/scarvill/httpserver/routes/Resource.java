package scarvill.httpserver.routes;

public class Resource {
    private String data;

    public Resource(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
