package pl.lenistwo.puzzlebot.exception.action;

public class ActionNotFoundException extends RuntimeException {
    public ActionNotFoundException(String message) {
        super("Action Not Found Exception: " + message);
    }
}
