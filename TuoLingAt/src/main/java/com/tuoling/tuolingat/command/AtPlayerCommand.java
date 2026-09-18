package com.tuoling.tuolingat.command;

import com.tuoling.tuolingat.TuoLingAt;
import com.tuoling.tuolingat.cache.PlayerCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// at <玩家名>：服务器与玩家都能用，艾特指定在线玩家
// 命中规则：根命令 + 首个参数不是 random/all（这两个交给对应命令），否则视为玩家名
public class AtPlayerCommand implements CommandHandler {

    @Override
    public boolean isMatch(Command command, CommandSender sender, String[] args) {
        CommandUtil util = new CommandUtil(command, sender, args);
        if (!util.isPluginCommand("at", "hanat")) {
            return false;
        }
        return args.length == 1
                && !"random".equalsIgnoreCase(args[0])
                && !"all".equalsIgnoreCase(args[0])
                && !"reload".equalsIgnoreCase(args[0])
                && !"help".equalsIgnoreCase(args[0]);
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        Player target = PlayerCache.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            // 玩家走配置化提示；控制台给固定提示
            if (sender instanceof Player) {
                TuoLingAt.getPlugin().getMessageSender().sendSelfMsg((Player) sender, "Message.Sender.AtWrong");
            } else {
                sender.sendMessage("§c玩家不存在于服务器中!");
            }
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (target.getUniqueId().equals(player.getUniqueId())) {
                TuoLingAt.getPlugin().getMessageSender().sendSelfMsg(player, "Message.Sender.CannotAtSelf");
                return;
            }
            if (!AtExecUtil.checkCooldown(player)) {
                return;
            }
            AtExecUtil.at(player, target);
        } else {
            // 控制台发起：%sender% = Console，无自我反馈、无冷却
            AtExecUtil.atFromConsole(target);
        }
    }
}