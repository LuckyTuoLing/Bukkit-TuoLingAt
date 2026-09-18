package com.tuoling.tuolingat.compat.title;

import com.tuoling.tuolingat.compat.model.TitleModel;
import org.bukkit.entity.Player;

public interface TitleCompat {
    void sendTitle(Player player, TitleModel model);

    boolean isMatch();
}