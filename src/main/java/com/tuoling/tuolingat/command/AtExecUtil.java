package com.tuoling.tuolingat.command;

import com.tuoling.tuolingat.TuoLingAt;
import com.tuoling.tuolingat.api.event.AtEvent;
import com.tuoling.tuolingat.cache.CooldownCache;
import com.tuoling.tuolingat.cache.PlayerCache;
import com.tuoling.tuolingat.message.MessageSender;
import com.tuoling.tuolingat.utils.ConsoleMessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 艾特命令的共享执行逻辑：OP 校验、冷却、在线筛选、真正的发送分发（支持控制台发起）
public final class AtExecUtil {

    private static final Random random = new Random();

    private AtExecUtil() {
    }

    // 玩家艾特：给对方通知（chatbox/title/音效）+ 给自己反馈（兜底拦截艾特自己）
    public static void at(Player player, Player target) {
        if (player == null || target == null || player.getUniqueId().equals(target.getUniqueId())) {
            return;
        }
        // 先通知对方，避免自身反馈异常时对方收不到
        notify(player, target, player.getName());

        MessageSender ms = TuoLingAt.getPlugin().getMessageSender();
        ms.sendSelfMsg(player, "Message.Sender.AtOther", "target", target.getName());
        ms.sendSelfSound(player, "Message.Sender.Sound");
    }

    // 静默艾特：只通知对方，不给发起者任何反馈（艾特全员时逐条提示会刷屏）
    public static void atSilent(Player player, Player target) {
        if (player == null || target == null || player.getUniqueId().equals(target.getUniqueId())) {
            return;
        }
        notify(player, target, player.getName());
    }

    // 控制台艾特：无自我反馈，%sender% = Console
    public static void atFromConsole(Player target) {
        if (target == null) {
            return;
        }
        notify(null, target, "Console");
    }

    // 给对方发聊天通知 + title + 音效；realSender 为 null 表示控制台发起（不解析 PAPI）
    private static void notify(Player realSender, Player target, String senderName) {
        // 对外抛出艾特事件，供其他插件获取「谁 at 了谁」
        TuoLingAt.getPlugin().getServer().getPluginManager()
                .callEvent(new AtEvent(realSender, senderName, target));

        MessageSender ms = TuoLingAt.getPlugin().getMessageSender();
        ms.sendTargetMsg(realSender, target, "Message.target.ChatBot", "sender", senderName);
        ms.sendTargetTitle(realSender, target, "Message.target.Title", "sender", senderName);
        ms.sendTargetSound(realSender, target, "Message.target.Sound");
    }

    public static List<Player> othersOnline(Player self) {
        List<Player> list = new ArrayList<>();
        for (Player p : PlayerCache.getAllPlayers()) {
            if (p.isOnline() && !p.getUniqueId().equals(self.getUniqueId())) {
                list.add(p);
            }
        }
        return list;
    }

    public static List<Player> allOnline() {
        List<Player> list = new ArrayList<>();
        for (Player p : PlayerCache.getAllPlayers()) {
            if (p.isOnline()) {
                list.add(p);
            }
        }
        return list;
    }

    public static Player randomOne(Player self) {
        List<Player> others = othersOnline(self);
        return others.isEmpty() ? null : others.get(random.nextInt(others.size()));
    }

    public static Player randomAny() {
        List<Player> all = allOnline();
        return all.isEmpty() ? null : all.get(random.nextInt(all.size()));
    }

    // 冷却判断；通过时记录本次冷却（仅玩家调用）
    public static boolean checkCooldown(Player player) {
        int cds = TuoLingAt.getPlugin().getConfig().getInt("Setting.CommandCooldown", 0);
        if (cds <= 0) {
            return true;
        }

        // 配置开启时 OP 无视冷却
        if (player.isOp() && TuoLingAt.getPlugin().getConfig().getBoolean("Setting.OpNoCooldown", true)) {
            return true;
        }

        long remain = CooldownCache.remain(player.getUniqueId());
        if (remain > 0) {
            TuoLingAt.getPlugin().getMessageSender()
                    .sendSelfMsg(player, "Message.Sender.InCooldown", "cooldown", String.valueOf(remain));
            return false;
        }
        CooldownCache.set(player.getUniqueId(), System.currentTimeMillis() + cds * 1000L);
        return true;
    }

    public static void noPerm(CommandSender sender) {
        sender.sendMessage("§c你没有权限使用该指令!");
    }

    // 服务器无其他人在线提示（写死在代码里，不走配置）
    public static void notOnline(CommandSender sender) {
        sender.sendMessage("§f[§e系统§f] §c服务器无其他人在线.");
    }

    public static void sendHelp(CommandSender sender) {
        sender.sendMessage(ConsoleMessageUtil.banner());
        sender.sendMessage("§e◈§b/at [玩家名] §7艾特指定玩家(无需权限)");
        sender.sendMessage("§e◈§b/at all §7艾特所有玩家(权限: tuolingat.atall)");
        sender.sendMessage("§e◈§b/at random §7艾特随机玩家(权限: tuolingat.atrandom)");
        sender.sendMessage("§e◈§b/at reload §7重载[检测玩家聊天]插件(权限: tuolingat.reload)");
        sender.sendMessage(ConsoleMessageUtil.banner());
    }
}