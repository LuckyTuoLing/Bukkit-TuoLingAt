package com.tuoling.tuolingat.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// 指令冷却缓存：保存玩家下次可用艾特的时间戳（ms）
public final class CooldownCache {

    private static final Map<UUID, Long> cooldown =  new ConcurrentHashMap<>(); //多线程锁机制,处理小概率并发

    private CooldownCache() {
    }

    // 记录玩家下次可用时间戳（ms）
    public static void set(UUID uuid, long nextAvailable) {
        cooldown.put(uuid, nextAvailable);
    }

    // 返回剩余冷却秒数；无冷却或已过期返回 0，并清理该记录
    public static long remain(UUID uuid) {
        Long next = cooldown.get(uuid);
        if (next == null) {
            return 0L;
        }
        long remain = (next - System.currentTimeMillis()) / 1000L;
        if (remain <= 0) {
            cooldown.remove(uuid);
            return 0L;
        }
        return remain;
    }

    public static void clear() {
        cooldown.clear();
    }
}