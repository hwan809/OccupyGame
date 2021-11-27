package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Message extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 2) return false;

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++){
            sb.append(args[i]).append(" ");
        }
        String message = sb.toString().trim();

        if (!isPlayerMember(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않습니다.");
            return true;
        }

        MinecraftTeam playerTeam = getPlayerTeam(player);
        playerTeam.sendMessage(player, message);

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
