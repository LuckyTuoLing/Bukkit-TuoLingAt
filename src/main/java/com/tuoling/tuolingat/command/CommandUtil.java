package com.tuoling.tuolingat.command;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@Getter
public class CommandUtil {
    private final Command command;
    private final CommandSender sender;
    private final String[] args;

    public CommandUtil(Command command, CommandSender sender, String[] args) {
        this.command = command;
        this.sender = sender;
        this.args = args;
    }

    public boolean isPluginCommand(String... mainCmdNames) {
        String commandName = command.getName();
        for (String cmdName : mainCmdNames) {
            if (commandName.equalsIgnoreCase(cmdName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkFirstArg(String subCmdName, String... mainCmdNames) {
        if (!isPluginCommand(mainCmdNames)) {
            return false;
        }
        if (args.length == 0) {
            return false;
        }
        return args[0].equalsIgnoreCase(subCmdName);
    }
}

