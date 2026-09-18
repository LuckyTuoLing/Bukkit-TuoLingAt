package com.tuoling.tuolingat.utils;

import com.tuoling.tuolingat.TuoLingAt;
import org.bukkit.Bukkit;

// 控制台提示：启动 / 关闭横幅，分隔线与帮助消息共用
public final class ConsoleMessageUtil {

    private static final String SUPPORT_VERSION = "1.8 ~ 26.x";

    private ConsoleMessageUtil() {
    }

    // 横幅：插件名后面跟版本号，如 [TuoLingAt 1.0]
    public static String banner() {
        return "§f§l§m—=—=—=—=-=-=-=-§e[§cTuoLingAt §f"
                + TuoLingAt.getPlugin().getDescription().getVersion()
                + "§e]§f§l§m—=—=—=—=-=-=-=-";
    }

    public static void printEnable() {
        Bukkit.getConsoleSender().sendMessage(banner());
        Bukkit.getConsoleSender().sendMessage("§e◈§b插件启动完成");
        Bukkit.getConsoleSender().sendMessage("§e◈§b支持版本: §f" + SUPPORT_VERSION);
        Bukkit.getConsoleSender().sendMessage("§e◈§b当前服务端版本: §f" + serverVersion());
        Bukkit.getConsoleSender().sendMessage(banner());
    }

    public static void printDisable() {
        Bukkit.getConsoleSender().sendMessage(banner());
        Bukkit.getConsoleSender().sendMessage("§e◈§b插件已关闭, 缓存已清除");
        Bukkit.getConsoleSender().sendMessage(banner());
    }

    // 软依赖加载成功
    public static void printDepend(String name, String version) {
        Bukkit.getConsoleSender().sendMessage("§a成功加载依赖: §e" + name + "§a(§e" + version + "§a)");
    }

    // 软依赖未安装，不影响使用
    public static void printDependFail(String name) {
        Bukkit.getConsoleSender().sendMessage("§c加载依赖失败(没装" + name + "，不影响使用)");
    }

    private static String serverVersion() {
        return Bukkit.getBukkitVersion().split("-")[0];
    }
}
