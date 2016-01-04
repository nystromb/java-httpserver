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

// Name of class I think could be better named. You use the Response object technically as your "HttpResponse"

// += operator could be changed to a StringBuffer for efficiency

// assembleResponse() could be similar to a toString() function and placed in the Response object
// as a way of formatting your response