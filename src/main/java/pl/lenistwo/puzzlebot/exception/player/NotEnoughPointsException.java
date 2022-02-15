package pl.lenistwo.puzzlebot.exception.player;

public class NotEnoughPointsException extends RuntimeException {
    public NotEnoughPointsException(String playerName) {
        super(String.format("Player %s doesnt have enough points", playerName));
    }
}
