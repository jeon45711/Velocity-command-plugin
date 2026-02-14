package org.gputest.proxy_command;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.gputest.proxy_command.Proxy_command;
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
        // 이 명령어가 커맨드 블록에서 실행되거나 콘솔에서 실행될 수 있도록 허용합니다.
        // 플레이어 컨텍스트가 필요하므로, 커맨드 블록에서는 'execute as @a run server <서버이름>' 형태로 사용해야 합니다.

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "사용법: /server <서버이름>");
            return true;
        }

        String targetServer = args[0];

        // **중요:** 커맨드 블록/콘솔에서 실행되더라도,
        // BungeeCord 채널을 통해 서버를 이동시키려면 최소 한 명의 플레이어가 필요합니다.
        // 따라서 커맨드 블록에서 'execute as @a'를 사용하거나,
        // 콘솔에서 실행 시 이동시킬 '특정 플레이어'를 지정해야 합니다.

        // 여기서는 CommandSender가 플레이어가 아닐 경우, 콘솔에서 실행되었다고 가정하고
        // 온라인 플레이어 중 아무나 한 명을 선택하여 메시지를 보냅니다.
        // 하지만 **커맨드 블록에서 'execute as @a run server <서버이름>' 형태로 사용하는 것을 강력히 권장**합니다.

        if (!(sender instanceof Player)) {
            // 콘솔/커맨드 블록에서 실행된 경우: 이 경우, 프록시가 모든 플레이어를 이동시키도록 메시지를 보낼 수 없습니다.
            // 따라서 이 명령어를 **반드시 'execute as <플레이어> run server <서버이름>' 형태로 사용해야 합니다.**
            if (plugin.getServer().getOnlinePlayers().isEmpty()) {
                sender.sendMessage(ChatColor.RED + "온라인 플레이어가 없어 명령을 처리할 수 없습니다. /server 명령어는 플레이어 컨텍스트가 필요합니다.");
                return true;
            }

            // 콘솔에서 실행될 때, 메시지를 프록시로 전달하기 위해 임의의 온라인 플레이어 한 명을 사용합니다.
            Player proxySender = plugin.getServer().getOnlinePlayers().iterator().next();
            sendPlayerToServer(proxySender, targetServer);

            sender.sendMessage(ChatColor.GREEN + "온라인 플레이어의 컨텍스트를 사용하여 Velocity에 서버 이동 요청을 보냈습니다: " + targetServer);
            return true;
        }

        Player player = (Player) sender;
        sendPlayerToServer(player, targetServer);

        // 플레이어에게 명령이 처리되었음을 알림 (실제 이동은 Velocity가 처리)
        player.sendMessage(ChatColor.GREEN + "서버 " + ChatColor.YELLOW + targetServer + ChatColor.GREEN + "로 이동 중...");

        return true;
    }

    // Velocity(BungeeCord) 통신 채널을 통해 플레이어 이동을 요청하는 핵심 메소드
    private void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect"); // 서브채널: 플레이어를 다른 서버로 이동
        out.writeUTF(serverName); // 이동할 서버의 이름 (Velocity 설정 파일에 등록된 이름)

        // 통신 채널 'BungeeCord'를 통해 Velocity로 메시지 전송
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}