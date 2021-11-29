package me.dasuonline.vivace.team.commands.args;

import me.dasuonline.vivace.Main;
import me.dasuonline.vivace.commands.interfaces.CustomExecutor;
import me.dasuonline.vivace.commands.interfaces.Manager;
import me.dasuonline.vivace.team.MinecraftTeam;
import me.dasuonline.vivace.team.manager.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Reject extends TeamUtils implements CustomExecutor {

    private Manager teamManager;

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) { // /팀 생성 x
            teamManager.logMessage(player, ChatColor.RED + "가입할 [팀 이름] 을 입력해주세요.");
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

        teamManager.logMessage(player,"[ " + nowTeam.getTeamName() + " ]" + ChatColor.RED + " 팀의 초대를 거절했습니다!");
        teamManager.logMessage(nowTeam.getAdmin().getPlayer(), "[ " + player.getName() + " ]" + ChatColor.RED + " 님이 초대를 거절했습니다!");

        nowTeam.removeInviteCode(inviteCode);

        return true;
    }

    @Override
    public void init() {
        teamManager = Main.getManager("Team");
    }
}
