package org.gputest.proxy_command;

import org.bukkit.plugin.java.JavaPlugin;

// ServerCommand 클래스가 이 패키지에 있다고 가정합니다. (아니라면 패키지명을 수정하세요)
import org.gputest.proxy_command.ServerCommand;

public class Proxy_command extends JavaPlugin { // (1) 클래스 이름

    @Override
    public void onEnable() {
        // 프록시 서버(Velocity/BungeeCord)와의 통신 채널 등록
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // '/server' 명령어 등록
        // 🚨 이 부분을 ServerCommand 인스턴스를 사용하도록 수정해야 합니다!
        try {
            getCommand("server").setExecutor(new ServerCommand(this)); // 👈 이 부분이 중요합니다.
        } catch (NullPointerException e) {
            getLogger().severe("ERROR: 'server' command not registered in plugin.yml!");
        }

        // 로그 메시지도 현재 플러그인 이름에 맞게 수정
        getLogger().info("Proxy_command is enabled!");
    }

    @Override
    public void onDisable() {
        // 통신 채널 등록 해제
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");

        // 로그 메시지 수정
        getLogger().info("Proxy_command is disabled!");
    }
}