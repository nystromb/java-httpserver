package scarvill.httpserver;

import java.io.*;
import java.util.function.BiConsumer;

public class HTTPService implements BiConsumer<InputStream, OutputStream> {
    private Router router;
    private Logger logger;

    public HTTPService(Router router, Logger logger) {
        this.router = router;
        this.logger = logger;
    }

    public void accept(InputStream inputStream, OutputStream outputStream) {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter out = new PrintWriter(outputStream, true);

        try {
            Request request = parseRequest(in);
            sendResponse(out, request);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request parseRequest(BufferedReader in) throws IOException {
        String rawRequest = readRequest(in);
        logger.logRequest(rawRequest);
        return new Request(rawRequest);
    }

    private String readRequest(BufferedReader in) throws IOException {
        String rawRequest = in.readLine();
        do rawRequest = rawRequest.concat(Character.toString((char)in.read())); while (in.ready());

        return rawRequest;
    }

    private void sendResponse(PrintWriter out, Request request) {
        Response response = router.routeRequest(request);
        logger.logResponse(response.generate());
        out.print(response.generate());
    }
}
