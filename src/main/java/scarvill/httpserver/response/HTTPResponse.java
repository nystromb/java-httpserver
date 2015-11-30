package scarvill.httpserver.response;

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

    private String statusToString(Status status) {
        return "HTTP/1.1 " + status.toString() + "\r\n";
    }
}
