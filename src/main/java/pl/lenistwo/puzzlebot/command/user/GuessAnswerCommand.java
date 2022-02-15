package pl.lenistwo.puzzlebot.command.user;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.Button;
import pl.lenistwo.puzzlebot.command.api.Command;
import pl.lenistwo.puzzlebot.exception.action.PlayerIsNotAllowedToActionException;
import pl.lenistwo.puzzlebot.model.button.Action;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;
import pl.lenistwo.puzzlebot.service.puzzel.PuzzelQueueService;
import pl.lenistwo.puzzlebot.util.BotConstants;
import pl.lenistwo.puzzlebot.util.MessageBuilder;

@Slf4j
public class GuessAnswerCommand extends Command {

    private final PuzzelQueueService puzzelQueueService;

    public GuessAnswerCommand(RestService restService, MessageService messageService, PuzzelQueueService puzzelQueueService) {
        super(BotConstants.COMMAND_PREFIX + "odpowiedz", 1, restService, messageService);
        this.puzzelQueueService = puzzelQueueService;
    }

    @Override
    public void execute(String[] args, String channelId, Member member) {
        var guess = args[0];
        log.info("Player {} guess: {}", member.getEffectiveName(), guess);

        if (!puzzelQueueService.isPlayerAllowedToGuess(member.getId(), Action.GUESS_ANSWER)) {
            log.warn("Player {} is not allowed to guess answer", member.getEffectiveName());
            throw new PlayerIsNotAllowedToActionException(Action.GUESS_ANSWER);
        }

        restService.updatePlayerPoints(member.getEffectiveName(), puzzelQueueService.getGuessCost());
        puzzelQueueService.removePlayer(member.getId());

        if (!puzzelQueueService.guessAnswer(guess)) {
            return;
        }

        messageService.editMessage(channelId, puzzelQueueService.getMessageId(), MessageBuilder.buildRiddleMessage(puzzelQueueService.getPuzzel().answer(), puzzelQueueService.getPuzzel().riddle()));

        var messageID = messageService.sendMessageAndGetID(channelId, MessageBuilder.buildWinnerMessage(member.getEffectiveName()));
        restService.saveWinner(member.getEffectiveName(), puzzelQueueService.getPuzzel().id());

        sendPuzzel(channelId, messageID);
    }

    private void sendPuzzel(String channelId, String messageID) {
        puzzelQueueService.setPuzzel(puzzelQueueService.newPuzzel());
        puzzelQueueService.setMessageId(messageID);

        var letterButton = Button.primary(Action.GUESS_LETTER.getId(), "Zgadnij litere");
        var answerButton = Button.primary(Action.GUESS_ANSWER.getId(), "Zgadnij odpowiedz");
        messageService.sendMessage(channelId, MessageBuilder.buildGuessesMessage(), letterButton, answerButton);
    }
}
