package scarvill.httpserver.server;

import org.junit.Test;
import scarvill.httpserver.request.Method;
import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.HttpResponse;
import scarvill.httpserver.response.Response;
import scarvill.httpserver.response.ResponseBuilder;
import scarvill.httpserver.response.Status;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class ServerIOTest {
    @Test
    public void testReadsRequestFromStream() throws IOException {
        BufferedReader in =
            new BufferedReader(
                new InputStreamReader(
                    new ByteArrayInputStream("GET / HTTP/1.1\r\n\r\n".getBytes())));

        Request request = new ServerIO().readRequest(in);

        assertEquals(Method.GET, request.getMethod());
        assertEquals("/", request.getURI());
    }

    @Test
    public void testWritesResponseToStream() throws IOException {
        Response expectedResponse = new ResponseBuilder()
            .setStatus(Status.OK)
            .setHeader("Header", "a header")
            .setBody("body".getBytes())
            .build();
        String expectedResponseString = new String(new HttpResponse().generate(expectedResponse));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        new ServerIO().writeResponse(outputStream, expectedResponse);

        assertEquals(expectedResponseString, new String(outputStream.toByteArray()));
    }
}
