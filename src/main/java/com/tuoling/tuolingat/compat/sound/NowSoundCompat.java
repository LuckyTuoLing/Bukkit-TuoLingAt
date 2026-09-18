package com.tuoling.tuolingat.compat.sound;

import com.tuoling.tuolingat.compat.model.SoundModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class NowSoundCompat implements SoundCompat {

    // 1.13 起音效改为命名空间注册表（Sound 实现 Keyed、带 getKey()），String 重载收的是资源路径而非枚举名
    private final boolean modernName = hasSoundKey();

    @Override
    public boolean isMatch() {
        try {
            // 1.9+ ：字符串音效名
            Player.class.getMethod("playSound", Location.class,
                    String.class, float.class, float.class);
            return true;
        } catch (NoSuchMethodException e) {
            // 1.8 ：Sound 枚举
            return false;
        }
    }

    @Override
    public void sendSound(Player player, SoundModel model) {
        if (model == null || model.name == null) return;
        // 按版本把 config 里的音效名转成该版本认识的格式
        String name = modernName
                ? SoundNameUtil.toResourcePath(model.name)
                : SoundNameUtil.toEnumName(model.name);
        try {
            player.playSound(
                    player.getLocation(),
                    name,
                    model.volume,
                    model.pitch
            );
        } catch (RuntimeException e) {
            Bukkit.getLogger().warning(
                    "TuoLingAt: 音效 '" + model.name + "' 在当前服务器版本无法解析,"
                            + "可能是当前音效不存在,请更换当前服务器支持的音效"
            );
        }
    }

    private static boolean hasSoundKey() {
        try {
            Sound.class.getMethod("getKey");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}