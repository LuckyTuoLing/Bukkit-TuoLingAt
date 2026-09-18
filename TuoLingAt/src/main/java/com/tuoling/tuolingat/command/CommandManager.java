package com.tuoling.tuolingat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 命令管理器：集中注册本插件的命令处理器，命中第一个 isMatch 的执行
public class CommandManager {

    private final List<CommandHandler> handlers = new ArrayList<>();

    public CommandManager() {
        register(new AtHelpCommand(), new AtReloadCommand(),
                new AtRandomCommand(), new AtAllCommand(), new AtPlayerCommand());
    }

    public void register(CommandHandler... handlers) {
        this.handlers.addAll(Arrays.asList(handlers));
    }

    // 遍历处理器，命中第一个 isMatch 的立即执行；均未命中则兜底发帮助
    public void runCommand(Command command, CommandSender sender, String[] args) {
        for (CommandHandler handler : handlers) {
            if (handler.isMatch(command, sender, args)) {
                handler.execute(command, sender, args);
                return;
            }
        }
        AtExecUtil.sendHelp(sender);
    }
}