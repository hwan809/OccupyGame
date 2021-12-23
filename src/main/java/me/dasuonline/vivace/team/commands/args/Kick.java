package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Kick extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (!playerHasTeam(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않습니다.");
            return true;
        }

        MinecraftTeam playerTeam = getPlayerTeam(player);

        if (!playerTeam.getAdmin().equals(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀의 대표만 멤버를 추방할 수 있습니다.");
            return true;
        }

        if (args.length < 2) {
            teamManager.logMessage(player, ChatColor.RED + "추방할 [플레이어] 닉네임을 입력해주세요.");
            return true;
        }

        Player removePlayer = Bukkit.getPlayer(args[2]);

        if (removePlayer == null) {
            teamManager.logMessage(player, ChatColor.RED + "그런 이름의 [플레이어] 가 없습니다.");
            return true;
        }

        if (removePlayer.equals(player)) return false;

        if (playerHasTeam(removePlayer)) {
            if (!getPlayerTeam(player).equals(playerTeam)) {
                teamManager.logMessage(player, ChatColor.RED + "다른 팀에 소속되어 있는 [플레이어] 입니다.");
                return true;
            }
        } else {
            teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않은 [플레이어] 입니다.");
            return true;
        }

        playerTeam.removePlayer(removePlayer);

        teamManager.logMessage(removePlayer,"[ " + playerTeam.getTeamName() + " ]" + ChatColor.RED + " 팀에서 추방되셨습니다.");
        teamManager.logMessage(player,"[ " + playerTeam.getTeamName() + " ]" + ChatColor.RED + " 팀의 멤버를 추방했습니다.");

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
