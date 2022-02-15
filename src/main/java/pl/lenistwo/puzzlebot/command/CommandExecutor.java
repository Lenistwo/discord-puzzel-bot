package pl.lenistwo.puzzlebot.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import pl.lenistwo.puzzlebot.command.api.AdminCommand;
import pl.lenistwo.puzzlebot.command.api.Command;
import pl.lenistwo.puzzlebot.exception.command.CommandNotAllowedException;
import pl.lenistwo.puzzlebot.exception.command.CommandNotFoundException;
import pl.lenistwo.puzzlebot.exception.command.NoteEnoughArgumentsException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.NONE)
public class CommandExecutor {

    private static final String SPACE = " ";
    private static final Set<Command> registeredCommands = new HashSet<>();

    public static void execute(String command, String channelId, Member member) {

        var cmd = command.indexOf(SPACE) > 0 ? command.substring(0, command.indexOf(SPACE)) : command;

        var foundCommand = registeredCommands.stream().filter(c -> c.getCommand().equalsIgnoreCase(cmd)).findFirst().orElseThrow(() -> new CommandNotFoundException(cmd));

        var split = command.split(SPACE);
        var args = split.length > 0 ? Arrays.copyOfRange(split, 1, split.length) : split;

        if (args.length < foundCommand.getMinArgsLength()) {
            throw new NoteEnoughArgumentsException();
        }

        if (foundCommand instanceof AdminCommand && !member.hasPermission(Permission.ADMINISTRATOR)) {
            throw new CommandNotAllowedException(command, member.getNickname());
        }

        foundCommand.execute(args, channelId, member);
    }

    public static void registerCommands(Command... commands) {
        registeredCommands.addAll(List.of(commands));
    }
}
