package scarvill.httpserver;

import java.io.*;
import java.util.function.BiConsumer;

public class HTTPService implements BiConsumer<InputStream, OutputStream> {
    private Router router;

    public HTTPService(Router router) {
        this.router = router;
    }

    public void accept(InputStream inputStream, OutputStream outputStream) {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter out = new PrintWriter(outputStream, true);

        try {
            String rawRequest = readRequest(in);
            System.out.print("Received request:\r\n" + rawRequest + "\r\n");
            Request request = new Request(rawRequest);

            Response response = router.routeRequest(request);
            System.out.print("Sent response:\r\n" + response.generate() + "\r\n");
            out.print(response.generate());

            out.close();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private String readRequest(BufferedReader in) throws IOException {
        String rawRequest = in.readLine();
        do rawRequest = rawRequest.concat(Character.toString((char)in.read())); while (in.ready());

        return rawRequest;
    }
}
