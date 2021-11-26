package me.dasuonline.vivace.team;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamCommands implements CommandExecutor {

    public final static String cmd1 = "팀";
    public static Map<String, Object> argsMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = command.getName();

        if (!name.equals(cmd1)) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "플레이어만 사용할 수 있습니다.");
            return true;
        }

        String args0 = args[0];

        if (TeamManager.argsMap.containsKey(args0)) {
            return TeamManager.argsMap.get(args0).execute((Player) sender, args);
        }

        return false;
    }
}
