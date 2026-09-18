package com.tuoling.tuolingat.command;

import com.tuoling.tuolingat.TuoLingAt;
import com.tuoling.tuolingat.cache.PlayerCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

// 命令触发器：Bukkit 命令入口，把命令委托给 CommandManager 分发，并提供 Tab 补全
public final class CommandTrigger implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TuoLingAt.getPlugin().getCommandManager().runCommand(command, sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (Player p : PlayerCache.getAllPlayers()) {
                result.add(p.getName());
            }
        }
        return result;
    }
}