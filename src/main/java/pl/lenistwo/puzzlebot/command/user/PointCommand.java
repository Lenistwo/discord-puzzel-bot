package pl.lenistwo.puzzlebot.command.user;

import net.dv8tion.jda.api.entities.Member;
import pl.lenistwo.puzzlebot.command.api.Command;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;

import static pl.lenistwo.puzzlebot.util.BotConstants.COMMAND_PREFIX;
import static pl.lenistwo.puzzlebot.util.MessageBuilder.buildPointsMessage;

public class PointCommand extends Command {

    public PointCommand(RestService restService, MessageService messageService) {
        super(COMMAND_PREFIX + "punkty", 0, restService, messageService);
    }

    @Override
    public void execute(String[] args, String channelId, Member member) {
        var playerPoints = restService.getPlayerPoints(member.getEffectiveName());
        messageService.sendMessage(channelId, buildPointsMessage(member.getEffectiveName(), playerPoints.points()));
    }
}
