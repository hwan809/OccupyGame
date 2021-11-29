package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Administrator extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {

        if (!isPlayerMember(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않습니다.");
            return true;
        }

        MinecraftTeam playerTeam = getPlayerTeam(player);

        if (!playerTeam.getAdmin().equals(player)) {
            teamManager.logMessage(player, ChatColor.RED + "팀의 대표만 관리자를 설정할 수 있습니다.");
            return true;
        }

        if (args.length < 3) {
            teamManager.logMessage(player, ChatColor.RED + "/팀 관리자 [추가|삭제] [플레이어]");
            return true;
        }

        if (!args[1].equals("추가") && !args[1].equals("삭제")) {
            teamManager.logMessage(player, ChatColor.RED + "관리자 [추가|삭제]");
            return true;
        }

        Player viceAdminPlayer = Bukkit.getPlayer(args[2]);

        if (viceAdminPlayer == null) {
            teamManager.logMessage(player, ChatColor.RED + "그런 이름의 [플레이어] 가 없습니다.");
            return true;
        }

        if (isPlayerMember(viceAdminPlayer)) {
            if (!getPlayerTeam(player).equals(playerTeam)) {
                teamManager.logMessage(player, ChatColor.RED + "다른 팀에 소속되어 있는 [플레이어] 입니다.");
                return true;
            }
        } else {
            teamManager.logMessage(player, ChatColor.RED + "팀에 소속되어 있지 않은 [플레이어] 입니다.");
            return true;
        }

        if (args[1].equals("추가")) {
            if (playerTeam.getAdmin().equals(viceAdminPlayer) || playerTeam.getViceAdmins().contains(viceAdminPlayer)) {
                teamManager.logMessage(player, ChatColor.RED + "이미 이 팀의 관리자인 [플레이어] 입니다.");
                return true;
            }

            playerTeam.addViceAdmin(viceAdminPlayer);

            teamManager.logMessage(viceAdminPlayer,"[ " + playerTeam.getTeamName() + " ]" + ChatColor.GREEN + " 팀의 부마스터가 되었습니다!");
            teamManager.logMessage(player,"[ " + playerTeam.getTeamName() + " ]" + ChatColor.GREEN + " 팀의 부마스터를 추가했습니다!");

            return true;
        } else if (args[1].equals("삭제")) {
            if (playerTeam.getTeamMembers().contains(viceAdminPlayer)) {
                teamManager.logMessage(player, ChatColor.RED + "이 [플레이어] 는 관리자가 아닙니다.");
                return true;
            }

            playerTeam.removeViceAdmin(viceAdminPlayer);

            teamManager.logMessage(viceAdminPlayer,"[ " + playerTeam.getTeamName() + " ]" + ChatColor.RED + " 팀의 부마스터에서 해지되었습니다.");
            teamManager.logMessage(player,"[ " + playerTeam.getTeamName() + " ]" + ChatColor.RED + " 팀의 부마스터를 해지했습니다.");

            return true;
        }

        return false;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
