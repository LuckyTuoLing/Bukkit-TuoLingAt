package com.tuoling.tuolingat.listener;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

// 监听器管理：常驻监听直接注册，可开关的监听按配置决定注册 / 取消注册
public class ListenerManager {

    private final JavaPlugin plugin;

    // 聊天框艾特监听实例，未启用时为 null
    @Getter
    private ChatAtListener chatAtListener;

    public ListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // 注册常驻监听（玩家进出缓存，/at 依赖它），并按配置决定聊天框艾特监听
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
        applyChatAt();
    }

    // 按 Setting.EnableChatAt 同步聊天框艾特监听：开启则注册、关闭则取消注册
    // 启动时与 /at reload 后各调一次
    public void applyChatAt() {
        boolean enable = plugin.getConfig().getBoolean("Setting.EnableChatAt", true);
        if (enable && chatAtListener == null) {
            chatAtListener = new ChatAtListener();
            plugin.getServer().getPluginManager().registerEvents(chatAtListener, plugin);
        } else if (!enable && chatAtListener != null) {
            HandlerList.unregisterAll(chatAtListener);
            chatAtListener = null;
        }
    }
}