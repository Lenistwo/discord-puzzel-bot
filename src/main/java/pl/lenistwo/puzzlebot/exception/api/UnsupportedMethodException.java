package pl.lenistwo.puzzlebot.exception.api;

public class UnsupportedMethodException extends RuntimeException {
    public UnsupportedMethodException(String URL) {
        super(String.format("Unsupported Method Exception For URL %s", URL));
    }
}
