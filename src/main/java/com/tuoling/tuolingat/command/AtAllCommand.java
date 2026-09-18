package com.tuoling.tuolingat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

// at all：艾特所有在线玩家（需要 tuolingat.atall 权限；控制台默认放行）
public class AtAllCommand implements CommandHandler {

    @Override
    public boolean isMatch(Command command, CommandSender sender, String[] args) {
        return new CommandUtil(command, sender, args).checkFirstArg("all", "at", "hanat");
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        if (!sender.hasPermission("tuolingat.atall")) {
            AtExecUtil.noPerm(sender);
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!AtExecUtil.checkCooldown(player)) {
                return;
            }
            List<Player> others = AtExecUtil.othersOnline(player);
            if (others.isEmpty()) {
                AtExecUtil.notOnline(player);
                return;
            }
            for (Player target : others) {
                AtExecUtil.atSilent(player, target);
            }
        } else {
            // 控制台：艾特所有在线玩家，%sender% = Console
            List<Player> all = AtExecUtil.allOnline();
            if (all.isEmpty()) {
                AtExecUtil.notOnline(sender);
                return;
            }
            for (Player target : all) {
                AtExecUtil.atFromConsole(target);
            }
        }
    }
}