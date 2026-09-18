package com.tuoling.tuolingat.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

// 艾特事件：一次艾特真正发出前触发，供其他插件获取「谁 at 了谁」；在主线程同步触发，可直接监听
@Getter
public class AtEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    // 发起者，控制台发起时为 null
    private final Player sender;
    // 发起者名，控制台发起时为 "Console"
    private final String senderName;
    private final Player target;

    public AtEvent(Player sender, String senderName, Player target) {
        this.sender = sender;
        this.senderName = senderName;
        this.target = target;
    }

    public boolean isFromConsole() {
        return sender == null;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}