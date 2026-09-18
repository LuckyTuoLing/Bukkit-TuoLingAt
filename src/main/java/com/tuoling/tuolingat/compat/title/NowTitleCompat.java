package com.tuoling.tuolingat.compat.title;

import com.tuoling.tuolingat.compat.model.TitleModel;
import org.bukkit.entity.Player;

public class NowTitleCompat implements TitleCompat {

    @Override
    public void sendTitle(Player player, TitleModel model) {
        player.sendTitle(
                model.mainTitle,
                model.subTitle,
                model.fadeIn,
                model.stay,
                model.fadeOut
        );
    }
    @Override
    public boolean isMatch() {
        try {
            // 1.12+ ：5 参数
            Player.class.getMethod("sendTitle", String.class, String.class,
                    int.class, int.class, int.class);
            return true;
        } catch (NoSuchMethodException e1) {
            return false;
        }
    }
}