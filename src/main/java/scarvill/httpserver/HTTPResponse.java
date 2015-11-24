package scarvill.httpserver;

public class HTTPResponse {

    public String generate(Response response) {
        String generatedResponse = response.getStatusLine();
        for (String header : response.getHeaders()) {
            generatedResponse = generatedResponse.concat(header);
        }
        generatedResponse = generatedResponse.concat("\r\n");
        generatedResponse = generatedResponse.concat(response.getBody());

        return generatedResponse;
    }
}
