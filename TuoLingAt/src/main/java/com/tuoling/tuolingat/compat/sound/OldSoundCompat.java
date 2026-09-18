package com.tuoling.tuolingat.compat.sound;

import com.tuoling.tuolingat.compat.model.SoundModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class OldSoundCompat implements SoundCompat {

    @Override
    public boolean isMatch() {
        try {
            // 1.9+ ：字符串音效名
            Player.class.getMethod("playSound", Location.class,
                    String.class, float.class, float.class);
            return false;
        } catch (NoSuchMethodException e) {
            // 1.8 ：Sound 枚举
            return true;
        }
    }

    @Override
    public void sendSound(Player player, SoundModel model) {
        Sound sound;
        try {
            sound = Sound.valueOf(SoundNameUtil.toEnumName(model.name));
        } catch (IllegalArgumentException | NullPointerException e) {
            // 1.8 只支持 Sound 枚举名（如 CAT_AMBIENT），不支持 1.9+ 的字符串音效名（如 ENTITY_CAT_AMBIENT）
            Bukkit.getLogger().warning(
                    "TuoLingAt: 音效 '" + model.name + "' 在当前服务器版本(1.8)无法解析,"
                            + "可能是当前音效: 使用了 1.9+ 的字符串名,请更换当前服务器支持的 Sound 枚举音效"
            );
            return;
        }
        player.playSound(player.getLocation(), sound, model.volume, model.pitch);
    }
}