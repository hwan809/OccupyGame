package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamManager;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamUpgrade extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (!isPlayerMember(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않습니다.");
            return true;
        }

        MinecraftTeam playerTeam = getPlayerTeam(player);

        if (!playerTeam.getAdmin().equals(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀의 대표만 팀 업그레이드를 할 수 있습니다.");
            return true;
        }

        if (!player.getInventory().contains(TeamManager.teamUpgradeItem)) {
            teamManager.logMessage(player, ChatColor.RED + "[팀 업그레이드권] 이 없습니다.");
            return true;
        }

        if (playerTeam.isTeamUpgradeAble()) {
            teamManager.logMessage(player, ChatColor.GREEN + "팀을 " + getTeamTitle(playerTeam.getTeamLevel()) + "으로 업그레이드 했습니다!");
            return true;
        } else {
            teamManager.logMessage(player, ChatColor.RED + "팀 업그레이드 요건을 충족하지 못합니다.");
            return true;
        }
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
