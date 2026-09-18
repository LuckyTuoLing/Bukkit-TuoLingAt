package com.tuoling.tuolingat;

import com.tuoling.tuolingat.cache.CooldownCache;
import com.tuoling.tuolingat.cache.PlayerCache;
import com.tuoling.tuolingat.command.CommandManager;
import com.tuoling.tuolingat.command.CommandTrigger;
import com.tuoling.tuolingat.compat.CompatManager;
import com.tuoling.tuolingat.listener.ListenerManager;
import com.tuoling.tuolingat.message.MessageGetter;
import com.tuoling.tuolingat.message.MessageSender;
import com.tuoling.tuolingat.placeholder.AtExpansion;
import com.tuoling.tuolingat.utils.ConsoleMessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public final class TuoLingAt extends JavaPlugin {
    @Getter
    private static TuoLingAt plugin;
    private MessageGetter messageGetter;
    private MessageSender messageSender;
    private CompatManager compatManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        plugin = this;

        registerManagers();
        registerCommands();
        registerListeners();
        registerPlaceholder();

        ConsoleMessageUtil.printEnable();
    }

    private void registerManagers() {
        compatManager = new CompatManager();
        messageGetter = new MessageGetter(this);
        messageSender = new MessageSender(messageGetter, compatManager);
        commandManager = new CommandManager();
    }

    private void registerCommands() {
        CommandTrigger trigger = new CommandTrigger();
        PluginCommand at = getCommand("at");
        PluginCommand hanat = getCommand("hanat");
        if (at != null) {
            at.setExecutor(trigger);
            at.setTabCompleter(trigger);
        }
        if (hanat != null) {
            hanat.setExecutor(trigger);
            hanat.setTabCompleter(trigger);
        }
    }

    private void registerListeners() {
        listenerManager = new ListenerManager(this);
        listenerManager.register();
    }

    private void registerPlaceholder() {
        // 软依赖：装了 PlaceholderAPI 才注册本插件对外占位符
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (papi == null) {
            ConsoleMessageUtil.printDependFail("PlaceholderAPI");
            return;
        }
        new AtExpansion().register();
        ConsoleMessageUtil.printDepend("PlaceholderAPI", papi.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        PlayerCache.clear();
        CooldownCache.clear();

        ConsoleMessageUtil.printDisable();
    }
}
