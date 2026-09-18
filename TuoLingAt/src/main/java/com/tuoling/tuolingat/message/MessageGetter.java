package com.tuoling.tuolingat.message;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

// 消息解析器：只负责按模式把配置消息解析成最终文本（get），发送交给 MessageSender
@Getter
public class MessageGetter {

    private final JavaPlugin plugin;

    public MessageGetter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // 按 mode 解析 path 处的配置消息：替换自定义占位符 %key%、转颜色、可选解析 PAPI
    // 例：getMessage(ParseMode.CUSTOM, player, "Message.Sender.AtOther", "target", "Steve")
    public String getMessage(ParseMode mode, CommandSender sender, String path, String... placeholders) {
        return format(mode, sender, plugin.getConfig().getString(path, ""), toMap(placeholders));
    }

    private String format(ParseMode mode, CommandSender sender, String text, Map<String, String> placeholders) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // 自定义占位符 %key%（仅 enableCustom 模式）
        if (mode.enableCustom && placeholders != null) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                text = text.replace("%" + e.getKey() + "%", e.getValue());
            }
        }

        text = ChatColor.translateAlternateColorCodes('&', text);

        // PAPI（软依赖，仅 enablePapi 模式且 sender 为玩家）
        if (mode.enablePapi
                && sender instanceof Player
                && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            text = PlaceholderAPI.setPlaceholders((Player) sender, text);
        }

        return text;
    }

    private Map<String, String> toMap(String... arr) {
        if (arr == null || arr.length < 2) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i + 1 < arr.length; i += 2) {
            map.put(arr[i], arr[i + 1]);
        }
        return map;
    }
}