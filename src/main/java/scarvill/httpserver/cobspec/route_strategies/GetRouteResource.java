package scarvill.httpserver.cobspec.route_strategies;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routes.Resource;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

public class GetRouteResource implements Function<Request, Response> {
    private Resource resource;

    public GetRouteResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Response apply(Request request) {
        if (request.getHeaders().containsKey("Range")) {
            return partialResourceResponse(request.getHeaders().get("Range"));
        } else {
            return entireResourceResponse();
        }
    }

    private Response  entireResourceResponse() {
        String contentLength = "Content-Length: " + resource.getData().length + "\r\n";
        return new ResponseBuilder().setStatus(Status.OK)
            .setHeaders(new String[]{contentLength})
            .setBody(resource.getData())
            .build();
    }

    private Response partialResourceResponse(String rangeInformation) {
        int start = startIndex(rangeInformation);
        int end = endIndex(rangeInformation);
        byte[] partialContent = Arrays.copyOfRange(resource.getData(), start, end + 1);
        String contentLength = "Content-Length: " + partialContent.length + "\r\n";

        return new ResponseBuilder().setStatus(Status.PARTIAL_CONTENT)
            .setHeaders(new String[]{contentLength})
            .setBody(partialContent)
            .build();
    }

    private int startIndex(String rangeInformation) {
        String startIndexString = rangeInformation.split("=")[1].split("-")[0];

        if (startIndexString == "") {
            startIndexString = "0";
        }

        return Integer.parseInt(startIndexString);
    }

    private int endIndex(String rangeInformation) {
        String[] startAndEndIndexes = rangeInformation.split("=")[1].split("-");
        int endIndex = resource.getData().length - 1;

        if (startAndEndIndexes.length > 1) {
            endIndex = Integer.parseInt(startAndEndIndexes[1]);
        }

        return endIndex;
    }
}
