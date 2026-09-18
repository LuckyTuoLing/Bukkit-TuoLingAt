package com.tuoling.tuolingat.listener;

import com.tuoling.tuolingat.cache.PlayerCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// 玩家进出时自动维护 PlayerCache（始终注册，保证 /at 能匹配到在线玩家）
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerCache.putPlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PlayerCache.removePlayer(e.getPlayer().getName());
    }
}