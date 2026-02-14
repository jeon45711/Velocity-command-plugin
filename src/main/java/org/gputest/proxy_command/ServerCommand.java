package org.gputest.proxy_command;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor {

    private final Proxy_command plugin;

    public ServerCommand(Proxy_command plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Allow this command to be executed via Command Blocks or Console.
        // Since a player context is required, it must be used as 'execute as @a run server <servername>' in Command Blocks.

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /server <servername>");
            return true;
        }

        String targetServer = args[0];

        // **Important:** Even when executed from a Command Block or Console, 
        // at least one online player is required to send a message through the BungeeCord channel.
        // Therefore, you should use 'execute as @a' in Command Blocks or specify a player context in Console.

        if (!(sender instanceof Player)) {
            // Case: Executed from Console/Command Block.
            // The proxy cannot move "everyone" through this message alone.
            // It is STRONGLY RECOMMENDED to use 'execute as <player> run server <servername>'.
            
            if (plugin.getServer().getOnlinePlayers().isEmpty()) {
                sender.sendMessage(ChatColor.RED + "No players online. The /server command requires a player context to process.");
                return true;
            }

            // If executed from Console, use an arbitrary online player to forward the message to the proxy.
            Player proxySender = plugin.getServer().getOnlinePlayers().iterator().next();
            sendPlayerToServer(proxySender, targetServer);

            sender.sendMessage(ChatColor.GREEN + "Sent transfer request to Velocity using online player context: " + targetServer);
            return true;
        }

        Player player = (Player) sender;
        sendPlayerToServer(player, targetServer);

        // Notify the player that the request is being processed (actual movement is handled by the Proxy)
        player.sendMessage(ChatColor.GREEN + "Connecting to server " + ChatColor.YELLOW + targetServer + ChatColor.GREEN + "...");

        return true;
    }

    /**
     * Core method to request player transfer via Velocity (BungeeCord) messaging channel.
     */
    private void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect"); // Sub-channel: Connect player to a different server
        out.writeUTF(serverName); // Target server name (must match the name in Velocity/Bungee config)

        // Send the plugin message to the proxy via the 'BungeeCord' channel
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
