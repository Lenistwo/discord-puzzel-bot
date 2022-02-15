package pl.lenistwo.puzzlebot.command.admin;

import net.dv8tion.jda.api.entities.Member;
import pl.lenistwo.puzzlebot.command.api.AdminCommand;
import pl.lenistwo.puzzlebot.rest.RestService;
import pl.lenistwo.puzzlebot.service.message.MessageService;
import pl.lenistwo.puzzlebot.util.BotConstants;

import static pl.lenistwo.puzzlebot.util.MessageBuilder.buildErrorMessage;
import static pl.lenistwo.puzzlebot.util.MessageBuilder.buildTakePointsMessage;

public class TakePlayerPointsCommand extends AdminCommand {

    public TakePlayerPointsCommand(RestService restService, MessageService messageService) {
        super(BotConstants.COMMAND_PREFIX + "wezpunkty", 1, restService, messageService);
    }

    @Override
    public void execute(String[] args, String channelId, Member member) {
        super.logUsage(member.getId());
        if (args.length > 1) {
            var playerName = args[0];
            var amount = getPointAmount(args[1], channelId);
            var pointsAmount = amount > 0 ? Math.negateExact(amount) : amount;
            updatePlayerPoints(playerName, pointsAmount);
            messageService.sendMessage(channelId, buildTakePointsMessage(playerName, pointsAmount));
            return;
        }

        var amount = getPointAmount(args[0], channelId);
        var pointsAmount = amount > 0 ? Math.negateExact(amount) : amount;
        updatePlayerPoints(member.getEffectiveName(), pointsAmount);
        messageService.sendMessage(channelId, buildTakePointsMessage(member.getEffectiveName(), pointsAmount));
    }

    private void updatePlayerPoints(String playerName, int pointsAmount) {
        restService.updatePlayerPoints(playerName, pointsAmount);
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
