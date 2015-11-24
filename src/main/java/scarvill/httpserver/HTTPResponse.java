package scarvill.httpserver;

import scarvill.httpserver.constants.Status;
import scarvill.httpserver.constants.StatusTwo;

public class HTTPResponse {

    public String generate(Response response) {
        String generatedResponse = statusToStatusLine(response.getStatus());
        for (String header : response.getHeaders()) {
            generatedResponse = generatedResponse.concat(header);
        }
        generatedResponse = generatedResponse.concat("\r\n");
        generatedResponse = generatedResponse.concat(response.getBody());

        return generatedResponse;
    }

    private String statusToStatusLine(StatusTwo status) {
        switch (status) {
            case OK: return Status.OK;
            case NOT_FOUND: return Status.NOT_FOUND;
            case METHOD_NOT_ALLOWED: return Status.METHOD_NOT_ALLOWED;
            case FOUND: return Status.FOUND;
            default: return null;
        }
    }
}
