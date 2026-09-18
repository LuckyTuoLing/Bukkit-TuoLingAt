package com.tuoling.tuolingat.listener;

import com.tuoling.tuolingat.TuoLingAt;
import com.tuoling.tuolingat.cache.PlayerCache;
import com.tuoling.tuolingat.command.AtExecUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 聊天框艾特：@玩家名 变绿并艾特该玩家，@all/@全体玩家 变黄并批量艾特，@random/@随机玩家 变黄并替换成抽到的玩家名（由 Setting.EnableChatAt 决定是否注册本监听）
public class ChatAtListener implements Listener {

    // @ 与名字之间允许有空格（如 "@ Steve"）；英文名优先，中文关键字只能整体匹配，避免吃掉跟在后面的中文
    private static final Pattern AT_PATTERN = Pattern.compile("@[ ]*([A-Za-z0-9_]{1,16}|全体玩家|随机玩家)");

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String msg = e.getMessage();

        List<Player> targets = new ArrayList<>();
        List<Player> randomTargets = new ArrayList<>();
        Set<UUID> done = new HashSet<>();
        Matcher matcher = AT_PATTERN.matcher(msg);
        StringBuffer sb = new StringBuffer();
        boolean matched = false;
        boolean selfHit = false;
        boolean wrongHit = false;
        boolean allHit = false;
        boolean permDenied = false;
        boolean emptyOnline = false;

        while (matcher.find()) {
            String name = matcher.group(1);
            matched = true;

            if (name.equalsIgnoreCase("all") || name.equals("全体玩家")) {
                allHit = true;
                matcher.appendReplacement(sb, Matcher.quoteReplacement("§e@" + name + "§r"));
                continue;
            }

            if (name.equalsIgnoreCase("random") || name.equals("随机玩家")) {
                // @random：立刻抽出在线玩家，把消息里的关键字换成真实玩家名
                if (!player.hasPermission("tuolingat.atrandom")) {
                    permDenied = true;
                    matcher.appendReplacement(sb, Matcher.quoteReplacement("§e@" + name + "§r"));
                } else {
                    Player picked = AtExecUtil.randomOne(player);
                    if (picked == null) {
                        emptyOnline = true;
                        matcher.appendReplacement(sb, Matcher.quoteReplacement("§e@" + name + "§r"));
                    } else {
                        if (done.add(picked.getUniqueId())) {
                            randomTargets.add(picked);
                        }
                        matcher.appendReplacement(sb, Matcher.quoteReplacement("§e@" + picked.getName() + "§r"));
                    }
                }
                continue;
            }

            Player target = PlayerCache.getPlayer(name);
            if (target != null && target.isOnline() && target.getUniqueId().equals(player.getUniqueId())) {
                // @ 自己：提示不能艾特自己
                selfHit = true;
                matcher.appendReplacement(sb, Matcher.quoteReplacement("@" + name));
            } else if (target != null && target.isOnline() && done.add(target.getUniqueId())) {
                // 在线的其他玩家：变绿并触发艾特
                targets.add(target);
                matcher.appendReplacement(sb, Matcher.quoteReplacement("§a@" + name + "§r"));
            } else {
                // 名字不存在或不在线：提示一次
                wrongHit = true;
                matcher.appendReplacement(sb, Matcher.quoteReplacement("@" + name));
            }
        }

        if (!matched) {
            return;
        }
        matcher.appendTail(sb);
        e.setMessage(sb.toString());

        // title/音效/消息必须在主线程发；冷却与指令艾特共用一套
        TuoLingAt plugin = TuoLingAt.getPlugin();
        final boolean notifySelf = selfHit;
        final boolean notifyWrong = wrongHit;
        final boolean notifyAll = allHit;
        final boolean notifyPermDenied = permDenied;
        final boolean notifyEmptyOnline = emptyOnline;
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (notifySelf) {
                plugin.getMessageSender().sendSelfMsg(player, "Message.Sender.CannotAtSelf");
            }
            if (notifyWrong) {
                plugin.getMessageSender().sendSelfMsg(player, "Message.Sender.AtWrong");
            }
            if (notifyPermDenied) {
                AtExecUtil.noPerm(player);
            }
            if (notifyEmptyOnline) {
                AtExecUtil.notOnline(player);
            }
            if (targets.isEmpty() && randomTargets.isEmpty() && !notifyAll) {
                return;
            }

            // 权限与指令 /at all 对齐，避免聊天框绕过权限
            boolean allDone = false;
            if (notifyAll) {
                if (!player.hasPermission("tuolingat.atall")) {
                    AtExecUtil.noPerm(player);
                } else {
                    List<Player> others = AtExecUtil.othersOnline(player);
                    if (others.isEmpty()) {
                        AtExecUtil.notOnline(player);
                    } else {
                        for (Player other : others) {
                            AtExecUtil.atSilent(player, other);
                        }
                        allDone = true;
                    }
                }
            }
            // @all 已覆盖全员，@random 不再重复艾特
            if (!allDone) {
                for (Player picked : randomTargets) {
                    AtExecUtil.at(player, picked);
                }
            }
            // 冷却只作用于普通 @玩家名（@all / @random 不占冷却）
            if (!allDone && !targets.isEmpty() && AtExecUtil.checkCooldown(player)) {
                for (Player target : targets) {
                    AtExecUtil.at(player, target);
                }
            }
        });
    }
}