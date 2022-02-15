package pl.lenistwo.puzzlebot.exception.command;

public class CommandNotAllowedException extends RuntimeException {
    public CommandNotAllowedException(String command, String user) {
        super(String.format("Command %s Is Not Allowed For User %s", command, user));
    }
}
