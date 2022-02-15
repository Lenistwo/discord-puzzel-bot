package pl.lenistwo.puzzlebot.exception.channel;

public class InvalidChannelException extends RuntimeException {
    public InvalidChannelException(String channelId) {
        super(String.format("Channel with id %s doesn't exist or is voice channel", channelId));
    }
}
