package pl.lenistwo.puzzlebot.command.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;

@RequiredArgsConstructor
public abstract class Command {

    @Getter
    private final String command;
    @Getter
    private final int minArgsLength;
    protected final RestService restService;
    protected final MessageService messageService;

    public abstract void execute(String[] args, String channelId, Member member);
}
