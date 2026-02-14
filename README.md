# Velocity-command-plugin 🚀

**This plugin fixes an issue where the Velocity proxy commands (e.g., `/server`) would not execute in Minecraft command blocks.**

## 📌 Overview
In a typical Minecraft proxy setup (Velocity or BungeeCord), proxy-level commands are designed for players to type directly. This makes it impossible for **Command Blocks** or the **Server Console** to trigger server-to-server teleportation. 

This plugin solves this by "wrapping" the command at the Spigot/Paper level, allowing automation via redstone and command blocks.

## ✨ Key Features
- **Command Block Support**: Enable `/server` command execution through redstone automation.
- **Console Compatibility**: Run proxy commands directly from the server console.
- **Permission Management**: Secure access using standard Bukkit permissions.

## 🛠 Installation
1. Download the `.jar` file from the [Releases](https://github.com/jeon45711/Velocity-command-plugin/releases) page.
2. Upload the plugin to your Spigot/Paper server's `plugins` folder.
3. Restart your server.
4. Ensure your proxy (Velocity) is configured to allow commands from sub-servers if necessary.

## 💻 Commands & Permissions
| Command | Description | Default Permission |
| :--- | :--- | :--- |
| `/server <name>` | Moves the player to a specific sub-server. | `commandblockservermover.server` (OP) |

## 📂 Configuration (plugin.yml)
The plugin comes pre-configured with the following permissions:
```yaml
permissions:
  commandblockservermover.server:
    description: Allows Command Blocks or Console to use the /server command.
    default: op

## 📄 License
This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.
