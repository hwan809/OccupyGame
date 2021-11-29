package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Accept extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            teamManager.logMessage(player, ChatColor.RED + "[ 팀 초대 오류 ]");
            return true;
        }

        String teamId = args[1].split(":")[0];
        String inviteCode = args[1].split(":")[1];

        if (!containsTeamId(teamId)) {
            teamManager.logMessage(player, ChatColor.RED + "잘못된 초대입니다. 팀이 삭제 / 수정된 것 같습니다.");
            return true;
        }

        MinecraftTeam nowTeam = getTeamById(teamId);

        if (!nowTeam.getInviteCode().contains(inviteCode)) {
            teamManager.logMessage(player, ChatColor.RED + "만료된 초대입니다.");
            return true;
        }

        nowTeam.addMember(player);

        teamManager.logMessage(player,"[ " + nowTeam.getTeamName() + " ]" + ChatColor.GREEN + " 팀에 가입했습니다!");
        teamManager.logMessage(nowTeam.getAdmin().getPlayer(), "[ " + nowTeam.getTeamName() + " ]" + ChatColor.GREEN + " 팀에 " +
                ChatColor.WHITE + "[ " + player.getName() + " ]" + ChatColor.GREEN + " 님이 가입했습니다!");

        nowTeam.removeInviteCode(inviteCode);
        nowTeam.removeInviteCode(inviteCode);

        return true;

    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
