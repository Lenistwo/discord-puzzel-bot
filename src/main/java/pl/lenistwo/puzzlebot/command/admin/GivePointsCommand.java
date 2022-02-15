package pl.lenistwo.puzzlebot.command.admin;

import net.dv8tion.jda.api.entities.Member;
import pl.lenistwo.puzzlebot.command.api.AdminCommand;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;

import static pl.lenistwo.puzzlebot.util.BotConstants.COMMAND_PREFIX;
import static pl.lenistwo.puzzlebot.util.MessageBuilder.buildAddPointsMessage;
import static pl.lenistwo.puzzlebot.util.MessageBuilder.buildErrorMessage;

public class GivePointsCommand extends AdminCommand {

    public GivePointsCommand(RestService restService, MessageService messageService) {
        super(COMMAND_PREFIX + "dajpunkty", 1, restService, messageService);
    }

    @Override
    public void execute(String[] args, String channelId, Member member) {
        super.logUsage(member.getId());

        if (args.length > 1) {
            var playerName = args[0];
            var pointsAmount = getPointAmount(args[1], channelId);
            givePoints(playerName, pointsAmount);
            messageService.sendMessage(channelId, buildAddPointsMessage(playerName, pointsAmount));
            return;
        }

        var pointsAmount = getPointAmount(args[0], channelId);
        givePoints(member.getEffectiveName(), pointsAmount);
        messageService.sendMessage(channelId, buildAddPointsMessage(member.getEffectiveName(), pointsAmount));
    }

    private void givePoints(String playerName, int amount) {
        restService.updatePlayerPoints(playerName, amount);
    }

    private int getPointAmount(String points, String channelId) {
        try {
            return Integer.parseInt(points);
        } catch (NumberFormatException e) {
            messageService.sendMessage(channelId, buildErrorMessage("Invalid Point Amount"));
            throw e;
        }
    }
}
