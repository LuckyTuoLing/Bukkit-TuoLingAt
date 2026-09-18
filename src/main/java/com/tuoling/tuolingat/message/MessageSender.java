package com.tuoling.tuolingat.message;

import com.tuoling.tuolingat.compat.CompatManager;
import com.tuoling.tuolingat.compat.model.SoundModel;
import com.tuoling.tuolingat.compat.model.TitleModel;
import lombok.Getter;
import org.bukkit.entity.Player;

// 消息发送器：只负责发送，解析交给 MessageGetter；title / sound 走 CompatManager 选中的兼容实现
// 分两组：sendSelf*（发给自己）与 sendTarget*（发给别人）
@Getter
public class MessageSender {

    private final MessageGetter messageGetter;
    private final CompatManager compat;

    public MessageSender(MessageGetter messageGetter, CompatManager compat) {
        this.messageGetter = messageGetter;
        this.compat = compat;
    }

    public void sendSelfMsg(Player self, String path, String... placeholders) {
        sendTargetMsg(self, self, path, placeholders);
    }

    public void sendSelfTitle(Player self, String path, String... placeholders) {
        sendTargetTitle(self, self, path, placeholders);
    }

    public void sendSelfSound(Player self, String path, String... placeholders) {
        sendTargetSound(self, self, path, placeholders);
    }

    public void sendTargetMsg(Player sender, Player target, String path, String... placeholders) {
        String msg = messageGetter.getMessage(ParseMode.CUSTOM, sender, path, placeholders);
        if (!msg.isEmpty()) {
            target.sendMessage(msg);
        }
    }

    // Title 配置格式同 TitleModel："标题,副标题,fadeIn,stay,fadeOut"
    public void sendTargetTitle(Player sender, Player target, String path, String... placeholders) {
        String raw = messageGetter.getMessage(ParseMode.CUSTOM, sender, path, placeholders);
        if (raw.isEmpty()) {
            return;
        }
        compat.getTitleCompat().sendTitle(target, new TitleModel(raw));
    }

    // 音效配置格式同 SoundModel："音效名,音量,音调"
    public void sendTargetSound(Player sender, Player target, String path, String... placeholders) {
        String raw = messageGetter.getMessage(ParseMode.CUSTOM, sender, path, placeholders);
        if (raw.isEmpty()) {
            return;
        }
        compat.getSoundCompat().sendSound(target, new SoundModel(raw));
    }
}