package com.tuoling.tuolingat.compat.title;

import com.tuoling.tuolingat.compat.model.TitleModel;
import org.bukkit.entity.Player;

public class MidTitleCompat implements TitleCompat {

    @Override
    public void sendTitle(Player player, TitleModel model) {
        // 1.11 只有 2 参数版，忽略 fadeIn/stay/fadeOut
        player.sendTitle(model.mainTitle, model.subTitle);
    }

    @Override
    public boolean isMatch() {
        // 存在 5 参 就算有 2 参也交给 Now → 用 Mid 需要：有 2 参但没有 5 参
        try {
            Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
            // 有 5 参：不归 Mid
            return false;
        } catch (NoSuchMethodException e5) {
            try {
                Player.class.getMethod("sendTitle", String.class, String.class);
                // 有 2 参、无 5 参：1.8 ~ 1.10
                return true;
            } catch (NoSuchMethodException e2) {
                return false;
            }
        }
    }
}