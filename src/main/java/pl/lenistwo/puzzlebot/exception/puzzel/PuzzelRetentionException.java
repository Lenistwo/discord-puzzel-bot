package pl.lenistwo.puzzlebot.exception.puzzel;

public class PuzzelRetentionException extends RuntimeException {
    public PuzzelRetentionException() {
        super("There Is Not Enough Puzzels To Draw Next Riddle");
    }
}
