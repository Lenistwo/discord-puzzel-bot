package pl.lenistwo.puzzlebot.command.api;

import lombok.extern.slf4j.Slf4j;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;

@Slf4j
public abstract class AdminCommand extends Command {

    public AdminCommand(String command, int minArgsLength, RestService restService, MessageService messageService) {
        super(command, minArgsLength, restService, messageService);
    }

    protected void logUsage(String userId) {
        log.info("User {} execute command {}", userId, getCommand());
    }
}
