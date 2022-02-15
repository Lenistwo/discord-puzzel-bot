package pl.lenistwo.puzzlebot.exception.api;

public class ApiCallException extends RuntimeException {
    public ApiCallException(String requestMethod, String URL) {
        super(String.format("Api Call Exception For %s URL %s", requestMethod, URL));
    }
}
