package pl.lenistwo.puzzlebot.service.message;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Component;
import org.springframework.stereotype.Service;
import pl.lenistwo.puzzlebot.exception.channel.InvalidChannelException;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final JDA jda;

    public void sendMessage(String channelId, MessageEmbed message) {
        var textChannel = retrieveTextChannel(channelId);
        textChannel.sendMessage(message).queue();
    }

    public void sendMessage(String channelId, MessageEmbed message, Component... components) {
        var textChannel = retrieveTextChannel(channelId);
        textChannel.sendMessage(message).setActionRow(components).queue();
    }

    public String sendMessageAndGetID(String channelId, MessageEmbed message) {
        var textChannel = retrieveTextChannel(channelId);
        var complete = textChannel.sendMessage(message).complete();
        return complete.getId();
    }

    public void editMessage(String channelId, String messageId, MessageEmbed messageEmbed) {
        var textChannel = retrieveTextChannel(channelId);
        textChannel.editMessageById(messageId, messageEmbed).queue();
    }

    public void deleteMessageById(String channelId, String messageId) {
        var textChannel = retrieveTextChannel(channelId);
        textChannel.deleteMessageById(messageId).queue();
    }

    private TextChannel retrieveTextChannel(String channelId) {
        var channel = jda.getGuildChannelById(channelId);

        if (channel instanceof TextChannel textChannel) {
            return textChannel;
        }
        throw new InvalidChannelException(channelId);
    }

}
