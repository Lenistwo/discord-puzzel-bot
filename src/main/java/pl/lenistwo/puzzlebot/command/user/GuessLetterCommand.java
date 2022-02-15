package pl.lenistwo.puzzlebot.command.user;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import pl.lenistwo.puzzlebot.command.api.Command;
import pl.lenistwo.puzzlebot.exception.action.PlayerIsNotAllowedToActionException;
import pl.lenistwo.puzzlebot.model.button.Action;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;
import pl.lenistwo.puzzlebot.service.puzzel.PuzzelQueueService;
import pl.lenistwo.puzzlebot.util.BotConstants;
import pl.lenistwo.puzzlebot.util.MessageBuilder;

@Slf4j
public class GuessLetterCommand extends Command {

    private static final int FIRST_INDEX = 0;

    private final PuzzelQueueService puzzelQueueService;

    public GuessLetterCommand(RestService restService, MessageService messageService, PuzzelQueueService puzzelQueueService) {
        super(BotConstants.COMMAND_PREFIX + "litera", 1, restService, messageService);
        this.puzzelQueueService = puzzelQueueService;
    }

    @Override
    public void execute(String[] args, String channelId, Member member) {
        var character = Character.valueOf(args[FIRST_INDEX].toUpperCase().toCharArray()[FIRST_INDEX]);
        log.info("Player {} guessing letter: {}", member.getEffectiveName(), character);

        if (!puzzelQueueService.isPlayerAllowedToGuess(member.getId(), Action.GUESS_LETTER)) {
            log.warn("Player {} is not allowed to guess letter", member.getEffectiveName());
            throw new PlayerIsNotAllowedToActionException(Action.GUESS_LETTER);
        }

        restService.updatePlayerPoints(member.getEffectiveName(), puzzelQueueService.getGuessCost());
        puzzelQueueService.removePlayer(member.getId());

        if (!puzzelQueueService.guessLetter(character)) {
            return;
        }

        messageService.editMessage(channelId,
                                   puzzelQueueService.getMessageId(),
                                   MessageBuilder.buildRiddleMessage(puzzelQueueService.getGuessedAnswer(),
                                                                     puzzelQueueService.getPuzzel().riddle()));
    }
}
