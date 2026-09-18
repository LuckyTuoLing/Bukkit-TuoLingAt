package com.tuoling.tuolingat.cache;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// 在线玩家缓存：按名字查找（忽略大小写）；只在进出服时写入、其余全是读，故用 CopyOnWriteArrayList 保证异步聊天线程读取安全
public class PlayerCache {

    private static final List<Player> cache = new CopyOnWriteArrayList<>();

    public static Player getPlayer(String name) {
        for (Player player : cache) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    public static void putPlayer(Player player) {
        if (getPlayer(player.getName()) == null) {
            cache.add(player);
        }
    }

    public static void removePlayer(String name) {
        cache.removeIf(player -> player.getName().equalsIgnoreCase(name));
    }

    public static boolean containsPlayer(String name) {
        return getPlayer(name) != null;
    }

    public static List<Player> getAllPlayers() {
        return new ArrayList<>(cache);
    }

    public static void clear() {
        cache.clear();
    }
}