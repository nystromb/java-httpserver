package scarvill.httpserver;

import java.io.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class HTTPService implements BiConsumer<InputStream, OutputStream> {
    private Router router;

    public HTTPService(Router router) {
        this.router = router;
    }

    public void accept(InputStream inputStream, OutputStream outputStream) {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter out = new PrintWriter(outputStream, true);

        try {
            Request request = new Request(in.readLine());
            Response response = router.routeRequest(request);
            out.print(response.generate());
            out.close();
        } catch (IOException e) { throw new RuntimeException(e); }
    }
}
