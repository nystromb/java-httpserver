package scarvill.httpserver;

import java.io.PrintStream;

public class Logger {
    private PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public void logRequest(String request) {
        out.println("Received request:");
        out.println(request);
    }

    public void logResponse(String response) {
        out.println("Sent response:");
        out.println(response);
    }
}
