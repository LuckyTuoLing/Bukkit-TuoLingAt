package com.tuoling.tuolingat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandHandler {

    boolean isMatch(Command command, CommandSender sender, String[] args);
    void execute(Command command, CommandSender sender, String[] args);
}