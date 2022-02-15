package pl.lenistwo.puzzlebot.event;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.lenistwo.puzzlebot.command.CommandExecutor;
import pl.lenistwo.puzzlebot.command.admin.GivePointsCommand;
import pl.lenistwo.puzzlebot.command.admin.TakePlayerPointsCommand;
import pl.lenistwo.puzzlebot.command.user.GuessAnswerCommand;
import pl.lenistwo.puzzlebot.command.user.GuessLetterCommand;
import pl.lenistwo.puzzlebot.command.user.PointCommand;
import pl.lenistwo.puzzlebot.listener.BotReadyListener;
import pl.lenistwo.puzzlebot.listener.ButtonClickListener;
import pl.lenistwo.puzzlebot.listener.MessageReceivedListener;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;
import pl.lenistwo.puzzlebot.service.puzzel.PuzzelQueueService;

@Component
@RequiredArgsConstructor
public class StartupEvent implements CommandLineRunner {

    private final JDA jda;
    private final RestService restService;
    private final MessageService messageService;
    private final BotReadyListener botReadyListener;
    private final PuzzelQueueService puzzelQueueService;
    private final ButtonClickListener buttonClickListener;
    private final MessageReceivedListener messageReceivedListener;

    @Override
    public void run(String... args) {
        registerCommands();
        jda.addEventListener(botReadyListener, buttonClickListener, messageReceivedListener);
    }

    private void registerCommands() {
        CommandExecutor.registerCommands(new PointCommand(restService, messageService),
                                         new GivePointsCommand(restService, messageService),
                                         new TakePlayerPointsCommand(restService, messageService),
                                         new GuessLetterCommand(restService, messageService, puzzelQueueService),
                                         new GuessAnswerCommand(restService, messageService, puzzelQueueService));
    }

}
