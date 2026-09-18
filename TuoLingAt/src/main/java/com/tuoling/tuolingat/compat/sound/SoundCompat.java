package com.tuoling.tuolingat.compat.sound;

import com.tuoling.tuolingat.compat.model.SoundModel;
import org.bukkit.entity.Player;

public interface SoundCompat {
    boolean isMatch();
    void sendSound(Player player, SoundModel model);
}
