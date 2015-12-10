package scarvill.httpserver.cobspec.route_strategies;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;
import scarvill.httpserver.routes.Resource;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.function.Function;

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
        return new ResponseBuilder().setStatus(Status.OK)
            .setHeader("Content-Length", String.valueOf(resource.getData().length))
            .setBody(resource.getData())
            .build();
    }

    private Response partialResourceResponse(String rangeInformation) {
        byte[] partialContent = readPartialResourceData(rangeInformation);

        return new ResponseBuilder().setStatus(Status.PARTIAL_CONTENT)
            .setHeader("Content-Length", String.valueOf(partialContent.length))
            .setBody(partialContent)
            .build();
    }

    private byte[] readPartialResourceData(String rangeInformation) {
        int startIndex = resourceReadStartIndex(rangeInformation);
        int endIndex = resourceReadEndIndex(rangeInformation);
        return Arrays.copyOfRange(resource.getData(), startIndex, endIndex + 1);
    }

    private int resourceReadStartIndex(String rangeInformation) {
        if (rangeStartIsSpecified(rangeInformation)) {
            return rangeStart(rangeInformation);
        } else {
            return resource.getData().length - rangeEnd(rangeInformation);
        }
    }

    private int resourceReadEndIndex(String rangeInformation) {
        int resourceDataLength = resource.getData().length;
        int endIndex = resourceDataLength - 1;

        if (rangeStartIsSpecified(rangeInformation) && rangeEndIsSpecified(rangeInformation)) {
            endIndex = Integer.min(resourceDataLength - 1, rangeEnd(rangeInformation));
        }

        return endIndex;
    }

    private boolean rangeStartIsSpecified(String rangeInformation) {
        return !rangeInformation.split("=")[1].split("-")[0].equals("");
    }

    private boolean rangeEndIsSpecified(String rangeInformation) {
        return rangeInformation.split("=")[1].split("-").length > 1;
    }

    private int rangeStart(String rangeInformation) {
        return Integer.parseInt(rangeInformation.split("=")[1].split("-")[0]);
    }

    private int rangeEnd(String rangeInformation) {
        return Integer.parseInt(rangeInformation.split("=")[1].split("-")[1]);
    }
}
