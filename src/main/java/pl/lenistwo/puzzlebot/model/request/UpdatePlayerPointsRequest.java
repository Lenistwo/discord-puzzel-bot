package pl.lenistwo.puzzlebot.model.request;

public record UpdatePlayerPointsRequest(String playerName, int amount) {
}
