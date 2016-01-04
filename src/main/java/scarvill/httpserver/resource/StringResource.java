package scarvill.httpserver.resource;

public class StringResource implements Resource {
    private String data;

    public StringResource(String data) {
        this.data = data;
    }

    @Override
    public byte[] getData() {
        return data.getBytes();
    }

    @Override
    public void setData(byte[] data) {
        if (data != null) {
            this.data = new String(data);
        } else {
            this.data = "";
        }
    }
}