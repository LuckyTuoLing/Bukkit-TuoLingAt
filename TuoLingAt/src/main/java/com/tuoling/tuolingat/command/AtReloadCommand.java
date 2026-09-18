package com.tuoling.tuolingat.command;

import com.tuoling.tuolingat.TuoLingAt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

// at reload：重载配置文件（需要 tuolingat.reload 权限；控制台默认放行）
public class AtReloadCommand implements CommandHandler {

    @Override
    public boolean isMatch(Command command, CommandSender sender, String[] args) {
        return new CommandUtil(command, sender, args).checkFirstArg("reload", "at", "hanat");
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        if (!sender.hasPermission("tuolingat.reload")) {
            AtExecUtil.noPerm(sender);
            return;
        }
        TuoLingAt plugin = TuoLingAt.getPlugin();
        plugin.reloadConfig();
        // 配置里的聊天框艾特开关可能被改动，同步监听的注册状态
        plugin.getListenerManager().applyChatAt();
        sender.sendMessage("§a配置已重载!");
    }
}