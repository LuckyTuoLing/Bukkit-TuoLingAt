package com.tuoling.tuolingat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// at random：随机艾特一个在线玩家（需要 tuolingat.atrandom 权限；控制台默认放行）
public class AtRandomCommand implements CommandHandler {

    @Override
    public boolean isMatch(Command command, CommandSender sender, String[] args) {
        return new CommandUtil(command, sender, args).checkFirstArg("random", "at", "hanat");
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        if (!sender.hasPermission("tuolingat.atrandom")) {
            AtExecUtil.noPerm(sender);
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!AtExecUtil.checkCooldown(player)) {
                return;
            }
            Player target = AtExecUtil.randomOne(player);
            if (target == null) {
                AtExecUtil.notOnline(player);
                return;
            }
            AtExecUtil.at(player, target);
        } else {
            // 控制台：随机艾特任意一个在线玩家，%sender% = Console
            Player target = AtExecUtil.randomAny();
            if (target == null) {
                AtExecUtil.notOnline(sender);
                return;
            }
            AtExecUtil.atFromConsole(target);
        }
    }
}