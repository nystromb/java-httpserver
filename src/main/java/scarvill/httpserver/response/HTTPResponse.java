package scarvill.httpserver.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class HTTPResponse {

    public byte[] generate(Response response) {
        try {
            return assembleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] assembleResponse(Response response) throws IOException {
        ByteArrayOutputStream rawResponse = new ByteArrayOutputStream();

        rawResponse.write(statusAndHeaders(response).getBytes());
        rawResponse.write(response.getBody());

        return rawResponse.toByteArray();
    }


    private String statusAndHeaders(Response response) {
        String statusAndHeaders = "HTTP/1.1 " + response.getStatus().toString() + "\r\n";

        for (Map.Entry<String, String > header : response.getHeaders().entrySet()) {
            statusAndHeaders += header.getKey() + ": " + header.getValue() + "\r\n";
        }

        return statusAndHeaders + "\r\n";
    }
}
