package pl.lenistwo.puzzlebot.listener;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pl.lenistwo.puzzlebot.command.CommandExecutor;
import pl.lenistwo.puzzlebot.service.message.MessageService;

import static pl.lenistwo.puzzlebot.util.BotConstants.COMMAND_PREFIX;

@Component
@RequiredArgsConstructor
public class MessageReceivedListener extends ListenerAdapter {

    private final MessageService messageService;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        var contentRaw = event.getMessage().getContentRaw();

        if (event.getAuthor().isBot()) {
            return;
        }

        if (contentRaw.startsWith(COMMAND_PREFIX)) {
            messageService.deleteMessageById(event.getChannel().getId(), event.getMessageId());
            CommandExecutor.execute(contentRaw, event.getChannel().getId(), event.getMember());
        }
    }
}
