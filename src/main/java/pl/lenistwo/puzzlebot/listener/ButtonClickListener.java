package pl.lenistwo.puzzlebot.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.lenistwo.puzzlebot.exception.player.NotEnoughPointsException;
import pl.lenistwo.puzzlebot.model.button.Action;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.puzzel.PuzzelQueueService;
import pl.lenistwo.puzzlebot.util.MessageBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
public class ButtonClickListener extends ListenerAdapter {

    private static final String RESIGN_BUTTON_LABEL = "Zrezygnuj";
    private static final String BUY_ANSWER_GUESS_BUTTON_LABEL = "Kup mozliwosc odpowiedzi";
    private static final String BUY_LETTER_GUESS_BUTTON_LABEL = "Kup mozliwosc zgadniecia litery";
    private static final boolean EPHEMERAL = true;

    private final Map<Action, Consumer<ButtonClickEvent>> actions;
    private final PuzzelQueueService puzzelQueueService;
    private final RestService restService;

    public ButtonClickListener(PuzzelQueueService puzzelQueueService, RestService restService) {
        this.actions = new HashMap<>() {{
            put(Action.GUESS_LETTER, guessLetter());
            put(Action.GUESS_ANSWER, guessAnswer());
            put(Action.CONFIRM_LETTER_GUESS, confirmLetterGuess());
            put(Action.CONFIRM_ANSWER_GUESS, confirmAnswerGuess());
            put(Action.RESIGN, resign());
        }};
        this.puzzelQueueService = puzzelQueueService;
        this.restService = restService;
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        var componentId = event.getComponentId();
        actions.getOrDefault(Action.getById(componentId), (e) -> log.warn("Action Not Found: {}", componentId)).accept(event);
    }

    private Consumer<ButtonClickEvent> guessLetter() {
        return buttonClickEvent -> {
            var buy = Button.success(Action.CONFIRM_LETTER_GUESS.getId(), BUY_LETTER_GUESS_BUTTON_LABEL);
            var resign = Button.danger(Action.RESIGN.getId(), RESIGN_BUTTON_LABEL);
            replyToUser(buttonClickEvent, MessageBuilder.buildGuessLetterMessage(), buy, resign);
        };
    }

    private Consumer<ButtonClickEvent> guessAnswer() {
        return buttonClickEvent -> {
            var buy = Button.success(Action.CONFIRM_ANSWER_GUESS.getId(), BUY_ANSWER_GUESS_BUTTON_LABEL);
            var resign = Button.danger(Action.RESIGN.getId(), RESIGN_BUTTON_LABEL);
            replyToUser(buttonClickEvent, MessageBuilder.buildGuessAnswerMessage(), buy, resign);
        };
    }

    private Consumer<ButtonClickEvent> confirmLetterGuess() {
        return buttonClickEvent -> {
            checkPlayerPoints(buttonClickEvent);

            puzzelQueueService.addAllowedPlayer(buttonClickEvent.getUser().getId(), Action.GUESS_LETTER);
            replyToUser(buttonClickEvent, MessageBuilder.buildBuyLetterReplySuccessMessage());
        };
    }

    private Consumer<ButtonClickEvent> confirmAnswerGuess() {
        return buttonClickEvent -> {
            checkPlayerPoints(buttonClickEvent);

            puzzelQueueService.addAllowedPlayer(buttonClickEvent.getUser().getId(), Action.GUESS_ANSWER);
            replyToUser(buttonClickEvent, MessageBuilder.buildBuyAnswerReplySuccessMessage());
        };
    }

    private Consumer<ButtonClickEvent> resign() {
        return buttonClickEvent -> replyToUser(buttonClickEvent, MessageBuilder.buildResignMessage());
    }

    private boolean haveEnoughPoints(String playerName) {
        var playerPoints = restService.getPlayerPoints(playerName);
        return playerPoints.points() >= puzzelQueueService.getGuessCost();
    }

    private void replyToUser(@NotNull ButtonClickEvent event, MessageEmbed message) {
        event.replyEmbeds(message).setEphemeral(EPHEMERAL).queue();
    }

    private void replyToUser(@NotNull ButtonClickEvent event, MessageEmbed message, net.dv8tion.jda.api.interactions.components.Component... components) {
        event.replyEmbeds(message).addActionRow(components).setEphemeral(EPHEMERAL).queue();
    }

    private void checkPlayerPoints(ButtonClickEvent buttonClickEvent) {
        var playerName = buttonClickEvent.getUser().getName();
        if (haveEnoughPoints(playerName)) {
            var playerPoints = restService.getPlayerPoints(playerName);
            restService.updatePlayerPoints(playerName, playerPoints.points() - puzzelQueueService.getGuessCost());
            return;
        }
        replyToUser(buttonClickEvent, MessageBuilder.buildBuyReplyFailureMessage());
        throw new NotEnoughPointsException(playerName);

    }
}
