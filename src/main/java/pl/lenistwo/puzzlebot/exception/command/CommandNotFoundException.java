package pl.lenistwo.puzzlebot.exception.command;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException(String command) {
        super("Command Not Found " + command);
    }
}
