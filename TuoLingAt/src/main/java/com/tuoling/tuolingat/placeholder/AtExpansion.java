package com.tuoling.tuolingat.placeholder;

import com.tuoling.tuolingat.TuoLingAt;
import com.tuoling.tuolingat.cache.CooldownCache;
import com.tuoling.tuolingat.cache.PlayerCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

// 本插件对外提供的 PlaceholderAPI 占位符（软依赖，仅装上 PAPI 时注册）：
// %tuolingat_online% 在线玩家数、%tuolingat_player% 当前玩家名、%tuolingat_cooldown% 剩余艾特冷却秒数
// 需要「谁 at 了谁」的信息请监听 api.event.AtEvent
public class AtExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "tuolingat";
    }

    @Override
    public String getAuthor() {
        return "TuoLing";
    }

    @Override
    public String getVersion() {
        return TuoLingAt.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params == null) {
            return null;
        }
        switch (params) {
            case "online":
                return String.valueOf(PlayerCache.getAllPlayers().size());
            case "player":
                return player == null ? "" : player.getName();
            case "cooldown":
                return player == null ? "0" : String.valueOf(CooldownCache.remain(player.getUniqueId()));
            default:
                return null;
        }
    }
}