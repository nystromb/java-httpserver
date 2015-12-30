package scarvill.httpserver.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpResponse {

    public byte[] generate(Response response) {
        try {
            return assembleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] assembleResponse(Response response) throws IOException {
        ByteArrayOutputStream rawResponse = new ByteArrayOutputStream();

        rawResponse.write(statusAndHeaders(response));
        rawResponse.write(response.getBody());

        return rawResponse.toByteArray();
    }


    private byte[] statusAndHeaders(Response response) {
        String statusAndHeaders = "HTTP/1.1 " + response.getStatus().toString() + "\r\n";

        for (String name : response.getHeaderNames()) {
            statusAndHeaders += name + ": " + response.getHeaderContent(name) + "\r\n";
        }

        return (statusAndHeaders + "\r\n").getBytes();
    }
}
