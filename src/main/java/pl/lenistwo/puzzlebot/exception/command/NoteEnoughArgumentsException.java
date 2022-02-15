package pl.lenistwo.puzzlebot.exception.command;

public class NoteEnoughArgumentsException extends RuntimeException {
    public NoteEnoughArgumentsException() {
        super("Not Enough Arguments In Command");
    }
}
