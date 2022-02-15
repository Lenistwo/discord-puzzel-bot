package pl.lenistwo.puzzlebot.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;


@NoArgsConstructor(access = AccessLevel.NONE)
public class MessageBuilder {

    public static MessageEmbed buildAddPointsMessage(String playerName, int amount) {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle(String.format("Gracz %s otrzymał %s punktów", playerName, amount));
        return embedBuilder.build();
    }

    public static MessageEmbed buildTakePointsMessage(String playerName, int amount) {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle(String.format("Gracz %s otrzymał %s punktów", playerName, amount));
        return embedBuilder.build();
    }

    public static MessageEmbed buildPointsMessage(String playerName, int amount) {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setTitle(String.format("Gracz %s obecnie posiada %s punktów", playerName, amount));
        return embedBuilder.build();
    }

    public static MessageEmbed buildErrorMessage(String errorMessage) {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle(errorMessage);
        return embedBuilder.build();
    }

    public static MessageEmbed buildRiddleMessage(String answer, String riddle) {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle(answer);
        embedBuilder.setDescription(riddle);
        return embedBuilder.build();
    }

    public static MessageEmbed buildGuessesMessage() {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setTitle("Zgadnij litere lub haslo");
        return embedBuilder.build();
    }

    public static MessageEmbed buildGuessLetterMessage() {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setTitle("Zgadnij litere");
        return embedBuilder.build();
    }

    public static MessageEmbed buildGuessAnswerMessage() {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setTitle("Zgadnij haslo");
        return embedBuilder.build();
    }

    public static MessageEmbed buildBuyLetterReplySuccessMessage() {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle(String.format("Aby Zgadnac litere wyslij %s oraz litere", BotConstants.COMMAND_PREFIX + "litera"));
        return embedBuilder.build();
    }

    public static MessageEmbed buildBuyAnswerReplySuccessMessage() {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle(String.format("Aby Zgadnac odpowiedz wyslij %s oraz haslo", BotConstants.COMMAND_PREFIX + "odpowiedz"));
        return embedBuilder.build();
    }

    public static MessageEmbed buildBuyReplyFailureMessage() {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("Niestety nie posiadasz wystarczajacej ilosci punktow");
        return embedBuilder.build();
    }

    public static MessageEmbed buildResignMessage() {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Rezygnuje");
        return embedBuilder.build();
    }

    public static MessageEmbed buildWinnerMessage(String playerName) {
        var embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle(String.format("Gracz %s zgadl odpowiedz!", playerName));
        return embedBuilder.build();
    }
}
