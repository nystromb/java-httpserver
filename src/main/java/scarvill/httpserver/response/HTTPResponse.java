package scarvill.httpserver.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HTTPResponse {

    public byte[] generate(Response response) {
        try {
            return assembleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] assembleResponse(Response response) throws IOException {
        ByteArrayOutputStream rawResponseByteStream = new ByteArrayOutputStream();

        rawResponseByteStream.write(statusToString(response.getStatus()).getBytes());
        for (String header : response.getHeaders()) {
            rawResponseByteStream.write(header.getBytes());
        }
        rawResponseByteStream.write("\r\n".getBytes());
        rawResponseByteStream.write(response.getBody());

        return rawResponseByteStream.toByteArray();
    }


    private String statusToString(Status status) {
        return "HTTP/1.1 " + status.toString() + "\r\n";
    }
}
