package scarvill.httpserver.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HTTPResponse {

    public String generate(Response response) {
        String generatedResponse = statusToString(response.getStatus());
        for (String header : response.getHeaders()) {
            generatedResponse = generatedResponse.concat(header);
        }
        generatedResponse = generatedResponse.concat("\r\n");
        generatedResponse = generatedResponse.concat(response.getBody());

        return generatedResponse;
    }

    public byte[] generateBytes(Response response) {
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
        rawResponseByteStream.write(response.getBody().getBytes());

        return rawResponseByteStream.toByteArray();
    }


    private String statusToString(Status status) {
        return "HTTP/1.1 " + status.toString() + "\r\n";
    }
}
