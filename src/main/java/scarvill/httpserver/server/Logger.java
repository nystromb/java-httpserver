package scarvill.httpserver.server;

import scarvill.httpserver.request.Request;
import scarvill.httpserver.response.Response;

import java.io.PrintStream;

public class Logger {
    private PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public void logRequest(Request request) {
        out.println("*** Received Request ***");
        out.println("MethodLine: "
            + request.getMethod().toString() + " "
            + request.getURI() + " HTTP/1.1");
        out.print("Parameters:");
        request.getParameterNames().forEach(
            (parameter) -> out.print(" " + parameter + "=" + request.getParameterValue(parameter)));
        out.println();
        out.println("Headers:");
        request.getHeaderNames().forEach(
            (name) -> out.println("- " + name + ": " + request.getHeaderContent(name)));
        out.println("Body-length: " + request.getBody().length);
        out.println();
    }

    public void logResponse(Response response) {
        out.println("*** Sent Response ***");
        out.println("Status: " + response.getStatus().toString());
        out.println("Headers:");
        response.getHeaderNames().forEach(
            (name) -> out.println("- " + name + ": " + response.getHeaderContent(name)));
        out.println("Body-length: " + response.getBody().length);
        out.println();
    }

    public void logException(String message) {
        out.println("*** Server Exception ***");
        out.println(message);
    }
}

// I really like the formatting of your logs so that they are easy to read and understand, but from what I read
// the built-in java logger is better performace wise (might not matter in this case) and it contains other useful debug information such as:
// 1. the class/method the message is logged
// 2. time of logging
// 3. severity level

