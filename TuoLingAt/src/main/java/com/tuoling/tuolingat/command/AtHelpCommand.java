package com.tuoling.tuolingat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

// at（无参数）或 at help：显示帮助
public class AtHelpCommand implements CommandHandler {

    @Override
    public boolean isMatch(Command command, CommandSender sender, String[] args) {
        CommandUtil util = new CommandUtil(command, sender, args);
        if (!util.isPluginCommand("at", "hanat")) {
            return false;
        }
        return args.length == 0 || "help".equalsIgnoreCase(args[0]);
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        AtExecUtil.sendHelp(sender);
    }
}