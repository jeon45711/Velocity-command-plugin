package org.gputest.proxy_command;

import org.bukkit.plugin.java.JavaPlugin;
import org.gputest.proxy_command.ServerCommand;

public class Proxy_command extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the outgoing plugin channel for proxy communication (Velocity/BungeeCord)
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Register the '/server' command
        try {
            getCommand("server").setExecutor(new ServerCommand(this));
        } catch (NullPointerException e) {
            getLogger().severe("ERROR: 'server' command not registered in plugin.yml!");
        }

        getLogger().info("Proxy_command is enabled!");
    }

    @Override
    public void onDisable() {
        // Unregister the plugin channel
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");

        getLogger().info("Proxy_command is disabled!");
    }
}
