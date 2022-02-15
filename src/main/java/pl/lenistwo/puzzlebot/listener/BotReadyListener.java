package pl.lenistwo.puzzlebot.listener;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.lenistwo.puzzlebot.config.BotProperties;
import pl.lenistwo.puzzlebot.model.button.Action;
import pl.lenistwo.puzzlebot.service.message.MessageService;
import pl.lenistwo.puzzlebot.service.puzzel.PuzzelQueueService;
import pl.lenistwo.puzzlebot.util.MessageBuilder;

@Component
@RequiredArgsConstructor
public class BotReadyListener extends ListenerAdapter {

    private final BotProperties botProperties;
    private final MessageService messageService;
    private final PuzzelQueueService puzzelQueueService;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        drawPuzzel();
    }

    private void drawPuzzel() {
        var puzzel = puzzelQueueService.newPuzzel();
        var messageID = messageService.sendMessageAndGetID(botProperties.riddleChannelId(),
                                                                 MessageBuilder.buildRiddleMessage(puzzelQueueService.getGuessedAnswer(),
                                                                                                   puzzel.riddle()));
        puzzelQueueService.setMessageId(messageID);

        var letterButton = Button.primary(Action.GUESS_LETTER.getId(), "Zgadnij litere");
        var answerButton = Button.primary(Action.GUESS_ANSWER.getId(), "Zgadnij odpowiedz");

        messageService.sendMessage(botProperties.riddleChannelId(),
                                   MessageBuilder.buildGuessesMessage(),
                                   letterButton,
                                   answerButton);
    }
}
