package scarvill.httpserver.request;

public enum Method {
    GET("GET"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    NULL_METHOD("UNRECOGNIZED_METHOD");

    private final String methodString;

    Method(String methodString) {
        this.methodString = methodString;
    }

    public String toString() {
        return this.methodString;
    }
}
